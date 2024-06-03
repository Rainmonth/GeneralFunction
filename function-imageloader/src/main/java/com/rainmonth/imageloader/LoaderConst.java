package com.rainmonth.imageloader;

import androidx.annotation.IntDef;

/**
 * @author randy
 * @date 2021/06/08 1:59 PM
 */
public interface LoaderConst {
    interface CacheClearType {
        /**
         * 清楚所有所有缓存
         */
        int CLEAR_ALL_CACHE = 1;
        /**
         * 清楚内存中的缓存
         */
        int CLEAR_MEM_CACHE = 2;
        /**
         * 清楚磁盘中的缓存
         */
        int CLEAR_DISK_CACHE = 3;
    }


    /**
     * Fresco
     */
    int LOADER_TYPE_FRESCO = 1;
    /**
     * Picasso
     */
    int LOADER_TYPE_PICASSO = 2;
    /**
     * Glide
     */
    int LOADER_TYPE_GLIDE = 3;
    /**
     * UIL
     */
    int LOADER_TYPE_UIL = 4;


    @IntDef({LOADER_TYPE_FRESCO, LOADER_TYPE_PICASSO, LOADER_TYPE_GLIDE, LOADER_TYPE_UIL})
    @interface LoaderType {
    }

}
