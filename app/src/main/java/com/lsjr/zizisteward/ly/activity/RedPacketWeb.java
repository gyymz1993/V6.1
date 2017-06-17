package com.lsjr.zizisteward.ly.activity;

import com.hyphenate.chat.EMClient;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.InviteFriends;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpConfig;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class RedPacketWeb extends Activity {

	private WebView wv;

	private LinearLayout ll_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.red_packet_web);
		this.wv = (WebView) findViewById(R.id.wv);
		this.ll_back = (LinearLayout) findViewById(R.id.ll_back);

		WebSettings s = wv.getSettings();
		wv.setVerticalScrollBarEnabled(false);
		s.setJavaScriptEnabled(true);
		s.setSupportZoom(true);
		s.setBuiltInZoomControls(true);

		wv.setVerticalScrollBarEnabled(false);
		wv.addJavascriptInterface(new DemoJavaInterface(), "control");

		wv.loadUrl(HttpConfig.IMAGEHOST + "/envelope/Aredenvelopetoinvite?user_id=" + App.getUserInfo().getId());

		wv.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {

			}
		});

		ll_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if (wv.canGoBack()) {
				// wv.goBack();
				// } else {
				finish();
				// }
			}
		});
	}

	public class DemoJavaInterface {

		@JavascriptInterface
		public void Invitefriends() {
			// 邀请好友
			startActivity(new Intent(RedPacketWeb.this, InviteFriends.class));
		}

		@JavascriptInterface
		public void housekeeper() {
			// 呼叫管家
			// 获取聊天前 的一些参数
			if (App.getUserInfo().getUsername().equals(EMClient.getInstance().getCurrentUser())) {
				Toast.makeText(RedPacketWeb.this, R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
			} else {
				App.CallSteward(RedPacketWeb.this);
			}
		}

		@JavascriptInterface
		public void prompt(String msg) {
			// 提示
			Toast.makeText(RedPacketWeb.this, msg, 0).show();
		}

		@JavascriptInterface
		public void Return() {
			// 返回
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && wv.canGoBack()) {
			wv.goBack();// 返回前一个页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
