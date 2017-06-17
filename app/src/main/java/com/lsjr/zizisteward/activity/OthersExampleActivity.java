package com.lsjr.zizisteward.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.ShareTime;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.newview.MyGridView;
import com.lsjr.zizisteward.utils.DensityUtil;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PullToRefreshLayout;
import com.lsjr.zizisteward.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OthersExampleActivity extends Activity implements OnClickListener {
	private MyGridView mGv_view;
	private TextView tv_name, tv_number, tv_feedback_number, tv_dashang_number, tv_call;
	private String mPublish_id;
	private RoundImageView mIv_name_photo;
	private ImageView mIv_level;
	private List<PersonalDetail> list = new ArrayList<PersonalDetail>();
	private RelativeLayout mRe_back;
	private int currentPage = 1;
	private MyAdapter mAdapter;
	private PullToRefreshLayout pull_to_refresh;
	private LinearLayout ll;
	PersonalZiShangList bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_others_example);
		Intent intent = getIntent();
		mPublish_id = intent.getStringExtra("publish_id");
		mGv_view = (MyGridView) findViewById(R.id.gv_view);// 网格布局
		mRe_back = (RelativeLayout) findViewById(R.id.re_back);
		mIv_name_photo = (RoundImageView) findViewById(R.id.iv_name_photo);
		tv_name = (TextView) findViewById(R.id.tv_name);
		mIv_level = (ImageView) findViewById(R.id.iv_level);
		tv_number = (TextView) findViewById(R.id.tv_number);
		tv_feedback_number = (TextView) findViewById(R.id.tv_feedback_number);
		tv_dashang_number = (TextView) findViewById(R.id.tv_dashang_number);
		tv_call = (TextView) findViewById(R.id.tv_call);
		pull_to_refresh = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);
		ll = (LinearLayout) findViewById(R.id.ll);

		pull_to_refresh.setOnRefreshListener(new Listener());
		mGv_view.setFocusable(false);
		getData(2);
		initListener();

	}

	private class Listener implements PullToRefreshLayout.OnRefreshListener {

		@Override
		public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
			currentPage = 1;
			getData(0);

		}

		@Override
		public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
			if (ll.getVisibility() == View.VISIBLE) {
				currentPage++;
				getData(1);
			} else {
				pull_to_refresh.loadmoreFinish(pullToRefreshLayout.SUCCEED);
			}
		}

	}

	private void initListener() {
		mRe_back.setOnClickListener(this);
		tv_call.setOnClickListener(this);
	}

	private void getData(final int mode) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("OPT", "76");
		map.put("user_id", EncryptUtils.addSign(Long.valueOf(mPublish_id), "u"));
		map.put("currPage", String.valueOf(currentPage));
		new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("个人孜赏列表" + result);
				bean = GsonUtil.getInstance().fromJson(result, PersonalZiShangList.class);
				switch (mode) {
				case 0:
					list = bean.getEnjoys().getPage();
					mAdapter = new MyAdapter(OthersExampleActivity.this, list);
					mGv_view.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
					if (null != list && list.size() > 5) {
						ll.setVisibility(View.VISIBLE);
					} else {
						ll.setVisibility(View.GONE);
					}
					pull_to_refresh.refreshFinish(pull_to_refresh.SUCCEED);
					break;
				case 1:
					if (bean.getEnjoys().getPage() != null) {
						List<PersonalDetail> list_more = new ArrayList<PersonalDetail>();
						list_more = bean.getEnjoys().getPage();

						if (list_more.size() > 0) {
							list.addAll(list_more);
							mAdapter.setList(list);
							mAdapter.notifyDataSetChanged();
						}

						if (null != list_more && list_more.size() > 5) {
							ll.setVisibility(View.VISIBLE);
						} else {
							ll.setVisibility(View.GONE);
						}

					}
					pull_to_refresh.loadmoreFinish(pull_to_refresh.SUCCEED);
					break;
				case 2:
					list = bean.getEnjoys().getPage();
					mAdapter = new MyAdapter(OthersExampleActivity.this, list);
					mGv_view.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
					if (null != list && list.size() > 5) {
						ll.setVisibility(View.VISIBLE);
					} else {
						ll.setVisibility(View.GONE);
					}

					if (bean.getCountEnjoy() != null) {
						Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + bean.getCountEnjoy().getPhoto())
								.into(mIv_name_photo);
						tv_number.setText("发起" + bean.getCountEnjoy().getCounts() + "条");
						tv_feedback_number.setText(bean.getCountEnjoy().getEnjoy_tickings() + "反馈");
						tv_dashang_number.setText(bean.getCountEnjoy().getPlayTours() + "打赏");
						tv_name.setText(bean.getCountEnjoy().getUser_name());
						if ("0".equals(bean.getCountEnjoy().getCreditLevelId())) {
							mIv_level.setImageResource(R.drawable.level_one);
						}
						if ("1".equals(bean.getCountEnjoy().getCreditLevelId())) {
							mIv_level.setImageResource(R.drawable.level_one);
						}
						if ("2".equals(bean.getCountEnjoy().getCreditLevelId())) {
							mIv_level.setImageResource(R.drawable.level_two);
						}
						if ("3".equals(bean.getCountEnjoy().getCreditLevelId())) {
							mIv_level.setImageResource(R.drawable.level_three);
						}
						if ("4".equals(bean.getCountEnjoy().getCreditLevelId())) {
							mIv_level.setImageResource(R.drawable.level_three);
						}
						if ("5".equals(bean.getCountEnjoy().getCreditLevelId())) {
							mIv_level.setImageResource(R.drawable.level_five);
						}
						if ("6".equals(bean.getCountEnjoy().getCreditLevelId())) {
							mIv_level.setImageResource(R.drawable.level_six);
						}
					}
					break;
				}

			}

			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});

	}

	public class MyAdapter extends BaseAdapter {
		private Context context;
		private List<PersonalDetail> list;
		private ViewHolder mHolder;

		public MyAdapter(Context context, List<PersonalDetail> list) {
			this.context = context;
			this.list = list;
		}

		public void setList(List<PersonalDetail> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		public void add(PersonalDetail page) {
			this.list.add(page);
			notifyDataSetChanged();
		}

		public void addFirst(PersonalDetail page) {
			this.list.add(0, page);
			notifyDataSetChanged();
		}

		public void addAll(List<PersonalDetail> list) {
			this.list.addAll(list);
			notifyDataSetChanged();
		}

		public void remove(int position) {
			this.list.remove(position);
			notifyDataSetChanged();
		}

		public void removeAll() {
			this.list.clear();
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_all_zishang_state,
						null);
				mHolder = new ViewHolder(convertView);
				convertView.setTag(mHolder);

			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			mHolder.mRe_beijing.getBackground().setAlpha(150);
			DisplayMetrics dm = getResources().getDisplayMetrics();
			int widthPixels = dm.widthPixels;
			int dip2px = DensityUtil.dip2px(OthersExampleActivity.this, 30);
			int iv_width = (widthPixels - dip2px) / 3;

			RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) mHolder.mRe_title.getLayoutParams();
			RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) mHolder.mIv_photo.getLayoutParams();
			params1.height = iv_width;
			params1.width = iv_width;
			params2.height = iv_width;
			params2.width = iv_width;
			mHolder.mRe_title.setLayoutParams(params2);
			mHolder.mIv_photo.setLayoutParams(params1);
			Glide.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getSpicfirst()).into(mHolder.mIv_photo);
			if ("0".equals(list.get(position).getIs_finish())) {
				mHolder.mTv_state.setText("未完成");
			} else {
				mHolder.mTv_state.setText("已完成");
			}
			mHolder.mTv_dashang.setText(list.get(position).getZizipeas());
			mHolder.mTv_fankui.setText(list.get(position).getEnjoy_ticking());
			return convertView;
		}

	}

	public class ViewHolder {

		private ImageView mIv_photo;
		private RelativeLayout mRe_beijing;
		private TextView mTv_state;
		private RelativeLayout mRe_title;
		private TextView mTv_fankui;
		private TextView mTv_dashang;

		public ViewHolder(View view) {
			mRe_title = (RelativeLayout) view.findViewById(R.id.re_title);// 父布局
			mIv_photo = (ImageView) view.findViewById(R.id.iv_photo);// 图片
			mRe_beijing = (RelativeLayout) view.findViewById(R.id.re_beijing);// 透明背景
			mTv_state = (TextView) view.findViewById(R.id.tv_state);// 文字状态
			mTv_fankui = (TextView) view.findViewById(R.id.tv_fankui);
			mTv_dashang = (TextView) view.findViewById(R.id.tv_dashang);

		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.re_back:
			finish();
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.tv_call:
			if (App.getUserInfo().getId().equals(mPublish_id)) {
				ToastUtils.show(getApplicationContext(), "不能与自己聊天哦...");
			} else {
				Intent intent = new Intent(OthersExampleActivity.this, ChatActivity.class);
				intent.putExtra(Constant.EXTRA_USER_ID, bean.getCountEnjoy().getName());
				intent.putExtra("userId", bean.getCountEnjoy().getName());
				intent.putExtra("guess", false);
				intent.putExtra("nike", bean.getCountEnjoy().getUser_name() + " (临时会话)");
				startActivity(intent);
			}
			break;
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

	public class PersonalZiShangList {
		private countEnjoy countEnjoy;// 个人统计字段数
		private String enjoyCount;
		private PersonalEnjoys enjoys;
		private String error;
		private String msg;

		public countEnjoy getCountEnjoy() {
			return countEnjoy;
		}

		public void setCountEnjoy(countEnjoy countEnjoy) {
			this.countEnjoy = countEnjoy;
		}

		public String getEnjoyCount() {
			return enjoyCount;
		}

		public void setEnjoyCount(String enjoyCount) {
			this.enjoyCount = enjoyCount;
		}

		public PersonalEnjoys getEnjoys() {
			return enjoys;
		}

		public void setEnjoys(PersonalEnjoys enjoys) {
			this.enjoys = enjoys;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}

	public class countEnjoy {
		private String Counts;// 总孜赏数
		private String enjoy_tickings;// 总反馈数
		private String playTours;// 总打赏数
		private String creditLevelId;// 用户等级
		private String name;// 用户账号
		private String photo;// 用户头像
		private String user_name;// 用户昵称

		public String getCreditLevelId() {
			return creditLevelId;
		}

		public void setCreditLevelId(String creditLevelId) {
			this.creditLevelId = creditLevelId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
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

		public String getPlayTours() {
			return playTours;
		}

		public void setPlayTours(String playTours) {
			this.playTours = playTours;
		}

		public String getCounts() {
			return Counts;
		}

		public void setCounts(String counts) {
			Counts = counts;
		}

		public String getEnjoy_tickings() {
			return enjoy_tickings;
		}

		public void setEnjoy_tickings(String enjoy_tickings) {
			this.enjoy_tickings = enjoy_tickings;
		}
	}

	public class PersonalEnjoys {
		private int currPage;
		private List<PersonalDetail> page;
		private int pageSize;
		private String pageTitle;
		private int totalCount;
		private int totalPageCount;

		public int getCurrPage() {
			return currPage;
		}

		public void setCurrPage(int currPage) {
			this.currPage = currPage;
		}

		public List<PersonalDetail> getPage() {
			return page;
		}

		public void setPage(List<PersonalDetail> page) {
			this.page = page;
		}

		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

		public String getPageTitle() {
			return pageTitle;
		}

		public void setPageTitle(String pageTitle) {
			this.pageTitle = pageTitle;
		}

		public int getTotalCount() {
			return totalCount;
		}

		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}

		public int getTotalPageCount() {
			return totalPageCount;
		}

		public void setTotalPageCount(int totalPageCount) {
			this.totalPageCount = totalPageCount;
		}
	}

	public class PersonalDetail {
		private String content;
		private String credit_level_id;
		private String custom_tag;
		private String enjoyImg;
		private String enjoy_ticking;// 每条孜赏的反馈数
		private ShareTime enjoy_time;
		private String entityId;
		private String id;
		private String img_wh;
		private String is_finish;
		private String persistent;
		private String photo;
		private String play_tours;
		private String spicfirst;
		private String ticking_number;
		private String user_id;
		private String user_name;
		private String zizipeas;
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
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

		public ShareTime getEnjoy_time() {
			return enjoy_time;
		}

		public void setEnjoy_time(ShareTime enjoy_time) {
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

		public String getIs_finish() {
			return is_finish;
		}

		public void setIs_finish(String is_finish) {
			this.is_finish = is_finish;
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

}
