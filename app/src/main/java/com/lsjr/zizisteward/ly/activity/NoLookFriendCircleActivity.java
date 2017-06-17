package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 不看他的朋友圈
 */
public class NoLookFriendCircleActivity extends Activity implements OnClickListener {
    private GVAdapter adapter;
    private GVAdapter_New adapter_New;
    private GridView gv_people;
    private LinearLayout ll_back;
    private LinearLayout ll_sure;
    public static List<MyFocusidsBean.MyFocusids> myfocusids = new ArrayList<>();
    public static List<FocusMyidsBean.FocusMyids> focusMyids = new ArrayList<>();
    private String activity;
    private String title;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.no_look_friend_circle_activity);
        this.findViewById();
        this.activity = getIntent().getStringExtra("activity");
        this.title = getIntent().getStringExtra("title");
        tv_title.setText(title);

        if (activity.equals("no_circle")) {
            getData();
        } else if (activity.equals("no_friend_circle")) {
            getData_New();
        }

        gv_people.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (activity.equals("no_circle")) {
                    if (myfocusids.get(position).getSpace() == 1) {
                        startActivityForResult(new Intent(NoLookFriendCircleActivity.this, FriendsCircleListActivity.class)
                                .putExtra("activity", activity), 1);
                    } else if (myfocusids.get(position).getSpace() == 2) {
                        for (int i = 0; i < myfocusids.size(); i++) {
                            myfocusids.get(i).setGet_rid(1);
                        }
                        myfocusids.remove(myfocusids.size() - 1);
                        myfocusids.remove(myfocusids.size() - 1);
                        adapter.notifyDataSetChanged();
                    }
                } else if (activity.equals("no_friend_circle")) {
                    if (focusMyids.get(position).getSpace() == 1) {
                        startActivityForResult(new Intent(NoLookFriendCircleActivity.this, FriendsCircleListActivity.class)
                                .putExtra("activity", activity), 1);
                    } else if (focusMyids.get(position).getSpace() == 2) {
                        for (int i = 0; i < focusMyids.size(); i++) {
                            focusMyids.get(i).setGet_rid(1);
                        }
                        focusMyids.remove(focusMyids.size() - 1);
                        focusMyids.remove(focusMyids.size() - 1);
                        adapter_New.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void getData() {
        //不看他的朋友圈列表
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "257");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(NoLookFriendCircleActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                MyFocusidsBean mfb = new Gson().fromJson(result, MyFocusidsBean.class);
                myfocusids = mfb.getMyfocusids();

                if (null == myfocusids) {
                    myfocusids = new ArrayList<>();
                }

                for (int i = 0; i < myfocusids.size(); i++) {
                    myfocusids.get(i).setSpace(3);
                }

                MyFocusidsBean.MyFocusids f1 = new MyFocusidsBean.MyFocusids();
                f1.setSpace(1);
                MyFocusidsBean.MyFocusids f2 = new MyFocusidsBean.MyFocusids();
                f2.setSpace(2);
                myfocusids.add(f1);
                myfocusids.add(f2);
                adapter = new GVAdapter(NoLookFriendCircleActivity.this, myfocusids);
                gv_people.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });
    }

    private void getData_New() {
        //不让他看我的朋友圈列表
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "259");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(NoLookFriendCircleActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("不让他看我的朋友圈列表:    " + result);
                FocusMyidsBean fmb = new Gson().fromJson(result, FocusMyidsBean.class);

                focusMyids = fmb.getFocusMyids();

                if (null == focusMyids) {
                    focusMyids = new ArrayList<>();
                }

                for (int i = 0; i < focusMyids.size(); i++) {
                    focusMyids.get(i).setSpace(3);
                }

                FocusMyidsBean.FocusMyids f1 = new FocusMyidsBean.FocusMyids();
                f1.setSpace(1);
                FocusMyidsBean.FocusMyids f2 = new FocusMyidsBean.FocusMyids();
                f2.setSpace(2);
                focusMyids.add(f1);
                focusMyids.add(f2);
                adapter_New = new GVAdapter_New(NoLookFriendCircleActivity.this, focusMyids);
                gv_people.setAdapter(adapter_New);
                adapter_New.notifyDataSetChanged();
            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });
    }

    private void findViewById() {
        this.tv_title = (TextView) super.findViewById(R.id.tv_title);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.gv_people = (GridView) super.findViewById(R.id.gv_people);
        this.ll_sure = (LinearLayout) super.findViewById(R.id.ll_sure);

        this.tv_title.setText(title);

        this.ll_back.setOnClickListener(this);
        this.ll_sure.setOnClickListener(this);
    }

    private class GVAdapter extends BaseAdapter {
        private Context context;
        private ViewHolder view;
        private List<MyFocusidsBean.MyFocusids> myfocusids;

        public GVAdapter(Context context, List<MyFocusidsBean.MyFocusids> myfocusids) {
            this.context = context;
            this.myfocusids = myfocusids;
        }

        @Override
        public int getCount() {
            return null == myfocusids ? 0 : myfocusids.size();
        }

        @Override
        public Object getItem(int position) {
            return myfocusids.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(R.layout.no_look_friend_circle_item, null);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {
                view = (ViewHolder) convertView.getTag();
            }

            if (myfocusids.get(position).getSpace() == 1) {
                view.riv_head.setImageResource(R.drawable.icon_ly_add);
            } else if (myfocusids.get(position).getSpace() == 2) {
                view.riv_head.setImageResource(R.drawable.icon_ly_delete);
            } else if (myfocusids.get(position).getSpace() == 3) {
                Picasso.with(context).load(HttpConfig.IMAGEHOST + myfocusids.get(position).getPhoto()).into(view.riv_head);
            }

            if (myfocusids.get(position).getGet_rid() == 1) {
                view.iv_get_rid.setVisibility(View.VISIBLE);
            } else {
                view.iv_get_rid.setVisibility(View.GONE);
            }

            view.iv_get_rid.setTag(position);
            view.iv_get_rid.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    myfocusids.remove((int) v.getTag());
                    adapter.notifyDataSetChanged();
                }
            });

            return convertView;
        }

        private class ViewHolder {
            private RoundImageView riv_head;
            private ImageView iv_get_rid;

            public ViewHolder(View v) {
                this.riv_head = (RoundImageView) v.findViewById(R.id.riv_head);
                this.iv_get_rid = (ImageView) v.findViewById(R.id.iv_get_rid);
            }
        }
    }

    private class GVAdapter_New extends BaseAdapter {
        private Context context;
        private ViewHolder view;
        private List<FocusMyidsBean.FocusMyids> focusMyids;

        public GVAdapter_New(Context context, List<FocusMyidsBean.FocusMyids> focusMyids) {
            this.context = context;
            this.focusMyids = focusMyids;
        }

        @Override
        public int getCount() {
            return null == focusMyids ? 0 : focusMyids.size();
        }

        @Override
        public Object getItem(int position) {
            return focusMyids.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(R.layout.no_look_friend_circle_item, null);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {
                view = (ViewHolder) convertView.getTag();
            }

            if (focusMyids.get(position).getSpace() == 1) {
                view.riv_head.setImageResource(R.drawable.icon_ly_add);
            } else if (focusMyids.get(position).getSpace() == 2) {
                view.riv_head.setImageResource(R.drawable.icon_ly_delete);
            } else if (focusMyids.get(position).getSpace() == 3) {
                Picasso.with(context).load(HttpConfig.IMAGEHOST + focusMyids.get(position).getPhoto()).into(view.riv_head);
            }

            if (focusMyids.get(position).getGet_rid() == 1) {
                view.iv_get_rid.setVisibility(View.VISIBLE);
            } else {
                view.iv_get_rid.setVisibility(View.GONE);
            }

            view.iv_get_rid.setTag(position);
            view.iv_get_rid.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    focusMyids.remove((int) v.getTag());
                    adapter_New.notifyDataSetChanged();
                }
            });

            return convertView;
        }

        private class ViewHolder {
            private RoundImageView riv_head;
            private ImageView iv_get_rid;

            public ViewHolder(View v) {
                this.riv_head = (RoundImageView) v.findViewById(R.id.riv_head);
                this.iv_get_rid = (ImageView) v.findViewById(R.id.iv_get_rid);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {
            case 1:

                if (activity.equals("no_circle")) {
                    MyFocusidsBean.MyFocusids f1 = new MyFocusidsBean.MyFocusids();
                    f1.setSpace(1);
                    MyFocusidsBean.MyFocusids f2 = new MyFocusidsBean.MyFocusids();
                    f2.setSpace(2);
                    myfocusids.add(f1);
                    myfocusids.add(f2);
                    adapter = new GVAdapter(NoLookFriendCircleActivity.this, myfocusids);
                    gv_people.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else if (activity.equals("no_friend_circle")) {
                    FocusMyidsBean.FocusMyids f1 = new FocusMyidsBean.FocusMyids();
                    f1.setSpace(1);
                    FocusMyidsBean.FocusMyids f2 = new FocusMyidsBean.FocusMyids();
                    f2.setSpace(2);
                    focusMyids.add(f1);
                    focusMyids.add(f2);
                    adapter_New = new GVAdapter_New(NoLookFriendCircleActivity.this, focusMyids);
                    gv_people.setAdapter(adapter_New);
                    adapter_New.notifyDataSetChanged();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setData(String space) {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "256");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("friend_ids", space);
        new HttpClientGet(NoLookFriendCircleActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                Toast.makeText(NoLookFriendCircleActivity.this, "更新名单成功...", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });
    }

    private void setData_New(String space) {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "258");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("friend_ids", space);
        new HttpClientGet(NoLookFriendCircleActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                Toast.makeText(NoLookFriendCircleActivity.this, "更新名单成功...", Toast.LENGTH_SHORT).show();
                finish();
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
                this.finish();
                break;

            case R.id.ll_sure:

                String space = "";

                if (activity.equals("no_circle")) {

                    for (int i = 0; i < myfocusids.size(); i++) {

                        if (myfocusids.get(i).getSpace() == 3) {
                            if (space.equals("")) {
                                space = myfocusids.get(i).getUserid();
                            } else {
                                space += "," + myfocusids.get(i).getUserid();
                            }
                        }
                    }

                    System.out.println("space   " + space);

                    setData(space);
                } else if (activity.equals("no_friend_circle")) {
                    for (int i = 0; i < focusMyids.size(); i++) {

                        if (focusMyids.get(i).getSpace() == 3) {
                            if (space.equals("")) {
                                space = focusMyids.get(i).getUserid();
                            } else {
                                space += "," + focusMyids.get(i).getUserid();
                            }
                        }
                    }
                    System.out.println("space   " + space);
                    setData_New(space);
                }

                break;
        }
    }
}
