package com.rainmonth.imageloader.strategy.glide;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.rainmonth.imageloader.ILoadStrategy;

import java.io.File;

/**
 * @author randy
 * @date 2021/06/08 5:05 PM
 */
public class GlideLoadTarget extends CustomTarget<File> {

    private ILoadStrategy.Callback mCallback;

    public GlideLoadTarget(ILoadStrategy.Callback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void onLoadStarted(@Nullable Drawable placeholder) {
        super.onLoadStarted(placeholder);
        if (mCallback != null) {
            mCallback.onStart();
        }
    }

    @Override
    public void onLoadFailed(@Nullable Drawable errorDrawable) {
        super.onLoadFailed(errorDrawable);
        if (mCallback != null) {
            mCallback.onFail(null);
        }
    }

    @Override
    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
        if (mCallback != null) {
            mCallback.onSuccess(resource);
        }
    }

    @Override
    public void onLoadCleared(@Nullable Drawable placeholder) {

    }
}
