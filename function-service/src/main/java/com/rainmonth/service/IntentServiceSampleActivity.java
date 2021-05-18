package com.rainmonth.service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IntentServiceSampleActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_sample);

        initViewsAndEvents();
    }

    private void initViewsAndEvents() {
        TextView tvBtnStartFoo = (TextView) findViewById(R.id.tv_btn_start_foo);
        TextView tvBtnStartBaz = (TextView) findViewById(R.id.tv_btn_start_baz);
        TextView tvBtnStartDownload = (TextView) findViewById(R.id.tv_btn_start_download);

        tvBtnStartFoo.setOnClickListener(this);
        tvBtnStartBaz.setOnClickListener(this);
        tvBtnStartDownload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_btn_start_foo:
                SampleIntentService.startActionFoo(this, "Foo", "Foo started!");
                break;
            case R.id.tv_btn_start_baz:
                SampleIntentService.startActionBaz(this, "Baz", "Baz started!");
                break;
            case R.id.tv_btn_start_download:
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    list.add("Android通过startService播放背景音乐;http://www.jb51.net/article/76479.htm");
                }
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    String str = (String) iterator.next();
                    String[] splits = str.split(";");
                    String name = splits[0];
                    String url = splits[1];
                    Intent intent = new Intent(this, DownloadIntentService.class);
                    intent.putExtra("name", name);
                    intent.putExtra("url", url);
                    //启动IntentService
                    startService(intent);
                }
                break;
            default:

                break;
        }
    }
}
