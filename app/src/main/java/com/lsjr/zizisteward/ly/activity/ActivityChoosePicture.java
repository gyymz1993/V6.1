package com.lsjr.zizisteward.ly.activity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.bean.ImageBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.utils.FileUtils_new;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.lsjr.zizisteward.utils.UploadUtil;
import com.lsjr.zizisteward.utils.UploadUtil.OnUploadProcessListener;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 创建群时
 */
public class ActivityChoosePicture extends Activity implements OnClickListener {

    private ImageView iv;
    public static EditText et;
    private Button btn;
    private LinearLayout ll_back;

    private String groupId;
    private String label_id;
    private String activity;
    private String maxusers;
    private String groupname;
    private String description;
    private String groupsPhoto;
    /***
     * 请求使用多长时间
     */
    private static int requestTime = 0;
    private int readTimeOut = 50 * 1000; // 读取超时
    private int connectTimeout = 50 * 1000; // 超时时间
    private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识
    // 随机生成
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
    private static final String CHARSET = "utf-8"; // 设置编码

    private static Context context;
    private boolean up = false;
    private Bitmap bitmap = null;
    private String uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_choose_picture);
        this.findViewById();
        context = ActivityChoosePicture.this;
    }

    private void findViewById() {
        up = false;
        this.et = (EditText) super.findViewById(R.id.et);
        this.btn = (Button) super.findViewById(R.id.btn);
        this.iv = (ImageView) super.findViewById(R.id.iv);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);

        this.activity = getIntent().getStringExtra("activity");

        if (activity.equals("gan")) {
            this.groupId = getIntent().getStringExtra("groupId");
            this.description = getIntent().getStringExtra("description");
            this.groupname = getIntent().getStringExtra("groupname");
            this.maxusers = getIntent().getStringExtra("maxusers");
            this.groupsPhoto = getIntent().getStringExtra("groupsPhoto");
            this.btn.setText("修改群信息");
            this.et.setText(groupname);
            if (null != groupsPhoto && !"".equals(groupsPhoto)) {
                Picasso.with(ActivityChoosePicture.this)
                        .load(HttpConfig.IMAGEHOST + groupsPhoto).into(iv);
            }
        } else {
            this.label_id = getIntent().getStringExtra("label_id");
        }

        this.ll_back.setOnClickListener(this);
        this.iv.setOnClickListener(this);
        this.btn.setOnClickListener(this);
    }

    public static void dis() {
        UploadUtil.space = 0;
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        ((ActivityChoosePicture) context).finish();
    }

    private void ToUp() {
        String fileKey = "imgFile";
        Map<String, String> map = new HashMap<>();
        map.put("type", "1");
        map.put("groupId", groupId);

        if (!up) {
            Toast.makeText(ActivityChoosePicture.this, "请重新选择图片...", Toast.LENGTH_SHORT).show();
        } else {
            File file = new File(uri);
            ToUp_Zero(file, fileKey, HttpConfig.uploadGroupPhoto, map);
        }
    }

    private void ToUp_Zero(final File file, final String fileKey,
                           final String RequestURL, final Map<String, String> param) {

        if (null == file || (!file.exists())) {
            Toast.makeText(ActivityChoosePicture.this, "请重新选择图片...", Toast.LENGTH_SHORT).show();
        } else {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    String result = null;
                    long requestTime = System.currentTimeMillis();
                    long responseTime = 0;

                    try {
                        URL url = new URL(RequestURL);
                        HttpURLConnection conn = (HttpURLConnection) url
                                .openConnection();
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
                        conn.setRequestProperty("Content-Type", CONTENT_TYPE
                                + ";boundary=" + BOUNDARY);
                        // conn.setRequestProperty("Content-Type",
                        // "application/x-www-form-urlencoded");

                        /**
                         * 当文件不为空，把文件包装并且上传
                         */
                        DataOutputStream dos = new DataOutputStream(conn
                                .getOutputStream());
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
                                sb.append(PREFIX).append(BOUNDARY)
                                        .append(LINE_END);
                                sb.append(
                                        "Content-Disposition: form-data; name=\"")
                                        .append(key).append("\"")
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
                        sb.append("Content-Disposition:form-data; name=\""
                                + fileKey + "\"; filename=\"" + file.getName()
                                + "\"" + LINE_END);
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
                        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                                .getBytes();
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
                            InputStreamReader isr = new InputStreamReader(
                                    input, "UTF-8");

                            StringBuffer sb1 = new StringBuffer();
                            int ss;
                            while ((ss = isr.read()) != -1) {
                                sb1.append((char) ss);
                            }
                            result = URLDecoder.decode(sb1.toString(), "utf-8");
                            System.out.println("result : " + result);

                            try {
                                JSONObject jObject = new JSONObject(result);
                                String error = jObject.getString("error");
                                String msg = jObject.getString("msg");
                                Toast.makeText(ActivityChoosePicture.this,
                                        "修改群资料成功...", Toast.LENGTH_SHORT).show();
                                setResult(
                                        1,
                                        getIntent().putExtra("name",
                                                et.getText().toString()));
                                finish();
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ll_back:

                finish();

                break;

            case R.id.iv:

                InputMethodManager imm = (InputMethodManager) ActivityChoosePicture.this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et.getWindowToken(), 0);

                startActivityForResult(new Intent(ActivityChoosePicture.this,
                        ActivityChooseUploadPictures.class), 1);

                break;

            case R.id.btn:

                String name = et.getText().toString();

                if (activity.equals("gan")) {
                    if (null != et.getText().toString()
                            && !et.getText().toString().equals(groupname)) {
                        setName();
                    }

                    if (up) {
                        ToUp();
                    }
                } else {

                    if (null != name && name.length() >= 2) {

                        if (up) {
                            startActivity(new Intent(ActivityChoosePicture.this,
                                    BuySomeGroupInformationActivity.class)
                                    .putExtra("groupname", name)
                                    .putExtra("uri", uri)
                                    .putExtra("label_id", label_id));
                            this.finish();
                        } else {
                            Toast.makeText(ActivityChoosePicture.this, "请上传群头像...",
                                    Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ActivityChoosePicture.this, "群名字至少要大于2个字", Toast.LENGTH_SHORT)
                                .show();
                    }
                }

                break;
        }
    }

    private void setName() {

        Map<String, String> map = new HashMap<>();
        map.put("OPT", "233");
        map.put("groupId", groupId);
        map.put("description", description);
        map.put("owner", PreferencesUtils.getString(ActivityChoosePicture.this,
                "user_account"));
        map.put("groupname", et.getText().toString());
        map.put("maxusers", maxusers);
        new HttpClientGet(ActivityChoosePicture.this, null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        if (!up) {
                            GanapatiDataActivity.tv_name.setText(et.getText().toString());
                            finish();
                        }
                    }
                });
    }

    private void Album() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, 3);
    }

    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "false");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 350);
        intent.putExtra("outputY", 350);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);

        startActivityForResult(intent, 5);
    }

    public void saveBitmap(Bitmap bm) {
        File f = new File(Environment.getExternalStorageDirectory(), "ly");
        if (!f.exists()) {
            f.mkdir();
        }
        String fileName = "new_temporary" + ".jpg";
        File file = new File(f, fileName);
        uri = file.getPath();

        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());

        iv.setImageBitmap(bitmap);

        uri = file.getPath();

        up = true;
    }

    private void Camera() {
        // 激活相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        startActivityForResult(intent, 4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {
            case 1:
                // 相机
                Camera();
                break;

            case 2:
                // 相册
                Album();
                break;

        }

        switch (requestCode) {

            case 3:
                // 相册回调
                if (data != null) {
                    // 得到图片的全路径
                    Uri uri = data.getData();
                    crop(uri);
                }
                break;

            case 4:
                // 相机回调
                if (data != null) {
                    // 得到图片的全路径

                    File f = new File(Environment.getExternalStorageDirectory(), "ly");
                    if (!f.exists()) {
                        f.mkdir();
                    }
                    String fileName = "temporary" + ".jpg";

                    File file = new File(f, fileName);

                    Bundle bundle = data.getExtras();
                    Bitmap bm = (Bitmap) bundle.get("data");
                    FileOutputStream b = null;
                    try {
                        b = new FileOutputStream(file.getPath());
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            b.flush();
                            b.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (file.exists()) {
                        crop(Uri.fromFile(file));
                    }
                } else {
                    Toast.makeText(ActivityChoosePicture.this, "拍照出错，请稍后重试...", Toast.LENGTH_SHORT).show();
                }

                break;

            case 5:
                // 裁剪回调

                if (null != data) {
                    bitmap = data.getParcelableExtra("data");
                    if (null != bitmap) {
                        saveBitmap(bitmap);
                    }
                }

                break;
        }
    }
}
