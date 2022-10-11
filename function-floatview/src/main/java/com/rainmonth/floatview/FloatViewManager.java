package com.rainmonth.floatview;

import static android.content.Context.WINDOW_SERVICE;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.rainmonth.utils.PermissionUtils;
import com.rainmonth.utils.ToastUtils;
import com.rainmonth.utils.Utils;
import com.rainmonth.utils.log.LogUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * 悬浮窗管理器
 */
public class FloatViewManager {
    private static final String TAG = "FloatViewManager";

    private static final int FLOAT_VIEW_ID_BASE = 100;
    public static final int FLOAT_VIEW_ID_MAIN_BOTTOM_LEFT = FLOAT_VIEW_ID_BASE + 1;
    public static final int FLOAT_VIEW_ID_MAIN_CENTER = FLOAT_VIEW_ID_BASE + 2;

    private WeakReference<FrameLayout> mContainerRef;
    private WindowManager mManager;
    // 为解决 在未获取到权限首次请求显示悬浮窗时不显示问题（问题原因：show 方法调用的时候，相应的FloatView还未添加导致不能显示）
    private boolean isShowLater = false;        // 辅助参数，是否需要稍后展示（可能show调用的时候，FloatView还没准备好）

    // 用来进行 FloatView 的管理
    private final HashMap<Integer, View> mFloatViewMap = new HashMap<>();
    /**
     * 由于多个悬浮窗的配置可能不同，如果都采用mConfig这个引用，可能会达不到每个悬浮窗分开配置的能力
     */
    private final HashMap<Integer, FloatViewConfig> mConfigMap = new HashMap<>();

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

    /**
     * 获取WindowManager、获取根FrameLayout
     *
     * @param activity    要添加悬浮View的Activity
     * @param floatViewId 悬浮窗id
     * @return FloatViewManager
     */
    public FloatViewManager with(Activity activity, int floatViewId) {
        if (mManager == null) {
            mManager = (WindowManager) Utils.getApp().getSystemService(WINDOW_SERVICE);
        }
        if (mContainerRef != null) {
            if (mContainerRef.get() != getRootView(activity)) { // 不是同一个页面，先移除floatView，再更新引用
                View floatView = getFloatView(floatViewId);
                mContainerRef.get().removeView(floatView);
                mContainerRef = new WeakReference<>(getRootView(activity));
            } else { // 是同一个页面
                // do nothing
            }
        } else {
            mContainerRef = new WeakReference<>(getRootView(activity));
        }
        return this;
    }

    /**
     * 配置悬浮View
     *
     * @param id     悬浮 View id
     * @param config 悬浮 View 配置
     */
    public FloatViewManager config(int id, @NonNull FloatViewConfig config) {
        if (!mConfigMap.containsKey(id)) {
            mConfigMap.put(id, config);
        } else {
            // todo update specify config
//            this.mConfig = config;
        }
        return this;
    }

    public FloatViewManager add(int floatViewId) {
        LogUtils.d(TAG, "add, floatViewId: " + floatViewId);
        FloatViewConfig config = mConfigMap.get(floatViewId);
        if (config == null) {
            throw new IllegalArgumentException("Config should not be null!");
        }

        if (config.isGlobalFloat) {
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
        LogUtils.d(TAG, "remove(), floatViewId: " + floatViewId);
        FloatViewConfig config = checkConfig(floatViewId);
        if (config.isGlobalFloat) {
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
        LogUtils.d(TAG, "removeGlobalFloatView, floatViewId: " + floatViewId);
        if (mManager == null) {
            return;
        }
        if (mFloatViewMap.containsKey(floatViewId)) {
            mManager.removeView(mFloatViewMap.get(floatViewId));
            mFloatViewMap.remove(floatViewId);
//            mConfigMap.remove(floatViewId);
        }
    }

    /**
     * 移除应用内悬浮窗
     *
     * @param floatViewId 悬浮窗id
     */
    private void removeAppFloatView(int floatViewId) {
        LogUtils.d(TAG, "removeAppFloatView, floatViewId: " + floatViewId);
        FrameLayout frameLayout = mContainerRef.get();
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
        LogUtils.d(TAG, "show(), floatViewId: " + floatViewId);
        View floatView = mFloatViewMap.get(floatViewId);
        if (floatView != null && floatView.getVisibility() != View.VISIBLE) {
            floatView.setVisibility(View.VISIBLE);
            isShowLater = false;
            LogUtils.d(TAG, "show()");
        } else {
            isShowLater = true;
            LogUtils.d(TAG, "show(), floatViewId: " + floatViewId + ", should show later!");
        }
    }

    /**
     * 隐藏悬浮窗
     *
     * @param floatViewId 悬浮窗id
     */
    public FloatViewManager hide(int floatViewId) {
        LogUtils.d(TAG, "hide, floatViewId: " + floatViewId);
        View floatView = mFloatViewMap.get(floatViewId);
        if (floatView != null) {
            floatView.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 请求请求在其他应用上层权限
     *
     * @param floatViewId 悬浮窗id
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestDrawOverlays(int floatViewId) {
        LogUtils.d(TAG, "requestDrawOverlays, floatViewId: " + floatViewId);
        if (Settings.canDrawOverlays(Utils.getApp())) {
            LogUtils.d(TAG, "already granted overlays!");
            // 已经有权限，直接添加
            addGlobalFloatView(floatViewId);
        } else {
            // 没有权限，请求权限
            PermissionUtils.requestDrawOverlays(new PermissionUtils.SimpleCallback() {
                @Override
                public void onGranted() {
                    LogUtils.d(TAG, "overlays granted!");
                    handleDrawOverlaysGranted(floatViewId);
                }

                @Override
                public void onDenied() {
                    LogUtils.d(TAG, "overlays denied!");
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
        LogUtils.d(TAG, "handleDrawOverlaysGranted(), floatViewId: " + floatViewId);
        addGlobalFloatView(floatViewId);
        if (isShowLater) {
            LogUtils.d(TAG, "handleDrawOverlaysGranted(), floatViewId: " + floatViewId + " show after permission granted!");
            show(floatViewId);
        }
    }

    /**
     * 添加全局悬浮窗
     */
    private void addGlobalFloatView(int floatViewId) {
        LogUtils.d(TAG, "addGlobalFloatView, floatViewId: " + floatViewId);

        if (mManager == null) {
            LogUtils.d(TAG, "addGlobalFloatView, windowManager is null");
            return;
        }
        LogUtils.i(TAG, "添加之前先移除");
        removeGlobalFloatView(floatViewId);

        View floatView = getFloatView(floatViewId);
        mFloatViewMap.put(floatViewId, floatView);
        WindowManager.LayoutParams params = getGlobalLayoutParams(floatViewId);
        floatView.setLayoutParams(params);
        mManager.addView(floatView, params);
        floatView.setVisibility(View.GONE);
    }

    /**
     * 添加应用内悬浮View
     *
     * @param floatViewId 悬浮View对应的id
     */
    private void addAppFloatView(int floatViewId) {
        LogUtils.d(TAG, "addAppFloatView, floatViewId: " + floatViewId);
        FrameLayout frameLayout = mContainerRef.get();
        if (frameLayout == null) {
            return;
        }
        if (mFloatViewMap.containsKey(floatViewId)) {
            frameLayout.removeView(mFloatViewMap.get(floatViewId));
            mFloatViewMap.remove(floatViewId);
        }
        View floatView = getFloatView(floatViewId);
        mFloatViewMap.put(floatViewId, floatView);
        frameLayout.addView(floatView, getAppLayoutParams(floatViewId));
        floatView.setVisibility(View.GONE);
    }

    /**
     * 开启 ACTION_MANAGE_OVERLAY_PERMISSION 权限失败
     * 提示或者采用兼容模式
     */
    private void handleDrawOverlaysDenied(int floatViewId) {
        LogUtils.d(TAG, "handleDrawOverlaysDenied(), floatViewId: " + floatViewId);
        FloatViewConfig config = checkConfig(floatViewId);
        if (config.autoCompat) {
            addAppFloatView(floatViewId);
            if (isShowLater) {
                LogUtils.d(TAG, "handleDrawOverlaysDenied(), floatViewId: " + floatViewId + " show after permission denied!");
                show(floatViewId);
            }
        } else {
            ToastUtils.showShort("您拒绝了 ACTION_MANAGE_OVERLAY_PERMISSION 权限");
        }
    }

    private View getFloatView(int floatViewId) {
        LogUtils.d(TAG, "getFloatView, floatViewId: " + floatViewId);
        if (mFloatViewMap.containsKey(floatViewId)) {
            return mFloatViewMap.get(floatViewId);
        } else {
            FloatViewConfig config = checkConfig(floatViewId);
            FloatViewContainer container = new FloatViewContainer(Utils.getApp());

            container.bindView(config);
            // 采用 Application Context
            mFloatViewMap.put(floatViewId, container);
            return container;
        }
    }

    //<editor-fold> 默认参数及配置

    /**
     * 获取默认的配置参数
     *
     * @return 默认配置参数
     */
    private FloatViewConfig getDefaultConfig() {
        LogUtils.d(TAG, "getDefaultConfig");
        return new FloatViewConfig();
    }

    /**
     * 获取全局悬浮布局参数
     * {@link #getAppLayoutParams(int)}
     *
     * @return 全局布局参数
     */
    private WindowManager.LayoutParams getGlobalLayoutParams(int floatViewId) {
        LogUtils.d(TAG, "getGlobalLayoutParams()");
        FloatViewConfig config = checkConfig(floatViewId);
        WindowManager.LayoutParams globalParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            globalParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            globalParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        //设置图片格式，效果为背景透明
        globalParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        globalParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        globalParams.gravity = Gravity.START | Gravity.TOP;

        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        mManager.getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        int screenWidth = dm.widthPixels;
        //窗口高度
        int screenHeight = dm.heightPixels;
        //以屏幕左上角为原点，设置x、y初始值，相对于gravity
        globalParams.x = screenWidth;
        globalParams.y = screenHeight;

        //设置悬浮窗口长宽数据
        globalParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        globalParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        LogUtils.d(TAG, "getGlobalLayoutParams()");
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            globalParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//        } else {
//            globalParams.type = WindowManager.LayoutParams.TYPE_PHONE;
//        }

        //设置图片格式，效果为背景透明
//        globalParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
//        globalParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        globalParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        //调整悬浮窗显示的停靠位置为左侧置顶

//        DisplayMetrics dm = new DisplayMetrics();
//        //取得窗口属性
//        mManager.getDefaultDisplay().getMetrics(dm);
//
//        globalParams.width = config.width;
//        globalParams.height = config.height;
//        globalParams.gravity = config.gravity;
        return globalParams;
    }

    private FloatViewConfig checkConfig(int floatViewId) {
        FloatViewConfig config = mConfigMap.get(floatViewId);
        if (config == null) {
            throw new IllegalArgumentException("Config should not be null!");
        }
        return config;
    }

    /**
     * 获取应用内悬浮布局参数
     * {@link #getGlobalLayoutParams(int)}
     *
     * @return 应用内布局参数
     */
    private FrameLayout.LayoutParams getAppLayoutParams(int floatViewId) {
        LogUtils.d(TAG, "getAppLayoutParams()");
        FloatViewConfig config = checkConfig(floatViewId);
        FrameLayout.LayoutParams appParams = new FrameLayout.LayoutParams(config.width, config.height);
        appParams.gravity = config.gravity;
        // todo margin 设置
//        appParams.setMargins();
        return appParams;
    }

    /**
     * 获取Activity的根布局以向其中添加FloatView
     */
    private FrameLayout getRootView(Activity activity) {
        LogUtils.d(TAG, "getRootView()");
        if (activity == null) {
            return null;
        }
        try {
            return activity.getWindow().getDecorView().findViewById(android.R.id.content);
        } catch (Throwable e) {
            LogUtils.printStackTrace(TAG, e);
            return null;
        }
    }

    //</editor-fold>
}
