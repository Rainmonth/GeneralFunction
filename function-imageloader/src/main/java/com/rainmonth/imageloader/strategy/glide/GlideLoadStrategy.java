package com.rainmonth.imageloader.strategy.glide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.EmptySignature;
import com.rainmonth.imageloader.ILoadStrategy;
import com.rainmonth.imageloader.LoadConfig;
import com.rainmonth.imageloader.LoaderConst;
import com.rainmonth.utils.ReflectionHelper;
import com.rainmonth.utils.Utils;

import java.io.File;
import java.util.ArrayList;
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

        loadImage(loadConfig, extendOption)
                .load(loadConfig.mUri)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e,
                                                Object model,
                                                Target<Drawable> target,
                                                boolean isFirstResource) {
                        if (callback != null) {
                            callback.onFail(e);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource,
                                                   Object model,
                                                   Target<Drawable> target,
                                                   DataSource dataSource,
                                                   boolean isFirstResource) {
                        if (callback != null) {
                            callback.onSuccess(resource);
                        }
                        return false;
                    }
                })
                .into((ImageView) view);
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
        if (type == LoaderConst.CacheClearType.CLEAR_ALL_CACHE) {
            clearDiskCacheKey(loadConfig);
            clearMemCacheKey(loadConfig);
        }
        if (type == LoaderConst.CacheClearType.CLEAR_DISK_CACHE) {
            clearDiskCacheKey(loadConfig);
        }
        if (type == LoaderConst.CacheClearType.CLEAR_MEM_CACHE) {
            clearMemCacheKey(loadConfig);
        }
    }

    /**
     * 清除硬盘中的 key
     *
     * @param loadConfig 加载配置选项
     */
    private void clearDiskCacheKey(LoadConfig loadConfig) {
        DiskCache diskCache = DiskLruCacheWrapper.create(Glide.getPhotoCacheDir(getContext()),
                loadConfig.mMaxDiskCacheSize);
        GlideCacheKey key = new GlideCacheKey(loadConfig.mUri, EmptySignature.obtain());
        diskCache.delete(key);
    }

    /**
     * 清除内存中的 key
     */
    private void clearMemCacheKey(LoadConfig loadConfig) {
        GlideCacheKey memKey = new GlideCacheKey(loadConfig.mUri, EmptySignature.obtain());
        MemoryCache cache = ReflectionHelper.INSTANCE.getField(Glide.get(getContext()), "memoryCache");
        cache.remove(memKey);
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
    public long getCacheSize(LoadConfig loadConfig) {
        DiskCache cache = DiskLruCacheWrapper.create(Glide.getPhotoCacheDir(getContext()),
                loadConfig.mMaxDiskCacheSize);
        DiskLruCache diskLruCache = ReflectionHelper.INSTANCE.getField(cache, "diskLruCache");
        return diskLruCache.size();
    }

    @Override
    public void downloadOnly(LoadConfig loadConfig, Callback callback, ExtendedOptions extendOption) {
        loadImage(loadConfig, extendOption).downloadOnly().load(loadConfig.mUri).into(new GlideLoadTarget(callback));
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
        return Glide.with(getContext().getApplicationContext())
                .setDefaultRequestOptions(getRequestOptions(loadConfig, extendedOptions));
    }

    @SuppressLint("CheckResult")
    private RequestOptions getRequestOptions(LoadConfig loadConfig, ExtendedOptions extendedOptions) {
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
        if (loadConfig.mTransformations != null && loadConfig.mTransformations.size() > 0) {
            try {
                requestOptions.transform((Transformation<Bitmap>) loadConfig.mTransformations);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (extendedOptions != null) {
            extendedOptions.onOptionsInit(requestOptions);
        }
        return requestOptions;
    }
}
