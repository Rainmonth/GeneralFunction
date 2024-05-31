package com.rainmonth.imageloader;

import android.graphics.Point;
import android.view.View;

import androidx.annotation.DrawableRes;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 图片加载配置
 *
 * @author randy
 * @date 2021/06/04 11:39 AM
 */
public class LoadConfig {
    /**
     * 默认占位图资源id
     */
    public int placeholderId;

    /**
     * 错误占位图资源id
     */
    public int errorId;

    //是否圆形
    public boolean isCircle = false;

    /**
     * 是否播放gif
     */
    public boolean isPlayGif = false;

    /**
     * 图片显示大小
     */
    public Point mSize = null;

    /**
     * 图片下载地址
     */
    public String mUri;

    /**
     * 最大磁盘缓存容量
     */
    public long mMaxDiskCacheSize = 256 * 1024 * 1024L;

    /**
     * 最大内存缓存容量
     */
    public long mMaxMemCacheSize = 16 * 1024 * 1024L;

    public ArrayList<Object> mTransformations = new ArrayList<>();

    public LoadConfig setPlaceholderId(int placeholderId) {
        this.placeholderId = placeholderId;
        return this;
    }

    public LoadConfig setErrorId(int errorId) {
        this.errorId = errorId;
        return this;
    }

    public LoadConfig setCircle(boolean circle) {
        isCircle = circle;
        return this;
    }

    public LoadConfig setPlayGif(boolean playGif) {
        isPlayGif = playGif;
        return this;
    }

    public LoadConfig setSize(Point size) {
        mSize = size;
        return this;
    }

    public LoadConfig setUri(String uri) {
        mUri = uri;
        return this;
    }

    /**
     * 图片处理
     * picasso <a href="https://github.com/wasabeef/picasso-transformations">...</a>
     * glide   <a href="https://github.com/wasabeef/glide-transformations">...</a>
     * fresco  <a href="https://github.com/wasabeef/fresco-processors">...</a>
     */
    public LoadConfig setTransformations(Objects transformation) {
        mTransformations.add(transformation);
        return this;
    }

    public LoadConfig maxDiskCacheSize(long maxDiskCacheSize) {
        mMaxDiskCacheSize = maxDiskCacheSize;
        return this;
    }

    public LoadConfig maxMemCacheSize(long maxMemCacheSize) {
        mMaxMemCacheSize = maxMemCacheSize;
        return this;
    }
}
