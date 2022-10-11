package com.rainmonth.floatview;

import static android.content.Context.WINDOW_SERVICE;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rainmonth.utils.DensityUtils;
import com.rainmonth.utils.SizeUtils;
import com.rainmonth.utils.ToastUtils;
import com.rainmonth.utils.Utils;
import com.rainmonth.utils.log.LogUtils;

/**
 * 悬浮View容器
 * 注意：
 * 1. 这个View可以直接通过WindowManager添加到Window上；
 * 2. 这个View可以添加到Activity的contentView上；
 * 上面这两种情况需要进行区分，不然会因为布局参数不同而发生ClassCastException。
 */
public class FloatViewContainer<T extends View> extends FrameLayout {

    private static final String TAG = "FloatViewContainer";
    private boolean isDragMode = true;


    private float mDeltaX, mDeltaY;
    private float mInViewX, mInViewY, mInScreenX, mInScreenY;
    private long mLastTouchDownTime;                                // 上次按下的时间
    private float mStatusBarHeight;                                 // 状态栏高度
    private float mScreenWidth, mScreenHeight;                      // 屏幕宽高
    private float mVisibleWidth;                                    // 收起来时可见宽度

    private WindowManager mManager;

    private FloatViewConfig mConfig;

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
        mScreenWidth = DensityUtils.getDisplayWidth();
        mScreenHeight = DensityUtils.getDisplayHeight();
        mVisibleWidth = SizeUtils.dp2px(35);
        LogUtils.d(TAG, "init()->w:" + mScreenWidth + ",h:" + mScreenHeight);
        mStatusBarHeight = DensityUtils.getStatusBarHeight();
        mManager = (WindowManager) Utils.getApp().getSystemService(WINDOW_SERVICE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtils.i(TAG, "onTouchEvent");
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
            // todo 这里不能直接通过 return true 来处理，因为子View可能需要单独处理点击事件
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    public void bindView(FloatViewConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("config should not be null!");
        }
        this.mConfig = config;
        if (config.itemViewRes == -1) {
            // todo
            throw new IllegalArgumentException("must provide a itemViewRes!");
        }
        isDragMode = config.isDragMode;
        View.inflate(getContext(), config.itemViewRes, this);
    }

    /**
     * 全局模式和应用内模式采用不同的策略处理，因为二者实现方式是不同的，全局是更新窗口的参数，而应用内是更新
     * 对应View的位置
     */
    private void handleActionDown(MotionEvent event) {
        // 这里不能新建，只能从已有的哪里进行转换
        if (mWindowLayoutParam == null && getLayoutParams() instanceof WindowManager.LayoutParams) {
            mWindowLayoutParam = (WindowManager.LayoutParams) getLayoutParams();
        }
        mInViewX = event.getX();
        mInViewY = event.getY();
        mInScreenX = event.getRawX();
        mInScreenY = event.getRawY();
        mLastTouchDownTime = System.currentTimeMillis();
        LogUtils.w(TAG, "ACTION_DOWN, mInViewX:" + mInViewX + ", mInViewY:" +
                mInViewY + ", mInScreenX:" + mInScreenX + ", mInScreenY:" + mInScreenY);
    }

    private void handleActionMove(MotionEvent event) {
        mInScreenX = event.getRawX();
        mInScreenY = event.getRawY();
        float desX = mInScreenX - mInViewX;
        float desY = mInScreenY - mInViewY;
        LogUtils.i(TAG, "ACTION_MOVE, mInViewX:" + mInViewX + ", mInViewY:" +
                mInViewY + ", mInScreenX:" + mInScreenX + ", mInScreenY:" + mInScreenY);
        updateFloatViewPosition(desX, desY);
    }

    @Nullable
    private WindowManager.LayoutParams mWindowLayoutParam;

    /**
     * 全局播放器时，这里的修正方法需要修改
     */
    private void updateFloatViewPosition(float desX, float desY) {
        LogUtils.i(TAG, "updateFloatViewPosition, desX:" + desX + ", desY:" + desY +
                "，width:" + getWidth() + ", height:" + getHeight());
        if (mConfig == null) {
            return;
        }
        if (mConfig.isGlobalFloat) {
            if (mWindowLayoutParam == null) {
                return;
            }
            mWindowLayoutParam.x = (int) desX;
            mWindowLayoutParam.y = (int) desY;
            mManager.updateViewLayout(this, mWindowLayoutParam);
        } else {
            setX(desX);
            setY(desY);
        }
    }

    private void handleActionUp(MotionEvent event) {
        ToastUtils.showLong("Action Up!!!!");
    }
}
