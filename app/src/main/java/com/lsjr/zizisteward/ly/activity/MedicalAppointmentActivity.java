package com.lsjr.zizisteward.ly.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.date.OptionsPickerView;
import com.lsjr.zizisteward.date.TimePickerView;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MedicalAppointmentActivity extends Activity implements
        OnClickListener {

    private String id;
    private TextView tv_time;
    private TextView tv_sure;
    private EditText et_name;
    private EditText et_number;
    private EditText et_id_card;
    private LinearLayout ll_back;
    private LinearLayout ll_time;
    private LinearLayout ll_clear;
    private TimePickerView pvTime;
    private OptionsPickerView pvOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.medical_appointment_activity);
        this.findViewById();
    }

    private void findViewById() {

        this.tv_time = (TextView) super.findViewById(R.id.tv_time);
        this.tv_sure = (TextView) super.findViewById(R.id.tv_sure);
        this.et_name = (EditText) super.findViewById(R.id.et_name);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.ll_time = (LinearLayout) super.findViewById(R.id.ll_time);
        this.et_number = (EditText) super.findViewById(R.id.et_number);
        this.et_id_card = (EditText) super.findViewById(R.id.et_id_card);
        this.ll_clear = (LinearLayout) super.findViewById(R.id.ll_clear);

        this.ll_time.setOnClickListener(this);
        this.ll_back.setOnClickListener(this);
        this.tv_sure.setOnClickListener(this);
        this.ll_clear.setOnClickListener(this);

        this.id = getIntent().getStringExtra("id");

        // 时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        // 控制时间范围
        // Calendar calendar = Calendar.getInstance();
        // pvTime.setRange(calendar.get(Calendar.YEAR) - 20,
        // calendar.get(Calendar.YEAR));
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);

        // 时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                tv_time.setText(getTime(date));
            }
        });

        this.et_id_card.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0) {
                    ll_clear.setVisibility(View.VISIBLE);
                } else {
                    ll_clear.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private void SubmitAppointment(String order_date, String order_peoper, String id_card, String mobile) {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "305");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("sid", id);
        map.put("order_date", order_date);
        map.put("order_peoper", order_peoper);
        map.put("id_card", id_card);
        map.put("mobile", mobile);
        new HttpClientGet(MedicalAppointmentActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("提交预约信息  " + result);
//				startActivity(new Intent(MedicalAppointmentActivity.this,
//						MedicalOrderPay.class));

                try {
                    JSONObject jObject = new JSONObject(result);
                    String error = jObject.getString("error");
                    String msg = jObject.getString("msg");
                    if (error.equals("1")) {
                        Toast.makeText(MedicalAppointmentActivity.this, msg, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(MedicalAppointmentActivity.this, "网络连接失败,请稍候重试...", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.ll_clear:
                this.et_id_card.setText("");
                this.ll_clear.setVisibility(View.GONE);
                break;

            case R.id.tv_sure:

                String data = tv_time.getText().toString();
                String name = et_name.getText().toString();
                String id_card = et_id_card.getText().toString();
                String number = et_number.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(MedicalAppointmentActivity.this, "姓名不能为空", Toast.LENGTH_SHORT)
                            .show();
                } else if (TextUtils.isEmpty(id_card) || id_card.length() != 18) {
                    Toast.makeText(MedicalAppointmentActivity.this,
                            "请检查您输入的身份证号码", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(number) || number.length() != 11) {
                    Toast.makeText(MedicalAppointmentActivity.this,
                            "请检查您输入的手机号码", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(data) || number.length() != 11) {
                    Toast.makeText(MedicalAppointmentActivity.this,
                            "请选择预约时间", Toast.LENGTH_SHORT).show();
                } else {
                    SubmitAppointment(data, name, id_card, number);
                }

                break;

            case R.id.ll_time:
                pvTime.show();
                break;
        }
    }
}
