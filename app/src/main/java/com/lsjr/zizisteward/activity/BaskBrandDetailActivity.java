package com.lsjr.zizisteward.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpConfig;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class BaskBrandDetailActivity extends BaseActivity {

	@Override
	public int getContainerView() {
		return R.layout.activity_search_brand_detail;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String mShopUrl = getIntent().getStringExtra("mShopUrl");
		setmTitle("商品详情");
		WebView webview = (WebView) findViewById(R.id.webview);
		WebSettings settings = webview.getSettings();
		webview.setVerticalScrollBarEnabled(false);
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		webview.addJavascriptInterface(new DemoJavaInterface(), "control");
		webview.loadUrl(HttpConfig.IMAGEHOST + mShopUrl);
		System.out.println("号码" + HttpConfig.IMAGEHOST + mShopUrl);

	}

	private String url;

	public class DemoJavaInterface {
		@JavascriptInterface
		public void housekeeper() {

			Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + "4001132121"));
			startActivity(phoneIntent);

		}

		@JavascriptInterface
		public void sendUrls(String urls) {
			url = urls;
			Intent intent = new Intent(BaskBrandDetailActivity.this, NewJARActivty.class);
			intent.putExtra("url", url);
			startActivity(intent);
			finish();
		}

	}
}
