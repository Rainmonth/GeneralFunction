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
    private float mOriginalX, mOriginalY, mOriginalRawX, mOriginalRawY;
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
        LogUtils.i(TAG, "handleActionDown, ori(x,y):(" + getX() + "," + getY() + ")");
        updateFloatViewPositionCompat();
        mOriginalRawX = event.getRawX();
        mOriginalRawY = event.getRawY();
        mLastTouchDownTime = System.currentTimeMillis();
    }

    public void updateFloatViewPositionCompat() {
        if (mConfig == null) {
            LogUtils.w(TAG, "mConfig is null, please check!");
            return;
        }
        if (mConfig.isGlobalFloat) {
            mOriginalX = ((WindowManager.LayoutParams) getLayoutParams()).x;
            mOriginalY = ((WindowManager.LayoutParams) getLayoutParams()).y;
        } else {
            mOriginalX = getX();
            mOriginalY = getY();
        }

        LogUtils.i(TAG, "id:", mConfig.id, ", mOriginalX:", mOriginalX, ", mOriginalY", mOriginalY);
    }

    private void handleActionMove(MotionEvent event) {
        float desX = mOriginalX + event.getRawX() - mOriginalRawX;
        float desY = mOriginalY + event.getRawY() - mOriginalRawY;
        LogUtils.i(TAG, "handleActionMove, desX:" + desX + ", desY:" + desY);
        fixPositionWhileMoving(desX, desY);
    }

    /**
     * 全局播放器时，这里的修正方法需要修改
     */
    private void fixPositionWhileMoving(float desX, float desY) {
        if (desX < -(getWidth() - mVisibleWidth)) {
            desX = -(getWidth() - mVisibleWidth);
        }
        if (desX > mScreenWidth - mVisibleWidth) {
            desX = mScreenWidth - mVisibleWidth;
        }
        LogUtils.i(TAG, "fixPositionWhileMoving, des(x,y):(" + desX + "," + desY + ")" +
                "， w:" + getWidth() + ", h:" + getHeight());
        if (mConfig == null) {
            return;
        }
        if (mConfig.isGlobalFloat) {
            WindowManager.LayoutParams params = (WindowManager.LayoutParams) getLayoutParams();
            params.x = (int) desX;
            params.y = (int) desY;
            LogUtils.i(TAG, "fixPositionWhileMoving, set(x,y):(" + params.x + "," + params.y + ")");
            mManager.updateViewLayout(this, params);
        } else {
            setX(desX);
            if (desY >= 0 && desY <= mStatusBarHeight) {
//            if (!mIsAllowCoverStatusBar) {
//                desY = mStatusBarHeight;
//            }
            } else if (desY < 0) {
//            if (mIsAllowCoverStatusBar) {
//                desY = 0;
//            } else {
//                desY = mStatusBarHeight;
//            }
            } else if (desY >= mScreenHeight - getHeight()) {
//            desY = mScreenHeight - getHeight() - bottomStayEdge;
            }

//        LogHelper.d(TAG, "fixPositionWhileMoving()->x:" + desX + ",y:" + desY);
            setY(desY);
        }
    }

    private void handleActionUp(MotionEvent event) {
        ToastUtils.showLong("Action Up!!!!");
    }
}
