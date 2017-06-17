package com.lsjr.zizisteward.ly.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.ShopIndentBean;
import com.lsjr.zizisteward.bean.ShopIndentBean.Shop_Indent.Page;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.squareup.picasso.Picasso;
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChooseBaskSingleActivity extends Activity {
	private LinearLayout ll_back;
	private SuperListView s_lv;
	private boolean isRefresh = true;
	private int pageNum = 1;
	private int count = 8;
	private ChooseBaskAdapter adapter;
	private List<Page> list = new ArrayList<>();
	private ShopIndentBean siBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.choose_bask_single_activity);
		this.findViewById();
	}

	private void findViewById() {
		this.s_lv = (SuperListView) super.findViewById(R.id.s_lv);
		this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
		
		this.ll_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		s_lv.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				isRefresh = true;
				getData();
			}
		});

		s_lv.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				isRefresh = false;
				getData();
			}
		});
		
		getData();
	}
	
	private void getData() {
		if (isRefresh) {
			pageNum = 1;
			if (null != adapter) {
				adapter.removeAll();
			}
		}

		Map<String, String> map = new HashMap<>();
		map.put("OPT", "41");
		map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
		map.put("currPage", String.valueOf(pageNum++));
		map.put("pay_state", "4");
		new HttpClientGet(ChooseBaskSingleActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println(result);
				siBean = new Gson().fromJson(result, ShopIndentBean.class);
				if ( null != siBean && "1".equals(siBean.getError())) {
					
					if ( 1 != pageNum && list.size() > 0) {
						list.addAll(siBean.getShop_indent().getPage());
						adapter.setList(list);
					}else {
						list = siBean.getShop_indent().getPage();
						adapter = new ChooseBaskAdapter(ChooseBaskSingleActivity.this, list);
						s_lv.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					}
					
					if (list.size() < Integer.valueOf(siBean.getShop_indent().getPageSize())) {
						s_lv.setIsLoadFull(false);
					}

					s_lv.finishRefresh();
					s_lv.finishLoadMore();
				}
			}
			
			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});
	}
	
	private class ChooseBaskAdapter extends BaseAdapter {
		
		private Context context;
		private ViewHolder view;
		private List<Page> list;
		
		public void setList(List<Page> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		public void add(Page page) {
			this.list.add(page);
			notifyDataSetChanged();
		}

		public void addFirst(Page page) {
			this.list.add(0, page);
			notifyDataSetChanged();
		}

		public void addAll(List<Page> list) {
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
		
		public ChooseBaskAdapter(Context context, List<Page> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			return null == list ? 0 : list.size();
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
			
			if ( null == convertView ) {
				
				convertView = LayoutInflater.from(context).inflate(R.layout.choose_bask_single_item, null);
				
				view = new ViewHolder(convertView);
				
				convertView.setTag(view);
				
			}else {
				view = (ViewHolder) convertView.getTag();
			}
			
			Picasso.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getGpic()).into(view.iv_goods);
			
			view.tv_goods_name.setText(list.get(position).getGsname());
			view.tv_goods_num.setText(list.get(position).getGnum());
			view.tv_price.setText(list.get(position).getOrder_price());
			
			view.tv_select.setTag(position);
			view.tv_select.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int pos = (int) v.getTag();
					setResult(77,getIntent()
							.putExtra("iv", list.get(pos).getGpic())
							.putExtra("name", list.get(pos).getGsname())
							.putExtra("price", list.get(pos).getOrder_price())
							.putExtra("num", list.get(pos).getGnum()));
					finish();
				}
			});
			
			return convertView;
		}
		
		private class ViewHolder {
			
			private ImageView iv_goods;
			private TextView tv_goods_name;
			private TextView tv_goods_num;
			private TextView tv_price;
			private TextView tv_select;
			
			public ViewHolder(View v) {
				this.iv_goods = (ImageView) v.findViewById(R.id.iv_goods);
				this.tv_goods_name = (TextView) v.findViewById(R.id.tv_goods_name);
				this.tv_goods_num = (TextView) v.findViewById(R.id.tv_goods_num);
				this.tv_price = (TextView) v.findViewById(R.id.tv_price);
				this.tv_select = (TextView) v.findViewById(R.id.tv_select);
			}
		}
	}
}
