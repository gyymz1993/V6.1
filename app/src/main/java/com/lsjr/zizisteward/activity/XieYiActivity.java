package com.lsjr.zizisteward.activity;

import java.util.HashMap;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.utils.GsonUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

@SuppressLint("SetJavaScriptEnabled")
public class XieYiActivity extends Activity {

	private WebView mWebview;
	private LinearLayout ll_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_xieyi);

		this.mWebview = (WebView) super.findViewById(R.id.webview);
		this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);

		WebSettings settings = mWebview.getSettings();
		mWebview.setVerticalScrollBarEnabled(false);
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);

		HashMap<String, String> map = new HashMap<>();
		map.put("OPT", "8");
		new HttpClientGet(XieYiActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				XieYiBean bean = GsonUtil.getInstance().fromJson(result, XieYiBean.class);
				mWebview.loadUrl(HttpConfig.IMAGEHOST + bean.getContent());
			}
		});

		this.ll_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public class XieYiBean {
		private String error;
		private String msg;
		private String content;

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}
}
