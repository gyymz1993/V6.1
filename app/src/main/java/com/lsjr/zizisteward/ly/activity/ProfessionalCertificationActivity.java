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

public class ProfessionalCertificationActivity extends Activity implements
        OnClickListener {

    private MyGridView gv_pic;
    private TextView tv_name;
    private TextView tv_submit;
    private LinearLayout ll_back;
    private LinearLayout ll_select;

    private int max_num = 9;
    public static String p_name = "ly";
    private GVAdapter gAdapter;
    public static String fileName;
    private String cardid;
    private String name;
    public static List<SCFBean.ImgBean> img_bean = new ArrayList<>();
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.professional_certification_activity);
        this.findViewById();
    }

    private void findViewById() {

        this.gv_pic = (MyGridView) super.findViewById(R.id.gv_pic);
        this.tv_name = (TextView) super.findViewById(R.id.tv_name);
        this.tv_submit = (TextView) super.findViewById(R.id.tv_submit);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.ll_select = (LinearLayout) super.findViewById(R.id.ll_select);

        this.ll_back.setOnClickListener(this);
        this.ll_select.setOnClickListener(this);
        this.tv_submit.setOnClickListener(this);

        DisplayMetrics dm = new DisplayMetrics();
        ProfessionalCertificationActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;

        LayoutParams lParams = (LayoutParams) gv_pic.getLayoutParams();

        lParams.width = width - 10;

        gv_pic.setLayoutParams(lParams);

        img_bean = new ArrayList<>();

        SCFBean.ImgBean imgBean = new SCFBean.ImgBean();
        imgBean.setType(3);
        img_bean.add(imgBean);

        this.gAdapter = new GVAdapter(ProfessionalCertificationActivity.this, img_bean);
        this.gv_pic.setAdapter(gAdapter);

        this.gv_pic.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                boolean space = false;

                if (img_bean.get(position).getType() != 3) {
                    space = true;
                }

                startActivityForResult(new Intent(
                                ProfessionalCertificationActivity.this,
                                PicturesChooseActivity.class).putExtra("pos", position)
                                .putExtra("space", space).putExtra("activity", "pca"),
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

                convertView = LayoutInflater.from(context).inflate(R.layout.item_published_grida, parent, false);

                view = new ViewHolder(convertView);

                LayoutParams lParams = (LayoutParams) view.img.getLayoutParams();

                lParams.width = (width - 10) / 4 - 18;

                lParams.height = (width - 10) / 4 - 18;

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
                if (img_bean.size() != 8
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

            case 3:

                cardid = data.getStringExtra("cardid");
                name = data.getStringExtra("name");

                tv_name.setText(null == name ? "" : name);

                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.tv_submit:

                String name = tv_name.getText().toString();

                if (TextUtils.isEmpty(name)) {

                    Toast.makeText(ProfessionalCertificationActivity.this, "请选择要认证的企业", Toast.LENGTH_SHORT).show();

                } else {

                    if (img_bean.size() < 2) {
                        Toast.makeText(ProfessionalCertificationActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
                    } else {
                        UpDataPicture();
                    }
                }

                break;

            case R.id.ll_select:

                startActivityForResult(new Intent(ProfessionalCertificationActivity.this, SelectEnterpriseActivity.class), 2);

                break;
        }
    }

    private void UpDataPicture() {
        CustomDialogUtils.startCustomProgressDialog(ProfessionalCertificationActivity.this, "请稍候");
        final Map<String, String> map = new HashMap<String, String>();
        map.put("cardid", cardid);
        map.put("type", "1");
        new Thread(new Runnable() {

            @Override
            public void run() {
                Newcompress(img_bean, map);
            }
        }).start();
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
                while (baos.toByteArray().length > 40 * 1024) {
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
            path[i] = _img.get(i).getNew_path() + "new" + ".jpg";
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

        MultipartRequest request = new MultipartRequest(HttpConfig.IMAGEHOST + "/app/uploadApprove",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        CustomDialogUtils.stopCustomProgressDialog(ProfessionalCertificationActivity.this);
                        Toast.makeText(ProfessionalCertificationActivity.this, "申请企业认证成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogUtils.stopCustomProgressDialog(ProfessionalCertificationActivity.this);
                Toast.makeText(getApplicationContext(), "网络异常,请稍后重试!",
                        Toast.LENGTH_SHORT).show();
                CustomDialogUtils.stopCustomProgressDialog(getApplicationContext());
            }
        }, "cardImg", f, params);

        mSingleQueue.add(request);

    }
}
