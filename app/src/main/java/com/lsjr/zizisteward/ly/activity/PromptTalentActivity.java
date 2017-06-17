package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.lsjr.zizisteward.R;

public class PromptTalentActivity extends Activity {

	private TextView tv_sure;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.prompt_talent_activity);
		this.tv_sure = (TextView) super.findViewById(R.id.tv_sure);
		
		this.tv_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
