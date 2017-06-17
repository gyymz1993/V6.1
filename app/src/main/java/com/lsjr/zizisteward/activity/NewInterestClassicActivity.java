package com.lsjr.zizisteward.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.db.DemoDBManager;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.bean.LabelClassic;
import com.lsjr.zizisteward.bean.LabelDetailInfo;
import com.lsjr.zizisteward.bean.LoginInfo;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.newview.MyGridView;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.lsjr.zizisteward.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/5/21.
 */

public class NewInterestClassicActivity extends BaseActivity implements View.OnClickListener {
    private MyGridView gridview;
    private List<LabelDetailInfo> list = new ArrayList<LabelDetailInfo>();
    private InterestAdapter adapter;
    private TextView tv_finish;
    private String passWord, type, phone, device;

    @Override
    public int getContainerView() {
        return R.layout.activity_new_interest_classic;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("选择感兴趣的分类");
        type = getIntent().getStringExtra("type");
        passWord = App.getInstance().getPassWord();// 注册密码
        phone = App.getInstance().getUserCellPhone();// 注册账号
        device = JPushInterface.getRegistrationID(this);// 设备号
        gridview = (MyGridView) findViewById(R.id.gridview);
        tv_finish = (TextView) findViewById(R.id.tv_finish);
        RelativeLayout re_right = (RelativeLayout) findViewById(R.id.re_right);// 跳过按钮
        TextView cancel_address = (TextView) findViewById(R.id.cancel_address);// 跳过文字
        getData();
        initListener();
        if ("set_password".equals(type)) {
            re_right.setVisibility(View.VISIBLE);
            cancel_address.setText("跳过");
        } else {
            re_right.setVisibility(View.GONE);
        }
        re_right.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 请求登录接口
                autoLogin();
            }
        });
    }

    @SuppressWarnings("unused")
    private void autoLogin() {
        // 请求登录接口
        CustomDialogUtils.startCustomProgressDialog(NewInterestClassicActivity.this, "正在跳转!");
        Map<String, String> map = new HashMap<String, String>();
        map.put("OPT", "1");
        map.put("name", phone);
        map.put("pwd", passWord);
        map.put("device", device);
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(final String result) {
                System.out.println("登录的消息" + result);
                DemoDBManager.getInstance().closeDB();
                DemoHelper.getInstance().setCurrentUserName(phone);
                EMClient.getInstance().login(phone, passWord, new EMCallBack() {

                    @SuppressWarnings({"unused", "static-access"})
                    @Override
                    public void onSuccess() {
                        LoginInfo bean = GsonUtil.getInstance().fromJson(result, LoginInfo.class);
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        EMClient.getInstance().updateCurrentUserNick(App.currentUserNick.trim());
                        DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                        //App.setUserInfo(bean);
                        PreferencesUtils.putObject(NewInterestClassicActivity.this, "userinfo", bean);
                        PreferencesUtils.putBoolean(NewInterestClassicActivity.this, "isLogin", true);
                        PreferencesUtils.putString(NewInterestClassicActivity.this, "user_account", phone);
                        PreferencesUtils.putString(NewInterestClassicActivity.this, "user_password", passWord);
                        CustomDialogUtils.stopCustomProgressDialog(NewInterestClassicActivity.this);
                        Intent intent = new Intent(getApplicationContext(), SixthNewActivity.class);
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
    }

    private void initListener() {
        tv_finish.setOnClickListener(this);
    }

    private void getData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("OPT", "19");
        new HttpClientGet(NewInterestClassicActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("什么" + result);
                LabelClassic bean = GsonUtil.getInstance().fromJson(result, LabelClassic.class);
                list = bean.getShop_types();

                adapter = new InterestAdapter(NewInterestClassicActivity.this, list);
                gridview.setAdapter(adapter);
                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        list.get(position).setSelectd(!list.get(position).isSelectd());
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_finish:
                String hobies = "";
                for (int i = 0; i < list.size(); i++) {

                    if (list.get(i).isSelectd()) {

                        if (hobies.equals("")) {
                            hobies = list.get(i).getId();
                        } else {
                            hobies += "," + list.get(i).getId();
                        }
                    }
                }
                System.out.print("李晨奇" + hobies);
                String[] ids = hobies.split(",");
                System.out.print("大小" + ids.length);
                if (ids.length < 1) {
                    ToastUtils.show(getApplicationContext(), "请至少选择一个兴趣分类");
                } else if (ids.length > 8) {
                    ToastUtils.show(getApplicationContext(), "最多选择8个兴趣分类");
                } else {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("OPT", "20");
                    map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                    map.put("hobbys", hobies);
                    new HttpClientGet(NewInterestClassicActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
                            Toast.makeText(getApplicationContext(), bean.getMsg(), Toast.LENGTH_SHORT).show();
                            if ("set_password".equals(type)) {
                                autoLogin();
                            } else {
                                finish();
                            }
                        }
                    });
                }
                break;
        }

    }

    private class InterestAdapter extends BaseAdapter {
        private Context context;
        private MyViewHolder holder;
        private List<LabelDetailInfo> list;

        public InterestAdapter(Context context, List<LabelDetailInfo> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_new_interest_classic, parent, false);
                holder = new MyViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (MyViewHolder) convertView.getTag();
            }

            holder.tv_name.setText(list.get(position).getTname());

            if (list.get(position).isSelectd()) {
                holder.tv_name.setTextColor(Color.parseColor("#b79771"));
                Glide.with(getApplicationContext())
                        .load(HttpConfig.IMAGEHOST + list.get(position).getType_icons()).into(holder.iv_photo);

            } else {
                holder.tv_name.setTextColor(Color.BLACK);
                Glide.with(getApplicationContext())
                        .load(HttpConfig.IMAGEHOST + list.get(position).getType_icon()).into(holder.iv_photo);
            }
            return convertView;
        }
    }

    private class MyViewHolder {
        ImageView iv_photo;
        TextView tv_name;

        public MyViewHolder(View view) {
            iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}
