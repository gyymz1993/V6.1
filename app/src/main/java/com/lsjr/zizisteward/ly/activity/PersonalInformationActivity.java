package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chatuidemo.DemoHelper;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.mybetterandroid.wheel.widget.TosAbsSpinner;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PersonalInformationActivity extends Activity implements
        OnClickListener {

    public static TextView tv_nickname;
    private LinearLayout ll_back;
    private LinearLayout ll_photo;
    private ImageView iv_real_name;
    private ImageView iv_nike_name;
    private RoundImageView riv_head;
    private LinearLayout ll_no_circle;
    private LinearLayout ll_nike_name;
    private LinearLayout ll_my_qr_code;
    private ImageView iv_account_search;
    private LinearLayout ll_no_friend_circle;

    public static Bitmap bitmap = null;
    public static String uri = null;

    private static int requestTime = 0;
    private int readTimeOut = 50 * 1000; // 读取超时
    private int connectTimeout = 50 * 1000; // 超时时间
    private boolean god = false;
    private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识
    // 随机生成
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
    private static final String CHARSET = "utf-8"; // 设置编码

    private List<UserMessageBean.UserMessage> userMessage = new ArrayList<>();
    private byte[] data = null;
    private Bitmap bp = null;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            riv_head.setImageBitmap(bp);

            Toast.makeText(
                    PersonalInformationActivity.this, "上传头像成功",
                    Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.personal_information_activity);
        this.findViewById();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void findViewById() {

        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.ll_photo = (LinearLayout) super.findViewById(R.id.ll_photo);
        this.riv_head = (RoundImageView) super.findViewById(R.id.riv_head);
        this.tv_nickname = (TextView) super.findViewById(R.id.tv_nickname);
        this.iv_nike_name = (ImageView) super.findViewById(R.id.iv_nike_name);
        this.iv_real_name = (ImageView) super.findViewById(R.id.iv_real_name);
        this.ll_my_qr_code = (LinearLayout) super.findViewById(R.id.ll_my_qr_code);
        this.ll_no_circle = (LinearLayout) super
                .findViewById(R.id.ll_no_circle);
        this.ll_nike_name = (LinearLayout) super
                .findViewById(R.id.ll_nike_name);
        this.iv_account_search = (ImageView) super
                .findViewById(R.id.iv_account_search);
        this.ll_no_friend_circle = (LinearLayout) super
                .findViewById(R.id.ll_no_friend_circle);

        this.ll_back.setOnClickListener(this);
        this.ll_photo.setOnClickListener(this);
        this.iv_nike_name.setOnClickListener(this);
        this.iv_real_name.setOnClickListener(this);
        this.ll_nike_name.setOnClickListener(this);
        this.ll_no_circle.setOnClickListener(this);
        this.ll_my_qr_code.setOnClickListener(this);
        this.iv_account_search.setOnClickListener(this);
        this.ll_no_friend_circle.setOnClickListener(this);

        getData();
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "250");
        map.put("user_id",
                Fragment_ChatList.addSign(
                        Long.valueOf(App.getUserInfo().getId()), "u"));

        new HttpClientGet(PersonalInformationActivity.this, null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        System.out.println("个人信息 " + result);
                        UserMessageBean umBean = new Gson().fromJson(result,
                                UserMessageBean.class);
                        userMessage = umBean.getUserMessage();

                        if (null != userMessage && userMessage.size() != 0) {

                            if (userMessage.get(0).getIs_show_realName()
                                    .equals("1")) {
                                iv_real_name
                                        .setImageResource(R.drawable.switch_close);
                            } else {
                                iv_real_name
                                        .setImageResource(R.drawable.switch_open);
                            }

                            if (userMessage.get(0).getIs_pass_account_search()
                                    .equals("1")) {
                                iv_account_search
                                        .setImageResource(R.drawable.switch_close);
                            } else {
                                iv_account_search
                                        .setImageResource(R.drawable.switch_open);
                            }

                            if (userMessage.get(0).getIs_pass_nickName_search()
                                    .equals("1")) {
                                iv_nike_name
                                        .setImageResource(R.drawable.switch_close);
                            } else {
                                iv_nike_name
                                        .setImageResource(R.drawable.switch_open);
                            }

                            Picasso.with(PersonalInformationActivity.this)
                                    .load(HttpConfig.IMAGEHOST
                                            + userMessage.get(0).getPhoto())
                                    .into(riv_head);

                            tv_nickname.setText(userMessage.get(0)
                                    .getUser_name());
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

            case R.id.ll_photo:

                startActivityForResult(new Intent(PersonalInformationActivity.this,
                        PersonImageSetActivity.class), 1);
                break;

            case R.id.ll_nike_name:
                // 修改昵称
                startActivityForResult(new Intent(PersonalInformationActivity.this,
                        ModifyNikeNameActivity.class).putExtra("name", userMessage.get(0)
                        .getUser_name()), 2);
                break;

            case R.id.iv_real_name:
                // 是否显示实名

                Map<String, String> map = new HashMap<>();
                map.put("OPT", "251");
                map.put("user_id",
                        Fragment_ChatList.addSign(
                                Long.valueOf(App.getUserInfo().getId()), "u"));
                map.put("type", userMessage.get(0).getIs_show_realName()
                        .equals("0") ? "1" : "0");

                new HttpClientGet(PersonalInformationActivity.this, null, map,
                        false, new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jObject = new JSONObject(result);

                            String error = jObject.getString("error");
                            if (error.equals("1")) {
                                userMessage.get(0).setIs_show_realName(
                                        userMessage.get(0)
                                                .getIs_show_realName()
                                                .equals("0") ? "1" : "0");

                                if (userMessage.get(0)
                                        .getIs_show_realName().equals("1")) {
                                    iv_real_name
                                            .setImageResource(R.drawable.switch_close);
                                } else {
                                    iv_real_name
                                            .setImageResource(R.drawable.switch_open);
                                }
                                Toast.makeText(
                                        PersonalInformationActivity.this,
                                        "改变是否显示实名状态成功...", Toast.LENGTH_SHORT).show();
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

                break;

            case R.id.iv_account_search:
                // 是否通过账号搜索到我

                Map<String, String> map_acc = new HashMap<>();
                map_acc.put("OPT", "253");
                map_acc.put("type", userMessage.get(0).getIs_pass_account_search()
                        .equals("0") ? "1" : "0");
                map_acc.put(
                        "user_id",
                        Fragment_ChatList.addSign(
                                Long.valueOf(App.getUserInfo().getId()), "u"));

                new HttpClientGet(PersonalInformationActivity.this, null, map_acc,
                        false, new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jObject = new JSONObject(result);
                            String error = jObject.getString("error");
                            if (error.equals("1")) {
                                userMessage
                                        .get(0)
                                        .setIs_pass_account_search(
                                                userMessage
                                                        .get(0)
                                                        .getIs_pass_account_search()
                                                        .equals("0") ? "1"
                                                        : "0");

                                if (userMessage.get(0)
                                        .getIs_pass_account_search()
                                        .equals("1")) {
                                    iv_account_search
                                            .setImageResource(R.drawable.switch_close);
                                } else {
                                    iv_account_search
                                            .setImageResource(R.drawable.switch_open);
                                }
                                Toast.makeText(
                                        PersonalInformationActivity.this,
                                        "改变是否通过账号搜到我状态成功...", Toast.LENGTH_SHORT).show();
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

                break;

            case R.id.iv_nike_name:
                // 是否通过昵称搜索到我
                Map<String, String> map_nike = new HashMap<>();
                map_nike.put("OPT", "252");
                map_nike.put("type", userMessage.get(0)
                        .getIs_pass_nickName_search().equals("0") ? "1" : "0");
                map_nike.put(
                        "user_id",
                        Fragment_ChatList.addSign(
                                Long.valueOf(App.getUserInfo().getId()), "u"));

                new HttpClientGet(PersonalInformationActivity.this, null, map_nike,
                        false, new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jObject = new JSONObject(result);
                            String error = jObject.getString("error");
                            if (error.equals("1")) {
                                userMessage
                                        .get(0)
                                        .setIs_pass_nickName_search(
                                                userMessage
                                                        .get(0)
                                                        .getIs_pass_nickName_search()
                                                        .equals("0") ? "1"
                                                        : "0");

                                if (userMessage.get(0)
                                        .getIs_pass_nickName_search()
                                        .equals("1")) {
                                    iv_nike_name
                                            .setImageResource(R.drawable.switch_close);
                                } else {
                                    iv_nike_name
                                            .setImageResource(R.drawable.switch_open);
                                }
                                Toast.makeText(
                                        PersonalInformationActivity.this,
                                        "改变是否通过昵称搜到我状态成功...", Toast.LENGTH_SHORT).show();
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

                break;

            case R.id.ll_no_friend_circle:
                // 不让他(她)看我的朋友圈
                startActivity(new Intent(PersonalInformationActivity.this,
                        NoLookFriendCircleActivity.class).putExtra("activity",
                        "no_friend_circle").putExtra("title", "不让他(她)看我的朋友圈"));
                break;

            case R.id.ll_no_circle:
                // 不看他(她的朋友圈)
                startActivity(new Intent(PersonalInformationActivity.this,
                        NoLookFriendCircleActivity.class).putExtra("activity",
                        "no_circle").putExtra("title", "不看他(她)的朋友圈"));

                break;

            case R.id.ll_my_qr_code:

                startActivity(new Intent(PersonalInformationActivity.this, MyQrCodeActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 2:
                this.tv_nickname.setText(App.getUserInfo().getUsername());
                Fragment_AddressBook.requestAddressBook();
                break;
        }

        switch (resultCode) {
            case 1:
                ToUp();
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void ToUp() {
        String fileKey = "imgFile";
        Map<String, String> map = new HashMap<>();
        map.put("type", "1");
        map.put("user_id",
                EncryptUtils.addSign(
                        Integer.parseInt(App.getUserInfo().getId()), "u"));
        if (null == uri) {
            Toast.makeText(PersonalInformationActivity.this, "请重新选择图片...", Toast.LENGTH_SHORT)
                    .show();
        } else {
            File file = new File(uri);
            bp = BitmapFactory.decodeFile(file.getPath());

            data = Bitmap2Bytes(bp);
            //Fragment_Circle_Friends.iv_head.setImageBitmap(bp);
            //handler.sendEmptyMessage(1);

            CustomDialogUtils.startCustomProgressDialog(PersonalInformationActivity.this, "请稍候");
            ToUp_Zero(file, fileKey, HttpConfig.UPLOAD_IMAGE, map);
        }
    }

    private void uploadUserAvatar(final byte[] data, final Bitmap bm) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                final String avatarUrl = DemoHelper.getInstance()
                        .getUserProfileManager().uploadUserAvatar(data);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        CustomDialogUtils.stopCustomProgressDialog(PersonalInformationActivity.this);

                        if (avatarUrl != null) {
                            System.out.println("上传头像成功");
                            riv_head.setImageBitmap(bm);

                            if (Fragment_Circle_Friends.iv_head != null) {
                                Fragment_Circle_Friends.getBackGroup(1);
                            }

                            Fragment_AddressBook.requestAddressBook();

                            Toast.makeText(
                                    PersonalInformationActivity.this,
                                    getString(R.string.toast_updatephoto_success),
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            System.out.println("上传头像失败");
                            Toast.makeText(PersonalInformationActivity.this,
                                    getString(R.string.toast_updatephoto_fail),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        System.out.println("头像转型成字节数组");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private void ToUp_Zero(final File file, final String fileKey,
                           final String RequestURL, final Map<String, String> param) {

        if (null == file || (!file.exists())) {
            Toast.makeText(PersonalInformationActivity.this, "请重新选择图片...", Toast.LENGTH_SHORT)
                    .show();
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
                                String filename = jObject.getString("filename");
                                App.getUserInfo().setHeadImg(filename);
                                String error = jObject.getString("error");
                                String msg = jObject.getString("msg");
                                Bitmap bp = BitmapFactory.decodeFile(file.getPath());

                                Fragment_AddressBook.requestAddressBook();

                                handler.sendEmptyMessage(0);

                                CustomDialogUtils.stopCustomProgressDialog(PersonalInformationActivity.this);

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
}
