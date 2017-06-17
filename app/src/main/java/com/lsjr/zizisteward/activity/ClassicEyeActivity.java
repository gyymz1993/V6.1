package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.bean.BasicUserInfo;
import com.lsjr.zizisteward.bean.ShiJieBean;
import com.lsjr.zizisteward.bean.ShiJieBean.ShiJieListDetail;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.lsjr.zizisteward.utils.ToastUtils;
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class ClassicEyeActivity extends BaseActivity {

    private String mClassic;
    private SuperListView mListview_shijie;
    private boolean isRefresh = true, state_base;
    private int pageNum = 1;
    private TextView mTv_no_content;
    private List<ShiJieListDetail> list = new ArrayList<ShiJieListDetail>();
    private ClassicEyeAdapter mAdapter;
    private String user_level;
    private int eval_item = 0;

    @Override
    public int getContainerView() {
        return R.layout.activity_classic_eye;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClassic = getIntent().getStringExtra("classic");
        setmTitle(mClassic);
        mListview_shijie = (SuperListView) findViewById(R.id.listview_shijie);
        mTv_no_content = (TextView) findViewById(R.id.tv_no_content);
        init();
    }

    private void init() {
        mAdapter = new ClassicEyeAdapter(ClassicEyeActivity.this, list);
        mListview_shijie.setAdapter(mAdapter);
        mListview_shijie.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                isRefresh = true;
                getData();
            }
        });
        mListview_shijie.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                isRefresh = false;
                getData();

            }
        });
        mListview_shijie.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean state3 = PreferencesUtils.getBoolean(ClassicEyeActivity.this, "isLogin");
                if (state3 == true) {
                    Intent intent = new Intent(ClassicEyeActivity.this, ShiJieDetailActivity.class);
                    intent.putExtra("id", list.get(position - 1).getId());
                    intent.putExtra("content", list.get(position - 1).getContent());// 内容
                    intent.putExtra("photo", list.get(position - 1).getPhoto());// 头像
                    intent.putExtra("shareImg", list.get(position - 1).getShareImg());// 图片
                    intent.putExtra("time", list.get(position - 1).getShare_time().getTime());// 时间
                    intent.putExtra("user_name", list.get(position - 1).getUser_name());// 用户名
                    intent.putExtra("sight_type", list.get(position - 1).getSight_type());// 内容类型
                    intent.putExtra("custom_tag", list.get(position - 1).getCustom_tag());// 标签内容
                    intent.putExtra("zan_count", list.get(position - 1).getShare_like());// 点赞次数
                    intent.putExtra("zan_state", list.get(position - 1).isZan());// 点赞状态
                    intent.putExtra("collect_state", list.get(position - 1).isCollect());// 收藏状态
                    intent.putExtra("image_size", list.get(position - 1).getImg_wh());// 图片尺寸
                    intent.putExtra("level", list.get(position - 1).getCredit_level_id());// 用户等级
                    intent.putExtra("user_id", list.get(position - 1).getUser_id());// 用户id
                    startActivity(intent);
                } else if (state3 == false) {
                    final Dialog dialog = new Dialog(ClassicEyeActivity.this, R.style.dialog);
                    dialog.setContentView(R.layout.popup_delete_address);
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER | Gravity.CENTER);
                    TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
                    TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
                    TextView tv_confirm = (TextView) dialog.findViewById(R.id.tv_confirm);
                    tv_msg.setText("请先登录,好吗?");
                    tv_confirm.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(ClassicEyeActivity.this, NoteLoginActivity.class);
                            intent.putExtra("personal", "shijie_detail");
                            startActivity(intent);
                        }
                    });

                    tv_cancel.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
            }
        });
        mListview_shijie.refresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 418:
                if (resultCode == 418) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            Intent intent = new Intent(ClassicEyeActivity.this, ShiJieDetailActivity.class);
                            intent.putExtra("id", list.get(eval_item).getId());
                            intent.putExtra("content", list.get(eval_item).getContent());// 内容
                            intent.putExtra("photo", list.get(eval_item).getPhoto());// 头像
                            intent.putExtra("shareImg", list.get(eval_item).getShareImg());// 图片
                            intent.putExtra("time", list.get(eval_item).getShare_time().getTime());// 时间
                            intent.putExtra("user_name", list.get(eval_item).getUser_name());// 用户名
                            intent.putExtra("sight_type", list.get(eval_item).getSight_type());// 内容类型
                            intent.putExtra("custom_tag", list.get(eval_item).getCustom_tag());// 标签内容
                            intent.putExtra("zan_count", list.get(eval_item).getShare_like());// 点赞次数
                            intent.putExtra("zan_state", list.get(eval_item).isZan());// 点赞状态
                            intent.putExtra("collect_state", list.get(eval_item).isCollect());// 收藏状态
                            intent.putExtra("image_size", list.get(eval_item).getImg_wh());// 图片尺寸
                            intent.putExtra("level", list.get(eval_item).getCredit_level_id());// 用户等级
                            intent.putExtra("user_id", list.get(eval_item).getUser_id());// 用户id
                            startActivity(intent);
                        }
                    }, 120);
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        getData();
        state_base = PreferencesUtils.getBoolean(getApplicationContext(), "isLogin");
        getUserData();
        super.onResume();
    }

    private void getUserData() {
        if (state_base == true) {
            HashMap<String, String> map = new HashMap<>();
            map.put("OPT", "199");
            map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
            new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                @Override
                public void onSuccess(String result) {
                    System.out.println("用户消息" + result);
                    BasicUserInfo bean = GsonUtil.getInstance().fromJson(result, BasicUserInfo.class);
                    user_level = bean.getCredit();
                }
            });
        }
    }

    private void setChange() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIs_give().equals("0")) {
                list.get(i).setZan(false);
            } else {
                list.get(i).setZan(true);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIs_collect().equals("0")) {
                list.get(i).setCollect(false);
            } else {
                list.get(i).setCollect(true);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIs_attention().equals("0")) {
                list.get(i).setCare(false);
            } else {
                list.get(i).setCare(true);
            }
        }
    }

    private void getData() {
        if (isRefresh) {
            pageNum = 1;
            mAdapter.removeAll();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "245");
        // 判断是否登录状态
        boolean state = PreferencesUtils.getBoolean(ClassicEyeActivity.this, "isLogin");
        if (state == true) {
            map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        } else {
            map.put("user_id", EncryptUtils.addSign(Integer.parseInt("-1"), "u"));
        }

        map.put("currPage", String.valueOf(pageNum++));
        map.put("identity_type", mClassic);

        new HttpClientGet(ClassicEyeActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("视界列表" + result);
                ShiJieBean bean = GsonUtil.getInstance().fromJson(result, ShiJieBean.class);
                if (bean.getSight().getTotalCount() == 0) {
                    mTv_no_content.setVisibility(View.VISIBLE);
                    mListview_shijie.setVisibility(View.GONE);
                } else {
                    mListview_shijie.setVisibility(View.VISIBLE);
                    mTv_no_content.setVisibility(View.GONE);
                    if (1 != pageNum) {
                        list.addAll(bean.getSight().getPage());
                        mAdapter.setList(list);
                    } else {
                        list = bean.getSight().getPage();
                        mAdapter = new ClassicEyeAdapter(ClassicEyeActivity.this, list);
                        mListview_shijie.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }

                    if (list.size() < bean.getSight().getPageSize()) {
                        mListview_shijie.setIsLoadFull(false);
                    }

                    mListview_shijie.finishRefresh();
                    mListview_shijie.finishLoadMore();
                    setChange();
                }

            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });
    }

    public class ClassicEyeAdapter extends BaseAdapter {
        private Context context;
        private ViewHolder mHolder;
        private List<ShiJieListDetail> list;

        public ClassicEyeAdapter(Context context, List<ShiJieListDetail> list) {
            this.context = context;
            this.list = list;
        }

        public void setList(List<ShiJieListDetail> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void add(ShiJieListDetail page) {
            this.list.add(page);
            notifyDataSetChanged();
        }

        public void addFirst(ShiJieListDetail page) {
            this.list.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<ShiJieListDetail> list) {
            this.list.addAll(list);
            notifyDataSetChanged();
        }

        public void remove(int position) {
            this.list.remove(position);
            notifyDataSetChanged();
        }

        public void removeAll() {
            this.list.clear();
            notifyDataSetChanged();
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_shijie_list, null);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            String img_wh = list.get(position).getImg_wh();
            String[] spic_first = img_wh.split(",");
            int width = Integer.valueOf(spic_first[0]);
            int height = Integer.valueOf(spic_first[1]);
            System.out.println("宽度" + width + "高度" + height);

            DisplayMetrics dm = getResources().getDisplayMetrics();
            int widthPixels = dm.widthPixels;
            int heightPixels = dm.heightPixels;

            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) mHolder.mRe_iv.getLayoutParams();
            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) mHolder.mIv_photo.getLayoutParams();
            params2.width = widthPixels * 2 / 3;
            params2.height = widthPixels;
            params1.height = widthPixels;
            params1.width = widthPixels;

            mHolder.mIv_photo.setLayoutParams(params2);
            mHolder.mRe_iv.setLayoutParams(params1);

            Glide.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getSpicfirst()).asBitmap().centerCrop()
                    .animate(android.R.anim.slide_in_left).into(mHolder.mIv_photo);
            Glide.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getPhoto())
                    .into(mHolder.mYouliao_yuantu);
            mHolder.mTv_name.setText(list.get(position).getUser_name());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            String share_time = formatter.format(Long.valueOf(list.get(position).getShare_time().getTime()));
            mHolder.mTv_time.setText(share_time);
            mHolder.mTv_content.setText(list.get(position).getContent());
            mHolder.mTv_count.setText(list.get(position).getPhoto_number() + "张");

            if (list.get(position).getCredit_level_id().equals("0")) {
                mHolder.mIv_level.setImageResource(R.drawable.level_zero);
            } else if (list.get(position).getCredit_level_id().equals("1")) {
                mHolder.mIv_level.setImageResource(R.drawable.level_one);
            } else if (list.get(position).getCredit_level_id().equals("2")) {
                mHolder.mIv_level.setImageResource(R.drawable.level_two);
            } else if (list.get(position).getCredit_level_id().equals("3")) {
                mHolder.mIv_level.setImageResource(R.drawable.level_three);
            } else if (list.get(position).getCredit_level_id().equals("6")) {
                mHolder.mIv_level.setImageResource(R.drawable.level_six);
            } else if (list.get(position).getCredit_level_id().equals("5")) {
                mHolder.mIv_level.setImageResource(R.drawable.level_five);
            } else if (list.get(position).getCredit_level_id().equals("4")) {
                mHolder.mIv_level.setImageResource(R.drawable.level_three);
            }

            if (TextUtils.isEmpty(list.get(position).getCustom_tag())) {
                mHolder.mTv_custom.setVisibility(View.GONE);
            } else {
                mHolder.mTv_custom.setVisibility(View.VISIBLE);
                mHolder.mTv_custom.setText(list.get(position).getCustom_tag());
            }

            mHolder.mPraise_count.setText(list.get(position).getShare_like() + " 赞");

            if (list.get(position).isCollect()) {// 已收藏
                mHolder.mIv_collect_gray.setVisibility(View.GONE);
                mHolder.mIv_collect_red.setVisibility(View.VISIBLE);
            } else {// 未收藏
                mHolder.mIv_collect_gray.setVisibility(View.VISIBLE);
                mHolder.mIv_collect_red.setVisibility(View.GONE);
            }

            if (list.get(position).isZan()) {// 已赞
                mHolder.mIv_zan_gray.setVisibility(View.GONE);
                mHolder.mIv_zan_red.setVisibility(View.VISIBLE);

            } else {// 未赞
                mHolder.mIv_zan_gray.setVisibility(View.VISIBLE);
                mHolder.mIv_zan_red.setVisibility(View.GONE);
            }

            if (state_base == true) {
                if (list.get(position).isCare()) {// 已关注
                    mHolder.re_care.setVisibility(View.GONE);
                    mHolder.re_care_cancel.setVisibility(View.VISIBLE);
                } else {// 未关注
                    mHolder.re_care.setVisibility(View.VISIBLE);
                    mHolder.re_care_cancel.setVisibility(View.GONE);
                }
            } else {
                mHolder.re_care.setVisibility(View.GONE);
                mHolder.re_care_cancel.setVisibility(View.GONE);
            }

            mHolder.re_care.setTag(position);
            mHolder.re_care.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    final int care_position = (int) v.getTag();
                    // 去关注
                    if (App.getUserInfo().getId().equals(list.get(care_position).getUser_id())) {
                        ToastUtils.show(getApplicationContext(), "不能关注自己");
                        return;
                    }
                    // 去关注
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("OPT", "30");
                    map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                    map.put("to_user_id", list.get(care_position).getUser_id());
                    new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
                            list.get(care_position).setCare(true);
                            mAdapter.notifyDataSetChanged();
                            ToastUtils.show(context, bean.getMsg());
                        }
                    });
                }
            });

            mHolder.re_care_cancel.setTag(position);
            mHolder.re_care_cancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    final int cancel_care_posiiton = (int) v.getTag();
                    // 取消关注
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("OPT", "31");
                    map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                    map.put("to_user_id", list.get(cancel_care_posiiton).getUser_id());
                    new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
                            list.get(cancel_care_posiiton).setCare(false);
                            mAdapter.notifyDataSetChanged();
                            ToastUtils.show(context, bean.getMsg());
                        }
                    });

                }
            });

            mHolder.mIv_collect_gray.setTag(position);
            mHolder.mIv_collect_gray.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (state_base == false) {
                        ToastUtils.show(getApplicationContext(), "您还未登录,请先登录");
                    } else {
                        final int tag = (int) v.getTag();
                        // 去收藏
                        HashMap<String, String> map = new HashMap<>();
                        map.put("OPT", "68");
                        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                        map.put("share_id", list.get(tag).getId());
                        new HttpClientGet(getApplicationContext(), null, map, false,
                                new HttpClientGet.CallBacks<String>() {

                                    @Override
                                    public void onSuccess(String result) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(result);
                                            String msg = jsonObject.getString("msg");
                                            list.get(tag).setCollect(true);
                                            mAdapter.notifyDataSetChanged();
                                            // ToastUtils.show(getApplicationContext(),
                                            // msg);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(MyError myError) {
                                        super.onFailure(myError);
                                    }
                                });
                    }
                }
            });

            mHolder.mIv_collect_red.setTag(position);
            mHolder.mIv_collect_red.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (state_base == false) {
                        ToastUtils.show(getApplicationContext(), "您还未登录,请先登录");
                    } else {
                        final int item = (int) v.getTag();
                        // 取消收藏
                        HashMap<String, String> map = new HashMap<>();
                        map.put("OPT", "69");
                        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                        map.put("share_id", list.get(item).getId());
                        new HttpClientGet(getApplicationContext(), null, map, false,
                                new HttpClientGet.CallBacks<String>() {

                                    @Override
                                    public void onSuccess(String result) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(result);
                                            String msg = jsonObject.getString("msg");
                                            list.get(item).setCollect(false);
                                            mAdapter.notifyDataSetChanged();
                                            // ToastUtils.show(getApplicationContext(),
                                            // msg);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(MyError myError) {
                                        super.onFailure(myError);
                                    }
                                });
                    }
                }
            });

            mHolder.mIv_zan_gray.setTag(position);
            mHolder.mIv_zan_gray.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (state_base == false) {
                        ToastUtils.show(getApplicationContext(), "您还未登录,请先登录");
                    } else {
                        final int item = (int) v.getTag();
                        // 去点赞
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("OPT", "187");
                        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                        map.put("share_id", list.get(item).getId());
                        new HttpClientGet(getApplicationContext(), null, map, false,
                                new HttpClientGet.CallBacks<String>() {

                                    @Override
                                    public void onSuccess(String result) {
                                        System.out.println("点赞" + result);
                                        try {
                                            JSONObject object = new JSONObject(result);
                                            String msg = object.getString("msg");
                                            String support = object.getString("support");
                                            String share_like = list.get(item).getShare_like();
                                            list.get(item)
                                                    .setShare_like(String.valueOf(Integer.parseInt(share_like) + 1));
                                            list.get(item).setZan(true);
                                            mAdapter.notifyDataSetChanged();
                                            // ToastUtils.show(getApplicationContext(),
                                            // msg);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void onFailure(MyError myError) {
                                        super.onFailure(myError);
                                    }

                                });
                    }
                }
            });

            mHolder.mIv_zan_red.setTag(position);
            mHolder.mIv_zan_red.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (state_base == false) {
                        ToastUtils.show(getApplicationContext(), "您还未登录,请先登录");
                    } else {
                        // 取消赞
                        final int pos = (int) v.getTag();
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("OPT", "67");
                        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                        map.put("share_id", list.get(pos).getId());
                        new HttpClientGet(getApplicationContext(), null, map, false,
                                new HttpClientGet.CallBacks<String>() {

                                    @Override
                                    public void onSuccess(String result) {
                                        System.out.println("取消赞" + result);
                                        try {
                                            JSONObject object = new JSONObject(result);
                                            String msg = object.getString("msg");
                                            String support = object.getString("support");
                                            list.get(pos).setShare_like(String
                                                    .valueOf(Integer.parseInt(list.get(pos).getShare_like()) - 1));
                                            list.get(pos).setZan(false);
                                            mAdapter.notifyDataSetChanged();
                                            // ToastUtils.show(getApplicationContext(),
                                            // msg);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                });
                    }
                }
            });

            mHolder.iv_eval.setTag(position);
            mHolder.iv_eval.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int tag = (int) v.getTag();
                    if (state_base == false) {
                        ToastUtils.show(getApplicationContext(), "您还未登录,请先登录");
                    } else {
                        if ("0".equals(user_level)) {
                            ToastUtils.show(getApplicationContext(), "您的用户等级还不够,暂不能评论");
                        } else {
                            eval_item = tag;
                            Intent mIntent = new Intent(getApplicationContext(), SendEvalActivity.class);
                            mIntent.putExtra("id", list.get(tag).getId());
                            startActivityForResult(mIntent, 418);
                        }
                    }
                }
            });
            return convertView;
        }

    }

    public class ViewHolder {
        private RoundImageView mYouliao_yuantu;// 用户头像
        private TextView mTv_key;// 身份类型
        private TextView mTv_name;// 用户名
        private TextView mTv_time;// 时间
        private ImageView mIv_level;// 会员等级图标
        private TextView mTv_content;// 内容
        private TextView mTv_count;// 图片数量
        private RoundImageView mIv_photo;// 图片
        private TextView mPraise_count;// 点赞次数
        private TextView mTv_custom;// 自定义标签内容
        private RelativeLayout mRe_zan;// 点击赞
        private ImageView mIv_zan_red;// 红色赞
        private ImageView mIv_zan_gray;// 灰色赞
        private RelativeLayout mRe_collect;// 点击收藏
        private ImageView mIv_collect_gray;// 灰色收藏
        private ImageView mIv_collect_red;// 红色收藏
        private RelativeLayout mRe_iv;// 图片包裹
        private RelativeLayout re_care;
        private RelativeLayout re_care_cancel;
        private ImageView iv_eval;

        public ViewHolder(View view) {
            mYouliao_yuantu = (RoundImageView) view.findViewById(R.id.youliao_yuantu);
            mTv_name = (TextView) view.findViewById(R.id.tv_name);
            mIv_level = (ImageView) view.findViewById(R.id.iv_level);
            mTv_time = (TextView) view.findViewById(R.id.tv_time);
            mTv_content = (TextView) view.findViewById(R.id.tv_content);
            mIv_photo = (RoundImageView) view.findViewById(R.id.iv_photo);
            mTv_count = (TextView) view.findViewById(R.id.tv_count);
            mTv_custom = (TextView) view.findViewById(R.id.tv_custom);
            mPraise_count = (TextView) view.findViewById(R.id.praise_count);
            mTv_key = (TextView) view.findViewById(R.id.tv_key);
            mRe_zan = (RelativeLayout) view.findViewById(R.id.re_zan);
            mIv_zan_red = (ImageView) view.findViewById(R.id.iv_zan_red);
            mIv_zan_gray = (ImageView) view.findViewById(R.id.iv_zan_gray);
            mRe_collect = (RelativeLayout) view.findViewById(R.id.re_collect);
            mIv_collect_gray = (ImageView) view.findViewById(R.id.iv_collect_gray);
            mIv_collect_red = (ImageView) view.findViewById(R.id.iv_collect_red);
            mRe_iv = (RelativeLayout) view.findViewById(R.id.re_iv);
            re_care = (RelativeLayout) view.findViewById(R.id.re_care);
            re_care_cancel = (RelativeLayout) view.findViewById(R.id.re_care_cancel);
            iv_eval = (ImageView) view.findViewById(R.id.iv_eval);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
