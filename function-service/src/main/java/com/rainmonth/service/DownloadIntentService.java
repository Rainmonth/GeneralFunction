package com.rainmonth.service;

import android.app.IntentService;
import android.content.Intent;

import com.rainmonth.utils.log.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadIntentService extends IntentService {
    public static final String TAG = DownloadIntentService.class.getSimpleName();

    public DownloadIntentService() {
        super("DownloadIntentService");
        LogUtils.d(TAG, "DownloadIntentService构造函数, Thread: " + Thread.currentThread().getName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i(TAG, "DownloadIntentService -> onCreate, Thread: " + Thread.currentThread().getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i("DemoLog", "DownloadIntentService -> onStartCommand, Thread: " + Thread.currentThread().getName() + " , startId: " + startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        HttpURLConnection conn = null;
        InputStream is = null;
        String blogUrl = intent.getStringExtra("url");
        String blogName = intent.getStringExtra("name");
        try {
            //下载指定的文件
            URL url = new URL(blogUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                //我们在此处得到所下载文章的输入流，可以将其以文件的形式写入到存储卡上面或
                //将其读取出文本显示在App中
                is = conn.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        LogUtils.i(TAG, "DownloadIntentService -> onHandleIntent, Thread: " + Thread.currentThread().getName()
                + ", 《" + blogName + "》下载完成");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i(TAG, "DownloadIntentService -> onDestroy, Thread: " + Thread.currentThread().getName());
    }
}
