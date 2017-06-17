package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

@SuppressLint("SetJavaScriptEnabled")
public class TaoCanDetailActivity extends BaseActivity {
	private WebView mWebview;
	private String sid;
	private RelativeLayout mIv_back;

	@Override
	public int getContainerView() {
		return R.layout.activity_travel_webview;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("出行");
		mWebview = (WebView) findViewById(R.id.webview);
		mIv_back = (RelativeLayout) findViewById(R.id.iv_back);
		sid = getIntent().getStringExtra("sid");
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
		getData();
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

	private void getData() {
		HashMap<String, String> map = new HashMap<>();
		map.put("OPT", "273");
		map.put("spid", sid);
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
		new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					String comboDetailUrl = jsonObject.getString("comboDetailUrl");
					mWebview.loadUrl(HttpConfig.IMAGEHOST + comboDetailUrl);
					mWebview.addJavascriptInterface(new DemoJavaInterface(), "control");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});

	}

	public class DemoJavaInterface {

		@JavascriptInterface
		public void serviceprogrammedemand(int code, String msg) {// 提交定制
			System.out.println("得到的数据" + code + msg);
			if (code == 1) {
				Intent intent = new Intent(getApplicationContext(), BasicWebViewActivity.class);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}

		}

		@JavascriptInterface
		public void Callthehousekeeper() {
			Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + "88300006"));
			startActivity(phoneIntent);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
			mWebview.goBack();// 返回前一个页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
