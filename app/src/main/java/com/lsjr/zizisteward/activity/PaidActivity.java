package com.lsjr.zizisteward.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.PaidBean;
import com.lsjr.zizisteward.bean.PaidBean.PaidDetail;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PaidActivity extends BaseActivity {
	private SuperListView mSlv_all;
	private boolean isRefresh = true;
	private int pageNum = 1;
	private PaidBean mBean;
	private List<PaidDetail> list = new ArrayList<PaidDetail>();
	private PaidAdapter mAdapter;

	@Override
	public int getContainerView() {
		return R.layout.activity_paid;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("已付款");
		mSlv_all = (SuperListView) findViewById(R.id.slv_all);
		mSlv_all.setVerticalScrollBarEnabled(false);
		initLayout();
	}

	private void initLayout() {
		mAdapter = new PaidAdapter(PaidActivity.this, list);
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
				Intent intent = new Intent(PaidActivity.this, PaidOrderDetailActivity.class);
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
		map.put("pay_state", "4");
		new HttpClientGet(PaidActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("已支付" + result);
				mBean = GsonUtil.getInstance().fromJson(result, PaidBean.class);
				if (null != mBean && "1".equals(mBean.getError())) {

					if (1 != pageNum) {
						list.addAll(mBean.getShop_indent().getPage());
						mAdapter.setList(list);
					} else {
						list = mBean.getShop_indent().getPage();
						mAdapter = new PaidAdapter(PaidActivity.this, list);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		pageNum = 1;
		getData();
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {
		super.onResume();
		getData();
	}

	public class PaidAdapter extends BaseAdapter {
		private Context context;
		private List<PaidDetail> list;
		private ViewHolder view;

		public void setList(List<PaidDetail> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		public void add(PaidDetail page) {
			this.list.add(page);
			notifyDataSetChanged();
		}

		public void addFirst(PaidDetail page) {
			this.list.add(0, page);
			notifyDataSetChanged();
		}

		public void addAll(List<PaidDetail> list) {
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

		public PaidAdapter(Context context, List<PaidDetail> list) {
			this.list = list;
			this.context = context;
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
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.all_fragment_item, parent, false);

				view = new ViewHolder(convertView);

				convertView.setTag(view);

			} else {
				view = (ViewHolder) convertView.getTag();
			}

			Picasso.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getGpic()).into(view.iv_pic);
			view.tv_brand.setText(list.get(position).getGsname());
			view.tv_number.setText("订单号: " + list.get(position).getGnum());
			view.tv_price.setText("￥" + list.get(position).getOrder_price());
			view.tv_price.setTextColor(Color.parseColor("#ffc52c"));

			return convertView;
		}

	}

	public class ViewHolder {

		ImageView iv_pic;
		TextView tv_brand;
		TextView tv_number;
		TextView tv_price;
		TextView btn_evaluation;

		public ViewHolder(View v) {

			iv_pic = (ImageView) v.findViewById(R.id.iv_pic);
			tv_brand = (TextView) v.findViewById(R.id.tv_brand);
			tv_number = (TextView) v.findViewById(R.id.tv_number);
			tv_price = (TextView) v.findViewById(R.id.tv_price);
			btn_evaluation = (TextView) v.findViewById(R.id.btn_evaluation);

		}
	}
}
