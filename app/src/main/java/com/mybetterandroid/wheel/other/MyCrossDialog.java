package com.mybetterandroid.wheel.other;

import com.lsjr.zizisteward.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by zhouxb on 2016/3/15.
 */
public class MyCrossDialog extends Dialog {
    private MyCrossButton myCrossButton;
    private LinearLayout linearLayout;
    public MyCrossDialog(Context context) {
        super(context,R.style.MyDialog);
        myCrossButton = new MyCrossButton(context);
        setContentView(R.layout.dialog_mycross);
        linearLayout = (LinearLayout) findViewById(R.id.dialog_mycross_linearLayout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);//外围点击消失
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = dialogWindow.getWindowManager().getDefaultDisplay().getWidth(); // 宽度
        lp.height = dialogWindow.getWindowManager().getDefaultDisplay().getHeight(); // 高度
        dialogWindow.setAttributes(lp);

//        myCrossButton.setPadding(310,900,310,20);
        linearLayout.addView(myCrossButton);
        LinearLayout.LayoutParams mHomeparams = (LinearLayout.LayoutParams)myCrossButton.getLayoutParams();
        mHomeparams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        mHomeparams.height = LinearLayout.LayoutParams.MATCH_PARENT;
        mHomeparams.gravity = Gravity.CENTER;
        mHomeparams.bottomMargin = 60;
    }
}
