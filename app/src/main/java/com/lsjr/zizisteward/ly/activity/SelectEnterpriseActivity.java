package com.lsjr.zizisteward.ly.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.MyCardBean;
import com.lsjr.zizisteward.bean.MyCardBean.Card;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.util.Base64;
import android.widget.LinearLayout;

/**企业认证*/
public class SelectEnterpriseActivity extends Activity implements OnClickListener {

	private LinearLayout ll_back;
	private MyListView mlv;
	private TextView tv;
	private List<Card> card = new ArrayList<>();
	private SEAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.select_enterprise_activity);
		this.findViewById();
	}

	private void findViewById() {
		
		this.tv = (TextView) super.findViewById(R.id.tv);
		this.mlv = (MyListView) super.findViewById(R.id.mlv);
		this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
		
		this.ll_back.setOnClickListener(this);
		this.ll_back.setOnClickListener(this);
		
		//this.getData();
		this.GetMyEnterpriseList();
	}
	
	/**获取我的名片里需要认证的企业列表*/
	private void GetMyEnterpriseList() {
		Map<String, String> map = new HashMap<>();
		map.put("OPT", "433");
		map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
		new HttpClientGet(SelectEnterpriseActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				
				System.out.println(result);
				
				MyCardBean mBean = new Gson().fromJson(result, MyCardBean.class);
				
				card = mBean.getCard();
				
				if (null != card && card.size() >= 1) {
					
					tv.setVisibility(View.GONE);
					mlv.setVisibility(View.VISIBLE);
					
					adapter = new SEAdapter(SelectEnterpriseActivity.this, card);
					mlv.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					
				} else {
					tv.setVisibility(View.VISIBLE);
					mlv.setVisibility(View.GONE);
				}
				
			}
			
			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});
	}
	
	private void getData() {
		Map<String, String> map = new HashMap<>();
		map.put("OPT", "290");
		map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
		
		new HttpClientGet(SelectEnterpriseActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println(result);
				
				MyCardBean mBean = new Gson().fromJson(result, MyCardBean.class);
				
				card = mBean.getCard();
				
				if (null != card && card.size() >= 1) {
					
					tv.setVisibility(View.GONE);
					mlv.setVisibility(View.VISIBLE);
					
					for (int i = 0; i < card.size(); i++) {
						
						if (null == card.get(i).getCompany_name() 
								|| card.get(i).getCompany_name().length() < 1) {
							card.remove(i);
						}
					}
					
					adapter = new SEAdapter(SelectEnterpriseActivity.this, card);
					mlv.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					
				} else {
					tv.setVisibility(View.VISIBLE);
					mlv.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});
	}
	
	private class SEAdapter extends BaseAdapter {
		private Context context;
		private List<Card> card;
		private ViewHolder view;
		
		public SEAdapter(Context content,List<Card> card) {
			this.context = content;
			this.card = card;
		}

		@Override
		public int getCount() {
			return null == card ? 0 : card.size();
		}

		@Override
		public Object getItem(int position) {
			return card.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (null == convertView) {
				
				convertView = LayoutInflater.from(context).inflate(R.layout.select_enterprise_activity_item, null);
				
				view = new ViewHolder(convertView);
				
				convertView.setTag(view);
				
			} else {
				
				view = (ViewHolder) convertView.getTag();
				
			}
			
			view.tv_name.setText(card.get(position).getCompany_name());
			
			view.tv_sure.setTag(position);
			view.tv_sure.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int pos = (int) v.getTag();
					Intent intent = new Intent();
					intent.putExtra("cardid", card.get(pos).getId());
					intent.putExtra("name", card.get(pos).getCompany_name());
					setResult(3, intent);
					finish();
				}
			});
			
			return convertView;
		}
		
		private class ViewHolder {
		
			private TextView tv_name;
			private TextView tv_sure;
			
			public ViewHolder(View v) {
				this.tv_name = (TextView) v.findViewById(R.id.tv_name);
				this.tv_sure = (TextView) v.findViewById(R.id.tv_sure);
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
