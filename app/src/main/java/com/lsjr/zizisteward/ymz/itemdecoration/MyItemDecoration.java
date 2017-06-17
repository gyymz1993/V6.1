package com.lsjr.zizisteward.ymz.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MyItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * 复写onDraw方法，从而达到在每隔条目的被绘制之前（或之后），让他先帮我们画上去几根线吧
     *
     * @param
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //先初始化一个Paint来简单指定一下Canvas的颜色，就黑的吧！

       // onDrawLine(c, parent);
        onDraw(c,parent);
        super.onDraw(c, parent, state);
    }


    public void onDrawLine(Canvas c, RecyclerView parent) {
        Paint paint = new Paint();
        paint.setColor(parent.getContext().getResources().getColor(android.R.color.black));

        //获得RecyclerView中总条目数量
        int childCount = parent.getChildCount();

        //遍历一下
        for (int i = 0; i < childCount; i++) {
            if (i == 0) {
                //如果是第一个条目，那么我们就不画边框了
                continue;
            }
            //获得子View，也就是一个条目的View，准备给他画上边框
            View childView = parent.getChildAt(i);

            //先获得子View的长宽，以及在屏幕上的位置，方便我们得到边框的具体坐标
            float x = childView.getX();
            float y = childView.getY();
            int width = childView.getWidth();
            int height = childView.getHeight();

            //根据这些点画条目的四周的线
            c.drawLine(x, y, x + width, y, paint);
            c.drawLine(x, y, x, y + height, paint);
            c.drawLine(x + width, y, x + width, y + height, paint);
            c.drawLine(x, y + height, x + width, y + height, paint);

            //当然了，这里大家肯定是要根据自己不同的设计稿进行画线，或者画一些其他的东西，都可以在这里搞，非常方便
        }
    }

    // 要提高精度可以使用float
    //圆的半径
    private int radius = 100;
    //圆之间的间距
    private int gap = 100;

    public void onDraw(Canvas canvas, RecyclerView parent) {
        Paint paint = new Paint();
        paint.setColor(parent.getContext().getResources().getColor(android.R.color.black));
        gap = 100;
        // 获取宽度与高度
        // int width = getWidth();
        // int height = getHeight();


        //获得RecyclerView中总条目数量
        int childCount = parent.getChildCount();
        //遍历一下
        for (int i = 0; i < childCount; i++) {
            if (i == 0) {
                //如果是第一个条目，那么我们就不画边框了
                continue;
            }
            //获得子View，也就是一个条目的View，准备给他画上边框
            View childView = parent.getChildAt(i);

            //先获得子View的长宽，以及在屏幕上的位置，方便我们得到边框的具体坐标
            float x = childView.getX();
            float y = childView.getY();
            int width = childView.getWidth();
            int height = childView.getHeight();


            //圆的个数
            int count;
            try {
                // 计算可以显示多少个圆
                count = (width - gap) / (radius * 2 + gap);
            } catch (Exception e) {
                count = 0;
                // Log.e(TAG, "onDraw: ", e);
            }
            // 圆的直径
            int h = (radius * 2);
            // 圆以外的长度
            int cgap = (width - (count * h));
            // 两侧端点的长度，
            int x1 = (cgap + gap - (gap * count)) / 2;
            // 绘制
            for (int j = 0; j < count; j++) {
                int z = x1 + radius + (h + gap) * j;
                canvas.drawCircle(z, 0, radius, paint);
                canvas.drawCircle(z, height, radius, paint);
            }

            //根据这些点画条目的四周的线
//            c.drawLine(x, y, x + width, y, paint);
//            c.drawLine(x, y, x, y + height, paint);
//            c.drawLine(x + width, y, x + width, y + height, paint);
//            c.drawLine(x, y + height, x + width, y + height, paint);

            //当然了，这里大家肯定是要根据自己不同的设计稿进行画线，或者画一些其他的东西，都可以在这里搞，非常方便
        }

    }
}

