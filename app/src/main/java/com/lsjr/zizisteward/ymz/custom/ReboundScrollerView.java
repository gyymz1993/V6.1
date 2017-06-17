package com.lsjr.zizisteward.ymz.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ScrollView;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/6/15 10:32
 */

public class ReboundScrollerView extends ScrollView {

    private Context mContext;
    private int mMaxYoverScrollDistance;
    private static final int MAX_Y_OVERSCROLL_DISTANNE = 500;

    public ReboundScrollerView(Context context) {
        this(context, null);
    }

    public ReboundScrollerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReboundScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initBoundScrollView();
    }

    private void initBoundScrollView() {
        final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        final float disity = metrics.density;
        mMaxYoverScrollDistance = (int) (disity * MAX_Y_OVERSCROLL_DISTANNE);

    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYoverScrollDistance, isTouchEvent);
    }
}
