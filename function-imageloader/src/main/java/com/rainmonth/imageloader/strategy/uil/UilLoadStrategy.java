package com.rainmonth.imageloader.strategy.uil;

import android.graphics.Bitmap;
import android.view.View;

import com.rainmonth.imageloader.ILoadStrategy;
import com.rainmonth.imageloader.LoadConfig;

import java.io.File;

/**
 * 使用 UniversalImageLoader 加载
 *
 * @author randy
 * @date 2021/06/04 11:56 AM
 */
public class UilLoadStrategy implements ILoadStrategy {
    @Override
    public void loadImage(LoadConfig loadConfig, View view, Callback callback, ExtendedOptions extendOption) {

    }

    @Override
    public void clearCache(int type) {

    }

    @Override
    public void clearCacheKey(int type, LoadConfig loadConfig) {

    }

    @Override
    public boolean isCache(LoadConfig loadConfig, ExtendedOptions extendedOption) {
        return false;
    }

    @Override
    public File getLocalCache(LoadConfig loadConfig, ExtendedOptions extendOption) {
        return null;
    }

    @Override
    public Bitmap getLocalCacheBitmap(LoadConfig loadConfig, ExtendedOptions extendOption) {
        return null;
    }

    @Override
    public long getCacheSize(LoadConfig loadConfig) {
        return 0;
    }

    @Override
    public void downloadOnly(LoadConfig loadConfig, Callback callback, ExtendedOptions extendOption) {

    }
}
