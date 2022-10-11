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
        LogUtils.init(true);
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

        TextView tvTestCombine = findViewById(R.id.tv_test_combine);
        tvTestCombine.setOnClickListener(new View.OnClickListener() {
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
                .setId(FloatViewManager.FLOAT_VIEW_ID_MAIN_BOTTOM_LEFT)
                .setGlobalFloat(true)
                .setAutoCompat(true)
                .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                .setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setGravity(Gravity.BOTTOM | Gravity.END)
                .setItemViewResId(R.layout.floatview_default_item_layout)
                .build();
        FloatViewManager.get()
                .with(this, FloatViewManager.FLOAT_VIEW_ID_MAIN_BOTTOM_LEFT)
                .config(FloatViewManager.FLOAT_VIEW_ID_MAIN_BOTTOM_LEFT, config)
                .add(FloatViewManager.FLOAT_VIEW_ID_MAIN_BOTTOM_LEFT)
                .show(FloatViewManager.FLOAT_VIEW_ID_MAIN_BOTTOM_LEFT);
    }

    private void handleAdd2Click() {
        LogUtils.d("FloatView", "handleAdd2Click");
        FloatViewConfig config = new FloatViewConfig.Builder()
                .setId(FloatViewManager.FLOAT_VIEW_ID_MAIN_CENTER)
                .setGlobalFloat(false)
                .setAutoCompat(true)
                .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setItemViewResId(R.layout.floatview_item_layout)
                .setGravity(Gravity.CENTER | Gravity.END)
                .build();
        FloatViewManager.get()
                .with(this, FloatViewManager.FLOAT_VIEW_ID_MAIN_CENTER)
                .config(FloatViewManager.FLOAT_VIEW_ID_MAIN_CENTER, config)
                .add(FloatViewManager.FLOAT_VIEW_ID_MAIN_CENTER)
                .show(FloatViewManager.FLOAT_VIEW_ID_MAIN_CENTER);
    }
}
