package com.lsjr.zizisteward.ly.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
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
import com.lsjr.zizisteward.activity.PopWindowCatFriend;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.ImageItem;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.newview.MyGridView;
import com.lsjr.zizisteward.utils.Bimp;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.MultipartRequest;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendCircleFriendsActivity extends Activity implements OnClickListener {

    private TextView tv_number;
    private EditText et_content;
    private LinearLayout ll_send;
    private LinearLayout ll_back;

    /**
     * 用户要上传的图片
     */
    private com.lsjr.zizisteward.newview.MyGridView gv_pic;
    private TextView tv_pic;
    private ImageView iv_head;
    public static String path;// 图片地址
    private CharSequence temp;
    private LinearLayout ll_add;
    private LinearLayout ll_order;
    private TextView tv_goods_name;
    private PopWindowCatFriend mPopw;
    private RelativeLayout layout_title_back;

    private String iv;
    private String name;
    private String num;
    private String price;
    private int max_num = 9;
    public static String p_name = "ly";

    public static Uri uri;
    private ImageView iv_clear;
    private GVAdapter gAdapter;
    public static String fileName;
    public static List<SCFBean.ImgBean> img_bean = new ArrayList<>();
    private boolean space = false;
    private Map<String, String> params;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.send_circle_friends_activity);
        App.getInstance().addActivity(this);
        this.img_bean = new ArrayList<>();
        this.findViewById();
    }

    private void findViewById() {

        this.gv_pic = (MyGridView) super.findViewById(R.id.gv_pic);
        this.tv_pic = (TextView) super.findViewById(R.id.tv_pic);
        this.iv_head = (ImageView) super.findViewById(R.id.iv_head);
        this.ll_add = (LinearLayout) super.findViewById(R.id.ll_add);
        this.iv_clear = (ImageView) super.findViewById(R.id.iv_clear);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.ll_send = (LinearLayout) super.findViewById(R.id.ll_send);
        this.tv_number = (TextView) super.findViewById(R.id.tv_number);
        this.et_content = (EditText) super.findViewById(R.id.et_content);
        this.ll_order = (LinearLayout) super.findViewById(R.id.ll_order);
        this.tv_goods_name = (TextView) super.findViewById(R.id.tv_goods_name);
        this.layout_title_back = (RelativeLayout) super.findViewById(R.id.layout_title_back);

        this.ll_add.setOnClickListener(this);
        this.ll_back.setOnClickListener(this);
        this.ll_send.setOnClickListener(this);
        this.iv_clear.setOnClickListener(this);
        this.ll_order.setOnClickListener(this);

        this.mPopw = new PopWindowCatFriend(SendCircleFriendsActivity.this, "发送界面");

        /**
         * this.adapter = new GridAdapter(this); this.adapter.update();
         * this.gv_pic.setAdapter(adapter);
         */

        DisplayMetrics dm = new DisplayMetrics();
        SendCircleFriendsActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;

        LayoutParams lParams = (LayoutParams) gv_pic.getLayoutParams();

        lParams.width = width / 3 * 2;

        gv_pic.setLayoutParams(lParams);

        SCFBean.ImgBean imgBean = new SCFBean.ImgBean();
        imgBean.setType(3);
        img_bean.add(imgBean);

        this.gAdapter = new GVAdapter(SendCircleFriendsActivity.this, img_bean);
        this.gv_pic.setAdapter(gAdapter);

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

        this.gv_pic.setOnItemClickListener(new OnItemClickListener() {

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

                startActivityForResult(new Intent(SendCircleFriendsActivity.this, PicturesChooseActivity.class)
                        .putExtra("pos", pos).putExtra("space", space).putExtra("activity", "scf"), 1);

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {

            case 1:
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

            case 77:

                if (null != data) {
                    iv = data.getStringExtra("iv");
                    name = data.getStringExtra("name");
                    price = data.getStringExtra("price");
                    num = data.getStringExtra("num");
                    Picasso.with(SendCircleFriendsActivity.this).load(HttpConfig.IMAGEHOST + iv).into(iv_head);
                    tv_goods_name.setText(name);
                    tv_pic.setText("¥" + price);
                    ll_add.setVisibility(View.GONE);
                    ll_order.setVisibility(View.VISIBLE);
                    space = true;
                }

                break;
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
            System.out.println(path[i]);
        }

        doUploadTest(params, path);
    }

    private void doUploadTest(Map<String, String> params, String[] path) {

        RequestQueue mSingleQueue = Volley.newRequestQueue(SendCircleFriendsActivity.this);

        List<File> f = new ArrayList<File>();
        for (int i = 0; i < path.length; i++) {
            File files = new File(path[i]);
            f.add(files);
        }

        MultipartRequest request = new MultipartRequest(HttpConfig.IMAGEHOST + "/app/uploadFriendCircle",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        CustomDialogUtils.stopCustomProgressDialog(SendCircleFriendsActivity.this);
                        Toast.makeText(SendCircleFriendsActivity.this, "发送朋友圈成功", Toast.LENGTH_SHORT).show();
                        setResult(1);
                        finish();
                    }
                }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogUtils.stopCustomProgressDialog(SendCircleFriendsActivity.this);
                System.out.println("发送失败");
                System.out.println(error.toString());
                ll_send.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "发表失败,请稍后重试!", Toast.LENGTH_SHORT).show();

            }
        }, "shareImg", f, params);

        mSingleQueue.add(request);
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
                    view.img.setImageBitmap(App.revitionImageSize(img_bean.get(position).getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (img_bean.get(position).getType() == 1) {

                try {

                    view.img.setImageBitmap(App.revitionImageSize(img_bean.get(position).getPath()));

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
                if (isOpen == true) {
                    imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0); // 强制隐藏键盘
                }
                setResult(77);
                finish();
                break;

            case R.id.ll_add:

                startActivityForResult(new Intent(SendCircleFriendsActivity.this, ChooseBaskSingleActivity.class), 77);

                break;

            case R.id.ll_order:

                startActivityForResult(new Intent(SendCircleFriendsActivity.this, ChooseBaskSingleActivity.class), 77);

                break;

            case R.id.ll_send:

                if (et_content.getText().toString().isEmpty()) {
                    Toast.makeText(SendCircleFriendsActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                } else {
                    if (img_bean.size() < 2) {
                        UpDataText(et_content.getText().toString());
                    } else {
                        UpDataPicture(et_content.getText().toString());
                    }
                }

                break;

            case R.id.iv_clear:

                iv = "";
                num = "";
                name = "";
                price = "";
                ll_order.setVisibility(View.GONE);
                iv_head.setImageResource(R.drawable.icon_head);
                ll_add.setVisibility(View.VISIBLE);
                space = false;
                break;
        }
    }

    private void UpDataText(String text) {
        CustomDialogUtils.startCustomProgressDialog(SendCircleFriendsActivity.this, "正在发送朋友圈...");
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "235");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("content", text);
        new HttpClientGet(SendCircleFriendsActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                CustomDialogUtils.stopCustomProgressDialog(SendCircleFriendsActivity.this);
                setResult(1);
                Toast.makeText(SendCircleFriendsActivity.this, "发送朋友圈成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(MyError myError) {
                CustomDialogUtils.stopCustomProgressDialog(SendCircleFriendsActivity.this);
                Toast.makeText(SendCircleFriendsActivity.this, "网络出现了问题,请重试!", Toast.LENGTH_SHORT).show();
                super.onFailure(myError);
            }
        });
    }

    private void UpDataPicture(String text) {
        CustomDialogUtils.startCustomProgressDialog(SendCircleFriendsActivity.this, "正在发送朋友圈...");
        final Map<String, String> map = new HashMap<>();
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("content", text);
        map.put("gnum", null == num || num.length() < 1 ? "" : num);
        map.put("type", "1");
        new Thread(new Runnable() {

            @Override
            public void run() {
                Newcompress(img_bean, map);
            }
        }).start();
    }
}
