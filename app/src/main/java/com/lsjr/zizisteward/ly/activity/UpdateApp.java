package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lsjr.zizisteward.R;

public class UpdateApp extends Activity {
	
	private TextView tv_sure;
	private TextView tv_cancel;
	private int resolution;
	private String url;
	private LinearLayout ll_cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.update_app);
		
		tv_sure = (TextView) findViewById(R.id.tv_sure);
		tv_cancel = (TextView) findViewById(R.id.tv_cancel);
		ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
		
		resolution = getIntent().getIntExtra("resolution", 7);
		
		url = getIntent().getStringExtra("url");
		
		switch (resolution) {
		case 0:
			ll_cancel.setVisibility(View.GONE);
			break;

		case 1:
			ll_cancel.setVisibility(View.VISIBLE);
			break;
		}
		
		tv_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(1);
				finish();
			}
		});
		
		tv_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent();  
				intent.setAction("android.intent.action.VIEW");  
				Uri content_url = Uri.parse(url); 
				intent.setData(content_url);  
				startActivity(intent);
				
//				setResult(0);
//				finish();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		 if (keyCode == KeyEvent.KEYCODE_BACK ) {
			 return false;
		 }
			 
		return true;
	}
}

