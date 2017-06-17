package com.lsjr.zizisteward.ly.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_AddFriends extends Activity implements OnClickListener {

    private TextView tv_add;
    private TextView tv_cancel;
    private EditText et_content;
    private LinearLayout ll_parent;
    private RelativeLayout rl_parent;
    protected InputMethodManager imm;
    private String randomCodes = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_friends);


        this.tv_add = (TextView) super.findViewById(R.id.tv_add);
        this.tv_cancel = (TextView) super.findViewById(R.id.tv_cancel);
        this.et_content = (EditText) super.findViewById(R.id.et_content);
        this.ll_parent = (LinearLayout) super.findViewById(R.id.ll_parent);
        this.rl_parent = (RelativeLayout) super.findViewById(R.id.rl_parent);

        this.tv_add.setOnClickListener(this);
        this.tv_cancel.setOnClickListener(this);
        this.ll_parent.setOnClickListener(this);
        this.rl_parent.setOnClickListener(this);
        this.et_content.setOnClickListener(this);

        this.imm = (InputMethodManager) Activity_AddFriends.this.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_cancel:

                this.imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);
                this.ll_parent.setVisibility(View.GONE);
                this.finish();

                break;

            case R.id.rl_parent:

                this.imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);
                this.ll_parent.setVisibility(View.GONE);
                this.finish();

                break;

            case R.id.tv_add:

                CustomDialogUtils.startCustomProgressDialog(Activity_AddFriends.this, "请稍候");

                if (null != et_content.getText().toString() && !"".equals(et_content.getText().toString())) {

                    Map<String, String> map = new HashMap<>();
                    map.put("OPT", "205");
                    map.put("name", PreferencesUtils.getString(Activity_AddFriends.this, "user_account"));
                    map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                    map.put("randomCodes", et_content.getText().toString());

                    new HttpClientGet(Activity_AddFriends.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            CustomDialogUtils.stopCustomProgressDialog(Activity_AddFriends.this);
                            try {
                                JSONObject jObject = new JSONObject(result);
                                String error = jObject.getString("error");
                                String msg = jObject.getString("msg");
                                ll_parent.setVisibility(View.GONE);

                                if (error.equals("1")) {
                                    //startActivity(new Intent(Activity_AddFriends.this,Activity_God.class).putExtra("god", 0));
                                    finish();
                                    Fragment_AddressBook.requestAddressBook();
                                    Toast.makeText(Activity_AddFriends.this, "添加好友成功", Toast.LENGTH_SHORT).show();

                                } else {
                                    //startActivity(new Intent(Activity_AddFriends.this,Activity_God.class).putExtra("god", 1));
                                    finish();
                                    Toast.makeText(Activity_AddFriends.this, "添加好友失败", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(MyError myError) {
                            CustomDialogUtils.stopCustomProgressDialog(Activity_AddFriends.this);
                            imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);
                            ll_parent.setVisibility(View.GONE);
                            //startActivity(new Intent(Activity_AddFriends.this,Activity_God.class).putExtra("god", 1));
                            finish();
                            super.onFailure(myError);
                        }
                    });
                } else {
                    Toast.makeText(Activity_AddFriends.this, "邀请码不可为空...", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
