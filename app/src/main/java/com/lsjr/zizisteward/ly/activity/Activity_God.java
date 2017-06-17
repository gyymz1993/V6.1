package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lsjr.zizisteward.R;

public class Activity_God extends Activity {
	
	private int God;
	private ImageView iv_state;
	private RelativeLayout rl_parent;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			rl_parent.setVisibility(View.GONE);
			Fragment_AddressBook.requestAddressBook();
			finish();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_friends_successful);

		God = getIntent().getIntExtra("god", 0);
		
		this.iv_state = (ImageView) super.findViewById(R.id.iv_state);
		
		this.rl_parent = (RelativeLayout) super.findViewById(R.id.rl_parent);
		
		if ( 0 == God ) {
			this.iv_state.setImageResource(R.drawable.icon_add_successful);
		}else {
			this.iv_state.setImageResource(R.drawable.icon_add_failure);
		}
		
		handler.postDelayed(null, 2000);
	}
}
