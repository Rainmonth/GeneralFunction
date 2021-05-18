package com.rainmonth.service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rainmonth.utils.log.LogUtils;

/**
 * Note：
 * 1、调用startService() 和 bindService()后，要销毁service必须同时调用stopService()和unbindService()方法
 * 才可以，点击Stop Service按钮只会让Service停止，点击Unbind Service按钮只会让Service和Activity解除关联，
 * 一个Service必须要在既没有和任何Activity关联又处理停止状态的时候才会被销毁；
 * 2、Service和Thread的关系——没有半毛钱关系
 * Thread，用于开启一个子线程，在里面执行一些耗时操作就不会阻塞主线程的运行；
 * Service，最初的理解是用来处理一些“后台”任务的，就认为一些比较耗时的操作就可以放在这里（后台任务嘛），这就产生了
 * 混淆了；其实Service也是运行在主线程中的
 * 3、后台和和子线程的概念
 * Android 的后台指的是它的运行完全不依赖UI；通常Service中需要另外开启一个子线程来执行那些不需要UI的耗时操作的；
 * 4、既然Service也要创建子线程去执行耗时操作，为何不再最初久在Activity中创建子线程去执行而要多Service这一步呢？
 * 关键在可控制行：Activity可以对Service进行很好的控制，所有的Activity都可以于Service进行关联，并且可以很好的
 * 操作Service中的方法，即使Activity被销毁了，只要重新创建就可以重新建立于Service的连接；而Activity对线程则是
 * 十分无奈的，当Activity被销毁之后，就没有其它方法再去获得之前创建的子线程的示例，而且一个Activity创建的子线程，
 * 另一个Activity是无法对其进行操作的。
 */
public class NormalServiceActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = NormalServiceActivity.class.getSimpleName();

    private NormalService.NormalServiceBinder normalServiceBinder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LogUtils.d(TAG, "onServiceConnected() executed");
            normalServiceBinder = (NormalService.NormalServiceBinder) iBinder;
            normalServiceBinder.startDownload();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            LogUtils.d(TAG, "onServiceDisconnected() executed");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_service);

        initViewsAndEvents();

        LogUtils.d(TAG, "NormalServiceActivity belong to Thread with id: " + Thread.currentThread().getId());
    }

    private void initViewsAndEvents() {
        TextView tvBtnStartService = (TextView) findViewById(R.id.tv_btn_start_service);
        TextView tvBtnStopService = (TextView) findViewById(R.id.tv_btn_stop_service);
        TextView tvBtnBindService = (TextView) findViewById(R.id.tv_btn_bind_service);
        TextView tvBtnUnbindService = (TextView) findViewById(R.id.tv_btn_unbind_service);

        tvBtnStartService.setOnClickListener(this);
        tvBtnStopService.setOnClickListener(this);
        tvBtnBindService.setOnClickListener(this);
        tvBtnUnbindService.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_btn_start_service:
                intent = new Intent(this, NormalService.class);
                startService(intent);
                break;
            case R.id.tv_btn_stop_service:
                LogUtils.d(TAG, "stop service btn clicked");
                intent = new Intent(this, NormalService.class);
                stopService(intent);
                break;
            case R.id.tv_btn_bind_service:
                intent = new Intent(this, NormalService.class);
                bindService(intent, serviceConnection, BIND_AUTO_CREATE);// ICS版本不支持这个Flag
                break;
            case R.id.tv_btn_unbind_service:
                LogUtils.d(TAG, "unbind service btn clicked");
                unbindService(serviceConnection);
                break;

            default:

                break;
        }
    }
}
