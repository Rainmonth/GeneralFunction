package com.rainmonth.floatview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.rainmonth.utils.PermissionUtils;
import com.rainmonth.utils.ToastUtils;
import com.rainmonth.utils.Utils;

import java.util.HashMap;

import static android.content.Context.WINDOW_SERVICE;

/**
 * 悬浮窗管理器
 */
public class FloatViewManager {
    private Activity mActivity;
    // todo 由于多个悬浮窗的配置可能不同，如果都采用mConfig这个引用，可能会达不到每个悬浮窗分开配置的能力
    private FloatViewConfig mConfig;

    // 用来进行 FloatView 的管理
    private HashMap<Integer, View> mFloatViewMap = new HashMap<>();

    // todo 注意处理内存泄漏问题
    private static volatile FloatViewManager mInstance;

    public static FloatViewManager get() {
        if (mInstance == null) {
            synchronized (FloatViewManager.class) {
                if (mInstance == null) {
                    mInstance = new FloatViewManager();
                }
            }
        }
        return mInstance;
    }

    private FloatViewManager with(Activity activity) {
        this.mActivity = activity;
        return this;
    }

    private FloatViewManager config(FloatViewConfig config) {
        this.mConfig = config;
        return this;
    }

    public FloatViewManager add(int floatViewId) {
        return add(floatViewId, false);
    }

    public FloatViewManager add(int floatViewId, boolean isNeedShow) {
        if (mConfig == null) {
            mConfig = getDefaultConfig();
        }

        if (mConfig.isGlobalFloat) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requestDrawOverlays(floatViewId, isNeedShow);
            } else {
                addGlobalFloatView(floatViewId, isNeedShow);
            }
        } else {
            addAppFloatView(floatViewId, isNeedShow);
        }
        return this;
    }

    public void remove(int floatViewId) {
        if (mConfig.isGlobalFloat) {
            removeGlobalFloatView(floatViewId);
        } else {
            removeAppFloatView(floatViewId);
        }
    }

    private void removeGlobalFloatView(int floatViewId) {
        WindowManager windowManager = (WindowManager) mActivity.getSystemService(WINDOW_SERVICE);
        if (windowManager == null) {
            return;
        }
        if (mFloatViewMap.containsKey(floatViewId)) {
            windowManager.removeView(mFloatViewMap.get(floatViewId));
        }
    }

    private void removeAppFloatView(int floatViewId) {
        FrameLayout frameLayout = getRootView();
        if (frameLayout == null) {
            return;
        }
        if (mFloatViewMap.containsKey(floatViewId)) {
            frameLayout.removeView(mFloatViewMap.get(floatViewId));
        }
    }

    public void show(int floatViewId) {
        View floatView = mFloatViewMap.get(floatViewId);
        if (floatView != null && floatView.getVisibility() != View.VISIBLE) {
            floatView.setVisibility(View.VISIBLE);
        }
    }

    public void hide(int floatViewId) {
        hide(floatViewId, false);
    }

    public void hide(int floatViewId, boolean isNeedRemove) {
        View floatView = mFloatViewMap.get(floatViewId);
        if (floatView != null) {
            floatView.setVisibility(View.GONE);
        }
        if (isNeedRemove) {
            remove(floatViewId);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestDrawOverlays(int floatViewId, boolean isNeedShow) {
        if (Settings.canDrawOverlays(mActivity)) {
            handleDrawOverlaysGranted(floatViewId, isNeedShow);
        } else {
            PermissionUtils.requestDrawOverlays(new PermissionUtils.SimpleCallback() {
                @Override
                public void onGranted() {
                    handleDrawOverlaysGranted(floatViewId, isNeedShow);
                }

                @Override
                public void onDenied() {
                    handleDrawOverlaysDenied(floatViewId, isNeedShow);
                }
            });
        }
    }

    /**
     * 开启 ACTION_MANAGE_OVERLAY_PERMISSION 权限成功
     * 正式开始悬浮窗的展示
     */
    private void handleDrawOverlaysGranted(int floatViewId, boolean isNeedShow) {
        addGlobalFloatView(floatViewId, isNeedShow);
    }

    /**
     * 添加全局悬浮窗
     */
    private void addGlobalFloatView(int floatViewId, boolean isNeedShow) {
        // todo 避免多次获取 WindowManager
        WindowManager windowManager = (WindowManager) mActivity.getSystemService(WINDOW_SERVICE);
        if (windowManager == null) {
            return;
        }
        removeGlobalFloatView(floatViewId);

        View floatView = getFloatView(floatViewId);
        mFloatViewMap.put(floatViewId, floatView);
        windowManager.addView(floatView, getGlobalLayoutParams());
        if (isNeedShow) {
            floatView.setVisibility(View.VISIBLE);
        } else {
            floatView.setVisibility(View.GONE);
        }
    }

    /**
     * 添加应用内悬浮View
     *
     * @param floatViewId 悬浮View对应的id
     */
    private void addAppFloatView(int floatViewId, boolean isNeedShow) {
        FrameLayout frameLayout = getRootView();
        if (frameLayout == null) {
            return;
        }
        if (mFloatViewMap.containsKey(floatViewId)) {
            frameLayout.removeView(mFloatViewMap.get(floatViewId));
        }
        View floatView = getFloatView(floatViewId);
        mFloatViewMap.put(floatViewId, floatView);
        frameLayout.addView(floatView, getAppLayoutParams());
        if (isNeedShow) {
            floatView.setVisibility(View.VISIBLE);
        } else {
            floatView.setVisibility(View.GONE);
        }
    }

    /**
     * 开启 ACTION_MANAGE_OVERLAY_PERMISSION 权限失败
     * 提示或者采用兼容模式
     */
    private void handleDrawOverlaysDenied(int floatViewId, boolean isNeedShow) {
        if (mConfig.autoCompat) {
            addAppFloatView(floatViewId, isNeedShow);
        } else {
            ToastUtils.showShort("您拒绝了 ACTION_MANAGE_OVERLAY_PERMISSION 权限");
        }
    }

    private View getFloatView(int floatViewId) {
        if (mFloatViewMap.containsKey(floatViewId)) {
            return mFloatViewMap.get(floatViewId);
        } else {
            // 采用 Application Context
            TextView button = new Button(Utils.getApp());
            button.setText("我是一个悬浮窗");
            button.setBackgroundColor(Color.BLUE);
            return button;
        }
    }

    //<editor-fold> 默认参数及配置

    /**
     * 获取默认的配置参数
     *
     * @return 默认配置参数
     */
    private FloatViewConfig getDefaultConfig() {
        return new FloatViewConfig();
    }

    /**
     * 获取全局悬浮布局参数
     *
     * @return 全局布局参数
     */
    private WindowManager.LayoutParams getGlobalLayoutParams() {
        WindowManager.LayoutParams globalParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            globalParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            globalParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        globalParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        globalParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        globalParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        globalParams.gravity = Gravity.END | Gravity.BOTTOM;
        // todo margin 设置
//        globalParams.verticalMargin = ;
//        globalParams.horizontalMargin = ;
        return globalParams;
    }

    /**
     * 获取应用内悬浮布局参数
     *
     * @return 应用内布局参数
     */
    private FrameLayout.LayoutParams getAppLayoutParams() {
        FrameLayout.LayoutParams appParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        appParams.gravity = Gravity.END | Gravity.BOTTOM;
        // todo margin 设置
//        appParams.setMargins();
        return appParams;
    }

    /**
     * 获取Activity的根布局以向其中添加FloatView
     */
    private FrameLayout getRootView() {
        if (mActivity == null) {
            return null;
        }
        return mActivity.findViewById(android.R.id.content);
    }

    //</editor-fold>
}
