package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.TimeBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.share.OnekeyShare;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;

@SuppressLint("SetJavaScriptEnabled")
public class TimeActivity extends Activity implements OnClickListener {
	private WebView mWebview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time);
		mWebview = (WebView) findViewById(R.id.webview);
		mShare = (RelativeLayout) findViewById(R.id.share);
		mBack = (RelativeLayout) findViewById(R.id.back);
		WebSettings settings = mWebview.getSettings();
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setJavaScriptEnabled(true);
		getData();
		initLayout();
	}

	private void initLayout() {
		mBack.setOnClickListener(this);
		mShare.setOnClickListener(this);

	}

	private void getData() {
		HashMap<String, String> map = new HashMap<>();
		map.put("OPT", "44");
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
		new HttpClientGet(TimeActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("时光轴" + result);
				TimeBean bean = GsonUtil.getInstance().fromJson(result, TimeBean.class);
				mWebview.loadUrl(HttpConfig.IMAGEHOST + bean.getFusionUrl());
				System.out.println("时光轴链接" + HttpConfig.IMAGEHOST + bean.getFusionUrl());
				mWebview.addJavascriptInterface(new DemoJavaInterface(), "control");

			}

			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});

	}

	private String urls;
	private RelativeLayout mBack;
	private RelativeLayout mShare;

	public class DemoJavaInterface {

		@JavascriptInterface
		public void Sharethepath(String url) {
			urls = url;
			System.out.println("分享链接" + urls);

		}
	}

	private String OriginalImge; // 商品名称和价格

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.share:
			if ("/fusions?axisid=".equals(urls) || null == urls) {
				Toast.makeText(TimeActivity.this, "分享前,请选择时光轴并点击完成", Toast.LENGTH_SHORT).show();
				return;
			}
			ShareSDK.initSDK(TimeActivity.this);
			OnekeyShare oks = new OnekeyShare();
			oks.disableSSOWhenAuthorize();
			String productUrl = HttpConfig.IMAGEHOST + urls;
			oks.setTitle("分享到");
			oks.setTitleUrl(productUrl);
			oks.setText(productUrl);
			// oks.setImagePath("/sdcard/logods.jpg");//确保sdcard 存在图片
			oks.setUrl(productUrl);
			oks.setSite("孜孜管家");
			oks.setSiteUrl(productUrl);
			oks.show(TimeActivity.this);
			break;
		}
	}
}
