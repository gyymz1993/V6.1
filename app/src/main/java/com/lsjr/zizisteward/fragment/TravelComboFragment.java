package com.lsjr.zizisteward.fragment;

import java.util.HashMap;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.BasicWebViewActivity;
import com.lsjr.zizisteward.activity.TaiWanHealthTravelActivity.BasicTravelBean;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;

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

//套餐
@SuppressLint("SetJavaScriptEnabled")
public class TravelComboFragment extends Fragment {
	private View rootView;
	private String mTdapid;
	private String mSpid;
	private String mTtid;
	private WebView mWebview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.activity_travel_combo, null);
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

			mSpid = PreferencesUtils.getString(getContext(), "spid");
			mTtid = PreferencesUtils.getString(getContext(), "ttid");
			mTdapid = PreferencesUtils.getString(getContext(), "tdapid");
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
				System.out.println("套餐" + result);
				BasicTravelBean bean = GsonUtil.getInstance().fromJson(result, BasicTravelBean.class);
				mWebview.loadUrl(HttpConfig.IMAGEHOST + bean.getComboDetailUrl());// 联网操作
				System.out.println("  套餐链接    " + HttpConfig.IMAGEHOST + bean.getComboDetailUrl());
				mWebview.addJavascriptInterface(new DemoJava(), "control");
			}

			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});
	}

	public class DemoJava {

		@JavascriptInterface
		public void serviceprogrammedemand(int code, String msg) {// 提交定制
			System.out.println("得到的数据" + code + msg);
			if (code == 1) {
				Intent intent = new Intent(getContext(), BasicWebViewActivity.class);
				startActivity(intent);
				getActivity().finish();
			} else {
				Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
			}

		}

		@JavascriptInterface
		public void Callthehousekeeper() {
			Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + "88300006"));
			startActivity(phoneIntent);
		}
	}

}
