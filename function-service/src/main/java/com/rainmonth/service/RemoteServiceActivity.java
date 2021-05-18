package com.rainmonth.service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rainmonth.utils.log.LogUtils;


public class RemoteServiceActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = RemoteServiceActivity.class.getSimpleName();

    private IRemoteAidlInterface iRemoteAidlInterface;

    private NormalService.NormalServiceBinder normalServiceBinder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LogUtils.d(TAG, "onServiceConnected() executed");
            iRemoteAidlInterface = IRemoteAidlInterface.Stub.asInterface(iBinder);
            try {
                int result = iRemoteAidlInterface.plus(3, 5);
                String upperStr = iRemoteAidlInterface.toUpperCase("hello world");
                LogUtils.d("TAG", "result is " + result);
                LogUtils.d("TAG", "upperStr is " + upperStr);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
//            normalServiceBinder = (NormalService.NormalServiceBinder) iBinder;
//            normalServiceBinder.startDownload();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            LogUtils.d(TAG, "onServiceDisconnected() executed");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_service);

        initViewsAndEvents();

        LogUtils.d(TAG, "RemoteServiceActivity belong to process with id: " + Process.myPid());
    }

    /**
     * 初始化视图和事件
     */
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
                intent = new Intent(this, RemoteService.class);
                startService(intent);
                break;
            case R.id.tv_btn_stop_service:
                LogUtils.d(TAG, "stop service btn clicked");
                intent = new Intent(this, RemoteService.class);
                stopService(intent);
                break;
            case R.id.tv_btn_bind_service:
                intent = new Intent(this, RemoteService.class);
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
