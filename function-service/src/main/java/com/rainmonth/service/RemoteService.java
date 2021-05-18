package com.rainmonth.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;

import com.rainmonth.utils.log.LogUtils;

/**
 * 远程Service 可实现跨进程通信
 */
public class RemoteService extends Service {

    public static final String TAG = RemoteService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d(TAG, "RemoteService belong to process with id: " + Process.myPid());
        LogUtils.d(TAG, "onCreate() executed");
        // todo 虽然不出现ANR，但貌似程序页不再往下执行了
//        try {
//            Thread.sleep(30000);
//        } catch (Exception e) {
//            LogUtils.e(TAG, e.getMessage());
//        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    IRemoteAidlInterface.Stub mBinder = new IRemoteAidlInterface.Stub() {
        @Override
        public int plus(int a, int b) throws RemoteException {
            return a + b;
        }

        @Override
        public String toUpperCase(String str) throws RemoteException {
            if (null != str) {
                return str.toUpperCase();
            }
            return null;
        }
    };
}
