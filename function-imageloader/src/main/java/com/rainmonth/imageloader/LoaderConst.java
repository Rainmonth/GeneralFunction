package com.rainmonth.imageloader;

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

}
