package com.rainmonth.imageloader.strategy.fresco;

import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.rainmonth.imageloader.ILoadStrategy;
import com.rainmonth.imageloader.LoadConfig;
import com.rainmonth.utils.Utils;

import java.io.File;

/**
 * 使用 Fresco 进行图片加载
 * Fresco 对应中文文档 https://www.fresco-cn.org/
 *
 * @author randy
 * @date 2021/06/04 11:52 AM
 */
public class FrescoLoadStrategy implements ILoadStrategy {

    public FrescoLoadStrategy(ImagePipelineConfig config) {
        if (config == null) {
            config = ImagePipelineConfig.newBuilder(Utils.getApp())
                    .setDownsampleEnabled(true)
                    .build();
        }
        Fresco.initialize(Utils.getApp(), config);
    }

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
        return ImagePipelineFactory.getInstance().getMainFileCache().getSize();
    }

    @Override
    public void downloadOnly(LoadConfig loadConfig, Callback callback,
                             ExtendedOptions extendOption) {
        ImageRequest imageRequest = buildImageRequestWithResource(loadConfig, extendOption);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<Void> dataSource = imagePipeline.prefetchToDiskCache(imageRequest, Utils.getApp());
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


    /**
     * 根据配置获取 {@link ImageRequest}
     *
     * @param loadConfig      通用配置
     * @param extendedOptions 加载框架扩展配置
     * @return ImageRequest
     */
    private ImageRequest buildImageRequestWithResource(LoadConfig loadConfig,
                                                       ExtendedOptions extendedOptions) {


        return null;
    }

}
