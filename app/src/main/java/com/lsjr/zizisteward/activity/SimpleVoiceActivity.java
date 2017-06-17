package com.lsjr.zizisteward.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.ui.MainActivity;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.AddressBookBean;
import com.lsjr.zizisteward.bean.GrouplabelBean;
import com.lsjr.zizisteward.bean.NoteLoginBean;
import com.lsjr.zizisteward.common.activtiy.TestVoice;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/12.
 */

public class SimpleVoiceActivity extends Activity implements View.OnClickListener {
    private Boolean isExit = false;
    public static List<GrouplabelBean.Grouplabel> list_grouplabel = new ArrayList<>();
    public static List<AddressBookBean.Friends> friends = new ArrayList<>();

    RelativeLayout voice_and_call_change, re_voice_search, re_search, rl_msg;
    LinearLayout ll_voice, ll_call, ll_one, ll_two, ll_three, ll_msg_parent;
    TextView tv_one, tv_two, tv_three, login_notif, tv_prompt;
    RoundImageView riv_one, riv_two, riv_three;
    boolean login_state;
    Intent intent;
    String shiming_state;
    static TextView tv_num;
    private List<SixthNewActivity.MsgList> list = new ArrayList<SixthNewActivity.MsgList>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_voice);

        voice_and_call_change = (RelativeLayout) findViewById(R.id.voice_and_call_change);// 语音翻转按钮
        re_voice_search = (RelativeLayout) findViewById(R.id.re_voice_search);// 语音界面的搜索
        re_search = (RelativeLayout) findViewById(R.id.re_search);// 语音界面的右上角按钮
        ll_voice = (LinearLayout) findViewById(R.id.ll_voice);//语音留言
        ll_call = (LinearLayout) findViewById(R.id.ll_call);//呼叫管家
        rl_msg = (RelativeLayout) findViewById(R.id.rl_msg);//消息
        tv_num = (TextView) findViewById(R.id.tv_num);
        riv_one = (RoundImageView) findViewById(R.id.riv_one);
        riv_two = (RoundImageView) findViewById(R.id.riv_two);
        riv_three = (RoundImageView) findViewById(R.id.riv_three);
        tv_one = (TextView) findViewById(R.id.tv_one);
        tv_two = (TextView) findViewById(R.id.tv_two);
        tv_three = (TextView) findViewById(R.id.tv_three);
        ll_one = (LinearLayout) findViewById(R.id.ll_one);
        ll_two = (LinearLayout) findViewById(R.id.ll_two);
        ll_three = (LinearLayout) findViewById(R.id.ll_three);
        ll_msg_parent = (LinearLayout) findViewById(R.id.ll_msg_parent);
        login_notif = (TextView) findViewById(R.id.login_notif);
        tv_prompt = (TextView) findViewById(R.id.tv_prompt);
        initListener();
       // getClassify();
    }

    private void getClassify() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "220");
        new HttpClientGet(SimpleVoiceActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                GrouplabelBean gBean = new Gson().fromJson(result, GrouplabelBean.class);
                list_grouplabel = gBean.getGrouplabel();
            }
        });
    }

    private void initListener() {
        voice_and_call_change.setOnClickListener(this);
        re_voice_search.setOnClickListener(this);
        re_search.setOnClickListener(this);
        ll_voice.setOnClickListener(this);
        ll_call.setOnClickListener(this);
        rl_msg.setOnClickListener(this);
        login_notif.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        login_state = PreferencesUtils.getBoolean(getApplicationContext(), "isLogin");
        if (login_state == true) {
            ll_msg_parent.setVisibility(View.VISIBLE);
            login_notif.setVisibility(View.GONE);

                setNum();
                getUserData();//获取用户是否实名状态
                getData();

        } else {
            ll_msg_parent.setVisibility(View.GONE);
            login_notif.setVisibility(View.VISIBLE);
        }

        super.onResume();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.voice_and_call_change:
                intent = new Intent(SimpleVoiceActivity.this, SixthNewActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.re_voice_search:
                intent = new Intent(SimpleVoiceActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.re_search:
                intent = new Intent(SimpleVoiceActivity.this, ZiXunActivity.class);
                intent.putExtra("type", "");
                startActivityForResult(intent, 100);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.ll_voice:
                if (login_state == true) {
                    startActivityForResult(new Intent(SimpleVoiceActivity.this, TestVoice.class), 1);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    intent = new Intent(SimpleVoiceActivity.this, NoteLoginActivity.class);
                    intent.putExtra("personal", "voice");
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.ll_call:
                if (login_state == true) {
                    if (shiming_state.equals("1")) {
                        intent = new Intent(SimpleVoiceActivity.this, NewCallStewardActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    } else {
                        final Dialog dialog = new Dialog(SimpleVoiceActivity.this, R.style.dialog);
                        dialog.setContentView(R.layout.dialog_real_name_confirm);
                        Window window = dialog.getWindow();
                        window.setGravity(Gravity.CENTER | Gravity.CENTER);
                        TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
                        TextView tv_confirm = (TextView) dialog.findViewById(R.id.tv_confirm);
                        tv_cancel.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        tv_confirm.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                intent = new Intent(SimpleVoiceActivity.this, RealNameConfirmActivity.class);
                                startActivityForResult(intent, 1);
                            }
                        });
                        dialog.show();

                    }
                } else if (login_state == false) {
                    intent = new Intent(SimpleVoiceActivity.this, NoteLoginActivity.class);
                    intent.putExtra("personal", "sixth_new");
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.rl_msg:
                if (login_state == true) {
                    App.requestAddressBook(SimpleVoiceActivity.this, 0);
                } else if (login_state == false) {
                    intent = new Intent(SimpleVoiceActivity.this, NoteLoginActivity.class);
                    intent.putExtra("personal", "quanzi");
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.login_notif:

                Intent intent = new Intent(SimpleVoiceActivity.this, NoteLoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == 555) {// 消息回调到李杨的main界面
                    App.requestAddressBook(SimpleVoiceActivity.this, 0);
                }

                if (resultCode == 419) {// 打电话登录回调到召唤私人管家
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            intent = new Intent(SimpleVoiceActivity.this, NewCallStewardActivity.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }, 200);
                }

                if (resultCode == 44) {// 回调到呼叫管家选择界面
                    new Handler().postDelayed(new Runnable() {

                        public void run() {
                            intent = new Intent(SimpleVoiceActivity.this, CallButtonActivtiy.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }, 200);
                }

                if (resultCode == 7) {// 语音留言回调到与管家聊天界面
                    // 获取聊天前 的一些参数
                    if (App.getUserInfo().getUsername().equals(EMClient.getInstance().getCurrentUser())) {
                        Toast.makeText(SimpleVoiceActivity.this, R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                    } else {
                        App.CallSteward(SimpleVoiceActivity.this);
                    }
                }
                break;
            case 100:
                break;
        }
    }

    public void getUserData() {
        // 实名认证和身份认证消息
        if (login_state == true) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("OPT", "88");
            map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
            new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                @Override
                public void onSuccess(String result) {
                    System.out.println("状态" + result);
                    RealNameConfirmActivity.StateBean bean = GsonUtil.getInstance().fromJson(result, RealNameConfirmActivity.StateBean.class);
                    shiming_state = bean.getIsIdNumberVerified();
                }
            });
        }
    }

    public static void setNum() {
        int space = MainActivity.getUnreadMsgCountTotal();
        if (space > 0) {
            tv_num.setText(String.valueOf(space));
            tv_num.setVisibility(View.VISIBLE);
        } else {
            tv_num.setVisibility(View.INVISIBLE);
        }
    }

    public void getData() {
        if (login_state == true) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("OPT", "428");
            map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
            new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                @Override
                public void onSuccess(String result) {
                    System.out.println("消息推送" + result);
                    final SixthNewActivity.MsgBea bea = GsonUtil.getInstance().fromJson(result, SixthNewActivity.MsgBea.class);
                    list = bea.getMessage_records();

                    if (null != list && list.size() > 0) {
                        tv_prompt.setVisibility(View.GONE);
                        for (int i = 0; i < list.size(); i++) {
                            switch (i) {
                                case 0:
                                    ll_one.setVisibility(View.VISIBLE);
                                    ll_two.setVisibility(View.GONE);
                                    ll_three.setVisibility(View.GONE);
                                    setResources(list.get(0).getMessage_type(), riv_one, list.get(0).getContent(), tv_one);
                                    break;

                                case 1:
                                    ll_one.setVisibility(View.VISIBLE);
                                    ll_two.setVisibility(View.VISIBLE);
                                    ll_three.setVisibility(View.GONE);
                                    setResources(list.get(1).getMessage_type(), riv_two, list.get(1).getContent(), tv_two);
                                    break;

                                case 2:
                                    ll_one.setVisibility(View.VISIBLE);
                                    ll_two.setVisibility(View.VISIBLE);
                                    ll_three.setVisibility(View.VISIBLE);
                                    setResources(list.get(2).getMessage_type(), riv_three, list.get(2).getContent(),
                                            tv_three);
                                    break;
                            }
                        }
                    } else {
                        ll_msg_parent.setVisibility(View.VISIBLE);
                        tv_prompt.setText(bea.getMsg());
                        tv_prompt.setVisibility(View.VISIBLE);
                        ll_one.setVisibility(View.GONE);
                        ll_two.setVisibility(View.GONE);
                        ll_three.setVisibility(View.GONE);
                    }

                    ll_one.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (bea.getMessage_records().get(0).getMessage_type().equals("5")) {// 推送的订单到订单物流详情
                                Intent intent = new Intent(SimpleVoiceActivity.this, LookLogisticsActivity.class);
                                intent.putExtra("lianjie", list.get(0).getUrl());
                                startActivity(intent);
                            } else {// 推送的方案到方案详情(出行 美食 活动 健康)
                                Intent intent = new Intent(SimpleVoiceActivity.this, ServiceProjectDetailActivity.class);
                                intent.putExtra("tspid", list.get(0).getUrl());
                                intent.putExtra("type", "url");
                                startActivity(intent);
                            }
                        }
                    });

                    ll_two.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (bea.getMessage_records().get(1).getMessage_type().equals("5")) {// 推送的订单到订单详情
                                Intent intent = new Intent(SimpleVoiceActivity.this, LookLogisticsActivity.class);
                                intent.putExtra("lianjie", list.get(1).getUrl());
                                startActivity(intent);
                            } else {// 推送的方案到方案详情(出行 美食 活动 健康)
                                Intent intent = new Intent(SimpleVoiceActivity.this, ServiceProjectDetailActivity.class);
                                intent.putExtra("tspid", list.get(1).getUrl());
                                intent.putExtra("type", "url");
                                startActivity(intent);
                            }
                        }
                    });
                    ll_three.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (bea.getMessage_records().get(2).getMessage_type().equals("5")) {// 推送的订单到订单详情
                                Intent intent = new Intent(SimpleVoiceActivity.this, LookLogisticsActivity.class);
                                intent.putExtra("lianjie", list.get(2).getUrl());
                                startActivity(intent);
                            } else {// 推送的方案到方案详情(出行 美食 活动 健康)
                                Intent intent = new Intent(SimpleVoiceActivity.this, ServiceProjectDetailActivity.class);
                                intent.putExtra("tspid", list.get(2).getUrl());
                                intent.putExtra("type", "url");
                                startActivity(intent);
                            }
                        }
                    });
                }

            });
        }
    }

    private void setResources(String type, RoundImageView riv, String content, TextView tv) {
        if (type.equals("1")) {
            riv.setImageResource(R.drawable.black_gray);
        } else if (type.equals("2")) {
            riv.setImageResource(R.drawable.food_gray);
        } else if (type.equals("3")) {
            riv.setImageResource(R.drawable.jiangkang_gray);
        } else if (type.equals("4")) {
            riv.setImageResource(R.drawable.huiyuan_gray);
        } else if (type.equals("5")) {
            riv.setImageResource(R.drawable.order_gray);
        }
        tv.setText(content);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == event.getKeyCode() && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (isExit) {
                finish();
            } else {
                isExit = true;
                Handler exitHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        isExit = false;
                    }
                };
                exitHandler.sendEmptyMessageDelayed(0, 1000);
                Toast.makeText(SimpleVoiceActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return super.dispatchKeyEvent(event);
    }
}