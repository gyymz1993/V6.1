package com.lsjr.zizisteward.ly.activity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lsjr.zizisteward.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FunctionalDevelopmentInterfaceActivity extends Activity {

	private LinearLayout ll_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.fdi_activity);
		this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
		
		this.ll_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
