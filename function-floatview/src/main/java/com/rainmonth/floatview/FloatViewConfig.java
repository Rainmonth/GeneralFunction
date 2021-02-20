package com.rainmonth.floatview;

import android.view.Gravity;
import android.view.ViewGroup;

/**
 * 悬浮View配置
 */
public class FloatViewConfig {
    // 是否支持全局悬浮，true 表示即使应用退到后台，也可以悬浮展示
    public boolean isGlobalFloat = false;
    // 是否自适应（即为获取到权限时 自动降级处理）
    public boolean autoCompat = true;
    // 是否自动隐藏
    public boolean autoHide = false;
    // 自动隐藏时间间隔，默认5s
    public int autoHideDelay = 5000;
    // 是否是拖动模式，true 表示可以在窗口内拖动，false 表示固定位置
    public boolean isDragMode = true;
    // 宽
    public int width = ViewGroup.LayoutParams.WRAP_CONTENT;
    // 高
    public int height = ViewGroup.LayoutParams.WRAP_CONTENT;
    // gravity
    public int gravity = Gravity.END | Gravity.BOTTOM;

    public FloatViewConfig() {

    }

    private FloatViewConfig(Builder builder) {
        this.isGlobalFloat = builder.isGlobalFloat;
        this.autoCompat = builder.autoCompat;
        this.autoHide = builder.autoHide;
        this.autoHideDelay = builder.autoHideDelay;
        this.isDragMode = builder.isDragMode;
        this.width = builder.width;
        this.height = builder.height;
        this.gravity = builder.gravity;
    }

    static class Builder {
        // 是否支持全局悬浮，true 表示即使应用退到后台，也可以悬浮展示
        private boolean isGlobalFloat = false;
        // 是否自适应（即为获取到权限时 自动降级处理）
        private boolean autoCompat = true;
        // 是否自动隐藏
        private boolean autoHide = false;
        // 自动隐藏时间间隔，默认5s
        private int autoHideDelay = 5000;
        // 是否是拖动模式，true 表示可以在窗口内拖动，false 表示固定位置
        private boolean isDragMode = true;
        // 宽
        private int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 高
        private int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // gravity
        private int gravity = Gravity.END | Gravity.BOTTOM;


        public boolean isGlobalFloat() {
            return isGlobalFloat;
        }

        public void setGlobalFloat(boolean globalFloat) {
            isGlobalFloat = globalFloat;
        }

        public boolean isAutoCompat() {
            return autoCompat;
        }

        public void setAutoCompat(boolean autoCompat) {
            this.autoCompat = autoCompat;
        }

        public boolean isAutoHide() {
            return autoHide;
        }

        public void setAutoHide(boolean autoHide) {
            this.autoHide = autoHide;
        }

        public int getAutoHideDelay() {
            return autoHideDelay;
        }

        public void setAutoHideDelay(int autoHideDelay) {
            this.autoHideDelay = autoHideDelay;
        }

        public boolean isDragMode() {
            return isDragMode;
        }

        public void setDragMode(boolean dragMode) {
            isDragMode = dragMode;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getGravity() {
            return gravity;
        }

        public void setGravity(int gravity) {
            this.gravity = gravity;
        }

        FloatViewConfig build() {
            return new FloatViewConfig(this);
        }
    }

}
