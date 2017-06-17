package com.lsjr.zizisteward.ly.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.bean.QuickContactsBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.quickcontacts.Activity_AddQuickContacts;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_PopUpWindow extends Activity implements OnClickListener {

    private TextView tv_content;
    private TextView tv_yes;
    private TextView tv_no;
    private String name;
    private String id;
    private RelativeLayout rl_parent;
    private LinearLayout ll_popwindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_window);

        this.id = getIntent().getStringExtra("id");
        this.name = getIntent().getStringExtra("name");

        this.tv_no = (TextView) super.findViewById(R.id.tv_no);
        this.tv_yes = (TextView) super.findViewById(R.id.tv_yes);
        this.tv_content = (TextView) super.findViewById(R.id.tv_content);
        this.rl_parent = (RelativeLayout) super.findViewById(R.id.rl_parent);
        this.ll_popwindow = (LinearLayout) super.findViewById(R.id.ll_popwindow);

        this.tv_content.setText("是否删除联系人" + name);

        this.tv_no.setOnClickListener(this);
        this.tv_yes.setOnClickListener(this);
        this.rl_parent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_parent:

                this.ll_popwindow.setVisibility(View.GONE);

                finish();

                break;

            case R.id.tv_no:

                this.ll_popwindow.setVisibility(View.GONE);

                finish();

                break;

            case R.id.tv_yes:

                this.ll_popwindow.setVisibility(View.GONE);

                Map<String, String> map = new HashMap<>();
                map.put("OPT", "203");
                map.put("id", id);
                new HttpClientGet(Activity_PopUpWindow.this, null, map, false,
                        new HttpClientGet.CallBacks<String>() {

                            @Override
                            public void onSuccess(String result) {

                                System.out.println("删除快速联系人：" + result);

                                try {
                                    JSONObject jObject = new JSONObject(result);
                                    if ("1".equals(jObject.getString("error"))) {
                                        Toast.makeText(Activity_PopUpWindow.this, "删除联系人成功", Toast.LENGTH_SHORT).show();
                                        setResult(1);
                                        finish();
                                    } else {
                                        Toast.makeText(Activity_PopUpWindow.this, "服务器开小差了,请稍后重试...", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                setResult(1);
                                finish();
                            }

                            @Override
                            public void onFailure(MyError myError) {
                                super.onFailure(myError);
                                finish();
                            }
                        });

                break;
        }
    }
}
