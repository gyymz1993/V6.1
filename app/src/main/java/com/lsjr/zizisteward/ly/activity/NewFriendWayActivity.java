package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.PullToRefreshLayout;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewFriendWayActivity extends Activity implements OnClickListener {
    private com.lsjr.zizisteward.ly.activity.MyListView mlv;
    private ImageView iv_clear;
    private TextView et_search;
    private LinearLayout ll_add;
    private LinearLayout ll_back;
    private LinearLayout ll;
    private LinearLayout ll_prompt;
    private PopupWindow popupWindow;
    private List<AddFriendRecordBean.AddFriendRecord> addFriendRecord;
    private PullToRefreshLayout ptrl;
    private int page = 1;
    private Adapter adapter;
    protected InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.new_friend_way_activity);
        this.findViewById();
    }

    private void findViewById() {
        this.mlv = (MyListView) super.findViewById(R.id.mlv);
        this.ll_add = (LinearLayout) super.findViewById(R.id.ll_add);
        this.iv_clear = (ImageView) super.findViewById(R.id.iv_clear);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.ll = (LinearLayout) super.findViewById(R.id.ll);
        this.et_search = (TextView) super.findViewById(R.id.et_search);
        this.ll_prompt = (LinearLayout) super.findViewById(R.id.ll_prompt);
        this.ptrl = (PullToRefreshLayout) super.findViewById(R.id.refresh_view);
        this.imm = (InputMethodManager) NewFriendWayActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);

        this.ll_add.setOnClickListener(this);
        this.ll_back.setOnClickListener(this);
        this.iv_clear.setOnClickListener(this);

        this.et_search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewFriendWayActivity.this,
                        SearchAddFriendActivity.class));
            }
        });

//		this.et_search.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				
//				List<AddFriendRecord> list_space = new ArrayList<>();
//				
//				for (int i = 0; i < addFriendRecord.size(); i++) {
//					
//					if (addFriendRecord.get(i).getUser_name().contains(s) 
//							|| addFriendRecord.get(i).getUser_name().toUpperCase().contains(s) 
//							|| addFriendRecord.get(i).getUser_name().toLowerCase().contains(s)) {
//						list_space.add(addFriendRecord.get(i));
//					}
//				}
//				
//				if (list_space.size() < 1) {
//					ptrl.setVisibility(View.GONE);
//					ll_prompt.setVisibility(View.VISIBLE);
//				} else {
//					ptrl.setVisibility(View.VISIBLE);
//					ll_prompt.setVisibility(View.GONE);
//				}
//
//				adapter = new Adapter(NewFriendWayActivity.this, list_space);
//				
//				mlv.setAdapter(adapter);
//				
//				adapter.notifyDataSetChanged();
//				
//				if (s.length() > 0) {
//					iv_clear.setVisibility(View.VISIBLE);
//				} else {
//					iv_clear.setVisibility(View.GONE);
//				}
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//			}
//		});

        this.ptrl.setOnRefreshListener(new MyListener());

        getData(1, 0);
    }

    private class MyListener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            getData(1, 1);
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            getData(page, 1);
        }
    }

    private void getData(int currPage, int type) {
        final int tp = type;
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "249");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("currPage", String.valueOf(currPage));
        new HttpClientGet(NewFriendWayActivity.this, null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        System.out.println(result);
                        AddFriendRecordBean afrb = new Gson().fromJson(result,
                                AddFriendRecordBean.class);

                        switch (tp) {
                            case 0:

                                addFriendRecord = afrb.getAddFriendRecord();
                                if (null != addFriendRecord) {
                                    adapter = new Adapter(NewFriendWayActivity.this, addFriendRecord);
                                    mlv.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }

                                break;

                            case 1:

                                addFriendRecord = afrb.getAddFriendRecord();
                                if (null != addFriendRecord) {
                                    adapter = new Adapter(NewFriendWayActivity.this, addFriendRecord);
                                    mlv.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }

                                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);

                                break;

                            case 2:
                                List<AddFriendRecordBean.AddFriendRecord> ard;
                                ard = afrb.getAddFriendRecord();

                                if (null != ard) {
                                    addFriendRecord.addAll(ard);
                                    adapter.notifyDataSetChanged();
                                }

                                ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                break;
                        }

                        if (null != addFriendRecord && addFriendRecord.size() > 4) {
                            ll.setVisibility(View.VISIBLE);
                        } else {
                            ll.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFailure(MyError myError) {
                        super.onFailure(myError);
                    }
                });
    }

    private class Adapter extends BaseAdapter {
        private Context context;
        private ViewHolder view;
        private List<AddFriendRecordBean.AddFriendRecord> addFriendRecord;

        public Adapter(Context context, List<AddFriendRecordBean.AddFriendRecord> addFriendRecord) {
            this.context = context;
            this.addFriendRecord = addFriendRecord;
        }

        @Override
        public int getCount() {
            return null == addFriendRecord ? 0 : addFriendRecord.size();
        }

        @Override
        public Object getItem(int position) {
            return addFriendRecord.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.new_friend_way_activity_item, null);
                view = new ViewHolder(convertView);
                convertView.setTag(view);
            } else {
                view = (ViewHolder) convertView.getTag();
            }

            switch (addFriendRecord.get(position).getRecord_state()) {
                case 0:

                    view.tv_sure.setVisibility(View.GONE);
                    view.tv_false.setVisibility(View.GONE);
                    view.tv.setText("等待添加");
                    view.tv.setVisibility(View.VISIBLE);

                    break;

                case 1:

                    view.tv_sure.setVisibility(View.GONE);
                    view.tv_false.setVisibility(View.GONE);
                    view.tv.setText("已同意");
                    view.tv.setVisibility(View.VISIBLE);

                    break;

                case 2:

                    view.tv_sure.setVisibility(View.GONE);
                    view.tv_false.setVisibility(View.GONE);
                    view.tv.setText("已拒绝");
                    view.tv.setVisibility(View.VISIBLE);

                    break;

                case 3:

                    view.tv_sure.setVisibility(View.VISIBLE);
                    view.tv_false.setVisibility(View.VISIBLE);
                    view.tv.setText("");
                    view.tv.setVisibility(View.GONE);

                    break;

                case 4:

                    view.tv_sure.setVisibility(View.GONE);
                    view.tv_false.setVisibility(View.GONE);
                    view.tv.setText("通过验证");
                    view.tv.setVisibility(View.VISIBLE);

                    break;

                case 5:

                    view.tv_sure.setVisibility(View.GONE);
                    view.tv_false.setVisibility(View.GONE);
                    view.tv.setText("未通过验证");
                    view.tv.setVisibility(View.VISIBLE);

                    break;

                case 6:

                    view.tv_sure.setVisibility(View.GONE);
                    view.tv_false.setVisibility(View.GONE);
                    view.tv.setText("已失效");
                    view.tv.setVisibility(View.VISIBLE);

                    break;
            }

            Picasso.with(context).load(HttpConfig.IMAGEHOST + addFriendRecord.get(position).getPhoto()).into(view.riv_head);

            view.tv_name.setText(addFriendRecord.get(position).getUser_name());

            view.tv_sure.setTag(position);
            view.tv_sure.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    CustomDialogUtils.startCustomProgressDialog(NewFriendWayActivity.this, "请稍候...");
                    int pos = (int) v.getTag();
                    Map<String, String> map = new HashMap<>();
                    map.put("OPT", "254");
                    map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                    map.put("by_user_id", addFriendRecord.get(pos).getBy_user_id());
                    map.put("type", "1");
                    new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            CustomDialogUtils.stopCustomProgressDialog(NewFriendWayActivity.this);
                            getData(1, 0);
                        }

                        @Override
                        public void onFailure(MyError myError) {
                            CustomDialogUtils.stopCustomProgressDialog(NewFriendWayActivity.this);
                            super.onFailure(myError);
                        }
                    });
                }
            });

            view.tv_false.setTag(position);
            view.tv_false.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    CustomDialogUtils.startCustomProgressDialog(NewFriendWayActivity.this, "请稍候...");
                    Map<String, String> map = new HashMap<>();
                    map.put("OPT", "254");
                    map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                    map.put("by_user_id", addFriendRecord.get(pos).getBy_user_id());
                    map.put("type", "0");
                    new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            CustomDialogUtils.stopCustomProgressDialog(NewFriendWayActivity.this);
                            getData(1, 0);
                        }

                        @Override
                        public void onFailure(MyError myError) {
                            CustomDialogUtils.stopCustomProgressDialog(NewFriendWayActivity.this);
                            super.onFailure(myError);
                        }
                    });

                }
            });

            return convertView;
        }

        private class ViewHolder {
            private TextView tv;
            private TextView tv_name;
            private TextView tv_sure;
            private TextView tv_false;
            private RoundImageView riv_head;

            public ViewHolder(View v) {
                this.tv = (TextView) v.findViewById(R.id.tv);
                this.tv_sure = (TextView) v.findViewById(R.id.tv_sure);
                this.tv_name = (TextView) v.findViewById(R.id.tv_name);
                this.tv_false = (TextView) v.findViewById(R.id.tv_false);
                this.riv_head = (RoundImageView) v.findViewById(R.id.riv_head);
            }
        }
    }

    private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(NewFriendWayActivity.this)
                .inflate(R.layout.popupwindow, null);
        // 设置按钮的点击事件
        TextView tv_one = (TextView) contentView.findViewById(R.id.tv_one);

        TextView tv_two = (TextView) contentView.findViewById(R.id.tv_two);

        TextView tv_three = (TextView) contentView.findViewById(R.id.tv_three);

        tv_one.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                startActivityForResult(new Intent(NewFriendWayActivity.this,
                        Activity_AddFriends.class), 1);
            }
        });

        tv_two.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                startActivity(new Intent(NewFriendWayActivity.this,
                        SearchAddFriendActivity.class));
            }
        });

        tv_three.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                startActivity(new Intent(NewFriendWayActivity.this,
                        ReadAddressBook.class));
            }
        });

        popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug

        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.icon_back_space));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);
    }

    @Override
    protected void onDestroy() {
        imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
        Fragment_AddressBook.requestAddressBook();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                if (null != popupWindow) {
                    popupWindow.dismiss();
                }
                imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
                this.finish();
                break;

            case R.id.ll_add:
                showPopupWindow(ll_add);
                break;

            case R.id.iv_clear:

                ll_prompt.setVisibility(View.GONE);
                ptrl.setVisibility(View.VISIBLE);

                et_search.setText("");

                adapter = new Adapter(NewFriendWayActivity.this, addFriendRecord);

                mlv.setAdapter(adapter);

                adapter.notifyDataSetChanged();

                imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);

                break;
        }
    }
}
