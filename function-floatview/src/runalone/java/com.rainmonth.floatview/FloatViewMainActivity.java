package com.rainmonth.floatview;

import android.Manifest;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.rainmonth.utils.PermissionUtils;
import com.rainmonth.utils.ToastUtils;
import com.rainmonth.utils.log.LogUtils;

public class FloatViewMainActivity extends AppCompatActivity {

    FloatViewConfig floatViewConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floatview_activity_float_view_main);

        floatViewConfig = new FloatViewConfig();

        TextView tvAdd = findViewById(R.id.tv_add);

        tvAdd.setOnClickListener(v -> {
            handleAddClick();
        });
    }

    private void handleAddClick() {
        LogUtils.d("FloatView", "handleAddClick");
        FloatViewConfig config = new FloatViewConfig.Builder()
                .setGlobalFloat(true)
                .setAutoCompat(true)
                .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .build();
        FloatViewManager.get()
                .with(this)
                .config(config)
                .add(FloatViewManager.FLOAT_VIEW_ID_MAIN)
                .show(FloatViewManager.FLOAT_VIEW_ID_MAIN);
    }
}
