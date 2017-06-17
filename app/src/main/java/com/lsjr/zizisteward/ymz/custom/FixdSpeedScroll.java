package com.lsjr.zizisteward.ymz.custom;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/6/15 9:13
 */

public class FixdSpeedScroll extends Scroller {

    private int mDucration=0;

    public FixdSpeedScroll(Context context) {
        super(context);
    }

    public FixdSpeedScroll(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public FixdSpeedScroll(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy,mDucration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDucration);
    }

    public void setmDucration(int ducration) {
        this.mDucration = ducration;
    }


}
