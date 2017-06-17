package com.lsjr.zizisteward.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
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
public class TravelWebViewActivity extends BaseActivity {

	private WebView mWebview;
	private String mUrl, title;
	private RelativeLayout mIv_back;
	private ProgressBar mProgressBar1;

	@Override
	public int getContainerView() {
		return R.layout.activity_travel_webview;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mProgressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		mWebview = (WebView) findViewById(R.id.webview);
		mIv_back = (RelativeLayout) findViewById(R.id.iv_back);
		mUrl = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		if (title.equals("travel")) {
			setmTitle("出行");
		}
		if (title.equals("home")) {
			setmTitle("孜孜生活");
		}
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

	public class DemoJavaInterface {

		private Intent mIntent;

		@JavascriptInterface
		public void travelbanner(String sid) {// 套餐详情
			Intent intent = new Intent(getApplicationContext(), TaoCanDetailActivity.class);
			intent.putExtra("sid", sid);
			startActivity(intent);
		}

		@JavascriptInterface
		public void Experiencecentre(String url) {// 体验中心
			Intent intent = new Intent(getApplicationContext(), ExperienceActivity.class);
			intent.putExtra("url", url);
			startActivity(intent);
		}

		@JavascriptInterface
		public void Contacthousekeeping() {// 联系管家
			boolean state = PreferencesUtils.getBoolean(TravelWebViewActivity.this, "isLogin");
			if (state == true) {
				mIntent = new Intent(getApplicationContext(), CallButtonActivtiy.class);
				startActivity(mIntent);
			} else {
				mIntent = new Intent(TravelWebViewActivity.this, NoteLoginActivity.class);
				mIntent.putExtra("personal", "webview");
				startActivity(mIntent);
			}

		}

		@JavascriptInterface
		public void Goodsdetails(String shopid) {
			startActivity(new Intent(getApplicationContext(), HomeBrandDetail.class).putExtra("sid", shopid));
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
