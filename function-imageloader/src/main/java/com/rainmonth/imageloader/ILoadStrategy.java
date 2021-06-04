package com.rainmonth.imageloader;

import android.widget.ImageView;

/**
 * 图片加载策略
 *
 * @author randy
 * @date 2021/06/04 11:49 AM
 */
public interface ILoadStrategy {
    default void loadImage(LoadConfig loadConfig) {

    }

    default void loadImage(LoadConfig loadConfig, ImageView targetView) {

    }
}
