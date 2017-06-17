package com.lsjr.zizisteward.ly.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.hyphenate.chatuidemo.ui.MainActivity;
import com.hyphenate.chatuidemo.ui.UserProfileActivity;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.SimpleVoiceActivity;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.AddressBookBean;
import com.lsjr.zizisteward.bean.AddressBookBean.Friends;
import com.lsjr.zizisteward.bean.UserListBean.UserList;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.lsjr.zizisteward.utils.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_AddressBook extends Fragment implements OnClickListener {

    private View rootView;
    private SideBar sidebar;
    private TextView dialog;
    private ImageView iv_clear;
    private EditText et_search;
    private LinearLayout ll_add;
    private LinearLayout ll_set;
    private LinearLayout ll_back;
    private LinearLayout ll_clear;
    private LinearLayout ll_prompt;
    private LinearLayout ll_my_card;
    private RelativeLayout rl_parent;
    private PullToRefreshLayout ptrl;
    private LinearLayout ll_background;
    private LinearLayout ll_new_friends;
    private LinearLayout ll_address_book;
    private LinearLayout ll_delete_friends;
    public static com.lsjr.zizisteward.newview.MyListView slv_address_book;

    private int size = 0;
    public static Context context;
    protected InputMethodManager imm;

    public static FragmentAddressBookAdapter adapter;
    public static List<Friends> friends = new ArrayList<>();
    private List<Friends> friends_space = new ArrayList<>();
    private static AddressBookBean abBean;

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (null == rootView) {

            this.rootView = inflater.inflate(R.layout.fragment_address_book, null);

            this.findViewById();
        }

        return rootView;
    }

    private void findViewById() {

        this.context = getContext();

        this.requestAddressBook();

        this.dialog = (TextView) rootView.findViewById(R.id.dialog);
        this.sidebar = (SideBar) rootView.findViewById(R.id.sidebar);
        this.ll_set = (LinearLayout) rootView.findViewById(R.id.ll_set);
        this.ll_add = (LinearLayout) rootView.findViewById(R.id.ll_add);
        this.iv_clear = (ImageView) rootView.findViewById(R.id.iv_clear);
        this.et_search = (EditText) rootView.findViewById(R.id.et_search);
        this.ll_back = (LinearLayout) rootView.findViewById(R.id.ll_back);
        this.ll_clear = (LinearLayout) rootView.findViewById(R.id.ll_clear);
        this.ll_prompt = (LinearLayout) rootView.findViewById(R.id.ll_prompt);
        this.ll_my_card = (LinearLayout) rootView.findViewById(R.id.ll_my_card);
        this.rl_parent = (RelativeLayout) rootView.findViewById(R.id.rl_parent);
        this.ptrl = (PullToRefreshLayout) rootView.findViewById(R.id.refresh_view);
        this.ll_background = (LinearLayout) rootView.findViewById(R.id.ll_background);
        this.ll_new_friends = (LinearLayout) rootView.findViewById(R.id.ll_new_friends);
        this.ll_address_book = (LinearLayout) rootView.findViewById(R.id.ll_address_book);
        this.slv_address_book = (com.lsjr.zizisteward.newview.MyListView) rootView.findViewById(R.id.slv_address_book);
        this.ll_delete_friends = (LinearLayout) rootView.findViewById(R.id.ll_delete_friends);
        this.imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        this.ll_set.setOnClickListener(this);
        this.ll_add.setOnClickListener(this);
        this.ll_back.setOnClickListener(this);
        this.ll_clear.setOnClickListener(this);
        this.rl_parent.setOnClickListener(this);
        this.ll_my_card.setOnClickListener(this);
        this.ll_background.setOnClickListener(this);
        this.ll_new_friends.setOnClickListener(this);
        this.ll_address_book.setOnClickListener(this);
        this.ll_delete_friends.setOnClickListener(this);

        this.ptrl.setOnRefreshListener((PullToRefreshLayout.OnRefreshListener) new MyListener());

        this.sidebar.setTextView(dialog);

        this.sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {

                if (null != friends && friends.size() > 0) {
                    int pos = adapter.getPositionForSelection(s.charAt(0));

                    if (pos != -1) {
                        slv_address_book.setSelection(pos);
                    }
                }
            }
        });

        this.et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                List<Friends> list_space = new ArrayList<>();
                for (int i = 0; i < friends.size(); i++) {
                    if (friends.get(i).getUser_name().contains(s)
                            || friends.get(i).getUser_name().toUpperCase().contains(s)
                            || friends.get(i).getUser_name().toLowerCase().contains(s)
                            || friends.get(i).getFirstPinYin().contains(s)
                            || friends.get(i).getFirstPinYin().toLowerCase().contains(s)) {
                        list_space.add(friends.get(i));
                    }
                }

                if (list_space.size() < 1) {
                    ptrl.setVisibility(View.GONE);
                    ll_prompt.setVisibility(View.VISIBLE);
                } else {
                    ptrl.setVisibility(View.VISIBLE);
                    ll_prompt.setVisibility(View.GONE);
                }

                adapter = new FragmentAddressBookAdapter(getActivity(), list_space);

                slv_address_book.setAdapter(adapter);

                adapter.notifyDataSetChanged();

                if (s.length() > 0) {
                    iv_clear.setVisibility(View.VISIBLE);
                    ll_background.setVisibility(View.GONE);
                    ll_new_friends.setVisibility(View.GONE);
                    ll_delete_friends.setVisibility(View.GONE);
                } else {
                    iv_clear.setVisibility(View.GONE);
                    ll_background.setVisibility(View.VISIBLE);
                    ll_new_friends.setVisibility(View.VISIBLE);
                    ll_delete_friends.setVisibility(View.VISIBLE);
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

        this.slv_address_book.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
                if (position == 0) {
                    startActivity(new Intent(getActivity(), UserProfileActivity.class)
                            .putExtra("_id", abBean.getUserUser_id())
                            .putExtra("activity", "AB"));
                } else {
                    startActivity(new Intent(getActivity(), UserProfileActivity.class)
                            .putExtra("friend_id", friends.get(position).getFriend_id())
                            .putExtra("user_id", friends.get(position).getUser_id())
                            .putExtra("img", friends.get(position).getPhoto())
                            .putExtra("name", friends.get(position).getUser_name())
                            .putExtra("account", friends.get(position).getAccount())
                            .putExtra("activity", "AddressBook"));
                }
            }
        });
    }

    private class MyListener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            requestAddressBook_one();
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    }

    private void requestAddressBook_one() {

        Map<String, String> map = new HashMap<>();
        map.put("OPT", "206");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getActivity(), null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        AddressBookBean abBean = new Gson().fromJson(result, AddressBookBean.class);

                        SimpleVoiceActivity.friends = abBean.getFriends();

                        if (null != abBean) {

                            if (null != abBean.getFriends()) {
                                friends = new ArrayList<>();
                                friends = abBean.getFriends();

                                Collections.sort(friends, new PinyinComparator());

                                Friends fs = new Friends();
                                fs.setId(abBean.getUserUser_id());
                                fs.setUser_name(abBean.getUserUser_name());
                                fs.setPhoto(abBean.getUserPhoto());
                                fs.setAccount(PreferencesUtils.getString(getActivity(), "user_account"));
                                fs.setFriend_id("me");
                                fs.setFirstPinYin("#");
                                fs.setPinYin("#");
                                friends.add(0, fs);

                                adapter = new FragmentAddressBookAdapter(getActivity(), friends);

                                slv_address_book.setAdapter(adapter);

                                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            }
                        }
                    }

                    @Override
                    public void onFailure(MyError myError) {

                        super.onFailure(myError);
                    }
                });
    }

    public static void requestAddressBook() {

        Map<String, String> map = new HashMap<>();
        map.put("OPT", "206");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(context, null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {

                        abBean = new Gson().fromJson(result, AddressBookBean.class);

                        SimpleVoiceActivity.friends = abBean.getFriends();

                        if (null != abBean) {

                            if (null != abBean.getFriends()) {
                                friends = abBean.getFriends();

                                List<UserList> UserList;
                                Collections.sort(friends, new PinyinComparator());
                                Collections.sort(friends, new PinyinComparator());

                                Friends fs = new Friends();
                                fs.setId(abBean.getUserUser_id());
                                fs.setUser_name(abBean.getUserUser_name());
                                fs.setPhoto(abBean.getUserPhoto());
                                fs.setAccount(PreferencesUtils.getString(context, "user_account"));
                                fs.setFriend_id("me");
                                fs.setFirstPinYin("#");
                                fs.setPinYin("#");
                                friends.add(0, fs);

                                adapter = new FragmentAddressBookAdapter(context, friends);

                                slv_address_book.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(MyError myError) {

                        super.onFailure(myError);
                    }
                });
    }

    public static class FragmentAddressBookAdapter extends BaseAdapter {

        private Context context;
        private ViewHolder view;
        private List<Friends> friends;

        private FragmentAddressBookAdapter(Context context, List<Friends> friends) {
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

                convertView = LayoutInflater.from(context).inflate(R.layout.fragment_address_book_item, null);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {

                view = (ViewHolder) convertView.getTag();

            }

            if (friends.get(position).getFriend_id().equals("me")) {
                view.tv_lv_item_tag.setVisibility(View.GONE);
            } else {
                int selection = friends.get(position).getFirstPinYin().charAt(0);
                int positionForSelection = getPositionForSelection(selection);

                if (position == positionForSelection) {
                    view.tv_lv_item_tag.setVisibility(View.VISIBLE);
                    view.tv_lv_item_tag.setText(friends.get(position).getFirstPinYin());
                } else {
                    view.tv_lv_item_tag.setVisibility(View.GONE);
                }
            }

            if (null != friends.get(position).getFriend_name() && friends.get(position).getFriend_name().length() > 0) {
                view.tv_name.setText(friends.get(position).getUser_name() + " (" + friends.get(position).getFriend_name() + ")");
            } else {
                view.tv_name.setText(friends.get(position).getUser_name());
            }

            Glide.with(context).load(HttpConfig.IMAGEHOST + friends.get(position).getPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.icon_head).into(view.iv_head);

            return convertView;
        }

        private class ViewHolder {

            private RoundImageView iv_head;
            private TextView tv_name;
            private TextView tv_lv_item_tag;

            public ViewHolder(View v) {
                this.iv_head = (RoundImageView) v.findViewById(R.id.iv_head);
                this.tv_name = (TextView) v.findViewById(R.id.tv_name);
                this.tv_lv_item_tag = (TextView) v.findViewById(R.id.tv_lv_item_tag);
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
    public void onStop() {
        imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
        super.onStop();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ll_background:
                startActivity(new Intent(getContext(),
                        SearchAddFriendActivity.class));
                break;

            case R.id.ll_set:
                startActivity(new Intent(getActivity(), PersonalInformationActivity.class));
                break;

            case R.id.ll_new_friends:

                startActivityForResult(new Intent(getActivity(), NewFriendWayActivity.class), 1);

                break;

            case R.id.ll_delete_friends:

                startActivity(new Intent(getActivity(), BatchDeleteActivity.class));

                break;

            case R.id.ll_back:

                MainActivity.dismiss();

                break;

            case R.id.ll_clear:

                ll_new_friends.setVisibility(View.VISIBLE);
                ll_delete_friends.setVisibility(View.VISIBLE);

                ptrl.setVisibility(View.VISIBLE);
                ll_prompt.setVisibility(View.GONE);

                this.et_search.setText("");

                adapter = new FragmentAddressBookAdapter(getActivity(), friends);

                slv_address_book.setAdapter(adapter);

                imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);

                break;

            case R.id.ll_add:

                startActivityForResult(new Intent(getActivity(), Activity_AddFriends.class), 1);

                break;

            case R.id.rl_parent:

                imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);

                break;

            case R.id.ll_my_card:

                startActivity(new Intent(getContext(), CardHolderActivity.class));

                imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
                break;

            case R.id.ll_address_book:

                startActivity(new Intent(getContext(), ReadAddressBook.class));

                break;
        }
    }
}
