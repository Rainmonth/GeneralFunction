package com.rainmonth.function.roundhelper;

/**
 * 设置圆角及边框相应的方法
 */
public interface RoundMethodInterface {
    void setRadius(float radiusDp);

    void setRadius(float radiusTopLeftDp, float radiusTopRightDp, float radiusBottomLeftDp, float radiusBottomRightDp);

    void setRadiusLeft(float radiusDp);

    void setRadiusRight(float radiusDp);

    void setRadiusTop(float radiusDp);

    void setRadiusBottom(float radiusDp);

    void setRadiusTopLeft(float radiusDp);

    void setRadiusTopRight(float radiusDp);

    void setRadiusBottomLeft(float radiusDp);

    void setRadiusBottomRight(float radiusDp);

    void setStrokeWidth(float widthDp);

    void setStrokeColor(int color);

    void setStrokeWidthColor(float widthDp, int color);
}
