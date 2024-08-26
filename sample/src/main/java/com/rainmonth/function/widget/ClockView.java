package com.rainmonth.function.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.rainmonth.utils.log.LogUtils;

import java.util.Calendar;

public class ClockView extends View {

    private Paint mBackgroundPaint, mHourHandPaint, mMinuteHandPaint, mSecondHandPaint, mTickPaint;
    private TextPaint mTextPaint;
    private RectF mCircleRect;
    private float mCenterX, mCenterY;
    private int mRadius;
    private Handler mHandler;

    private Calendar mTime;

    private boolean mAutoUpdate = false;

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(Color.WHITE);

        mHourHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHourHandPaint.setColor(Color.RED);

        mMinuteHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMinuteHandPaint.setColor(Color.GREEN);

        mSecondHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondHandPaint.setColor(Color.BLUE);

        mTickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTickPaint.setColor(Color.BLACK);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(30);

        mCircleRect = new RectF();

        mTime = Calendar.getInstance();

        mHandler = new Handler();
        mHandler.postDelayed(this::updateTime, 1000);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2f;
        mCenterY = h / 2f;
        mRadius = Math.min(w, h) / 2 - 10; // 减去边距
        mCircleRect.set(mCenterX - mRadius, mCenterY - mRadius, mCenterX + mRadius, mCenterY + mRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制背景圆
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mBackgroundPaint);

        // 绘制刻度
        drawTicks(canvas);

        // 绘制数字
        drawNumbers(canvas);

        // 绘制时针
        drawHand(canvas, mHourHandPaint, getHourAngle(), mRadius * 0.5f);

        // 绘制分针
        drawHand(canvas, mMinuteHandPaint, getMinuteAngle(), mRadius * 0.7f);

        // 绘制秒针
        drawHand(canvas, mSecondHandPaint, getSecondAngle(), mRadius * 0.8f);
    }

    private void drawTicks(Canvas canvas) {
        for (int i = 0; i < 60; i++) {
            float angle = (i * 6) - 90; // 起始角度为-90度
            float length = i % 5 == 0 ? mRadius * 0.85f : mRadius * 0.9f; // 长短刻度
            float startX = (float) (mCenterX + length * Math.sin(Math.toRadians(angle)));
            float startY = (float) (mCenterY - length * Math.cos(Math.toRadians(angle)));
            float endX = (float) (mCenterX + mRadius * Math.sin(Math.toRadians(angle)));
            float endY = (float) (mCenterY - mRadius * Math.cos(Math.toRadians(angle)));
            canvas.drawLine(startX, startY, endX, endY, mTickPaint);
        }
    }

    private void drawNumbers(Canvas canvas) {
        String[] numbers = {"12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
        float textHeight = mTextPaint.descent() - mTextPaint.ascent();
        for (int i = 0; i < 12; i++) {
            float angle = (i * 30); // 起始角度为-90度
            float x = (float) (mCenterX + (mRadius * 0.75f) * Math.sin(Math.toRadians(angle)));
            float y = (float) (mCenterY - (mRadius * 0.75f) * Math.cos(Math.toRadians(angle)));
            canvas.drawText(numbers[i], x - mTextPaint.measureText(numbers[i]) / 2, y + textHeight / 2, mTextPaint);
        }
    }

    private void drawHand(Canvas canvas, Paint paint, float angle, float length) {
        paint.setStrokeWidth(5); // 设置画笔宽度
        float endX = (float) (mCenterX + length * Math.sin(Math.toRadians(angle)));
        float endY = (float) (mCenterY - length * Math.cos(Math.toRadians(angle)));
        canvas.drawLine(mCenterX, mCenterY, endX, endY, paint);
    }

    private float getHourAngle() {
        if (mTime == null) {
            mTime = Calendar.getInstance();
        }
        int hour = mTime.get(Calendar.HOUR_OF_DAY);
        return (hour % 12) * 30 + (mTime.get(Calendar.MINUTE) / 2f);
    }

    private float getMinuteAngle() {
        if (mTime == null) {
            mTime = Calendar.getInstance();
        }
        return mTime.get(Calendar.MINUTE) * 6;
    }

    private float getSecondAngle() {
        if (mTime == null) {
            mTime = Calendar.getInstance();
        }
        return mTime.get(Calendar.SECOND) * 6;
    }


    /**
     * 设置时间并重绘视图。
     *
     * @param time 时间
     */
    public void setTime(Calendar time) {
        if (mTime == null) {
            mTime = Calendar.getInstance();
        }
        mTime.setTimeInMillis(time.getTimeInMillis());
        setAutoUpdate(mAutoUpdate);
    }

    private void updateTime() {
        invalidate(); // 重新绘制视图
        addTimeByOneSecond();
        mHandler.removeCallbacks(mUpdateRunnable);
        mHandler.postDelayed(mUpdateRunnable, 1000);
    }

    private final Runnable mUpdateRunnable = this::updateTime;

    public void setAutoUpdate(boolean autoUpdate) {
        this.mAutoUpdate = autoUpdate;
        if (autoUpdate) {
            updateTime();
        } else {
            mHandler.removeCallbacks(mUpdateRunnable);
            invalidate();
        }
    }

    private void addTimeByOneSecond() {
        LogUtils.w("ClockView", "addTimeByOneSecond");
        if (mTime == null) {
            mTime = Calendar.getInstance();
        }
        mTime.add(Calendar.SECOND, 1);
    }
}
