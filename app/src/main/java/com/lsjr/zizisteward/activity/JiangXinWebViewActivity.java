package com.lsjr.zizisteward.activity;

import java.util.HashMap;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

@SuppressLint("SetJavaScriptEnabled")
public class JiangXinWebViewActivity extends Activity {
	private WebView mWebview;
	private String mId;
	private ProgressBar mProgressBar1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jiangxin_webview);
		mId = getIntent().getStringExtra("id");
		System.out.println("这个id是" + mId);
		mWebview = (WebView) findViewById(R.id.webview);
		mProgressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		WebSettings settings = mWebview.getSettings();
		mWebview.setVerticalScrollBarEnabled(false);
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		getData();
		mWebview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					mProgressBar1.setVisibility(View.GONE);
				} else {
					mProgressBar1.setVisibility(View.VISIBLE);
					mProgressBar1.setProgress(newProgress);
				}
			}
		});
	}

	private void getData() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("OPT", "403");
		map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
		map.put("spid", mId);
		new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("详情" + result);
				WebviewBean bean = GsonUtil.getInstance().fromJson(result, WebviewBean.class);
				mWebview.loadUrl(HttpConfig.IMAGEHOST + bean.getProductDetail());
			}

			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});
	}

	public class WebviewBean {
		private String productDetail;

		public String getProductDetail() {
			return productDetail;
		}

		public void setProductDetail(String productDetail) {
			this.productDetail = productDetail;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
