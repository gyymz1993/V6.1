package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.chatuidemo.DemoHelper;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.bean.EmailBean;
import com.lsjr.zizisteward.bean.ImageBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.lcq.activity.IntoIndentityConfirmActivity;
import com.lsjr.zizisteward.ly.activity.MyQrCodeActivity;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.utils.CircleUtil;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PermissionUtils;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.lsjr.zizisteward.utils.ToastUtils;
import com.lsjr.zizisteward.utils.UploadUtil;
import com.lsjr.zizisteward.utils.UploadUtil.OnUploadProcessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("HandlerLeak")
public class PersonalInfoActivity extends Activity implements OnUploadProcessListener, View.OnClickListener {
    /*去上传文件*/
    protected static final int TO_UPLOAD_FILE = 1;
    /*上传文件响应*/
    protected static final int UPLOAD_FILE_DONE = 2;
    /*选择文件*/
    public static final int TO_SELECT_PHOTO = 3;
    /*上传初始化*/
    private static final int UPLOAD_INIT_PROCESS = 4;
    /* 上传中*/
    private static final int UPLOAD_IN_PROCESS = 5;
    private Bitmap bm = null;
    private Uri tempUri;
    private Bitmap mPhoto;
    /*控件*/
    private EditText et_nick, et_email;
    private CharSequence temp;
    private String user_name, user_email, birthday, sex, shenfen_state_num;
    private RelativeLayout re_back, re_sex, re_birth_day, my_er_code, my_address, add_interests, real_name, real_shenfen;
    private Map<String, String> map;
    private TextView tv_sex, tv_birth_day, address_notif, real_name_state, shenfen_state;
    private Intent intent;
    private RoundImageView user_photo;
    private RealNameConfirmActivity.StateBean bean;
    private Dialog dialog;
    private DateTimePickDialogUtil timeUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_new);
        user_name = App.getUserInfo().getUsername();/*从登录返回值获取昵称*/
        user_email = App.getUserInfo().getEmail();/*从登录返回值获取用户邮箱*/
        re_back = (RelativeLayout) findViewById(R.id.re_back);/*返回键*/
        et_nick = (EditText) findViewById(R.id.et_nick);/*昵称*/
        et_email = (EditText) findViewById(R.id.et_email);/*邮箱*/
        re_sex = (RelativeLayout) findViewById(R.id.re_sex);/*性别选择*/
        tv_sex = (TextView) findViewById(R.id.tv_sex);/*性别文本*/
        tv_birth_day = (TextView) findViewById(R.id.tv_birth_day);/*生日文本*/
        re_birth_day = (RelativeLayout) findViewById(R.id.re_birth_day);/*生日选择*/
        my_er_code = (RelativeLayout) findViewById(R.id.my_er_code);/*二维码*/
        my_address = (RelativeLayout) findViewById(R.id.my_address);/*地址点击*/
        add_interests = (RelativeLayout) findViewById(R.id.add_interests);/*兴趣点击*/
        real_name = (RelativeLayout) findViewById(R.id.real_name);/*实名认证点击*/
        real_shenfen = (RelativeLayout) findViewById(R.id.real_shenfen);/*身份认证点击*/
        user_photo = (RoundImageView) findViewById(R.id.user_photo);/*用户图像*/
        address_notif = (TextView) findViewById(R.id.address_notif);/*地址有无*/
        real_name_state = (TextView) findViewById(R.id.real_name_state);/*实名认证有无*/
        shenfen_state = (TextView) findViewById(R.id.shenfen_state);/*身份认证状态*/
        dialog = new Dialog(this, R.style.dialog);

        initSex();
        initNick();
        initEmail();
        initListener();
    }

    private void initSex() {
        if (App.getUserInfo().getSex().equals("0")) {
            tv_sex.setText("未设置");
        } else if (App.getUserInfo().getSex().equals("1")) {
            tv_sex.setText("男");
        } else {
            tv_sex.setText("女");
        }
        if (TextUtils.isEmpty(App.getUserInfo().getBirthday())) {
            tv_birth_day.setText("未设置");
            re_birth_day.setEnabled(true);
        } else {
            tv_birth_day.setText(App.getUserInfo().getBirthday());
            re_birth_day.setEnabled(true);
        }
    }

    private void initListener() {
        re_back.setOnClickListener(this);
        re_birth_day.setOnClickListener(this);
        re_sex.setOnClickListener(this);
        re_birth_day.setOnClickListener(this);
        my_er_code.setOnClickListener(this);
        my_address.setOnClickListener(this);
        add_interests.setOnClickListener(this);
        real_name.setOnClickListener(this);
        real_shenfen.setOnClickListener(this);
        user_photo.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_back:
                if ((et_nick.getText().toString().trim().equals(user_name) || TextUtils.isEmpty(et_nick.getText().toString().trim()) || "".equals(et_nick.getText().toString().trim()) || et_nick.getText().toString().trim().equals("未设置")) && (et_email.getText().toString().trim().equals(user_email) || et_email.getText().toString().trim().equals("未设置") || TextUtils.isEmpty(et_email.getText().toString().trim()))) {
                    finish();
                } else {
                    if (et_email.getText().toString().trim().equals(user_email) || et_email.getText().toString().trim().equals("未设置") || TextUtils.isEmpty(et_email.getText().toString().trim())) {
                        getNickData();/*只有昵称更改*/
                    } else if (et_nick.getText().toString().trim().equals(user_name) || TextUtils.isEmpty(et_nick.getText().toString().trim()) || "".equals(et_nick.getText().toString().trim()) || et_nick.getText().toString().trim().equals("未设置")) {
                        getEmailData();/*只有邮箱更改*/
                    } else {
                        getNickAndEmail();/*邮箱和昵称同时修改了*/
                    }
                }
                break;
            case R.id.re_birth_day:
                if (TextUtils.isEmpty(App.getUserInfo().getBirthday())) {
                    timeUtils = new DateTimePickDialogUtil(PersonalInfoActivity.this, "");
                    timeUtils.dateTimePicKDialog(tv_birth_day);
                    timeUtils.setOnClickListener(new DateTimePickDialogUtil.OnClickListener() {
                        @Override
                        public void onClickListener(String time) {
                            getDataTime();
                        }
                    });
                } else {
                    dialog.setContentView(R.layout.dialog_birthday_save);
                    Window birthday_window = dialog.getWindow();
                    birthday_window.setGravity(Gravity.CENTER | Gravity.CENTER);
                    TextView sure = (TextView) dialog.findViewById(R.id.sure);
                    sure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                break;
            case R.id.re_sex:
                dialog.setContentView(R.layout.dialog_sex_selector);
                Window sex_window = dialog.getWindow();
                sex_window.setGravity(Gravity.CENTER | Gravity.CENTER);
                Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                Button bt_boy = (Button) dialog.findViewById(R.id.bt_boy);
                Button bt_gril = (Button) dialog.findViewById(R.id.bt_gril);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                bt_boy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        getDataSex("1");
                    }
                });
                bt_gril.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        getDataSex("2");
                    }
                });
                dialog.show();
                break;
            case R.id.my_er_code:
                intent = new Intent(PersonalInfoActivity.this, MyQrCodeActivity.class);
                startActivity(intent);
                break;
            case R.id.my_address:
                intent = new Intent(PersonalInfoActivity.this, ManageAddress.class);
                startActivity(intent);
                break;
            case R.id.add_interests:
                intent = new Intent(PersonalInfoActivity.this, NewInterestClassicActivity.class);
                intent.putExtra("type", "set_centry");
                startActivity(intent);
                break;
            case R.id.user_photo:
                dialog.setContentView(R.layout.dialog_user_photo);
                Window photo_window = dialog.getWindow();
                photo_window.setGravity(Gravity.CENTER | Gravity.CENTER);
                ((Button) dialog.findViewById(R.id.btn_take_photo)).setOnClickListener(new View.OnClickListener() {// 拍照

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (PermissionUtils.isCameraPermission(PersonalInfoActivity.this, 68)) {
                            // 先验证手机是否有sdcard
                            String status = Environment.getExternalStorageState();
                            if (status.equals(Environment.MEDIA_MOUNTED)) {
                                try {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                                    startActivityForResult(intent, 1);
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(PersonalInfoActivity.this, "没有找到储存目录", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(PersonalInfoActivity.this, "没有储存卡", Toast.LENGTH_LONG).show();
                            }
                        } else {

                        }

                    }
                });

                ((Button) dialog.findViewById(R.id.btn_pick_photo)).setOnClickListener(new View.OnClickListener() {// 相册选取

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        try {
                            dialog.dismiss();
                            if (PermissionUtils.isCameraPermission(PersonalInfoActivity.this, 68)) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent, 2);
                            } else {

                            }

                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(PersonalInfoActivity.this, "未能找到照片", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                ((Button) dialog.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.real_name:
                intent = new Intent(PersonalInfoActivity.this, RealNameConfirmActivity.class);
                startActivity(intent);
                break;
            case R.id.real_shenfen:
                if (shenfen_state_num.equals("0")) {// 未身份认证
//                    intent = new Intent(PersonalInfoActivity.this, IdentityAuthenticationActivity.class);
                    intent = new Intent(PersonalInfoActivity.this, IntoIndentityConfirmActivity.class);
                    intent.putExtra("shefen", "set_centry");
                    startActivity(intent);
                } else {// 已身份认证 状态多个
                    intent = new Intent(PersonalInfoActivity.this, IdentityConfirmActivity.class);
                    intent.putExtra("type", shenfen_state_num);
                    startActivity(intent);
                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0x007:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "拍照权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void getDataTime() {
        CustomDialogUtils.startCustomProgressDialog(PersonalInfoActivity.this, "正在设置生日");
        map = new HashMap<String, String>();
        map.put("OPT", "519");
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        map.put("birthday", tv_birth_day.getText().toString().trim());
        new HttpClientGet(PersonalInfoActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                CustomDialogUtils.stopCustomProgressDialog(PersonalInfoActivity.this);
                BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
                App.getUserInfo().setBirthday(tv_birth_day.getText().toString().trim());
                PreferencesUtils.putObject(PersonalInfoActivity.this, "userinfo", App.getUserInfo());
            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
                CustomDialogUtils.stopCustomProgressDialog(PersonalInfoActivity.this);
            }
        });

    }

    private void getDataSex(final String sex) {
        CustomDialogUtils.startCustomProgressDialog(PersonalInfoActivity.this, "正在设置性别");
        map = new HashMap<String, String>();
        map.put("OPT", "518");
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        map.put("sex", sex);
        new HttpClientGet(PersonalInfoActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                CustomDialogUtils.stopCustomProgressDialog(PersonalInfoActivity.this);
                BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
                App.getUserInfo().setSex(sex);
                PreferencesUtils.putObject(PersonalInfoActivity.this, "userinfo", App.getUserInfo());
                if ("1".equals(sex)) {
                    tv_sex.setText("男");
                } else {
                    tv_sex.setText("女");
                }
            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
                CustomDialogUtils.stopCustomProgressDialog(PersonalInfoActivity.this);
            }
        });
    }

    private void getNickAndEmail() {
        CustomDialogUtils.startCustomProgressDialog(PersonalInfoActivity.this, "正在更新内容");
        map = new HashMap<String, String>();
        map.put("OPT", "11");
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        map.put("emailaddress", et_email.getText().toString().trim());
        new HttpClientGet(PersonalInfoActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("邮箱" + result);
                EmailBean bean = GsonUtil.getInstance().fromJson(result, EmailBean.class);
                App.getUserInfo().setEmail(et_email.getText().toString().trim());
                PreferencesUtils.putObject(PersonalInfoActivity.this, "userinfo", App.getUserInfo());

                map = new HashMap<String, String>();
                map.put("OPT", "10");
                map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
                map.put("username", et_nick.getText().toString().trim());
                new HttpClientGet(PersonalInfoActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        CustomDialogUtils.stopCustomProgressDialog(PersonalInfoActivity.this);
                        BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
                        App.getUserInfo().setUsername(et_nick.getText().toString().trim());
                        PreferencesUtils.putObject(PersonalInfoActivity.this, "userinfo", App.getUserInfo());
                        finish();
                        ToastUtils.show(getApplicationContext(), "保存用户信息成功");
                    }

                    @Override
                    public void onFailure(MyError myError) {
                        super.onFailure(myError);
                        CustomDialogUtils.stopCustomProgressDialog(PersonalInfoActivity.this);
                    }

                });


            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
                CustomDialogUtils.stopCustomProgressDialog(PersonalInfoActivity.this);
            }
        });
    }

    /*邮箱请求接口*/
    private void getEmailData() {
        CustomDialogUtils.startCustomProgressDialog(PersonalInfoActivity.this, "正在更新邮箱");
        Map<String, String> map = new HashMap<String, String>();
        map.put("OPT", "11");
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        map.put("emailaddress", et_email.getText().toString().trim());
        new HttpClientGet(PersonalInfoActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("邮箱" + result);
                CustomDialogUtils.stopCustomProgressDialog(PersonalInfoActivity.this);
                EmailBean bean = GsonUtil.getInstance().fromJson(result, EmailBean.class);
                App.getUserInfo().setEmail(et_email.getText().toString().trim());
                PreferencesUtils.putObject(PersonalInfoActivity.this, "userinfo", App.getUserInfo());
                finish();
                ToastUtils.show(getApplicationContext(), "保存用户信息成功");
            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
                CustomDialogUtils.stopCustomProgressDialog(PersonalInfoActivity.this);
            }
        });
    }

    /*昵称接口请求*/
    private void getNickData() {
        CustomDialogUtils.startCustomProgressDialog(PersonalInfoActivity.this, "正在更新昵称!");
        map = new HashMap<String, String>();
        map.put("OPT", "10");
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        map.put("username", et_nick.getText().toString().trim());
        new HttpClientGet(PersonalInfoActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                CustomDialogUtils.stopCustomProgressDialog(PersonalInfoActivity.this);
                BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
                App.getUserInfo().setUsername(et_nick.getText().toString().trim());
                PreferencesUtils.putObject(PersonalInfoActivity.this, "userinfo", App.getUserInfo());
                finish();
                ToastUtils.show(getApplicationContext(), "保存用户信息成功");
            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
                CustomDialogUtils.stopCustomProgressDialog(PersonalInfoActivity.this);
            }

        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((et_nick.getText().toString().trim().equals(user_name) || TextUtils.isEmpty(et_nick.getText().toString().trim()) || "".equals(et_nick.getText().toString().trim()) || et_nick.getText().toString().trim().equals("未设置")) && (et_email.getText().toString().trim().equals(user_email) || et_email.getText().toString().trim().equals("未设置") || TextUtils.isEmpty(et_email.getText().toString().trim()))) {
                finish();
            } else {
                if (et_email.getText().toString().trim().equals(user_email) || et_email.getText().toString().trim().equals("未设置") || TextUtils.isEmpty(et_email.getText().toString().trim())) {
                    getNickData();/*只有昵称更改*/
                } else if (et_nick.getText().toString().trim().equals(user_name) || TextUtils.isEmpty(et_nick.getText().toString().trim()) || "".equals(et_nick.getText().toString().trim()) || et_nick.getText().toString().trim().equals("未设置")) {
                    getEmailData();/*只有邮箱更改*/
                } else {
                    getNickAndEmail();/*邮箱和昵称同时修改了*/
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initNick() {
        if (user_name.length() > 8) {
            et_nick.setText("未设置");
            et_nick.setSelection(et_nick.getText().toString().trim().length());
        } else {
            et_nick.setText(user_name);
            et_nick.setSelection(user_name.length());
        }
        et_nick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (temp.length() > 7) {
                    ToastUtils.show(getApplicationContext(), "该昵称不能超过8个字段");
                }

            }
        });

    }

    private void initEmail() {
        if (TextUtils.isEmpty(user_email) || "".equals(user_email)) {
            et_email.setText("未设置");
            et_email.setSelection(et_email.getText().toString().trim().length());
        } else {
            et_email.setText(user_email);
            et_email.setSelection(user_email.length());
        }
    }

    /*判断用户是否有地址*/
    private void getAddressData() {
        map = new HashMap<String, String>();
        map.put("OPT", "516");
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        new HttpClientGet(PersonalInfoActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.print("地址" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String address_count = jsonObject.getString("count");
                    if ("0".equals(address_count)) {
                        address_notif.setVisibility(View.VISIBLE);
                    } else {
                        address_notif.setVisibility(View.GONE);
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

    @Override
    protected void onResume() {
        super.onResume();
        getAddressData();
        getData();
        Glide.with(PersonalInfoActivity.this).load(HttpConfig.IMAGEHOST + App.getUserInfo().getHeadImg()).into(user_photo);
    }

    /*检查实名认证状态和身份认证状态*/
    private void getData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "88");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("状态" + result);
                bean = GsonUtil.getInstance().fromJson(result, RealNameConfirmActivity.StateBean.class);
                String state = bean.getIsIdNumberVerified();
                shenfen_state_num = bean.getIsPassCertificate();
                if (state.equals("0")) {
                    real_name_state.setText("未设置");
                } else {
                    real_name_state.setText("已认证");
                }
                if (shenfen_state_num.equals("0")) {
                    shenfen_state.setText("未设置");
                } else if (shenfen_state_num.equals("1")) {
                    shenfen_state.setText("审核中");
                } else {
                    shenfen_state.setText("已认证");
                }


            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:// 拍照
                    startPhotoZoom(tempUri);
                    break;
                case 2:// 本地选中
                    startPhotoZoom(data.getData());
                    break;
                case 3:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;

            }
        }
    }

    /*裁剪图片方法实现*/
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /*保存裁剪之后的图片数据*/
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mPhoto = extras.getParcelable("data");
            // photo = CircleUtil.toRoundBitmap(photo, tempUri/
            // 这个时候的图片已经被处理成圆形的了/
            user_photo.setImageBitmap(mPhoto);
            CustomDialogUtils.startCustomProgressDialog(PersonalInfoActivity.this, "正在上传图像...");
            uploadPic(mPhoto);
        }
    }

    @SuppressWarnings("unused")
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(getResources(), photo);
            uploadUserAvatar(Bitmap2Bytes(photo));
        }
    }

    private void uploadUserAvatar(final byte[] data) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                final String avatarUrl = DemoHelper.getInstance().getUserProfileManager().uploadUserAvatar(data);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (avatarUrl != null) {
                            // Toast.makeText(PersonalInfoActivity.this,
                            // getString(R.string.toast_updatephoto_success),
                            // Toast.LENGTH_SHORT).show();
                        } else {
                            // Toast.makeText(PersonalInfoActivity.this,
                            // getString(R.string.toast_updatephoto_fail),
                            // Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        bm = bitmap;
        String imagePath = CircleUtil.savePhoto(bitmap, Environment.getExternalStorageDirectory().getAbsolutePath(),
                String.valueOf(System.currentTimeMillis()));
        Log.e("imagePath", imagePath + "");
        if (imagePath != null) {
            // 拿着imagePath上传了

            String fileKey = "imgFile";
            UploadUtil.getInstance().setOnUploadProcessListener(this);
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
            map.put("type", "1");
            UploadUtil.getInstance().uploadFile(imagePath, fileKey, HttpConfig.UPLOAD_IMAGE, map);
        }
    }

    @Override
    public void onUploadDone(int responseCode, String message) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_FILE_DONE;
        msg.arg1 = responseCode;
        msg.obj = message;
        handler.sendMessage(msg);
    }

    @Override
    public void onUploadProcess(int uploadSize) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_IN_PROCESS;
        msg.arg1 = uploadSize;
        handler.sendMessage(msg);
    }

    @Override
    public void initUpload(int fileSize) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_INIT_PROCESS;
        msg.arg1 = fileSize;
        handler.sendMessage(msg);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TO_UPLOAD_FILE:
                    break;
                case UPLOAD_INIT_PROCESS:
                    break;
                case UPLOAD_IN_PROCESS:
                    break;
                case UPLOAD_FILE_DONE:
                    @SuppressWarnings("unused")
                    String result = "响应码：" + msg.arg1 + "\n响应信息：" + msg.obj + "\n耗时：" + UploadUtil.getRequestTime() + "秒";
                    String message = (String) msg.obj;
                    ImageBean bean = GsonUtil.getInstance().fromJson(message, ImageBean.class);
                    CustomDialogUtils.stopCustomProgressDialog(PersonalInfoActivity.this);
                    if ("上传图片成功".equals(bean.getMsg())) {
                        Glide.with(PersonalInfoActivity.this).load(HttpConfig.IMAGEHOST + bean.getFilename()).into(user_photo);
                        App.getUserInfo().setHeadImg(bean.getFilename());
                        PreferencesUtils.putObject(PersonalInfoActivity.this, "userinfo", App.getUserInfo());
                        // uploadUserAvatar(Bitmap2Bytes(bm));
                    } else {
                        CustomDialogUtils.stopCustomProgressDialog(PersonalInfoActivity.this);
                        Toast.makeText(PersonalInfoActivity.this, message, Toast.LENGTH_SHORT).show();
                        Glide.with(PersonalInfoActivity.this).load(HttpConfig.IMAGEHOST + App.getUserInfo().getHeadImg())
                                .into(user_photo);
                    }
                    break;

            }
            super.handleMessage(msg);
        }
    };
}
