package com.rainmonth.imageloader.strategy.uil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.rainmonth.imageloader.ILoadStrategy;
import com.rainmonth.imageloader.LoadConfig;
import com.rainmonth.imageloader.LoaderConst;
import com.rainmonth.utils.Utils;

import java.io.File;
import java.util.List;

/**
 * 使用 UniversalImageLoader 加载
 *
 * @author randy
 * @date 2021/06/04 11:56 AM
 */
public class UilLoadStrategy implements ILoadStrategy {
    private static final long DEFAULT_DISK_CACHE_SIZE = 256 * 1024 * 1024L;
    private ImageLoaderConfiguration mConfiguration;
    private DisplayImageOptions mDisplayImageOptions;

    public UilLoadStrategy() {
        if (mConfiguration == null) {
            mConfiguration = getImageLoaderConfiguration();
        }
        ImageLoader.getInstance().init(mConfiguration);
    }

    private ImageLoaderConfiguration getImageLoaderConfiguration() {
        if (mDisplayImageOptions == null) {
            mDisplayImageOptions = getDefaultOptions(null);
        }
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(Utils.getApp())
                .threadPoolSize(3) // 线程池配置
                .threadPriority(3) // 线程优先级配置
                .denyCacheImageMultipleSizesInMemory() // 防止缓存多套图片到内存中
                .memoryCache(new WeakMemoryCache()) // 使用弱引用内存缓存
                .diskCacheSize((int) DEFAULT_DISK_CACHE_SIZE) // 磁盘缓存大小
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) // 使用md5命名缓存文件
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .defaultDisplayImageOptions(mDisplayImageOptions)
                .build();
        return configuration;
    }

    private DisplayImageOptions getDefaultOptions(LoadConfig loadConfig) {
        DisplayImageOptions options = null;
        if (loadConfig == null) {
            options = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri()
//                .showImageOnFail()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .decodingOptions(new BitmapFactory.Options())
                    .resetViewBeforeLoading(true)
                    .build();
        } else {
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(loadConfig.placeholderId)
                    .showImageOnFail(loadConfig.errorId)
                    .cacheOnDisk(true)
                    .cacheInMemory(true)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .decodingOptions(new BitmapFactory.Options())
                    .resetViewBeforeLoading(true)
                    .build();
        }
        return options;
    }

    @Override
    public void loadImage(LoadConfig loadConfig, View view, Callback callback, ExtendedOptions extendOption) {
        if (!(view instanceof ImageView)) {
            throw new IllegalArgumentException("view must be ImageView");
        }
        ImageSize imageSize;
        DisplayImageOptions options;
        if (loadConfig.mSize != null) {
            imageSize = new ImageSize(loadConfig.mSize.x, loadConfig.mSize.y);
            ImageLoader.getInstance().displayImage(loadConfig.mUri, (ImageView) view, imageSize);
        } else {
            options = getDefaultOptions(loadConfig);
            ImageLoader.getInstance().displayImage(loadConfig.mUri, (ImageView) view, options);
        }
    }

    @Override
    public void clearCache(int type) {
        if (type == LoaderConst.CacheClearType.CLEAR_ALL_CACHE) {
            ImageLoader.getInstance().clearDiskCache();
            ImageLoader.getInstance().clearMemoryCache();
        }
        if (type == LoaderConst.CacheClearType.CLEAR_MEM_CACHE) {
            ImageLoader.getInstance().clearMemoryCache();
        }
        if (type == LoaderConst.CacheClearType.CLEAR_DISK_CACHE) {
            ImageLoader.getInstance().clearDiskCache();
        }
    }

    @Override
    public void clearCacheKey(int type, LoadConfig loadConfig) {
        if (type == LoaderConst.CacheClearType.CLEAR_ALL_CACHE) {
            MemoryCacheUtils.removeFromCache(loadConfig.mUri,
                    ImageLoader.getInstance().getMemoryCache());
            DiskCacheUtils.removeFromCache(loadConfig.mUri,
                    ImageLoader.getInstance().getDiskCache());
        }
        if (type == LoaderConst.CacheClearType.CLEAR_MEM_CACHE) {
            MemoryCacheUtils.removeFromCache(loadConfig.mUri,
                    ImageLoader.getInstance().getMemoryCache());
        }
        if (type == LoaderConst.CacheClearType.CLEAR_DISK_CACHE) {
            DiskCacheUtils.removeFromCache(loadConfig.mUri,
                    ImageLoader.getInstance().getDiskCache());
        }
    }

    @Override
    public boolean isCache(LoadConfig loadConfig, ExtendedOptions extendedOption) {
        File cacheFile = DiskCacheUtils.findInCache(loadConfig.mUri,
                ImageLoader.getInstance().getDiskCache());
        return cacheFile != null;
    }

    @Override
    public File getLocalCache(LoadConfig loadConfig, ExtendedOptions extendOption) {
        return DiskCacheUtils.findInCache(loadConfig.mUri,
                ImageLoader.getInstance().getDiskCache());
    }

    @Override
    public Bitmap getLocalCacheBitmap(LoadConfig loadConfig, ExtendedOptions extendedOptions) {
        return getDiskCachedBitmap(loadConfig, extendedOptions);
    }

    /**
     * 获取内存中缓存的 Bitmap
     *
     * @param loadConfig      loadConfig
     * @param extendedOptions extendedOptions
     * @return 内存中缓存的 Bitmap
     */
    private Bitmap getMemCachedBitmap(LoadConfig loadConfig, ExtendedOptions extendedOptions) {
        List<String> memCachedList = MemoryCacheUtils.findCacheKeysForImageUri(loadConfig.mUri,
                ImageLoader.getInstance().getMemoryCache());
        if (memCachedList.size() > 0) {
            return ImageLoader.getInstance().getMemoryCache().get(memCachedList.get(0));
        }
        return null;
    }

    /**
     * 获取文件中缓存的 Bitmap
     *
     * @param loadConfig      loadConfig
     * @param extendedOptions extendedOptions
     * @return 文件中缓存的 Bitmap
     */
    private Bitmap getDiskCachedBitmap(LoadConfig loadConfig, ExtendedOptions extendedOptions) {
        File file = getLocalCache(loadConfig, extendedOptions);
        try {
            String filePath = file.getPath();
            // todo  优化处理
            return BitmapFactory.decodeFile(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getCacheSize(LoadConfig loadConfig) {
        // todo 获取缓存大小
        return 0;
    }

    @Override
    public void downloadOnly(LoadConfig loadConfig, Callback callback,
                             ExtendedOptions extendOption) {
        ImageSize imageSize;
        DisplayImageOptions options;
        ImageLoadingListener listener = new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if (callback != null) {
                    callback.onStart();
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (callback != null) {
                    callback.onFail(new Exception(failReason.getCause()));
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (callback != null) {
                    callback.onSuccess(loadedImage);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        };
        if (loadConfig.mSize != null) {
            imageSize = new ImageSize(loadConfig.mSize.x, loadConfig.mSize.y);
            ImageLoader.getInstance().loadImage(loadConfig.mUri, imageSize, listener);
        } else {
            options = getDefaultOptions(loadConfig);
            ImageLoader.getInstance().loadImage(loadConfig.mUri, options, listener);
        }
    }
}
