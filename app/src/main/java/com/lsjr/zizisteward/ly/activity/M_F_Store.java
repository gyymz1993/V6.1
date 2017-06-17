package com.lsjr.zizisteward.ly.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.MyStoreBean;
import com.lsjr.zizisteward.bean.MyStoreBean.pageBrand;
import com.lsjr.zizisteward.bean.MyStoreBean.pageBrand.page;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.PullToRefreshLayout;
import com.squareup.picasso.Picasso;
import com.unionpay.mobile.android.widgets.ad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**店铺*/
public class M_F_Store extends Fragment {

	private View rootView;
	private static MyListView mlv;
	private static LinearLayout ll_more;
	private static LinearLayout ll_none;
	private static PullToRefreshLayout ptrl;
	
	public static Context context;
	public static int pageNum = 1;
	private static int pageSize = 10;
	public static boolean ly = false;
	public static M_FAdapter adapter;
	public static List<page> page = new ArrayList<>();
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		if (null == rootView) {
			this.rootView = inflater.inflate(R.layout.m_f_store, null);
			context = getContext();
			this.findViewById();
		}
		
		return rootView;
	}

	private void findViewById() {
		
		this.mlv = (MyListView) rootView.findViewById(R.id.mlv);
		this.ll_none = (LinearLayout) rootView.findViewById(R.id.ll_none);
		this.ll_more = (LinearLayout) rootView.findViewById(R.id.ll_more);
		this.ptrl = (PullToRefreshLayout) rootView.findViewById(R.id.refresh_view);
		
		this.ptrl.setOnRefreshListener((PullToRefreshLayout.OnRefreshListener) new MyListener());
		
		this.ptrl.setVisibility(View.VISIBLE);
		
		pageNum = 1;
		
		getM_F_Data(0);
		
		this.mlv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (ly) {
					
					if (!page.get(position).isSpace()) {
						MyAttention.tv.setText("反选");
					}
					
					page.get(position).setSpace(!page.get(position).isSpace());
					
					adapter.notifyDataSetChanged();
					
				} else {
					
					Intent intent = new Intent(getContext(), NewShopDetailActivity.class);// 店铺对应的店铺详情
					intent.putExtra("id", page.get(position).getId());
					startActivity(intent);
					
				}
			}
		});
	}
	
	private class MyListener implements PullToRefreshLayout.OnRefreshListener {

		@Override
		public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
			pageNum = 1;
			getM_F_Data(1);
		}

		@Override
		public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
			pageNum ++;
			getM_F_Data(2);
		}
	}
	
	/**查看我关注的店铺*/
	public static void getM_F_Data(final int space) {
		
		Map<String, String> map = new HashMap<>();
		
		map.put("OPT", "327");
		
		map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
		
		map.put("currPage", String.valueOf(pageNum));
		
		map.put("pageSize", String.valueOf(pageSize));
		
		new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				
				MyStoreBean msBean = new Gson().fromJson(result, MyStoreBean.class);
				pageBrand pbBean = msBean.getPageBrand();
				
				switch (space) {
				
				case 0:
					//第一次加载
					page = new ArrayList<>();
					page = pbBean.getPage();
					
					adapter = new M_FAdapter(context,page);
					
					mlv.setAdapter(adapter);
					
					adapter.notifyDataSetChanged();
					
					if (null != page && page.size() > 0) {
						
						ll_none.setVisibility(View.GONE);
						ptrl.setVisibility(View.VISIBLE);
						
					} else {
						ll_none.setVisibility(View.VISIBLE);
						ptrl.setVisibility(View.GONE);
						
					}
					
					if (null != page && page.size() >= pageSize) {
						ll_more.setVisibility(View.VISIBLE);
					} else {
						ll_more.setVisibility(View.GONE);
					}
					
					break;

				case 1:
					//刷新
					page = new ArrayList<>();
					page = pbBean.getPage();
					
					adapter = new M_FAdapter(context,page);
					
					mlv.setAdapter(adapter);
					
					adapter.notifyDataSetChanged();
					
					if (null != page && page.size() > 0) {
						
						ll_none.setVisibility(View.GONE);
						ptrl.setVisibility(View.VISIBLE);
						
					} else {
						ll_none.setVisibility(View.VISIBLE);
						ptrl.setVisibility(View.GONE);
						
					}
					
					if (null != page && page.size() >= pageSize) {
						ll_more.setVisibility(View.VISIBLE);
					} else {
						ll_more.setVisibility(View.GONE);
					}
					
					ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
					
					break;
					
				case 2:
					//加载更多
					
					List<page> _page = new ArrayList<>();
					
					_page = pbBean.getPage();
					
					if (null != _page && _page.size() > 0) {
						
						page.addAll(_page);
						
						adapter = new M_FAdapter(context,page);
						
						mlv.setAdapter(adapter);
						
						adapter.notifyDataSetChanged();
						
						if (null != page && page.size() > 0) {
							
							ll_none.setVisibility(View.GONE);
							ptrl.setVisibility(View.VISIBLE);
							
						} else {
							ll_none.setVisibility(View.VISIBLE);
							ptrl.setVisibility(View.GONE);
							
						}
						
						if (null != page && page.size() >= pageSize) {
							ll_more.setVisibility(View.VISIBLE);
						} else {
							ll_more.setVisibility(View.GONE);
						}
					}
					
					ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					
					break;
					
				}
				
			}
			
			@Override
			public void onFailure(MyError myError) {
				
				super.onFailure(myError);
				
			}
			
		});
		
	}
	
	public static class M_FAdapter extends BaseAdapter {
		
		private Context context;
		private ViewHolder view;
		private List<page> page;
		
		public M_FAdapter(Context context,List<page> page) {
			this.context = context;
			this.page = page;
		}

		@Override
		public int getCount() {
			return null == page ? 0 : page.size();
		}

		@Override
		public Object getItem(int position) {
			return page.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (null == convertView) {
				
				convertView = LayoutInflater.from(context).inflate(R.layout.m_f_store_item, null);
				
				view = new ViewHolder(convertView);
				
				convertView.setTag(view);
				
			} else {
				
				view = (ViewHolder) convertView.getTag();
				
			}
			
			if (ly) {
				view.ll_check.setVisibility(View.VISIBLE);
			} else {
				view.ll_check.setVisibility(View.GONE);
			}
			
			if (page.get(position).isSpace()) {
				view.iv_check.setImageResource(R.drawable.icon_collection_true);
			} else {
				view.iv_check.setImageResource(R.drawable.icon_collection_false);
			}
			
			Picasso.with(context).load(HttpConfig.IMAGEHOST + page.get(position).getBlogo()).into(view.riv_head);
			
			view.tv_name.setText(page.get(position).getBname());
			
			return convertView;
		}
		
		private class ViewHolder {
			
			//选中框
			private LinearLayout ll_check;
			private ImageView iv_check;
			//商品图片
			private RoundImageView riv_head;
			//商品名字
			private TextView tv_name;
			
			public ViewHolder(View v) {
			
				this.tv_name = (TextView) v.findViewById(R.id.tv_name);
				this.iv_check = (ImageView) v.findViewById(R.id.iv_check);
				this.ll_check = (LinearLayout) v.findViewById(R.id.ll_check);
				this.riv_head = (RoundImageView) v.findViewById(R.id.riv_head);
				
			}
		}
	}
}
