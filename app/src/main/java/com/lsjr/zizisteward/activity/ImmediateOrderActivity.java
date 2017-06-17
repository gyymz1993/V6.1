package com.lsjr.zizisteward.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpConfig;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class ImmediateOrderActivity extends BaseActivity {

	private WebView mWebview;
	private String mUrl;

	@Override
	public int getContainerView() {
		return R.layout.activity_search_brand_detail;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mWebview = (WebView) findViewById(R.id.webview);
		mUrl = getIntent().getStringExtra("url");
		WebSettings settings = mWebview.getSettings();
		mWebview.setVerticalScrollBarEnabled(false);
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		mWebview.setWebChromeClient(new WebChromeClient());
		mWebview.loadUrl(HttpConfig.IMAGEHOST + mUrl);
		System.out.println("订单地址" + HttpConfig.IMAGEHOST + mUrl);

	}
}
