package com.lsjr.zizisteward.ly.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.hyphenate.chat.a.b.c;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.R.id;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class NewCardActivity extends Activity implements OnClickListener {

    private LinearLayout ll_cancel;
    private LinearLayout ll_save;
    private TextView tv_et_one;
    private TextView tv_et_two;
    private TextView tv_et_three;
    private EditText et;
    private EditText et_name;
    private EditText et_number;
    private EditText et_email;
    private EditText et_company_name;
    private EditText et_position_name;

    private String activity;
    private String name;
    private String phone;
    private String company_name;
    private String email;
    private String position;
    private String label;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.new_card_activity);
        this.findViewById();
    }

    private void findViewById() {

        this.et = (EditText) super.findViewById(R.id.et);
        this.et_name = (EditText) super.findViewById(R.id.et_name);
        this.et_email = (EditText) super.findViewById(R.id.et_email);
        this.tv_et_one = (TextView) super.findViewById(R.id.tv_et_one);
        this.ll_save = (LinearLayout) super.findViewById(R.id.ll_save);
        this.tv_et_two = (TextView) super.findViewById(R.id.tv_et_two);
        this.et_number = (EditText) super.findViewById(R.id.et_number);
        this.ll_cancel = (LinearLayout) super.findViewById(R.id.ll_cancel);
        this.tv_et_three = (TextView) super.findViewById(R.id.tv_et_three);
        this.et_company_name = (EditText) super
                .findViewById(R.id.et_company_name);
        this.et_position_name = (EditText) super
                .findViewById(R.id.et_position_name);

        this.ll_save.setOnClickListener(this);
        this.ll_cancel.setOnClickListener(this);

        this.activity = getIntent().getStringExtra("activity");

        if (activity.equals("editor")) {
            this.id = getIntent().getStringExtra("id");
            this.name = getIntent().getStringExtra("name");
            this.label = getIntent().getStringExtra("label");
            this.phone = getIntent().getStringExtra("phone");
            this.email = getIntent().getStringExtra("email");
            this.activity = getIntent().getStringExtra("activity");
            this.position = getIntent().getStringExtra("position");
            this.company_name = getIntent().getStringExtra("company_name");

            this.et_name.setText(name);
            this.et_number.setText(null == phone ? "" : phone);
            this.et_email.setText(null == email ? "" : email);
            this.et_position_name.setText(null == position ? "" : position);
            this.et_company_name.setText(null == company_name ? ""
                    : company_name);

            if (null != label && label.length() > 1) {

                String[] str = label.split(",");

                if (str.length == 1) {
                    tv_et_one.setText(str[0]);
                    tv_et_one.setVisibility(View.VISIBLE);
                } else if (str.length == 2) {
                    tv_et_one.setText(str[0]);
                    tv_et_one.setVisibility(View.VISIBLE);

                    tv_et_two.setText(str[1]);
                    tv_et_two.setVisibility(View.VISIBLE);
                } else if (str.length == 3) {
                    tv_et_one.setText(str[0]);
                    tv_et_one.setVisibility(View.VISIBLE);

                    tv_et_two.setText(str[1]);
                    tv_et_two.setVisibility(View.VISIBLE);

                    tv_et_three.setText(str[2]);
                    tv_et_three.setVisibility(View.VISIBLE);

                    et.setVisibility(View.GONE);
                }
            }
        }

        this.tv_et_one.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                tv_et_one.setText("");
                tv_et_one.setVisibility(View.GONE);
                et.setVisibility(View.VISIBLE);
            }
        });

        this.tv_et_two.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                tv_et_two.setText("");
                tv_et_two.setVisibility(View.GONE);
                et.setVisibility(View.VISIBLE);
            }
        });

        this.tv_et_three.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                tv_et_three.setText("");
                tv_et_three.setVisibility(View.GONE);
                et.setVisibility(View.VISIBLE);
            }
        });

        this.et.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_GO
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_SEND) {

					/* 隐藏软键盘 */
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }

                    if (tv_et_one.getVisibility() == View.GONE) {
                        tv_et_one.setText(et.getText().toString());
                        tv_et_one.setVisibility(View.VISIBLE);
                    } else if (tv_et_two.getVisibility() == View.GONE) {
                        tv_et_two.setText(et.getText().toString());
                        tv_et_two.setVisibility(View.VISIBLE);
                    } else if (tv_et_three.getVisibility() == View.GONE) {
                        tv_et_three.setText(et.getText().toString());
                        tv_et_three.setVisibility(View.VISIBLE);
                    }

                    et.setText("");

                    if (tv_et_one.getVisibility() == View.VISIBLE
                            && tv_et_two.getVisibility() == View.VISIBLE
                            && tv_et_three.getVisibility() == View.VISIBLE) {
                        et.setText("");
                        et.setVisibility(View.GONE);
                    }

                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {

		/* 隐藏软键盘 */
        InputMethodManager imm = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        switch (v.getId()) {

            case R.id.ll_cancel:

                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }

                finish();

                break;

            case R.id.ll_save:

                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }

                String username = et_name.getText().toString();
                String phone = et_number.getText().toString();
                String company_name = et_company_name.getText().toString();
                String position = et_position_name.getText().toString();
                String email = et_email.getText().toString();
                String label = "";

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(NewCardActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(NewCardActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                } else if (phone.length() != 11) {
                    Toast.makeText(NewCardActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                } else {

                    if (null == company_name || company_name.length() < 1) {
                        company_name = "";
                    } else if (null == company_name || company_name.length() < 1) {
                        position = "";
                    }

                    if (tv_et_one.getVisibility() == View.VISIBLE) {

                        if (label.equals("")) {
                            label = tv_et_one.getText().toString();
                        } else {
                            label += "," + tv_et_one.getText().toString();
                        }
                    }

                    if (tv_et_two.getVisibility() == View.VISIBLE) {
                        if (label.equals("")) {
                            label = tv_et_two.getText().toString();
                        } else {
                            label += "," + tv_et_two.getText().toString();
                        }
                    }

                    if (tv_et_three.getVisibility() == View.VISIBLE) {
                        if (label.equals("")) {
                            label = tv_et_three.getText().toString();
                        } else {
                            label += "," + tv_et_three.getText().toString();
                        }
                    }

                    if (et_email.getText().toString() != null
                            && et_email.getText().toString().length() > 1) {
                        String regEx = "^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$";
                        Pattern pattern = Pattern.compile(regEx);
                        Matcher matcher = pattern.matcher(et_email.getText()
                                .toString());
                        boolean state = matcher.matches();

                        if (state) {
                            if (activity.equals("editor")) {
                                EditorCard(username, phone, company_name, position,
                                        label, email);
                            } else {
                                SaveNewCard(username, phone, company_name,
                                        position, label, email);
                            }
                        } else {
                            Toast.makeText(NewCardActivity.this, "请输入正确的邮箱", Toast.LENGTH_SHORT)
                                    .show();
                        }

                    } else {
                        email = "";

                        if (activity.equals("editor")) {
                            EditorCard(username, phone, company_name, position,
                                    label, email);
                        } else {
                            SaveNewCard(username, phone, company_name, position,
                                    label, email);
                        }
                    }
                }

                break;
        }
    }

    private void EditorCard(String username, String phone, String company_name,
                            String position, String label, String email) {
        CustomDialogUtils.startCustomProgressDialog(NewCardActivity.this, "请稍候");
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "287");
        map.put("cardid", id);
        map.put("username", username);
        map.put("phone", phone);
        map.put("company_name", company_name);
        map.put("position", position);
        map.put("email", email);
        map.put("label", label);
        new HttpClientGet(NewCardActivity.this, null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        CustomDialogUtils.stopCustomProgressDialog(NewCardActivity.this);
                        System.out.println("修改名片" + result);
                        try {
                            JSONObject jObject = new JSONObject(result);
                            String error = jObject.getString("error");

                            if (error.equals("1")) {
                                Toast.makeText(NewCardActivity.this,
                                        "修改名片成功...", Toast.LENGTH_SHORT).show();
                                setResult(1);
                                finish();
                            } else {
                                Toast.makeText(NewCardActivity.this,
                                        "网络链接失败,请重试...", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(MyError myError) {
                        CustomDialogUtils.stopCustomProgressDialog(NewCardActivity.this);
                        super.onFailure(myError);
                    }
                });
    }

    private void SaveNewCard(String username, String phone,
                             String company_name, String position, String label, String email) {
        CustomDialogUtils.startCustomProgressDialog(NewCardActivity.this, "请稍候");
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "286");
        map.put("user_id",
                Fragment_ChatList.addSign(
                        Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("username", username);
        map.put("phone", phone);
        map.put("email", email);
        map.put("company_name", company_name);
        map.put("position", position);
        map.put("label", label);
        new HttpClientGet(NewCardActivity.this, null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        CustomDialogUtils.stopCustomProgressDialog(NewCardActivity.this);
                        System.out.println("新增名片" + result);
                        try {
                            JSONObject jObject = new JSONObject(result);
                            String error = jObject.getString("error");

                            if (error.equals("1")) {
                                Toast.makeText(NewCardActivity.this,
                                        "新增名片成功...", Toast.LENGTH_SHORT).show();
                                setResult(1);
                                finish();
                            } else {
                                Toast.makeText(NewCardActivity.this,
                                        "网络链接失败,请重试...", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            CustomDialogUtils.stopCustomProgressDialog(NewCardActivity.this);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(MyError myError) {
                        super.onFailure(myError);
                    }
                });
    }
}
