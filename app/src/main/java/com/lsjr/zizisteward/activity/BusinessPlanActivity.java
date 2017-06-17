package com.lsjr.zizisteward.activity;

import com.hyphenate.chat.EMClient;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpConfig;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class BusinessPlanActivity extends BaseActivity {
	private WebView webview;
	private String friend_id;

	@Override
	public int getContainerView() {
		return R.layout.activity_business_plan;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String id = intent.getStringExtra("id");
		if ("0".equals(id)) {
			setmTitle("豪车");
		}
		if ("1".equals(id)) {
			setmTitle("游艇");
		}
		if ("2".equals(id)) {
			setmTitle("商务机");
		}

		webview = (WebView) findViewById(R.id.webview);
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
		if ("0".equals(id)) {
			webview.loadUrl(HttpConfig.IMAGEHOST + "/Travelsector/Luxurycars");
		}
		if ("1".equals(id)) {
			webview.loadUrl(HttpConfig.IMAGEHOST + "/Travelsector/catamaran");
		}
		if ("2".equals(id)) {
			webview.loadUrl(HttpConfig.IMAGEHOST + "/Travelsector/Businessindex");
		}

		webview.addJavascriptInterface(new DemoJavaInterface(), "control");
	}

	public class DemoJavaInterface {
		@JavascriptInterface
		public void onlinesupport() {
			if (App.getUserInfo().getUsername().equals(EMClient.getInstance().getCurrentUser())) {
				Toast.makeText(BusinessPlanActivity.this, R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
			} else {
				App.CallSteward(BusinessPlanActivity.this);
			}
		}

		@JavascriptInterface
		public void CallManagerPro() {
			// Intent phoneIntent = new Intent("android.intent.action.CALL",
			// Uri.parse("tel:" + "88300006"));
			Intent phoneIntent = new Intent(getApplicationContext(), CallButtonActivtiy.class);
			startActivity(phoneIntent);
		}
	}

}
