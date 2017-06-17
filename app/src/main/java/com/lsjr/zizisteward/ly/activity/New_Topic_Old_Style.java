package com.lsjr.zizisteward.ly.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.hyphenate.chatuidemo.ui.MainActivity;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.bean.GroupListBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.lsjr.zizisteward.utils.PullToRefreshLayout;
import com.lsjr.zizisteward.utils.PullableScrollView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/17.
 */

public class New_Topic_Old_Style extends Fragment implements View.OnClickListener {

    private View rootView;

    public static Context context;

    private LinearLayout ll_back;

    private LinearLayout ll_new_topic;

    /**管理*/
    private LinearLayout ll_manage;
    /**我创建的*/
    private LinearLayout ll_c_p;
    private static LinearLayout ll_space_add;
    private static MyListView mlv_create;
    public static New_Old_Create_Adapter create_adapter;
    public static List<GroupListBean.GroupList> list_create = new ArrayList<>();

    /**我加入的*/
    public static int i_join = 0;
    private static TextView tv_join;
    private LinearLayout ll_j;
    private LinearLayout ll_j_p;
    private TextView tv_j_pack_up;
    private LinearLayout ll_j_more;
    private static ImageView iv_join;
    private static MyListView mlv_join;
    private static RelativeLayout rl_join;
    private static RoundImageView riv_one;
    private static RoundImageView riv_two;
    private static RoundImageView riv_three;
    public static New_Old_Join_Adapter join_adapter;
    private static List<GroupListBean.GroupList> list_join = new ArrayList<>();

    /**朋友的*/
    public static int i_friends = 0;
    private LinearLayout ll_f;
    private static TextView tv_friends;
    private LinearLayout ll_f_p;
    private LinearLayout ll_f_more;
    private TextView tv_f_pack_up;
    private static MyListView mlv_friends;
    private LinearLayout ll_friends;
    private static RoundImageView riv_f_one;
    private static RoundImageView riv_f_two;
    private static RelativeLayout rl_friedns;
    private static RoundImageView riv_f_three;
    public static New_Old_Friends_Adapter friends_adapter;
    private static List<GroupListBean.GroupList> list_frineds = new ArrayList<>();

    private static int c_number = 0;
    private static int j_number = 0;

    private static boolean is_refrsh = false;

    private PullToRefreshLayout ptrl;

    public static void refreshData(){
        is_refrsh = true;
        MainActivity.unread_topic_number.setVisibility(View.INVISIBLE);
        MainActivity.unread_topic_number.setText("0");
        JoinRefreshData(i_join);
        CreateRefreshData();
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (null == rootView) {

            this.rootView = inflater.inflate(R.layout.new_topic_style, null);

            this.context = getContext();

            this.is_refrsh = false;

            this.c_number = 0;

            this.j_number = 0;

            this.i_join = 0;

            this.i_friends = 0;

            this.list_create = new ArrayList<>();
            this.create_adapter = null;

            this.list_join = new ArrayList<>();
            this.join_adapter = null;

            this.list_frineds = new ArrayList<>();
            this.friends_adapter = null;

            this.findViewById();

        }

        return rootView;
    }

    private void findViewById() {

        this.ptrl = (PullToRefreshLayout) rootView.findViewById(R.id.refresh_view);

        this.ll_j = (LinearLayout) rootView.findViewById(R.id.ll_j);
        this.ll_f = (LinearLayout) rootView.findViewById(R.id.ll_f);
        this.tv_join = (TextView) rootView.findViewById(R.id.tv_join);
        this.tv_join = (TextView) rootView.findViewById(R.id.tv_join);
        this.iv_join = (ImageView) rootView.findViewById(R.id.iv_join);
        this.ll_c_p = (LinearLayout) rootView.findViewById(R.id.ll_c_p);
        this.ll_j_p = (LinearLayout) rootView.findViewById(R.id.ll_j_p);
        this.ll_f_p = (LinearLayout) rootView.findViewById(R.id.ll_f_p);
        this.ll_back = (LinearLayout) rootView.findViewById(R.id.ll_back);
        this.mlv_join = (MyListView) rootView.findViewById(R.id.mlv_join);
        this.rl_join = (RelativeLayout) rootView.findViewById(R.id.rl_join);
        this.riv_one = (RoundImageView) rootView.findViewById(R.id.riv_one);
        this.riv_two = (RoundImageView) rootView.findViewById(R.id.riv_two);
        this.tv_friends = (TextView) rootView.findViewById(R.id.tv_friends);
        this.ll_f_more = (LinearLayout) rootView.findViewById(R.id.ll_f_more);
        this.ll_j_more = (LinearLayout) rootView.findViewById(R.id.ll_j_more);
        this.mlv_create = (MyListView) rootView.findViewById(R.id.mlv_create);
        this.ll_manage = (LinearLayout) rootView.findViewById(R.id.ll_manage);
        this.tv_j_pack_up = (TextView) rootView.findViewById(R.id.tv_j_pack_up);
        this.tv_f_pack_up = (TextView) rootView.findViewById(R.id.tv_f_pack_up);
        this.riv_f_one = (RoundImageView) rootView.findViewById(R.id.riv_f_one);
        this.riv_f_two = (RoundImageView) rootView.findViewById(R.id.riv_f_two);
        this.riv_three = (RoundImageView) rootView.findViewById(R.id.riv_three);
        this.mlv_friends = (MyListView) rootView.findViewById(R.id.mlv_friends);
        this.rl_friedns = (RelativeLayout) rootView.findViewById(R.id.rl_friedns);
        this.ll_new_topic = (LinearLayout) rootView.findViewById(R.id.ll_new_topic);
        this.ll_space_add = (LinearLayout) rootView.findViewById(R.id.ll_space_add);
        this.riv_f_three = (RoundImageView) rootView.findViewById(R.id.riv_f_three);

        this.CreateData();
        this.JoinData();
        this.FriendsData();

        this.ptrl.setOnRefreshListener(new MyListener());

        this.ll_j_p.setOnClickListener(this);
        this.ll_f_p.setOnClickListener(this);
        this.ll_back.setOnClickListener(this);
        this.ll_manage.setOnClickListener(this);
        this.ll_f_more.setOnClickListener(this);
        this.ll_j_more.setOnClickListener(this);
        this.ll_new_topic.setOnClickListener(this);
        this.ll_space_add.setOnClickListener(this);
        this.tv_j_pack_up.setOnClickListener(this);
        this.tv_f_pack_up.setOnClickListener(this);


        this.mlv_create.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                // it is group chat
                intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                intent.putExtra("userId", list_create.get(position).getGroup().getGroupId());
                intent.putExtra("guess", true);
                intent.putExtra("Groupid", list_create.get(position).getGroup().getGroupId());
                intent.putExtra("Groupname", list_create.get(position).getGroup().getGroupName());
                intent.putExtra("Groupmax", list_create.get(position).getGroup().getMaxusers());
                intent.putExtra("Groupisopen", list_create.get(position).getGroup().getIs_open());
                intent.putExtra("Groupmin", list_create.get(position).getSize());
                intent.putExtra("Groupisowner", list_create.get(position).getGroup().getIs_owner());
                intent.putExtra("Groupdescription", list_create.get(position).getGroup().getDescription());
                startActivityForResult(intent, 0);
            }
        });

        this.mlv_join.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                // it is group chat
                intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                intent.putExtra("userId", list_join.get(position).getGroup().getGroupId());
                intent.putExtra("guess", true);
                intent.putExtra("Groupid", list_join.get(position).getGroup().getGroupId());
                intent.putExtra("Groupname", list_join.get(position).getGroup().getGroupName());
                intent.putExtra("Groupmax", list_join.get(position).getGroup().getMaxusers());
                intent.putExtra("Groupisopen", list_join.get(position).getGroup().getIs_open());
                intent.putExtra("Groupmin", list_join.get(position).getSize());
                intent.putExtra("Groupisowner", list_join.get(position).getGroup().getIs_owner());
                intent.putExtra("Groupdescription", list_join.get(position).getGroup().getDescription());
                startActivityForResult(intent, 0);
            }
        });

        this.mlv_friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), NetworkInformationActivity.class);
                // it is group chat
                intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                intent.putExtra("userId", list_frineds.get(position).getGroup().getGroupId());
                intent.putExtra("guess", true);
                intent.putExtra("Groupid", list_frineds.get(position).getGroup().getGroupId());
                intent.putExtra("Groupname", list_frineds.get(position).getGroup().getGroupName());
                intent.putExtra("Groupmax", list_frineds.get(position).getGroup().getMaxusers());
                intent.putExtra("Groupisopen", list_frineds.get(position).getGroup().getIs_open());
                intent.putExtra("Groupmin", list_frineds.get(position).getSize());
                intent.putExtra("Groupisowner", list_frineds.get(position).getGroup().getIs_owner());
                intent.putExtra("Groupdescription", list_frineds.get(position).getGroup().getDescription());
                intent.putExtra("activity", "friend");
                startActivityForResult(intent, 0);
            }
        });
    }

    private class MyListener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {


            if(i_friends == 1) {
                ll_c_p.setVisibility(View.VISIBLE);
                ll_j_p.setVisibility(View.VISIBLE);
                ll_f_p.setVisibility(View.VISIBLE);

                rl_friedns.setVisibility(View.VISIBLE);
                ll_f_more.setVisibility(View.VISIBLE);

                tv_f_pack_up.setVisibility(View.GONE);

                i_friends = 0;

                friends_adapter = null;

                list_frineds = new ArrayList<>();

                friends_adapter = new New_Old_Friends_Adapter(getContext(), list_frineds, i_friends);

                mlv_friends.setAdapter(friends_adapter);

                friends_adapter.notifyDataSetChanged();
            } else if (i_join == 1) {
                ll_c_p.setVisibility(View.VISIBLE);
                ll_f_p.setVisibility(View.VISIBLE);

                rl_join.setVisibility(View.VISIBLE);

                ll_j_more.setVisibility(View.VISIBLE);
                ll_j_p.setVisibility(View.VISIBLE);

                tv_j_pack_up.setVisibility(View.GONE);

                i_join = 0;

                if (j_number > 0) {
                    iv_join.setVisibility(View.VISIBLE);
                } else {
                    iv_join.setVisibility(View.GONE);
                }
                join_adapter = null;
                list_join = new ArrayList<>();
                join_adapter = new New_Old_Join_Adapter(getContext(), list_join, i_join);
                mlv_join.setAdapter(join_adapter);
                join_adapter.notifyDataSetChanged();
            }

            CreateData();
            JoinData();
            FriendsData();
            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    }

    public static void CreateData() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "409");
        map.put("name", PreferencesUtils.getString(context, "user_account"));
        new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("我创建的群：" + result);
                GroupListBean gBean = new Gson().fromJson(result,GroupListBean.class);
                list_create = new ArrayList<>();
                list_create = gBean.getGrouplist();

                if (null != list_create && list_create.size() > 0) {
                    ll_space_add.setVisibility(View.GONE);
                    mlv_create.setVisibility(View.VISIBLE);
                    CreateRefreshData();
                } else {
                    ll_space_add.setVisibility(View.VISIBLE);
                    mlv_create.setVisibility(View.GONE);
                }

            }
        });
    }

    public static void JoinData() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "410");
        map.put("name",PreferencesUtils.getString(context, "user_account"));
        new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("我加入的群：" + result);
                GroupListBean gBean = new Gson().fromJson(result,GroupListBean.class);
                list_join = new ArrayList<>();
                list_join = gBean.getGrouplist();

                if (null != list_join && list_join.size() > 0) {

                    mlv_join.setVisibility(View.VISIBLE);

                    tv_join.setText(String.valueOf(list_join.size()));

                    for (int i = 0; i < list_join.size(); i++) {

                        switch (i) {

                            case 0:

                                if (i_join == 0) {
                                    rl_join.setVisibility(View.VISIBLE);
                                    riv_one.setVisibility(View.VISIBLE);
                                    riv_two.setVisibility(View.GONE);
                                    riv_three.setVisibility(View.GONE);
                                }

                                Picasso.with(context).load(HttpConfig.IMAGEHOST + list_join.get(i).getOwnerPhoto()).into(riv_one);

                                break;

                            case 1:

                                if (i_join == 0) {
                                    riv_two.setVisibility(View.VISIBLE);
                                    riv_three.setVisibility(View.GONE);
                                }

                                Picasso.with(context).load(HttpConfig.IMAGEHOST + list_join.get(i).getOwnerPhoto()).into(riv_two);

                                break;

                            case 2:

                                if (i_join == 0) {
                                    riv_three.setVisibility(View.VISIBLE);
                                }

                                Picasso.with(context).load(HttpConfig.IMAGEHOST + list_join.get(i).getOwnerPhoto()).into(riv_three);

                                break;
                        }
                    }

                    JoinRefreshData(i_join);

                } else {
                    mlv_join.setVisibility(View.GONE);
                    join_adapter = new New_Old_Join_Adapter(context, list_join, 1);
                    mlv_join.setAdapter(join_adapter);
                    join_adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public static void FriendsData() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "222");
        map.put("name",PreferencesUtils.getString(context, "user_account"));
        new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("朋友的群：" + result);
                GroupListBean gBean = new Gson().fromJson(result,GroupListBean.class);
                list_frineds = new ArrayList<>();
                list_frineds = gBean.getGrouplist();

                if (null != list_frineds && list_frineds.size() > 0) {

                    for (int i = 0; i < list_frineds.size(); i++) {

                        mlv_friends.setVisibility(View.VISIBLE);

                        tv_friends.setText(String.valueOf(list_frineds.size()));

                        switch (i) {

                            case 0:

                                if (i_friends == 0) {
                                    rl_friedns.setVisibility(View.VISIBLE);
                                    riv_f_one.setVisibility(View.VISIBLE);
                                    riv_f_two.setVisibility(View.GONE);
                                    riv_f_three.setVisibility(View.GONE);
                                }

                                Picasso.with(context).load(HttpConfig.IMAGEHOST + list_frineds.get(i).getOwnerPhoto()).into(riv_f_one);

                                break;

                            case 1:

                                if (i_friends == 0) {
                                    riv_f_two.setVisibility(View.VISIBLE);
                                    riv_f_three.setVisibility(View.GONE);
                                }

                                Picasso.with(context).load(HttpConfig.IMAGEHOST + list_frineds.get(i).getOwnerPhoto()).into(riv_f_two);

                                break;

                            case 2:

                                if (i_friends == 0) {
                                    riv_f_three.setVisibility(View.VISIBLE);
                                }

                                Picasso.with(context).load(HttpConfig.IMAGEHOST + list_frineds.get(i).getOwnerPhoto()).into(riv_f_three);

                                break;
                        }
                    }
                } else {
                    mlv_friends.setVisibility(View.GONE);
                }

                friends_adapter = new New_Old_Friends_Adapter(context, list_frineds, i_friends);
                mlv_friends.setAdapter(friends_adapter);
                friends_adapter.notifyDataSetChanged();
            }
        });
    }

    public static void CreateRefreshData() {
        List<EMConversation> list = EaseUserUtils.loadConversationList();
        if (null != list) {
            int create_count = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getType().toString().equals("GroupChat")) {
                    for (int j = 0; j < list_create.size(); j++) {
                        if (list.get(i).getUserName().equals(list_create.get(j).getGroup().getGroupId())) {
                            list_create.get(j).getGroup().setMsg(list.get(i).getLastMessage());
//							list_create.get(j).getGroup().setWho_hair(EaseUserUtils.returnUserNick(context,list.get(i).getLastMessage().getFrom()));
                            list_create.get(j).getGroup().setWho_hair(list.get(i).getLastMessage().getFrom());
                            if (list.get(i).getUnreadMsgCount() > 0) {
                                list_create.get(j).getGroup().setNo_read_count(String.valueOf(list.get(i).getUnreadMsgCount()));
                                create_count += list.get(i).getUnreadMsgCount();
                            } else {
                                list_create.get(j).getGroup().setNo_read_count("0");
                            }
                        }
                    }
                }

                c_number = create_count;

                if (j_number + c_number > 0) {
                    MainActivity.unread_topic_number.setVisibility(View.VISIBLE);
                    MainActivity.unread_topic_number.setText(String.valueOf(c_number + j_number));
                } else {
                    MainActivity.unread_topic_number.setText("0");
                    MainActivity.unread_topic_number.setVisibility(View.INVISIBLE);
                }

                create_adapter = new New_Old_Create_Adapter(context, list_create);
                mlv_create.setAdapter(create_adapter);
                create_adapter.notifyDataSetChanged();
            }
        }
    }

    private static void JoinRefreshData(final int _space) {
        List<EMConversation> list = EaseUserUtils.loadConversationList();
        if (null != list) {
            int join_count = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getType().toString().equals("GroupChat")) {
                    for (int j = 0; j < list_join.size(); j++) {
                        if (list.get(i).getUserName().equals(list_join.get(j).getGroup().getGroupId())) {
                            list_join.get(j).getGroup().setMsg(list.get(i).getLastMessage());
                            //list_join.get(j).getGroup().setWho_hair(EaseUserUtils.returnUserNick(context,list.get(i).getLastMessage().getFrom()));
                            list_join.get(j).getGroup().setWho_hair(list.get(i).getLastMessage().getFrom());
                            if (list.get(i).getUnreadMsgCount() > 0) {
                                list_join.get(j).getGroup().setNo_read_count(String.valueOf(list.get(i).getUnreadMsgCount()));
                                join_count += list.get(i).getUnreadMsgCount();
                            } else {
                                list_join.get(j).getGroup().setNo_read_count("0");
                            }
                        }
                    }
                }
            }

            j_number = join_count;

            if (j_number > 0) {
                iv_join.setVisibility(View.VISIBLE);
            } else {
                iv_join.setVisibility(View.GONE);
            }

            if (j_number + c_number > 0) {
                MainActivity.unread_topic_number.setVisibility(View.VISIBLE);
                MainActivity.unread_topic_number.setText(String.valueOf(c_number + j_number));
            } else {
                MainActivity.unread_topic_number.setText("0");
                MainActivity.unread_topic_number.setVisibility(View.INVISIBLE);
            }

            join_adapter = new New_Old_Join_Adapter(context, list_join, _space);
            mlv_join.setAdapter(join_adapter);
            join_adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {

        if (is_refrsh) {
            CreateRefreshData();
            JoinRefreshData(i_join);
        }

        super.onResume();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ll_manage:
                startActivity(new Intent(context,Topic_Management.class));

                break;

            case R.id.ll_j_more:

                if (null != list_join && list_join.size() > 0) {
                    ll_c_p.setVisibility(View.GONE);
                    ll_f_p.setVisibility(View.GONE);
                    rl_join.setVisibility(View.GONE);
                    iv_join.setVisibility(View.GONE);
                    ll_j_more.setVisibility(View.GONE);
                    ll_j_p.setVisibility(View.VISIBLE);
                    tv_j_pack_up.setVisibility(View.VISIBLE);

                    i_join = 1;

                    join_adapter = new New_Old_Join_Adapter(getContext(), list_join, i_join);
                    mlv_join.setAdapter(join_adapter);
                    join_adapter.notifyDataSetChanged();
                }

                break;

            case R.id.tv_j_pack_up:

                if (null != list_join && list_join.size() > 0) {

                    ll_c_p.setVisibility(View.VISIBLE);
                    ll_f_p.setVisibility(View.VISIBLE);

                    rl_join.setVisibility(View.VISIBLE);

                    ll_j_more.setVisibility(View.VISIBLE);
                    ll_j_p.setVisibility(View.VISIBLE);

                    tv_j_pack_up.setVisibility(View.GONE);

                    i_join = 0;

                    if (j_number > 0) {
                        iv_join.setVisibility(View.VISIBLE);
                    } else {
                        iv_join.setVisibility(View.GONE);
                    }

                    join_adapter = new New_Old_Join_Adapter(getContext(), list_join, i_join);
                    mlv_join.setAdapter(join_adapter);
                    join_adapter.notifyDataSetChanged();
                }

                break;

            case R.id.ll_f_more:

                if (null != list_frineds && list_frineds.size() > 0) {

                    ll_c_p.setVisibility(View.GONE);
                    ll_j_p.setVisibility(View.GONE);
                    ll_f_p.setVisibility(View.VISIBLE);

                    rl_friedns.setVisibility(View.GONE);
                    ll_f_more.setVisibility(View.GONE);

                    tv_f_pack_up.setVisibility(View.VISIBLE);

                    i_friends = 1;

                    friends_adapter = new New_Old_Friends_Adapter(getContext(), list_frineds, i_friends);
                    mlv_friends.setAdapter(friends_adapter);
                    friends_adapter.notifyDataSetChanged();
                }

                break;

            case R.id.tv_f_pack_up:

                if (null != list_frineds && list_frineds.size() > 0) {

                    ll_c_p.setVisibility(View.VISIBLE);
                    ll_j_p.setVisibility(View.VISIBLE);
                    ll_f_p.setVisibility(View.VISIBLE);

                    rl_friedns.setVisibility(View.VISIBLE);
                    ll_f_more.setVisibility(View.VISIBLE);

                    tv_f_pack_up.setVisibility(View.GONE);

                    i_friends = 0;

                    friends_adapter = new New_Old_Friends_Adapter(getContext(), list_frineds, i_friends);
                    mlv_friends.setAdapter(friends_adapter);
                    friends_adapter.notifyDataSetChanged();
                }

                break;

            case R.id.ll_space_add:

                if (null != list_create && list_create.size() < 3) {
                    startActivity(new Intent(context,
                            BuildGroupClassifyActivity.class));
                } else {
                    Toast.makeText(getActivity(), "目前只能建立三个话题", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.ll_new_topic:

                if (null != list_create && list_create.size() < 3) {
                    startActivity(new Intent(context,
                            BuildGroupClassifyActivity.class));
                } else {
                    Toast.makeText(getActivity(), "目前只能建立三个话题", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.ll_back:

                if(i_friends == 1) {
                    ll_c_p.setVisibility(View.VISIBLE);
                    ll_j_p.setVisibility(View.VISIBLE);
                    ll_f_p.setVisibility(View.VISIBLE);

                    rl_friedns.setVisibility(View.VISIBLE);
                    ll_f_more.setVisibility(View.VISIBLE);

                    tv_f_pack_up.setVisibility(View.GONE);

                    i_friends = 0;

                    friends_adapter = new New_Old_Friends_Adapter(getContext(), list_frineds, i_friends);
                    mlv_friends.setAdapter(friends_adapter);
                    friends_adapter.notifyDataSetChanged();
                } else if (i_join == 1) {
                    ll_c_p.setVisibility(View.VISIBLE);
                    ll_f_p.setVisibility(View.VISIBLE);

                    rl_join.setVisibility(View.VISIBLE);

                    ll_j_more.setVisibility(View.VISIBLE);
                    ll_j_p.setVisibility(View.VISIBLE);

                    tv_j_pack_up.setVisibility(View.GONE);

                    i_join = 0;

                    if (j_number > 0) {
                        iv_join.setVisibility(View.VISIBLE);
                    } else {
                        iv_join.setVisibility(View.GONE);
                    }

                    join_adapter = new New_Old_Join_Adapter(getContext(), list_join, i_join);
                    mlv_join.setAdapter(join_adapter);
                    join_adapter.notifyDataSetChanged();
                } else {
                    MainActivity.dismiss();
                }

                break;
        }
    }
}
