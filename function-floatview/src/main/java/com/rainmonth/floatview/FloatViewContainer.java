package com.rainmonth.floatview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rainmonth.utils.log.LogUtils;

/**
 * 悬浮View容器
 */
public class FloatViewContainer<T extends View> extends FrameLayout {

    private static final String TAG = "FloatViewContainer";
    private boolean isDragMode = false;


    private float lastX, lastY;

    public FloatViewContainer(@NonNull Context context) {
        this(context, null);
    }

    public FloatViewContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatViewContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (getInnerViewLayoutId() != -1) {
            View view = View.inflate(context, getInnerViewLayoutId(), this);
            bindView(view);
        } else {
            LogUtils.w(TAG, "init(), not specify layoutId!");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isDragMode) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handleActionDown(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    handleActionMove(event);
                    break;
                case MotionEvent.ACTION_UP:
                    handleActionUp(event);
                    break;
            }
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    public int getInnerViewLayoutId() {
        return -1;
    }

    public void bindView(View view) {

    }

    private void handleActionDown(MotionEvent event) {
        event.getX();
    }

    private void handleActionMove(MotionEvent event) {

    }

    private void handleActionUp(MotionEvent event) {

    }
}
