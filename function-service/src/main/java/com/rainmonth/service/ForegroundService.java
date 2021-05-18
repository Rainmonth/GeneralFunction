package com.rainmonth.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.rainmonth.utils.log.LogUtils;

public class ForegroundService extends Service {

    public static final String TAG = NormalService.class.getSimpleName();

    private ForegroundServiceBinder foregroundServiceBinder = new ForegroundServiceBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("这是通知的标题")
                .setContentText("这是通知的内容")
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setTicker("通知来了");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 适配Android 8.0 的代码
            mBuilder.setChannelId("notification_id");
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("notification_id", "notification_name", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        // todo 前台Service的关键
        startForeground(1, mBuilder.build());
        LogUtils.d(TAG, "onCreate() executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d(TAG, "onStartCommand() executed");
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 开始执行后台任务
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return foregroundServiceBinder;
    }

    static class ForegroundServiceBinder extends Binder {
        public void startDownload() {
            LogUtils.d(TAG, "startDownload() executed");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 执行具体的下载任务
                }
            }).start();
        }
    }
}
