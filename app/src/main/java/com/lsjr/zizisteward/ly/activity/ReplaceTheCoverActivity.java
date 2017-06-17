package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lsjr.zizisteward.R;

public class ReplaceTheCoverActivity extends Activity implements OnClickListener {
	
	private TextView tv_book;
	private TextView tv_cancel;
	private RelativeLayout rl_parent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.replace_the_cover_activity);
		this.findViewById();
	}

	private void findViewById() {
		this.rl_parent = (RelativeLayout) super.findViewById(R.id.rl_parent);
		this.tv_book = (TextView) super.findViewById(R.id.tv_book);
		this.tv_cancel = (TextView) super.findViewById(R.id.tv_cancel);
		
		this.tv_book.setOnClickListener(this);
		this.tv_cancel.setOnClickListener(this);
		this.rl_parent.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.tv_cancel:
			rl_parent.setVisibility(View.GONE);
			finish();
			break;
			
		case R.id.rl_parent:
			rl_parent.setVisibility(View.GONE);
			finish();
			break;
		
		case R.id.tv_book:
			setResult(2);
			rl_parent.setVisibility(View.GONE);
			finish();
			break;
		}
	}
}
