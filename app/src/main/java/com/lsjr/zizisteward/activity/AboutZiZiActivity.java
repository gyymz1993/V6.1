package com.lsjr.zizisteward.activity;

import android.os.Bundle;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;

public class AboutZiZiActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("关于孜孜");

    }

    @Override
    public int getContainerView() {
        return R.layout.activity_about_us;
    }


}
