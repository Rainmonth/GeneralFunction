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
        LogUtils.i("xx", "handleActionDown");
        mOriginalX = getX();
        mOriginalY = getY();
        mOriginalRawX = event.getRawX();
        mOriginalRawY = event.getRawY();
        mLastTouchDownTime = System.currentTimeMillis();
    }

    private void handleActionMove(MotionEvent event) {
        LogUtils.i("xx", "handleActionMove");
        float desX = mOriginalX + event.getRawX() - mOriginalRawX;
        float desY = mOriginalY + event.getRawY() - mOriginalRawY;

        fixPositionWhileMoving(desX, desY);
    }

    private void fixPositionWhileMoving(float desX, float desY) {
        if (desX < -(getWidth() - mVisibleWidth)) {
            desX = -(getWidth() - mVisibleWidth);
        }
        if (desX > mScreenWidth - mVisibleWidth) {
            desX = mScreenWidth - mVisibleWidth;
        }
        if (mConfig == null) {
            return;
        }
        if (mConfig.isGlobalFloat) {
            WindowManager.LayoutParams params = (WindowManager.LayoutParams) getLayoutParams();
            params.x = (int) desX;
            params.y = (int) desY;
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
