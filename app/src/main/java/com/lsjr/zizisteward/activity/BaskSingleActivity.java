package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.bean.ImageItem;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.SCFBean;
import com.lsjr.zizisteward.utils.Bimp;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.MultipartRequest;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("SimpleDateFormat")
public class BaskSingleActivity extends Activity implements OnClickListener {
    /* 发送按钮 */
    private LinearLayout ll_send;
    /* 返回按钮 */
    private LinearLayout ll_back;
    /* 文字编辑 */
    private EditText et_content;
    /* 文字数量 */
    private TextView tv_number;
    /* 图片位置 */
    private GridView gv_pic;
    private String mBrand_name;
    private String mGnum;

    private GVAdapter gAdapter;
    public static String fileName;
    public static List<SCFBean.ImgBean> img_bean = new ArrayList<SCFBean.ImgBean>();
    public static String p_name = "ly";
    public static Uri uri;
    public static String path;// 图片地址
    private CharSequence temp;
    private PopWindowCatFriend mPopw;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bask_single_activity);
        mBrand_name = getIntent().getStringExtra("name");
        mGnum = getIntent().getStringExtra("gid");
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_send = (LinearLayout) findViewById(R.id.ll_send);
        et_content = (EditText) findViewById(R.id.et_content);
        tv_number = (TextView) findViewById(R.id.tv_number);
        gv_pic = (GridView) findViewById(R.id.gv_pic);
        TextView tv_time = (TextView) findViewById(R.id.tv_time);
        TextView tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(mBrand_name);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new java.util.Date());
        tv_time.setText(date);
        findViewById();
        DisplayMetrics dm = new DisplayMetrics();
        BaskSingleActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        LayoutParams lParams = (LayoutParams) gv_pic.getLayoutParams();
        lParams.width = width / 3 * 2;
        gv_pic.setLayoutParams(lParams);
    }

    private void findViewById() {
        ll_back.setOnClickListener(this);
        ll_send.setOnClickListener(this);
        img_bean = new ArrayList<>();
        mPopw = new PopWindowCatFriend(getApplicationContext(), "发送界面");
        SCFBean.ImgBean imgBean = new SCFBean.ImgBean();
        imgBean.setType(3);
        img_bean.add(imgBean);
        gAdapter = new GVAdapter(getApplicationContext(), img_bean);
        gv_pic.setAdapter(gAdapter);
        et_content.addTextChangedListener(new TextWatcher() {

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

        gv_pic.setOnItemClickListener(new OnItemClickListener() {

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

                startActivityForResult(new Intent(getApplicationContext(), SelectorPicturesActivity.class)
                        .putExtra("pos", pos).putExtra("space", space).putExtra("activity", "shaidan"), 1);

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

    private void doUploadTest(Map<String, String> params, String[] path) {
        RequestQueue mSingleQueue = Volley.newRequestQueue(getApplicationContext());
        List<File> f = new ArrayList<File>();
        for (int i = 0; i < path.length; i++) {
            File files = new File(path[i]);
            f.add(files);
        }

        MultipartRequest request = new MultipartRequest(HttpConfig.IMAGEHOST + "/app/uploadSharePhoto",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        System.out.println("发送晒单结果" + response);
                        BasicParameterBean bean = GsonUtil.getInstance().fromJson(response, BasicParameterBean.class);
                        CustomDialogUtils.stopCustomProgressDialog(BaskSingleActivity.this);
                        Toast.makeText(getApplicationContext(), bean.getMsg(), Toast.LENGTH_SHORT).show();
                        Intent intent = getIntent();
                        BaskSingleActivity.this.setResult(1, intent);
                        BaskSingleActivity.this.finish();
                    }
                }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ll_send.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "发表失败,请稍后重试!", Toast.LENGTH_SHORT).show();
                CustomDialogUtils.stopCustomProgressDialog(BaskSingleActivity.this);
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
            case R.id.ll_send:
                if (TextUtils.isEmpty(et_content.getText().toString().trim())) {
                    Toast.makeText(BaskSingleActivity.this, "说点什么吧....", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((img_bean.size() - 1) == 0) {
                    Toast.makeText(BaskSingleActivity.this, "晒单图片不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadingPictures();
                break;
        }
    }

    private void uploadingPictures() {
        CustomDialogUtils.startCustomProgressDialog(BaskSingleActivity.this, "正在发送晒单...");
        final Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        params.put("content", et_content.getText().toString());
        params.put("gnum", mGnum);
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
