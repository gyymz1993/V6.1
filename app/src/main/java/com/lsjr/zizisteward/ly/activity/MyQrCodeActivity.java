package com.lsjr.zizisteward.ly.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.libs.zxing.CaptureActivity;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.MoreQrCodeActivity;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.BasicUserInfo;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.share.OnekeyShare;
import com.shove.security.Encrypt;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import cn.sharesdk.framework.ShareSDK;

public class MyQrCodeActivity extends Activity implements OnClickListener {

    private ImageView iv;
    private TextView tv_name;
    private ImageView iv_level;
    private LinearLayout ll_scan;
    private LinearLayout ll_save;
    private LinearLayout ll_back;
    private LinearLayout ll_share;
    private RoundImageView riv_head;

    private String qrCode;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            Glide.get(MyQrCodeActivity.this).clearDiskCache();
            Glide.get(MyQrCodeActivity.this).clearMemory();
            Glide.with(MyQrCodeActivity.this).load(
                    HttpConfig.IMAGEHOST + qrCode);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.my_qr_code_activity);
        this.findViewById();
    }

    private void findViewById() {

        this.iv = (ImageView) super.findViewById(R.id.iv);
        this.tv_name = (TextView) super.findViewById(R.id.tv_name);
        this.iv_level = (ImageView) super.findViewById(R.id.iv_level);
        this.ll_scan = (LinearLayout) super.findViewById(R.id.ll_scan);
        this.ll_save = (LinearLayout) super.findViewById(R.id.ll_save);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.ll_share = (LinearLayout) super.findViewById(R.id.ll_share);
        this.riv_head = (RoundImageView) super.findViewById(R.id.riv_head);

        this.ll_back.setOnClickListener(this);
        this.ll_scan.setOnClickListener(this);
        this.ll_save.setOnClickListener(this);
        this.ll_share.setOnClickListener(this);

        this.tv_name.setText(App.getUserInfo().getUsername());
        Picasso.with(MyQrCodeActivity.this).load(HttpConfig.IMAGEHOST + App.getUserInfo().getHeadImg()).into(riv_head);

        if (App.getUserInfo().getCredit().equals("0")) {
            iv_level.setImageResource(R.drawable.icon_vip_zero);
        } else if (App.getUserInfo().getCredit().equals("1")) {
            iv_level.setImageResource(R.drawable.icon_vip_one);
        } else if (App.getUserInfo().getCredit().equals("2")) {
            iv_level.setImageResource(R.drawable.icon_vip_two);
        } else if (App.getUserInfo().getCredit().equals("3")) {
            iv_level.setImageResource(R.drawable.icon_vip_three);
        } else if (App.getUserInfo().getCredit().equals("5")) {
            iv_level.setImageResource(R.drawable.icon_vip_four);
        } else if (App.getUserInfo().getCredit().equals("6")) {
            iv_level.setImageResource(R.drawable.icon_vip_five);
        }

        getBean();
        getQrCodeData();
    }

    private void getBean() {
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "199");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("用户消息" + result);
                BasicUserInfo bean = new Gson().fromJson(result, BasicUserInfo.class);
            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });
    }

    private void getQrCodeData() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "431");
        map.put("content", App.getUserInfo().getId());
        map.put("type", "1");
        new HttpClientGet(MyQrCodeActivity.this, null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        System.out.println(result);
                        try {
                            JSONObject jObject = new JSONObject(result);
                            String qrCode = jObject.getString("qrCode");

                            Picasso.with(MyQrCodeActivity.this)
                                    .load(HttpConfig.IMAGEHOST + qrCode)
                                    .into(iv);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                this.finish();
                break;

            case R.id.ll_scan:
                Intent intent = new Intent(MyQrCodeActivity.this,
                        CaptureActivity.class);
                startActivityForResult(intent, 0);
                break;

            case R.id.ll_share:
                Share();
                break;

            case R.id.ll_save:
                iv.setDrawingCacheEnabled(true);// 获取bm前执行，否则无法获取
                Bitmap bm = iv.getDrawingCache();
                saveImageToGallery(MyQrCodeActivity.this, bm);
                Toast.makeText(MyQrCodeActivity.this, "保存二维码成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:

                if (null != data) {
                    String text = data.getStringExtra("text");
                    handleResult(text);
                }

                break;

            case 1:
                switch (resultCode) {
                    case 1:
                        SkinPeeler();
                        break;
                }

                break;
        }
    }

    private void Share() {
        ShareSDK.initSDK(MyQrCodeActivity.this);
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        String productUrl = "http://www.imzi.com/";
        oks.setTitle("分享到");
        oks.setTitleUrl(productUrl);
        oks.setText(productUrl);
        iv.setDrawingCacheEnabled(true);// 获取bm前执行，否则无法获取
        Bitmap bm = iv.getDrawingCache();
        String path = saveImageToGallery(MyQrCodeActivity.this, bm);
        oks.setImagePath(path);// 确保sdcard 存在图片
        oks.setUrl(productUrl);
        oks.setSite("孜孜管家");
        oks.setSiteUrl(productUrl);
        oks.show(MyQrCodeActivity.this);
    }

    private void handleResult(String text) {
        // 普通字符串
        if (TextUtils.isEmpty(text)) {
            return;
        }

        if (text.startsWith("http://") || text.startsWith("https://")) {
            // 网址
            // 跳到浏览器加载网页
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(text));
            startActivity(intent);
        } else if (text.startsWith("tel:")) {
            // 跳到打电话页面
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(text));
            startActivity(intent);
        } else if (text.startsWith("smsto:")) {
            // 跳到发短信页面
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(text));
            startActivity(intent);
        } else if (text.startsWith("mailto:")) {
            // 跳到发邮件页面
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(text));
            startActivity(intent);
        } else if (text.startsWith("market://")) {
            // 跳到应用市场页面
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(text));
            startActivity(intent);
        } else if (text.startsWith("ziziUser:")) {
            String[] str = text.split("ziziUser:");
            getQrCode("1", str[1]);
        } else if (text.startsWith("ziziGroup:")) {
            String[] str = text.split("ziziGroup:");
            getQrCode("2", str[1]);
        } else if (text.startsWith("zizicard:")) {
            String[] str = text.split("zizicard:");
            // 获取解析的数据
            startActivity(new Intent(MyQrCodeActivity.this,
                    CardHolderDetails.class).putExtra("id",
                    str[1]).putExtra("activity", "save"));
        } else {
            // 用弹窗展示信息
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("扫描结果:");// 标题
            builder.setMessage(text);// 内容
            builder.setPositiveButton("确定", null);
            builder.show();
        }
    }

    public static boolean isNumeric(String str) {

        Pattern pattern = Pattern.compile("[0-9]*");

        return pattern.matcher(str).matches();
    }

    private void getQrCode(final String type, final String content) {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "266");
        map.put("content", content);
        map.put("type", type);
        new HttpClientGet(MyQrCodeActivity.this, null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        try {
                            System.out.println(result);
                            JSONObject jObject = new JSONObject(result);
                            String error = jObject.getString("error");

                            if (error.equals("1")) {
                                switch (type) {
                                    case "1":
                                        startActivity(new Intent(
                                                MyQrCodeActivity.this,
                                                QrCodePersonalActivity.class)
                                                .putExtra("id", content));
                                        break;

                                    case "2":
                                        startActivity(new Intent(
                                                MyQrCodeActivity.this,
                                                QrCodeAddGroupActivity.class)
                                                .putExtra("id", content));
                                        break;
                                }
                            } else {
                                // 用弹窗展示信息
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        MyQrCodeActivity.this);
                                builder.setTitle("扫描结果:");// 标题
                                builder.setMessage(content);// 内容
                                builder.setPositiveButton("确定", null);
                                builder.show();
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

    private void SkinPeeler() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "265");
        map.put("user_id",
                addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("content", App.getUserInfo().getId());
        new HttpClientGet(MyQrCodeActivity.this, null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        System.out.println("换肤   " + result);

                        try {
                            JSONObject jObject = new JSONObject(result);
                            qrCode = jObject.getString("qrCode");

                            // handler.sendEmptyMessage(0);
                            Glide.with(MyQrCodeActivity.this)
                                    .load(HttpConfig.IMAGEHOST + qrCode)
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .into(iv);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // getQrCodeData();
                    }

                    @Override
                    public void onFailure(MyError myError) {
                        super.onFailure(myError);
                    }
                });
    }

    private String addSign(long id, String action) {
        String des = Encrypt.encrypt3DES(id + "," + action + ","
                + dateToString(new Date()), "TNNYF17DbevNyxVv");
        String md5 = Encrypt.MD5(des + "TNNYF17DbevNyxVv");
        String sign = des + md5.substring(0, 8);
        return sign;
    }

    @SuppressLint("SimpleDateFormat")
    private static String dateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public String saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(),
                "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + file.getPath())));
        return file.getPath();
    }
}
