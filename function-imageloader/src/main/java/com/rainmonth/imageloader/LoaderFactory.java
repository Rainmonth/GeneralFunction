package com.rainmonth.imageloader;

import com.rainmonth.imageloader.strategy.fresco.FrescoLoadStrategy;
import com.rainmonth.imageloader.strategy.glide.GlideLoadStrategy;
import com.rainmonth.imageloader.strategy.picasso.PicassoLoadStrategy;
import com.rainmonth.imageloader.strategy.uil.UilLoadStrategy;

/**
 * @author 张豪成
 * @date 2024/5/31 15:54
 */
public class LoaderFactory {
    public static ILoadStrategy getLoadStrategy(@LoaderConst.LoaderType int type) {
        switch (type) {
            default:
            case LoaderConst.LOADER_TYPE_FRESCO:
                return new FrescoLoadStrategy(null);
            case LoaderConst.LOADER_TYPE_PICASSO:
                return new PicassoLoadStrategy(null);
            case LoaderConst.LOADER_TYPE_GLIDE:
                return new GlideLoadStrategy();
            case LoaderConst.LOADER_TYPE_UIL:
                return new UilLoadStrategy();
        }
    }
}
