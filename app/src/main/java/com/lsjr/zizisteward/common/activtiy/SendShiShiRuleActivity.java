package com.lsjr.zizisteward.common.activtiy;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpConfig;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

@SuppressLint("SetJavaScriptEnabled")
public class SendShiShiRuleActivity extends BaseActivity {
	private ProgressBar mProgressBar;

	@Override
	public int getContainerView() {
		return R.layout.activity_send_shishi_rule;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String name = getIntent().getStringExtra("name");
		if ("1".equals(name)) {
			setmTitle("孜孜管家身份认证条例");
		} else {
			setmTitle("时视发布规则");
		}
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
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
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					mProgressBar.setVisibility(View.GONE);
				} else {
					mProgressBar.setVisibility(View.VISIBLE);
					mProgressBar.setProgress(newProgress);
				}

			}
		});
		if ("1".equals(name)) {
			webview.loadUrl(HttpConfig.IMAGEHOST + "/thehousekeeperidentity");
		} else {
			webview.loadUrl(HttpConfig.IMAGEHOST + "/horizonreleaserules");
		}

	}
}
