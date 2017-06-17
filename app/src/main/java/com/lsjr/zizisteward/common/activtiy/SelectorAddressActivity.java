package com.lsjr.zizisteward.common.activtiy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.ManageAddress;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.QueryAddressBean;
import com.lsjr.zizisteward.bean.QueryAddressInfo;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SelectorAddressActivity extends BaseActivity {
	TextView manage;
	ListView listview;
	List<QueryAddressInfo> list = new ArrayList<QueryAddressInfo>();
	ListAddressAdapter adapter;
	String shopid, amount;

	@Override
	public int getContainerView() {
		return R.layout.activity_selector_address;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("选择收货地址");
		Intent intent = getIntent();
		shopid = intent.getStringExtra("shopid");
		amount = intent.getStringExtra("amount");
		manage = (TextView) findViewById(R.id.manage);
		listview = (ListView) findViewById(R.id.listview);
	}

	@Override
	protected void onResume() {
		init();
		getData();
		super.onResume();
	}

	private void init() {
		manage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), ManageAddress.class));
			}
		});
	}

	private void getData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("OPT", "13");
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
		new HttpClientGet(SelectorAddressActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				QueryAddressBean bean = GsonUtil.getInstance().fromJson(result, QueryAddressBean.class);
				list = bean.getCheckaddr();
				adapter = new ListAddressAdapter(SelectorAddressActivity.this, list);
				listview.setAdapter(adapter);
				listview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						HashMap<String, String> map = new HashMap<>();
						map.put("OPT", "321");
						map.put("shopid", shopid);
						map.put("userid", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
						map.put("amount", amount);
						map.put("addrid", list.get(position).getAid());
						new HttpClientGet(getApplicationContext(), null, map, false,
								new HttpClientGet.CallBacks<String>() {

									@Override
									public void onSuccess(String result) {
										System.out.println("结果" + result);
										LCQBean bean = GsonUtil.getInstance().fromJson(result, LCQBean.class);
										Intent intent = getIntent();
										intent.putExtra("url", bean.getURL());
										SelectorAddressActivity.this.setResult(1, intent);
										SelectorAddressActivity.this.finish();
									}
								});
					}
				});
			}

			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});
	}

	@SuppressWarnings("unused")
	private class ListAddressAdapter extends BaseAdapter {
		Context context;
		List<QueryAddressInfo> list;
		private ViewHolder mHolder;

		public void setList(List<QueryAddressInfo> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		public void add(QueryAddressInfo e) {
			this.list.add(e);
			notifyDataSetChanged();
		}

		public void addFirst(QueryAddressInfo e) {
			this.list.add(0, e);
			notifyDataSetChanged();
		}

		public void addAll(List<QueryAddressInfo> list) {
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

		public ListAddressAdapter(Context context, List<QueryAddressInfo> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.size();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.item_selector_address, null);
				mHolder = new ViewHolder(convertView);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			mHolder.name.setText(list.get(position).getCname());
			mHolder.phone.setText(list.get(position).getCphone());
			if ("0".endsWith(list.get(position).getIs_common())) {
				mHolder.address.setText(list.get(position).getCaddr());
			} else {
				mHolder.address.setText("[默认]  " + list.get(position).getCaddr());
			}
			return convertView;
		}

		private class ViewHolder {
			TextView name, phone, address;

			public ViewHolder(View view) {
				name = (TextView) view.findViewById(R.id.name);
				phone = (TextView) view.findViewById(R.id.phone);
				address = (TextView) view.findViewById(R.id.address);
			}

		}
	}

	private class LCQBean {
		private String URL;

		public String getURL() {
			return URL;
		}

		public void setURL(String uRL) {
			URL = uRL;
		}

	}
}
