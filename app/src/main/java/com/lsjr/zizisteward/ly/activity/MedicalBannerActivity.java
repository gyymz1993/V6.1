package com.lsjr.zizisteward.ly.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.HomeBrandDetail.DemoJavaInterface;
import com.lsjr.zizisteward.http.HttpConfig;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

@SuppressLint({ "SetJavaScriptEnabled", "NewApi", "JavascriptInterface" })
public class MedicalBannerActivity extends Activity {

	private WebView wv;
	private String url;
	private LinearLayout ll_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.medical_banner_activity);
		this.wv = (WebView) super.findViewById(R.id.wv);
		this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back); 
		this.url = getIntent().getStringExtra("url");
		
		this.wv.getSettings().setDomStorageEnabled(true);
		
		WebSettings s = wv.getSettings();
		
		s.setJavaScriptEnabled(true);
		
		this.wv.addJavascriptInterface(new H_Five_Interaction(), "control");
		
		this.wv.loadUrl(HttpConfig.Http + url);
		
		this.ll_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	public class H_Five_Interaction {
		
		public H_Five_Interaction() {
			super();
		}
		
		@JavascriptInterface
		public void Amedical(String code,String msg,String sid){
			
			startActivity(new Intent(MedicalBannerActivity.this,PhysicaDetailsActivity.class).putExtra("id", sid));
			
		}
	}
}
