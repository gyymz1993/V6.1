package com.lsjr.zizisteward.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bumptech.glide.Glide;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.ZiShangDetailActivity.FeedBackPeople;
import com.lsjr.zizisteward.activity.ZiShangDetailActivity.TickingsList;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.newview.CircleImageView;
import com.lsjr.zizisteward.newview.MyGridView;
import com.lsjr.zizisteward.utils.DensityUtil;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.ToastUtils;
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FeedBackListActivity extends BaseActivity {

	private SuperListView mFeedback_list;
	private boolean isRefresh = true;
	private int pageNum = 1;
	private List<TickingsList> list = new ArrayList<TickingsList>();
	private String mPublish_id, feedback_number, zidou, zishang_id, state;
	private FeedBackListAdapter mAdapter;
	private TextView mTv_nocontent;
	private int isShang = 0;

	@Override
	public int getContainerView() {
		return R.layout.activity_feedback_list;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("反馈列表");
		Intent intent = getIntent();
		mPublish_id = intent.getStringExtra("publish_id");//用户id
		feedback_number = intent.getStringExtra("feedback_number");
		zidou = intent.getStringExtra("zidou");// 反馈打赏孜豆
		zishang_id = intent.getStringExtra("zishang_id");// 孜赏id
		state = intent.getStringExtra("state");// 孜赏状态
		TextView tv_number_title = (TextView) findViewById(R.id.tv_number_title);
		tv_number_title.setText(feedback_number + "条反馈");
		mFeedback_list = (SuperListView) findViewById(R.id.feedback_list);
		mTv_nocontent = (TextView) findViewById(R.id.tv_nocontent);// 无反馈内容提示
		mIv_back = (RelativeLayout) findViewById(R.id.iv_back);// 返回按钮
		init();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (isShang == 0) {
				finish();
			} else if (isShang == 1) {
				Intent intent = getIntent();
				FeedBackListActivity.this.setResult(33, intent);
				FeedBackListActivity.this.finish();
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void init() {
		mIv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("现在的状态" + isShang);
				if (isShang == 0) {
					finish();
				} else if (isShang == 1) {
					Intent intent = getIntent();
					FeedBackListActivity.this.setResult(33, intent);
					FeedBackListActivity.this.finish();
				}

			}
		});

		mAdapter = new FeedBackListAdapter(FeedBackListActivity.this, list);
		mFeedback_list.setAdapter(mAdapter);
		mFeedback_list.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				isRefresh = true;
				getData();

			}
		});
		mFeedback_list.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				isRefresh = false;
				getData();

			}
		});
		mFeedback_list.refresh();
	}

	private void getData() {
		if (isRefresh) {
			pageNum = 1;
			mAdapter.removeAll();
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("OPT", "81");
		map.put("currPage", String.valueOf(pageNum++));
		map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
		map.put("enjoy_id", zishang_id);
		new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("反馈者集合" + result);
				FeedBackPeople bean = GsonUtil.getInstance().fromJson(result, FeedBackPeople.class);
				if (bean.getTickings().getTotalCount() == 0) {
					mTv_nocontent.setVisibility(View.VISIBLE);
					mFeedback_list.setVisibility(View.GONE);
				} else {
					mTv_nocontent.setVisibility(View.GONE);
					mFeedback_list.setVisibility(View.VISIBLE);
					if (1 != pageNum) {
						list.addAll(bean.getTickings().getPage());
						mAdapter.setList(list);
					} else {
						list = bean.getTickings().getPage();
						mAdapter = new FeedBackListAdapter(FeedBackListActivity.this, list);
						mFeedback_list.setAdapter(mAdapter);
						mAdapter.notifyDataSetChanged();
					}

					if (list.size() < bean.getTickings().getPageSize()) {
						mFeedback_list.setIsLoadFull(false);
					}

					mFeedback_list.finishRefresh();
					mFeedback_list.finishLoadMore();
				}

			}

			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});
	}

	private String[] mImage_numbers;
	private RelativeLayout mIv_back;

	public class FeedBackListAdapter extends BaseAdapter {
		private Context context;
		private List<TickingsList> list;
		private ViewHolder mHolder;

		public FeedBackListAdapter(Context context, List<TickingsList> list) {
			this.list = list;
			this.context = context;
		}

		public void setList(List<TickingsList> list) {
			this.list = list;
		}

		public void add(TickingsList page) {
			this.list.add(page);
			notifyDataSetChanged();
		}

		public void addFirst(TickingsList page) {
			this.list.add(0, page);
			notifyDataSetChanged();
		}

		public void addAll(List<TickingsList> list) {
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_feedback_list, null);
				mHolder = new ViewHolder(convertView);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			Glide.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getPhoto()).into(mHolder.mIv_name_photo);
			mHolder.mTv_name.setText(list.get(position).getUser_name());
			if ("0".equals(list.get(position).getCredit_level_id())) {
				mHolder.mIv_level.setImageResource(R.drawable.level_one);
			}
			if ("1".equals(list.get(position).getCredit_level_id())) {
				mHolder.mIv_level.setImageResource(R.drawable.level_one);
			}
			if ("2".equals(list.get(position).getCredit_level_id())) {
				mHolder.mIv_level.setImageResource(R.drawable.level_two);
			}
			if ("3".equals(list.get(position).getCredit_level_id())) {
				mHolder.mIv_level.setImageResource(R.drawable.level_three);
			}
			if ("4".equals(list.get(position).getCredit_level_id())) {
				mHolder.mIv_level.setImageResource(R.drawable.level_three);
			}
			if ("5".equals(list.get(position).getCredit_level_id())) {
				mHolder.mIv_level.setImageResource(R.drawable.level_five);
			}
			if ("6".equals(list.get(position).getCredit_level_id())) {
				mHolder.mIv_level.setImageResource(R.drawable.level_six);
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
			String share_time = formatter.format(Long.valueOf(list.get(position).getEnjoy_time().getTime()));
			mHolder.mTv_time.setText(share_time);
			mHolder.mTv_content.setText(list.get(position).getContent());
			String enjoyImg = list.get(position).getEnjoyImg();
			mImage_numbers = enjoyImg.split(",");
			FiveGridViewAdapter adapter = new FiveGridViewAdapter(FeedBackListActivity.this, mImage_numbers);
			mHolder.mGd5_view.setAdapter(adapter);
			// if (mImage_numbers.length == 3 || mImage_numbers.length == 5 ||
			// mImage_numbers.length == 6
			// || mImage_numbers.length == 7 || mImage_numbers.length == 8 ||
			// mImage_numbers.length == 9) {
			// mHolder.mGd5_view.setNumColumns(3);
			// FiveGridViewAdapter adapter = new
			// FiveGridViewAdapter(FeedBackListActivity.this, mImage_numbers);
			// mHolder.mGd5_view.setAdapter(adapter);
			// }
			// if (mImage_numbers.length == 2 || mImage_numbers.length == 4) {
			// mHolder.mGd5_view.setNumColumns(2);
			// FiveGridViewAdapter adapter = new
			// FiveGridViewAdapter(FeedBackListActivity.this, mImage_numbers);
			// mHolder.mGd5_view.setAdapter(adapter);
			// }
			// if (mImage_numbers.length == 1) {
			// mHolder.mGd5_view.setNumColumns(1);
			// FiveGridViewAdapter adapter = new
			// FiveGridViewAdapter(FeedBackListActivity.this, mImage_numbers);
			// mHolder.mGd5_view.setAdapter(adapter);
			// }

			if ("1".equals(list.get(position).getPlay_zizipeas())) {// 已打赏
				mHolder.mRe_shang_no_click.setVisibility(View.VISIBLE);
				mHolder.mRe_shang.setVisibility(View.GONE);
				mHolder.mRe_shang_no_click.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ToastUtils.show(getApplicationContext(), "您对该反馈已打赏,不能打赏了...");
					}
				});

			} else {
				mHolder.mRe_shang_no_click.setVisibility(View.GONE);
				mHolder.mRe_shang.setVisibility(View.VISIBLE);
				// 点击打赏
				mHolder.mRe_shang.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("OPT", "79");
						map.put("user_id", EncryptUtils.addSign(Long.valueOf(list.get(position).getUser_id()), "u"));
						map.put("enjoy_id", zishang_id);
						map.put("ticking_id", list.get(position).getId());
						map.put("play_zizipeas", zidou);
						new HttpClientGet(getApplicationContext(), null, map, false,
								new HttpClientGet.CallBacks<String>() {

									@Override
									public void onSuccess(String result) {
										System.out.println("反馈孜赏结果" + result);
										BasicParameterBean bean = GsonUtil.getInstance().fromJson(result,
												BasicParameterBean.class);
										ToastUtils.show(getApplicationContext(), bean.getMsg());
										mFeedback_list.refresh();
										isShang = 1;

									}

									@Override
									public void onFailure(MyError myError) {
										super.onFailure(myError);
									}
								});

					}
				});
			}
			mHolder.mRe_call_chat.setOnClickListener(new OnClickListener() {

				@SuppressWarnings("static-access")
				@Override
				public void onClick(View v) {
					if (list.get(position).getUser_id().equals(App.getInstance().getUserInfo().getId())) {
						ToastUtils.show(getApplicationContext(), "不能跟自己聊天哦...");
					} else {
						Intent intent = new Intent(FeedBackListActivity.this, ChatActivity.class);
						intent.putExtra(Constant.EXTRA_USER_ID, list.get(position).getName());
						intent.putExtra("userId", list.get(position).getName());
						intent.putExtra("guess", false);
						startActivity(intent);
					}

				}
			});
			return convertView;
		}

	}

	public class ViewHolder {
		private MyGridView mGd5_view;// 5张图片及以上的布局
		private ImageView iv;// 图片
		private CircleImageView mIv_name_photo;// 用户图像
		private TextView mTv_name;// 用户名
		private ImageView mIv_level;// 等级图像
		private TextView mTv_time;// 时间
		private TextView mTv_content;// 内容
		private RelativeLayout mRe_call_chat;// 临时会话
		private RelativeLayout mRe_shang;// 打赏
		private RelativeLayout mRe_shang_no_click;// 不能打赏

		public ViewHolder(View view) {
			mIv_name_photo = (CircleImageView) view.findViewById(R.id.iv_name_photo);
			mTv_name = (TextView) view.findViewById(R.id.tv_name);
			mIv_level = (ImageView) view.findViewById(R.id.iv_level);
			mTv_time = (TextView) view.findViewById(R.id.tv_time);
			mTv_content = (TextView) view.findViewById(R.id.tv_content);
			mGd5_view = (MyGridView) view.findViewById(R.id.gd5_view);
			mRe_call_chat = (RelativeLayout) view.findViewById(R.id.re_call_chat);
			mRe_shang = (RelativeLayout) view.findViewById(R.id.re_shang);
			iv = (ImageView) view.findViewById(R.id.iv);
			mRe_shang_no_click = (RelativeLayout) view.findViewById(R.id.re_shang_no_click);

		}
	}

	public class FiveGridViewAdapter extends BaseAdapter {
		private Context context;
		private ViewHolder mHolder;
		private String[] mImage_numbers;

		public FiveGridViewAdapter(Context context, String[] mImage_numbers) {
			this.context = context;
			this.mImage_numbers = mImage_numbers;
		}

		@Override
		public int getCount() {
			return mImage_numbers == null ? 0 : mImage_numbers.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_five_photos, null);
				mHolder = new ViewHolder(convertView);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}

			DisplayMetrics dm = getResources().getDisplayMetrics();
			int widthPixels = dm.widthPixels;

			int dip2px = DensityUtil.dip2px(FeedBackListActivity.this, 30);
			int iv_width = (widthPixels - dip2px) / 3;
			RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) mHolder.iv.getLayoutParams();
			params1.height = iv_width;
			params1.width = iv_width;
			mHolder.iv.setLayoutParams(params1);

			// if (mImage_numbers.length == 2 || mImage_numbers.length == 4) {
			// int two_width = DensityUtil.dip2px(FeedBackListActivity.this,
			// 25);
			// int two_iv_width = (widthPixels - two_width) / 2;
			// RelativeLayout.LayoutParams params2 =
			// (RelativeLayout.LayoutParams) mHolder.iv.getLayoutParams();
			// params2.height = two_iv_width;
			// params2.width = two_iv_width;
			// mHolder.iv.setLayoutParams(params2);
			// }
			// if (mImage_numbers.length == 1) {
			// int one_width = DensityUtil.dip2px(FeedBackListActivity.this,
			// 20);
			// int one_iv_width = widthPixels - one_width;
			// RelativeLayout.LayoutParams params3 =
			// (RelativeLayout.LayoutParams) mHolder.iv.getLayoutParams();
			// params3.height = one_iv_width;
			// params3.width = one_iv_width;
			// mHolder.iv.setLayoutParams(params3);
			// }

			Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + mImage_numbers[position]).into(mHolder.iv);
			return convertView;
		}

	}

}
