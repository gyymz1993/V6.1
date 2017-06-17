package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.common.activtiy.SendShiShiRuleActivity;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.newview.MyGridView;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
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

/**
 * 身份认证
 */
public class IdentityAuthenticationActivity extends Activity implements OnClickListener {

    private String[] label = {"资深摄影师", "美食专家", "豪车品鉴师", "游艇名家", "独立设计师", "飞行达人", "时装模特", "家居潮人", "旅途王者"};
    private LabelAdapter la;
    private GVAdapter gv;
    private MyGridView gv_label;
    private MyGridView gv_pic;
    private LinearLayout ll_back;
    private LinearLayout ll_skip;
    private EditText et;
    private int space = 99;
    private TextView tv_sure;
    public static String p_name = "ly";
    private int skip = 0;
    public static List<SCFBean.ImgBean> img_bean = new ArrayList<>();
    private String mShefen;
    private int width;
    RelativeLayout re_agree;
    ImageView iv_agree;
    ImageView iv_disagree;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.identity_authentication_activity);
        this.findViewById();
    }

    private void findViewById() {
        mShefen = getIntent().getStringExtra("shefen");
        this.et = (EditText) super.findViewById(R.id.et);
        this.gv_pic = (MyGridView) super.findViewById(R.id.gv_pic);
        this.tv_sure = (TextView) super.findViewById(R.id.tv_sure);
        this.gv_label = (MyGridView) super.findViewById(R.id.gv_label);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.ll_skip = (LinearLayout) super.findViewById(R.id.ll_skip);
        this.la = new LabelAdapter(IdentityAuthenticationActivity.this, label);
        re_agree = (RelativeLayout) findViewById(R.id.re_agree);
        iv_agree = (ImageView) findViewById(R.id.iv_agree);
        iv_disagree = (ImageView) findViewById(R.id.iv_disagree);

        this.gv_label.setAdapter(la);
        img_bean = new ArrayList<>();
        SCFBean.ImgBean imgBean = new SCFBean.ImgBean();
        imgBean.setType(3);
        img_bean.add(imgBean);

        this.gv = new GVAdapter(IdentityAuthenticationActivity.this, img_bean);

        this.gv_pic.setAdapter(gv);

        this.tv_sure.setOnClickListener(this);
        this.ll_back.setOnClickListener(this);
        this.ll_skip.setOnClickListener(this);

        this.gv_label.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                space = position;
                la.notifyDataSetChanged();
            }
        });

        this.gv_pic.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
                if (isOpen == true) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
                }

                boolean space = false;

                if (img_bean.get(position).getType() != 3) {
                    space = true;
                }

                startActivityForResult(new Intent(IdentityAuthenticationActivity.this, PicturesChooseActivity.class)
                        .putExtra("pos", position).putExtra("space", space).putExtra("activity", "ia"), 1);
            }
        });

        if ("setting".equals(mShefen)) {// 设置密码过来的
            ll_skip.setVisibility(View.VISIBLE);
            tv_sure.setText("下一步");
        }
        if ("send_eye".equals(mShefen)) {// 发送视界过来的
            ll_skip.setVisibility(View.GONE);
            tv_sure.setText("完成");
        }
        if ("set_centry".equals(mShefen)) {// 设置中心过来的
            ll_skip.setVisibility(View.GONE);
            tv_sure.setText("完成");
        }
        DisplayMetrics dm = new DisplayMetrics();
        IdentityAuthenticationActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        LayoutParams lParams = (LayoutParams) gv_pic.getLayoutParams();
        lParams.width = width / 3 * 2;
        gv_pic.setLayoutParams(lParams);

    }

    @Override
    protected void onResume() {
        iv_agree.setVisibility(View.VISIBLE);
        iv_disagree.setVisibility(View.GONE);
        re_agree.setOnClickListener(this);
        iv_agree.setOnClickListener(this);
        iv_disagree.setOnClickListener(this);
        super.onResume();
    }

    private class LabelAdapter extends BaseAdapter {

        private String[] label;
        private Context context;
        private ViewHolder view;

        public LabelAdapter(Context context, String[] label) {
            this.context = context;
            this.label = label;
        }

        @Override
        public int getCount() {
            return null == label ? 0 : label.length;
        }

        @Override
        public Object getItem(int position) {
            return label[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(R.layout.ia_label_item, parent, false);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {

                view = (ViewHolder) convertView.getTag();

            }

            if (space == 99) {
                view.tv_label.setTextColor(Color.parseColor("#757575"));
                view.tv_label.setBackgroundColor(Color.parseColor("#ECECEC"));
            } else if (space == position) {
                view.tv_label.setTextColor(Color.parseColor("#FFFFFF"));
                view.tv_label.setBackgroundColor(Color.parseColor("#000000"));
            } else {
                view.tv_label.setTextColor(Color.parseColor("#757575"));
                view.tv_label.setBackgroundColor(Color.parseColor("#ECECEC"));
            }

            this.view.tv_label.setText(label[position]);

            return convertView;
        }

        private class ViewHolder {

            private TextView tv_label;

            public ViewHolder(View v) {
                this.tv_label = (TextView) v.findViewById(R.id.tv_label);
            }
        }
    }

    private class GVAdapter extends BaseAdapter {

        private Context context;
        private ViewHolder view;
        private List<SCFBean.ImgBean> iBeans;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                if (img_bean.size() != 9 && img_bean.get(img_bean.size() - 1).getType() != 3) {
                    SCFBean.ImgBean imgBean = new SCFBean.ImgBean();
                    imgBean.setType(3);
                    img_bean.add(imgBean);
                }

                gv.notifyDataSetChanged();

                break;

            case 2:

                gv.notifyDataSetChanged();

                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_skip:
                skip = 1;
                intent = new Intent(IdentityAuthenticationActivity.this, NoteLoginActivity.class);
                intent.putExtra("personal", "shenfen_no");
                startActivity(intent);
                finish();
                break;
            case R.id.tv_sure:
                if (space == 99) {
                    Toast.makeText(IdentityAuthenticationActivity.this, "请选择一个身份标签...", Toast.LENGTH_SHORT).show();
                } else {
                    List<SCFBean.ImgBean> img_space = new ArrayList<>();
                    img_space = img_bean;
                    for (int i = 0; i < img_space.size(); i++) {
                        if (img_space.get(i).getType() == 3) {
                            img_space.remove(i);
                        }
                    }

                    if (img_space.size() < 1) {
                        Toast.makeText(IdentityAuthenticationActivity.this, "请至少选择一张照片...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (iv_agree.getVisibility() == View.GONE) {
                        ToastUtils.show(getApplicationContext(), "请您先阅读孜孜管家身份认证条例并勾选同意条例");
                        return;
                    }
                    upDataPictures();
                }

                break;
            case R.id.re_agree:
                intent = new Intent(getApplicationContext(), SendShiShiRuleActivity.class);
                intent.putExtra("name", "1");
                startActivity(intent);
                break;
            case R.id.iv_agree:
                iv_agree.setVisibility(View.GONE);
                iv_disagree.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_disagree:
                iv_agree.setVisibility(View.VISIBLE);
                iv_disagree.setVisibility(View.GONE);
                break;
        }
    }

    private void upDataPictures() {
        CustomDialogUtils.startCustomProgressDialog(IdentityAuthenticationActivity.this, "正在验证身份...");
        final Map<String, String> map = new HashMap<>();
        map.put("identity_type", label[space]);
        if (TextUtils.isEmpty(et.getText().toString())) {
            map.put("certificate_numer", "");
        } else {
            map.put("certificate_numer", et.getText().toString());
        }
        map.put("type", "1");
        if ("setting".equals(mShefen)) {// 设置密码过来
            //map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getRegisterInfo().getUser_id()), "u"));
        } else if ("send_eye".equals(mShefen)) {// 发送视界过来的
            map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        } else if ("set_centry".equals(mShefen)) {// 设置中心过来
            map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        }
        new Thread(new Runnable() {
            public void run() {
                NewCompress(img_bean, map);
            }
        }).start();
    }

    protected void NewCompress(List<SCFBean.ImgBean> img_bean, Map<String, String> map) {
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

        doUploadTest(map, path);
    }

    private void doUploadTest(Map<String, String> params, String[] path) {

        RequestQueue mSingleQueue = Volley.newRequestQueue(getApplicationContext());

        List<File> f = new ArrayList<File>();
        for (int i = 0; i < path.length; i++) {
            File files = new File(path[i]);
            f.add(files);
        }

        MultipartRequest request = new MultipartRequest(HttpConfig.IMAGEHOST + "/app/uploadIdentityApprove",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        System.out.println("结果" + response);
                        CustomDialogUtils.stopCustomProgressDialog(IdentityAuthenticationActivity.this);
                        Toast.makeText(IdentityAuthenticationActivity.this, "身份认证成功...", Toast.LENGTH_SHORT).show();
                        PreferencesUtils.putString(getApplicationContext(), "label", label[space]);
                        skip = 1;

                        if ("setting".equals(mShefen)) {// 设置密码过来的 成功之后到登录界面
                            Intent intent = new Intent(IdentityAuthenticationActivity.this, NoteLoginActivity.class);
                            intent.putExtra("personal", "shefen");
                            startActivity(intent);
                            finish();
                        }
                        if ("send_eye".equals(mShefen)) {// 发送时视 直接完成
                            finish();
                        }
                        if ("set_centry".equals(mShefen)) {// 设置中心过来 直接完成
                            finish();
                        }

                    }
                }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogUtils.stopCustomProgressDialog(IdentityAuthenticationActivity.this);
                Toast.makeText(getApplicationContext(), "发表失败,请稍后重试!", Toast.LENGTH_SHORT).show();
            }
        }, "voucherImg", f, params);

        mSingleQueue.add(request);
    }

}
