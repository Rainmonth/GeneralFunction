package com.rainmonth.function.widget;

import android.view.TextureView;

/**
 * @author 张豪成
 * @date 2024/5/29 16:51
 */
public class AutoFitTextureView extends TextureView {
    private int mRatioWidth = 0;
    private int mRatioHeight = 0;
    private boolean mIsAutoFit = true;

    public AutoFitTextureView(android.content.Context context) {
        this(context, null);
    }

    public AutoFitTextureView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoFitTextureView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAspectRatio(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    public void setAutoFit(boolean isAutoFit) {
        mIsAutoFit = isAutoFit;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (mIsAutoFit) {
            if (0 == mRatioWidth || 0 == mRatioHeight) {
                setMeasuredDimension(width, height);
            } else {
                if (width < height * mRatioWidth / mRatioHeight) {
                    setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
                } else {
                    setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
                }
            }
        }
    }
}
