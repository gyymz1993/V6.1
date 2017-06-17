package com.lsjr.zizisteward.activity;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.BaseViewHolder;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;
import com.lsjr.zizisteward.newview.MyListView;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TravelActivity extends BaseActivity implements OnClickListener {

	private ImageView mIv_order_ticket;
	private ImageView mIv_order_hotel;
	private ImageView mIv_car_service;
	private ImageView mIv_travel_department;
	private ImageView mIv_deep_travel;
	private MyListView mListview;
	private Intent mIntent;

	@Override
	public int getContainerView() {
		return R.layout.activity_travel;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmRight("定制");
		setmTitle("出行");
		mIv_order_ticket = (ImageView) findViewById(R.id.iv_order_ticket);
		mIv_order_hotel = (ImageView) findViewById(R.id.iv_order_hotel);
		mIv_car_service = (ImageView) findViewById(R.id.iv_car_service);
		mIv_travel_department = (ImageView) findViewById(R.id.iv_travel_department);
		mIv_deep_travel = (ImageView) findViewById(R.id.iv_deep_travel);
		mListview = (MyListView) findViewById(R.id.listview);
		MyAdapter adapter = new MyAdapter();
		mListview.setAdapter(adapter);

		init();
		((RelativeLayout) findViewById(R.id.re_right)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {// 判断是否登录
				boolean state2 = PreferencesUtils.getBoolean(TravelActivity.this, "isLogin");
				System.out.println("现在状态" + state2);
				if (state2 == true) {
					mIntent = new Intent(TravelActivity.this, CustomTravelActivity.class);
					startActivity(mIntent);
				} else if (state2 == false) {
					mIntent = new Intent(TravelActivity.this, NoteLoginActivity.class);
					mIntent.putExtra("personal", "custom_travel");
					startActivity(mIntent);
				}

			}
		});

		new Handler().postDelayed(new Runnable() {
			public void run() {
				final Dialog dialog = new Dialog(TravelActivity.this, R.style.dialog);
				dialog.setContentView(R.layout.dialog_travel);
				Window window = dialog.getWindow();
				window.setGravity(Gravity.CENTER | Gravity.CENTER);
				ImageView tv_jinru_travel = (ImageView) dialog.findViewById(R.id.tv_jinru_travel);
				TextView tv_call = (TextView) dialog.findViewById(R.id.tv_call);
				TextView tv_chat = (TextView) dialog.findViewById(R.id.tv_chat);
				TextView tv_travel = (TextView) dialog.findViewById(R.id.tv_travel);
				tv_call.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + "88300006"));
						startActivity(phoneIntent);
					}
				});

				tv_chat.setOnClickListener(new OnClickListener() {// 在线吩咐

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						if (PreferencesUtils.getBoolean(TravelActivity.this, "isLogin")) {
							// 获取聊天前 的一些参数
							if (App.getUserInfo().getUsername().equals(EMClient.getInstance().getCurrentUser())) {
								Toast.makeText(TravelActivity.this, R.string.Cant_chat_with_yourself,
										Toast.LENGTH_SHORT).show();
							} else {
								App.CallSteward(TravelActivity.this);
							}
						} else {
							Intent intent = new Intent(TravelActivity.this, NoteLoginActivity.class);
							intent.putExtra("personal", "online_travel");
							startActivity(intent);
							TravelActivity.this.finish();
						}
					}
				});

				tv_travel.setOnClickListener(new OnClickListener() {// 定制出行

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						boolean state3 = PreferencesUtils.getBoolean(TravelActivity.this, "isLogin");
						if (state3 == true) {
							mIntent = new Intent(TravelActivity.this, CustomTravelActivity.class);
							startActivity(mIntent);
						} else if (state3 == false) {
							mIntent = new Intent(TravelActivity.this, NoteLoginActivity.class);
							mIntent.putExtra("personal", "custom_travel");
							startActivity(mIntent);
						}

					}
				});
				tv_jinru_travel.setOnClickListener(new OnClickListener() {// 点击×

					@Override
					public void onClick(View v) {
						dialog.dismiss();

					}
				});
				dialog.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						dialog.dismiss();

					}
				});
				dialog.show();
			}
		}, 300);
	}

	private void init() {
		mIv_order_ticket.setOnClickListener(this);
		mIv_order_hotel.setOnClickListener(this);
		mIv_car_service.setOnClickListener(this);
		mIv_travel_department.setOnClickListener(this);
		mIv_deep_travel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_order_ticket:// 判断是否登录
			if (PreferencesUtils.getBoolean(TravelActivity.this, "isLogin")) {
				mIntent = new Intent(getApplicationContext(), OrderTicketServiceActivity.class);// 订票服务
				startActivity(mIntent);
			} else {
				mIntent = new Intent(TravelActivity.this, NoteLoginActivity.class);
				mIntent.putExtra("personal", "order_ticket");
				startActivity(mIntent);
			}

			break;
		case R.id.iv_order_hotel:// 定制酒店
			if (PreferencesUtils.getBoolean(TravelActivity.this, "isLogin")) {
				mIntent = new Intent(getApplicationContext(), HotelOrderActivity.class);
				startActivity(mIntent);
			} else {
				mIntent = new Intent(TravelActivity.this, NoteLoginActivity.class);
				mIntent.putExtra("personal", "order_hotel");
				startActivity(mIntent);
			}

			break;
		case R.id.iv_car_service:// 专车服务
			if (PreferencesUtils.getBoolean(TravelActivity.this, "isLogin")) {
				mIntent = new Intent(getApplicationContext(), CarServiceActivity.class);
				startActivity(mIntent);
			} else {
				mIntent = new Intent(TravelActivity.this, NoteLoginActivity.class);
				mIntent.putExtra("personal", "order_car");
				startActivity(mIntent);
			}

			break;
		case R.id.iv_travel_department:// 出行装备
			mIntent = new Intent(getApplicationContext(), TravelOutDoorsActivity.class);
			startActivity(mIntent);
			break;
		case R.id.iv_deep_travel:// 深度旅游
			if (PreferencesUtils.getBoolean(TravelActivity.this, "isLogin")) {
				mIntent = new Intent(getApplicationContext(), TravelDeepActivity.class);
				startActivity(mIntent);
			} else {
				mIntent = new Intent(TravelActivity.this, NoteLoginActivity.class);
				mIntent.putExtra("personal", "travel_deep");
				startActivity(mIntent);
			}
			break;

		}
	}

	int[] images = { R.drawable.travel_one, R.drawable.travel_two, R.drawable.travel_three };

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return images.length;
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
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_travel, null);
			}
			ImageView travel = BaseViewHolder.get(convertView, R.id.travel);
			Glide.with(getApplicationContext()).load(images[position]).into(travel);
			mListview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					switch (position) {
					case 2:
						if (PreferencesUtils.getBoolean(TravelActivity.this, "isLogin")) {
							mIntent = new Intent(getApplicationContext(), BusinessPlanActivity.class);
							mIntent.putExtra("id", "2");
							startActivity(mIntent);
						} else {
							mIntent = new Intent(TravelActivity.this, NoteLoginActivity.class);
							mIntent.putExtra("personal", "common2");
							startActivityForResult(mIntent, 2);
						}

						break;
					case 0:
						if (PreferencesUtils.getBoolean(TravelActivity.this, "isLogin")) {
							mIntent = new Intent(getApplicationContext(), BusinessPlanActivity.class);
							mIntent.putExtra("id", "0");
							startActivity(mIntent);
						} else {
							mIntent = new Intent(TravelActivity.this, NoteLoginActivity.class);
							mIntent.putExtra("personal", "common0");
							startActivityForResult(mIntent, 0);
						}

						break;
					case 1:
						if (PreferencesUtils.getBoolean(TravelActivity.this, "isLogin")) {
							mIntent = new Intent(getApplicationContext(), BusinessPlanActivity.class);
							mIntent.putExtra("id", "1");
							startActivity(mIntent);
						} else {
							mIntent = new Intent(TravelActivity.this, NoteLoginActivity.class);
							mIntent.putExtra("personal", "common1");
							startActivityForResult(mIntent, 1);
						}

						break;
					}
				}
			});
			return convertView;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 2) {
			if (App.getUserInfo().getUsername().equals(EMClient.getInstance().getCurrentUser())) {
				Toast.makeText(TravelActivity.this, R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
			} else {
				App.CallSteward(TravelActivity.this);
			}
		}
		if (requestCode == 0 && resultCode == 10) {
			mIntent = new Intent(getApplicationContext(), BusinessPlanActivity.class);
			mIntent.putExtra("id", "0");
			startActivity(mIntent);
		}
		if (requestCode == 1 && resultCode == 11) {
			mIntent = new Intent(getApplicationContext(), BusinessPlanActivity.class);
			mIntent.putExtra("id", "1");
			startActivity(mIntent);
		}
		if (requestCode == 2 && resultCode == 12) {
			mIntent = new Intent(getApplicationContext(), BusinessPlanActivity.class);
			mIntent.putExtra("id", "2");
			startActivity(mIntent);
		}
	}

}
