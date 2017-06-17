package com.lsjr.zizisteward.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.LookServiceProjectActivity;
import com.lsjr.zizisteward.activity.TaiWanHealthTravelActivity.BasicTravelBean;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import java.util.HashMap;

//定制
@SuppressLint("SetJavaScriptEnabled")
public class CustomFragment extends Fragment {
	private View rootView;
	private WebView mWebview;
	private String mTdapid;
	private String mSpid;
	private String mTtid;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.activity_travel_custom, null);

			mSpid = PreferencesUtils.getString(getContext(), "spid");
			mTtid = PreferencesUtils.getString(getContext(), "ttid");
			mTdapid = PreferencesUtils.getString(getContext(), "tdapid");

			mWebview = (WebView) rootView.findViewById(R.id.webview);
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
		}
		return rootView;
	}

	private void getData() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("OPT", "272");
		map.put("ttid", mTtid);
		map.put("spid", mSpid);
		map.put("currPage", "1");
		map.put("tdapid", mTdapid);
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
		new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("第一次套餐" + result);
				BasicTravelBean bean = GsonUtil.getInstance().fromJson(result, BasicTravelBean.class);
				String customizationUrl = bean.getCustomizationUrl();
				mWebview.loadUrl(HttpConfig.IMAGEHOST + customizationUrl);
				mWebview.addJavascriptInterface(new DemoInterface(), "control");
			}

			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});
	}

	public class DemoInterface {

		@JavascriptInterface
		public void Callthehousekeeper() {
			Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + "88300006"));
			startActivity(phoneIntent);
		}

		@JavascriptInterface
		public void Submittedtothecustom(int code, String msg) {// 提交定制
			System.out.println("得到的数据" + code + msg);
			if (code == 1) {
				Intent intent = new Intent(getContext(), LookServiceProjectActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
			}

		}
	}

}
