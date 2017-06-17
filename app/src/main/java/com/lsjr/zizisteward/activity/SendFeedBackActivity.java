package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.RealNameConfirmActivity.StateBean;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.bean.BasicUserInfo;
import com.lsjr.zizisteward.bean.ImageItem;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.SCFBean;
import com.lsjr.zizisteward.newview.MyGridView;
import com.lsjr.zizisteward.utils.Bimp;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.MultipartRequest;
import com.lsjr.zizisteward.utils.ToastUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendFeedBackActivity extends BaseActivity implements OnClickListener {
    private RelativeLayout mRe_right;
    private EditText mEt_content;
    private TextView mTv_number;
    private MyGridView mGv_pic;
    private LinearLayout mLl_base;
    private String mPublish_id, zishang_id;
    private TextView mTv_level;
    private ImageView mIv_level;

    private GVAdapter gAdapter;
    public static String fileName;
    public static List<SCFBean.ImgBean> img_bean = new ArrayList<SCFBean.ImgBean>();
    public static String p_name = "ly";
    public static Uri uri;
    public static String path;// 图片地址
    private CharSequence temp;
    private PopWindowCatFriend mPopw;
    public static String gid;
    private int width;
    private String shiming_type;

    @Override
    public int getContainerView() {
        return R.layout.activity_send_feedback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("反馈");
        setmRight("发送");
        Intent intent = getIntent();
        mPublish_id = intent.getStringExtra("publish_id");
        zishang_id = intent.getStringExtra("zishang_id");
        mLl_base = (LinearLayout) findViewById(R.id.ll_base);// 父布局
        mRe_right = (RelativeLayout) findViewById(R.id.re_right);// 发送按钮
        mEt_content = (EditText) findViewById(R.id.et_content);// 编辑内容
        mTv_number = (TextView) findViewById(R.id.tv_number);// 编辑文字数量
        mGv_pic = (MyGridView) findViewById(R.id.gv_pic);// 图片网格
        mTv_level = (TextView) findViewById(R.id.tv_level);
        mIv_level = (ImageView) findViewById(R.id.iv_level);
        init();
        initListener();
        DisplayMetrics dm = new DisplayMetrics();
        SendFeedBackActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        LayoutParams lParams = (LayoutParams) mGv_pic.getLayoutParams();
        lParams.width = width / 3 * 2;
        mGv_pic.setLayoutParams(lParams);
    }

    @Override
    protected void onResume() {
        getData();
        getStateData();
        super.onResume();
    }

    // 实名认证状态
    private void getStateData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "88");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                StateBean bean = GsonUtil.getInstance().fromJson(result, StateBean.class);
                shiming_type = bean.getIsIdNumberVerified();
            }
        });
    }

    @Override
    protected void onDestroy() {
        Bimp.tempSelectBitmap = new ArrayList<ImageItem>();
        super.onDestroy();
    }

    private void getData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "199");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("用户消息" + result);
                BasicUserInfo bean = GsonUtil.getInstance().fromJson(result, BasicUserInfo.class);
                if ("0".equals(bean.getCredit())) {
                    mIv_level.setImageResource(R.drawable.level_zero);
                    mTv_level.setText("v0级会员");
                }
                if ("1".equals(bean.getCredit())) {
                    mIv_level.setImageResource(R.drawable.level_one);
                    mTv_level.setText("v1级会员");
                }
                if ("2".equals(bean.getCredit())) {
                    mIv_level.setImageResource(R.drawable.level_two);
                    mTv_level.setText("v2级会员");
                }
                if ("3".equals(bean.getCredit())) {
                    mIv_level.setImageResource(R.drawable.level_three);
                    mTv_level.setText("v3级会员");
                }
                if ("4".equals(bean.getCredit())) {
                    mIv_level.setImageResource(R.drawable.level_three);
                    mTv_level.setText("v3级会员");
                }
                if ("5".equals(bean.getCredit())) {
                    mIv_level.setImageResource(R.drawable.level_five);
                    mTv_level.setText("v5级会员");
                }
                if ("6".equals(bean.getCredit())) {
                    mIv_level.setImageResource(R.drawable.level_six);
                    mTv_level.setText("v6级会员");
                }

            }
        });
    }

    private void initListener() {
        mRe_right.setOnClickListener(this);
    }

    private void init() {
        img_bean = new ArrayList<>();
        mPopw = new PopWindowCatFriend(SendFeedBackActivity.this, "发送界面");
        SCFBean.ImgBean imgBean = new SCFBean.ImgBean();
        imgBean.setType(3);
        img_bean.add(imgBean);

        this.gAdapter = new GVAdapter(SendFeedBackActivity.this, img_bean);
        this.mGv_pic.setAdapter(gAdapter);

        this.mEt_content.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mTv_number.setText(temp.length() + "/240字");
            }
        });

        mGv_pic.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
                if (isOpen == true) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
                }

                boolean space = false;

                if (img_bean.get(pos).getType() != 3) {
                    space = true;
                }

                startActivityForResult(new Intent(SendFeedBackActivity.this, SelectorPicturesActivity.class)
                        .putExtra("pos", pos).putExtra("space", space).putExtra("activity", "fankui"), 1);

            }
        });
    }

    @SuppressLint("HandlerLeak")
    public class GVAdapter extends BaseAdapter {
        private Context context;
        private List<SCFBean.ImgBean> iBeans;
        private ViewHolder view;

        public GVAdapter(Context context, List<SCFBean.ImgBean> iBeans) {
            this.context = context;
            this.iBeans = iBeans;
        }

        @Override
        public int getCount() {
            return null == iBeans ? 0 : iBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return iBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_published_grida, parent, false);
                view = new ViewHolder(convertView);
                LayoutParams lParams = (LayoutParams) view.img.getLayoutParams();
                lParams.width = ((width / 3) * 2) / 3 - 4;
                lParams.height = ((width / 3) * 2) / 3 - 4;
                view.img.setLayoutParams(lParams);
                convertView.setTag(view);
            } else {
                view = (ViewHolder) convertView.getTag();
            }

            if (img_bean.get(position).getType() == 3) {
                view.img.setImageResource(R.drawable.add_photo);
            } else if (img_bean.get(position).getType() == 2) {
                try {
                    view.img.setImageBitmap(revitionImageSize(img_bean.get(position).getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (img_bean.get(position).getType() == 1) {

                try {

                    view.img.setImageBitmap(revitionImageSize(img_bean.get(position).getPath()));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return convertView;
        }

        private class ViewHolder {
            private ImageView img;

            public ViewHolder(View v) {
                this.img = (ImageView) v.findViewById(R.id.item_grida_image);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                System.out.println("回调:  " + img_bean.size());
                if (img_bean.size() != 9 && img_bean.get(img_bean.size() - 1).getType() != 3) {
                    SCFBean.ImgBean imgBean = new SCFBean.ImgBean();
                    imgBean.setType(3);
                    img_bean.add(imgBean);
                }
                gAdapter.notifyDataSetChanged();
                break;
            case 2:
                gAdapter.notifyDataSetChanged();
                break;

        }
    }

    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    public void compress(String srcPath, int pos) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        float hh = dm.heightPixels;
        float ww = dm.widthPixels;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, opts);
        opts.inJustDecodeBounds = false;
        int w = opts.outWidth;
        int h = opts.outHeight;
        int size = 0;
        if (w <= ww && h <= hh) {
            size = 1;
        } else {
            double scale = w >= h ? w / ww : h / hh;
            double log = Math.log(scale) / Math.log(2);
            double logCeil = Math.ceil(log);
            size = (int) Math.pow(2, logCeil);
        }
        opts.inSampleSize = size;
        bitmap = BitmapFactory.decodeFile(srcPath, opts);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        System.out.println(baos.toByteArray().length);
        while (baos.toByteArray().length > 45 * 1024) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 7;
            System.out.println(baos.toByteArray().length);
        }
        try {
            baos.writeTo(new FileOutputStream(img_bean.get(pos).getNew_path() + "new" + ".jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_right:
                if (TextUtils.isEmpty(mEt_content.getText().toString().trim())) {
                    ToastUtils.show(getApplicationContext(), "反馈内容不能为空哦...");
                    return;
                }
                if ((img_bean.size() - 1) == 0) {
                    Toast.makeText(SendFeedBackActivity.this, "反馈图片不能为空...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("0".equals(shiming_type)) {
                    final Dialog dialog = new Dialog(SendFeedBackActivity.this, R.style.dialog);
                    dialog.setContentView(R.layout.dialog_real_name_confirm);
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER | Gravity.CENTER);
                    TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
                    TextView tv_confirm = (TextView) dialog.findViewById(R.id.tv_confirm);
                    tv_cancel.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    tv_confirm.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(SendFeedBackActivity.this, RealNameConfirmActivity.class);
                            startActivity(intent);
                        }
                    });
                    dialog.show();
                    return;
                }
                uploadingPictures();
                break;

        }

    }

    private void uploadingPictures() {
        CustomDialogUtils.startCustomProgressDialog(SendFeedBackActivity.this, "正在发送反馈...");
        final Map<String, String> params = new HashMap<String, String>();
        params.put("publish_user_id", EncryptUtils.addSign(Long.valueOf(mPublish_id), "u"));
        params.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        params.put("content", mEt_content.getText().toString());
        params.put("enjoy_id", zishang_id);
        params.put("imgNumber", String.valueOf(img_bean.size() - 1));
        params.put("type", "1");
        new Thread(new Runnable() {
            public void run() {
                NewCompress(img_bean, params);
            }
        }).start();
    }

    protected void NewCompress(List<SCFBean.ImgBean> img_bean, Map<String, String> params) {
        for (int i = 0; i < img_bean.size(); i++) {

            if (img_bean.get(i).getType() != 3) {

                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                float hh = dm.heightPixels;
                float ww = dm.widthPixels;
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                Bitmap bitmap = BitmapFactory.decodeFile(img_bean.get(i).getPath(), opts);
                opts.inJustDecodeBounds = false;
                int w = opts.outWidth;
                int h = opts.outHeight;
                int size = 0;
                if (w <= ww && h <= hh) {
                    size = 1;
                } else {
                    double scale = w >= h ? w / ww : h / hh;
                    double log = Math.log(scale) / Math.log(2);
                    double logCeil = Math.ceil(log);
                    size = (int) Math.pow(2, logCeil);
                }
                opts.inSampleSize = size;
                bitmap = BitmapFactory.decodeFile(img_bean.get(i).getPath(), opts);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int quality = 100;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                System.out.println(baos.toByteArray().length);
                while (baos.toByteArray().length > 45 * 1024) {
                    baos.reset();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                    if (quality > 30) {
                        quality -= 20;
                    } else {
                        break;
                    }
                }

                try {
                    baos.writeTo(new FileOutputStream(img_bean.get(i).getNew_path() + "new" + ".jpg"));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        baos.flush();
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        List<SCFBean.ImgBean> _img = new ArrayList<SCFBean.ImgBean>();

        for (int j = 0; j < img_bean.size(); j++) {

            if (img_bean.get(j).getType() != 3) {
                _img.add(img_bean.get(j));
            }
        }

        String[] path = new String[_img.size()];
        for (int i = 0; i < _img.size(); i++) {
            path[i] = _img.get(i).getNew_path() + "new" + ".jpg";
            System.out.println(path[i]);
        }

        doUploadTest(params, path);
    }

    private void doUploadTest(Map<String, String> params, String[] path) {
        RequestQueue mSingleQueue = Volley.newRequestQueue(getApplicationContext());
        List<File> f = new ArrayList<File>();
        for (int i = 0; i < path.length; i++) {
            File files = new File(path[i]);
            f.add(files);
        }

        MultipartRequest request = new MultipartRequest(HttpConfig.IMAGEHOST + "/app/uploadTicking",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        System.out.println("反馈结果" + response);
                        BasicParameterBean bean = GsonUtil.getInstance().fromJson(response, BasicParameterBean.class);
                        CustomDialogUtils.stopCustomProgressDialog(getApplicationContext());
                        Toast.makeText(SendFeedBackActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        Intent intent = getIntent();
                        SendFeedBackActivity.this.setResult(1, intent);
                        SendFeedBackActivity.this.finish();

                    }
                }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogUtils.stopCustomProgressDialog(SendFeedBackActivity.this);
                Toast.makeText(getApplicationContext(), "发表失败,请稍后重试!", Toast.LENGTH_SHORT).show();
                CustomDialogUtils.stopCustomProgressDialog(getApplicationContext());
            }
        }, "tickingImg", f, params);

        mSingleQueue.add(request);
    }
}
