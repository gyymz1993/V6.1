package com.lsjr.zizisteward.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.BaseListAdapter;
import com.lsjr.zizisteward.adapter.BaseViewHolder;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.bean.QueryAddressBean;
import com.lsjr.zizisteward.bean.QueryAddressInfo;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//查询地址
@SuppressLint("UseSparseArrays")
public class ManageAddress extends Activity implements OnClickListener {
	private ListView listview_address;
	private TextView tv_add;
	private List<QueryAddressInfo> list = new ArrayList<QueryAddressInfo>();
	private AddressAdapter adapter;
	private Intent mIntent;
	private ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_address);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		listview_address = (ListView) findViewById(R.id.listview_address);
		tv_add = (TextView) findViewById(R.id.tv_add);

		// 动态注册广播接收者
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("action.address");
		registerReceiver(refreshAddressList, intentFilter);

		initLayout();

	}

	@Override
	protected void onResume() {
		getData();
		super.onResume();
	}

	private BroadcastReceiver refreshAddressList = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("action.address")) {
				getData();
			}
		}
	};

	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(refreshAddressList);

	};

	private void initLayout() {
		tv_add.setOnClickListener(this);
		iv_back.setOnClickListener(this);
	}

	protected void getData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("OPT", "13");
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
		new HttpClientGet(ManageAddress.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				Log.i("test", "查询地址" + result);
				QueryAddressBean bean = GsonUtil.getInstance().fromJson(result, QueryAddressBean.class);
				list = bean.getCheckaddr();
				adapter = new AddressAdapter(ManageAddress.this, list);
				listview_address.setAdapter(adapter);
			}

			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});
	}

	class AddressAdapter extends BaseListAdapter<QueryAddressInfo> {
		private Activity context;
		private List<QueryAddressInfo> list;

		public AddressAdapter(Activity context, List<QueryAddressInfo> list) {
			super(context, list);
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
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
		public View bindView(final int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_address_manage, parent, false);
			}
			RelativeLayout re_set_common = BaseViewHolder.get(convertView, R.id.re_set_common);
			ImageView iv_iscommon = BaseViewHolder.get(convertView, R.id.iv_iscommon);
			TextView tv_common = BaseViewHolder.get(convertView, R.id.tv_common);
			ImageView iv_delete = BaseViewHolder.get(convertView, R.id.iv_delete);
			ImageView iv_edit_address = BaseViewHolder.get(convertView, R.id.iv_edit_address);
			TextView tv_name = BaseViewHolder.get(convertView, R.id.tv_name);
			TextView tv_phone = BaseViewHolder.get(convertView, R.id.tv_phone);
			TextView tv_address = BaseViewHolder.get(convertView, R.id.tv_address);
			tv_name.setText(list.get(position).getCname());
			tv_phone.setText(list.get(position).getCphone());
			tv_address.setText(list.get(position).getCaddr());
			iv_edit_address.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ManageAddress.this, EditAddress.class);
					intent.putExtra("name", list.get(position).getCname());
					intent.putExtra("phone", list.get(position).getCphone());
					intent.putExtra("address", list.get(position).getCaddr());
					intent.putExtra("clocation", list.get(position).getClocation());
					intent.putExtra("aid", list.get(position).getAid());
					intent.putExtra("cpostcode", list.get(position).getCpostcode());
					intent.putExtra("is_common", list.get(position).getIs_common());
					startActivity(intent);
				}
			});
			iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					final Dialog dialog = new Dialog(ManageAddress.this, R.style.dialog);
					dialog.setContentView(R.layout.popup_delete_address);
					Window window = dialog.getWindow();
					window.setGravity(Gravity.CENTER | Gravity.CENTER);
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
							map.put("OPT", "16");
							map.put("addr_id", list.get(position).getAid());
							map.put("mid", App.getUserInfo().getId());
							map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
							new HttpClientGet(ManageAddress.this, null, map, false,
									new HttpClientGet.CallBacks<String>() {

										@Override
										public void onSuccess(String result) {
											BasicParameterBean bean = GsonUtil.getInstance().fromJson(result,
													BasicParameterBean.class);

											Toast.makeText(ManageAddress.this, bean.getMsg(), Toast.LENGTH_SHORT)
													.show();
											// 发送广播
											Intent intent = new Intent();
											intent.setAction("action.address");
											sendBroadcast(intent);
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

			if (list.get(position).getIs_common().equals("0")) {
				iv_iscommon.setImageResource(R.drawable.recycle_address);
				tv_common.setText("设成默认");
				re_set_common.setEnabled(true);
				re_set_common.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("OPT", "17");
						map.put("mid", App.getUserInfo().getId());
						map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
						map.put("addr_id", list.get(position).getAid());
						new HttpClientGet(ManageAddress.this, null, map, false, new HttpClientGet.CallBacks<String>() {

							@Override
							public void onSuccess(String result) {
								BasicParameterBean bean = GsonUtil.getInstance().fromJson(result,
										BasicParameterBean.class);
								Toast.makeText(ManageAddress.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
								getData();
							}

							@Override
							public void onFailure(MyError myError) {
								super.onFailure(myError);
							}
						});
					}
				});

			} else if (list.get(position).getIs_common().equals("1")) {
				iv_iscommon.setImageResource(R.drawable.recycle_address_on);
				tv_common.setText("已设成默认");
				re_set_common.setEnabled(false);
			}

			return convertView;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_add:
			mIntent = new Intent(ManageAddress.this, AddAddressActivity.class);
			startActivity(mIntent);
			break;
		case R.id.iv_back:
			finish();
			break;

		}
	}

}
