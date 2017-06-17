package com.lsjr.zizisteward.ly.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CardQrCodeActivirty extends Activity implements OnClickListener {

    /**
     * 返回键
     */
    private LinearLayout ll_back;
    /**
     * 名字
     */
    private TextView tv_name;
    /**
     * 职位名字
     */
    private TextView tv_position_name;
    /**
     * 二维码图片
     */
    private ImageView iv;
    /**
     * 保存二维码
     */
    private TextView tv_save;

    private String id;
    private String name;
    private String c_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.card_qr_code_activirty);
        this.findViewById();
    }

    private void findViewById() {
        this.iv = (ImageView) super.findViewById(R.id.iv);
        this.tv_name = (TextView) super.findViewById(R.id.tv_name);
        this.tv_save = (TextView) super.findViewById(R.id.tv_save);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.tv_position_name = (TextView) super.findViewById(R.id.tv_position_name);

        this.tv_save.setOnClickListener(this);
        this.ll_back.setOnClickListener(this);

        this.id = getIntent().getStringExtra("id");
        this.name = getIntent().getStringExtra("name");
        this.c_name = getIntent().getStringExtra("c_name");

        this.tv_name.setText(name);
        this.tv_position_name.setText(c_name);

        this.getData();
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "430");
        map.put("content", id);

        new HttpClientGet(CardQrCodeActivirty.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jObject = new JSONObject(result);
                    String cardCode = jObject.getString("cardCode");
                    Picasso.with(CardQrCodeActivirty.this).load(HttpConfig.IMAGEHOST + cardCode).into(iv);

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

                finish();

                break;

            case R.id.tv_save:
                iv.setDrawingCacheEnabled(true);//获取bm前执行，否则无法获取
                Bitmap bm = iv.getDrawingCache();
                saveImageToGallery(CardQrCodeActivirty.this, bm);
                break;
        }
    }

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
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
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
        Toast.makeText(context, "保存二维码成功...", Toast.LENGTH_SHORT).show();
    }
}
