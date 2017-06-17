package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.bean.ImageItem;
import com.lsjr.zizisteward.common.activtiy.SendShiShiRuleActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.IdentityAuthenticationActivity;
import com.lsjr.zizisteward.ly.activity.SCFBean;
import com.lsjr.zizisteward.newview.MyGridView;
import com.lsjr.zizisteward.utils.Bimp;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.MultipartRequest;
import com.lsjr.zizisteward.utils.PreferencesUtils;
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

public class SendShiJieActivity extends Activity implements OnClickListener {
    private LinearLayout ll_send;
    private LinearLayout ll_back;
    private EditText et_content;
    private TextView tv_number;
    private RelativeLayout layout_title_back, re_agree;
    private TextView mTv_shenfen;
    private RelativeLayout mRe_go_yanzheng;
    private RelativeLayout mRe_shenfen;
    private EditText mEt_input;

    private GVAdapter gAdapter;
    public static String fileName;
    public static List<SCFBean.ImgBean> img_bean = new ArrayList<SCFBean.ImgBean>();
    private int max_num = 9;
    public static String p_name = "ly";
    public static String path;// 图片地址
    private CharSequence temp;
    private PopWindowCatFriend mPopw;
    private String num;
    private MyGridView mGv_pic;

    private String shefen_type;// 身份状态
    private String shiming_type;// 实名状态
    private String label_shefen;// 身份认证之后保存的身份字段

    private int width;
    private ImageView iv_agree, iv_disagree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_send_shijie);
        App.getInstance().addActivity(this);
        mGv_pic = (MyGridView) findViewById(R.id.gv_pic);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_send = (LinearLayout) findViewById(R.id.ll_send);
        tv_number = (TextView) findViewById(R.id.tv_number);
        et_content = (EditText) findViewById(R.id.et_content);
        layout_title_back = (RelativeLayout) findViewById(R.id.layout_title_back);
        mTv_shenfen = (TextView) findViewById(R.id.tv_shenfen);// 美食专家身份
        mRe_go_yanzheng = (RelativeLayout) findViewById(R.id.re_go_yanzheng);// 前去认证身份
        mRe_shenfen = (RelativeLayout) findViewById(R.id.re_shenfen);// 身份信息
        mEt_input = (EditText) findViewById(R.id.et_input);// 自定义标签
        re_agree = (RelativeLayout) findViewById(R.id.re_agree);// 时视发布规则
        iv_agree = (ImageView) findViewById(R.id.iv_agree);// 同意
        iv_disagree = (ImageView) findViewById(R.id.iv_disagree);// 不同意

        findViewById();
        initListener();

        DisplayMetrics dm = new DisplayMetrics();
        SendShiJieActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) mGv_pic.getLayoutParams();
        lParams.width = width / 3 * 2;
        mGv_pic.setLayoutParams(lParams);
    }

    @Override
    protected void onResume() {
        iv_agree.setVisibility(View.VISIBLE);
        iv_disagree.setVisibility(View.GONE);
        label_shefen = PreferencesUtils.getString(SendShiJieActivity.this, "label");
        getSheFenTypeData();
        super.onResume();
    }

    private void getSheFenTypeData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "88");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("状态" + result);
                StateBean bean = GsonUtil.getInstance().fromJson(result, StateBean.class);
                shiming_type = bean.getIsIdNumberVerified();
                shefen_type = bean.getIsPassCertificate();
                if ("0".equals(shiming_type) || "0".equals(shefen_type)) {
                    mRe_shenfen.setVisibility(View.GONE);
                    mRe_go_yanzheng.setVisibility(View.VISIBLE);
                } else {
                    mRe_shenfen.setVisibility(View.VISIBLE);
                    mRe_go_yanzheng.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(App.getUserInfo().getIdentity_type())) {
                        mTv_shenfen.setText(App.getUserInfo().getIdentity_type());
                    } else {
                        mTv_shenfen.setText(label_shefen);
                    }

                }

            }
        });
    }

    /*
     * 控件监听
     *
     */
    private void initListener() {
        mRe_go_yanzheng.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        ll_send.setOnClickListener(this);
        re_agree.setOnClickListener(this);
        iv_agree.setOnClickListener(this);
        iv_disagree.setOnClickListener(this);
    }

    private void findViewById() {
        img_bean = new ArrayList<>();
        mPopw = new PopWindowCatFriend(SendShiJieActivity.this, "发送界面");
        SCFBean.ImgBean imgBean = new SCFBean.ImgBean();
        imgBean.setType(3);
        img_bean.add(imgBean);

        this.gAdapter = new GVAdapter(SendShiJieActivity.this, img_bean);
        this.mGv_pic.setAdapter(gAdapter);

        this.et_content.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_number.setText(temp.length() + "/240字");
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

                startActivityForResult(new Intent(SendShiJieActivity.this, SelectorPicturesActivity.class)
                        .putExtra("pos", pos).putExtra("space", space).putExtra("activity", "shijie"), 1);

            }
        });
    }

    @Override
    protected void onDestroy() {
        Bimp.tempSelectBitmap = new ArrayList<ImageItem>();
        super.onDestroy();
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
                LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) view.img.getLayoutParams();
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

    private void doUploadTest(Map<String, String> params, String[] path) {
        RequestQueue mSingleQueue = Volley.newRequestQueue(getApplicationContext());
        List<File> f = new ArrayList<File>();
        for (int i = 0; i < path.length; i++) {
            File files = new File(path[i]);
            f.add(files);
        }

        MultipartRequest request = new MultipartRequest(HttpConfig.IMAGEHOST + "/app/uploadSight",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        System.out.println("发送视界结果" + response);
                        BasicParameterBean bean = GsonUtil.getInstance().fromJson(response, BasicParameterBean.class);
                        CustomDialogUtils.stopCustomProgressDialog(getApplicationContext());
                        Toast.makeText(SendShiJieActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        Intent intent = getIntent();
                        SendShiJieActivity.this.setResult(1, intent);
                        SendShiJieActivity.this.finish();
                    }
                }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "发表失败,请稍后重试!", Toast.LENGTH_SHORT).show();
                CustomDialogUtils.stopCustomProgressDialog(getApplicationContext());
            }
        }, "shareImg", f, params);

        mSingleQueue.add(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.ll_send:// 发送视界时

                if (et_content.getText().toString().equals("")) {
                    Toast.makeText(SendShiJieActivity.this, "说点什么吧....", Toast.LENGTH_SHORT).show();
                    return;

                }

                if ((img_bean.size() - 1) == 0) {
                    Toast.makeText(SendShiJieActivity.this, "时视图片不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("0".equals(shiming_type)) {
                    ToastUtils.show(getApplicationContext(), "您还未实名认证,请先实名认证");
                    return;
                }

                if ("0".equals(shefen_type)) {
                    ToastUtils.show(getApplicationContext(), "您还未身份认证,请先身份认证");
                    return;
                }

                if ("1".equals(shefen_type)) {
                    ToastUtils.show(getApplicationContext(), "您的身份认证正在审核中,暂时不能发送时视");
                    return;
                }

                if (iv_agree.getVisibility() == View.GONE) {
                    ToastUtils.show(getApplicationContext(), "请您先阅读发布规则并勾选接受规则");
                    return;
                }

                if ("2".equals(shefen_type)) {
                    upDataPics();
                }

                break;
            case R.id.re_go_yanzheng:
                if ("0".equals(shiming_type)) {// 未实名认证
                    Intent intent = new Intent(getApplicationContext(), RealNameConfirmActivity.class);
                    startActivityForResult(intent, 111);
                } else {

                    if ("0".equals(shefen_type)) {// 未身份认证
                        Intent intent = new Intent(getApplicationContext(), IdentityAuthenticationActivity.class);
                        intent.putExtra("shefen", "send_eye");
                        startActivityForResult(intent, 333);
                    }

                }

                break;
            case R.id.iv_agree:
                iv_agree.setVisibility(View.GONE);
                iv_disagree.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_disagree:
                iv_agree.setVisibility(View.VISIBLE);
                iv_disagree.setVisibility(View.GONE);
                break;
            case R.id.re_agree:
                Intent intent = new Intent(getApplicationContext(), SendShiShiRuleActivity.class);
                intent.putExtra("name", "2");
                startActivity(intent);
                break;
        }
    }

    private void upDataPics() {
        CustomDialogUtils.startCustomProgressDialog(SendShiJieActivity.this, "正在发送时视...");
        final Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        params.put("content", et_content.getText().toString());
        if (!TextUtils.isEmpty(App.getUserInfo().getIdentity_type())) {
            params.put("sightType", App.getUserInfo().getIdentity_type());
        } else {
            params.put("sightType", label_shefen);
        }
        String customTag = mEt_input.getText().toString();
        if (TextUtils.isEmpty(customTag)) {
            params.put("customTag", "");
        } else {
            params.put("customTag", customTag);
        }
        if (img_bean.size() == 9) {
            params.put("imgNumber", String.valueOf(img_bean.size()));
        } else {
            params.put("imgNumber", String.valueOf(img_bean.size() - 1));
        }
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

}
