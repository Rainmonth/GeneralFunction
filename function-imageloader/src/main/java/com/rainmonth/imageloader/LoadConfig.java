package com.rainmonth.imageloader;

import android.graphics.Point;
import android.view.View;

import androidx.annotation.DrawableRes;

import java.util.ArrayList;

/**
 * 图片加载配置
 *
 * @author randy
 * @date 2021/06/04 11:39 AM
 */
public class LoadConfig {
    //默认图片
    public int placeholderId;

    //错误图片
    public int errorId;

    //是否圆形
    public boolean isCircle = false;

    //是否播放gif
    public boolean isPlayGif = false;

    //大小
    public Point mSize = null;

    //图片
    public String mUri;

    public ArrayList<Object> mTransformations = new ArrayList<>();

}
