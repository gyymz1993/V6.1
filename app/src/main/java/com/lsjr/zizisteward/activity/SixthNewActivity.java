package com.lsjr.zizisteward.activity;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.libs.zxing.CaptureActivity;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.RealNameConfirmActivity.StateBean;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.ShareTime;
import com.lsjr.zizisteward.fragment.F_FoodHomeActivity;
import com.lsjr.zizisteward.fragment.F_MedicalHomeActivity;
import com.lsjr.zizisteward.fragment.F_TravelExpertsActivity;
import com.lsjr.zizisteward.fragment.JiangPinFragment;
import com.lsjr.zizisteward.fragment.NewHomeFragment;
import com.lsjr.zizisteward.fragment.NewLuxuryFragment;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.ly.activity.CardHolderDetails;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;
import com.lsjr.zizisteward.ly.activity.QrCodeAddGroupActivity;
import com.lsjr.zizisteward.ly.activity.QrCodePersonalActivity;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.ExampleUtil;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.lsjr.zizisteward.utils.ToastUtils;
import com.lsjr.zizisteward.ymz.LoveHouseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;

import static com.lsjr.zizisteward.utils.PreferencesUtils.getBoolean;

public class SixthNewActivity extends FragmentActivity implements OnClickListener {
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.lsjr.zizisteward.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground = false;

    /**
     * 搜索按钮
     */
    private RelativeLayout re_search;
    /**
     * 语音留言
     */
    private LinearLayout ll_voice;
    /**
     * 呼叫管家
     */
    private LinearLayout ll_call;
    /**
     * 消息
     */
    private RelativeLayout rl_msg;
    /**
     * 消息数
     */
    private static TextView tv_num;

    private RoundImageView riv_one;
    private RoundImageView riv_two;
    private RoundImageView riv_three;

    private TextView tv_one;
    private TextView tv_two;
    private TextView tv_three;

    private LinearLayout ll_one;
    private LinearLayout ll_two;
    private LinearLayout ll_three;
    private LinearLayout ll_msg_parent;

    private static Context context;

    private FragmentManager mManager;
    private FragmentTransaction mTransaction;
    private RelativeLayout mRe_vip, re_famous_product, re_health, re_home, re_travel, re_food, re_home_send,
            re_vip_selectd, re_famous_product_sel, re_health_sel, re_home_selector, re_travel_sel, re_food_sel,
            re_home_send_sel;
    private RelativeLayout mImageButton4, mImageButton2, mCall, mImageButton5, re_member, mRe_title, mRe_saomiao,
            re_fashion, mRe_food, mRe_family;
    private LinearLayout mLl_group;

    private JiangPinFragment frag1;// 匠品
//    private ShePinFragment frag2;// 奢品
    private F_TravelExpertsActivity frag3;// 出行
    private NewHomeFragment frag4;// 首页
    private F_FoodHomeActivity frag5;// 美食
    private F_MedicalHomeActivity frag6;// 健康
    //private HuoseInheritFragment frag7;// 家族
    private LoveHouseFragment frag7;// 家族
    /*新版首页改版*/
//    private LuxuryFragment frag2;/*奢品*/
    private NewLuxuryFragment frag2;/*奢品*/
    private HorizontalScrollView hsl_images;
    private Intent mIntent;
    private Boolean isExit = false;
    private RelativeLayout mRe_change;
    private ImageView iv_scan;
    private RelativeLayout mRe_city;
    private View mCity_view;
    private PopupWindow mCity_popupWindow;
    private TextView mWuhan, shanghai, login_notif;
    private RelativeLayout mRe_top;
    public static TextView tv_new_message;
    public static boolean space = false;
    private final int LOGIN_CODE = 123;
    private RelativeLayout mSearch_parent;
    private int mWidthPixels;
    private RelativeLayout mVoice_and_call_change;
    /**
     * 初始视图索引
     */
    private RelativeLayout mRe_main_home, imageButton1;
    private LinearLayout ll_main_home;
    private boolean mPerformClick;
    private RelativeLayout re_voice_search;

    private TextView tv_prompt;
    private String shiming_state;
    private boolean user_login_state;

    private TextView tv_city;
    private boolean vip_food;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        App.getInstance().addActivity(this);
        setContentView(R.layout.sixth_new_main);

		/* 注册广播 */
        JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this); // 初始化 JPush
        registerMessageReceiver();

//		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService("layout_inflater");
        tv_new_message = (TextView) findViewById(R.id.tv_new_message);

		/* 执行翻转的控件 */
        ll_main_home = (LinearLayout) findViewById(R.id.ll_main_home);// 主界面布局
        mRe_change = (RelativeLayout) findViewById(R.id.re_change);// 首页翻转按钮
        hsl_images = (HorizontalScrollView) findViewById(R.id.hsl_images);// 图片
        mRe_vip = (RelativeLayout) findViewById(R.id.re_vip);// 匠品
        re_vip_selectd = (RelativeLayout) findViewById(R.id.re_vip_selectd);// 匠品高亮
        re_famous_product = (RelativeLayout) findViewById(R.id.re_famous_product);// 奢品
        re_famous_product_sel = (RelativeLayout) findViewById(R.id.re_famous_product_sel);// 奢品高亮
        re_health = (RelativeLayout) findViewById(R.id.re_health);// 健康
        re_health_sel = (RelativeLayout) findViewById(R.id.re_health_sel);// 健康高亮
        re_home = (RelativeLayout) findViewById(R.id.re_home);// 首页
        re_home_selector = (RelativeLayout) findViewById(R.id.re_home_selector);// 首页高亮
        re_travel = (RelativeLayout) findViewById(R.id.re_travel);// 出行
        re_travel_sel = (RelativeLayout) findViewById(R.id.re_travel_sel);// 出行高亮
        re_food = (RelativeLayout) findViewById(R.id.re_food);// 美食
        re_food_sel = (RelativeLayout) findViewById(R.id.re_food_sel);// 美食高亮
        re_home_send = (RelativeLayout) findViewById(R.id.re_home_send);// 家族
        re_home_send_sel = (RelativeLayout) findViewById(R.id.re_home_send_sel);// 家族高亮
        imageButton1 = (RelativeLayout) findViewById(R.id.imageButton1);// 顶部首页
        mImageButton2 = (RelativeLayout) findViewById(R.id.imageButton2);// 视界
        mCall = (RelativeLayout) findViewById(R.id.call);// 一键呼叫
        mImageButton4 = (RelativeLayout) findViewById(R.id.imageButton4);// 圈子
        mImageButton5 = (RelativeLayout) findViewById(R.id.imageButton5);// 个人
        mSearch_parent = (RelativeLayout) findViewById(R.id.search_parent);// 搜索
        iv_scan = (ImageView) findViewById(R.id.iv_scan);// 扫描
        mRe_city = (RelativeLayout) findViewById(R.id.re_city);// 城市选择
        mRe_top = (RelativeLayout) findViewById(R.id.re_top);// 顶部父布局

        tv_city = (TextView) findViewById(R.id.tv_city);
        mManager = getSupportFragmentManager();
        hsl_images.setVisibility(View.VISIBLE);

        mCity_view = getLayoutInflater().inflate(R.layout.popup_city, null);
        mCity_popupWindow = new PopupWindow(mCity_view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        mCity_popupWindow.setOutsideTouchable(true);
        mCity_popupWindow.setTouchable(true);
        mCity_popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mWuhan = (TextView) mCity_view.findViewById(R.id.wuhan);
        shanghai = (TextView) mCity_view.findViewById(R.id.shanghai);

        String main = PreferencesUtils.getString(getApplicationContext(), "main");

        context = SixthNewActivity.this;
        init();
        initListener();
        initToOthers();
        // openGPSSettings();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                hsl_images.smoothScrollTo(
                        re_home_selector.getLeft() - getResources().getDisplayMetrics().widthPixels / 2
                                + (re_home_selector.getRight() - re_home_selector.getLeft()) / 2,
                        0);
            }
        }, 100);
    }

    private void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!TextUtils.isEmpty(messge)) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            // 用户登出
                            final Dialog dialog = new Dialog(SixthNewActivity.this, R.style.dialog);
                            dialog.setContentView(R.layout.chongfudenglu);
                            Window window = dialog.getWindow();
                            window.setGravity(Gravity.CENTER | Gravity.CENTER);
                            TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
                            TextView tv_confirm = (TextView) dialog.findViewById(R.id.tv_confirm);
                            dialog.setCancelable(false);
                            tv_confirm.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    DemoHelper.getInstance().logout(false, new EMCallBack() {

                                        @Override
                                        public void onSuccess() {
                                            PreferencesUtils.putBoolean(SixthNewActivity.this, "isLogin", false);
                                            Intent intent = new Intent(SixthNewActivity.this, NoteLoginActivity.class);
                                            intent.putExtra("personal", "login_out");
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onProgress(int arg0, String arg1) {
                                        }

                                        @Override
                                        public void onError(int arg0, String arg1) {
                                        }
                                    });

                                }
                            });
                            dialog.show();
                        }
                    }, 200);

                }
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();

    }

    private void initToOthers() {
        mRe_change.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        imageButton1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (re_home.getVisibility() == View.VISIBLE) {// 不在首页这里
                    mPerformClick = re_home.performClick();
                    if (frag4 == null) {
                        frag4 = new NewHomeFragment();
                        FragmentTransaction transaction = mManager.beginTransaction();
                        transaction.add(R.id.fl, frag4, null);
                        transaction.show(frag4);
                        transaction.commit();
                    }
                    initVisibile();
                } else {
                    hsl_images.smoothScrollTo(
                            re_home_selector.getLeft() - getResources().getDisplayMetrics().widthPixels / 2
                                    + (re_home_selector.getRight() - re_home_selector.getLeft()) / 2,
                            0);
                }
            }
        });

        mImageButton2.setOnClickListener(new OnClickListener() {
            // 视界
            @Override
            public void onClick(View v) {
                mIntent = new Intent(SixthNewActivity.this, ZiXunActivity.class);
                mIntent.putExtra("type", "");
                startActivityForResult(mIntent, 100);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        mCall.setOnClickListener(new OnClickListener() {
            // 首页的 一键呼叫
            @Override
            public void onClick(View v) {
                if (user_login_state == true) {
                    mIntent = new Intent(SixthNewActivity.this, CallButtonActivtiy.class);
                    startActivityForResult(mIntent, 100);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else if (user_login_state == false) {
                    mIntent = new Intent(SixthNewActivity.this, NoteLoginActivity.class);
                    mIntent.putExtra("personal", "sixth_new");
                    startActivityForResult(mIntent, 1);
                }
            }
        });
        mImageButton4.setOnClickListener(new OnClickListener() {
            // 圈子
            @Override
            public void onClick(View v) {
                if (user_login_state == true) {
                    App.requestAddressBook(context, 0);
                } else if (user_login_state == false) {
                    mIntent = new Intent(SixthNewActivity.this, NoteLoginActivity.class);
                    mIntent.putExtra("personal", "quanzi");
                    startActivityForResult(mIntent, 1);
                }
            }
        });
        mImageButton5.setOnClickListener(new OnClickListener() {
            // 个人
            @Override
            public void onClick(View v) {
                if (user_login_state == true) {
                    mIntent = new Intent(SixthNewActivity.this, PersonalCenterActivity.class);
                    startActivityForResult(mIntent, 100);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else if (user_login_state == false) {
                    mIntent = new Intent(SixthNewActivity.this, NoteLoginActivity.class);
                    mIntent.putExtra("personal", "personal");
                    startActivityForResult(mIntent, 1);
                }
            }
        });
        mSearch_parent.setOnClickListener(new OnClickListener() {
            // 首页界面的搜索
            @Override
            public void onClick(View v) {
                mIntent = new Intent(SixthNewActivity.this, SearchActivity.class);
                startActivityForResult(mIntent, 100);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        iv_scan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mIntent = new Intent(SixthNewActivity.this, CaptureActivity.class);
                startActivityForResult(mIntent, 1);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        mRe_city.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mCity_popupWindow.showAsDropDown(mRe_top, 0, 0);
            }
        });
        shanghai.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mCity_popupWindow.dismiss();
                ToastUtils.show(getApplicationContext(), "该城市数据暂未开发");
            }
        });
        mWuhan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mCity_popupWindow.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (re_home.getVisibility() == View.VISIBLE) { // 不在首页这里
                    mPerformClick = re_home.performClick();
                    if (frag4 == null) {
                        frag4 = new NewHomeFragment();
                        FragmentTransaction transaction = mManager.beginTransaction();
                        transaction.add(R.id.fl, frag4, null);
                        transaction.show(frag4);
                        transaction.commit();
                    }
                    initVisibile();
                } else {
                    hsl_images
                            .smoothScrollTo(re_home_selector.getLeft() - getResources().getDisplayMetrics().widthPixels / 2
                                    + (re_home_selector.getRight() - re_home_selector.getLeft()) / 2, 0);
                }

                if (resultCode == RESULT_OK) {// 二维码接口回调
                    // 获取解析的数据
                    String text = data.getStringExtra("text");
                    handleResult(text);
                }
                if (resultCode == 555) {// 消息回调到李杨的main界面
                    App.requestAddressBook(SixthNewActivity.this, 0);
                }

                if (resultCode == 419) {// 打电话登录回调到召唤私人管家
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            mIntent = new Intent(SixthNewActivity.this, NewCallStewardActivity.class);
                            startActivity(mIntent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }, 200);
                }
                if (resultCode == 44) {// 回调到呼叫管家选择界面
                    new Handler().postDelayed(new Runnable() {

                        public void run() {
                            mIntent = new Intent(SixthNewActivity.this, CallButtonActivtiy.class);
                            startActivity(mIntent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }, 200);
                }
                if (resultCode == 7) {// 语音留言回调到与管家聊天界面
                    // 获取聊天前 的一些参数
                    if (App.getUserInfo().getUsername().equals(EMClient.getInstance().getCurrentUser())) {
                        Toast.makeText(SixthNewActivity.this, R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                    } else {
                        App.CallSteward(SixthNewActivity.this);
                    }
                }
                break;
            case 100:
                if (re_home.getVisibility() == View.VISIBLE) {// 不在首页这里
                    mPerformClick = re_home.performClick();
                    if (frag4 == null) {
                        frag4 = new NewHomeFragment();

                        FragmentTransaction transaction = mManager.beginTransaction();
                        transaction.add(R.id.fl, frag4, null);
                        transaction.show(frag4);
                        transaction.commit();
                    }
                    initVisibile();
                } else {
                    hsl_images
                            .smoothScrollTo(re_home_selector.getLeft() - getResources().getDisplayMetrics().widthPixels / 2
                                    + (re_home_selector.getRight() - re_home_selector.getLeft()) / 2, 0);
                }
                break;

        }
    }

    @Override
    protected void onResume() {
        isForeground = true;
        user_login_state = getBoolean(getApplicationContext(), "isLogin");
        getUserData();
        super.onResume();
        JPushInterface.onResume(this);
    }

    private void getUserData() {
        // 实名认证和身份认证消息
        if (user_login_state == true) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("OPT", "88");
            map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
            new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                @Override
                public void onSuccess(String result) {
                    System.out.println("状态" + result);
                    StateBean bean = GsonUtil.getInstance().fromJson(result, StateBean.class);
                    shiming_state = bean.getIsIdNumberVerified();
                }
            });
        }
    }

    @Override
    protected void onPause() {
        isForeground = true;
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void init() {
        mPerformClick = re_home.performClick();
        if (frag4 == null) {
            frag4 = new NewHomeFragment();
            FragmentTransaction transaction = mManager.beginTransaction();
            transaction.add(R.id.fl, frag4, null);
            transaction.show(frag4);
            transaction.commit();
        }
        initVisibile();
    }

    private void initVisibile() {
        mRe_vip.setVisibility(View.VISIBLE);
        re_famous_product.setVisibility(View.VISIBLE);
        re_health.setVisibility(View.VISIBLE);
        re_home_selector.setVisibility(View.VISIBLE);
        re_health.setVisibility(View.VISIBLE);
        re_food.setVisibility(View.VISIBLE);
        re_home_send.setVisibility(View.VISIBLE);

        re_vip_selectd.setVisibility(View.GONE);
        re_famous_product_sel.setVisibility(View.GONE);
        re_health_sel.setVisibility(View.GONE);
        re_home.setVisibility(View.GONE);
        re_travel_sel.setVisibility(View.GONE);
        re_food_sel.setVisibility(View.GONE);
        re_home_send_sel.setVisibility(View.GONE);
    }

    private void initListener() {
        mRe_vip.setOnClickListener(this);
        re_famous_product.setOnClickListener(this);
        re_health.setOnClickListener(this);
        re_home.setOnClickListener(this);
        re_travel.setOnClickListener(this);
        re_food.setOnClickListener(this);
        re_home_send.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        mTransaction = mManager.beginTransaction();
        hideAllFragment();
        switch (v.getId()) {
            case R.id.re_vip:
                hsl_images.smoothScrollTo(hsl_images.getLeft(), 0);
                if (mRe_vip.getVisibility() == View.VISIBLE) {
                    re_vip_selectd.setVisibility(View.VISIBLE);
                    mRe_vip.setVisibility(View.GONE);
                    if (frag1 == null) {
                        frag1 = new JiangPinFragment();
                        mTransaction.add(R.id.fl, frag1, null);
                    }

                    mTransaction.setCustomAnimations(R.anim.fragment_slide_in_from_left, R.anim.fragment_slide_out_to_right,
                            R.anim.fragment_slide_in_from_right, R.anim.fragment_slide_out_to_left);
                    mTransaction.show(frag1);

                    re_famous_product.setVisibility(View.VISIBLE);
                    re_health.setVisibility(View.VISIBLE);
                    re_home.setVisibility(View.VISIBLE);
                    re_travel.setVisibility(View.VISIBLE);
                    re_food.setVisibility(View.VISIBLE);
                    re_home_send.setVisibility(View.VISIBLE);

                    re_famous_product_sel.setVisibility(View.GONE);
                    re_health_sel.setVisibility(View.GONE);
                    re_home_selector.setVisibility(View.GONE);
                    re_travel_sel.setVisibility(View.GONE);
                    re_food_sel.setVisibility(View.GONE);
                    re_home_send_sel.setVisibility(View.GONE);
                }
                break;
            case R.id.re_famous_product:
                hsl_images.smoothScrollTo(hsl_images.getLeft(), 0);
                if (re_famous_product.getVisibility() == View.VISIBLE) {
                    re_famous_product_sel.setVisibility(View.VISIBLE);
                    re_famous_product.setVisibility(View.GONE);
                    if (frag2 == null) {
                        frag2 = new NewLuxuryFragment();
                        mTransaction.add(R.id.fl, frag2, null);
                    }

                    mTransaction.setCustomAnimations(R.anim.fragment_slide_in_from_left, R.anim.fragment_slide_out_to_right,
                            R.anim.fragment_slide_in_from_right, R.anim.fragment_slide_out_to_left);
                    mTransaction.show(frag2);

                    mRe_vip.setVisibility(View.VISIBLE);
                    re_health.setVisibility(View.VISIBLE);
                    re_home.setVisibility(View.VISIBLE);
                    re_travel.setVisibility(View.VISIBLE);
                    re_food.setVisibility(View.VISIBLE);
                    re_home_send.setVisibility(View.VISIBLE);

                    re_vip_selectd.setVisibility(View.GONE);
                    re_health_sel.setVisibility(View.GONE);
                    re_home_selector.setVisibility(View.GONE);
                    re_travel_sel.setVisibility(View.GONE);
                    re_food_sel.setVisibility(View.GONE);
                    re_home_send_sel.setVisibility(View.GONE);
                }
                break;
            case R.id.re_travel:
                hsl_images.smoothScrollTo(hsl_images.getLeft(), 0);
                if (re_travel.getVisibility() == View.VISIBLE) {
                    re_travel_sel.setVisibility(View.VISIBLE);
                    re_travel.setVisibility(View.GONE);
                    if (frag3 == null) {
                        frag3 = new F_TravelExpertsActivity();
                        mTransaction.add(R.id.fl, frag3, null);
                    }

                    mTransaction.setCustomAnimations(R.anim.fragment_slide_in_from_left, R.anim.fragment_slide_out_to_right,
                            R.anim.fragment_slide_in_from_right, R.anim.fragment_slide_out_to_left);
                    mTransaction.show(frag3);

                    mRe_vip.setVisibility(View.VISIBLE);
                    re_famous_product.setVisibility(View.VISIBLE);
                    re_health.setVisibility(View.VISIBLE);
                    re_home.setVisibility(View.VISIBLE);
                    re_food.setVisibility(View.VISIBLE);
                    re_home_send.setVisibility(View.VISIBLE);

                    re_vip_selectd.setVisibility(View.GONE);
                    re_famous_product_sel.setVisibility(View.GONE);
                    re_health_sel.setVisibility(View.GONE);
                    re_home_selector.setVisibility(View.GONE);
                    re_food_sel.setVisibility(View.GONE);
                    re_home_send_sel.setVisibility(View.GONE);
                }
                break;
            case R.id.re_home:
                hsl_images.smoothScrollTo(re_home_selector.getLeft() - getResources().getDisplayMetrics().widthPixels / 2
                        + (re_home_selector.getRight() - re_home_selector.getLeft()) / 2, 0);
                if (re_home.getVisibility() == View.VISIBLE) {
                    re_home_selector.setVisibility(View.VISIBLE);
                    re_home.setVisibility(View.GONE);
                    if (frag4 == null) {
                        frag4 = new NewHomeFragment();
                        mTransaction.add(R.id.fl, frag4, null);
                    }

                    mTransaction.setCustomAnimations(R.anim.fragment_slide_in_from_left, R.anim.fragment_slide_out_to_right,
                            R.anim.fragment_slide_in_from_right, R.anim.fragment_slide_out_to_left);
                    mTransaction.show(frag4);

                    mRe_vip.setVisibility(View.VISIBLE);
                    re_famous_product.setVisibility(View.VISIBLE);
                    re_health.setVisibility(View.VISIBLE);
                    re_travel.setVisibility(View.VISIBLE);
                    re_food.setVisibility(View.VISIBLE);
                    re_home_send.setVisibility(View.VISIBLE);

                    re_vip_selectd.setVisibility(View.GONE);
                    re_famous_product_sel.setVisibility(View.GONE);
                    re_health_sel.setVisibility(View.GONE);
                    re_travel_sel.setVisibility(View.GONE);
                    re_food_sel.setVisibility(View.GONE);
                    re_home_send_sel.setVisibility(View.GONE);
                }
                break;
            case R.id.re_food:
                hsl_images.smoothScrollTo(hsl_images.getRight(), 0);
                if (re_food.getVisibility() == View.VISIBLE) {
                    re_food_sel.setVisibility(View.VISIBLE);
                    re_food.setVisibility(View.GONE);
                    if (frag5 == null) {
                        frag5 = new F_FoodHomeActivity();
                        mTransaction.add(R.id.fl, frag5, null);
                    }

                    mTransaction.setCustomAnimations(R.anim.fragment_slide_in_from_left, R.anim.fragment_slide_out_to_right,
                            R.anim.fragment_slide_in_from_right, R.anim.fragment_slide_out_to_left);
                    mTransaction.show(frag5);

                    mRe_vip.setVisibility(View.VISIBLE);
                    re_famous_product.setVisibility(View.VISIBLE);
                    re_health.setVisibility(View.VISIBLE);
                    re_home.setVisibility(View.VISIBLE);
                    re_travel.setVisibility(View.VISIBLE);
                    re_home_send.setVisibility(View.VISIBLE);

                    re_vip_selectd.setVisibility(View.GONE);
                    re_famous_product_sel.setVisibility(View.GONE);
                    re_health_sel.setVisibility(View.GONE);
                    re_home_selector.setVisibility(View.GONE);
                    re_travel_sel.setVisibility(View.GONE);
                    re_home_send_sel.setVisibility(View.GONE);
                }
                break;
            case R.id.re_health:
                hsl_images.smoothScrollTo(hsl_images.getRight(), 0);
                if (re_health.getVisibility() == View.VISIBLE) {
                    re_health_sel.setVisibility(View.VISIBLE);
                    re_health.setVisibility(View.GONE);
                    if (frag6 == null) {
                        frag6 = new F_MedicalHomeActivity();
                        mTransaction.add(R.id.fl, frag6, null);
                    }

                    mTransaction.setCustomAnimations(R.anim.fragment_slide_in_from_left, R.anim.fragment_slide_out_to_right,
                            R.anim.fragment_slide_in_from_right, R.anim.fragment_slide_out_to_left);
                    mTransaction.show(frag6);

                    mRe_vip.setVisibility(View.VISIBLE);
                    re_famous_product.setVisibility(View.VISIBLE);
                    re_home.setVisibility(View.VISIBLE);
                    re_travel.setVisibility(View.VISIBLE);
                    re_food.setVisibility(View.VISIBLE);
                    re_home_send.setVisibility(View.VISIBLE);

                    re_vip_selectd.setVisibility(View.GONE);
                    re_famous_product_sel.setVisibility(View.GONE);
                    re_home_selector.setVisibility(View.GONE);
                    re_travel_sel.setVisibility(View.GONE);
                    re_food_sel.setVisibility(View.GONE);
                    re_home_send_sel.setVisibility(View.GONE);
                }
                break;
            case R.id.re_home_send:
                hsl_images.smoothScrollTo(hsl_images.getRight(), 0);
                if (re_home_send.getVisibility() == View.VISIBLE) {
                    re_home_send_sel.setVisibility(View.VISIBLE);
                    re_home_send.setVisibility(View.GONE);
                    if (frag7 == null) {
                        frag7 = new LoveHouseFragment();
                        mTransaction.add(R.id.fl, frag7, null);
                    }

                    mTransaction.setCustomAnimations(R.anim.fragment_slide_in_from_left, R.anim.fragment_slide_out_to_right,
                            R.anim.fragment_slide_in_from_right, R.anim.fragment_slide_out_to_left);
                    mTransaction.show(frag7);

                    mRe_vip.setVisibility(View.VISIBLE);
                    re_famous_product.setVisibility(View.VISIBLE);
                    re_health.setVisibility(View.VISIBLE);
                    re_home.setVisibility(View.VISIBLE);
                    re_travel.setVisibility(View.VISIBLE);
                    re_food.setVisibility(View.VISIBLE);

                    re_vip_selectd.setVisibility(View.GONE);
                    re_famous_product_sel.setVisibility(View.GONE);
                    re_health_sel.setVisibility(View.GONE);
                    re_home_selector.setVisibility(View.GONE);
                    re_travel_sel.setVisibility(View.GONE);
                    re_food_sel.setVisibility(View.GONE);
                }
                break;
        }
        mTransaction.commit();
    }

    private void hideAllFragment() {
        if (frag1 != null) {
            mTransaction.hide(frag1);
        }
        if (frag2 != null) {
            mTransaction.hide(frag2);
        }
        if (frag3 != null) {
            mTransaction.hide(frag3);
        }
        if (frag4 != null) {
            mTransaction.hide(frag4);
        }
        if (frag5 != null) {
            mTransaction.hide(frag5);
        }
        if (frag6 != null) {
            mTransaction.hide(frag6);
        }
        if (frag7 != null) {
            mTransaction.hide(frag7);
        }

    }

    private void removeAll() {
        if (frag1 != null) {
            mTransaction.remove(frag1);
        }
        if (frag2 != null) {
            mTransaction.hide(frag2);
        }
        if (frag3 != null) {
            mTransaction.hide(frag3);
        }
        if (frag4 != null) {
            mTransaction.hide(frag4);
        }
        if (frag5 != null) {
            mTransaction.hide(frag5);
        }
        if (frag6 != null) {
            mTransaction.hide(frag6);
        }
        if (frag7 != null) {
            mTransaction.hide(frag7);
        }
    }

    private void handleResult(String text) {
        // 普通字符串
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (text.startsWith("http://") || text.startsWith("https://")) {
            // 跳到浏览器加载网页
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(text));
            startActivity(intent);
        } else if (text.startsWith("tel:")) {
            // 跳到打电话页面
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(text));
            startActivity(intent);
        } else if (text.startsWith("smsto:")) {
            // 跳到发短信页面
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(text));
            startActivity(intent);
        } else if (text.startsWith("mailto:")) {
            // 跳到发邮件页面
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(text));
            startActivity(intent);
        } else if (text.startsWith("market://")) {
            // 跳到应用市场页面
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(text));
            startActivity(intent);
        } else if (text.startsWith("ziziUser:")) {
            boolean state = getBoolean(SixthNewActivity.this, "isLogin");
            if (state == true) {
                String[] str = text.split("ziziUser:");
                getQrCode("1", str[1]);
            } else {
                Toast.makeText(SixthNewActivity.this, "请先登录后,再次扫一扫", Toast.LENGTH_SHORT).show();
            }
        } else if (text.startsWith("ziziGroup:")) {
            boolean state = getBoolean(SixthNewActivity.this, "isLogin");
            if (state == true) {
                String[] str = text.split("ziziGroup:");
                getQrCode("2", str[1]);
            } else {
                Toast.makeText(SixthNewActivity.this, "请先登录后,再次扫一扫", Toast.LENGTH_SHORT).show();
            }
        } else if (text.startsWith("zizicard:")) {
            String[] str = text.split("zizicard:");
            // 获取解析的数据
            startActivity(new Intent(SixthNewActivity.this, CardHolderDetails.class).putExtra("id", str[1])
                    .putExtra("activity", "save"));
        } else {
            // 用弹窗展示信息
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("扫描结果:");// 标题
            builder.setMessage(text);// 内容
            builder.setPositiveButton("确定", null);
            builder.show();
        }
    }

    private void getQrCode(final String type, final String content) {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "266");
        map.put("content", content);
        map.put("type", type);
        new HttpClientGet(SixthNewActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                try {
                    System.out.println(result);
                    JSONObject jObject = new JSONObject(result);
                    String error = jObject.getString("error");

                    if (error.equals("1")) {
                        switch (type) {
                            case "1":
                                startActivity(new Intent(SixthNewActivity.this, QrCodePersonalActivity.class).putExtra("id",
                                        content));
                                break;

                            case "2":
                                startActivity(new Intent(SixthNewActivity.this, QrCodeAddGroupActivity.class).putExtra("id",
                                        content));
                                break;
                        }
                    } else {
                        // 用弹窗展示信息
                        AlertDialog.Builder builder = new AlertDialog.Builder(SixthNewActivity.this);
                        builder.setTitle("扫描结果:");// 标题
                        builder.setMessage(content);// 内容
                        builder.setPositiveButton("确定", null);
                        builder.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }


    public static void Toast() {
        if (space) {
            tv_new_message.setVisibility(View.VISIBLE);
        } else {
            tv_new_message.setVisibility(View.GONE);
        }
    }

    public class MsgBea {
        private String error;
        private String msg;
        private List<MsgList> message_records;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<MsgList> getMessage_records() {
            return message_records;
        }

        public void setMessage_records(List<MsgList> message_records) {
            this.message_records = message_records;
        }
    }

    public class MsgList {
        private String content;
        private String id;
        private String message_type;
        private ShareTime time;
        private String user_id;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessage_type() {
            return message_type;
        }

        public void setMessage_type(String message_type) {
            this.message_type = message_type;
        }

        public ShareTime getTime() {
            return time;
        }

        public void setTime(ShareTime time) {
            this.time = time;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }

    // 1，打开GPS设置
    private void openGPSSettings() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            getCityNameBylocation();
            return;
        } else {
            Toast.makeText(this, "请开启GPS！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
            startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面
        }

    }

    static Geocoder geocoder;
    LocationManager locationManager;
    static String cityName = "";

//    private void getCityNameBylocation() {
//        geocoder = new Geocoder(getApplicationContext());
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        String provider = LocationManager.NETWORK_PROVIDER;
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
//        criteria.setAltitudeRequired(false); // 不要求海拔
//        criteria.setBearingRequired(false); // 不要求方位
//        criteria.setCostAllowed(false); // 不允许有话费
//        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
//
//        // 通过最后一次的地理位置来获得Location对象
//        Location location = locationManager.getLastKnownLocation(provider);
//
//        String queryed_name = updateWithNewLocation(location);
//        if ((queryed_name != null) && (0 != queryed_name.length())) {
//
//            cityName = queryed_name;
//            System.out.println("这是城市名字" + cityName);
//            tv_city.setText(cityName);
//        }
//
//		/*
//         * 第二个参数表示更新的周期，单位为毫秒；第三个参数的含义表示最小距离间隔，单位是米 设定每30秒进行一次自动定位
//		 */
//        locationManager.requestLocationUpdates(provider, 30000, 50, locationListener);
//        // 移除监听器，在只有一个widget的时候，这个还是适用的
//        locationManager.removeUpdates(locationListener);
//    }

    /**
     * 方位改变时触发，进行调用
     */
    private final static LocationListener locationListener = new LocationListener() {
        String tempCityName;

        public void onLocationChanged(Location location) {

            tempCityName = updateWithNewLocation(location);
            if ((tempCityName != null) && (tempCityName.length() != 0)) {

                cityName = tempCityName;
            }
        }

        public void onProviderDisabled(String provider) {
            tempCityName = updateWithNewLocation(null);
            if ((tempCityName != null) && (tempCityName.length() != 0)) {

                cityName = tempCityName;
            }
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private static String updateWithNewLocation(Location location) {
        String mcityName = "";
        double lat = 0;
        double lng = 0;
        List<Address> addList = null;
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        } else {

            System.out.println("无法获取地理信息");
        }

        try {

            addList = geocoder.getFromLocation(lat, lng, 1); // 解析经纬度

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addList != null && addList.size() > 0) {
            for (int i = 0; i < addList.size(); i++) {
                Address add = addList.get(i);
                mcityName += add.getLocality();
            }
        }
        if (mcityName.length() != 0) {

            return mcityName.substring(0, (mcityName.length() - 1));
        } else {
            return mcityName;
        }
    }

    private boolean gpsIsOpen() {
        boolean isOpen = true;
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {// 没有开启GPS
            isOpen = false;
        }
        return isOpen;
    }

    private boolean netWorkIsOpen() {
        boolean netIsOpen = true;
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {// 没有开启网络定位
            netIsOpen = false;
        }
        return netIsOpen;
    }

}
