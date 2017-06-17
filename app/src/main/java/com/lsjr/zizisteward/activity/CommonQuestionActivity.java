package com.lsjr.zizisteward.activity;

import java.util.HashMap;
import java.util.Map;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class CommonQuestionActivity extends BaseActivity {

    private WebView mMe;

    @Override
    public int getContainerView() {
        return R.layout.activity_commonquestion;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("常见问题");
        mMe = (WebView) findViewById(R.id.me);
        WebSettings settings = mMe.getSettings();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        initData();
    }

    private void initData() {
        CustomDialogUtils.startCustomProgressDialog(CommonQuestionActivity.this, "正在加载....");
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "177");
        new HttpClientGet(CommonQuestionActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                CustomDialogUtils.stopCustomProgressDialog(CommonQuestionActivity.this);
                Common jie = GsonUtil.getInstance().fromJson(result, Common.class);
                mMe.loadUrl(HttpConfig.IMAGEHOST + jie.questionUrl);
            }
        });
    }

    public class Common {
        private String error;
        private String msg;
        private String questionUrl;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getQuestionUrl() {
            return questionUrl;
        }

        public void setQuestionUrl(String questionUrl) {
            this.questionUrl = questionUrl;
        }

    }

}
