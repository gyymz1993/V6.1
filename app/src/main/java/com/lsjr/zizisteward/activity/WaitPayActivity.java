package com.lsjr.zizisteward.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.NoPayBean;
import com.lsjr.zizisteward.bean.NoPayBean.NoPayDetail;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.squareup.picasso.Picasso;
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WaitPayActivity extends BaseActivity {
	private SuperListView mSlv_all;
	private NoPayAdapter mAdapter;
	private boolean isRefresh = true;
	private int pageNum = 1;
	private List<NoPayDetail> list = new ArrayList<NoPayDetail>();
	private NoPayBean mBean;

	@Override
	public int getContainerView() {
		return R.layout.activity_wait_pay;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("待付款");
		mSlv_all = (SuperListView) findViewById(R.id.slv_all);
		mSlv_all.setVerticalScrollBarEnabled(false);
		initLayout();

	}

	private void initLayout() {
		mAdapter = new NoPayAdapter(WaitPayActivity.this, list);
		mSlv_all.setAdapter(mAdapter);
		mSlv_all.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				isRefresh = true;
				getData();
			}
		});
		mSlv_all.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				isRefresh = false;
				getData();
			}
		});
		mSlv_all.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(WaitPayActivity.this, OrderDetailActivity.class);
				intent.putExtra("indentNo", list.get(position - 1).getGnum());

				startActivity(intent);
			}
		});

		mSlv_all.refresh();
	}

	protected void getData() {
		if (isRefresh) {
			pageNum = 1;
			mAdapter.removeAll();
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("OPT", "41");
		map.put("id", App.getUserInfo().getId());
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
		map.put("currPage", String.valueOf(pageNum++));
		map.put("pay_state", "0");
		new HttpClientGet(WaitPayActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("未支付" + result);
				mBean = GsonUtil.getInstance().fromJson(result, NoPayBean.class);
				if (null != mBean && "1".equals(mBean.getError())) {

					if (1 != pageNum) {
						list.addAll(mBean.getShop_indent().getPage());
						mAdapter.setList(list);
					} else {
						list = mBean.getShop_indent().getPage();
						mAdapter = new NoPayAdapter(WaitPayActivity.this, list);
						mSlv_all.setAdapter(mAdapter);
						mAdapter.notifyDataSetChanged();
					}

					if (list.size() < mBean.getShop_indent().getPageSize()) {
						mSlv_all.setIsLoadFull(false);
					} else {

					}
				}

				if (list.size() < mBean.getShop_indent().getPageSize()) {
					mSlv_all.setIsLoadFull(false);
				}

				mSlv_all.finishRefresh();
				mSlv_all.finishLoadMore();
			}

			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});
	}

	public class NoPayAdapter extends BaseAdapter {
		private Context context;
		private List<NoPayDetail> list;
		private ViewHolder mHolder;

		public NoPayAdapter(Context context, List<NoPayDetail> list) {
			this.context = context;
			this.list = list;
		}

		public void setList(List<NoPayDetail> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		public void add(NoPayDetail page) {
			this.list.add(page);
			notifyDataSetChanged();
		}

		public void addFirst(NoPayDetail page) {
			this.list.add(0, page);
			notifyDataSetChanged();
		}

		public void addAll(List<NoPayDetail> list) {
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.no_pay_fragment_item, parent, false);
				mHolder = new ViewHolder(convertView);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}

			Picasso.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getGpic()).into(mHolder.mIv_pic);
			mHolder.mTv_brand.setText(list.get(position).getGsname());
			mHolder.mTv_number.setText("订单号: " + list.get(position).getGnum());
			mHolder.mTv_price.setText("￥" + list.get(position).getOrder_price());
			mHolder.mTv_price.setTextColor(Color.parseColor("#ffc52c"));
			mHolder.mTv_pay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(WaitPayActivity.this, PayDetailActivity.class);
					intent.putExtra("total_price", list.get(position).getOrder_price());
					intent.putExtra("intentNo", list.get(position).getGnum());

					startActivity(intent);
				}
			});
			return convertView;
		}

	}

	public class ViewHolder {

		private ImageView mIv_pic;
		private TextView mTv_brand;
		private TextView mTv_number;
		private TextView mTv_price;
		private TextView mTv_pay;

		public ViewHolder(View view) {
			mIv_pic = (ImageView) view.findViewById(R.id.iv_pic);
			mTv_brand = (TextView) view.findViewById(R.id.tv_brand);
			mTv_number = (TextView) view.findViewById(R.id.tv_number);
			mTv_price = (TextView) view.findViewById(R.id.tv_price);
			mTv_pay = (TextView) view.findViewById(R.id.tv_pay);
		}

	}
}
