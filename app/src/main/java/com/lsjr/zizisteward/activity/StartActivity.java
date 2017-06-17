package com.lsjr.zizisteward.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.ly.activity.UpdateApp;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StartActivity extends Activity {
    private static final int ANIMATION_DURATION = 1000;
    ImageView mImageView;
    private String content;
    private Handler handler;

    private static final int sleepTime = 1500;
    public static boolean isForeground = false;
    public String device;
    private TextView yangben;
    private String mUrl;

    private boolean space = false;

    public static final String MESSAGE_RECEIVED_ACTION = "com.lsjr.zizisteward.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    private ImageView splash;
    private String newmsg;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        splash = (ImageView) findViewById(R.id.splash);
        playAnimation();
        yangben = (TextView) findViewById(R.id.yangben);
        getDis();
    }


    public void getDis(){
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();//真实分辨率 宽
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();//真实分辨率 高
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        int densityDPI = dm.densityDpi;     // 屏幕密度（每寸像素：120(ldpi)/160(mdpi)/213(tvdpi)/240(hdpi)/320(xhdpi)）
        Toast.makeText(this, "真实分辨率："+screenWidth+"*"+screenHeight+"  每英寸:"+densityDPI, Toast.LENGTH_LONG).show();


        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）
        int height = metric.heightPixels;  // 屏幕高度（像素）
        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        double diagonalPixels = Math.sqrt(Math.pow(width, 2)+Math.pow(height, 2)) ;
        double screenSize = diagonalPixels/density;
        Toast.makeText(this, "最小屏宽："+screenSize, Toast.LENGTH_LONG).show();


    }

    private void CheckUpdate() {

        space = true;

        Map<String, String> map = new HashMap<>();
        map.put("OPT", "164");
        new HttpClientGet(StartActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String error = jsonObject.getString("error");

                    if (null != error && error.equals("1")) {
                        String mVersion = jsonObject.getString("versionNumber");
                        mUrl = jsonObject.getString("downloadLink");

                        //获取最新版本号
                        String[] Version = mVersion.split(",");
                        String[] S_V = (Version[0]).split("AZ");
                        String[] V = S_V[1].split("\\.");

                        String v = "";

                        for (int i = 0; i < V.length; i++) {
                            v += V[i];
                        }

                        //获取安装的版本号
                        String[] U_V = getV().split("AZ");
                        String[] _V = U_V[1].split("\\.");

                        String _v = "";

                        for (int i = 0; i < _V.length; i++) {
                            _v += _V[i];
                        }

                        if (Integer.valueOf(v) == Integer.valueOf(_v)) {
                            //如果版本相同
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    if (!PreferencesUtils.getBoolean(StartActivity.this, "isFirst")) {
                                        intent = new Intent(StartActivity.this, GuideActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.fade, R.anim.hold);
                                        finish();
                                        PreferencesUtils.putBoolean(StartActivity.this, "isFirst", true);
                                    } else {
                                        intent = new Intent(StartActivity.this, SimpleVoiceActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.fade, R.anim.hold);
                                        finish();
                                    }

                                }
                            }, 2000);

                        } else if (Integer.valueOf(v) > Integer.valueOf(_v)) {
                            //如果有最新版本
                            if ((Integer.valueOf(v) - Integer.valueOf(_v)) == 1) {
                                //版本低于1个版本
                                if (mVersion.split(",")[1].equals("1")) {
                                    //1是强制更新
                                    System.out.println("强制更新");
                                    startActivity(new Intent(StartActivity.this, UpdateApp.class)
                                            .putExtra("resolution", 0).putExtra("url", mUrl));
                                } else {
                                    //0是不强制更新
                                    System.out.println("不强制更新");
                                    startActivityForResult(new Intent(StartActivity.this, UpdateApp.class)
                                            .putExtra("resolution", 1).putExtra("url", mUrl), 1);
                                }
                            } else {
                                //版本低于2个版本,应强制更新
                                System.out.println("强制更新");
                                startActivity(new Intent(StartActivity.this, UpdateApp.class)
                                        .putExtra("resolution", 0).putExtra("url", mUrl));
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(MyError myError) {
                Toast.makeText(StartActivity.this, "请检查网络环境", Toast.LENGTH_SHORT).show();
                super.onFailure(myError);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {
            case 0:
                intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(mUrl);
                intent.setData(content_url);
                startActivity(intent);
                break;

            case 1:
                if (!PreferencesUtils.getBoolean(StartActivity.this, "isFirst")) {
                    intent = new Intent(StartActivity.this, GuideActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                    finish();
                    PreferencesUtils.putBoolean(StartActivity.this, "isFirst", true);
                } else {
                    intent = new Intent(StartActivity.this, SimpleVoiceActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onResume() {

        if (!space) {

            //如果版本相同
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    CheckUpdate();
                }
            }, 500);
        }

        super.onResume();
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

    private void playAnimation() {
        AnimationSet animationSet = new AnimationSet(true);
        // 缩放的动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(ANIMATION_DURATION);
        // 透明度动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(ANIMATION_DURATION);

        // 把两个动画效果装到一起
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        animationSet.setAnimationListener(animationListener);

        // 同时播放两种动画效果
        splash.startAnimation(animationSet);
    }

    private AnimationListener animationListener = new AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        // 当动画结束时，会调用此方法
        @Override
        public void onAnimationEnd(Animation animation) {

        }
    };

}
