package com.lsjr.zizisteward.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.http.HttpConfig;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class BannerWebViewActivity extends Activity {
	private WebView mBaner_webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bannr_webview);
		String url = getIntent().getStringExtra("url");
		mBaner_webview = (WebView) findViewById(R.id.baner_webview);
		WebSettings settings = mBaner_webview.getSettings();
		mBaner_webview.setVerticalScrollBarEnabled(false);
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		mBaner_webview.loadUrl(HttpConfig.IMAGEHOST + url);
	}
}
