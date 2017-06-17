package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chatuidemo.DemoHelper;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.LoginOutBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.ly.activity.UpdateApp;
import com.lsjr.zizisteward.ly.activity.UpdateInfoService;
import com.lsjr.zizisteward.share.OnekeyShare;
import com.lsjr.zizisteward.utils.BitmapUtils;
import com.lsjr.zizisteward.utils.ClearCacheUtils;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

import static com.lsjr.zizisteward.R.id.iv;

public class SettingActivity extends BaseActivity implements OnClickListener {
    private RelativeLayout clean_cache, idea_feedback, common_question, about_us, re_version_update, share_app, re_agreement;
    private TextView login_out, version, user_agreement;
    private ImageView mIv, iv_logo;
    private Intent intent;
    private String mVersion;
    private String mUrl;
    private UpdateInfoService updateInfoService;
    private ProgressDialog progressDialog;
    private LoginOutBean mBean;

    @Override
    public int getContainerView() {
        return R.layout.activity_setting_new;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("设置");
        clean_cache = (RelativeLayout) findViewById(R.id.clean_cache);// 清理缓存
        idea_feedback = (RelativeLayout) findViewById(R.id.idea_feedback);// 意见反馈
        common_question = (RelativeLayout) findViewById(R.id.common_question);// 常见问题
        about_us = (RelativeLayout) findViewById(R.id.about_us);// 关于我们
        re_version_update = (RelativeLayout) findViewById(R.id.re_version_update);// 检查更新
        share_app = (RelativeLayout) findViewById(R.id.share_app);// 分享app
        login_out = (TextView) findViewById(R.id.login_out);// 登出
        version = (TextView) findViewById(R.id.version);// 版本号
        user_agreement = (TextView) findViewById(R.id.user_agreement);//用户协议
        mIv = (ImageView) findViewById(iv);// 红点
        iv_logo = (ImageView) findViewById(R.id.iv_logo);// 孜孜管家logo
        re_agreement = (RelativeLayout) findViewById(R.id.re_agreement);/*用户协议点击*/

        getCheckUpdate();
        initLayout();

    }

    private void initLayout() {
        iv_logo.setImageResource(R.drawable.zz_logo);
        clean_cache.setOnClickListener(this);
        idea_feedback.setOnClickListener(this);
        common_question.setOnClickListener(this);
        about_us.setOnClickListener(this);
        re_version_update.setOnClickListener(this);
        share_app.setOnClickListener(this);
        login_out.setOnClickListener(this);
        re_agreement.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clean_cache:
                ClearCacheUtils.clearAppCache(SettingActivity.this);/*清理缓存*/
                break;
            case R.id.idea_feedback:
                startActivity(new Intent(SettingActivity.this, FeekBackActivity.class));// 意见反馈
                break;
            case R.id.common_question:
                startActivity(new Intent(SettingActivity.this, CommonQuestionActivity.class));/*常见问题*/
                break;
            case R.id.about_us:
                break;
            case R.id.re_version_update:
                CheckUpdate();
                break;
            case R.id.share_app:
                ShareSDK.initSDK(SettingActivity.this);
                OnekeyShare oks = new OnekeyShare();
                oks.disableSSOWhenAuthorize();
                String productUrl = "http://www.zizi.com.cn";
                oks.setTitle("孜孜管家官方网站");
                oks.setTitleUrl(productUrl);
                oks.setText("http://www.zizi.com.cn");
//                oks.setImageUrl(productUrl);

                iv_logo.setDrawingCacheEnabled(true);
                Bitmap bm = iv_logo.getDrawingCache();
                String imagePath = BitmapUtils.saveImageToGallery(SettingActivity.this, bm);

                oks.setImagePath(imagePath);// 确保sdcard 存在图片
                oks.setUrl(productUrl);
                oks.setSite("孜孜官网");
                oks.setSiteUrl(productUrl);
                oks.setCallback(new PlatformActionListener() {

                    @Override
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        System.out.println("分享失败");
                    }

                    @Override
                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        System.out.println("成功");
                    }

                    @Override
                    public void onCancel(Platform arg0, int arg1) {
                        System.out.println("取消分享");
                    }
                });
                oks.show(SettingActivity.this);
                break;
            case R.id.login_out:
                final Dialog dialog = new Dialog(SettingActivity.this, R.style.dialog);
                dialog.setContentView(R.layout.dialog_login_out);
                Window window = dialog.getWindow();
                window.setGravity(Gravity.CENTER | Gravity.CENTER);
                TextView confirm = (TextView) dialog.findViewById(R.id.confirm);
                TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
                confirm.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        CustomDialogUtils.startCustomProgressDialog(SettingActivity.this, "正在退出!");
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("OPT", "198");
                        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                        new HttpClientGet(SettingActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                            @Override
                            public void onSuccess(String result) {
                                mBean = GsonUtil.getInstance().fromJson(result, LoginOutBean.class);
                                System.out.println("退出的消息" + result);
                                DemoHelper.getInstance().logout(false, new EMCallBack() {

                                    @Override
                                    public void onSuccess() {
                                        App.setLoginOutBean(mBean);
                                        PreferencesUtils.putBoolean(SettingActivity.this, "isLogin", false);
                                        CustomDialogUtils.stopCustomProgressDialog(SettingActivity.this);
                                        Intent intent = new Intent(SettingActivity.this, SixthNewActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onProgress(int arg0, String arg1) {
                                    }

                                    @Override
                                    public void onError(int arg0, String arg1) {
                                    }
                                });
                            }

                            @Override
                            public void onFailure(MyError myError) {
                                super.onFailure(myError);
                                CustomDialogUtils.stopCustomProgressDialog(SettingActivity.this);
                            }
                        });
                    }
                });
                cancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.re_agreement:
                startActivity(new Intent(SettingActivity.this, ServiceClauseActivity.class));
                break;
        }
    }

    public String getV() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = "AZ" + info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "获取版本号失败";
        }
    }

    private void CheckUpdate() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "164");
        new HttpClientGet(SettingActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("版本号消息" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String error = jsonObject.getString("error");

                    // if (null != error && error.equals("1")) {
                    // mVersion = jsonObject.getString("versionNumber");
                    // mUrl = jsonObject.getString("downloadLink");
                    //
                    // if (!getV().equals(mVersion)) {
                    // mTv_version.setText(mVersion);
                    // mIv.setVisibility(View.VISIBLE);
                    // handler.sendEmptyMessage(0);
                    //
                    // } else {
                    // mIv.setVisibility(View.GONE);
                    // }
                    // }

                    if (null != error && error.equals("1")) {
                        String mVersion = jsonObject.getString("versionNumber");
                        mUrl = jsonObject.getString("downloadLink");

                        System.out.println("最新版本    " + mVersion);
                        System.out.println("用户安装版本    " + getV());

                        // 获取最新版本号
                        String[] Version = mVersion.split(",");
                        String[] S_V = (Version[0]).split("AZ");
                        String[] V = S_V[1].split("\\.");

                        String v = "";

                        for (int i = 0; i < V.length; i++) {
                            v += V[i];
                        }

                        // 获取安装的版本号
                        String[] U_V = getV().split("AZ");
                        String[] _V = U_V[1].split("\\.");

                        String _v = "";

                        for (int i = 0; i < _V.length; i++) {
                            _v += _V[i];
                        }

                        System.out.println(v);
                        System.out.println(_v);

                        if (Integer.valueOf(v) == Integer.valueOf(_v)) {
                            // 如果版本相同
                            Toast.makeText(SettingActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
                        } else {
                            // 如果有最新版本
                            startActivityForResult(new Intent(SettingActivity.this, UpdateApp.class), 1);
                        }
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

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 如果有更新就提示
            updateInfoService = new UpdateInfoService(SettingActivity.this);
            showUpdateDialog(mUrl, mVersion);
        }

        ;
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {
            case 0:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(mUrl);
                intent.setData(content_url);
                startActivity(intent);
                break;

            case 1:
                Intent _intent = new Intent(SettingActivity.this, SixthNewActivity.class);
                startActivity(_intent);
                finish();
                break;
        }
    }

    private void showUpdateDialog(final String url, String version) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.zz_logo_mini);
        builder.setTitle("请升级APP至版本" + version);
        builder.setMessage("版本更新...");
        builder.setCancelable(false);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // downFile(url);
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(url);
                    intent.setData(content_url);
                    startActivity(intent);

                } else {
                    Toast.makeText(SettingActivity.this, "SD卡不可用，请插入SD卡", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    public void downFile(final String url) {
        progressDialog = new ProgressDialog(SettingActivity.this); // 进度条，在下载的时候实时更新进度，提高用户友好度
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在下载");
        progressDialog.setMessage("请稍候...");
        progressDialog.setProgress(0);
        progressDialog.show();
        updateInfoService.downLoadFile(url, progressDialog, handler);
    }

    private void getCheckUpdate() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "164");
        new HttpClientGet(SettingActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("版本" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String error = jsonObject.getString("error");

                    if (null != error && error.equals("1")) {
                        mVersion = jsonObject.getString("versionNumber");
                        mUrl = jsonObject.getString("downloadLink");

                        version.setText(mVersion.substring(0, mVersion.length() - 2));
                        if (!getV().equals(mVersion.substring(0, mVersion.length() - 2))) {
                            mIv.setVisibility(View.VISIBLE);
                        } else {
                            mIv.setVisibility(View.GONE);
                        }
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
}
