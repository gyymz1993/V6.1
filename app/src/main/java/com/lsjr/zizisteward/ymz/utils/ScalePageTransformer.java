package com.lsjr.zizisteward.ymz.utils;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ScalePageTransformer implements ViewPager.PageTransformer {

    private static final float MAX_SCALE = 1.1f;
    private static final float MIN_SCALE = 0.7f;
    private static final float MIN_ALPHA = 0.5f;

    @Override
    public void transformPage(View page, float position) {

        if (position < -1) {
            position = -1;
            page.setAlpha(0);
        } else if (position > 1) {
            position = 1;
            page.setAlpha(0);
        }

        float tempScale = position < 0 ? 1 + position : 1 - position;

        float slope = (MAX_SCALE - MIN_SCALE) / 1;
        //一个公式
        float scaleValue = MIN_SCALE + tempScale * slope;
        page.setScaleX(scaleValue);
        page.setScaleY(scaleValue);

        // Fade the page relative to its size.
        float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
        page.setAlpha(MIN_ALPHA +
                (scaleFactor - MIN_SCALE) /
                        (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            page.getParent().requestLayout();
        }
    }
}