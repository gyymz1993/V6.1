package com.lsjr.zizisteward.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.ShareTime;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.newview.MyGridView;
import com.lsjr.zizisteward.utils.DensityUtil;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PullToRefreshLayout;
import com.lsjr.zizisteward.utils.PullableScrollView;
import com.lsjr.zizisteward.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PersonalShiJieStatisticsActivity extends Activity {
	private String mUser_id, type;
	private RoundImageView mIv_name_photo;
	private ImageView mIv_level;
	private TextView mTv_name;
	private TextView mTv_type;
	private TextView mTv_number;
	private TextView mTv_feedback_number;
	private TextView mTv_dashang_number, tv_care, tv_call;
	private MyGridView mGv_view;
	private List<SharesList> list = new ArrayList<SharesList>();
	private int pageNum = 1;
	private MyAdapter adapter;
	private PullToRefreshLayout pull_to_refresh;
	private PullableScrollView psv;
	private LinearLayout ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_shijie_statistics);
		Intent intent = getIntent();
		type = intent.getStringExtra("shijie_statistics");// 从哪里进来的字段
		mUser_id = intent.getStringExtra("user_id");
		pull_to_refresh = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);
		psv = (PullableScrollView) findViewById(R.id.psv);
		ll = (LinearLayout) findViewById(R.id.ll);
		mIv_name_photo = (RoundImageView) findViewById(R.id.iv_name_photo);
		mTv_name = (TextView) findViewById(R.id.tv_name);
		mIv_level = (ImageView) findViewById(R.id.iv_level);
		mTv_type = (TextView) findViewById(R.id.tv_type);
		mTv_number = (TextView) findViewById(R.id.tv_number);
		mTv_feedback_number = (TextView) findViewById(R.id.tv_feedback_number);
		mTv_dashang_number = (TextView) findViewById(R.id.tv_dashang_number);
		tv_care = (TextView) findViewById(R.id.tv_care);
		mGv_view = (MyGridView) findViewById(R.id.gv_view);
		RelativeLayout re_back = (RelativeLayout) findViewById(R.id.re_back);
		tv_call = (TextView) findViewById(R.id.tv_call);
		re_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mGv_view.setFocusable(false);
		pull_to_refresh.setOnRefreshListener(new Listener());

	}

	@Override
	protected void onResume() {
		getData(2);
		mGv_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (type.equals("mingrenbang")) {// 从名人榜进来的

					if ("0".equals(list.get(position).getIs_audit())) {

						if (App.getUserInfo().getId().equals(list.get(position).getUser_id())) {
							toShiJieDetail(position);
						} else {
							ToastUtils.show(getApplicationContext(), "该时视正在审核中,暂时不能观看");
						}

					} else {

						toShiJieDetail(position);

					}

				}

				if (type.equals("shishi_detail")) {// 从时视详情进来的

					if ("0".equals(list.get(position).getIs_audit())) {

						if (App.getUserInfo().getId().equals(list.get(position).getUser_id())) {
							toSheJieDetail2(position);
						} else {
							ToastUtils.show(getApplicationContext(), "该时视正在审核中,暂时不能观看");
						}

					} else {

						toSheJieDetail2(position);

					}
				}

			}

		});
		super.onResume();
	}

	@SuppressWarnings("unused")
	private void setPraise() {
		for (int i = 0; i < list.size(); i++) {
			String share_time_uid = list.get(i).getShare_time_uid();
			if (share_time_uid != null && share_time_uid.length() > 0) {
				String[] user_ids = share_time_uid.split(",");
				for (int j = 0; j < user_ids.length; j++) {
					if (App.getUserInfo().getId().equals(user_ids[j])) {
						list.get(i).setZan(true);
					} else {
						list.get(i).setZan(false);
					}
				}
			}

		}

		for (int i = 0; i < list.size(); i++) {
			String collect_time_uid = list.get(i).getCollect_time_uid();
			if (collect_time_uid != null && collect_time_uid.length() > 0) {
				String[] collect_ids = collect_time_uid.split(",");
				for (int j = 0; j < collect_ids.length; j++) {
					if (App.getUserInfo().getId().equals(collect_ids[j])) {
						list.get(i).setCollect(true);
					} else {
						list.get(i).setCollect(false);
					}
				}

			}

		}
	}

	private class Listener implements PullToRefreshLayout.OnRefreshListener {

		@Override
		public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
			pageNum = 1;
			getData(0);
		}

		@Override
		public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
			if (ll.getVisibility() == View.VISIBLE) {
				pageNum++;
				getData(1);
			} else {
				pull_to_refresh.loadmoreFinish(pullToRefreshLayout.SUCCEED);
			}
		}

	}

	private void getData(final int mode) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("OPT", "80");
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt(mUser_id), "u"));
		map.put("currPage", String.valueOf(pageNum));
		new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("视界统计" + result);
				final ShiJieStatistics bean = GsonUtil.getInstance().fromJson(result, ShiJieStatistics.class);
				switch (mode) {
				case 0:// 刷新
					list = bean.getShares().getPage();
					adapter = new MyAdapter(PersonalShiJieStatisticsActivity.this, list);
					mGv_view.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					if (null != list && list.size() > 5) {
						ll.setVisibility(View.VISIBLE);
					} else {
						ll.setVisibility(View.GONE);
					}
					pull_to_refresh.refreshFinish(pull_to_refresh.SUCCEED);
					setPraise();
					break;
				case 1:// 加载
					if (bean.getShares().getPage() != null) {
						List<SharesList> list_more = new ArrayList<SharesList>();
						list_more = bean.getShares().getPage();

						if (list_more.size() > 0) {
							list.addAll(list_more);
							adapter.setList(list);
							adapter.notifyDataSetChanged();
						}

						if (null != list_more && list_more.size() > 5) {
							ll.setVisibility(View.VISIBLE);
						} else {
							ll.setVisibility(View.GONE);
						}

					}
					pull_to_refresh.loadmoreFinish(pull_to_refresh.SUCCEED);
					setPraise();
					break;
				case 2:// 进来就加载
					mTv_number.setText("发起" + bean.getCounts().getSightCount() + "条");
					mTv_feedback_number.setText(bean.getCounts().getLikeCount() + "赞");
					mTv_dashang_number.setText(bean.getCounts().getCommentCount() + "评论");
					tv_care.setText(bean.getCounts().getIsAttentions() + "关注");
					Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + bean.getCounts().getPhoto())
							.into(mIv_name_photo);
					mTv_name.setText(bean.getCounts().getUser_name());
					if ("0".equals(bean.getCounts().getCreditLevelId())) {
						mIv_level.setImageResource(R.drawable.level_zero);
					}
					if ("1".equals(bean.getCounts().getCreditLevelId())) {
						mIv_level.setImageResource(R.drawable.level_one);
					}
					if ("2".equals(bean.getCounts().getCreditLevelId())) {
						mIv_level.setImageResource(R.drawable.level_two);
					}
					if ("3".equals(bean.getCounts().getCreditLevelId())) {
						mIv_level.setImageResource(R.drawable.level_three);
					}
					if ("4".equals(bean.getCounts().getCreditLevelId())) {
						mIv_level.setImageResource(R.drawable.level_three);
					}
					if ("5".equals(bean.getCounts().getCreditLevelId())) {
						mIv_level.setImageResource(R.drawable.level_five);
					}
					if ("6".equals(bean.getCounts().getCreditLevelId())) {
						mIv_level.setImageResource(R.drawable.level_six);
					}
					mTv_type.setText(bean.getCounts().getIdentityType());

					tv_call.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (App.getUserInfo().getId().equals(mUser_id)) {
								ToastUtils.show(getApplicationContext(), "不能与自己聊天哦...");
							} else {
								Intent intent = new Intent(PersonalShiJieStatisticsActivity.this, ChatActivity.class);

								intent.putExtra(Constant.EXTRA_USER_ID, bean.getCounts().getName());
								intent.putExtra("userId", bean.getCounts().getName());
								intent.putExtra("guess", false);
								intent.putExtra("nike", bean.getCounts().getUser_name() + " (临时会话)");
								startActivity(intent);
							}
						}
					});

					list = bean.getShares().getPage();
					adapter = new MyAdapter(PersonalShiJieStatisticsActivity.this, list);
					mGv_view.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					if (null != list && list.size() > 5) {
						ll.setVisibility(View.VISIBLE);
					} else {
						ll.setVisibility(View.GONE);
					}
					setPraise();
					break;
				}

			}
		});

	}

	private void toShiJieDetail(int position) {
		Intent intent = new Intent(getApplicationContext(), ShiJieDetailActivity.class);
		intent.putExtra("id", list.get(position).getId());
		intent.putExtra("content", list.get(position).getContent());// 内容
		intent.putExtra("photo", list.get(position).getPhoto());// 头像
		intent.putExtra("shareImg", list.get(position).getShareImg());// 图片
		intent.putExtra("time", list.get(position).getShare_time().getTime());// 时间
		intent.putExtra("user_name", list.get(position).getUser_name());// 用户名
		intent.putExtra("sight_type", list.get(position).getSight_type());// 内容类型
		intent.putExtra("custom_tag", list.get(position).getCustom_tag());// 标签内容
		intent.putExtra("zan_count", list.get(position).getShare_like());// 点赞次数
		intent.putExtra("zan_state", list.get(position).isZan());// 点赞状态
		intent.putExtra("collect_state", list.get(position).isCollect());// 收藏状态
		intent.putExtra("image_size", list.get(position).getImg_wh());// 图片尺寸
		intent.putExtra("level", list.get(position).getCredit_level_id());// 用户等级
		intent.putExtra("user_id", list.get(position).getUser_id());// 用户id
		startActivityForResult(intent, 2);
	}

	private void toSheJieDetail2(int position) {
		Intent intent = getIntent();
		intent.putExtra("id", list.get(position).getId());
		intent.putExtra("content", list.get(position).getContent());// 内容
		intent.putExtra("photo", list.get(position).getPhoto());// 头像
		intent.putExtra("shareImg", list.get(position).getShareImg());// 图片
		intent.putExtra("time", list.get(position).getShare_time().getTime());// 时间
		intent.putExtra("user_name", list.get(position).getUser_name());// 用户名
		intent.putExtra("sight_type", list.get(position).getSight_type());// 内容类型
		intent.putExtra("custom_tag", list.get(position).getCustom_tag());// 标签内容
		intent.putExtra("zan_count", list.get(position).getShare_like());// 点赞次数
		intent.putExtra("zan_state", list.get(position).isZan());// 点赞状态
		intent.putExtra("collect_state", list.get(position).isCollect());// 收藏状态
		intent.putExtra("image_size", list.get(position).getImg_wh());// 图片尺寸
		intent.putExtra("level", list.get(position).getCredit_level_id());// 用户等级
		intent.putExtra("user_id", list.get(position).getUser_id());// 用户id
		PersonalShiJieStatisticsActivity.this.setResult(222, intent);
		PersonalShiJieStatisticsActivity.this.finish();
	}

	public class MyAdapter extends BaseAdapter {
		private Context context;
		private List<SharesList> list;
		private ViewHolder mHolder;

		public MyAdapter(Context context, List<SharesList> list) {
			this.context = context;
			this.list = list;
		}

		public void setList(List<SharesList> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		public void add(SharesList list) {
			this.list.add(list);
			notifyDataSetChanged();
		}

		public void addFirst(SharesList page) {
			this.list.add(0, page);
			notifyDataSetChanged();
		}

		public void addAll(List<SharesList> list) {
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
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_all_shijie_state,
						null);
				mHolder = new ViewHolder(convertView);
				convertView.setTag(mHolder);

			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			mHolder.mRe_beijing.getBackground().setAlpha(150);
			DisplayMetrics dm = getResources().getDisplayMetrics();
			int widthPixels = dm.widthPixels;
			int dip2px = DensityUtil.dip2px(PersonalShiJieStatisticsActivity.this, 30);
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
			if (TextUtils.isEmpty(list.get(position).getCustom_tag())) {
				mHolder.mRe_beijing.setVisibility(View.GONE);
			} else {
				mHolder.mRe_beijing.setVisibility(View.VISIBLE);
				mHolder.mTv_state.setText(list.get(position).getCustom_tag());
			}
			mHolder.tv_eval.setText(list.get(position).getShare_comment());// 评论
			mHolder.tv_collect.setText(list.get(position).getShare_like());// 收藏
			if ("0".equals(list.get(position).is_audit)) {
				mHolder.shenhei.setVisibility(View.VISIBLE);
			} else {
				mHolder.shenhei.setVisibility(View.GONE);
			}
			return convertView;
		}

	}

	public class ViewHolder {

		private ImageView mIv_photo, shenhei;
		private RelativeLayout mRe_beijing;
		private TextView mTv_state;
		private RelativeLayout mRe_title;
		private TextView tv_eval;
		private TextView tv_collect;

		public ViewHolder(View view) {
			mRe_title = (RelativeLayout) view.findViewById(R.id.re_title);// 父布局
			mIv_photo = (ImageView) view.findViewById(R.id.iv_photo);// 图片
			mRe_beijing = (RelativeLayout) view.findViewById(R.id.re_beijing);// 透明背景
			mTv_state = (TextView) view.findViewById(R.id.tv_state);// 文字状态
			tv_eval = (TextView) view.findViewById(R.id.tv_eval);
			tv_collect = (TextView) view.findViewById(R.id.tv_collect);
			shenhei = (ImageView) view.findViewById(R.id.shenhei);

		}

	}

	public class ShiJieStatistics {
		private Counts counts;
		private String error;
		private String msg;
		private Shares shares;

		public Counts getCounts() {
			return counts;
		}

		public void setCounts(Counts counts) {
			this.counts = counts;
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

		public Shares getShares() {
			return shares;
		}

		public void setShares(Shares shares) {
			this.shares = shares;
		}
	}

	public class Counts {
		private String CommentCount;// 总评论数
		private String likeCount;// 总点赞次数
		private String sightCount;// 总发起数
		private String creditLevelId;
		private String identityType;
		private String isAttentions;
		private String photo;
		private String user_name;
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCreditLevelId() {
			return creditLevelId;
		}

		public void setCreditLevelId(String creditLevelId) {
			this.creditLevelId = creditLevelId;
		}

		public String getIdentityType() {
			return identityType;
		}

		public void setIdentityType(String identityType) {
			this.identityType = identityType;
		}

		public String getIsAttentions() {
			return isAttentions;
		}

		public void setIsAttentions(String isAttentions) {
			this.isAttentions = isAttentions;
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

		public String getCommentCount() {
			return CommentCount;
		}

		public void setCommentCount(String commentCount) {
			CommentCount = commentCount;
		}

		public String getLikeCount() {
			return likeCount;
		}

		public void setLikeCount(String likeCount) {
			this.likeCount = likeCount;
		}

		public String getSightCount() {
			return sightCount;
		}

		public void setSightCount(String sightCount) {
			this.sightCount = sightCount;
		}
	}

	public class Shares {
		private String currPage;
		private List<SharesList> page;
		private int pageSize;
		private String pageTitle;
		private int totalCount;
		private String totalPageCount;

		public String getCurrPage() {
			return currPage;
		}

		public void setCurrPage(String currPage) {
			this.currPage = currPage;
		}

		public List<SharesList> getPage() {
			return page;
		}

		public void setPage(List<SharesList> page) {
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

		public String getTotalPageCount() {
			return totalPageCount;
		}

		public void setTotalPageCount(String totalPageCount) {
			this.totalPageCount = totalPageCount;
		}
	}

	public class SharesList {
		private String collect_time_uid;
		private String content;
		private String credit_level_id;
		private String custom_tag;
		private String entityId;
		private String forbid_reason;
		private String gnum;
		private String id;
		private String img_wh;
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
		private ShareTime share_time;
		private String share_time_uid;
		private String share_type;
		private String sight_type;
		private String spicfirst;
		private String user_id;
		private String user_name;
		private String want_count;
		private String want_users;
		private boolean isZan;
		private boolean isCollect;

		public boolean isCollect() {
			return isCollect;
		}

		public void setCollect(boolean isCollect) {
			this.isCollect = isCollect;
		}

		public boolean isZan() {
			return isZan;
		}

		public void setZan(boolean isZan) {
			this.isZan = isZan;
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

		public ShareTime getShare_time() {
			return share_time;
		}

		public void setShare_time(ShareTime share_time) {
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

}
