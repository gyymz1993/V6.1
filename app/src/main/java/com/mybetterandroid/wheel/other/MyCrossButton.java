package com.mybetterandroid.wheel.other;

import com.lsjr.zizisteward.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by zhouxb on 2016/3/15.
 */
public class MyCrossButton extends FrameLayout {
    private ImageView imageView;
    private ImageView leftView;
    private ImageView rightView;
    private Bitmap button;
    private boolean isPress = false;

    // 震动动画
    private Animation shakeAnimationLeft;
    private Animation shakeAnimationRight;
    // 插值器
    private CycleInterpolator cycleInterpolator;

//    private boolean isOk = false;

    public MyCrossButton(Context context) {
        this(context, null);
    }

    public MyCrossButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.button = BitmapFactory.decodeResource(getResources(), R.drawable.newtanchu);
        imageView = new ImageView(context);
        leftView = new ImageView(context);
        rightView = new ImageView(context);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                100,
//                100);
        addView(leftView);
        addView(rightView);
        addView(imageView);
        LayoutParams mHomeparams = (LayoutParams) imageView.getLayoutParams();
        mHomeparams.width = 100;
        mHomeparams.height = 100;
        mHomeparams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

        LayoutParams mLeftparams = (LayoutParams) leftView.getLayoutParams();
        mLeftparams.width = 100;
        mLeftparams.height = 100;
        mLeftparams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

        LayoutParams mrightparams = (LayoutParams) rightView.getLayoutParams();
        mrightparams.width = 100;
        mrightparams.height = 100;
        mrightparams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

        // 初始化震动动画
        shakeAnimationLeft = new TranslateAnimation(-150, -140, -250, -245);
        shakeAnimationLeft.setDuration(100);
        shakeAnimationLeft.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        cycleInterpolator = new CycleInterpolator(8);
        shakeAnimationLeft.setInterpolator(cycleInterpolator);

        shakeAnimationRight = new TranslateAnimation(150, 160, -250, -255);
        shakeAnimationRight.setDuration(100);
        shakeAnimationRight.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        cycleInterpolator = new CycleInterpolator(8);
        shakeAnimationRight.setInterpolator(cycleInterpolator);
        init();
    }

    public MyCrossButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        leftView.setImageBitmap(button);
        rightView.setImageBitmap(button);
        imageView.setImageBitmap(button);


//        imageView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Matrix matrix = new Matrix();
//        matrix.postScale(0.3f, 0.3f); //长和宽放大缩小的比例
//        if(isPress) {
//
//        }else {
//            Bitmap resizeBmp = Bitmap.createBitmap(button, 0, 0, button.getWidth(), button.getHeight(), matrix, true);
//
//            canvas.drawBitmap(resizeBmp, 0, 0, null);
//        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                isPress = true;
                if (!isPress) {
                    RotateAnimation animation = new RotateAnimation(0f, 45f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(500);//设置动画持续时间
                    animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
                    imageView.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            Animation translateAnimation = new TranslateAnimation(-250, -150, 0, -250);    //设置位置变化动画
                            Animation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                            设置尺寸变化动画
//                            Animation rotateAnimation=new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                            translateAnimation.setDuration(300);      //设置位置变化动画的持续时间
                            scaleAnimation.setDuration(300);          //设置尺寸变化动画的持续时间
//                            rotateAnimation.setDuration(1000);          //设置透明度渐变动画的持续时间
                            AnimationSet set = new AnimationSet(true);    //创建动画集对象
                            set.addAnimation(translateAnimation);       //添加位置变化动画
                            set.addAnimation(scaleAnimation);           //添加尺寸变化动画
//                            set.addAnimation(rotateAnimation);           //添加透明度渐变动画
                            set.setFillAfter(true);                 //停留在最后的位置
                            set.setFillEnabled(true);
                            leftView.startAnimation(set);

                            set.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    leftView.startAnimation(shakeAnimationLeft);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                            Animation translateAnimation2 = new TranslateAnimation(250, 150, 0, -250);    //设置位置变化动画
                            Animation scaleAnimation2 = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                            设置尺寸变化动画
//                            Animation rotateAnimation2=new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                            translateAnimation2.setDuration(300);      //设置位置变化动画的持续时间
                            scaleAnimation2.setDuration(300);          //设置尺寸变化动画的持续时间
//                            rotateAnimation2.setDuration(1000);          //设置透明度渐变动画的持续时间
                            AnimationSet set2 = new AnimationSet(true);    //创建动画集对象
                            set2.addAnimation(translateAnimation2);       //添加位置变化动画
                            set2.addAnimation(scaleAnimation2);           //添加尺寸变化动画
//                            set2.addAnimation(rotateAnimation2);           //添加透明度渐变动画
                            set2.setFillAfter(true);                 //停留在最后的位置
                            set2.setFillEnabled(true);
                            rightView.startAnimation(set2);
                            set2.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    rightView.startAnimation(shakeAnimationRight);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    isPress = true;
                } else {
                    RotateAnimation animation = new RotateAnimation(45f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(500);//设置动画持续时间
                    animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
                    imageView.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
//                            Animation translateAnimation = new TranslateAnimation(-250, -150, 0, -250);    //设置位置变化动画
                            Animation translateAnimation = new TranslateAnimation(-150, -250, -250, 0);    //设置位置变化动画
                            Animation scaleAnimation = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                            设置尺寸变化动画
//                            Animation rotateAnimation=new RotateAnimation(0f, 30f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                            translateAnimation.setDuration(300);      //设置位置变化动画的持续时间
                            scaleAnimation.setDuration(300);          //设置尺寸变化动画的持续时间
//                            rotateAnimation.setDuration(1000);          //设置透明度渐变动画的持续时间
                            AnimationSet set = new AnimationSet(true);    //创建动画集对象
                            set.addAnimation(translateAnimation);       //添加位置变化动画
                            set.addAnimation(scaleAnimation);           //添加尺寸变化动画
//                            set.addAnimation(rotateAnimation);           //添加透明度渐变动画
                            set.setFillAfter(true);                 //停留在最后的位置
                            set.setFillEnabled(true);
                            leftView.startAnimation(set);
//                            Animation translateAnimation2 = new TranslateAnimation(250, 150, 0, -250);    //设置位置变化动画
                            Animation translateAnimation2 = new TranslateAnimation(150, 250, -250, 0);    //设置位置变化动画
                            Animation scaleAnimation2 = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                            设置尺寸变化动画
//                            Animation rotateAnimation2=new RotateAnimation(0f, 30f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                            translateAnimation2.setDuration(300);      //设置位置变化动画的持续时间
                            scaleAnimation2.setDuration(300);          //设置尺寸变化动画的持续时间
//                            rotateAnimation2.setDuration(1000);          //设置透明度渐变动画的持续时间
                            AnimationSet set2 = new AnimationSet(true);    //创建动画集对象
                            set2.addAnimation(translateAnimation2);       //添加位置变化动画
                            set2.addAnimation(scaleAnimation2);           //添加尺寸变化动画
//                            set2.addAnimation(rotateAnimation2);           //添加透明度渐变动画
                            set2.setFillAfter(true);                 //停留在最后的位置
                            set2.setFillEnabled(true);
                            rightView.startAnimation(set2);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    isPress = false;
                }
                break;

        }
        return super.onTouchEvent(event);
    }

}
