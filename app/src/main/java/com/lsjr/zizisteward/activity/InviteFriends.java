package com.lsjr.zizisteward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.CodeBean;
import com.lsjr.zizisteward.common.activtiy.InviteUsersActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.share.OnekeyShare;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

public class InviteFriends extends BaseActivity implements OnClickListener {
    private EditText input_yao;
    private EditText input_yan;
    private TextView get_yan;
    private TextView get_yao;
    private Map<String, String> map;
    private RelativeLayout re_check;
    private String content, title, url;

    @Override
    public int getContainerView() {
        return R.layout.activity_invite_friends;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("邀请好友");
        input_yao = (EditText) findViewById(R.id.input_yao);
        input_yan = (EditText) findViewById(R.id.input_yan);
        get_yan = (TextView) findViewById(R.id.get_yan);
        get_yao = (TextView) findViewById(R.id.get_yao);// 获取邀请码
        get_yao.setText("获取邀请码");
        re_check = (RelativeLayout) findViewById(R.id.re_check);// 一键查询
        get_yao.setOnClickListener(this);
        get_yan.setOnClickListener(this);
        re_check.setOnClickListener(this);

    }

    private void getShareData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "319");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("分享结果" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    content = jsonObject.getString("content");
                    title = jsonObject.getString("title");
                    url = jsonObject.getString("url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_yan:
                CustomDialogUtils.startCustomProgressDialog(InviteFriends.this, "正在获取验证码!");
                map = new HashMap<String, String>();
                map.put("OPT", "204");
                map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                new HttpClientGet(InviteFriends.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        CodeBean bean = GsonUtil.getInstance().fromJson(result, CodeBean.class);
                        input_yan.setText(bean.getRandomCodes());
                        CustomDialogUtils.stopCustomProgressDialog(InviteFriends.this);
                    }

                    @Override
                    public void onFailure(MyError myError) {
                        CustomDialogUtils.stopCustomProgressDialog(InviteFriends.this);
                        super.onFailure(myError);
                    }
                });
                break;
            case R.id.get_yao:
                if ("获取邀请码".equals(get_yao.getText().toString().trim())) {
                    CustomDialogUtils.startCustomProgressDialog(InviteFriends.this, "正在获取邀请码!");
                    map = new HashMap<String, String>();
                    map.put("OPT", "171");
                    map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                    new HttpClientGet(InviteFriends.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            CodeBean bean = GsonUtil.getInstance().fromJson(result, CodeBean.class);
                            input_yao.setText(bean.getRandomCodes());
                            CustomDialogUtils.stopCustomProgressDialog(InviteFriends.this);
                            get_yao.setText("分享邀请码");
                        }

                        @Override
                        public void onFailure(MyError myError) {
                            CustomDialogUtils.stopCustomProgressDialog(InviteFriends.this);
                            super.onFailure(myError);
                        }
                    });
                } else {
                    getShareData();
                    ShareSDK.initSDK(InviteFriends.this);
                    OnekeyShare oks = new OnekeyShare();
                    oks.disableSSOWhenAuthorize();
                    String productUrl = HttpConfig.IMAGEHOST + url;
                    oks.setTitle(title);
                    oks.setTitleUrl(productUrl);
                    oks.setText(content);
                    oks.setImageUrl(HttpConfig.IMAGEHOST + url);
                    oks.setImagePath("");// 确保sdcard 存在图片
                    oks.setUrl(productUrl);
                    oks.setSite("孜孜官网");
                    oks.setSiteUrl(productUrl);
                    oks.setCallback(new PlatformActionListener() {

                        @Override
                        public void onError(Platform arg0, int arg1, Throwable arg2) {
                            System.out.println("分享失败");
                        }

                        @Override
                        public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                            System.out.println("成功");
                        }

                        @Override
                        public void onCancel(Platform arg0, int arg1) {
                            System.out.println("取消分享");
                        }
                    });
                    oks.show(InviteFriends.this);
                }
                break;
            case R.id.re_check:
                startActivity(new Intent(InviteFriends.this, InviteUsersActivity.class));
                break;

        }
    }
}
