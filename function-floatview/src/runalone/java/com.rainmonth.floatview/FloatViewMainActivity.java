package com.rainmonth.floatview;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rainmonth.utils.log.LogUtils;

public class FloatViewMainActivity extends AppCompatActivity {

    FloatViewConfig floatViewConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floatview_activity_float_view_main);

        floatViewConfig = new FloatViewConfig();

        TextView tvAdd1 = findViewById(R.id.tv_add1);

        TextView tvAdd2 = findViewById(R.id.tv_add2);

        tvAdd1.setOnClickListener(v -> {
            handleAdd1Click();
        });
        tvAdd2.setOnClickListener(v -> {
            handleAdd2Click();
        });

        TextView tvTest = findViewById(R.id.tv_test);
        tvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FloatViewMainActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });
    }

    private void handleAdd1Click() {
        LogUtils.d("FloatView", "handleAdd1Click");
        FloatViewConfig config = new FloatViewConfig.Builder()
                .setGlobalFloat(true)
                .setAutoCompat(true)
                .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.BOTTOM | Gravity.END)
                .build();
        FloatViewManager.get()
                .with(this, FloatViewManager.FLOAT_VIEW_ID_MAIN_BOTTOM_LEFT)
                .config(config)
                .add(FloatViewManager.FLOAT_VIEW_ID_MAIN_BOTTOM_LEFT)
                .show(FloatViewManager.FLOAT_VIEW_ID_MAIN_BOTTOM_LEFT);
    }

    private void handleAdd2Click() {
        LogUtils.d("FloatView", "handleAdd2Click");
        FloatViewConfig config = new FloatViewConfig.Builder()
                .setGlobalFloat(true)
                .setAutoCompat(true)
                .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.CENTER)
                .build();
        FloatViewManager.get()
                .with(this, FloatViewManager.FLOAT_VIEW_ID_MAIN_CENTER)
                .config(config)
                .add(FloatViewManager.FLOAT_VIEW_ID_MAIN_CENTER)
                .show(FloatViewManager.FLOAT_VIEW_ID_MAIN_CENTER);
    }
}
