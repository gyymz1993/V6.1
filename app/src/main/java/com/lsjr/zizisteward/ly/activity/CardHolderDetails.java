package com.lsjr.zizisteward.ly.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.ShiJieDetailActivity;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.MyCardSingleBean;
import com.lsjr.zizisteward.bean.MyCardSingleBean.Card;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.share.OnekeyShare;
import com.lsjr.zizisteward.utils.CustomDialogUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.sharesdk.framework.ShareSDK;

public class CardHolderDetails extends Activity implements OnClickListener {

    /**
     * 返回键
     */
    private LinearLayout ll_back;
    /**
     * 编辑
     */
    private LinearLayout ll_editor;
    /**
     * 名字
     */
    private TextView tv_name;
    /**
     * 职位名称
     */
    private TextView tv_position_name;
    /**
     * 实名认证
     */
    private ImageView iv_real_name;
    /**
     * 职业身份
     */
    private ImageView iv_professional;
    /**
     * 标签
     */
    private TextView tv_label_one;
    private TextView tv_label_two;
    private TextView tv_label_three;
    /**
     * 电话号码
     */
    private TextView tv_number;
    private LinearLayout ll_number;
    /**
     * 邮箱
     */
    private TextView tv_email;
    /**
     * 公司
     */
    private TextView tv_company;
    /**
     * 发名片
     */
    private TextView tv_send;

    private String id;
    private Card card;
    private String activity;
    private String name;
    private LinearLayout ll;
    private LinearLayout ll_one;
    private LinearLayout ll_email;
    private LinearLayout ll_company;
    private LinearLayout ll_certification;

    private LinearLayout ll_o;
    private LinearLayout ll_t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.card_holder_details);
        this.findViewById();
    }

    private void findViewById() {

        this.ll_o = (LinearLayout) super.findViewById(R.id.ll_o);
        this.ll_t = (LinearLayout) super.findViewById(R.id.ll_t);
        this.ll = (LinearLayout) super.findViewById(R.id.ll);
        this.tv_send = (TextView) super.findViewById(R.id.tv_send);
        this.tv_name = (TextView) super.findViewById(R.id.tv_name);
        this.tv_email = (TextView) super.findViewById(R.id.tv_email);
        this.ll_one = (LinearLayout) super.findViewById(R.id.ll_one);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.tv_number = (TextView) super.findViewById(R.id.tv_number);
        this.tv_company = (TextView) super.findViewById(R.id.tv_company);
        this.ll_email = (LinearLayout) super.findViewById(R.id.ll_email);
        this.ll_editor = (LinearLayout) super.findViewById(R.id.ll_editor);
        this.ll_number = (LinearLayout) super.findViewById(R.id.ll_number);
        this.ll_company = (LinearLayout) super.findViewById(R.id.ll_company);
        this.tv_label_one = (TextView) super.findViewById(R.id.tv_label_one);
        this.tv_label_two = (TextView) super.findViewById(R.id.tv_label_two);
        this.iv_real_name = (ImageView) super.findViewById(R.id.iv_real_name);
        this.tv_label_three = (TextView) super.findViewById(R.id.tv_label_three);
        this.iv_professional = (ImageView) super.findViewById(R.id.iv_professional);
        this.tv_position_name = (TextView) super.findViewById(R.id.tv_position_name);
        this.ll_certification = (LinearLayout) super.findViewById(R.id.ll_certification);

        this.tv_send.setOnClickListener(this);
        this.ll_back.setOnClickListener(this);
        this.tv_number.setOnClickListener(this);
        this.ll_editor.setOnClickListener(this);
        this.ll_number.setOnClickListener(this);

        this.id = getIntent().getStringExtra("id");
        this.activity = getIntent().getStringExtra("activity");

        if (activity.equals("cha")) {
            name = getIntent().getStringExtra("name");
            tv_send.setText("发名片");
        } else if (activity.equals("mca")) {
            tv_send.setVisibility(View.GONE);
            ll_editor.setVisibility(View.GONE);
        } else {
            ll_editor.setVisibility(View.GONE);
            tv_send.setText("收藏名片");
        }
    }

    private void getCardDetails() {

        tv_label_one.setText("");
        tv_label_two.setText("");
        tv_label_three.setText("");
        ll_one.setVisibility(View.GONE);
        tv_label_one.setVisibility(View.GONE);
        tv_label_two.setVisibility(View.GONE);
        tv_label_three.setVisibility(View.GONE);

        CustomDialogUtils.startCustomProgressDialog(CardHolderDetails.this, "请稍候");
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "289");
        map.put("cardid", id);
        new HttpClientGet(CardHolderDetails.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                CustomDialogUtils.stopCustomProgressDialog(CardHolderDetails.this);
                System.out.println(result);

                MyCardSingleBean mBean = new Gson().fromJson(result, MyCardSingleBean.class);

                card = mBean.getCard();

                tv_name.setText(card.getUsername());

                if (mBean.getIs_pass_certificate() == 2) {
                    // 身份认证是否通过(0 否 1 是审核中，2是实名认证通过)
                    iv_real_name.setImageResource(R.drawable.icon_card_real_name_true);
                } else {

                    if (activity.equals("mca")) {
                        ll_o.setVisibility(View.GONE);
//						iv_real_name.setVisibility(View.GONE);
                    } else {
                        ll_o.setVisibility(View.VISIBLE);
//						iv_real_name.setVisibility(View.VISIBLE);
                    }

                    iv_real_name.setImageResource(R.drawable.icon_card_real_name);
                }

                if (card.getIs_approve() == 1) {
                    // 是否职业认证（0否。1是）
                    iv_professional.setImageResource(R.drawable.icon_card_professional_true);
                } else {

                    if (activity.equals("mca")) {
                        ll_t.setVisibility(View.GONE);
//						iv_professional.setVisibility(View.GONE);
                    } else {
                        ll_o.setVisibility(View.VISIBLE);
//						iv_professional.setVisibility(View.VISIBLE);
                    }

                    iv_professional.setImageResource(R.drawable.icon_card_professional);
                }

                if (mBean.getIs_pass_certificate() != 2 && card.getIs_approve() != 1) {
                    ll_certification.setVisibility(View.GONE);
                }

                if (null != card.getCompany_name() && card.getCompany_name().length() > 1
                        || null != card.getPosition() && card.getPosition().length() > 1) {

                    ll.setVisibility(View.VISIBLE);

                    // 如果有填写职位
                    if (null != card.getPosition() && card.getPosition().length() > 1) {

                        // 如果也填写了公司
                        if (null != card.getCompany_name() && card.getCompany_name().length() > 1) {

                            tv_position_name.setText(card.getPosition());
                            tv_position_name.setVisibility(View.VISIBLE);
                        } else {
                            tv_position_name.setText(card.getPosition());
                            tv_position_name.setVisibility(View.VISIBLE);
                        }

                        // 如果有填写公司
                    } else if (null != card.getCompany_name() && card.getCompany_name().length() > 1) {

                        // 如果也填写了职位
                        if (null != card.getPosition() && card.getPosition().length() > 1) {

                            tv_position_name.setText(card.getPosition());
                            tv_position_name.setVisibility(View.VISIBLE);
                        } else {
                        }
                    }
                }

                if (null != card.getLabel() && card.getLabel().length() > 1) {
                    String[] str = card.getLabel().split(",");

                    ll_one.setVisibility(View.VISIBLE);

                    switch (str.length) {
                        case 1:

                            tv_label_one.setText(str[0]);
                            tv_label_one.setVisibility(View.VISIBLE);

                            break;

                        case 2:

                            tv_label_one.setText(str[0]);
                            tv_label_one.setVisibility(View.VISIBLE);

                            tv_label_two.setText(str[1]);
                            tv_label_two.setVisibility(View.VISIBLE);

                            break;

                        case 3:

                            tv_label_one.setText(str[0]);
                            tv_label_one.setVisibility(View.VISIBLE);

                            tv_label_two.setText(str[1]);
                            tv_label_two.setVisibility(View.VISIBLE);

                            tv_label_three.setText(str[2]);
                            tv_label_three.setVisibility(View.VISIBLE);

                            break;
                    }
                }

                tv_number.setText(card.getPhone());

                if (null != card.getEmail() && card.getEmail().length() > 1) {
                    ll_email.setVisibility(View.VISIBLE);
                    tv_email.setText(card.getEmail());
                }

                if (null != card.getCompany_name() && card.getCompany_name().length() > 1) {
                    ll_company.setVisibility(View.VISIBLE);
                    tv_company.setText(card.getCompany_name());
                }
            }

            @Override
            public void onFailure(MyError myError) {
                CustomDialogUtils.stopCustomProgressDialog(CardHolderDetails.this);
                super.onFailure(myError);
            }
        });
    }

    @Override
    protected void onResume() {
        this.getCardDetails();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:

                setResult(1);
                finish();

                break;

            case R.id.ll_editor:
                startActivityForResult(new Intent(CardHolderDetails.this, NewCardActivity.class)
                        .putExtra("activity", "editor").putExtra("name", card.getUsername())
                        .putExtra("phone", card.getPhone()).putExtra("company_name", card.getCompany_name())
                        .putExtra("email", card.getEmail()).putExtra("position", card.getPosition())
                        .putExtra("label", card.getLabel()).putExtra("id", card.getId()), 1);
                break;

            case R.id.ll_number:

                if (null != card.getPhone() && card.getPhone().length() > 1) {

                    startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + card.getPhone()))
                            .putExtra("sms_body", ""));
                }

                break;

            case R.id.tv_number:

                if (null != card.getPhone() && card.getPhone().length() > 1) {

                    startActivity(new Intent().setAction(Intent.ACTION_CALL).setData(Uri.parse("tel:" + card.getPhone())));

                }

                break;

            case R.id.tv_send:

                if (activity.equals("cha")) {

                    ShareBusinessCard();

                } else {

                    if (tv_send.getText().toString().equals("收藏名片")) {
                        CustomDialogUtils.startCustomProgressDialog(CardHolderDetails.this, "请稍候");
                        SaveCard();
                    }

                }

                break;
        }
    }

    private void ShareBusinessCard() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "406");
        map.put("cardid", id);
        new HttpClientGet(CardHolderDetails.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("名片分享" + result);

                try {
                    JSONObject jObject = new JSONObject(result);

                    String CardShareUrl = jObject.getString("CardShareUrl");
                    String error = jObject.getString("error");
                    String msg = jObject.getString("msg");

                    if (error.equals("1")) {
                        ShareSDK.initSDK(CardHolderDetails.this);
                        OnekeyShare oks = new OnekeyShare();
                        oks.disableSSOWhenAuthorize();
                        String productUrl = HttpConfig.IMAGEHOST + CardShareUrl;
                        oks.setTitle(name + "的电子名片,请惠存");
                        oks.setTitleUrl(productUrl);
                        oks.setText("孜孜网络");
                        oks.setImageUrl(HttpConfig.IMAGEHOST + "/public/images/userLogo.png");//分享图片网络地址
//						oks.setImagePath();// 微信分享用本地图片地址
                        oks.setUrl(productUrl);
                        oks.setSite("孜孜管家");
                        oks.setSiteUrl(productUrl);
                        oks.show(CardHolderDetails.this);
                    } else {
                        Toast.makeText(CardHolderDetails.this, "请检查您的网络状态...", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });
    }

    private void SaveCard() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "292");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("cardid", id);
        new HttpClientGet(CardHolderDetails.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                CustomDialogUtils.stopCustomProgressDialog(CardHolderDetails.this);
                try {
                    JSONObject jObject = new JSONObject(result);
                    String error = jObject.getString("error");

                    if (error.equals("1")) {
                        tv_send.setText("已收藏");
                        Toast.makeText(CardHolderDetails.this, "收藏名片成功...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CardHolderDetails.this, "网络异常,请重试...", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    tv_send.setText("收藏名片");
                    CustomDialogUtils.stopCustomProgressDialog(CardHolderDetails.this);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(MyError myError) {
                tv_send.setText("已收藏");
                CustomDialogUtils.stopCustomProgressDialog(CardHolderDetails.this);
                super.onFailure(myError);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        getCardDetails();
    }
}
