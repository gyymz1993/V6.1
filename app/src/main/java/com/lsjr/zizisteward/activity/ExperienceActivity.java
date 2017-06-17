package com.lsjr.zizisteward.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpConfig;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

@SuppressLint("SetJavaScriptEnabled")
public class ExperienceActivity extends BaseActivity {
	private WebView mWebview;
	private String mUrl;
	private ProgressBar mProgressBar1;
	private RelativeLayout mIv_back;

	@Override
	public int getContainerView() {
		return R.layout.activity_tiyan;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("体验中心");
		mIv_back = (RelativeLayout) findViewById(R.id.iv_back);
		mProgressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		mWebview = (WebView) findViewById(R.id.webview);
		mUrl = getIntent().getStringExtra("url");
		WebSettings settings = mWebview.getSettings();
		mWebview.setVerticalScrollBarEnabled(false);
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		mWebview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
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
		mWebview.loadUrl(HttpConfig.IMAGEHOST + mUrl);
		mWebview.addJavascriptInterface(new DemoJavaInterface(), "control");
		mIv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mWebview.canGoBack()) {
					mWebview.goBack();
				} else {
					finish();
				}
			}
		});

	}

	public class DemoJavaInterface {

		@JavascriptInterface
		public void Thephonebook() {
			Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + "88300006"));
			startActivity(phoneIntent);
		}

	}

	// android webview点击返回键返回上一个html
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
			mWebview.goBack();// 返回前一个页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
