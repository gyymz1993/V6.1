package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchAddFriendActivity extends Activity implements OnClickListener {
    private MyListView mlv;
    private EditText et_search;
    private ImageView iv_clear;
    private LinearLayout ll_back;
    private LinearLayout ll_search;
    private Adapter adapter;
    private List<SeekUserBean.SeekUser> seekUser = new ArrayList<>();
    protected InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.search_add_friend_activity);
        this.findViewById();
    }

    private void findViewById() {
        this.mlv = (MyListView) super.findViewById(R.id.mlv);
        this.iv_clear = (ImageView) super.findViewById(R.id.iv_clear);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.et_search = (EditText) super.findViewById(R.id.et_search);
        this.ll_search = (LinearLayout) super.findViewById(R.id.ll_search);

        this.ll_back.setOnClickListener(this);
        this.iv_clear.setOnClickListener(this);
        this.ll_search.setOnClickListener(this);

        this.imm = (InputMethodManager) SearchAddFriendActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);

        this.et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    ll_search.setVisibility(View.VISIBLE);
                    iv_clear.setVisibility(View.VISIBLE);
                } else {
                    iv_clear.setVisibility(View.GONE);
                    ll_search.setVisibility(View.GONE);
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

    private void getData() {

        String seekContent = et_search.getText().toString();

        if (null == seekContent || seekContent.length() < 1) {
            Toast.makeText(SearchAddFriendActivity.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("OPT", "247");
            map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
            map.put("seekContent", seekContent);
            new HttpClientGet(SearchAddFriendActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                @Override
                public void onSuccess(String result) {
                    SeekUserBean sub = new Gson().fromJson(result, SeekUserBean.class);
                    seekUser = sub.getSeekUser();
                    adapter = new Adapter(SearchAddFriendActivity.this, seekUser);
                    mlv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(MyError myError) {
                    super.onFailure(myError);
                }
            });
        }

    }

    private class Adapter extends BaseAdapter {

        private Context context;
        private ViewHolder view;
        private List<SeekUserBean.SeekUser> seekUser;

        public Adapter(Context context, List<SeekUserBean.SeekUser> seekUser) {
            this.context = context;
            this.seekUser = seekUser;
        }

        @Override
        public int getCount() {
            return null == seekUser ? 0 : seekUser.size();
        }

        @Override
        public Object getItem(int position) {
            return seekUser.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {
                convertView = LayoutInflater.from(context).inflate(R.layout.search_add_friend_acitvity_item, null);
                view = new ViewHolder(convertView);
                convertView.setTag(view);
            } else {
                view = (ViewHolder) convertView.getTag();
            }

            view.tv_name.setText(seekUser.get(position).getUser().getUser_name());
            Picasso.with(context).load(HttpConfig.IMAGEHOST + seekUser.get(position).getUser().getPhoto()).into(view.riv_head);

            if (seekUser.get(position).getUser().getName().equals(PreferencesUtils.getString(context, "user_account"))) {
                view.tv_add.setVisibility(View.GONE);
                view.tv.setText("不能添加自己");
                view.tv.setVisibility(View.VISIBLE);
            } else {
                if (seekUser.get(position).getType().equals("0")) {
                    view.tv_add.setVisibility(View.VISIBLE);
                    view.tv.setVisibility(View.GONE);
                } else {
                    view.tv_add.setVisibility(View.GONE);
                    view.tv.setText("已添加");
                    view.tv.setVisibility(View.VISIBLE);
                }
            }

            view.tv_add.setTag(position);
            view.tv_add.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    CustomDialogUtils.startCustomProgressDialog(SearchAddFriendActivity.this, "请稍候...");
                    int pos = (int) v.getTag();
                    Map<String, String> map = new HashMap<>();
                    map.put("OPT", "248");
                    map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                    map.put("friend_id", seekUser.get(pos).getUser().getId());
                    new HttpClientGet(SearchAddFriendActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {

                            try {
                                CustomDialogUtils.stopCustomProgressDialog(SearchAddFriendActivity.this);
                                JSONObject jObject = new JSONObject(result);
                                String error = jObject.getString("error");
                                if (error.equals("1")) {
                                    Toast.makeText(SearchAddFriendActivity.this, "已向对方提交申请,请等待对方同意...", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SearchAddFriendActivity.this, "提交申请失败...", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                CustomDialogUtils.stopCustomProgressDialog(SearchAddFriendActivity.this);
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(MyError myError) {
                            CustomDialogUtils.stopCustomProgressDialog(SearchAddFriendActivity.this);
                            super.onFailure(myError);
                        }
                    });
                }
            });

            return convertView;
        }

        private class ViewHolder {
            private RoundImageView riv_head;
            private TextView tv_name;
            private TextView tv_add;
            private TextView tv;

            public ViewHolder(View v) {
                this.tv = (TextView) v.findViewById(R.id.tv);
                this.tv_add = (TextView) v.findViewById(R.id.tv_add);
                this.tv_name = (TextView) v.findViewById(R.id.tv_name);
                this.riv_head = (RoundImageView) v.findViewById(R.id.riv_head);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
                this.finish();

                break;

            case R.id.ll_search:
                imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
                String content = et_search.getText().toString();

                if (null != content && content.length() > 0) {
                    getData();
                } else {
                    Toast.makeText(SearchAddFriendActivity.this, "搜索内容不能为空...", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.iv_clear:
                et_search.setText("");
                break;
        }
    }
}
