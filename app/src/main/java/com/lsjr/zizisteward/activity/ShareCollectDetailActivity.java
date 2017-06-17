package com.lsjr.zizisteward.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.newview.MyListView;
import com.lsjr.zizisteward.utils.GsonUtil;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class ShareCollectDetailActivity extends BaseActivity {

	private TextView tv_name;
	private TextView mTv_time;
	private RelativeLayout mIv_delete;
	private MyListView mListview;
	private String mTitle;
	private String mTime;
	private String mPhotoUrl;
	private String mImgWidth, id;
	private String[] mImgs;
	private String[] mImg_widths;
	private ImageAdapter mAdapter;
	private RelativeLayout mIv_back;
	private int mWidthPixels;

	@Override
	public int getContainerView() {
		return R.layout.activity_share_detail;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("收藏详情");
		Intent intent = getIntent();
		mTitle = intent.getStringExtra("title");
		mTime = intent.getStringExtra("time");
		mPhotoUrl = intent.getStringExtra("photoUrl");
		mImgWidth = intent.getStringExtra("imgWidth");
		id = intent.getStringExtra("id");
		mIv_back = (RelativeLayout) findViewById(R.id.iv_back);
		tv_name = (TextView) findViewById(R.id.tv_name);
		mTv_time = (TextView) findViewById(R.id.tv_time);
		mIv_delete = (RelativeLayout) findViewById(R.id.iv_delete);
		mListview = (MyListView) findViewById(R.id.listview);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		String share_time = formatter.format(Long.valueOf(mTime));
		mTv_time.setText(share_time);
		tv_name.setText(mTitle);
		mImgs = mPhotoUrl.split(",");
		mImg_widths = mImgWidth.split(",");
		mAdapter = new ImageAdapter(ShareCollectDetailActivity.this, mImgs);
		mListview.setAdapter(mAdapter);
		mIv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
		});
		mIv_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(ShareCollectDetailActivity.this, R.style.dialog);
				dialog.setContentView(R.layout.popup_delete_address);
				Window window = dialog.getWindow();
				window.setGravity(Gravity.CENTER | Gravity.CENTER);
				TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
				tv_msg.setText("确定删除该收藏吗?");
				((TextView) dialog.findViewById(R.id.tv_cancel)).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				((TextView) dialog.findViewById(R.id.tv_confirm)).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						Map<String, String> map = new HashMap<String, String>();
						map.put("OPT", "74");
						map.put("groupS_id", id);
						new HttpClientGet(ShareCollectDetailActivity.this, null, map, false,
								new HttpClientGet.CallBacks<String>() {

									@Override
									public void onSuccess(String result) {
										BasicParameterBean bean = GsonUtil.getInstance().fromJson(result,
												BasicParameterBean.class);
										Toast.makeText(ShareCollectDetailActivity.this, bean.getMsg(),
												Toast.LENGTH_SHORT).show();
										finish();

									}

									@Override
									public void onFailure(MyError myError) {
										super.onFailure(myError);
									}
								});
					}
				});

				dialog.show();
			}
		});
		DisplayMetrics dm = getResources().getDisplayMetrics();
		mWidthPixels = dm.widthPixels;
	}

	public class ImageAdapter extends BaseAdapter {
		private Context context;
		private String[] mImgs;
		private ViewHolder mHolder;

		public ImageAdapter(Context context, String[] mImgs) {
			this.context = context;
			this.mImgs = mImgs;
		}

		@Override
		public int getCount() {
			return mImgs == null ? 0 : mImgs.length;
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
				mHolder = new ViewHolder(convertView);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}

			RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) mHolder.mIv.getLayoutParams();
			double space = 0;
			double _with = Double.valueOf(mImg_widths[position * 2]);
			double _height = Double.valueOf(mImg_widths[position * 2 + 1]);
			space = (mWidthPixels) / _with;
			linearParams.width = mWidthPixels;
			linearParams.height = (int) (_height * space);
			mHolder.mIv.setLayoutParams(linearParams);
			Glide.with(context).load(HttpConfig.IMAGEHOST + mImgs[position]).into(mHolder.mIv);

			return convertView;
		}

	}

	public class ViewHolder {
		private ImageView mIv;

		public ViewHolder(View view) {
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
