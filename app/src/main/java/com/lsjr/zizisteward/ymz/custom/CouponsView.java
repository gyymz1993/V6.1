package com.lsjr.zizisteward.ymz.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by 折扣卷布局 on 2016/5/17.
 */
public class CouponsView extends LinearLayout {
    //圆的半径
    private int radius = 20;
    //间隔长度
    private int mLength = 8;
    //画笔
    private Paint mPaint;
    //圆的个数
    private int mCircleNum;
    //不能整除时的间隔
    private int remain;

    public CouponsView(Context context) {
        super(context);
        init();
    }

    public CouponsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CouponsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        remain = (w - mLength) % (2 * radius + mLength);
        mCircleNum = (w - mLength) / (2 * radius + mLength);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mCircleNum; i++) {
            float x = mLength + radius + remain / 2 + ((mLength + radius * 2) * i);
            canvas.drawCircle(x, 0, radius, mPaint);
            canvas.drawCircle(x, getHeight(), radius, mPaint);
        }
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{24, 12}, 0));
        paint.setStrokeWidth(6);
//        Path path=new Path();
//        path.moveTo(remain/2+mLength+2*radius, 2*radius);
//        path.lineTo(remain/2+mLength+2*radius, getHeight()-2*radius);
//        canvas.drawPath(path, paint);
//
//        Path path2=new Path();
//        path2.moveTo(getWidth()-remain/2-mLength-2*radius,2*radius);
//        path2.lineTo(getWidth()-remain/2-mLength-2*radius,getHeight()-2*radius);
//        canvas.drawPath(path2, paint);

        Path path3=new Path();
        path3.moveTo(remain/2+mLength, 2*radius);
        path3.lineTo(remain/2+mLength, getHeight()-2*radius);
        path3.lineTo(getWidth()-remain/2-mLength,getHeight()-2*radius);
        path3.lineTo(getWidth()-remain/2-mLength,2*radius);
        path3.close();
        canvas.drawPath(path3, paint);

    }
}