package com.rainmonth.floatview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.rainmonth.utils.log.LogUtils;

/**
 * 字卡View
 * 文字内容会根据View的宽度自动调整
 *
 * @author 张豪成
 * @date 2021/03/03
 */
public class KaDaWordCardView extends FrameLayout {


    public KaDaWordCardView(Context context) {
        this(context, null);
    }

    public KaDaWordCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context,R.layout.floatview_word_card, this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LogUtils.d("Randy", "w: " + w + ",h: " + h);
        LogUtils.d("Randy", "oldw: " + w + ",oldh: " + h);
    }
}
