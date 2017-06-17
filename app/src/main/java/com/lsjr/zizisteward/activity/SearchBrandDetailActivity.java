package com.lsjr.zizisteward.activity;

import java.util.HashMap;
import java.util.Map;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.LianJieBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class SearchBrandDetailActivity extends BaseActivity {

	private WebView webview;

	@Override
	public int getContainerView() {
		// TODO Auto-generated method stub
		return R.layout.activity_search_brand_detail;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setmTitle("商品详情");
		webview = (WebView) findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		getData();

	}

	private String productUrl;

	private void getData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("OPT", "37");
		map.put("id", App.getUserInfo().getId());
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
		map.put("sid", "42");
		new HttpClientGet(SearchBrandDetailActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("链接消息" + result);
				LianJieBean bean = GsonUtil.getInstance().fromJson(result, LianJieBean.class);
				productUrl = bean.getProductUrl();
				webview.loadUrl(HttpConfig.IMAGEHOST + productUrl);
			}

			@Override
			public void onFailure(MyError myError) {
				// TODO Auto-generated method stub
				super.onFailure(myError);
			}
		});

	}

}
