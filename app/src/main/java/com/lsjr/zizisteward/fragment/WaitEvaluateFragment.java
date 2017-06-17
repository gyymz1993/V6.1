package com.lsjr.zizisteward.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.BaskSingleActivity;
import com.lsjr.zizisteward.activity.EvaluationActivity;
import com.lsjr.zizisteward.activity.PaidOrderDetailActivity;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WaitEvaluateFragment extends Fragment {

	private View rootView;
	private SuperListView mSlv_all;
	private boolean isRefresh = true;
	private int pageNum = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_wait_evaluate, null);
			mSlv_all = (SuperListView) rootView.findViewById(R.id.slv_all);
			mTv_no_order = (TextView) rootView.findViewById(R.id.tv_no_order);
			mSlv_all.setVerticalScrollBarEnabled(false);
			initView();
		}
		return rootView;
	}

	private void initView() {
		mAdapter = new PaidAdapter(getActivity(), list);
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
				Intent intent = new Intent(getActivity(), PaidOrderDetailActivity.class);
				intent.putExtra("indentNo", list.get(position - 1).getGnum());
				startActivity(intent);
			}
		});

		mSlv_all.refresh();
	}

	private PaidAdapter mAdapter;
	private WaitEvaluateBean mBean;
	private List<WaitEvaluateBean.WaitEvaluateDetail> list = new ArrayList<>();
	private TextView mTv_no_order;

	protected void getData() {
		if (isRefresh) {
			pageNum = 1;
			mAdapter.removeAll();
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("OPT", "41");
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
		map.put("currPage", String.valueOf(pageNum++));
		map.put("pay_state", "4");
		new HttpClientGet(getActivity(), null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("待评价订单列表" + result);
				mBean = GsonUtil.getInstance().fromJson(result, WaitEvaluateBean.class);
				if (mBean.getShop_indent().getTotalCount() == 0) {
					mSlv_all.setVisibility(View.GONE);
					mTv_no_order.setVisibility(View.VISIBLE);
				} else {
					mSlv_all.setVisibility(View.VISIBLE);
					mTv_no_order.setVisibility(View.GONE);
					if (null != mBean && "1".equals(mBean.getError())) {

						if (1 != pageNum) {
							list.addAll(mBean.getShop_indent().getPage());
							mAdapter.setList(list);
							mAdapter.notifyDataSetChanged();
						} else {
							list = mBean.getShop_indent().getPage();
							mAdapter = new PaidAdapter(getActivity(), list);
							mSlv_all.setAdapter(mAdapter);
							mAdapter.notifyDataSetChanged();
						}

					}
					if (list.size() < mBean.getShop_indent().getPageSize()) {
						mSlv_all.setIsLoadFull(false);
					}

					mSlv_all.finishRefresh();
					mSlv_all.finishLoadMore();
				}
			}

		});
	}

	@Override
	public void onResume() {
		isRefresh = true;
		getData();
		super.onResume();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == 1) {
				mSlv_all.refresh();
			}
			break;
		}
	};

	public class PaidAdapter extends BaseAdapter {
		private Context context;
		private List<WaitEvaluateBean.WaitEvaluateDetail> list;
		private ViewHolder view;

		public void setList(
				List<WaitEvaluateBean.WaitEvaluateDetail> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		public void add(WaitEvaluateBean.WaitEvaluateDetail page) {
			this.list.add(page);
			notifyDataSetChanged();
		}

		public void addFirst(
				WaitEvaluateBean.WaitEvaluateDetail page) {
			this.list.add(0, page);
			notifyDataSetChanged();
		}

		public void addAll(
				List<WaitEvaluateBean.WaitEvaluateDetail> list) {
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

		public PaidAdapter(Context context,
				List<WaitEvaluateBean.WaitEvaluateDetail> list) {
			this.list = list;
			this.context = context;
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.wait_evaluate_fragment, parent, false);

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

			if (list.get(position).getBask_order().equals("1") && list.get(position).getAssess_order().equals("1")) {// 已晒单
				// 已评价
				view.btn_bask.setText("已晒单");
				view.btn_bask.setEnabled(false);
				view.btn_bask.setTextColor(Color.GRAY);

				view.btn_evaluation.setText("已评价");
				view.btn_evaluation.setEnabled(false);
				view.btn_evaluation.setTextColor(Color.GRAY);
				mAdapter.notifyDataSetChanged();
			} else if (list.get(position).getBask_order().equals("0")
					&& list.get(position).getAssess_order().equals("0")) {
				// 未晒单 未评价
				view.btn_bask.setText("晒单");
				view.btn_bask.setTextColor(Color.WHITE);
				view.btn_bask.setEnabled(true);
				view.btn_evaluation.setText("评价");
				view.btn_evaluation.setTextColor(Color.WHITE);
				view.btn_evaluation.setEnabled(true);
				view.btn_bask.setTag(position);
				view.btn_bask.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int pos = (int) v.getTag();
						System.out.println(pos);

						Intent intent = new Intent(getActivity(), BaskSingleActivity.class);
						intent.putExtra("gid", list.get(pos).getGnum());
						intent.putExtra("name", list.get(pos).getGsname());
						startActivityForResult(intent, 1);
					}
				});

				view.btn_evaluation.setTag(position);
				view.btn_evaluation.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int pos = (int) v.getTag();

						Intent intent = new Intent(getContext(), EvaluationActivity.class);
						intent.putExtra("gnum", list.get(position).getGnum());
						startActivityForResult(intent, 1);
					}
				});
				mAdapter.notifyDataSetChanged();
			} else if (list.get(position).getBask_order().equals("1")
					&& list.get(position).getAssess_order().equals("0")) {
				// 已晒单 未评价
				view.btn_bask.setText("已晒单");
				view.btn_bask.setEnabled(false);
				view.btn_bask.setTextColor(Color.GRAY);
				view.btn_evaluation.setText("评价");
				view.btn_evaluation.setTextColor(Color.WHITE);
				view.btn_evaluation.setEnabled(true);
				view.btn_evaluation.setTag(position);
				view.btn_evaluation.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int pos = (int) v.getTag();

						Intent intent = new Intent(getActivity(), EvaluationActivity.class);
						intent.putExtra("gnum", list.get(pos).getGnum());
						startActivityForResult(intent, 1);
					}
				});
				mAdapter.notifyDataSetChanged();
			} else if (list.get(position).getBask_order().equals("0")
					&& list.get(position).getAssess_order().equals("1")) {
				// 未晒单 已评价
				view.btn_bask.setText("晒单");
				view.btn_bask.setTextColor(Color.WHITE);
				view.btn_bask.setEnabled(true);
				view.btn_bask.setTag(position);
				view.btn_bask.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int pos = (int) v.getTag();
						System.out.println(pos);

						Intent intent = new Intent(getActivity(), BaskSingleActivity.class);
						intent.putExtra("gid", list.get(pos).getGnum());
						intent.putExtra("name", list.get(pos).getGsname());
						startActivityForResult(intent, 1);
					}
				});
				view.btn_evaluation.setText("已评价");
				view.btn_evaluation.setEnabled(false);
				view.btn_evaluation.setTextColor(Color.GRAY);
				mAdapter.notifyDataSetChanged();
			}

			return convertView;
		}

	}

	public class ViewHolder {

		ImageView iv_pic;
		TextView tv_brand;
		TextView tv_number;
		TextView tv_price;
		TextView btn_evaluation;
		TextView btn_bask;

		public ViewHolder(View v) {

			iv_pic = (ImageView) v.findViewById(R.id.iv_pic);
			tv_brand = (TextView) v.findViewById(R.id.tv_brand);
			tv_number = (TextView) v.findViewById(R.id.tv_number);
			tv_price = (TextView) v.findViewById(R.id.tv_price);
			btn_evaluation = (TextView) v.findViewById(R.id.btn_evaluation);
			btn_bask = (TextView) v.findViewById(R.id.btn_bask);
		}
	}

	public class WaitEvaluateBean {
		private String error;
		private String msg;
		private WaitEvaluateInfo shop_indent;
		private String totalNum;

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public WaitEvaluateInfo getShop_indent() {
			return shop_indent;
		}

		public void setShop_indent(WaitEvaluateInfo shop_indent) {
			this.shop_indent = shop_indent;
		}

		public String getTotalNum() {
			return totalNum;
		}

		public void setTotalNum(String totalNum) {
			this.totalNum = totalNum;
		}

		private class WaitEvaluateInfo {

			private int currPage;
			private List<WaitEvaluateDetail> page;
			private int pageSize;
			private String pageTitle;
			private int totalCount;
			private int totalPageCount;

			public int getCurrPage() {
				return currPage;
			}

			public void setCurrPage(int currPage) {
				this.currPage = currPage;
			}

			public List<WaitEvaluateDetail> getPage() {
				return page;
			}

			public void setPage(List<WaitEvaluateDetail> page) {
				this.page = page;
			}

			public int getPageSize() {
				return pageSize;
			}

			public void setPageSize(int pageSize) {
				this.pageSize = pageSize;
			}

			public String getPageTitle() {
				return pageTitle;
			}

			public void setPageTitle(String pageTitle) {
				this.pageTitle = pageTitle;
			}

			public int getTotalCount() {
				return totalCount;
			}

			public void setTotalCount(int totalCount) {
				this.totalCount = totalCount;
			}

			public int getTotalPageCount() {
				return totalPageCount;
			}

			public void setTotalPageCount(int totalPageCount) {
				this.totalPageCount = totalPageCount;
			}
		}

		private class WaitEvaluateDetail {
			private String assess_order;
			private String bask_order;
			private String credit_pay_money;
			private String ems_code;
			private String ems_no;
			private String entityId;
			private String favour_mode;
			private String favour_type;
			private String gnum;
			private String gpic;
			private String gsname;
			private String id;
			private String is_send;
			private String order_man;
			private String order_man_type;
			private String order_price;
			private String order_real_money;
			private String order_state;
			private String order_time;
			private String other_pay_money;
			private String parent_id;
			private String pay_state;
			private String pay_type;
			private String persistent;
			private String remarks;
			private String sample_id;
			private String service_state;
			private String service_type;
			private String shipments_info;
			private String stage_pay_money;
			private String stage_pay_state;
			private String transflow;
			private String tshow;
			private String uid;

			public String getAssess_order() {
				return assess_order;
			}

			public void setAssess_order(String assess_order) {
				this.assess_order = assess_order;
			}

			public String getBask_order() {
				return bask_order;
			}

			public void setBask_order(String bask_order) {
				this.bask_order = bask_order;
			}

			public String getCredit_pay_money() {
				return credit_pay_money;
			}

			public void setCredit_pay_money(String credit_pay_money) {
				this.credit_pay_money = credit_pay_money;
			}

			public String getEms_code() {
				return ems_code;
			}

			public void setEms_code(String ems_code) {
				this.ems_code = ems_code;
			}

			public String getEms_no() {
				return ems_no;
			}

			public void setEms_no(String ems_no) {
				this.ems_no = ems_no;
			}

			public String getEntityId() {
				return entityId;
			}

			public void setEntityId(String entityId) {
				this.entityId = entityId;
			}

			public String getFavour_mode() {
				return favour_mode;
			}

			public void setFavour_mode(String favour_mode) {
				this.favour_mode = favour_mode;
			}

			public String getFavour_type() {
				return favour_type;
			}

			public void setFavour_type(String favour_type) {
				this.favour_type = favour_type;
			}

			public String getGnum() {
				return gnum;
			}

			public void setGnum(String gnum) {
				this.gnum = gnum;
			}

			public String getGpic() {
				return gpic;
			}

			public void setGpic(String gpic) {
				this.gpic = gpic;
			}

			public String getGsname() {
				return gsname;
			}

			public void setGsname(String gsname) {
				this.gsname = gsname;
			}

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getIs_send() {
				return is_send;
			}

			public void setIs_send(String is_send) {
				this.is_send = is_send;
			}

			public String getOrder_man() {
				return order_man;
			}

			public void setOrder_man(String order_man) {
				this.order_man = order_man;
			}

			public String getOrder_man_type() {
				return order_man_type;
			}

			public void setOrder_man_type(String order_man_type) {
				this.order_man_type = order_man_type;
			}

			public String getOrder_price() {
				return order_price;
			}

			public void setOrder_price(String order_price) {
				this.order_price = order_price;
			}

			public String getOrder_real_money() {
				return order_real_money;
			}

			public void setOrder_real_money(String order_real_money) {
				this.order_real_money = order_real_money;
			}

			public String getOrder_state() {
				return order_state;
			}

			public void setOrder_state(String order_state) {
				this.order_state = order_state;
			}

			public String getOrder_time() {
				return order_time;
			}

			public void setOrder_time(String order_time) {
				this.order_time = order_time;
			}

			public String getOther_pay_money() {
				return other_pay_money;
			}

			public void setOther_pay_money(String other_pay_money) {
				this.other_pay_money = other_pay_money;
			}

			public String getParent_id() {
				return parent_id;
			}

			public void setParent_id(String parent_id) {
				this.parent_id = parent_id;
			}

			public String getPay_state() {
				return pay_state;
			}

			public void setPay_state(String pay_state) {
				this.pay_state = pay_state;
			}

			public String getPay_type() {
				return pay_type;
			}

			public void setPay_type(String pay_type) {
				this.pay_type = pay_type;
			}

			public String getPersistent() {
				return persistent;
			}

			public void setPersistent(String persistent) {
				this.persistent = persistent;
			}

			public String getRemarks() {
				return remarks;
			}

			public void setRemarks(String remarks) {
				this.remarks = remarks;
			}

			public String getSample_id() {
				return sample_id;
			}

			public void setSample_id(String sample_id) {
				this.sample_id = sample_id;
			}

			public String getService_state() {
				return service_state;
			}

			public void setService_state(String service_state) {
				this.service_state = service_state;
			}

			public String getService_type() {
				return service_type;
			}

			public void setService_type(String service_type) {
				this.service_type = service_type;
			}

			public String getShipments_info() {
				return shipments_info;
			}

			public void setShipments_info(String shipments_info) {
				this.shipments_info = shipments_info;
			}

			public String getStage_pay_money() {
				return stage_pay_money;
			}

			public void setStage_pay_money(String stage_pay_money) {
				this.stage_pay_money = stage_pay_money;
			}

			public String getStage_pay_state() {
				return stage_pay_state;
			}

			public void setStage_pay_state(String stage_pay_state) {
				this.stage_pay_state = stage_pay_state;
			}

			public String getTransflow() {
				return transflow;
			}

			public void setTransflow(String transflow) {
				this.transflow = transflow;
			}

			public String getTshow() {
				return tshow;
			}

			public void setTshow(String tshow) {
				this.tshow = tshow;
			}

			public String getUid() {
				return uid;
			}

			public void setUid(String uid) {
				this.uid = uid;
			}

		}
	}
}