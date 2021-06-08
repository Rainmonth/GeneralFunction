package com.rainmonth.imageloader.strategy.glide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.rainmonth.imageloader.ILoadStrategy;
import com.rainmonth.imageloader.LoadConfig;
import com.rainmonth.imageloader.LoaderConst;
import com.rainmonth.utils.Utils;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * 使用 Glide 进行图片加载
 *
 * @author randy
 * @date 2021/06/04 11:52 AM
 */
public class GlideLoadStrategy implements ILoadStrategy {

    @Override
    public void loadImage(LoadConfig loadConfig, View view, Callback callback, ExtendedOptions extendOption) {
        if (!(view instanceof ImageView)) {
            throw new IllegalArgumentException("view must be ImageView");
        }

    }

    @Override
    public void clearCache(int type) {
        if (type == LoaderConst.CacheClearType.CLEAR_DISK_CACHE) {
            Glide.get(getContext()).clearDiskCache();
        } else if (type == LoaderConst.CacheClearType.CLEAR_MEM_CACHE) {
            Glide.get(getContext()).clearMemory();
        } else {
            Glide.get(getContext()).clearDiskCache();
            Glide.get(getContext()).clearMemory();
        }
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
        RequestBuilder<Bitmap> bitmapRequestBuilder = loadImage(loadConfig, extendOption).asBitmap().load(loadConfig.mUri);
        try {
            return bitmapRequestBuilder.submit().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getCacheSize() {
        return 0;
    }

    @Override
    public void downloadOnly(LoadConfig loadConfig, Callback callback, ExtendedOptions extendOption) {
        loadImage(loadConfig,extendOption).downloadOnly().load(loadConfig.mUri).into(new GlideLoadTarget(callback));
    }

    /**
     * 获取 Context
     * todo 进行替换
     *
     * @return Context
     */
    private Context getContext() {
        return Utils.getApp();
    }

    private RequestManager loadImage(LoadConfig loadConfig, ExtendedOptions extendedOptions) {
        return Glide.with(getContext().getApplicationContext()).setDefaultRequestOptions(getRequestOptions(loadConfig));
    }

    @SuppressLint("CheckResult")
    private RequestOptions getRequestOptions(LoadConfig loadConfig) {
        RequestOptions requestOptions = new RequestOptions();
        if (loadConfig.placeholderId > 0) {
            requestOptions.placeholder(loadConfig.placeholderId);
        }

        if (loadConfig.errorId > 0) {
            requestOptions.error(loadConfig.errorId);
        }

        if (loadConfig.isCircle) {
            requestOptions.circleCrop();
        }
        if (loadConfig.mSize != null) {
            requestOptions.override(loadConfig.mSize.x, loadConfig.mSize.y);
        }
        return requestOptions;
    }
}
