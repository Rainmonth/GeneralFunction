package com.rainmonth.imageloader.strategy.fresco;

import android.graphics.Point;
import android.graphics.PostProcessor;

import com.rainmonth.imageloader.LoadConfig;

/**
 * 1. 支持设置加载占位图、错误占位图
 * 2. 支持展示Gif动图
 * 3. 支持缓存（二级内存缓存+一级磁盘缓存）
 *
 * @author randy
 * @date 2021/06/04 1:44 PM
 */
public class FrescoLoadConfig extends LoadConfig {


    // 使用建造者模式创建这个类的实例
    public static class Builder {
        private final FrescoLoadConfig frescoLoadConfig;

        public Builder() {
            frescoLoadConfig = new FrescoLoadConfig();
        }

        public Builder setSize(Point size) {
            frescoLoadConfig.mSize = size;
            return this;
        }

        public Builder setTransformations(PostProcessor transformation) {
            frescoLoadConfig.mTransformations.add(transformation);
            return this;
        }

        public Builder setPlaceholderId(int placeholderId) {
            frescoLoadConfig.placeholderId = placeholderId;
            return this;
        }

        public Builder setErrorId(int errorId) {
            frescoLoadConfig.errorId = errorId;
            return this;
        }

        public Builder setUri(String uri) {
            frescoLoadConfig.mUri = uri;
            return this;
        }

        public Builder setPlayGif(boolean playGif) {
            frescoLoadConfig.isPlayGif = playGif;
            return this;
        }

        public Builder setCircle(boolean circle) {
            frescoLoadConfig.isCircle = circle;
            return this;
        }

        public FrescoLoadConfig build() {
            return frescoLoadConfig;
        }
    }

}
