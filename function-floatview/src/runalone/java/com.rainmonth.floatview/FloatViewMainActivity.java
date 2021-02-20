package com.rainmonth.floatview;

import android.Manifest;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.rainmonth.utils.PermissionUtils;
import com.rainmonth.utils.ToastUtils;

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
        if (isNeedRequestPermission()) {
            requestDrawOverlays();
        } else {
            handleDrawOverlaysGranted();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestAlertWindow() {
        PermissionUtils.permission(Manifest.permission.SYSTEM_ALERT_WINDOW)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        handleAlertPermissionGranted();
                    }

                    @Override
                    public void onDenied() {
                        handleAlertPermissionDenied();
                    }
                }).request();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestDrawOverlays() {
        if (Settings.canDrawOverlays(this)) {
            handleDrawOverlaysGranted();
        } else {
            PermissionUtils.requestDrawOverlays(new PermissionUtils.SimpleCallback() {
                @Override
                public void onGranted() {
                    handleDrawOverlaysGranted();
                }

                @Override
                public void onDenied() {
                    handleDrawOverlaysDenied();
                }
            });
        }
    }

    /**
     * 获取 SYSTEM_ALERT_WINDOW 成功
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void handleAlertPermissionGranted() {
        if (!PermissionUtils.isGrantedDrawOverlays()) {
            requestDrawOverlays();
        } else {
            handleDrawOverlaysGranted();
        }
    }

    /**
     * 获取 SYSTEM_ALERT_WINDOW 失败
     * 提示或者采用兼容模式
     */
    private void handleAlertPermissionDenied() {
        ToastUtils.showShort("您拒绝了 SYSTEM_ALERT_WINDOW 权限");
    }

    /**
     * 开启 ACTION_MANAGE_OVERLAY_PERMISSION 权限成功
     * 正式开始悬浮窗的展示
     */
    private void handleDrawOverlaysGranted() {
        addGlobalFloatView();
    }

    /**
     * 添加全局悬浮窗
     */
    private void addGlobalFloatView() {
        WindowManager windowManager = (WindowManager) getApplication().getSystemService(WINDOW_SERVICE);
        if (windowManager == null) {
            return;
        }
        View floatView = getFloatView();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        layoutParams.format = PixelFormat.RGBA_8888;
        // 不加上这句这个View所在的窗口就会屏蔽所有事件，就变成了流氓应用了
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.x = 300;
        layoutParams.y = 300;

        windowManager.addView(floatView, layoutParams);
    }

    private View getFloatView() {
        TextView button = new Button(this);
        button.setText("我是一个悬浮窗");
        button.setBackgroundColor(Color.BLUE);
        return button;
    }

    /**
     * 开启 ACTION_MANAGE_OVERLAY_PERMISSION 权限失败
     * 提示或者采用兼容模式
     */
    private void handleDrawOverlaysDenied() {
        ToastUtils.showShort("您拒绝了 ACTION_MANAGE_OVERLAY_PERMISSION 权限");
        if (floatViewConfig.autoCompat) {

        }
    }

    /**
     * 添加应用内悬浮窗
     */
    private void addAppFloatView() {

    }

    private boolean isNeedRequestPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
