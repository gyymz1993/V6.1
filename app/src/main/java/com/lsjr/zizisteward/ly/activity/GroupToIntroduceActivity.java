package com.lsjr.zizisteward.ly.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.http.HttpClientGet;

public class GroupToIntroduceActivity extends Activity implements
		OnClickListener {

	/** 返回键 */
	private LinearLayout ll_back;
	private EditText et_content;
	private LinearLayout ll_sure;
	private TextView tv_title;
	private String activity;
	private String Groupid;
	private String Groupmax;
	private String Groupname;
	private String Groupisowner;
	private String content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.group_to_introduce_activity);
		this.findViewById();
	}

	private void findViewById() {
		this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
		this.et_content = (EditText) super.findViewById(R.id.et_content);
		this.tv_title = (TextView) super.findViewById(R.id.tv_title);
		this.ll_sure = (LinearLayout) super.findViewById(R.id.ll_sure);

		this.ll_back.setOnClickListener(this);
		this.ll_sure.setOnClickListener(this);

		this.activity = getIntent().getStringExtra("activity");
		this.Groupid = getIntent().getStringExtra("Groupid");
		this.Groupmax = getIntent().getStringExtra("Groupmax");
		this.Groupname = getIntent().getStringExtra("Groupname");
		this.Groupisowner = getIntent().getStringExtra("Groupisowner");
		this.content = getIntent().getStringExtra("content");
		
		this.et_content.setText(content);

		if ("GAN".equals(activity)) {
			this.ll_sure.setVisibility(View.VISIBLE);
			this.et_content.setEnabled(true);
		} else {
			this.ll_sure.setVisibility(View.GONE);
			this.et_content.setEnabled(false);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.ll_back:

			finish();

			break;

		case R.id.ll_sure:
			final String description = et_content.getText().toString();

			if (description.length() > 10) {
				Map<String, String> map = new HashMap<>();
				map.put("OPT", "227");
				map.put("groupId", Groupid);
				map.put("description", description);
				map.put("owner", Groupisowner);
				map.put("groupname", Groupname);
				map.put("maxusers", Groupmax);
				
				new HttpClientGet(GroupToIntroduceActivity.this, null, map,
						false, new HttpClientGet.CallBacks<String>() {

							@Override
							public void onSuccess(String result) {
								setResult(
										3,
										getIntent().putExtra("content",
												description));
								finish();
							}
						});
			} else {
				Toast.makeText(GroupToIntroduceActivity.this, "输入简介内容过短", Toast.LENGTH_SHORT).show();
			}

			break;
		}
	}
}
