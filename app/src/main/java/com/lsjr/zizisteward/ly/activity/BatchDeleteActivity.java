package com.lsjr.zizisteward.ly.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.AddressBookBean;
import com.lsjr.zizisteward.bean.AddressBookBean.Friends;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.squareup.picasso.Picasso;

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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BatchDeleteActivity extends Activity implements OnClickListener {

    private SideBar sidebar;
    private TextView dialog;
    private ImageView iv_clear;
    private EditText et_search;
    private LinearLayout ll_sure;
    private LinearLayout ll_clear;
    private com.lsjr.zizisteward.newview.MyListView mlv_people;
    private LinearLayout ll_back;
    private InputMethodManager imm;
    private LinearLayout ll_background;

    private FCLAdapter adapter;

    public static List<Friends> friends = new ArrayList<>();
    private List<Friends> friends_space = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.batch_delete_activity);
        this.findViewById();
    }

    private void findViewById() {
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.dialog = (TextView) super.findViewById(R.id.dialog);
        this.sidebar = (SideBar) super.findViewById(R.id.sidebar);
        this.iv_clear = (ImageView) super.findViewById(R.id.iv_clear);
        this.et_search = (EditText) super.findViewById(R.id.et_search);
        this.ll_sure = (LinearLayout) super.findViewById(R.id.ll_sure);
        this.ll_clear = (LinearLayout) super.findViewById(R.id.ll_clear);
        this.mlv_people = (com.lsjr.zizisteward.newview.MyListView) super.findViewById(R.id.mlv_people);
        this.ll_background = (LinearLayout) super.findViewById(R.id.ll_background);

        this.ll_back.setOnClickListener(this);
        this.ll_sure.setOnClickListener(this);
        this.ll_clear.setOnClickListener(this);

        getData();

        this.imm = (InputMethodManager) BatchDeleteActivity.this
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        this.et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                List<Friends> list_space = new ArrayList<>();
                for (int i = 0; i < friends.size(); i++) {
                    if (friends.get(i).getUser_name().contains(s)
                            || friends.get(i).getUser_name().toUpperCase()
                            .contains(s)
                            || friends.get(i).getUser_name().toLowerCase()
                            .contains(s)
                            || friends.get(i).getFirstPinYin().contains(s)
                            || friends.get(i).getFirstPinYin().toLowerCase()
                            .contains(s)) {
                        list_space.add(friends.get(i));
                    }
                }

                adapter = new FCLAdapter(BatchDeleteActivity.this, list_space);

                mlv_people.setAdapter(adapter);

                adapter.notifyDataSetChanged();

                if (s.length() > 0) {
                    iv_clear.setVisibility(View.VISIBLE);
                    ll_background.setVisibility(View.GONE);
                } else {
                    iv_clear.setVisibility(View.GONE);
                    ll_background.setVisibility(View.VISIBLE);
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

        this.mlv_people.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (friends.get(position).getType() != 2) {
                    friends.get(position).setType(
                            0 == friends.get(position).getType() ? 1 : 0);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
        super.onStop();
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "206");
        map.put("user_id",
                Fragment_ChatList.addSign(
                        Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(BatchDeleteActivity.this, null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        AddressBookBean abBean = new Gson().fromJson(result, AddressBookBean.class);

                        if (null != abBean) {
                            if (null != abBean.getFriends()) {
                                friends = abBean.getFriends();
                                Collections.sort(friends, new PinyinComparator());
                                for (int i = 0; i < friends.size(); i++) {
                                    friends.get(i).setType(0);
                                }
                                adapter = new FCLAdapter(BatchDeleteActivity.this, friends);
                                mlv_people.setAdapter(adapter);
                            }
                        }
                    }

                    @Override
                    public void onFailure(MyError myError) {

                        super.onFailure(myError);
                    }
                });
    }

    private class FCLAdapter extends BaseAdapter {

        private ViewHolder view;
        private Context context;
        private List<Friends> friends;

        public FCLAdapter(Context context, List<Friends> friends) {
            this.context = context;
            this.friends = friends;
        }

        @Override
        public int getCount() {
            return null == friends ? 0 : friends.size();
        }

        @Override
        public Object getItem(int position) {
            return friends.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(
                        R.layout.friend_circle_list_item, null);

                view = new ViewHolder(convertView);

                convertView.setTag(view);
            } else {
                view = (ViewHolder) convertView.getTag();
            }

            switch (friends.get(position).getType()) {
                case 0:
                    view.iv_select
                            .setImageResource(R.drawable.icon_check_select_false);
                    break;

                case 1:
                    view.iv_select
                            .setImageResource(R.drawable.icon_check_select_true);
                    break;
            }

            // 获取首字母的assii值
            int selection = friends.get(position).getFirstPinYin().charAt(0);
            // 通过首字母的assii值来判断是否显示字母
            int positionForSelection = getPositionForSelection(selection);

            if (position == positionForSelection) {// 相等说明需要显示字母
                view.tv_lv_item_tag.setVisibility(View.VISIBLE);
                view.tv_lv_item_tag.setText(friends.get(position)
                        .getFirstPinYin());
            } else {
                view.tv_lv_item_tag.setVisibility(View.GONE);
            }

            view.tv_name.setText(friends.get(position).getUser_name());
            Picasso.with(context)
                    .load(HttpConfig.IMAGEHOST
                            + friends.get(position).getPhoto())
                    .into(view.iv_head);

            return convertView;
        }

        private class ViewHolder {

            private RoundImageView iv_head;
            private TextView tv_name;
            private TextView tv_lv_item_tag;
            private ImageView iv_select;

            public ViewHolder(View v) {
                this.tv_name = (TextView) v.findViewById(R.id.tv_name);
                this.iv_select = (ImageView) v.findViewById(R.id.iv_select);
                this.iv_head = (RoundImageView) v.findViewById(R.id.iv_head);
                this.tv_lv_item_tag = (TextView) v
                        .findViewById(R.id.tv_lv_item_tag);
            }
        }

        public int getPositionForSelection(int selection) {
            for (int i = 0; i < friends.size(); i++) {
                String Fpinyin = friends.get(i).getFirstPinYin();
                char first = Fpinyin.toUpperCase().charAt(0);
                if (first == selection) {
                    return i;
                }
            }
            return -1;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ll_back:
                imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
                finish();
                break;

            case R.id.ll_sure:

                imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);

                String space = "";

                for (int i = 0; i < friends.size(); i++) {
                    if (friends.get(i).getType() == 1) {
                        if (space.equals("")) {
                            space = friends.get(i).getFriend_id();
                        } else {
                            space += "," + friends.get(i).getFriend_id();
                        }
                    }
                }

                if (space.length() < 1) {
                    Toast.makeText(BatchDeleteActivity.this, "没有选中任何朋友...", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, String> map = new HashMap<>();
                    map.put("OPT", "207");
                    map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                    map.put("delete_id", space);
                    new HttpClientGet(BatchDeleteActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            Fragment_AddressBook.requestAddressBook();
                            finish();
                        }
                    });
                }

                break;
        }
    }
}
