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

import com.hyphenate.chat.EMClient;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.TaiWanHealthTravelActivity.BasicTravelBean;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import java.util.HashMap;

//简介
@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface", "SimpleDateFormat" })
public class AbstractFragment extends Fragment {
	private View view;
	private WebView webview;
	private String mTdapid;
	private String mSpid;
	private String mTtid;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.activity_abstract, null);
			mSpid = PreferencesUtils.getString(getContext(), "spid");
			mTtid = PreferencesUtils.getString(getContext(), "ttid");
			mTdapid = PreferencesUtils.getString(getContext(), "tdapid");
			webview = (WebView) view.findViewById(R.id.webview);
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
			getData();

		}
		return view;
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
				String introUrl = bean.getIntroUrl();
				webview.loadUrl(HttpConfig.IMAGEHOST + introUrl);// 联网操作
				webview.addJavascriptInterface(new DemoJava(), "control");

			}

			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});
	}

	public class DemoJava {

		@JavascriptInterface
		public void CallManagerPro() {
			Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + "88300006"));
			startActivity(phoneIntent);
		}

		@JavascriptInterface
		public void onlinesupport() {
			// 获取聊天前 的一些参数
			if (App.getUserInfo().getUsername().equals(EMClient.getInstance().getCurrentUser())) {
				Toast.makeText(getContext(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
			} else {
				App.CallSteward(getContext());
			}
		}

	}

}
