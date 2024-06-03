package com.rainmonth.imageloader.strategy.fresco;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.rainmonth.imageloader.ILoadStrategy;
import com.rainmonth.imageloader.LoadConfig;

/**
 * @author RandyZhang
 * @date 2021/8/31 3:14 下午
 */
public class BaseFrescoLoadStrategy {

    boolean isCached(Context context, String uri) {
        try {
            return isCached(context, Uri.parse(uri));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 是否缓存
     *
     * @param context caller context
     * @param uri     the load Uri
     * @return true if had cached
     */
    boolean isCached(Context context, Uri uri) {
        ImagePipeline pipeline = Fresco.getImagePipeline();
        DataSource<Boolean> dataSource = pipeline.isInDiskCache(uri);
        if (dataSource == null) {
            return false;
        }
        ImageRequest imageRequest = ImageRequest.fromUri(uri);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                .getEncodedCacheKey(imageRequest, context);
        BinaryResource resource = ImagePipelineFactory.getInstance()
                .getMainFileCache().getResource(cacheKey);
        return resource != null && dataSource.getResult() != null && dataSource.getResult();
    }

    /**
     * 获取要加载的 Uri
     *
     * @param targetUri 待加载的地址，可能是String，也可能是Uri
     * @return 要加载的Uri
     */
    Uri getUri(Object targetUri) {
        Uri uri = null;
        if (targetUri instanceof String) {
            try {
                uri = Uri.parse((String) targetUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (targetUri instanceof Uri) {
            uri = (Uri) targetUri;
        }

        return uri;
    }


    /**
     * 初始化要展示图像的View
     *
     * @param simpleDraweeView 展示图片的View
     * @param loadConfig       加载配置
     */
    void initFrescoView(SimpleDraweeView simpleDraweeView, LoadConfig loadConfig) {
        if (loadConfig.placeholderId > 0) {
            simpleDraweeView.getHierarchy().setPlaceholderImage(loadConfig.placeholderId);
        }
        if (loadConfig.errorId > 0) {
            simpleDraweeView.getHierarchy().setFailureImage(loadConfig.errorId);
        }
        if (loadConfig.isCircle) {
            setRoundingParmas(simpleDraweeView, getRoundingParams(simpleDraweeView).setRoundAsCircle(true));
        } else {
            setRoundingParmas(simpleDraweeView, getRoundingParams(simpleDraweeView).setRoundAsCircle(false));
        }
    }

    ImageRequest buildImageRequestWithResource(LoadConfig loadConfig,
                                               ILoadStrategy.ExtendedOptions extendedOptions) {
        String remoteTarget = loadConfig.mUri;
        ImageRequestBuilder builder;
        // todo 增加不同显示方式的判断
        Uri uri = Uri.parse(remoteTarget);
        builder = ImageRequestBuilder.newBuilderWithSource(uri);

        if (loadConfig.mSize != null && loadConfig.mSize.x > 0 && loadConfig.mSize.y > 0) {
            ResizeOptions options = new ResizeOptions(loadConfig.mSize.x, loadConfig.mSize.y);
            builder.setResizeOptions(options);
        } else {
            builder.setResizeOptions(null);
        }
        if (loadConfig.mTransformations != null && loadConfig.mTransformations.size() > 0) {
            Object object = loadConfig.mTransformations.get(0);
            if (object instanceof BasePostprocessor) {
                builder.setPostprocessor((Postprocessor) object);
            }
        }
        if (extendedOptions != null) {
            extendedOptions.onOptionsInit(builder);
        }
        return builder.build();
    }

    ImageRequest buildLowImageRequest(SimpleDraweeView simpleDraweeView,
                                      LoadConfig loadConfig,
                                      ILoadStrategy.ExtendedOptions extendOption) {
        /*String lowThumbnail = null
        if (TextUtils.isEmpty(fresco.getLowThumbnailUrl())) {
            return null
        }
        lowThumbnail = fresco.getLowThumbnailUrl()
        val uri = Uri.parse(lowThumbnail)
        return ImageRequest.fromUri(uri)*/
        return null;
    }

    DraweeController buildDraweeController(SimpleDraweeView simpleDraweeView,
                                           LoadConfig loadConfig,
                                           ILoadStrategy.Callback callback,
                                           ImageRequest imageRequest,
                                           ImageRequest lowRequest) {
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder();
        builder.setImageRequest(imageRequest)
                .setAutoPlayAnimations(loadConfig.isPlayGif)
                //.setTapToRetryEnabled(fresco.getTapToRetryEnabled())
                .setLowResImageRequest(lowRequest);
        if (callback != null) {
            builder.setControllerListener(new ControllerListener<ImageInfo>() {
                @Override
                public void onSubmit(String id, Object callerContext) {
                    callback.onStart();
                }

                @Override
                public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo,
                                            @Nullable Animatable animatable) {
                    callback.onSuccess(new Exception(id));
                }

                @Override
                public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                    callback.onSuccess(new Exception(id));
                }

                @Override
                public void onIntermediateImageFailed(String id, Throwable throwable) {
                    callback.onFail(new Exception(throwable));
                }

                @Override
                public void onFailure(String id, Throwable throwable) {
                    callback.onFail(new Exception(throwable));
                }

                @Override
                public void onRelease(String id) {

                }
            });
        } else {
            builder.setControllerListener(new BaseControllerListener<>());
        }
        builder.setOldController(simpleDraweeView.getController());
        return builder.build();
    }

    RoundingParams getRoundingParams(SimpleDraweeView simpleDraweeView) {
        RoundingParams roundingParams = simpleDraweeView.getHierarchy().getRoundingParams();
        if (roundingParams == null) {
            roundingParams = new RoundingParams();
        }
        return roundingParams;
    }

    void setRoundingParmas(SimpleDraweeView simpleDraweeView, RoundingParams roundingParmas) {
        simpleDraweeView.getHierarchy().setRoundingParams(roundingParmas);
    }
}
