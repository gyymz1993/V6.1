package com.lsjr.zizisteward.ly.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DeleteActivity extends Activity implements OnClickListener {

	private LinearLayout ll_dismiss;
	private TextView tv_delete;
	private TextView tv_cancel;
	private String user_id;
	private String friend_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delete_activity);
		
		this.ll_dismiss = (LinearLayout) super.findViewById(R.id.ll_dismiss);
		this.tv_delete = (TextView) super.findViewById(R.id.tv_delete);
		this.tv_cancel = (TextView) super.findViewById(R.id.tv_cancel);
		
		user_id = getIntent().getStringExtra("user_id");
		friend_id = getIntent().getStringExtra("friend_id");
		
		this.ll_dismiss.setOnClickListener(this);
		this.tv_delete.setOnClickListener(this);
		this.tv_cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.ll_dismiss:
			tv_cancel.setVisibility(View.GONE);
			tv_delete.setVisibility(View.GONE);
			finish();
			break;
			
		case R.id.tv_cancel:
			tv_cancel.setVisibility(View.GONE);
			tv_delete.setVisibility(View.GONE);
			finish();
			break;
			
		case R.id.tv_delete:
			
			CustomDialogUtils.startCustomProgressDialog(DeleteActivity.this, "请稍等...");
			
			Map<String, String> map = new HashMap<>();
			map.put("OPT", "207");
			map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
			map.put("delete_id", friend_id);
			
			new HttpClientGet(DeleteActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

						@Override
						public void onSuccess(String result) {
							System.out.println("删除好友:" + result);
							try {
								JSONObject jObject = new JSONObject(result);
								String error = jObject.getString("error");
								if ("1".equals(error)) {
									CustomDialogUtils.stopCustomProgressDialog(DeleteActivity.this);
									setResult(2);
									finish();
								}
							} catch (JSONException e) {
								CustomDialogUtils.stopCustomProgressDialog(DeleteActivity.this);
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(MyError myError) {
							CustomDialogUtils.stopCustomProgressDialog(DeleteActivity.this);
							super.onFailure(myError);
						}
					});
			break;
		}
	}
}
