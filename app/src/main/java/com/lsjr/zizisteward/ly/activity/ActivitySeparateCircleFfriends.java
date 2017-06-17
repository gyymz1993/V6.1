package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.CircleBean;
import com.lsjr.zizisteward.bean.CircleBean.Circle;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.newview.CircleImageView;
import com.lsjr.zizisteward.newview.MyGridView;
import com.lsjr.zizisteward.newview.MyListView;
import com.lsjr.zizisteward.photoflow.ImagePagerActivity;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivitySeparateCircleFfriends extends Activity implements
		OnClickListener {

	private com.lsjr.zizisteward.newview.MyListView mlv;
	private TextView tv_name;
	private CircleImageView civ;
	private LinearLayout ll_back;
	private List<Circle> circle = new ArrayList<>();
	private String user_id;
	private String name;
	private String photo;
	private SCFAdapter adapter;
	private int width = 0;
	private ScrollView sv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_separate_circle_ffriends);
		this.findViewById();
	}

	private void findViewById() {

		this.sv = (ScrollView) super.findViewById(R.id.sv);
		this.mlv = (MyListView) super.findViewById(R.id.mlv);
		this.civ = (CircleImageView) super.findViewById(R.id.civ);
		this.tv_name = (TextView) super.findViewById(R.id.tv_name);
		this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);

		this.ll_back.setOnClickListener(this);
		
		sv.smoothScrollTo(0,20);

		this.user_id = getIntent().getStringExtra("user_id");
		this.name = getIntent().getStringExtra("name");
		this.photo = getIntent().getStringExtra("photo");

		this.tv_name.setText(name);
		Picasso.with(ActivitySeparateCircleFfriends.this)
				.load(HttpConfig.IMAGEHOST + photo).into(civ);
		getData();

		this.mlv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startActivityForResult(
						new Intent(ActivitySeparateCircleFfriends.this,
								ActivityFriendsCircleDetails.class)
								.putExtra("share_id",
										circle.get(position).getId())
								.putExtra("user_id",
										circle.get(position).getUser_id())
								.putExtra("user_id", App.getUserInfo().getId())
								.putExtra("name",
										circle.get(position).getUser_name())
								.putExtra("photo",
										circle.get(position).getPhoto()), 0);
			}
		});
	}

	private void getData() {
		Map<String, String> map = new HashMap<>();
		map.put("OPT", "238");
		map.put("user_id",
				Fragment_ChatList.addSign(Long.valueOf(user_id), "u"));
		new HttpClientGet(ActivitySeparateCircleFfriends.this, null, map,
				false, new HttpClientGet.CallBacks<String>() {

					@Override
					public void onSuccess(String result) {
						System.out.println("个人朋友圈:  " + result);
						circle = new ArrayList<>();
						CircleBean cBean = new Gson().fromJson(result,
								CircleBean.class);
						circle = cBean.getCircle();
						
						DisplayMetrics dm = new DisplayMetrics();
						ActivitySeparateCircleFfriends.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
						width = dm.widthPixels;

						adapter = null;
						circle = new ArrayList<>();
						circle = cBean.getCircle();
						adapter = new SCFAdapter(ActivitySeparateCircleFfriends.this, circle);
						mlv.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(MyError myError) {
						super.onFailure(myError);
					}
				});
	}

	private class SCFAdapter extends BaseAdapter {

		private Context context;
		private ViewHolder view;
		private List<Circle> circle;
		private Circle c;
		private String[] strs;
		private LayoutParams linParams;

		public SCFAdapter(Context context, List<Circle> circle) {
			this.context = context;
			this.circle = circle;
		}

		@Override
		public int getCount() {
			return null == circle ? 0 : circle.size();
		}

		@Override
		public Object getItem(int position) {
			return circle.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (null == convertView) {

				convertView = LayoutInflater.from(context).inflate(
						R.layout.fragment_circle_friends_item_s, null);

				view = new ViewHolder(convertView);
				
				linParams = (LayoutParams) view.gv.getLayoutParams();

				convertView.setTag(view);

			} else {

				view = (ViewHolder) convertView.getTag();
			}

			// TODO 头像
			Picasso.with(context)
					.load(HttpConfig.IMAGEHOST
							+ circle.get(position).getPhoto()).into(view.c_iv);
			// TODO 内容
			view.tv_content.setText(circle.get(position).getContent());
			// TODO 用户名字
			view.tv_name.setText(circle.get(position).getUser_name());
			
			if (null != circle.get(position).getShareImg() 
					&& circle.get(position).getShareImg().length() > 1) {
				
				strs = circle.get(position).getShareImg().split(",");
				
				if (strs.length > 0) {
					
					view.gv.setVisibility(View.VISIBLE);
					
					switch (strs.length) {
					case 1:
						
						view.gv.setNumColumns(1);
						linParams.width = (width - 52) / 3;
						
						linParams.leftMargin = 45;
						view.gv.setLayoutParams(linParams);
						
						break;

					case 2:
					case 4:
						
						view.gv.setNumColumns(2);
						linParams.width = ((width - 52) / 3) * 2;
						linParams.height = (width - 52) / 3;
						linParams.leftMargin = 45;
						view.gv.setLayoutParams(linParams);
						
						break;
						
					case 3:
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
						
						view.gv.setNumColumns(3);
						linParams.width = width - 52;
						linParams.height = (width - 52) / 3;
						linParams.leftMargin = 26;
						linParams.rightMargin = 26;
						view.gv.setLayoutParams(linParams);
						
						break;
					}
					
					GVAdapter adapter = new GVAdapter(context, strs, (width - 52) / 3);

					view.gv.setAdapter(adapter);
				
					adapter.notifyDataSetChanged();
				} else {
					view.gv.setVisibility(View.GONE);
				}
				
			} else {
				view.gv.setVisibility(View.GONE);
			}

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			if (null != circle.get(position).getShare_time().getTime()) {
				Date date = new Date(Long.valueOf(circle.get(position)
						.getShare_time().getTime()));
				if (null != date) {
					view.tv_time.setText(format.format(date));
				}
			}

			if (circle.get(position).getUser_id()
					.equals(App.getUserInfo().getId())) {
				view.ll_delete.setVisibility(View.VISIBLE);
			} else {
				view.ll_delete.setVisibility(View.GONE);
			}

			view.ll_delete.setTag(position);
			view.ll_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CustomDialogUtils.startCustomProgressDialog(context, "请稍候");
					int pos = (int) v.getTag();
					Map<String, String> map = new HashMap<>();
					map.put("OPT", "239");
					map.put("share_id", circle.get(pos).getId());
					new HttpClientGet(context, null, map, false,
							new HttpClientGet.CallBacks<String>() {

								@Override
								public void onSuccess(String result) {
									CustomDialogUtils.stopCustomProgressDialog(context);
									getData();
								}

								@Override
								public void onFailure(MyError myError) {
									CustomDialogUtils.stopCustomProgressDialog(context);
									super.onFailure(myError);
								}
							});
				}
			});

			return convertView;
		}
		
		protected void imageBrower(int position, ArrayList<String> urls2) {
			Intent intent = new Intent(context, ImagePagerActivity.class);
			// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
			intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
			intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
			context.startActivity(intent);
		}

		private class ViewHolder {

			private TextView tv_name;
			private TextView tv_time;
			private TextView tv_content;
			private CircleImageView c_iv;
			private LinearLayout ll_delete;
			private MyGridView gv;
			
			public ViewHolder(View v) {
				
				this.gv = (MyGridView) v.findViewById(R.id.gv);
				
				this.c_iv = (CircleImageView) v.findViewById(R.id.c_iv);
				this.tv_name = (TextView) v.findViewById(R.id.tv_name);
				this.tv_time = (TextView) v.findViewById(R.id.tv_time);
				this.tv_content = (TextView) v.findViewById(R.id.tv_content);
				this.ll_delete = (LinearLayout) v.findViewById(R.id.ll_delete);
			}
		}
	}

	private class GVAdapter extends BaseAdapter {

		private String[] path;
		private Context context;
		private ViewHolder view;
		private int width;
	    private ArrayList<String> imageUrls;

		public GVAdapter(Context context, String[] path, int width) {
			this.context = context;
			this.width = width;
			this.path = path;
		}

		@Override
		public int getCount() {
			return null == path ? 0 : path.length;
		}

		@Override
		public Object getItem(int position) {
			return path[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (null == convertView) {

				convertView = LayoutInflater.from(context).inflate(
						R.layout.fcf_gv_item, null);

				view = new ViewHolder(convertView);
				
				LayoutParams linParams = (LayoutParams) view.iv.getLayoutParams();
				
				linParams.height = width;
				
				view.iv.setLayoutParams(linParams);

				convertView.setTag(view);

			} else {

				view = (ViewHolder) convertView.getTag();

			}
			
			 Picasso.with(context).load(HttpConfig.IMAGEHOST + path[position]).into(view.iv);
			 
			 imageUrls = new ArrayList<>(Arrays.asList(path));
			 
			 view.iv.setTag(position);
			 view.iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int pos = (int) v.getTag();
					imageBrower(pos, imageUrls);
				}
			});

			return convertView;
		}
		
		protected void imageBrower(int position, ArrayList<String> urls2) {
			Intent intent = new Intent(context, ImagePagerActivity.class);
			intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
			intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
			context.startActivity(intent);
		}

		private class ViewHolder {

			private ImageView iv;

			public ViewHolder(View v) {
				this.iv = (ImageView) v.findViewById(R.id.iv);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		}
	}
}
