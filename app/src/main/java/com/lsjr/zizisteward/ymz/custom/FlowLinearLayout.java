package com.lsjr.zizisteward.ymz.custom;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.utils.DensityUtil;

import java.util.TimerTask;

import static android.widget.LinearLayout.LayoutParams.*;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/6/10 12:57
 */

public class FlowLinearLayout extends LinearLayout {

    protected Context mContext;
    private int mColumns;
    private int mRows;
    private int mTotalWidth;
    private boolean mIsFirst = true;
    private int mColumeHeight = 50;
    private static final float DEFUALT_SPACING = 1f;
    private float mSpacing = DEFUALT_SPACING;
    private int mCount;

    public FlowLinearLayout(Context context) {
        super(context);
        addLayout(1);
    }

    public FlowLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addLayout(1);
    }

    public FlowLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addLayout(1);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mTotalWidth = right - left;
        if (mIsFirst) {
            notifyDataSetChanged();
            mIsFirst = false;
        }
    }


    public void addLayout(int count) {
        this.mCount = count;
        if (count == 0) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
        if (!mIsFirst) {
            notifyDataSetChanged();
        }
    }


    public void notifyDataSetChanged() {
        post(new TimerTask() {
            @Override
            public void run() {
                refreshLayou();
            }
        });
    }

    private void layoutParams() {
        int singleHeight = mColumeHeight;
        //根据子view数量确定高度
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = (int) (singleHeight * mRows + mSpacing * (mRows - 1));
        setLayoutParams(params);
    }


    private void addOneItmeView() {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = DensityUtil.dip2px(getContext(),mColumeHeight);
        params.width = MATCH_PARENT;
        setLayoutParams(params);
        addView(oneItem());
    }

    public LinearLayout oneItem() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(HORIZONTAL);
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.ic_launcher);
        TextView textView = new TextView(getContext());
        textView.setText("一张图片");
        LayoutParams layoutParams=new LayoutParams(MATCH_PARENT,50);
        linearLayout.addView(imageView,layoutParams);
        linearLayout.addView(textView,layoutParams);
        linearLayout.setLayoutParams(layoutParams);
        return linearLayout;
    }

    private void addTwoItmeView() {
        setOrientation(HORIZONTAL);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = 150;
        params.width = MATCH_PARENT;
        setLayoutParams(params);
        addView(oneItem(),params);

    }


    private void refreshLayou() {
        switch (mCount) {
            case 1:
                addOneItmeView();
                break;
            case 2:
                addTwoItmeView();
                break;
        }
    }

}
