package com.lsjr.zizisteward.ly.activity;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.CircleBean;
import com.lsjr.zizisteward.bean.CircleBean.Circle;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.newview.CircleImageView;
import com.lsjr.zizisteward.newview.MyGridView;
import com.lsjr.zizisteward.photoflow.ImagePagerActivity;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.PullToRefreshLayout;
import com.lsjr.zizisteward.utils.PullableScrollView;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_Circle_Friends extends Activity implements OnClickListener {

    private View v_space;
    private LinearLayout ll;
    private com.lsjr.zizisteward.newview.MyListView s_lv;
    private TextView tv_send;
    private EditText et_content;
    private LinearLayout ll_add;
    private LinearLayout ll_back;
    private PullableScrollView psv;
    public static TextView tv_name;
    private LinearLayout ll_publish;
    private PullToRefreshLayout ptrl;
    public static RoundImageView iv_head;
    private static ImageView iv_backgroup;

    public static List<Circle> circle = new ArrayList<>();

    private Uri uri;
    private String path;

    private int width = 0;
    private int count = 8;
    private int pageNum = 1;
    public static FCFAdapter fcf_adapter;

    private static int requestTime = 0;
    private int readTimeOut = 50 * 1000; // 读取超时
    private int connectTimeout = 50 * 1000; // 超时时间
    private boolean god = false;
    private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识
    // 随机生成
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
    private static final String CHARSET = "utf-8"; // 设置编码

    private static Context context;
    private InputMethodManager imm;
    private String share_id;
    private int _p = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.fcf_activity);
        this.findViewById();
    }

    private void findViewById() {

        context = this;

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        this.v_space = findViewById(R.id.v_space);
        this.ll = (LinearLayout) findViewById(R.id.ll);
        this.s_lv = (com.lsjr.zizisteward.newview.MyListView) findViewById(R.id.s_lv);
        this.tv_send = (TextView) findViewById(R.id.tv_send);
        this.tv_name = (TextView) findViewById(R.id.tv_name);
        this.ll_add = (LinearLayout) findViewById(R.id.ll_add);
        this.psv = (PullableScrollView) findViewById(R.id.psv);
        this.ll_back = (LinearLayout) findViewById(R.id.ll_back);
        this.iv_head = (RoundImageView) findViewById(R.id.iv_head);
        this.et_content = (EditText) findViewById(R.id.et_content);
        this.ll_publish = (LinearLayout) findViewById(R.id.ll_publish);
        this.iv_backgroup = (ImageView) findViewById(R.id.iv_backgroup);
        this.ptrl = (PullToRefreshLayout) findViewById(R.id.refresh_view);

        this.ll_add.setOnClickListener(this);
        this.tv_send.setOnClickListener(this);
        this.ll_back.setOnClickListener(this);
        this.iv_head.setOnClickListener(this);
        this.iv_backgroup.setOnClickListener(this);

        this.iv_head.setVisibility(View.VISIBLE);

        this.tv_name.setText(App.getUserInfo().getUsername());

        Glide.with(Fragment_Circle_Friends.this).load(HttpConfig.IMAGEHOST + App.getUserInfo().getHeadImg())
                .into(iv_head);

        this.pageNum = 1;
        this.getData(2);
        this.getBackGroup(0);

        this.s_lv.setFocusable(false);

        this.ptrl.setOnRefreshListener(new MyListener());

        this.s_lv.setSelector(new ColorDrawable());

        this.s_lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ll_publish.setVisibility(View.GONE);
                et_content.clearFocus();
                imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);
                startActivityForResult(new Intent(Fragment_Circle_Friends.this, ActivityFriendsCircleDetails.class)
                        .putExtra("share_id", circle.get(position).getId())
                        .putExtra("user_id", App.getUserInfo().getId())
                        .putExtra("name", circle.get(position).getUser_name()).putExtra("pos", position)
                        .putExtra("photo", circle.get(position).getPhoto()), 8);
            }
        });
    }

    private class MyListener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            pageNum = 1;
            getData(0);
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            if (ll.getVisibility() == View.VISIBLE) {
                pageNum += 1;
                getData(1);
            } else {
                ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void getData(final int space) {

        Map<String, String> map = new HashMap<>();
        map.put("OPT", "234");
        map.put("currPage", String.valueOf(pageNum));
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(Fragment_Circle_Friends.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {

                System.out.println("朋友圈" + result);

                CircleBean cBean = new Gson().fromJson(result, CircleBean.class);

                switch (space) {

                    case 0:

                        // 刷新
                        fcf_adapter = null;
                        circle = new ArrayList<>();
                        circle = cBean.getCircle();

                        if (null != circle && circle.size() > 0) {

                            s_lv.setVisibility(View.VISIBLE);
                            v_space.setVisibility(View.GONE);

                            setLy();

                            if (null != circle && circle.size() > 5) {
                                ll.setVisibility(View.VISIBLE);
                            } else {
                                ll.setVisibility(View.GONE);
                            }

                        } else {
                            s_lv.setVisibility(View.GONE);
                            v_space.setVisibility(View.VISIBLE);
                        }

                        ptrl.refreshFinish(ptrl.SUCCEED);
                        break;

                    case 1:

                        // 加载更多
                        if (null != cBean.getCircle()) {

                            List<Circle> load_c = new ArrayList<>();

                            load_c = cBean.getCircle();

                            if (null != load_c && load_c.size() > 0) {
                                circle.addAll(load_c);
                                setLy();
                            }

                            if (null != load_c && load_c.size() > 5) {
                                ll.setVisibility(View.VISIBLE);
                            } else {
                                ll.setVisibility(View.GONE);
                            }
                        }

                        ptrl.loadmoreFinish(ptrl.SUCCEED);

                        break;

                    case 2:

                        fcf_adapter = null;
                        circle = new ArrayList<>();
                        circle = cBean.getCircle();

                        if (null != circle && circle.size() > 0) {

                            s_lv.setVisibility(View.VISIBLE);
                            v_space.setVisibility(View.GONE);

                            setLy();

                            if (null != circle && circle.size() > 5) {
                                ll.setVisibility(View.VISIBLE);
                            } else {
                                ll.setVisibility(View.GONE);
                            }

                            DisplayMetrics dm = new DisplayMetrics();
                            Fragment_Circle_Friends.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                            width = dm.widthPixels;
                        } else {
                            s_lv.setVisibility(View.GONE);
                            v_space.setVisibility(View.VISIBLE);
                        }

                        break;
                }
            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });
    }

    private void setLy() {

        for (int i = 0; i < circle.size(); i++) {

            if (null != circle.get(i).getShare_time_uid() && circle.get(i).getShare_time_uid().length() > 0) {

                String[] space = circle.get(i).getShare_time_uid().split(",");

                for (int j = 0; j < space.length; j++) {
                    if (space[j].equals(App.getUserInfo().getId())) {
                        circle.get(i).setLy(true);
                    } else {
                        circle.get(i).setLy(false);
                    }
                }
            }
        }

        fcf_adapter = new FCFAdapter(Fragment_Circle_Friends.this, circle);
        s_lv.setAdapter(fcf_adapter);
        fcf_adapter.notifyDataSetChanged();
    }

    class FCFAdapter extends BaseAdapter {

        private Context context;
        private List<Circle> circle;
        private ViewHolder view;
        private LayoutParams linParams;

        public FCFAdapter(Context context, List<Circle> circle) {
            this.context = context;
            this.circle = circle;
        }

        @Override
        public int getCount() {
            return null == circle ? 0 : circle.size();
        }

        @Override
        public Object getItem(int position) {
            return circle.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(R.layout.fcf_item, null);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {

                view = (ViewHolder) convertView.getTag();

            }

            // TODO 头像
            Glide.with(context).load(HttpConfig.IMAGEHOST + circle.get(position).getPhoto()).into(view.c_iv);
            // TODO 内容
            view.tv_content.setText(circle.get(position).getContent());
            // TODO 点赞数
            view.tv_num.setText(returnNumber(circle.get(position).getShare_like()));
            // TODO 评论数
            view.tv_comments_num.setText(returnNumber(circle.get(position).getShare_comment()));
            // TODO 用户名字
            view.tv_name.setText(circle.get(position).getUser_name());

            if (circle.get(position).isLy()) {
                view.iv_like_cancel.setVisibility(View.VISIBLE);
                view.iv_like.setVisibility(View.GONE);
            } else {
                view.iv_like_cancel.setVisibility(View.GONE);
                view.iv_like.setVisibility(View.VISIBLE);
            }

            view.iv_like.setTag(position);// 灰色
            view.iv_like.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    CustomDialogUtils.startCustomProgressDialog(context, "请稍候");
                    final int pos = (int) v.getTag();
                    Map<String, String> map = new HashMap<>();
                    map.put("OPT", "187");
                    map.put("share_id", circle.get(pos).getId());
                    map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                    new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            CustomDialogUtils.stopCustomProgressDialog(context);
                            circle.get(pos).setShare_like(
                                    String.valueOf(Integer.valueOf(circle.get(pos).getShare_like()) + 1));
                            circle.get(pos).setLy(true);
                            fcf_adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(MyError myError) {
                            CustomDialogUtils.stopCustomProgressDialog(context);
                            super.onFailure(myError);
                        }
                    });
                }
            });

            view.iv_like_cancel.setTag(position);// 红色
            view.iv_like_cancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    CustomDialogUtils.startCustomProgressDialog(context, "请稍候");
                    final int pos = (int) v.getTag();
                    Map<String, String> map = new HashMap<>();
                    map.put("OPT", "67");
                    map.put("share_id", circle.get(pos).getId());
                    map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                    new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            CustomDialogUtils.stopCustomProgressDialog(context);
                            circle.get(pos).setShare_like(
                                    String.valueOf(Integer.valueOf(circle.get(pos).getShare_like()) - 1));
                            circle.get(pos).setLy(false);
                            fcf_adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(MyError myError) {
                            CustomDialogUtils.stopCustomProgressDialog(context);
                            super.onFailure(myError);
                        }
                    });
                }
            });

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            if (null != circle.get(position).getShare_time().getTime()) {

                Date date = new Date(Long.valueOf(circle.get(position).getShare_time().getTime()));

                if (null != date) {

                    view.tv_time.setText(format.format(date));

                }
            }

            if (null != circle.get(position).getShareImg() && circle.get(position).getShareImg().length() > 1) {

                linParams = (LayoutParams) view.gv.getLayoutParams();

                String[] strs = circle.get(position).getShareImg().split(",");

                if (strs.length > 0) {

                    view.gv.setVisibility(View.VISIBLE);

                    switch (strs.length) {
                        case 1:

                            view.gv.setNumColumns(1);
                            linParams.width = (width - 52) / 3;

                            linParams.leftMargin = 45;
                            view.gv.setLayoutParams(linParams);

                            break;

                        case 2:
                        case 4:

                            view.gv.setNumColumns(2);
                            linParams.width = ((width - 52) / 3) * 2;
                            linParams.height = (width - 52) / 3;
                            linParams.leftMargin = 45;
                            view.gv.setLayoutParams(linParams);

                            break;

                        case 3:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:

                            view.gv.setNumColumns(3);
                            linParams.width = width - 52;
                            linParams.height = (width - 52) / 3;
                            linParams.leftMargin = 26;
                            linParams.rightMargin = 26;
                            view.gv.setLayoutParams(linParams);

                            break;
                    }

                    GVAdapter adapter = new GVAdapter(context, strs, (width - 52) / 3);

                    view.gv.setAdapter(adapter);

                    adapter.notifyDataSetChanged();

                } else {
                    view.gv.setVisibility(View.GONE);
                }
            } else {
                view.gv.setVisibility(View.GONE);
            }

            view.ll_comm.setTag(position);
            view.ll_comm.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
//					startActivityForResult(new Intent(Fragment_Circle_Friends.this, ActivityFriendsCircleDetails.class)
//							.putExtra("share_id", circle.get(pos).getId())
//							.putExtra("user_id", circle.get(pos).getUser_id())
//							.putExtra("name", circle.get(pos).getUser_name())
//							.putExtra("photo", circle.get(pos).getPhoto()), 0);
                    share_id = circle.get(pos).getId();
                    ll_publish.setVisibility(View.VISIBLE);
                    et_content.requestFocus();

                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            });

            return convertView;
        }

        protected void imageBrower(int position, ArrayList<String> urls2) {
            Intent intent = new Intent(context, ImagePagerActivity.class);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
            context.startActivity(intent);
        }

        private class ViewHolder {

            /** 朋友圈 */
            /**
             * 展示的图片
             */
            private ImageView iv_god;
            /**
             * 用户头像
             */
            private CircleImageView c_iv;
            /**
             * 用户名字
             */
            private TextView tv_name;
            /**
             * 展示文字
             */
            private TextView tv_content;
            /**
             * 展示时间
             */
            private TextView tv_time;
            /**
             * 评论数
             */
            private TextView tv_comments_num;
            /**
             * 点赞
             */
            private TextView tv_num;
            private LinearLayout ll_like;
            private LinearLayout ll_comm;

            private ImageView iv_like;
            private ImageView iv_like_cancel;
            private MyGridView gv;

            public ViewHolder(View v) {

                this.gv = (MyGridView) v.findViewById(R.id.gv);
                this.tv_num = (TextView) v.findViewById(R.id.tv_num);
                this.tv_name = (TextView) v.findViewById(R.id.tv_name);
                this.tv_time = (TextView) v.findViewById(R.id.tv_time);
                this.iv_like = (ImageView) v.findViewById(R.id.iv_like);
                this.c_iv = (CircleImageView) v.findViewById(R.id.c_iv);
                this.iv_god = (ImageView) v.findViewById(R.id.iv_weixin);
                this.ll_like = (LinearLayout) v.findViewById(R.id.ll_like);
                this.ll_comm = (LinearLayout) v.findViewById(R.id.ll_comm);
                this.tv_content = (TextView) v.findViewById(R.id.tv_content);
                this.iv_like_cancel = (ImageView) v.findViewById(R.id.iv_like_cancel);
                this.tv_comments_num = (TextView) v.findViewById(R.id.tv_comments_num);
            }
        }
    }

    private class GVAdapter extends BaseAdapter {

        private String[] path;
        private Context context;
        private ViewHolder view;
        private int width;
        private ArrayList<String> imageUrls;

        public GVAdapter(Context context, String[] path, int width) {
            this.context = context;
            this.path = path;
            this.width = width;
        }

        @Override
        public int getCount() {
            return null == path ? 0 : path.length;
        }

        @Override
        public Object getItem(int position) {
            return path[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(R.layout.fcf_gv_item, null);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {

                view = (ViewHolder) convertView.getTag();

            }

            LayoutParams linParams = (LayoutParams) view.iv.getLayoutParams();

            linParams.width = width;

            linParams.height = width;

            view.iv.setLayoutParams(linParams);

            Picasso.with(context).load(HttpConfig.IMAGEHOST + path[position]).into(view.iv);

            imageUrls = new ArrayList<>(Arrays.asList(path));

            view.iv.setTag(position);
            view.iv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    imageBrower(pos, imageUrls);
                }
            });

            return convertView;
        }

        protected void imageBrower(int position, ArrayList<String> urls2) {
            Intent intent = new Intent(context, ImagePagerActivity.class);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
            context.startActivity(intent);
        }

        private class ViewHolder {

            private ImageView iv;

            public ViewHolder(View v) {
                this.iv = (ImageView) v.findViewById(R.id.iv);
            }
        }
    }

    private String returnNumber(String space) {

        int length = space.length();

        if (length <= 4) {
            return space;
        } else if (length <= 8) {
            return space.substring(0, length - 4) + "万";
        } else if (length > 8) {
            return space.substring(0, length - 8) + "亿";
        }

        return space;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ll_back:

                ll_publish.setVisibility(View.GONE);
                et_content.clearFocus();
                imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);
                finish();

                break;

            case R.id.ll_add:
                ll_publish.setVisibility(View.GONE);
                et_content.clearFocus();
                imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);
                startActivityForResult(new Intent(Fragment_Circle_Friends.this, SendCircleFriendsActivity.class), 1);

                break;

            case R.id.iv_backgroup:
                ll_publish.setVisibility(View.GONE);
                et_content.clearFocus();
                imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);
                startActivityForResult(new Intent(Fragment_Circle_Friends.this, ReplaceTheCoverActivity.class), 2);

                break;

            case R.id.iv_head:
                ll_publish.setVisibility(View.GONE);
                et_content.clearFocus();
                imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);
                startActivity(new Intent(Fragment_Circle_Friends.this, PersonalInformationActivity.class));
                break;

            case R.id.tv_send:

                ll_publish.setVisibility(View.GONE);
                et_content.clearFocus();
                imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);

                String content = et_content.getText().toString();

                if (null != content && content.length() > 0) {
                    Map<String, String> map_send = new HashMap<>();
                    map_send.put("OPT", "188");
                    map_send.put("share_id", share_id);
                    map_send.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                    map_send.put("content", content);

                    new HttpClientGet(Fragment_Circle_Friends.this, null, map_send, false,
                            new HttpClientGet.CallBacks<String>() {

                                @Override
                                public void onSuccess(String result) {

                                    circle.get(_p)
                                            .setShare_comment(String.valueOf(Integer.valueOf(
                                                    Fragment_Circle_Friends.circle
                                                            .get(_p).getShare_comment()) + 1));
                                    fcf_adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(MyError myError) {
                                    super.onFailure(myError);
                                }
                            });
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {
            case 1:
                // 发送朋友圈回调
                pageNum = 1;
                getData(0);
                break;

            case 2:
                // 更换朋友圈背景图片回调
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "请选择图片"), 101);
                break;

            case 44:
                pageNum = 1;
                getData(2);
                break;

        }

        switch (requestCode) {

            case 101:

                if (null != data) {
                    uri = data.getData();
                    if (null != uri) {
                        path = uri.getPath();
                        Intent it = new Intent();
                        it.setAction("com.android.camera.action.CROP");
                        it.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
                        it.putExtra("crop", "false");
                        it.putExtra("aspectX", 2);// 裁剪框比例
                        it.putExtra("aspectY", 1);
                        it.putExtra("outputX", 500);// 输出图片大小
                        it.putExtra("outputY", 250);
                        it.putExtra("return-data", true);
                        startActivityForResult(it, 102);
                    }
                }

                break;

            case 102:
                // 拿到剪切数据

                if (null != data) {

                    Bitmap bmap = data.getParcelableExtra("data");
                    if (null == bmap) {

                    } else {
                        try {

                            File file = saveFile(bmap, Environment.getExternalStorageDirectory().toString(), "ly_bg.png");

                            ToUp(file);
                            iv_backgroup.setImageBitmap(bmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
                break;
        }
    }

    private void ToUp(File file) {
        String fileKey = "shareImg";
        Map<String, String> map = new HashMap<>();
        map.put("type", "1");
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        ToUp_Zero(file, fileKey, HttpConfig.Up_friend_cicle, map);
    }

    private File saveFile(Bitmap bm, String path, String fileName) throws IOException {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path, fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;
    }

    private void ToUp_Zero(final File file, final String fileKey, final String RequestURL,
                           final Map<String, String> param) {

        if (null == file || (!file.exists())) {
            Toast.makeText(Fragment_Circle_Friends.this, "请重新选择图片...", Toast.LENGTH_SHORT).show();
        } else {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    String result = null;
                    long requestTime = System.currentTimeMillis();
                    long responseTime = 0;

                    try {
                        URL url = new URL(RequestURL);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(readTimeOut);
                        conn.setConnectTimeout(connectTimeout);
                        conn.setDoInput(true); // 允许输入流
                        conn.setDoOutput(true); // 允许输出流
                        conn.setUseCaches(false); // 不允许使用缓存
                        conn.setRequestMethod("POST"); // 请求方式
                        conn.setRequestProperty("Charset", CHARSET); // 设置编码
                        conn.setRequestProperty("connection", "keep-alive");
                        conn.setRequestProperty("user-agent",
                                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
                        conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
                        // conn.setRequestProperty("Content-Type",
                        // "application/x-www-form-urlencoded");

                        /**
                         * 当文件不为空，把文件包装并且上传
                         */
                        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                        StringBuffer sb = null;
                        String params = "";

                        /***
                         * 以下是用于上传参数
                         */
                        if (param != null && param.size() > 0) {
                            Iterator<String> it = param.keySet().iterator();
                            while (it.hasNext()) {
                                sb = null;
                                sb = new StringBuffer();
                                String key = it.next();
                                String value = param.get(key);
                                sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                                sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"")
                                        .append(LINE_END).append(LINE_END);
                                sb.append(value).append(LINE_END);
                                params = sb.toString();
                                dos.write(params.getBytes());
                                // dos.flush();
                            }
                        }

                        sb = null;
                        params = null;
                        sb = new StringBuffer();
                        /**
                         * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                         * filename是文件的名字，包含后缀名的 比如:abc.png
                         */
                        sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                        sb.append("Content-Disposition:form-data; name=\"" + fileKey + "\"; filename=\""
                                + file.getName() + "\"" + LINE_END);
                        sb.append("Content-Type:image/pjpeg" + LINE_END); // 这里配置的Content-type很重要的
                        // ，用于服务器端辨别文件的类型的
                        sb.append(LINE_END);
                        params = sb.toString();
                        System.out.println(sb.toString());
                        sb = null;

                        dos.write(params.getBytes());
                        /** 上传文件 */
                        InputStream is = new FileInputStream(file);
                        byte[] bytes = new byte[1024];
                        int len = 0;
                        int curLen = 0;
                        while ((len = is.read(bytes)) != -1) {
                            curLen += len;
                            dos.write(bytes, 0, len);
                        }
                        is.close();

                        dos.write(LINE_END.getBytes());
                        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                        dos.write(end_data);
                        dos.flush();
                        //
                        // dos.write(tempOutputStream.toByteArray());
                        /**
                         * 获取响应码 200=成功 当响应成功，获取响应的流
                         */
                        int res = conn.getResponseCode();
                        responseTime = System.currentTimeMillis();
                        if (res == 200) {
                            InputStream input = conn.getInputStream();
                            InputStreamReader isr = new InputStreamReader(input, "UTF-8");

                            StringBuffer sb1 = new StringBuffer();
                            int ss;
                            while ((ss = isr.read()) != -1) {
                                sb1.append((char) ss);
                            }
                            result = URLDecoder.decode(sb1.toString(), "utf-8");

                            try {
                                System.out.println("上传背景图: " + result);
                                JSONObject jObject = new JSONObject(result);
                                String error = jObject.getString("error");
                                String msg = jObject.getString("msg");
                                Bitmap bp = BitmapFactory.decodeFile(file.getPath());
                                getBackGroup(2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            return;
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }).start();
        }
    }

    public static void getBackGroup(final int space) {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "261");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jObject = new JSONObject(result);

                    switch (space) {
                        case 0:
                            String background_picture = jObject.getString("background_picture");
                            String img_wh = jObject.getString("img_wh");
                            String photo = jObject.getString("photo");
                            String user_name = jObject.getString("user_name");
                            if (!TextUtils.isEmpty(user_name)) {
                                tv_name.setText(user_name);
                            }

                            if (null != photo && photo.length() > 1) {
                                Glide.with(context).load(HttpConfig.IMAGEHOST + photo).into(iv_head);
                            }

                            if (null != background_picture && background_picture.length() > 1) {
                                Glide.with(context).load(HttpConfig.IMAGEHOST + background_picture).into(iv_backgroup);
                            } else {
                                iv_backgroup.setImageResource(R.drawable.friend_circle_backgroup);
                            }
                            break;

                        case 1:
                            String _photo = jObject.getString("photo");
                            String _user_name = jObject.getString("user_name");

                            if (!TextUtils.isEmpty(_user_name)) {
                                tv_name.setText(_user_name);
                            }

                            if (null != _photo && _photo.length() > 1) {
                                Glide.with(context).load(HttpConfig.IMAGEHOST + _photo).into(iv_head);
                            }

                            break;

                        case 2:
                            String _background_picture = jObject.getString("background_picture");

                            if (null != _background_picture && _background_picture.length() > 1) {
                                Glide.with(context).load(HttpConfig.IMAGEHOST + _background_picture).into(iv_backgroup);
                            } else {
                                iv_backgroup.setImageResource(R.drawable.friend_circle_backgroup);
                            }

                            break;
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
