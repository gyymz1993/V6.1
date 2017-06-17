package com.lsjr.zizisteward.common.activtiy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.OthersExampleActivity;
import com.lsjr.zizisteward.activity.ZiShangDetailActivity;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.newview.MyListView;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ZiShangSearchNickAndLabelActivity extends BaseActivity implements OnClickListener {
	private String name;
	private RelativeLayout re_parent;
	private TextView tv_name;
	private ImageView label_photo, label_photo_two;
	private MyListView listview_famous;
	private RelativeLayout more_famous_people, re_label_one, re_label_two, more_labels, iv_back;
	private TextView text, label_content, label_content_two;
	private List<OrdersEnjoy> list_orders = new ArrayList<OrdersEnjoy>();
	private List<UserEnjoy> list_users = new ArrayList<UserEnjoy>();
	private NickAdapter nick_adapter;
	private LinearLayout ll_famous_people, ll_label;
	private View view_fengexian, view_label_xian;
	private Intent intent;
	private boolean login_state;
	private int location = 0;

	@Override
	public int getContainerView() {
		return R.layout.activity_shishisearch_famous_and_label;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("孜赏搜索");
		name = getIntent().getStringExtra("name");
		iv_back = (RelativeLayout) findViewById(R.id.iv_back);// 返回键
		re_parent = (RelativeLayout) findViewById(R.id.re_parent);// 搜索框
		tv_name = (TextView) findViewById(R.id.tv_name);// 搜索框里的文字
		text = (TextView) findViewById(R.id.text);// 无搜索内容
		listview_famous = (MyListView) findViewById(R.id.listview_famous);// 名人列表
		more_famous_people = (RelativeLayout) findViewById(R.id.more_famous_people);// 更多名人
		ll_famous_people = (LinearLayout) findViewById(R.id.ll_famous_people);// 名人显示与否
		view_fengexian = findViewById(R.id.view_fengexian);// 分割线
		ll_label = (LinearLayout) findViewById(R.id.ll_label);// 标签显示与否
		re_label_one = (RelativeLayout) findViewById(R.id.re_label_one);// 标签布局1
		label_photo = (ImageView) findViewById(R.id.label_photo);// 标签图标1
		label_content = (TextView) findViewById(R.id.label_content);// 标签内容1
		re_label_two = (RelativeLayout) findViewById(R.id.re_label_two);// 标签布局2
		label_photo_two = (ImageView) findViewById(R.id.label_photo_two);// 标签图标2
		label_content_two = (TextView) findViewById(R.id.label_content_two);// 标签内容2
		more_labels = (RelativeLayout) findViewById(R.id.more_labels);// 更多标签
		view_label_xian = findViewById(R.id.view_label_xian);// 标签分割线
		TextView famous_people = (TextView) findViewById(R.id.famous_people);// 人的名称
		TextView label = (TextView) findViewById(R.id.label);// 孜赏标签
		TextView tv_more_labels = (TextView) findViewById(R.id.tv_more_labels);// 更多孜赏标签
		famous_people.setText("人的名称");
		label.setText("孜赏标签");
		tv_more_labels.setText("更多孜赏标签");
		tv_name.setText(name);
		initListener();
	}

	private void initListener() {
		more_famous_people.setOnClickListener(this);
		more_labels.setOnClickListener(this);
		re_parent.setOnClickListener(this);
		re_label_one.setOnClickListener(this);
		re_label_two.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		/* 到个人孜赏统计 */
		listview_famous.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (login_state == true) {
					intent = new Intent(getApplicationContext(), OthersExampleActivity.class);
					intent.putExtra("publish_id", list_users.get(position).getId());// 发布者id
					startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				} else {
					intent = new Intent(getApplicationContext(), NoteLoginActivity.class);
					intent.putExtra("personal", "zishangsearch");
					startActivity(intent);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		getData();
		login_state = PreferencesUtils.getBoolean(getApplicationContext(), "isLogin");
		super.onResume();
	}

	private void getData() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("OPT", "99");
		map.put("keyword", name);
		new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("孜赏搜索" + result);
				ZiShangSearchBean bean = GsonUtil.getInstance().fromJson(result, ZiShangSearchBean.class);
				list_orders = bean.getZiziEnjoy().getOrdersEnjoy();// 标签集合
				list_users = bean.getZiziEnjoy().getUserEnjoy();// 昵称集合
				if (list_orders.size() == 0 && list_users.size() == 0) {
					ll_famous_people.setVisibility(View.GONE);
					ll_label.setVisibility(View.GONE);
					view_fengexian.setVisibility(View.GONE);
					text.setVisibility(View.VISIBLE);
					text.setText("该类型的名人或孜赏标签暂时没有,请搜索其他");
				} else {
					text.setVisibility(View.GONE);
					if (list_users.size() > 0 && list_orders.size() > 0) {// 名人和标签同时有
						view_fengexian.setVisibility(View.VISIBLE);
						ll_label.setVisibility(View.VISIBLE);
						ll_famous_people.setVisibility(View.VISIBLE);
						/* 名人数据 */
						setNickData();
						/* 标签数据 */
						if (list_orders.size() == 1) {// 只有一个标签
							setFirstLaber();
						}
						if (list_orders.size() > 1) {// 多个标签
							setSecondLaber();
						}
					}
					if (list_users.size() > 0 && list_orders.size() == 0) {// 只有名人
						ll_famous_people.setVisibility(View.VISIBLE);
						ll_label.setVisibility(View.GONE);
						view_fengexian.setVisibility(View.GONE);
						setNickData();
					}
					if (list_orders.size() > 0 && list_users.size() == 0) {// 只有标签
						ll_famous_people.setVisibility(View.GONE);
						ll_label.setVisibility(View.VISIBLE);
						view_fengexian.setVisibility(View.GONE);
						if (list_orders.size() == 1) {
							setFirstLaber();
						}
						if (list_orders.size() > 1) {
							setSecondLaber();
						}
					}

				}
			}
		});
	}

	private void setFirstLaber() {
		view_label_xian.setVisibility(View.GONE);
		re_label_two.setVisibility(View.GONE);
		Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + list_orders.get(0).getSpicfirst())
				.into(label_photo);
		label_content.setText(list_orders.get(0).getCustom_tag());
	}

	private void setSecondLaber() {
		view_label_xian.setVisibility(View.VISIBLE);
		re_label_two.setVisibility(View.VISIBLE);
		Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + list_orders.get(0).getSpicfirst())
				.into(label_photo);
		label_content.setText(list_orders.get(0).getCustom_tag());
		Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + list_orders.get(1).getSpicfirst())
				.into(label_photo_two);
		label_content_two.setText(list_orders.get(1).getCustom_tag());
	}

	private void setNickData() {
		nick_adapter = new NickAdapter(ZiShangSearchNickAndLabelActivity.this, list_users);
		listview_famous.setAdapter(nick_adapter);
	}

	private class NickAdapter extends BaseAdapter {
		private Context context;
		private ViewHolder mHolder;
		private List<UserEnjoy> list_users;

		public NickAdapter(Context context, List<UserEnjoy> list_users) {
			this.context = context;
			this.list_users = list_users;
		}

		@Override
		public int getCount() {
			return list_users == null ? 0 : list_users.size();
		}

		@Override
		public Object getItem(int position) {
			return list_users.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.item_famous_search, parent, false);
				mHolder = new ViewHolder(convertView);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			mHolder.mName.setText(list_users.get(position).getUser_name());
			if ("0".equals(list_users.get(position).getCredit_level_id())) {
				mHolder.mIv_level.setImageResource(R.drawable.level_zero);
			}
			if ("1".equals(list_users.get(position).getCredit_level_id())) {
				mHolder.mIv_level.setImageResource(R.drawable.level_one);
			}
			if ("2".equals(list_users.get(position).getCredit_level_id())) {
				mHolder.mIv_level.setImageResource(R.drawable.level_two);
			}
			if ("3".equals(list_users.get(position).getCredit_level_id())) {
				mHolder.mIv_level.setImageResource(R.drawable.level_three);
			}
			if ("4".equals(list_users.get(position).getCredit_level_id())) {
				mHolder.mIv_level.setImageResource(R.drawable.level_three);
			}
			if ("5".equals(list_users.get(position).getCredit_level_id())) {
				mHolder.mIv_level.setImageResource(R.drawable.level_five);
			}
			if ("6".equals(list_users.get(position).getCredit_level_id())) {
				mHolder.mIv_level.setImageResource(R.drawable.level_six);
			}
			Glide.with(context).load(HttpConfig.IMAGEHOST + list_users.get(position).getPhoto())
					.into(mHolder.mUser_photo);
			return convertView;
		}

	}

	private class ViewHolder {
		private RoundImageView mUser_photo;
		private ImageView mIv_level;
		private TextView mName;

		public ViewHolder(View view) {
			mUser_photo = (RoundImageView) view.findViewById(R.id.user_photo);
			mIv_level = (ImageView) view.findViewById(R.id.iv_level);
			mName = (TextView) view.findViewById(R.id.name);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.re_parent:
			finish();
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.iv_back:
			finish();
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.more_famous_people:
			intent = new Intent(getApplicationContext(), ZiShangMoreNicksListAtivivty.class);
			intent.putExtra("name", name);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.more_labels:
			intent = new Intent(getApplicationContext(), ZiShangMoreLabelsListActivity.class);
			intent.putExtra("name", name);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.re_label_one:
			toZiShangDetail(0);
			break;
		case R.id.re_label_two:
			toZiShangDetail(1);
			break;
		}
	}

	private void toZiShangDetail(int item) {
		if (login_state == true) {
			intent = new Intent(getApplicationContext(), ZiShangDetailActivity.class);
			intent.putExtra("user_photo", list_orders.get(item).getPhoto());// 用户图像
			intent.putExtra("user_name", list_orders.get(item).getUser_name());// 用户名字
			intent.putExtra("user_level", list_orders.get(item).getCredit_level_id());// 用户等级图像
			intent.putExtra("faqi_number", list_orders.get(item).getTicking_number());// 发起条数
			intent.putExtra("dashang_number", list_orders.get(item).getPlay_tours());// 打赏笔数
			intent.putExtra("time", list_orders.get(item).getEnjoy_time());// 时间
			intent.putExtra("state", list_orders.get(item).getIs_finish());// 是否完成状态
			intent.putExtra("zishang_number", list_orders.get(item).getZizipeas());// 孜赏数量
			intent.putExtra("photo_number", list_orders.get(item).getEnjoyImg());// 图片数量
			intent.putExtra("custom_number", list_orders.get(item).getCustom_tag());// 标签数量
			intent.putExtra("feedback_number", list_orders.get(item).getEnjoy_ticking());// 传过去的反馈数量
			intent.putExtra("user_id", list_orders.get(item).getUser_id());// 用户id
			intent.putExtra("content", list_orders.get(item).getContent());// 评论内容
			intent.putExtra("image_size", list_orders.get(item).getImg_wh());// 图片尺寸集
			intent.putExtra("zishang_id", list_orders.get(item).getId());// 孜赏id
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		} else {
			intent = new Intent(getApplicationContext(), NoteLoginActivity.class);
			intent.putExtra("personal", "zishangsearch");
			startActivity(intent);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	class ZiShangSearchBean {
		private ZiZiEnjiy ziziEnjoy;

		public ZiZiEnjiy getZiziEnjoy() {
			return ziziEnjoy;
		}

		public void setZiziEnjoy(ZiZiEnjiy ziziEnjoy) {
			this.ziziEnjoy = ziziEnjoy;
		}
	}

	class ZiZiEnjiy {
		private List<OrdersEnjoy> ordersEnjoy;
		private List<UserEnjoy> userEnjoy;

		public List<OrdersEnjoy> getOrdersEnjoy() {
			return ordersEnjoy;
		}

		public void setOrdersEnjoy(List<OrdersEnjoy> ordersEnjoy) {
			this.ordersEnjoy = ordersEnjoy;
		}

		public List<UserEnjoy> getUserEnjoy() {
			return userEnjoy;
		}

		public void setUserEnjoy(List<UserEnjoy> userEnjoy) {
			this.userEnjoy = userEnjoy;
		}
	}

	class OrdersEnjoy {
		private String content;
		private String credit_level_id;
		private String custom_tag;
		private String enjoyImg;
		private String enjoy_ticking;
		private String enjoy_time;
		private String entityId;
		private String id;
		private String img_wh;
		private String is_dispaly;
		private String is_finish;
		private String name;
		private String persistent;
		private String photo;
		private String play_tours;
		private String spicfirst;
		private String ticking_number;
		private String user_id;
		private String user_name;
		private String zizipeas;

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

		public String getEnjoyImg() {
			return enjoyImg;
		}

		public void setEnjoyImg(String enjoyImg) {
			this.enjoyImg = enjoyImg;
		}

		public String getEnjoy_ticking() {
			return enjoy_ticking;
		}

		public void setEnjoy_ticking(String enjoy_ticking) {
			this.enjoy_ticking = enjoy_ticking;
		}

		public String getEnjoy_time() {
			return enjoy_time;
		}

		public void setEnjoy_time(String enjoy_time) {
			this.enjoy_time = enjoy_time;
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

		public String getImg_wh() {
			return img_wh;
		}

		public void setImg_wh(String img_wh) {
			this.img_wh = img_wh;
		}

		public String getIs_dispaly() {
			return is_dispaly;
		}

		public void setIs_dispaly(String is_dispaly) {
			this.is_dispaly = is_dispaly;
		}

		public String getIs_finish() {
			return is_finish;
		}

		public void setIs_finish(String is_finish) {
			this.is_finish = is_finish;
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

		public String getPlay_tours() {
			return play_tours;
		}

		public void setPlay_tours(String play_tours) {
			this.play_tours = play_tours;
		}

		public String getSpicfirst() {
			return spicfirst;
		}

		public void setSpicfirst(String spicfirst) {
			this.spicfirst = spicfirst;
		}

		public String getTicking_number() {
			return ticking_number;
		}

		public void setTicking_number(String ticking_number) {
			this.ticking_number = ticking_number;
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

		public String getZizipeas() {
			return zizipeas;
		}

		public void setZizipeas(String zizipeas) {
			this.zizipeas = zizipeas;
		}
	}

	class UserEnjoy {
		private String credit_level_id;
		private String id;
		private String identity_type;
		private String photo;
		private String user_name;

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

		public String getIdentity_type() {
			return identity_type;
		}

		public void setIdentity_type(String identity_type) {
			this.identity_type = identity_type;
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
