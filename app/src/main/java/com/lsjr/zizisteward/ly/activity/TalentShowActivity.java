package com.lsjr.zizisteward.ly.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.hyphenate.chat.EMClient;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.BusinessPlanActivity;
import com.lsjr.zizisteward.activity.ExclusiveStewardActivity.DemoJavaInterface;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class TalentShowActivity extends Activity implements OnClickListener {

	private LinearLayout ll_back;
	private WebView wv;
	private String id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.talent_show_activity);
		this.findViewById();
	}

	private void findViewById() {
		this.wv = (WebView) super.findViewById(R.id.wv);
		this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
		
		this.ll_back.setOnClickListener(this);
		
		this.id = getIntent().getStringExtra("id");
		
		getAddress();
		
		this.wv.getSettings().setDomStorageEnabled(true);
		
		WebSettings s = wv.getSettings();
		s.setSupportZoom(true);
		s.setBuiltInZoomControls(true);
		s.setJavaScriptEnabled(true);
		
		wv.setVerticalScrollBarEnabled(false);
	}

	private void getAddress() {
		Map<String, String> map = new HashMap<>();
		map.put("OPT", "298");
		map.put("shop_id", id);
		map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
		new HttpClientGet(TalentShowActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				
				System.out.println(result);
				
				try {
					JSONObject jsonObject = new JSONObject(result);
					String cateDetailUrl = jsonObject.getString("cateDetailUrl");
					String expertUrl = jsonObject.getString("expertUrl");
					wv.loadUrl(HttpConfig.IMAGEHOST + expertUrl);
					
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
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_back:
			this.finish();
			break;
		}
	}
}
