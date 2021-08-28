package com.rainmonth.imageloader;

import android.graphics.Bitmap;
import android.view.View;

import java.io.File;

/**
 * @author randy
 * @date 2021/06/04 11:44 AM
 */
public class ImageLoader implements ILoadStrategy{
    private ILoadStrategy mLoadStrategy;

    public ImageLoader(ILoadStrategy loadStrategy) {
        mLoadStrategy = loadStrategy;
    }

    @Override
    public void loadImage(LoadConfig loadConfig, View view, Callback callback, ExtendedOptions extendOption) {
        mLoadStrategy.loadImage(loadConfig, view, callback, extendOption);
    }

    @Override
    public void clearCache(int type) {
        mLoadStrategy.clearCache(type);
    }

    @Override
    public void clearCacheKey(int type, LoadConfig loadConfig) {
        mLoadStrategy.clearCacheKey(type, loadConfig);
    }

    @Override
    public boolean isCache(LoadConfig loadConfig, ExtendedOptions extendedOption) {
        return mLoadStrategy.isCache(loadConfig, extendedOption);
    }

    @Override
    public File getLocalCache(LoadConfig loadConfig, ExtendedOptions extendOption) {
        return mLoadStrategy.getLocalCache(loadConfig, extendOption);
    }

    @Override
    public Bitmap getLocalCacheBitmap(LoadConfig loadConfig, ExtendedOptions extendOption) {
        return mLoadStrategy.getLocalCacheBitmap(loadConfig, extendOption);
    }

    @Override
    public long getCacheSize(LoadConfig loadConfig) {
        return mLoadStrategy.getCacheSize(loadConfig);
    }

    @Override
    public void downloadOnly(LoadConfig loadConfig, Callback callback, ExtendedOptions extendOption) {
        mLoadStrategy.downloadOnly(loadConfig, callback, extendOption);
    }
}
