package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lsjr.zizisteward.R;

public class ReservationSuccessTips extends Activity {

	private TextView tv_sure;
	private LinearLayout ll_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.reservation_success_tips);
		this.tv_sure = (TextView) super.findViewById(R.id.tv_sure);
		this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
		
		this.tv_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(1);
				finish();
			}
		});
		
		this.ll_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(1);
				finish();
			}
		});
	}
}
