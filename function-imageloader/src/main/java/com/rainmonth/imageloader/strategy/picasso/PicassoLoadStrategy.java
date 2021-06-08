package com.rainmonth.imageloader.strategy.picasso;

import android.graphics.Bitmap;
import android.view.View;

import com.rainmonth.imageloader.ILoadStrategy;
import com.rainmonth.imageloader.LoadConfig;

import java.io.File;

/**
 * 使用 Picasso 进行图片加载
 *
 * @author randy
 * @date 2021/06/04 11:55 AM
 */
public class PicassoLoadStrategy implements ILoadStrategy {

    @Override
    public void loadImage(LoadConfig loadOption, View view, Callback callback, ExtendedOptions extendOption) {

    }

    @Override
    public void clearCache(int type) {

    }

    @Override
    public void clearCacheKey(int type, LoadConfig loadOption) {

    }

    @Override
    public boolean isCache(LoadConfig loadOption, ExtendedOptions extendedOption) {
        return false;
    }

    @Override
    public File getLocalCache(LoadConfig loadOption, ExtendedOptions extendOption) {
        return null;
    }

    @Override
    public Bitmap getLocalCacheBitmap(LoadConfig loadOption, ExtendedOptions extendOption) {
        return null;
    }

    @Override
    public long getCacheSize() {
        return 0;
    }

    @Override
    public void downloadOnly(LoadConfig loadOption, Callback callback, ExtendedOptions extendOption) {

    }
}
