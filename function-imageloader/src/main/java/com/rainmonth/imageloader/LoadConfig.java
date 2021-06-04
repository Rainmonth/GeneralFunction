package com.rainmonth.imageloader;

import android.view.View;

import androidx.annotation.DrawableRes;

/**
 * 图片加载配置
 *
 * @author randy
 * @date 2021/06/04 11:39 AM
 */
public class LoadConfig {
    public @DrawableRes
    int placeholderId;
    public @DrawableRes
    int errorHolderId;
    /**
     * 要加载图片的View
     */
    public View targetView;

}
