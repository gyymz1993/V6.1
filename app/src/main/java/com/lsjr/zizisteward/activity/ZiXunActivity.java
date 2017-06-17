package com.lsjr.zizisteward.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.MainPageAdapter;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.common.activtiy.CareAndCollectActivity;
import com.lsjr.zizisteward.common.activtiy.ZiShangSearchActivity;
import com.lsjr.zizisteward.fragment.ShiJieFragment;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class ZiXunActivity extends FragmentActivity
        implements OnClickListener, OnPageChangeListener, OnCheckedChangeListener {
    private RelativeLayout mImageButton1;
    private RelativeLayout mImageButton4;
    private RelativeLayout mImageButton5;
    private RelativeLayout mCall;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private ViewPager mAll_page;
    private MainPageAdapter mAdapter;
    private RadioGroup mRadioGroup1;
    private RadioButton mRadio0;
    private RadioButton mRadio1;
    private Intent mIntent;
    private Drawable mDrawable;
    public int Success = 0;
    public static TextView tv_new_message;
    RelativeLayout re_to_collect, re_search;
    private String type;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zixun);
        type = getIntent().getStringExtra("type");
      
        fragments.add(new ShiJieFragment());// 时视
        fragments.add(new ZiShangFragment());// 孜赏
        re_to_collect = (RelativeLayout) findViewById(R.id.re_to_collect);
        re_search = (RelativeLayout) findViewById(R.id.re_search);

        tv_new_message = (TextView) findViewById(R.id.tv_new_message);
        mImageButton1 = (RelativeLayout) findViewById(R.id.imageButton1);
        mImageButton4 = (RelativeLayout) findViewById(R.id.imageButton4);
        mImageButton5 = (RelativeLayout) findViewById(R.id.imageButton5);

        mCall = (RelativeLayout) findViewById(R.id.call);
        mRadioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        mRadio0 = (RadioButton) findViewById(R.id.radio0);
        mRadio1 = (RadioButton) findViewById(R.id.radio1);

        mAll_page = (ViewPager) findViewById(R.id.all_page);

        mAdapter = new MainPageAdapter(getSupportFragmentManager(), fragments);
        mAll_page.setAdapter(mAdapter);

        Resources resources = this.getResources();
        mDrawable = resources.getDrawable(R.drawable.huangxian);
        mDrawable.setBounds(1, 1, 120, 8);

        mRadio0.setCompoundDrawables(null, null, null, mDrawable);
        mRadio1.setCompoundDrawables(null, null, null, null);


        mAll_page.setOffscreenPageLimit(fragments.size());


        mAll_page.addOnPageChangeListener(this);
        mRadioGroup1.setOnCheckedChangeListener(this);

        if (type.equals("vip_zishang")) {
            mRadio1.setChecked(true);
        } else if (type.equals("vip_shishi")) {
            mRadio0.setChecked(true);
        } else {
            mRadio0.setChecked(true);
        }
        initLayout();

    }

    private void initLayout() {
        re_to_collect.setOnClickListener(this);
        mImageButton1.setOnClickListener(this);
        mImageButton4.setOnClickListener(this);
        mImageButton5.setOnClickListener(this);
        mCall.setOnClickListener(this);
        re_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton1:
                PreferencesUtils.putString(getApplicationContext(), "main", "main");
                startActivity(new Intent(ZiXunActivity.this, SixthNewActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
            case R.id.imageButton4:
                boolean state2 = PreferencesUtils.getBoolean(ZiXunActivity.this, "isLogin");
                if (state2 == true) {
                    App.requestAddressBook(ZiXunActivity.this, 0);
                } else if (state2 == false) {
                    mIntent = new Intent(ZiXunActivity.this, NoteLoginActivity.class);
                    mIntent.putExtra("personal", "quanzi");
                    startActivityForResult(mIntent, 1);
                }
                break;
            case R.id.imageButton5:// 到个人中心判断是否登录
                boolean state = PreferencesUtils.getBoolean(ZiXunActivity.this, "isLogin");
                if (state == true) {
                    mIntent = new Intent(ZiXunActivity.this, PersonalCenterActivity.class);
                    startActivity(mIntent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                } else if (state == false) {
                    mIntent = new Intent(ZiXunActivity.this, NoteLoginActivity.class);
                    mIntent.putExtra("personal", "personal");
                    startActivity(mIntent);
                }
                finish();
                break;
            case R.id.call:
                boolean state4 = PreferencesUtils.getBoolean(ZiXunActivity.this, "isLogin");
                if (state4 == true) {
                    mIntent = new Intent(ZiXunActivity.this, CallButtonActivtiy.class);
                    startActivity(mIntent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else if (state4 == false) {
                    mIntent = new Intent(ZiXunActivity.this, NoteLoginActivity.class);
                    mIntent.putExtra("personal", "callbutton");
                    startActivity(mIntent);
                }
                break;
            case R.id.re_to_collect:
                boolean sta = PreferencesUtils.getBoolean(getApplicationContext(), "isLogin");
                if (sta == true) {
                    mIntent = new Intent(getApplicationContext(), CareAndCollectActivity.class);
                    startActivity(mIntent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    mIntent = new Intent(ZiXunActivity.this, NoteLoginActivity.class);
                    mIntent.putExtra("personal", "care_and_collect");
                    startActivityForResult(mIntent, 1);
                }
                break;
            case R.id.re_search:
                if (re_to_collect.getVisibility() == View.VISIBLE) {
                    mIntent = new Intent(getApplicationContext(), WorldSearchActivity.class);
                } else {
                    mIntent = new Intent(getApplicationContext(), ZiShangSearchActivity.class);
                }
                startActivity(mIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == 555) {
                    App.requestAddressBook(ZiXunActivity.this, 0);
                }
                if (resultCode == 25) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            mIntent = new Intent(ZiXunActivity.this, CareAndCollectActivity.class);
                            startActivity(mIntent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }, 200);
                }
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (type.equals("vip_zishang") || type.equals("vip_shishi")) {
                finish();
            } else {
                startActivity(new Intent(ZiXunActivity.this, SixthNewActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int position) {
        getTabState(position);
    }

    private void getTabState(int index) {
        mRadio0.setChecked(false);
        mRadio1.setChecked(false);

        switch (index) {
            case 0:
                mRadio0.setChecked(true);
                mRadio0.setCompoundDrawables(null, null, null, mDrawable);
                mRadio1.setCompoundDrawables(null, null, null, null);
                re_to_collect.setVisibility(View.VISIBLE);
                break;
            case 1:
                mRadio1.setChecked(true);
                mRadio0.setCompoundDrawables(null, null, null, null);
                mRadio1.setCompoundDrawables(null, null, null, mDrawable);
                re_to_collect.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.radio0:
                mAll_page.setCurrentItem(0);
                break;
            case R.id.radio1:
                mAll_page.setCurrentItem(1);
                break;
        }
    }

}
