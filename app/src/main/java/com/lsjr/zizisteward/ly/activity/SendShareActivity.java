package com.lsjr.zizisteward.ly.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.ImageItem;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.utils.Bimp;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.MultipartRequest;

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

public class SendShareActivity extends Activity implements OnClickListener {

    private LinearLayout ll_cancel;
    private LinearLayout ll_send;
    private EditText et_title;
    private EditText et_content;
    private GridView gv;
    private String Groupid;
    private int max_num = 9;
    public static String p_name = "ly";
    private GVAdapter gAdapter;
    public static String fileName;
    public static List<SCFBean.ImgBean> img_bean = new ArrayList<>();
    private Map<String, String> params;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.send_share_activity);
        this.findViewById();
    }

    private void findViewById() {

        this.gv = (GridView) super.findViewById(R.id.gv);
        this.et_title = (EditText) super.findViewById(R.id.et_title);
        this.ll_send = (LinearLayout) super.findViewById(R.id.ll_send);
        this.et_content = (EditText) super.findViewById(R.id.et_content);
        this.ll_cancel = (LinearLayout) super.findViewById(R.id.ll_cancel);

        this.Groupid = getIntent().getStringExtra("Groupid");

        this.ll_send.setOnClickListener(this);
        this.ll_cancel.setOnClickListener(this);

        DisplayMetrics dm = new DisplayMetrics();
        SendShareActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;

        LayoutParams lParams = (LayoutParams) gv.getLayoutParams();

        lParams.width = width / 3 * 2;

        gv.setLayoutParams(lParams);


        img_bean = new ArrayList<>();

        SCFBean.ImgBean imgBean = new SCFBean.ImgBean();
        imgBean.setType(3);
        img_bean.add(imgBean);

        this.gAdapter = new GVAdapter(SendShareActivity.this, img_bean);
        this.gv.setAdapter(gAdapter);

        this.gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                boolean space = false;

                if (img_bean.get(position).getType() != 3) {
                    space = true;
                }

                startActivityForResult(new Intent(SendShareActivity.this,
                                PicturesChooseActivity.class).putExtra("pos", position)
                                .putExtra("space", space).putExtra("activity", "ssa"),
                        1);
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
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.item_published_grida, parent, false);
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
                    view.img.setImageBitmap(revitionImageSize(img_bean.get(
                            position).getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (img_bean.get(position).getType() == 1) {

                try {

                    view.img.setImageBitmap(revitionImageSize(img_bean.get(
                            position).getPath()));

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
    protected void onDestroy() {
        Bimp.tempSelectBitmap = new ArrayList<ImageItem>();
        super.onDestroy();
    }

    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1000)
                    && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {

            case 1:
                if (img_bean.size() != 9
                        && img_bean.get(img_bean.size() - 1).getType() != 3) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_cancel:
                finish();
                break;

            case R.id.ll_send:

                if (TextUtils.isEmpty(et_title.getText().toString())) {
                    Toast.makeText(SendShareActivity.this, "标题不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(et_content.getText().toString())) {
                    Toast.makeText(SendShareActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                } else if (img_bean.size() < 2) {
                    Toast.makeText(SendShareActivity.this, "图片不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    toUploadFile();
                }

                break;
        }
    }

    private void toUploadFile() {
        params = new HashMap<String, String>();
        params.put("OPT", "314");
        params.put("share_type", "1");
        params.put("fid", Groupid);
        params.put("flock_title", et_title.getText().toString());
        params.put("share_content", et_content.getText().toString());
        params.put(
                "user_id",
                Fragment_ChatList.addSign(
                        Long.valueOf(App.getUserInfo().getId()), "u"));
        params.put("type", "1");

        if (null != img_bean && img_bean.size() > 1) {
            CustomDialogUtils.startCustomProgressDialog(SendShareActivity.this, "正在发送朋友圈");
            new Thread(new Runnable() {

                @Override
                public void run() {
                    Newcompress(img_bean, params);
                }
            }).start();

        } else {
            Toast.makeText(SendShareActivity.this, "请选择图片...", Toast.LENGTH_SHORT).show();
        }
    }

    public void Newcompress(List<SCFBean.ImgBean> img_bean, Map<String, String> params) {

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
                int quality = 80;
                bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);
                System.out.println(baos.toByteArray().length);
                while (baos.toByteArray().length > 45 * 1024) {
                    baos.reset();
                    bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);
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
            path[i] = img_bean.get(i).getNew_path() + "new" + ".jpg";
//			path[i] = img_bean.get(i).getPath();
        }

        doUploadTest(params, path);
    }

    private void doUploadTest(Map<String, String> params, String[] path) {
        RequestQueue mSingleQueue = Volley
                .newRequestQueue(getApplicationContext());

        List<File> f = new ArrayList<File>();
        for (int i = 0; i < path.length; i++) {
            File files = new File(path[i]);
            f.add(files);
        }

        MultipartRequest request = new MultipartRequest(HttpConfig.IMAGEHOST
                + "/app/AppAddGroupShare",

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        CustomDialogUtils
                                .stopCustomProgressDialog(SendShareActivity.this);
                        Toast.makeText(SendShareActivity.this, "群分享发送成功...", Toast.LENGTH_SHORT).show();
                        setResult(1);
                        finish();
                    }
                }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogUtils
                        .stopCustomProgressDialog(SendShareActivity.this);
                Toast.makeText(getApplicationContext(),
                        "发表失败,请稍后重试!",
                        Toast.LENGTH_SHORT).show();
            }
        }, "photo", f, params);

        mSingleQueue.add(request);
    }
}
