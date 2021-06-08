package com.rainmonth.imageloader.strategy.glide;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import com.rainmonth.imageloader.LoadConfig;

/**
 * @author randy
 * @date 2021/06/04 1:44 PM
 */
public class GlideLoadConfig extends LoadConfig {
    @Nullable private Drawable errorPlaceHolder;
    private int errorId;
    @Nullable private Drawable placeholderDrawable;
    private int placeholderId;
    private Drawable fallbackDrawable;
}
