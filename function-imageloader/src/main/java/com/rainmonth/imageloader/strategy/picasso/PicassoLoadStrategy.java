package com.rainmonth.imageloader.strategy.picasso;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.rainmonth.imageloader.ILoadStrategy;
import com.rainmonth.imageloader.LoadConfig;
import com.rainmonth.utils.ReflectionHelper;
import com.squareup.picasso.Cache;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.File;

/**
 * 使用 Picasso 进行图片加载
 *
 * @author randy
 * @date 2021/06/04 11:55 AM
 */
public class PicassoLoadStrategy implements ILoadStrategy {

    private final Picasso mPicasso;

    public PicassoLoadStrategy(Picasso.Builder builder) {
        if (builder != null) {
            mPicasso = builder.build();
        } else {
            mPicasso = Picasso.get();
        }
    }

    @Override
    public void loadImage(LoadConfig loadConfig, View view, Callback callback, ExtendedOptions extendOption) {
        if (!(view instanceof ImageView)) {
            throw new IllegalArgumentException("View must be ImageView");
        }
        RequestCreator requestCreator = getRequest(loadConfig, extendOption);
        if (requestCreator != null) {
            requestCreator.into((ImageView) view, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                }

                @Override
                public void onError(Exception e) {
                    if (callback != null) {
                        callback.onFail(e);
                    }
                }
            });
        }

    }

    @Override
    public void clearCache(int type) {
        try {
            Cache cache = ReflectionHelper.INSTANCE.getField(mPicasso, "cache");
            cache.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearCacheKey(int type, LoadConfig loadConfig) {
        mPicasso.invalidate(loadConfig.mUri);
    }

    @Override
    public boolean isCache(LoadConfig loadConfig, ExtendedOptions extendedOption) {
        Log.e(getClass().getSimpleName(), "not support for picasso");
        return false;
    }

    @Override
    public File getLocalCache(LoadConfig loadConfig, ExtendedOptions extendOption) {
        Log.e(getClass().getSimpleName(), "not support for picasso");
        return null;
    }

    @Override
    public Bitmap getLocalCacheBitmap(LoadConfig loadConfig, ExtendedOptions extendOption) {
        Bitmap bitmap = null;
        try {
            bitmap = getRequest(loadConfig, extendOption) != null ? getRequest(loadConfig, extendOption).get() : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public long getCacheSize(LoadConfig loadConfig) {
        return mPicasso.getSnapshot().size;
    }

    @Override
    public void downloadOnly(LoadConfig loadConfig, Callback callback, ExtendedOptions extendOption) {
        RequestCreator requestCreator = getRequest(loadConfig, extendOption);
        if (requestCreator != null) {
            requestCreator.into(new Target() {
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    if (callback != null) {
                        callback.onStart();
                    }
                }

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    if (callback != null) {
                        callback.onSuccess(bitmap);
                    }
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    if (callback != null) {
                        callback.onFail(e);
                    }
                }
            });
        }
    }


    private RequestCreator getRequest(LoadConfig loadConfig, ExtendedOptions extendedOptions) {
        RequestCreator requestCreator = null;
        requestCreator = mPicasso.load(loadConfig.mUri);

        if (requestCreator != null) {
            if (loadConfig.placeholderId > 0) {
                requestCreator.placeholder(loadConfig.placeholderId);
            }
            if (loadConfig.errorId > 0) {
                requestCreator.error(loadConfig.errorId);
            }
            if (loadConfig.isCircle) {
                // todo
            }
            if (loadConfig.mSize != null) {
                requestCreator.resize(loadConfig.mSize.x, loadConfig.mSize.y);
            }
            if (loadConfig.mTransformations != null && loadConfig.mTransformations.size() > 0) {
                requestCreator.transform((Transformation) loadConfig.mTransformations);
            }
            if (extendedOptions != null) {
                extendedOptions.onOptionsInit(requestCreator);
            }
        }

        return requestCreator;
    }
}
