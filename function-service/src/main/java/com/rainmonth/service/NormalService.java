package com.rainmonth.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.rainmonth.utils.log.LogUtils;

/**
 * Created by RandyZhang on 16/8/12.
 */
public class NormalService extends Service {

    public static final String TAG = NormalService.class.getSimpleName();

    private NormalServiceBinder normalServiceBinder = new NormalServiceBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d(TAG, "NormalService belong to Thread with id: " + Thread.currentThread().getId());
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
        return normalServiceBinder;
    }

    static class NormalServiceBinder extends Binder {

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
