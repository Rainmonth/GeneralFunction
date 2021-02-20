package com.rainmonth.floatview;

import android.app.Activity;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class FloatViewManager {
    private Activity mActivity;
    private FloatViewConfig mConfig;

    private WindowManager.LayoutParams mGlobalParams;

    private FloatViewManager with(Activity activity) {
        this.mActivity = activity;
        return this;
    }

    private FloatViewManager config(FloatViewConfig config) {
        this.mConfig = config;
        return this;
    }


    //<editor-fold> 默认参数及配置

    /**
     * 获取默认的配置参数
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
        FrameLayout.LayoutParams appParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        appParams.gravity = Gravity.END | Gravity.BOTTOM;
        // todo margin 设置
//        appParams.setMargins();
        return appParams;
    }

    //</editor-fold>
}
