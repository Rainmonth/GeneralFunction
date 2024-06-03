package com.rainmonth.imageloader.strategy.glide;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.Transformation;
import com.rainmonth.imageloader.LoadConfig;

/**
 * @author randy
 * @date 2021/06/04 1:44 PM
 */
public class GlideLoadConfig extends LoadConfig {


    // 使用建造者模式创建这个类的实例
    public static class Builder {
        private final GlideLoadConfig glideLoadConfig;

        public Builder() {
            glideLoadConfig = new GlideLoadConfig();
        }

        public Builder setUri(String uri) {
            glideLoadConfig.mUri = uri;
            return this;
        }

        public Builder setSize(Point size) {
            glideLoadConfig.mSize = size;
            return this;
        }

        public Builder setTransformations(Transformation<Bitmap> transformation) {
            glideLoadConfig.mTransformations.add(transformation);
            return this;
        }

        public Builder setErrorId(int errorId) {
            glideLoadConfig.errorId = errorId;
            return this;
        }

        public Builder setPlaceholderId(int placeholderId) {
            glideLoadConfig.placeholderId = placeholderId;
            return this;
        }

        public Builder setCircle(boolean circle) {
            glideLoadConfig.isCircle = circle;
            return this;
        }

        public Builder setPlayGif(boolean playGif) {
            glideLoadConfig.isPlayGif = playGif;
            return this;
        }

        public GlideLoadConfig build() {
            return glideLoadConfig;
        }
    }

}
