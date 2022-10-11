package com.rainmonth.floatview;

import android.view.Gravity;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

/**
 * 悬浮View配置
 */
public class FloatViewConfig {
    /**
     * 悬浮View的id
     */
    public int id;
    /**
     * 是否支持全局悬浮，true 表示即使应用退到后台，也可以悬浮展示
     */
    public boolean isGlobalFloat = false;
    /**
     * 是否自适应（即为获取到权限时 自动降级处理）
     */
    public boolean autoCompat = true;
    /**
     * 是否自动隐藏
     */
    public boolean autoHide = false;
    /**
     * 自动隐藏时间间隔，默认5s
     */
    public int autoHideDelay = 5000;
    /**
     * 是否是拖动模式，true 表示可以在窗口内拖动，false 表示固定位置
     */
    public boolean isDragMode = true;
    /**
     * 初始x轴位置
     */
    public int initPosX = 0;
    /**
     * 初始y轴位置
     */
    public int initPosY = 0;
    /**
     * 宽
     */
    public int width = ViewGroup.LayoutParams.WRAP_CONTENT;
    /**
     * 高
     */
    public int height = ViewGroup.LayoutParams.WRAP_CONTENT;
    /**
     * gravity
     */
    public int gravity = Gravity.START | Gravity.TOP;

    @LayoutRes
    public int itemViewRes = -1;

    public FloatViewConfig() {

    }

    private FloatViewConfig(Builder builder) {
        this.id = builder.id;
        this.isGlobalFloat = builder.isGlobalFloat;
        this.autoCompat = builder.autoCompat;
        this.autoHide = builder.autoHide;
        this.autoHideDelay = builder.autoHideDelay;
        this.isDragMode = builder.isDragMode;
        this.initPosX = builder.initPosX;
        this.initPosY = builder.initPosY;
        this.width = builder.width;
        this.height = builder.height;
        this.gravity = builder.gravity;
        this.itemViewRes = builder.itemViewRes;
    }

    /**
     * 更新配置
     *
     * @param config 配置对象
     */
    public void update(@NonNull FloatViewConfig config) {
        this.id = config.id;
        this.isGlobalFloat = config.isGlobalFloat;
        this.autoCompat = config.autoCompat;
        this.autoHide = config.autoHide;
        this.autoHideDelay = config.autoHideDelay;
        this.isDragMode = config.isDragMode;
        this.initPosX = config.initPosX;
        this.initPosY = config.initPosY;
        this.width = config.width;
        this.height = config.height;
        this.gravity = config.gravity;
        this.itemViewRes = config.itemViewRes;
    }

    static class Builder {
        private int id;
        private boolean isGlobalFloat = false;
        private boolean autoCompat = true;
        private boolean autoHide = false;
        private int autoHideDelay = 5000;
        private boolean isDragMode = true;
        public int initPosX = 0;
        public int initPosY = 0;
        private int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        private int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        private int gravity = Gravity.START | Gravity.TOP;
        @LayoutRes
        public int itemViewRes = -1;

        /**
         * 设置悬浮View的id
         *
         * @param id
         */
        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        /**
         * 设置是否全局悬浮
         *
         * @param globalFloat true则可以悬浮在其他应用上面（需要请求权限
         */
        public Builder setGlobalFloat(boolean globalFloat) {
            isGlobalFloat = globalFloat;
            return this;
        }

        /**
         * 是否进行自动适配处理
         *
         * @param autoCompat true时全局悬浮窗权限被拒绝后会自动采用应用内悬浮窗进行适配
         */
        public Builder setAutoCompat(boolean autoCompat) {
            this.autoCompat = autoCompat;
            return this;
        }

        /**
         * 设置悬浮View是否自动隐藏
         *
         * @param autoHide ture 时会自动隐藏
         */
        public Builder setAutoHide(boolean autoHide) {
            this.autoHide = autoHide;
            return this;
        }

        /**
         * 设置悬浮View在多久后自动隐藏
         *
         * @param autoHideDelay 自动隐藏的延时
         */
        public Builder setAutoHideDelay(int autoHideDelay) {
            this.autoHideDelay = autoHideDelay;
            return this;
        }

        /**
         * 设置悬浮View 是否支持拖动
         *
         * @param dragMode 是否是拖动模式 true 表示悬浮View可以自用拖动
         */
        public Builder setDragMode(boolean dragMode) {
            isDragMode = dragMode;
            return this;
        }

        /**
         * 设置初始态状态下相对于屏幕左上角的x轴坐标
         *
         * @param initPosX 相对于左上角x轴位置
         */
        public Builder setInitPosX(int initPosX) {
            this.initPosX = initPosX;
            return this;
        }

        /**
         * 设置初始态状态下相对于屏幕左上角的y轴坐标
         *
         * @param initPosY 相对于左上角y轴位置
         * @return
         */
        public Builder setInitPosY(int initPosY) {
            this.initPosY = initPosY;
            return this;
        }

        /**
         * 设置悬浮View的宽，默认为{@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT}
         *
         * @param width 宽度
         */
        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        /**
         * 设置悬浮View的高，默认为{@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT}
         *
         * @param height 高
         */
        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        /**
         * 设置悬浮View的gravity，默认为 {@link Gravity#END } | {@link Gravity#BOTTOM}
         *
         * @param gravity gravity位置
         */
        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
         * 设置悬浮View的子View
         *
         * @param itemViewRes 子View的资源id，没有设置会跑异常
         */
        public Builder setItemViewRes(int itemViewRes) {
            this.itemViewRes = itemViewRes;
            return this;
        }

        FloatViewConfig build() {
            return new FloatViewConfig(this);
        }
    }

}
