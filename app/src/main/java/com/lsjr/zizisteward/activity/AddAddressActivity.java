package com.lsjr.zizisteward.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.mybetterandroid.wheel.other.WheelViewTimeActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

//添加地址
public class AddAddressActivity extends Activity implements OnClickListener {
    private RelativeLayout selector_area;
    private EditText et_detail_address, et_name, et_phone;
    private TextView tv_area;
    private String address, province, city, area, zipCode;
    public static final Pattern IS_PHONE = Pattern.compile("^(1(3|4|5|7|8))\\d{9}$");
    private ImageView iv_back;
    private String mBrand_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        mBrand_detail = getIntent().getStringExtra("brand_detail");
        selector_area = (RelativeLayout) findViewById(R.id.RelativeLayout6);
        et_name = (EditText) findViewById(R.id.et_name);
        et_detail_address = (EditText) findViewById(R.id.et_detail_address);
        et_phone = (EditText) findViewById(R.id.et_phone);
        tv_area = (TextView) findViewById(R.id.tv_area);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        initLayout();
    }

    private void initLayout() {
        selector_area.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RelativeLayout6:
                Intent intent = new Intent(AddAddressActivity.this, WheelViewTimeActivity.class);
                startActivityForResult(intent, 99);

                break;
            case R.id.iv_back:
                finish();

                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == 77) {
            address = data.getStringExtra("address");
            province = data.getStringExtra("province");
            city = data.getStringExtra("city");
            area = data.getStringExtra("area");
            zipCode = data.getStringExtra("zipCode");
            tv_area.setText(address);
            tv_area.setTextColor(Color.BLACK);
            tv_area.setTextSize(18);

        }
    }

    public void save(View view) {
        String address = et_detail_address.getText().toString().trim();
        String name = et_name.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String area = tv_area.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "收货人不能为空", Toast.LENGTH_SHORT).show();
            return;

        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;

        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "地址不能为空", Toast.LENGTH_SHORT).show();
            return;

        }
        if (TextUtils.isEmpty(area)) {
            Toast.makeText(this, "所在地区不能为空", Toast.LENGTH_SHORT).show();
            return;

        }
        if (!IS_PHONE.matcher(phone).matches()) {
            Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            return;

        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("OPT", "14");
        map.put("id", App.getUserInfo().getId());
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        map.put("cname", name);
        map.put("cphone", phone);
        map.put("clocation", area);
        map.put("caddr", address);
        map.put("cpostcode", "431700");
        new HttpClientGet(AddAddressActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                Log.i("test", "添加的地址消息" + result);
                BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
                Toast.makeText(AddAddressActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();

                // 发送广播
                Intent intent = new Intent();
                intent.setAction("action.address");
                sendBroadcast(intent);
                finish();

            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });

    }
}
