package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.share.OnekeyShare;
import com.lsjr.zizisteward.utils.EncryptUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

public class ShareDetailsActivity extends Activity implements OnClickListener {

    private LinearLayout ll_back;
    private LinearLayout ll_share;
    private TextView tv_title;
    private TextView tv_time;
    private MyListView mlv;
    private TextView tv_content;
    private TextView tv_save;
    private SDAdapter adapter;
    private String Tufsid;
    private TextView tv_collection;
    private String[] img;
    private String[] img_wh;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.share_details_activity);
        this.findViewById();
    }

    private void findViewById() {

        this.mlv = (MyListView) super.findViewById(R.id.mlv);
        this.tv_save = (TextView) super.findViewById(R.id.tv_save);
        this.tv_time = (TextView) super.findViewById(R.id.tv_time);
        this.tv_title = (TextView) super.findViewById(R.id.tv_title);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.ll_share = (LinearLayout) super.findViewById(R.id.ll_share);
        this.tv_content = (TextView) super.findViewById(R.id.tv_content);
        this.tv_collection = (TextView) super.findViewById(R.id.tv_collection);

        this.ll_back.setOnClickListener(this);
        this.ll_share.setOnClickListener(this);
        this.tv_collection.setOnClickListener(this);

        this.Tufsid = getIntent().getStringExtra("id");

        this.getData();
    }

    private void getData() {

        Map<String, String> map = new HashMap<>();
        map.put("OPT", "313");
        map.put("tufsid", Tufsid);
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));

        new HttpClientGet(ShareDetailsActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println(result);

                GroupShareBean gsBean = new Gson().fromJson(result, GroupShareBean.class);

                GroupShareBean.GroupShare gs = gsBean.getGroupShare();

                tv_title.setText(gs.getFlock_title());
                tv_content.setText(gs.getShare_content());

                if (gsBean.getCollection() == 0) {
                    tv_collection.setText("收藏");
                } else {
                    tv_collection.setText("取消收藏");
                }

                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");

                String time = format.format(Long.parseLong(gs.getStime().getTime()));

                tv_time.setText(time);

                if (null != gs.getPhoto() && gs.getPhoto().length() > 1) {
                    img = gs.getPhoto().split(",");
                    img_wh = gs.getImg_wh().split(",");

                    adapter = new SDAdapter(ShareDetailsActivity.this, img);

                    mlv.setAdapter(adapter);

                    DisplayMetrics dm = new DisplayMetrics();
                    ShareDetailsActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                    width = dm.widthPixels;
                } else {
                    mlv.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });

    }

    private class SDAdapter extends BaseAdapter {

        private Context context;
        private ViewHolder view;
        private String[] img;

        public SDAdapter(Context context, String[] img) {
            this.context = context;
            this.img = img;
        }

        @Override
        public int getCount() {
            return null == img ? 0 : img.length;
        }

        @Override
        public Object getItem(int position) {
            return img[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(R.layout.share_details_activity_item, null);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {

                view = (ViewHolder) convertView.getTag();

            }

            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) view.iv.getLayoutParams(); // 取控件textView当前的布局参数
            double space = 0;
            double _with = Double.valueOf(img_wh[position * 2]);
            double _height = Double.valueOf(img_wh[position * 2 + 1]);

            space = (width) / _with;

            linearParams.width = width;
            linearParams.height = (int) (_height * space);

            view.iv.setLayoutParams(linearParams);

            Glide.with(context).load(HttpConfig.IMAGEHOST + img[position]).into(view.iv);

            return convertView;
        }

        private class ViewHolder {
            private ImageView iv;

            public ViewHolder(View v) {
                this.iv = (ImageView) v.findViewById(R.id.iv);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.tv_collection:

                if (tv_collection.getText().toString().equals("收藏")) {
                    Map<String, String> map = new HashMap<>();
                    map.put("OPT", "316");
                    map.put("tufs_id", Tufsid);
                    map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));

                    new HttpClientGet(ShareDetailsActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            System.out.println(result);
                            tv_collection.setText("取消收藏");
                        }

                        @Override
                        public void onFailure(MyError myError) {
                            super.onFailure(myError);
                        }
                    });
                } else if (tv_collection.getText().toString().equals("取消收藏")) {
                    Map<String, String> map = new HashMap<>();
                    map.put("OPT", "320");
                    map.put("tufs_id", Tufsid);
                    map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));

                    new HttpClientGet(ShareDetailsActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            System.out.println(result);
                            tv_collection.setText("收藏");
                        }

                        @Override
                        public void onFailure(MyError myError) {
                            super.onFailure(myError);
                        }
                    });
                }

                break;

            case R.id.ll_share:

                ShareGroup();
                break;
        }
    }

    private void ShareGroup() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "405");
        map.put("flockid", Tufsid);
        new HttpClientGet(ShareDetailsActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("群分享：  " + result);

                try {
                    JSONObject jObject = new JSONObject(result);

                    String GroupShareUrl = jObject.getString("GroupShareUrl");
                    String error = jObject.getString("error");
                    String msg = jObject.getString("msg");

                    if (error.equals("1")) {

                        ShareSDK.initSDK(ShareDetailsActivity.this);
                        OnekeyShare oks = new OnekeyShare();
                        oks.disableSSOWhenAuthorize();
                        String productUrl = HttpConfig.IMAGEHOST + GroupShareUrl;
                        oks.setTitle("分享给您的群文件");
                        oks.setTitleUrl(productUrl);
                        //oks.setText(productUrl);
                        //oks.setImagePath("/sdcard/logods.jpg");//确保sdcard 存在图片
                        oks.setUrl(productUrl);
                        oks.setSite("孜孜管家");
                        oks.setSiteUrl(productUrl);

                        oks.setCallback(new PlatformActionListener() {

                            @Override
                            public void onError(Platform arg0, int arg1, Throwable arg2) {
                                System.out.println("分享失败");
                            }

                            @Override
                            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("OPT", "300");
                                map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                                new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                                    @Override
                                    public void onSuccess(String result) {
                                        System.out.println("积分结果" + result);
                                    }
                                });
                            }

                            @Override
                            public void onCancel(Platform arg0, int arg1) {
                                System.out.println("取消分享");
                            }
                        });

                        oks.show(ShareDetailsActivity.this);

                    } else {
                        Toast.makeText(ShareDetailsActivity.this, "请检查您的网络状态...", Toast.LENGTH_SHORT).show();
                    }

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
