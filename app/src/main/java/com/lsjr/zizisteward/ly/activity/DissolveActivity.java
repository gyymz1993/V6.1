package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.lsjr.zizisteward.R;

public class DissolveActivity extends Activity implements OnClickListener {
	private TextView tv_no;
	private TextView tv_yes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dissolve_activity);
		this.tv_no = (TextView) super.findViewById(R.id.tv_no);
		this.tv_yes = (TextView) super.findViewById(R.id.tv_yes);
		
		this.tv_no.setOnClickListener(this);
		this.tv_yes.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_no:
			
			finish();
			
			break;
			
		case R.id.tv_yes:
			
			break;
		}
		
	}
}
