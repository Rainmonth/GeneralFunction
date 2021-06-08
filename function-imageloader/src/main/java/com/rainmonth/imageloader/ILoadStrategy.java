package com.rainmonth.imageloader;

import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.UiThread;

import java.io.File;

/**
 * 图片加载策略
 *
 * @author randy
 * @date 2021/06/04 11:49 AM
 */
public interface ILoadStrategy {
    /**
     * 加载图片
     *
     * @param loadOption   加载图片配置
     * @param view         加载目标对象，ImageView or SimpleDraweeView
     * @param callback     加载回调
     * @param extendOption 额外配置接口
     */
    void loadImage(LoadConfig loadOption, View view, Callback callback, ExtendedOptions extendOption);

    /**
     * 清除缓存
     *
     * @param type 清除类型
     *             {@link LoaderConst.CacheClearType#CLEAR_ALL_CACHE}
     *             {@link LoaderConst.CacheClearType#CLEAR_MEM_CACHE}
     *             {@link LoaderConst.CacheClearType#CLEAR_DISK_CACHE}
     */
    void clearCache(int type);

    /**
     * 清除指定缓存
     *
     * @param type       清除类型
     *                   {@link LoaderConst.CacheClearType#CLEAR_ALL_CACHE}
     *                   {@link LoaderConst.CacheClearType#CLEAR_MEM_CACHE}
     *                   {@link LoaderConst.CacheClearType#CLEAR_DISK_CACHE}
     * @param loadOption 加载图片配置
     */
    void clearCacheKey(int type, LoadConfig loadOption);

    /**
     * 是否已经缓存到本地
     *
     * @param loadOption     加载图片配置
     * @param extendedOption 额外配置接口
     * @return Boolean 是否已经缓存到本地
     */
    boolean isCache(LoadConfig loadOption, ExtendedOptions extendedOption);

    /**
     * 获取本地缓存
     *
     * @param loadOption   加载图片配置
     * @param extendOption 额外配置接口
     * @return File
     */
    File getLocalCache(LoadConfig loadOption, ExtendedOptions extendOption);

    /**
     * 获取本地缓存bitmap
     *
     * @param loadOption   加载图片配置
     * @param extendOption 额外配置接口
     * @return Bitmap
     */
    Bitmap getLocalCacheBitmap(LoadConfig loadOption, ExtendedOptions extendOption);


    /**
     * 获取本地缓存大小
     *
     * @return Long
     */
    long getCacheSize();


    /**
     * 下载图片
     *
     * @param loadOption   加载图片配置
     * @param callback     加载回调
     * @param extendOption 额外配置接口
     */
    void downloadOnly(LoadConfig loadOption, Callback callback, ExtendedOptions extendOption);

    /**
     * 额外配置支持
     */
    interface ExtendedOptions {
        /**
         * @param option 配置对象
         *               Glide    com.bumptech.glide.request.RequestOptions
         *               Picasso  com.squareup.picasso.RequestCreator
         *               Fresco   com.facebook.imagepipeline.request.ImageRequestBuilder
         */
        void onOptionsInit(Object option);
    }

    /**
     * 回调接口
     */
    @UiThread
    interface Callback {
        void onStart();

        void onSuccess(Object result);

        void onFail(Exception error);
    }
}
