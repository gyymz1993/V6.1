package com.lsjr.zizisteward.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.BasicUserInfo;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.CardHolderActivity;
import com.lsjr.zizisteward.ly.activity.MyAttention;
import com.lsjr.zizisteward.ly.activity.MyCollection;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.newview.CropCircleTransformation;
import com.lsjr.zizisteward.recyclerview.FullyLinearLayoutManager;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalCenterActivity extends Activity implements View.OnClickListener {
    private RecyclerView recyclerview;
    private String[] titles = {"服务方案", "小孜关怀", "名片夹", "专属管家"};
    private int[] images = {R.drawable.service_project, R.drawable.xiaozicare, R.drawable.mylabel, R.drawable.zhuanshu_steward};
    private MyAdapter adapter;
    private Intent intent;
    private ImageView iv_set, iv_level;
    private RoundImageView user_photo;
    private RelativeLayout re_to_zizi_nums, re_vip_level, re_order, re_collect, re_care, mImageButton1, mImageButton2, mCall, mImageButton4;
    private TextView tv_order_nums, tv_collect_nums, tv_care_nums, tv_real_name, tv_zizi_nums;
    private Map<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().addActivity(this);
        setContentView(R.layout.activity_personal_center);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new FullyLinearLayoutManager(PersonalCenterActivity.this));
        iv_set = (ImageView) findViewById(R.id.iv_set);/*设置按钮*/
        iv_level = (ImageView) findViewById(R.id.iv_level);/*用户等级图标*/
        user_photo = (RoundImageView) findViewById(R.id.user_photo);/*用户图像*/
        re_to_zizi_nums = (RelativeLayout) findViewById(R.id.re_to_zizi_nums);/*豆孜数箭头按钮*/
        re_vip_level = (RelativeLayout) findViewById(R.id.re_vip_level);/*会员权益*/
        re_order = (RelativeLayout) findViewById(R.id.re_order);/*订单*/
        re_collect = (RelativeLayout) findViewById(R.id.re_collect);/*收藏*/
        re_care = (RelativeLayout) findViewById(R.id.re_care);/*关注*/
        tv_order_nums = (TextView) findViewById(R.id.tv_order_nums);/*订单数字*/
        tv_collect_nums = (TextView) findViewById(R.id.tv_collect_nums);/*收藏数字*/
        tv_care_nums = (TextView) findViewById(R.id.tv_care_nums);/*关注数字*/
        mImageButton1 = (RelativeLayout) findViewById(R.id.imageButton1);// 首页
        mImageButton2 = (RelativeLayout) findViewById(R.id.imageButton2);// 视界
        mCall = (RelativeLayout) findViewById(R.id.call);// 打电话
        mImageButton4 = (RelativeLayout) findViewById(R.id.imageButton4);// 圈子
        tv_real_name = (TextView) findViewById(R.id.tv_real_name);/*昵称*/
        tv_zizi_nums = (TextView) findViewById(R.id.tv_zizi_nums);/*豆孜数量*/
        initView();
        initListener();
    }

    private void initListener() {
        iv_set.setOnClickListener(this);
        user_photo.setOnClickListener(this);
        re_to_zizi_nums.setOnClickListener(this);
        re_vip_level.setOnClickListener(this);
        re_order.setOnClickListener(this);
        re_collect.setOnClickListener(this);
        re_care.setOnClickListener(this);
        mImageButton1.setOnClickListener(this);
        mImageButton2.setOnClickListener(this);
        mImageButton4.setOnClickListener(this);
        mCall.setOnClickListener(this);
    }

    private void initView() {
        adapter = new MyAdapter(PersonalCenterActivity.this, titles);
        recyclerview.setAdapter(adapter);
        adapter.setOnClickListener(new OnItemClickListener() {
            @Override
            public void ItemClickListener(View view, int position) {
                switch (position) {
                    case 0:
                        intent = new Intent(PersonalCenterActivity.this, ServiceProjectActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(PersonalCenterActivity.this, SmallZiZiCareActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(PersonalCenterActivity.this, CardHolderActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(PersonalCenterActivity.this, ExclusiveStewardActivity.class);
                        intent.putExtra("list", "1");
                        startActivity(intent);
                        break;
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_set:
                intent = new Intent(PersonalCenterActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.user_photo:
                intent = new Intent(PersonalCenterActivity.this, PersonalInfoActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.re_to_zizi_nums:
                intent = new Intent(PersonalCenterActivity.this, LevelActivity.class);
                intent.putExtra("url", "/viplevel?uid=" + App.getUserInfo().getId());
                startActivity(intent);
                break;
            case R.id.re_vip_level:
                intent = new Intent(PersonalCenterActivity.this, VipCrossActiviy.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.re_order:
                intent = new Intent(PersonalCenterActivity.this, MyAllOrderActivity.class);
                intent.putExtra("order", "all");
                startActivity(intent);
                break;
            case R.id.re_collect:
                intent = new Intent(PersonalCenterActivity.this, MyCollection.class);
                startActivity(intent);
                break;
            case R.id.re_care:
                intent = new Intent(PersonalCenterActivity.this, MyAttention.class);
                startActivity(intent);
                break;
            case R.id.imageButton1:
                intent = new Intent(PersonalCenterActivity.this, SixthNewActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
            case R.id.imageButton2:
                intent = new Intent(PersonalCenterActivity.this, ZiXunActivity.class);
                intent.putExtra("type", "");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
            case R.id.imageButton4:
                App.requestAddressBook(PersonalCenterActivity.this, 0);
                break;
            case R.id.call:
                intent = new Intent(PersonalCenterActivity.this, CallButtonActivtiy.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

    public interface OnItemClickListener {
        void ItemClickListener(View view, int position);
    }


    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private Context context;
        private String[] titles;
        private OnItemClickListener clickListener;

        public void setOnClickListener(OnItemClickListener clickListener) {
            this.clickListener = clickListener;
        }

        public MyAdapter(Context context, String[] titles) {
            this.titles = titles;
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_personal, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.iv_photo.setImageResource(images[position]);
            holder.tv_name.setText(titles[position]);
            if (clickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentPos = holder.getAdapterPosition();
                        clickListener.ItemClickListener(holder.itemView, currentPos);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return titles == null ? 0 : titles.length;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private ImageView iv_photo;

        public MyViewHolder(View view) {
            super(view);
            iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
        }
    }


    private void getData2() {
        map = new HashMap<>();
        map.put("OPT", "199");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("用户消息" + result);
                BasicUserInfo bean = GsonUtil.getInstance().fromJson(result, BasicUserInfo.class);
                if (bean.getCredit().equals("0")) {
                    iv_level.setImageResource(R.drawable.level_zero);
                } else if (bean.getCredit().equals("1")) {
                    iv_level.setImageResource(R.drawable.level_one);
                } else if (bean.getCredit().equals("2")) {
                    iv_level.setImageResource(R.drawable.level_two);
                } else if (bean.getCredit().equals("3")) {
                    iv_level.setImageResource(R.drawable.level_three);
                } else if (bean.getCredit().equals("6")) {
                    iv_level.setImageResource(R.drawable.level_six);
                } else if (bean.getCredit().equals("5")) {
                    iv_level.setImageResource(R.drawable.level_five);
                } else if (bean.getCredit().equals("4")) {
                    iv_level.setImageResource(R.drawable.level_three);
                }
                tv_zizi_nums.setText("豆孜:  " + bean.getScore());
            }
        });
    }

    private void getData() {
        map = new HashMap<String, String>();
        map.put("OPT", "56");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("数量" + result);
                NumberBean bean = GsonUtil.getInstance().fromJson(result, NumberBean.class);
                int int1 = bean.getIndentCount().getFortheGoodCount();
                int int2 = bean.getIndentCount().getObligationCount();
                int int3 = bean.getIndentCount().getOverhangCount();
                int int4 = bean.getIndentCount().getToevaluateCount();
                int total_num = int1 + int2 + int3 + int4;
                tv_order_nums.setText(String.valueOf(total_num));
            }

        });
    }

    private void getData3() {
        map = new HashMap<String, String>();
        map.put("OPT", "515");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("收藏和关注" + result);
                try {
                    JSONObject json = new JSONObject(result);
                    String countCollect = json.getString("countCollect");
                    String countFocus = json.getString("countFocus");
                    tv_collect_nums.setText(countCollect);
                    tv_care_nums.setText(countFocus);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onResume() {
        super.onResume();

        if (!TextUtils.isEmpty(App.getUserInfo().getReality_name())) {
            tv_real_name.setText(App.getUserInfo().getReality_name());
        }

        if (TextUtils.isEmpty(App.getUserInfo().getReality_name())) {
            if ("0".equals(App.getUserInfo().getSex())) {
                tv_real_name.setText("先生/女士");
            }
            if ("1".equals(App.getUserInfo().getSex())) {
                tv_real_name.setText("先生");
            }
            if ("2".equals(App.getUserInfo().getSex())) {
                tv_real_name.setText("女士");
            }
        }

        Glide.with(this).load(HttpConfig.IMAGEHOST + App.getUserInfo().getHeadImg()).placeholder(R.drawable.icon_head)
                .bitmapTransform(new CropCircleTransformation(this)).dontAnimate().into(user_photo);
        getData();// 订单状态数量
        getData2();// 用户消息状态
        getData3();/*收藏数量和关注数量*/
//        getBannerData();/*广告图*/

    }



/*    private void getBannerData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "510");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(PersonalCenterActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.print("图片" + result);
                final PicturesBean bean = GsonUtil.getInstance().fromJson(result, PicturesBean.class);
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                int widthPixels = displayMetrics.widthPixels;
                int heightP = widthPixels / 5;
                RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) iv_adver.getLayoutParams();
                linearParams.width = widthPixels;
                linearParams.height = heightP;
                iv_adver.setLayoutParams(linearParams);
                Glide.with(PersonalCenterActivity.this).load(HttpConfig.IMAGEHOST + bean.getAdvertisements().get(0).getImage_filename()).into(iv_adver);
                iv_adver.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(PersonalCenterActivity.this, ZeroToVipActivity.class);
                        intent.putExtra("url", bean.getUrl());
                        startActivity(intent);
                    }
                });
            }
        });
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            startActivity(new Intent(PersonalCenterActivity.this, SixthNewActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class NumberBean {
        private String error;
        private NumberDetail indentCount;
        private String msg;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public NumberDetail getIndentCount() {
            return indentCount;
        }

        public void setIndentCount(NumberDetail indentCount) {
            this.indentCount = indentCount;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public class NumberDetail {
        private int FortheGoodCount;
        private int ObligationCount;
        private int OverhangCount;
        private int ToevaluateCount;

        public int getFortheGoodCount() {
            return FortheGoodCount;
        }

        public void setFortheGoodCount(int fortheGoodCount) {
            FortheGoodCount = fortheGoodCount;
        }

        public int getObligationCount() {
            return ObligationCount;
        }

        public void setObligationCount(int obligationCount) {
            ObligationCount = obligationCount;
        }

        public int getOverhangCount() {
            return OverhangCount;
        }

        public void setOverhangCount(int overhangCount) {
            OverhangCount = overhangCount;
        }

        public int getToevaluateCount() {
            return ToevaluateCount;
        }

        public void setToevaluateCount(int toevaluateCount) {
            ToevaluateCount = toevaluateCount;
        }
    }

    public class PicturesBean {
        private List<PicturesList> advertisements;
        private String url;

        public List<PicturesList> getAdvertisements() {
            return advertisements;
        }

        public void setAdvertisements(List<PicturesList> advertisements) {
            this.advertisements = advertisements;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }


    }

    public class PicturesList {
        public String getImage_filename() {
            return image_filename;
        }

        public void setImage_filename(String image_filename) {
            this.image_filename = image_filename;
        }

        private String image_filename;
    }
}
