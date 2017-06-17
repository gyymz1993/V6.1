package com.lsjr.zizisteward.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.BaseViewHolder;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.SonAccountBean;
import com.lsjr.zizisteward.bean.SonAccountInfo;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AccreditAccountActivity extends BaseActivity {

	private ListView mPay;
	private List<SonAccountInfo> mList = new ArrayList<>();

	@Override
	public int getContainerView() {
		return R.layout.activity_accredit_account;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("启用授权账户");
		mPay = (ListView) findViewById(R.id.pay);
		getData();

	}

	public class ListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mList == null ? 0 : mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_accredit_account,
						parent, false);
			}

			TextView tv_name = BaseViewHolder.get(convertView, R.id.tv_name);
			tv_name.setText(mList.get(position).getSon());
			return convertView;
		}

	}

	@Override
	protected void onResume() {
		getData();
		super.onResume();
	}

	private void getData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("OPT", "211");
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
		new HttpClientGet(AccreditAccountActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("账号消息" + result);
				SonAccountBean bean = GsonUtil.getInstance().fromJson(result, SonAccountBean.class);
				mList = bean.getSonUser();
				ListAdapter adapter = new ListAdapter();
				mPay.setAdapter(adapter);
				mPay.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
						Intent intent = new Intent(getApplicationContext(), SonAccountPsdSetActivity.class);
						intent.putExtra("son_type", mList.get(position2).getSon_type());
						intent.putExtra("son_use_start", mList.get(position2).getSon_use_start());
						intent.putExtra("son_name", mList.get(position2).getSon_name());
						intent.putExtra("son_mobile", mList.get(position2).getSon_mobile());
						intent.putExtra("limit", mList.get(position2).getSon_limits());
						startActivity(intent);
					}
				});
			}

		});
	}

}
