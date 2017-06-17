package com.lsjr.zizisteward.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

public class TestActivity extends Activity {

	private String content;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		// setContentView(R.layout.activity_test_push);
		// TextView text = (TextView) findViewById(R.id.text);
		final Dialog dialog = new Dialog(TestActivity.this, R.style.dialog);
		dialog.setContentView(R.layout.dialog_send_goods);
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER | Gravity.CENTER);
		TextView tv_confirm = (TextView) dialog.findViewById(R.id.tv_confirm);
		TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
		Intent intent = getIntent();
		dialog.setCancelable(false);
		if (null != intent) {
			Bundle bundle = getIntent().getExtras();
			String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
			content = bundle.getString(JPushInterface.EXTRA_ALERT);
			tv_msg.setText(content);
			tv_confirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					finish();
				}
			});
			// dialog.setOnCancelListener(new OnCancelListener() {
			//
			// @Override
			// public void onCancel(DialogInterface dialog) {
			// dialog.dismiss();
			// finish();
			// }
			// });
			dialog.show();
		}
	}

}
