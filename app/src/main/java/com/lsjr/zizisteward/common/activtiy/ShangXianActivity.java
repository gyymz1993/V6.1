package com.lsjr.zizisteward.common.activtiy;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.SixthNewActivity;
import com.lsjr.zizisteward.activity.ZiXunActivity;
import com.lsjr.zizisteward.http.HttpConfig;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

@SuppressLint("SetJavaScriptEnabled")
public class ShangXianActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shangxian);
		String url = getIntent().getStringExtra("url");
		RelativeLayout back = (RelativeLayout) findViewById(R.id.back);
		WebView webview = (WebView) findViewById(R.id.webview);
		WebSettings settings = webview.getSettings();
		webview.setVerticalScrollBarEnabled(false);
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		webview.loadUrl(HttpConfig.IMAGEHOST + url);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
