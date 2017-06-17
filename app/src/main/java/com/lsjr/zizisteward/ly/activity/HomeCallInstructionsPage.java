package com.lsjr.zizisteward.ly.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class HomeCallInstructionsPage extends Activity {

	private ImageView iv_one;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.home_call_instructions_page);
		this.iv_one = (ImageView) super.findViewById(R.id.iv_one);
		
		PreferencesUtils.putBoolean(HomeCallInstructionsPage.this, "isHCIP", true);
		
		this.iv_one.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
