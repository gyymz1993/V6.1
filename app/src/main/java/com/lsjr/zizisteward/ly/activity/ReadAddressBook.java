package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.ActivityCompat;
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
import com.lsjr.zizisteward.bean.UserListBean;
import com.lsjr.zizisteward.bean.UserListBean.UserList;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.newview.MyListView;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadAddressBook extends Activity {

    private Adapter adapter;
    private TextView tv;
    private LinearLayout ll;
    private EditText et;
    private LinearLayout ll_back;
    protected InputMethodManager imm;
    private List<PhoneInfo> list;
    private List<UserList> UserList = new ArrayList<>();
    private PullToRefreshLayout ptrl;
    protected static com.lsjr.zizisteward.newview.MyListView mlv;
    private LinearLayout ll_more;
    private int count = 30;
    private int pageNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.read_address_book);

        ptrl = (PullToRefreshLayout) findViewById(R.id.refresh_view);
        mlv = (MyListView) findViewById(R.id.mlv);
        ll = (LinearLayout) findViewById(R.id.ll);
        et = (EditText) findViewById(R.id.et);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_more = (LinearLayout) findViewById(R.id.ll_more);

        ActivityCompat.requestPermissions(ReadAddressBook.this,
                new String[]{android.Manifest.permission.READ_CONTACTS}, 1);

        imm = (InputMethodManager) ReadAddressBook.this
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);

        Calculate(getNumber(), 0);

        tv = (TextView) findViewById(R.id.tv);
        tv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ActivityCompat
                        .requestPermissions(
                                ReadAddressBook.this,
                                new String[]{android.Manifest.permission.READ_CONTACTS},
                                1);
            }
        });

        ll_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                finish();
            }
        });

        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                List<UserList> ul_space = new ArrayList<>();

                for (int i = 0; i < UserList.size(); i++) {

                    if (UserList.get(i).getName().contains(s)
                            || UserList.get(i).getName().toLowerCase()
                            .contains(s)
                            || UserList.get(i).getName().toUpperCase()
                            .contains(s)
                            || UserList.get(i).getUser_name().contains(s)
                            || UserList.get(i).getUser_name().toLowerCase()
                            .contains(s)
                            || UserList.get(i).getUser_name().toUpperCase()
                            .contains(s)
                            || UserList.get(i).getFpinyin().contains(s)
                            || UserList.get(i).getFpinyin().toLowerCase()
                            .contains(s)
                            || UserList.get(i).getFpinyin().toUpperCase()
                            .contains(s)
                            || UserList.get(i).getPinyin().contains(s)
                            || UserList.get(i).getPinyin().toLowerCase()
                            .contains(s)
                            || UserList.get(i).getPinyin().toUpperCase()
                            .contains(s)) {
                        ul_space.add(UserList.get(i));
                    }

                    adapter = new Adapter(ReadAddressBook.this, ul_space);

                    mlv.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
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

        this.ptrl.setOnRefreshListener(new MyListener());
    }

    private void Calculate(List<PhoneInfo> list, int space) {

        if (null != list && list.size() > 0) {

            ll.setVisibility(View.GONE);
            mlv.setVisibility(View.VISIBLE);

            String phone = "";
            String said = "";

            if (list.size() <= count) {


                for (int i = 0; i < list.size(); i++) {

                    if (phone.equals("")) {

                        phone = list.get(i).getNumber();

                    } else {

                        phone += "," + list.get(i).getNumber();

                    }

                    if (said.equals("")) {

                        said = list.get(i).getName();

                    } else {

                        said += "," + list.get(i).getName();

                    }
                }

                getAddressBook(phone, said, 0);

            } else {

                //首先计算余数
                int remainder = list.size() % count;

                //再计算减去余数后能被除几次
                int number = (list.size() - remainder) / count;

                if (pageNum <= number) {

                    for (int i = (pageNum - 1) * count; i < pageNum * count; i++) {

                        if (phone.equals("")) {

                            phone = list.get(i).getNumber();

                        } else {

                            phone += "," + list.get(i).getNumber();

                        }

                        if (said.equals("")) {

                            said = list.get(i).getName();

                        } else {

                            said += "," + list.get(i).getName();

                        }
                    }

                } else {

                    for (int i = (pageNum - 1) * count; i < list.size(); i++) {

                        if (phone.equals("")) {

                            phone = list.get(i).getNumber();

                        } else {

                            phone += "," + list.get(i).getNumber();

                        }

                        if (said.equals("")) {

                            said = list.get(i).getName();

                        } else {

                            said += "," + list.get(i).getName();

                        }
                    }

                }

            }

            System.out.println("said: " + said);
            System.out.println("phone: " + phone);

            if (said.length() <= 0 || phone.length() <= 0) {

                ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);

                ll_more.setVisibility(View.GONE);

            } else {

                ll_more.setVisibility(View.VISIBLE);

                getAddressBook(phone, said, space);

            }

        } else {
            ll.setVisibility(View.VISIBLE);
            mlv.setVisibility(View.GONE);
        }
    }

    private void AddFriends(String id) {
        CustomDialogUtils
                .startCustomProgressDialog(ReadAddressBook.this, "请稍候");
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "248");
        map.put("user_id",
                Fragment_ChatList.addSign(
                        Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("friend_id", id);
        new HttpClientGet(ReadAddressBook.this, null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        CustomDialogUtils
                                .stopCustomProgressDialog(ReadAddressBook.this);
                        try {
                            JSONObject jObject = new JSONObject(result);
                            String error = jObject.getString("error");
                            if (error.equals("1")) {
                                Toast.makeText(ReadAddressBook.this,
                                        "已向对方提交申请,请等待对方同意...", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ReadAddressBook.this,
                                        "提交申请失败...", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(MyError myError) {
                        CustomDialogUtils
                                .stopCustomProgressDialog(ReadAddressBook.this);
                        super.onFailure(myError);
                    }
                });
    }

    private List<PhoneInfo> getNumber() {

        List<PhoneInfo> list = null;

        list = getPhoneNumberFromMobile(ReadAddressBook.this);

        return list;
    }

    private void getAddressBook(String phone, String said, final int space) {
        // CustomDialogUtils.startCustomProgressDialog(ReadAddressBook.this,
        // "请稍候");
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "323");
        map.put("user_id",
                Fragment_ChatList.addSign(
                        Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("phone", phone);
        map.put("said", said);
        new HttpClientGet(ReadAddressBook.this, null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        // CustomDialogUtils.stopCustomProgressDialog(ReadAddressBook.this);
                        System.out.println(result);

                        UserListBean ulBean = new Gson().fromJson(result, UserListBean.class);

                        switch (space) {
                            case 0:
                                //第一次加载

                                UserList = null;

                                adapter = null;

                                UserList = ulBean.getUserList();

                                Collections.sort(UserList, new PinyinComparatorRead());

                                adapter = new Adapter(ReadAddressBook.this, UserList);

                                mlv.setAdapter(adapter);

                                adapter.notifyDataSetChanged();

                                break;

                            case 1:
                                //刷新

                                UserList = null;

                                adapter = null;

                                UserList = ulBean.getUserList();

                                Collections.sort(UserList, new PinyinComparatorRead());

                                adapter = new Adapter(ReadAddressBook.this, UserList);

                                mlv.setAdapter(adapter);

                                adapter.notifyDataSetChanged();

                                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);

                                break;

                            case 2:
                                //加载更多

                                List<UserList> space_list = null;

                                space_list = ulBean.getUserList();

                                if (null != space_list) {

                                    UserList.addAll(space_list);

                                    Collections.sort(UserList, new PinyinComparatorRead());

                                    adapter = new Adapter(ReadAddressBook.this, UserList);

                                    mlv.setAdapter(adapter);

                                    adapter.notifyDataSetChanged();
                                }

                                ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);

                                break;
                        }
                    }

                    @Override
                    public void onFailure(MyError myError) {
                        // CustomDialogUtils.stopCustomProgressDialog(ReadAddressBook.this);
                        super.onFailure(myError);
                    }
                });
    }

    private class MyListener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            pageNum = 1;
            Calculate(getNumber(), 1);
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            pageNum++;
            Calculate(getNumber(), 2);
        }
    }

    private class Adapter extends BaseAdapter {

        private Context context;
        private List<UserList> list;
        private ViewHolder view;

        public Adapter(Context context, List<UserList> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return null == list ? 0 : list.size();
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

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(
                        R.layout.read_address_book_item, null);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {

                view = (ViewHolder) convertView.getTag();

            }

            int selection = list.get(position).getFpinyin().charAt(0);
            int positionForSelection = getPositionForSelection(selection);

            if (position == positionForSelection) {
                view.tv_lv_item_tag.setVisibility(View.VISIBLE);
                view.tv_lv_item_tag.setText(list.get(position).getFpinyin());
            } else {
                view.tv_lv_item_tag.setVisibility(View.GONE);
            }

            view.tv_name.setText(list.get(position).getUser_name());

            if (!list.get(position).getId().equals(App.getUserInfo().getId())) {

                switch (list.get(position).getState()) {
                    case "0":
                        // 用户不存在
                        view.iv_invitation.setVisibility(View.VISIBLE);
                        view.tv_have.setVisibility(View.GONE);
                        view.iv_add.setVisibility(View.GONE);
                        break;

                    case "1":
                        // 不是好友
                        view.iv_invitation.setVisibility(View.GONE);
                        view.iv_add.setVisibility(View.VISIBLE);
                        view.tv_have.setVisibility(View.GONE);
                        break;

                    case "2":
                        // 是好友
                        view.iv_invitation.setVisibility(View.GONE);
                        view.iv_add.setVisibility(View.GONE);
                        view.tv_have.setVisibility(View.VISIBLE);
                        break;
                }
            } else {
                view.iv_invitation.setVisibility(View.GONE);
                view.tv_have.setVisibility(View.GONE);
                view.iv_add.setVisibility(View.GONE);
            }

            view.iv_add.setTag(position);
            view.iv_add.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                    int pos = (int) v.getTag();
                    AddFriends(list.get(pos).getId());
                }
            });

            view.iv_invitation.setTag(position);
            view.iv_invitation.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                    int pos = (int) v.getTag();
                    GetSmsData(pos);
                }
            });

            return convertView;
        }

        private class ViewHolder {

            private ImageView iv;
            private TextView tv_name;
            private TextView tv_have;
            private ImageView iv_add;
            private ImageView iv_invitation;
            private TextView tv_lv_item_tag;

            public ViewHolder(View v) {
                this.iv = (ImageView) v.findViewById(R.id.iv);
                this.iv_add = (ImageView) v.findViewById(R.id.iv_add);
                this.tv_have = (TextView) v.findViewById(R.id.tv_have);
                this.tv_name = (TextView) v.findViewById(R.id.tv_name);
                this.iv_invitation = (ImageView) v
                        .findViewById(R.id.iv_invitation);
                this.tv_lv_item_tag = (TextView) v
                        .findViewById(R.id.tv_lv_item_tag);
            }
        }

        public int getPositionForSelection(int selection) {
            for (int i = 0; i < list.size(); i++) {
                String Fpinyin = list.get(i).getFpinyin();
                char first = Fpinyin.toUpperCase().charAt(0);
                if (first == selection) {
                    return i;
                }
            }
            return -1;

        }
    }

    public void GetSmsData(final int pos) {

        CustomDialogUtils.startCustomProgressDialog(ReadAddressBook.this, "请稍候");

        Map<String, String> map = new HashMap<>();
        map.put("OPT", "324");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("system", "Android");
        map.put("mobiles", UserList.get(pos).getName());
        new HttpClientGet(ReadAddressBook.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                CustomDialogUtils.stopCustomProgressDialog(ReadAddressBook.this);
                System.out.println(result);
                try {
                    JSONObject jObject = new JSONObject(result);

                    String error = jObject.getString("error");

                    if (error.equals("1")) {
                        Toast.makeText(ReadAddressBook.this, "邀请短信发送成功!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReadAddressBook.this, "网络异常,请重试!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(MyError myError) {
                CustomDialogUtils.stopCustomProgressDialog(ReadAddressBook.this);
                super.onFailure(myError);
            }
        });
    }

    public static List<PhoneInfo> getPhoneNumberFromMobile(Context context) {
        // 已授权
        List<PhoneInfo> list = new ArrayList<PhoneInfo>();
        Cursor cursor = context.getContentResolver().query(Phone.CONTENT_URI,
                null, null, null, null);
        // moveToNext方法返回的是一个boolean类型的数据
        while (cursor.moveToNext()) {
            // 读取通讯录的姓名
            String name = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));

            // 读取通讯录的号码
            String number = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));

            String photo = cursor.getString(cursor.getColumnIndex(Phone.PHOTO_URI));

            PhoneInfo phoneInfo = new PhoneInfo(name, number, photo);

            list.add(phoneInfo);
        }

        cursor.close();

        return list;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {

        if (requestCode == 1) {

            Calculate(getNumber(), 0);
        }
    }

    public static class PhoneInfo {
        private String name;
        private String number;
        private String photo;

        public PhoneInfo(String name, String number, String photo) {
            this.name = name;
            this.number = number;
            this.photo = photo;
        }

        public String getName() {
            return name;
        }

        public String getNumber() {
            return number;
        }

        public String getPhoto() {
            return photo;
        }
    }

}
