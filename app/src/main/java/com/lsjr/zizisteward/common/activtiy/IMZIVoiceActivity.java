package com.lsjr.zizisteward.common.activtiy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.ui.MainActivity;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.CallButtonActivtiy;
import com.lsjr.zizisteward.activity.SixthNewActivity;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.ShareTime;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IMZIVoiceActivity extends Activity implements OnClickListener {

	/** 翻转按钮 */
	private RelativeLayout re_change;
	/** 搜索按钮 */
	private RelativeLayout re_search;
	/** 语音留言 */
	private LinearLayout ll_voice;
	/** 呼叫管家 */
	private LinearLayout ll_call;
	/** 消息 */
	private RelativeLayout rl_msg;
	/** 消息数 */
	private static TextView tv_num;

	private RoundImageView riv_one;
	private RoundImageView riv_two;
	private RoundImageView riv_three;

	private TextView tv_one;
	private TextView tv_two;
	private TextView tv_three;

	private LinearLayout ll_one;
	private LinearLayout ll_two;
	private LinearLayout ll_three;

	private static Context context;

	private List<MsgList> list = new ArrayList<MsgList>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imzi_voice);

		context = IMZIVoiceActivity.this;
		re_change = (RelativeLayout) findViewById(R.id.re_change);
		
		re_search = (RelativeLayout) findViewById(R.id.re_search);
		ll_voice = (LinearLayout) findViewById(R.id.ll_voice);
		ll_call = (LinearLayout) findViewById(R.id.ll_call);
		rl_msg = (RelativeLayout) findViewById(R.id.rl_msg);
		tv_num = (TextView) findViewById(R.id.tv_num);
		riv_one = (RoundImageView) findViewById(R.id.riv_one);
		riv_two = (RoundImageView) findViewById(R.id.riv_two);
		riv_three = (RoundImageView) findViewById(R.id.riv_three);
		tv_one = (TextView) findViewById(R.id.tv_one);
		tv_two = (TextView) findViewById(R.id.tv_two);
		tv_three = (TextView) findViewById(R.id.tv_three);
		ll_one = (LinearLayout) findViewById(R.id.ll_one);
		ll_two = (LinearLayout) findViewById(R.id.ll_two);
		ll_three = (LinearLayout) findViewById(R.id.ll_three);

		getData();
		setNum();
		re_change.setOnClickListener(this);
		ll_voice.setOnClickListener(this);
		ll_call.setOnClickListener(this);
		rl_msg.setOnClickListener(this);

	}
	
	public static void setNum() {
		if (PreferencesUtils.getBoolean(context, "isLogin")) {
			int space = MainActivity.getUnreadMsgCountTotal();

			if (space > 0) {
				tv_num.setText(String.valueOf(space));
				tv_num.setVisibility(View.VISIBLE);
			} else {
				tv_num.setVisibility(View.INVISIBLE);
			}
		}
	}

	private void getData() {

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("OPT", "413");
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt("64"), "u"));
		new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {

				System.out.println("消息推送" + result);

				MsgBea bea = GsonUtil.getInstance().fromJson(result, MsgBea.class);
				list = bea.getMessage_records();

				if (null != list) {
					for (int i = 0; i < list.size(); i++) {
						switch (i) {
						case 0:
							ll_one.setVisibility(View.VISIBLE);
							ll_two.setVisibility(View.INVISIBLE);
							ll_three.setVisibility(View.INVISIBLE);
							setResources(list.get(0).getMessage_type(), riv_one, list.get(0).getContent(), tv_one);
							break;

						case 1:
							ll_one.setVisibility(View.INVISIBLE);
							ll_two.setVisibility(View.VISIBLE);
							ll_three.setVisibility(View.INVISIBLE);
							setResources(list.get(1).getMessage_type(), riv_two, list.get(1).getContent(), tv_two);
							break;

						case 2:
							ll_one.setVisibility(View.INVISIBLE);
							ll_two.setVisibility(View.INVISIBLE);
							ll_three.setVisibility(View.VISIBLE);
							setResources(list.get(2).getMessage_type(), riv_three, list.get(2).getContent(), tv_three);
							break;
						}
					}
				} else {
					ll_one.setVisibility(View.INVISIBLE);
					ll_two.setVisibility(View.INVISIBLE);
					ll_three.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	private void setResources(String type, RoundImageView riv, String content, TextView tv) {
		if (type.equals("1")) {
			riv.setImageResource(R.drawable.black_gray);
		} else if (type.equals("2")) {
			riv.setImageResource(R.drawable.food_gray);
		} else if (type.equals("3")) {
			riv.setImageResource(R.drawable.jiangkang_gray);
		} else if (type.equals("4")) {
			riv.setImageResource(R.drawable.huiyuan_gray);
		}

		tv.setText(content);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.re_change:
			startActivity(new Intent(getApplicationContext(), SixthNewActivity.class));
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			break;

		case R.id.ll_call:
			startActivity(new Intent(getApplicationContext(), CallButtonActivtiy.class));
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			break;

		case R.id.ll_voice:
			startActivityForResult(new Intent(IMZIVoiceActivity.this, TestVoice.class), 1);
			// overridePendingTransition(android.R.anim.fade_in,
			// android.R.anim.fade_out);
			break;

		case R.id.rl_msg:

			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {
		case 0:

			// 获取聊天前 的一些参数
			if (App.getUserInfo().getUsername().equals(EMClient.getInstance().getCurrentUser())) {
				Toast.makeText(IMZIVoiceActivity.this, R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
			} else {
				App.CallSteward(IMZIVoiceActivity.this);
			}

			break;

		}
	}

	public class MsgBea {
		private List<MsgList> message_records;

		public List<MsgList> getMessage_records() {
			return message_records;
		}

		public void setMessage_records(List<MsgList> message_records) {
			this.message_records = message_records;
		}
	}

	public class MsgList {
		private String content;
		private String entityId;
		private String id;
		private String message_type;
		private String persistent;
		private ShareTime time;
		private String user_id;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getEntityId() {
			return entityId;
		}

		public void setEntityId(String entityId) {
			this.entityId = entityId;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getMessage_type() {
			return message_type;
		}

		public void setMessage_type(String message_type) {
			this.message_type = message_type;
		}

		public String getPersistent() {
			return persistent;
		}

		public void setPersistent(String persistent) {
			this.persistent = persistent;
		}

		public ShareTime getTime() {
			return time;
		}

		public void setTime(ShareTime time) {
			this.time = time;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
	}
}
