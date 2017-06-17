package com.lsjr.zizisteward.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.common.activtiy.ShiShiSearchFamousAndLabelActivity;
import com.lsjr.zizisteward.utils.ToastUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WorldSearchActivity extends BaseActivity implements OnClickListener {
	private EditText ed_input;
	private ImageView iv_search;

	@Override
	public int getContainerView() {
		return R.layout.activity_world_search;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("时视搜索");
		ed_input = (EditText) findViewById(R.id.ed_input);// 搜索关键字
		iv_search = (ImageView) findViewById(R.id.iv_search);// 搜索按钮
		((RelativeLayout) findViewById(R.id.iv_back)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
		});
		initListener();
	}

	private void initListener() {
		iv_search.setOnClickListener(this);
		ed_input.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					search();
					return true;
				}
				return false;
			}
		});

	}

	protected void search() {
		if (TextUtils.isEmpty(ed_input.getText().toString().trim())) {
			ToastUtils.show(getApplicationContext(), "请输入搜索关键字");
			return;
		}
		Intent intent = new Intent(getApplicationContext(), ShiShiSearchFamousAndLabelActivity.class);
		intent.putExtra("name", ed_input.getText().toString().trim());
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ed_input.setFocusable(true);
		ed_input.setFocusableInTouchMode(true);
		ed_input.requestFocus();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(ed_input, 0);
			}
		}, 100);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search:
			if (TextUtils.isEmpty(ed_input.getText().toString().trim())) {
				ToastUtils.show(getApplicationContext(), "请输入搜索关键字");
				return;
			}
			Intent intent = new Intent(getApplicationContext(), ShiShiSearchFamousAndLabelActivity.class);
			intent.putExtra("name", ed_input.getText().toString().trim());
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public class ShiShiSearchBean {
		private UsersAndOrders sights;

		public UsersAndOrders getSights() {
			return sights;
		}

		public void setSights(UsersAndOrders sights) {
			this.sights = sights;
		}
	}

	public class UsersAndOrders {
		private List<Orders> orders;
		private List<Users> users;

		public List<Orders> getOrders() {
			return orders;
		}

		public void setOrders(List<Orders> orders) {
			this.orders = orders;
		}

		public List<Users> getUsers() {
			return users;
		}

		public void setUsers(List<Users> users) {
			this.users = users;
		}
	}

	public class Orders {
		private String check_time;
		private String collect_time_uid;
		private String content;
		private String credit_level_id;
		private String custom_tag;
		private String entityId;
		private String forbid_reason;
		private String gnum;
		private String id;
		private String img_wh;
		private String is_attention;
		private String is_audit;
		private String is_collect;
		private String is_dispaly;
		private String is_give;
		private String name;
		private String persistent;
		private String photo;
		private String photo_number;
		private String reality_name;
		private String shareImg;
		private String share_comment;
		private String share_like;
		private String share_read;
		private String share_time;
		private String share_time_uid;
		private String share_type;
		private String sight_type;
		private String spicfirst;
		private String user_id;
		private String user_name;
		private String want_count;
		private String want_users;

		public String getCheck_time() {
			return check_time;
		}

		public void setCheck_time(String check_time) {
			this.check_time = check_time;
		}

		public String getCollect_time_uid() {
			return collect_time_uid;
		}

		public void setCollect_time_uid(String collect_time_uid) {
			this.collect_time_uid = collect_time_uid;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getCredit_level_id() {
			return credit_level_id;
		}

		public void setCredit_level_id(String credit_level_id) {
			this.credit_level_id = credit_level_id;
		}

		public String getCustom_tag() {
			return custom_tag;
		}

		public void setCustom_tag(String custom_tag) {
			this.custom_tag = custom_tag;
		}

		public String getEntityId() {
			return entityId;
		}

		public void setEntityId(String entityId) {
			this.entityId = entityId;
		}

		public String getForbid_reason() {
			return forbid_reason;
		}

		public void setForbid_reason(String forbid_reason) {
			this.forbid_reason = forbid_reason;
		}

		public String getGnum() {
			return gnum;
		}

		public void setGnum(String gnum) {
			this.gnum = gnum;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getImg_wh() {
			return img_wh;
		}

		public void setImg_wh(String img_wh) {
			this.img_wh = img_wh;
		}

		public String getIs_attention() {
			return is_attention;
		}

		public void setIs_attention(String is_attention) {
			this.is_attention = is_attention;
		}

		public String getIs_audit() {
			return is_audit;
		}

		public void setIs_audit(String is_audit) {
			this.is_audit = is_audit;
		}

		public String getIs_collect() {
			return is_collect;
		}

		public void setIs_collect(String is_collect) {
			this.is_collect = is_collect;
		}

		public String getIs_dispaly() {
			return is_dispaly;
		}

		public void setIs_dispaly(String is_dispaly) {
			this.is_dispaly = is_dispaly;
		}

		public String getIs_give() {
			return is_give;
		}

		public void setIs_give(String is_give) {
			this.is_give = is_give;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPersistent() {
			return persistent;
		}

		public void setPersistent(String persistent) {
			this.persistent = persistent;
		}

		public String getPhoto() {
			return photo;
		}

		public void setPhoto(String photo) {
			this.photo = photo;
		}

		public String getPhoto_number() {
			return photo_number;
		}

		public void setPhoto_number(String photo_number) {
			this.photo_number = photo_number;
		}

		public String getReality_name() {
			return reality_name;
		}

		public void setReality_name(String reality_name) {
			this.reality_name = reality_name;
		}

		public String getShareImg() {
			return shareImg;
		}

		public void setShareImg(String shareImg) {
			this.shareImg = shareImg;
		}

		public String getShare_comment() {
			return share_comment;
		}

		public void setShare_comment(String share_comment) {
			this.share_comment = share_comment;
		}

		public String getShare_like() {
			return share_like;
		}

		public void setShare_like(String share_like) {
			this.share_like = share_like;
		}

		public String getShare_read() {
			return share_read;
		}

		public void setShare_read(String share_read) {
			this.share_read = share_read;
		}

		public String getShare_time() {
			return share_time;
		}

		public void setShare_time(String share_time) {
			this.share_time = share_time;
		}

		public String getShare_time_uid() {
			return share_time_uid;
		}

		public void setShare_time_uid(String share_time_uid) {
			this.share_time_uid = share_time_uid;
		}

		public String getShare_type() {
			return share_type;
		}

		public void setShare_type(String share_type) {
			this.share_type = share_type;
		}

		public String getSight_type() {
			return sight_type;
		}

		public void setSight_type(String sight_type) {
			this.sight_type = sight_type;
		}

		public String getSpicfirst() {
			return spicfirst;
		}

		public void setSpicfirst(String spicfirst) {
			this.spicfirst = spicfirst;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public String getWant_count() {
			return want_count;
		}

		public void setWant_count(String want_count) {
			this.want_count = want_count;
		}

		public String getWant_users() {
			return want_users;
		}

		public void setWant_users(String want_users) {
			this.want_users = want_users;
		}
	}

	public class Users {
		private String credit_level_id;// 用户等级
		private String id;// 用户id
		private String photo;// 用户图像
		private String user_name;// 用户昵称

		public String getCredit_level_id() {
			return credit_level_id;
		}

		public void setCredit_level_id(String credit_level_id) {
			this.credit_level_id = credit_level_id;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getPhoto() {
			return photo;
		}

		public void setPhoto(String photo) {
			this.photo = photo;
		}

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}
	}
}
