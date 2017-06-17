package com.lsjr.zizisteward.activity;

import java.text.SimpleDateFormat;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.newview.CircleImageView;
import com.lsjr.zizisteward.newview.MyListView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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

public class CollectDetailActivity extends BaseActivity {
	private String content, photo, shareImg, time, user_name, custom_tag, level, size;
	private CircleImageView mYouliao_yuantu;
	private TextView mTv_name;
	private ImageView mIv_level;
	private TextView mTv_time;
	private String[] mImags;
	private TextView mTv_label;
	private TextView mTv_content;
	private ImageView mIv_eval;
	private RelativeLayout mIv_back;
	private MyListView mIv_listview;
	private String[] mSize_imgs;
	private int mWidthPixels;

	@Override
	public int getContainerView() {
		return R.layout.activity_collect_detail;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("收藏详情");
		mIv_back = (RelativeLayout) findViewById(R.id.iv_back);
		content = getIntent().getStringExtra("content");
		photo = getIntent().getStringExtra("photo");
		shareImg = getIntent().getStringExtra("shareImg");
		time = getIntent().getStringExtra("time");
		user_name = getIntent().getStringExtra("user_name");
		custom_tag = getIntent().getStringExtra("custom_tag");
		level = getIntent().getStringExtra("level");
		size = getIntent().getStringExtra("size");

		mYouliao_yuantu = (CircleImageView) findViewById(R.id.youliao_yuantu);
		mTv_name = (TextView) findViewById(R.id.tv_name);
		mIv_level = (ImageView) findViewById(R.id.iv_level);
		mTv_time = (TextView) findViewById(R.id.tv_time);
		mIv_listview = (MyListView) findViewById(R.id.iv_listview);

		DisplayMetrics dm = getResources().getDisplayMetrics();
		mWidthPixels = dm.widthPixels;

		mTv_label = (TextView) findViewById(R.id.tv_label);// 自定义标签
		mTv_content = (TextView) findViewById(R.id.tv_content);// 用户发送的世界内容

		mImags = shareImg.split(",");
		mSize_imgs = size.split(",");

		mIv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
		});
		init();

	}

	/**
	 * 赋值
	 */
	private void init() {
		ImageAdapter adapter = new ImageAdapter(CollectDetailActivity.this, mImags);
		mIv_listview.setAdapter(adapter);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		String share_time = formatter.format(Long.valueOf(time));
		mTv_time.setText(share_time);
		Glide.with(CollectDetailActivity.this).load(HttpConfig.IMAGEHOST + photo).into(mYouliao_yuantu);
		mTv_name.setText(user_name);

		if (level.equals("0")) {
			mIv_level.setImageResource(R.drawable.level_zero);
		} else if (level.equals("1")) {
			mIv_level.setImageResource(R.drawable.level_one);
		} else if (level.equals("2")) {
			mIv_level.setImageResource(R.drawable.level_two);
		} else if (level.equals("3")) {
			mIv_level.setImageResource(R.drawable.level_three);
		} else if (level.equals("6")) {
			mIv_level.setImageResource(R.drawable.level_six);
		} else if (level.equals("5")) {
			mIv_level.setImageResource(R.drawable.level_five);
		} else if (level.equals("4")) {
			mIv_level.setImageResource(R.drawable.level_three);
		}

		if (TextUtils.isEmpty(custom_tag)) {
			mTv_label.setVisibility(View.GONE);
		} else {
			mTv_label.setVisibility(View.VISIBLE);
			mTv_label.setText(custom_tag);
		}
		mTv_content.setText(content);

	}

	public class ImageAdapter extends BaseAdapter {
		private Context context;
		private String[] mImags;
		private Viewholdr mHolder;

		public ImageAdapter(Context context, String[] mImags) {
			this.context = context;
			this.mImags = mImags;
		}

		@Override
		public int getCount() {
			return mImags == null ? 0 : mImags.length;
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
				convertView = LayoutInflater.from(context).inflate(R.layout.item_share_collect_detail, null);
				mHolder = new Viewholdr(convertView);
				convertView.setTag(mHolder);
			} else {
				mHolder = (Viewholdr) convertView.getTag();
			}
			RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) mHolder.mIv.getLayoutParams();
			double space = 0;
			double _with = Double.valueOf(mSize_imgs[position * 2]);
			double _height = Double.valueOf(mSize_imgs[position * 2 + 1]);
			space = (mWidthPixels) / _with;
			linearParams.width = mWidthPixels;
			linearParams.height = (int) (_height * space);
			mHolder.mIv.setLayoutParams(linearParams);
			Glide.with(context).load(HttpConfig.IMAGEHOST + mImags[position]).into(mHolder.mIv);
			return convertView;
		}

	}

	public class Viewholdr {
		private ImageView mIv;

		public Viewholdr(View view) {
			mIv = (ImageView) view.findViewById(R.id.iv);
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

}