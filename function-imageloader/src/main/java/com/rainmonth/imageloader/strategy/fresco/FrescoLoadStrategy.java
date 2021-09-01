package com.rainmonth.imageloader.strategy.fresco;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.cache.MemoryCache;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.rainmonth.imageloader.ILoadStrategy;
import com.rainmonth.imageloader.LoadConfig;
import com.rainmonth.imageloader.LoaderConst;
import com.rainmonth.utils.Utils;

import java.io.File;

/**
 * 使用 Fresco 进行图片加载
 * Fresco 对应中文文档 https://www.fresco-cn.org/
 *
 * @author randy
 * @date 2021/06/04 11:52 AM
 */
public class FrescoLoadStrategy extends BaseFrescoLoadStrategy implements ILoadStrategy {

    public FrescoLoadStrategy(ImagePipelineConfig config) {
        if (config == null) {
            config = ImagePipelineConfig.newBuilder(Utils.getApp())
                    .setDownsampleEnabled(true)
                    .build();
        }
        Fresco.initialize(Utils.getApp(), config);
    }

    @Override
    public void loadImage(LoadConfig loadConfig, View view, Callback callback,
                          ExtendedOptions extendOption) {
        if (!(view instanceof SimpleDraweeView)) {
            throw new IllegalArgumentException("view must be SimpleDraweeView");
        }

        try {
            SimpleDraweeView target = (SimpleDraweeView) view;
            initFrescoView(target, loadConfig);
            ImageRequest imageRequest = buildImageRequestWithResource(loadConfig, extendOption);
            ImageRequest lowRequest = buildLowImageRequest(target, loadConfig, extendOption);
            target.setController(buildDraweeController(target, loadConfig, callback, imageRequest,
                    lowRequest));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearCache(int type) {
        if (type == LoaderConst.CacheClearType.CLEAR_ALL_CACHE) {
            Fresco.getImagePipeline().clearCaches();
        }
        if (type == LoaderConst.CacheClearType.CLEAR_MEM_CACHE) {
            Fresco.getImagePipeline().clearMemoryCaches();
        }
        if (type == LoaderConst.CacheClearType.CLEAR_DISK_CACHE) {
            Fresco.getImagePipeline().clearDiskCaches();
        }
    }

    @Override
    public void clearCacheKey(int type, LoadConfig loadConfig) {
        Uri loadUri = getUri(loadConfig.mUri);
        if (type == LoaderConst.CacheClearType.CLEAR_ALL_CACHE) {
            Fresco.getImagePipeline().evictFromCache(loadUri);
        }
        if (type == LoaderConst.CacheClearType.CLEAR_MEM_CACHE) {
            Fresco.getImagePipeline().evictFromMemoryCache(loadUri);
        }
        if (type == LoaderConst.CacheClearType.CLEAR_DISK_CACHE) {
            Fresco.getImagePipeline().evictFromDiskCache(loadUri);
        }
    }

    @Override
    public boolean isCache(LoadConfig loadConfig, ExtendedOptions extendedOption) {
        return isCached(Utils.getApp(), loadConfig.mUri);
    }

    @Override
    public File getLocalCache(LoadConfig loadConfig, ExtendedOptions extendOption) {
        Uri loadUri = getUri(loadConfig.mUri);
        if (!isCached(Utils.getApp(), loadUri)) {
            return null;
        }
        ImageRequest imageRequest = ImageRequest.fromUri(loadUri);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                .getEncodedCacheKey(imageRequest, Utils.getApp());
        BinaryResource resource = ImagePipelineFactory.getInstance()
                .getMainFileCache().getResource(cacheKey);
        if (resource instanceof FileBinaryResource) {
            return ((FileBinaryResource) resource).getFile();
        } else {
            return null;
        }
    }

    @Override
    public Bitmap getLocalCacheBitmap(LoadConfig loadConfig, ExtendedOptions extendOption) {
        Uri loadUri = getUri(loadConfig.mUri);
        if (!isCached(Utils.getApp(), loadUri))
            return null;
        ImageRequest request = buildImageRequestWithResource(loadConfig, extendOption);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                .getBitmapCacheKey(request, Utils.getApp());
        CloseableReference<CloseableImage> resource = ImagePipelineFactory.getInstance()
                .getBitmapCountingMemoryCache().get(cacheKey);
        if (resource != null && resource.get() instanceof CloseableBitmap) {
            return ((CloseableBitmap) resource.get()).getUnderlyingBitmap();
        } else {
            return null;
        }
    }

    @Override
    public long getCacheSize(LoadConfig loadConfig) {
        return ImagePipelineFactory.getInstance().getMainFileCache().getSize();
    }

    @Override
    public void downloadOnly(LoadConfig loadConfig, Callback callback,
                             ExtendedOptions extendOption) {
        ImageRequest imageRequest = buildImageRequestWithResource(loadConfig, extendOption);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<Void> dataSource = imagePipeline.prefetchToDiskCache(imageRequest,
                Utils.getApp());
        dataSource.subscribe(new BaseDataSubscriber<Void>() {

            @Override
            protected void onNewResultImpl(@NonNull DataSource<Void> dataSource) {
                File file = getLocalCache(loadConfig, extendOption);
                if (callback != null) {
                    callback.onSuccess(file);
                }
            }

            @Override
            protected void onFailureImpl(@NonNull DataSource<Void> dataSource) {
                if (callback != null) {
                    callback.onFail(null);
                }
            }
        }, CallerThreadExecutor.getInstance());
    }

}
