package com.rainmonth.utils;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.rainmonth.utils.log.LogUtils;

/**
 * 提供方便的获取各种资源的工具类
 *
 * @author 张豪成
 * @date 2019-11-20 14:27
 */
public class ResUtil {
    private static final String TAG = "ResUtil";

    public static String getString(int strResId) {
        try {
            return Utils.getApp().getString(strResId);
        } catch (Exception e) {
            LogUtils.printStackTrace(TAG, e);
        }
        return "";
    }

    public static int getColor(int colorResId) {
        try {
            return Utils.getApp().getResources().getColor(colorResId);
        } catch (Exception e) {
            LogUtils.printStackTrace(TAG, e);
        }
        return 0;
    }

    public static Drawable getDrawable(int drawableResId) {
        try {
            return Utils.getApp().getResources().getDrawable(drawableResId);
        } catch (Exception e) {
            LogUtils.printStackTrace(TAG, e);
        }

        return null;
    }

    public static int getInteger(int integerResId) {
        return getInteger(integerResId, 0);
    }

    public static int getInteger(int integerResId, int defaultValue) {
        try {
            return Utils.getApp().getResources().getInteger(integerResId);
        } catch (Exception e) {
            LogUtils.printStackTrace(TAG, e);
        }
        return defaultValue;
    }

    public static boolean getBoolean(int booleanResId) {
        try {
            return Utils.getApp().getResources().getBoolean(booleanResId);
        } catch (Exception e) {
            LogUtils.printStackTrace(TAG, e);
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static float getFloat(int floatResId) {
        try {
            return Utils.getApp().getResources().getFloat(floatResId);
        } catch (Exception e) {
            LogUtils.printStackTrace(TAG, e);
        }
        return 0;
    }

    public static Uri getRawUri(int rawId) {
        return Uri.parse("android.resource://" + Utils.getApp().getPackageName() + "/" + rawId);
    }

    public static int[] getIntArray(int intArrayResId) {
        try {
            return Utils.getApp().getResources().getIntArray(intArrayResId);
        } catch (Exception e) {
            LogUtils.printStackTrace(TAG, e);
        }
        return null;
    }

    public static String[] getStringArray(int stringArrayResId) {
        try {
            return Utils.getApp().getResources().getStringArray(stringArrayResId);
        } catch (Exception e) {
            LogUtils.printStackTrace(TAG, e);
        }
        return null;
    }

    public static int gerOrientation() {
        return Utils.getApp().getResources().getConfiguration().orientation;
    }
}
