package com.rainmonth.floatview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.rainmonth.utils.PermissionUtils;
import com.rainmonth.utils.ToastUtils;
import com.rainmonth.utils.Utils;
import com.rainmonth.utils.log.LogUtils;

import java.util.HashMap;

import static android.content.Context.WINDOW_SERVICE;

/**
 * 悬浮窗管理器
 * todo fix 未获取到权限首次请求显示悬浮窗时不显示问题（问题原因：show 方法调用的时候，相应的FloatView还未添加
 * 导致不能显示）
 * todo fix
 */
public class FloatViewManager {
    private static final String TAG = "FloatViewManager";

    private static final int FLOAT_VIEW_ID_BASE = 100;
    public static final int FLOAT_VIEW_ID_MAIN_BOTTOM_LEFT = FLOAT_VIEW_ID_BASE + 1;
    public static final int FLOAT_VIEW_ID_MAIN_CENTER = FLOAT_VIEW_ID_BASE + 2;

    private Activity mActivity;
    private WindowManager mManager;
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

    public FloatViewManager with(Activity activity) {
        this.mActivity = activity;
        mManager = (WindowManager) mActivity.getSystemService(WINDOW_SERVICE);
        return this;
    }

    public FloatViewManager config(FloatViewConfig config) {
        this.mConfig = config;
        return this;
    }

    public FloatViewManager add(int floatViewId) {
        LogUtils.d("FloatView", "add, floatViewId: " + floatViewId);
        if (mConfig == null) {
            mConfig = getDefaultConfig();
        }

        if (mConfig.isGlobalFloat) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestDrawOverlays(floatViewId);
            } else {
                addGlobalFloatView(floatViewId);
            }
        } else {
            addAppFloatView(floatViewId);
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

    /**
     * 移除全局悬浮窗
     *
     * @param floatViewId 悬浮窗id
     */
    private void removeGlobalFloatView(int floatViewId) {
        LogUtils.d("FloatView", "removeGlobalFloatView, floatViewId: " + floatViewId);
        if (mManager == null) {
            return;
        }
        if (mFloatViewMap.containsKey(floatViewId)) {
            mManager.removeView(mFloatViewMap.get(floatViewId));
            mFloatViewMap.remove(floatViewId);
        }
    }

    /**
     * 移除应用内悬浮窗
     *
     * @param floatViewId 悬浮窗id
     */
    private void removeAppFloatView(int floatViewId) {
        LogUtils.d("FloatView", "removeAppFloatView, floatViewId: " + floatViewId);
        FrameLayout frameLayout = getRootView();
        if (frameLayout == null) {
            return;
        }
        if (mFloatViewMap.containsKey(floatViewId)) {
            frameLayout.removeView(mFloatViewMap.get(floatViewId));
            mFloatViewMap.remove(floatViewId);
        }
    }

    /**
     * 显示悬浮窗
     *
     * @param floatViewId 悬浮窗id
     */
    public void show(int floatViewId) {
        LogUtils.d("FloatView", "show, floatViewId: " + floatViewId);
        View floatView = mFloatViewMap.get(floatViewId);
        if (floatView != null && floatView.getVisibility() != View.VISIBLE) {
            floatView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏悬浮窗
     *
     * @param floatViewId 悬浮窗id
     */
    public void hide(int floatViewId) {
        LogUtils.d("FloatView", "hide, floatViewId: " + floatViewId);
        View floatView = mFloatViewMap.get(floatViewId);
        if (floatView != null) {
            floatView.setVisibility(View.GONE);
        }
    }

    /**
     * 请求请求在其他应用上层权限
     *
     * @param floatViewId 悬浮窗id
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestDrawOverlays(int floatViewId) {
        LogUtils.d("FloatView", "requestDrawOverlays, floatViewId: " + floatViewId);
        if (Settings.canDrawOverlays(mActivity)) {
            // 已经有权限，直接添加
            addGlobalFloatView(floatViewId);
        } else {
            // 没有权限，请求权限
            PermissionUtils.requestDrawOverlays(new PermissionUtils.SimpleCallback() {
                @Override
                public void onGranted() {
                    LogUtils.d("FloatView", "overlays granted!");
                    handleDrawOverlaysGranted(floatViewId);
                }

                @Override
                public void onDenied() {
                    LogUtils.d("FloatView", "overlays denied!");
                    handleDrawOverlaysDenied(floatViewId);
                }
            });
        }
    }

    /**
     * 请求 ACTION_MANAGE_OVERLAY_PERMISSION 权限成功
     * 正式开始悬浮窗的展示
     */
    private void handleDrawOverlaysGranted(int floatViewId) {
        LogUtils.d("FloatView", "handleDrawOverlaysGranted, floatViewId: " + floatViewId);
        addGlobalFloatView(floatViewId);

    }

    /**
     * 添加全局悬浮窗
     */
    private void addGlobalFloatView(int floatViewId) {
        LogUtils.d("FloatView", "addGlobalFloatView, floatViewId: " + floatViewId);

        if (mManager == null) {
            LogUtils.d("FloatView", "addGlobalFloatView, windowManager is null");
            return;
        }
        removeGlobalFloatView(floatViewId);

        View floatView = getFloatView(floatViewId);
        mFloatViewMap.put(floatViewId, floatView);
        mManager.addView(floatView, getGlobalLayoutParams());
        floatView.setVisibility(View.GONE);
    }

    /**
     * 添加应用内悬浮View
     *
     * @param floatViewId 悬浮View对应的id
     */
    private void addAppFloatView(int floatViewId) {
        LogUtils.d("FloatView", "addAppFloatView, floatViewId: " + floatViewId);
        FrameLayout frameLayout = getRootView();
        if (frameLayout == null) {
            return;
        }
        if (mFloatViewMap.containsKey(floatViewId)) {
            frameLayout.removeView(mFloatViewMap.get(floatViewId));
            mFloatViewMap.remove(floatViewId);
        }
        View floatView = getFloatView(floatViewId);
        mFloatViewMap.put(floatViewId, floatView);
        frameLayout.addView(floatView, getAppLayoutParams());
        floatView.setVisibility(View.GONE);
    }

    /**
     * 开启 ACTION_MANAGE_OVERLAY_PERMISSION 权限失败
     * 提示或者采用兼容模式
     */
    private void handleDrawOverlaysDenied(int floatViewId) {
        LogUtils.d("FloatView", "handleDrawOverlaysDenied, floatViewId: " + floatViewId);
        if (mConfig.autoCompat) {
            addAppFloatView(floatViewId);
        } else {
            ToastUtils.showShort("您拒绝了 ACTION_MANAGE_OVERLAY_PERMISSION 权限");
        }
    }

    private View getFloatView(int floatViewId) {
        LogUtils.d("FloatView", "getFloatView, floatViewId: " + floatViewId);
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
        LogUtils.d("FloatView", "getDefaultConfig");
        return new FloatViewConfig();
    }

    /**
     * 获取全局悬浮布局参数
     *
     * @return 全局布局参数
     */
    private WindowManager.LayoutParams getGlobalLayoutParams() {
        LogUtils.d(TAG, "getGlobalLayoutParams()");
        WindowManager.LayoutParams globalParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            globalParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            globalParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        globalParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        globalParams.width = mConfig.width;
        globalParams.height = mConfig.height;
        globalParams.gravity = mConfig.gravity;
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
        LogUtils.d(TAG, "getAppLayoutParams()");
        FrameLayout.LayoutParams appParams = new FrameLayout.LayoutParams(mConfig.width, mConfig.height);
        appParams.gravity = mConfig.gravity;
        // todo margin 设置
//        appParams.setMargins();
        return appParams;
    }

    /**
     * 获取Activity的根布局以向其中添加FloatView
     */
    private FrameLayout getRootView() {
        LogUtils.d(TAG, "getRootView()");
        if (mActivity == null) {
            return null;
        }
        try {
            return mActivity.getWindow().getDecorView().findViewById(android.R.id.content);
        } catch (Throwable e) {
            LogUtils.printStackTrace(TAG, e);
            return null;
        }
    }

    //</editor-fold>
}
