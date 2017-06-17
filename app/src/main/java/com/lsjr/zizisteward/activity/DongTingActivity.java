package com.lsjr.zizisteward.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("SetJavaScriptEnabled")
public class DongTingActivity extends Activity {
	private WebView webview;
	private String mUrl;
	private TextView mTv_title;
	private Intent mIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dongting);
		mUrl = getIntent().getStringExtra("url");
		webview = (WebView) findViewById(R.id.webview);
		mTv_title = (TextView) findViewById(R.id.tv_title);
		if (mUrl.equals("/Membershipprivileges/membercenter")) {
			mTv_title.setText("会员中心");
		}
		if (mUrl.equals("/Membershipprivileges/legalconsulting")) {
			mTv_title.setText("法务咨询");
		}
		if (mUrl.equals("lotterydraw")) {
			mTv_title.setText("上线庆典");
		}

		RelativeLayout findViewById = (RelativeLayout) findViewById(R.id.back);
		findViewById.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (webview.canGoBack()) {
					webview.goBack();
				} else {
					finish();
				}
			}
		});

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
		System.out.println("法务咨询链接" + HttpConfig.IMAGEHOST + mUrl);
		webview.loadUrl(HttpConfig.IMAGEHOST + mUrl);
		webview.addJavascriptInterface(new DemoJavaInterface(), "control");
	}

	public class DemoJavaInterface {

		@JavascriptInterface
		public void Thephonebook() {// 打电话给管家
			mIntent = new Intent(getApplicationContext(), CallButtonActivtiy.class);
			startActivity(mIntent);
		}

		@JavascriptInterface
		public void housekeeping() {// 与管家联系
			boolean state = PreferencesUtils.getBoolean(getApplicationContext(), "isLogin");
			if (state == true) {
				mIntent = new Intent(getApplicationContext(), CallButtonActivtiy.class);
				startActivity(mIntent);
			} else {
				mIntent = new Intent(getApplicationContext(), NoteLoginActivity.class);
				mIntent.putExtra("personal", "law_works");
				startActivity(mIntent);
			}

		}
	}

	// android webview点击返回键返回上一个html
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
			webview.goBack();// 返回前一个页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
