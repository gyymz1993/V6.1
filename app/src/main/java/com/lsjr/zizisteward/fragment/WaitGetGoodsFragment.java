package com.lsjr.zizisteward.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.LookLogisticsActivity;
import com.lsjr.zizisteward.activity.TradeSuccessActivity;
import com.lsjr.zizisteward.activity.WaitGetGoodsOrderDetailActivity;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.squareup.picasso.Picasso;
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;

//待收货
public class WaitGetGoodsFragment extends Fragment {
	private View rootView;
	private SuperListView mSlv_all;
	private boolean isRefresh = true;
	private int pageNum = 1;
	private WaitGetGoodsBean mBean;
	private PaidAdapter mAdapter;
	private List<WaitGetGoodsBean.WaitGetInfo.WaitGetGoodsDetail> list = new ArrayList<>();
	private TextView mTv_no_order;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_nopay, null);
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
				Intent intent = new Intent(getActivity(), WaitGetGoodsOrderDetailActivity.class);
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
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
		map.put("currPage", String.valueOf(pageNum++));
		map.put("pay_state", "2");
		new HttpClientGet(getActivity(), null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("待收货订单列表" + result);
				mBean = GsonUtil.getInstance().fromJson(result, WaitGetGoodsBean.class);
				if (mBean.getShop_indent().getTotalCount() == 0) {
					mTv_no_order.setVisibility(View.VISIBLE);
					mSlv_all.setVisibility(View.GONE);
				} else {
					mTv_no_order.setVisibility(View.GONE);
					mSlv_all.setVisibility(View.VISIBLE);
					if (null != mBean && "1".equals(mBean.getError())) {

						if (1 != pageNum) {
							list.addAll(mBean.getShop_indent().getPage());
							mAdapter.setList(list);
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
		super.onResume();
		isRefresh = true;
		getData();
	}

	public class PaidAdapter extends BaseAdapter {
		private Context context;
		private List<WaitGetGoodsBean.WaitGetInfo.WaitGetGoodsDetail> list;
		private ViewHolder view;

		public void setList(
				List<WaitGetGoodsBean.WaitGetInfo.WaitGetGoodsDetail> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		public void add(
				WaitGetGoodsBean.WaitGetInfo.WaitGetGoodsDetail page) {
			this.list.add(page);
			notifyDataSetChanged();
		}

		public void addFirst(
				WaitGetGoodsBean.WaitGetInfo.WaitGetGoodsDetail page) {
			this.list.add(0, page);
			notifyDataSetChanged();
		}

		public void addAll(
				List<WaitGetGoodsBean.WaitGetInfo.WaitGetGoodsDetail> list) {
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
				List<WaitGetGoodsBean.WaitGetInfo.WaitGetGoodsDetail> list) {
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.wait_get_goods, parent, false);

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
			view.check_logistics.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					HashMap<String, String> map = new HashMap<>();
					map.put("OPT", "246");
					map.put("gnum", list.get(position).getGnum());
					new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

						@Override
						public void onSuccess(String result) {
							System.out.println("物流链接" + result);
							WuLiuLianJie lianJie = GsonUtil.getInstance().fromJson(result, WuLiuLianJie.class);
							Intent intent = new Intent(getContext(), LookLogisticsActivity.class);
							intent.putExtra("lianjie", lianJie.getLogisticsUrl());
							startActivity(intent);
						}
					});
				}
			});
			view.get_goods.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					final Dialog dialog = new Dialog(getActivity(), R.style.dialog);
					dialog.setContentView(R.layout.popup_delete_address);
					Window window = dialog.getWindow();
					window.setGravity(Gravity.CENTER | Gravity.CENTER);
					TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
					tv_msg.setText("是否确认收货");
					TextView tv_confirm = (TextView) dialog.findViewById(R.id.tv_confirm);
					TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
					tv_cancel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					tv_confirm.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							HashMap<String, String> map = new HashMap<>();
							map.put("OPT", "53");
							map.put("gnum", list.get(position).getGnum());
							new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

								@Override
								public void onSuccess(String result) {
									BasicParameterBean bean = GsonUtil.getInstance().fromJson(result,
											BasicParameterBean.class);
									Toast.makeText(getContext(), bean.getMsg(), Toast.LENGTH_SHORT).show();
									Intent intent = new Intent(getContext(), TradeSuccessActivity.class);
									intent.putExtra("gnum", list.get(position).getGnum());
									startActivity(intent);
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

			return convertView;
		}

	}

	public class ViewHolder {

		ImageView iv_pic;
		TextView tv_brand;
		TextView tv_number;
		TextView tv_price;
		TextView get_goods;
		TextView check_logistics;

		public ViewHolder(View v) {

			iv_pic = (ImageView) v.findViewById(R.id.iv_pic);
			tv_brand = (TextView) v.findViewById(R.id.tv_brand);
			tv_number = (TextView) v.findViewById(R.id.tv_number);
			tv_price = (TextView) v.findViewById(R.id.tv_price);
			get_goods = (TextView) v.findViewById(R.id.get_goods);
			check_logistics = (TextView) v.findViewById(R.id.check_logistics);
		}
	}

	public class WaitGetGoodsBean {
		private String error;
		private String msg;
		private WaitGetInfo shop_indent;
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

		public WaitGetInfo getShop_indent() {
			return shop_indent;
		}

		public void setShop_indent(WaitGetInfo shop_indent) {
			this.shop_indent = shop_indent;
		}

		public String getTotalNum() {
			return totalNum;
		}

		public void setTotalNum(String totalNum) {
			this.totalNum = totalNum;
		}

		public class WaitGetInfo {

			private int currPage;
			private List<WaitGetGoodsDetail> page;
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

			public List<WaitGetGoodsDetail> getPage() {
				return page;
			}

			public void setPage(List<WaitGetGoodsDetail> page) {
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

			private class WaitGetGoodsDetail {
				private String assess_order;
				private String assess_order_time;
				private String bask_order;
				private String bask_order_time;
				private String credit_pay_money;
				private String ems_code;
				private String ems_no;
				private String entityId;
				private String favour_mode;
				private String favour_type;
				private String feedback_time;
				private String gnum;
				private String gpic;
				private String gsname;
				private String id;
				private String is_send;
				private String need_time;
				private String order_man;
				private String order_man_type;
				private String order_off_time;
				private String order_price;
				private String order_real_money;
				private String order_state;
				private String order_time;
				private String other_pay_money;
				private String parent_id;
				private String pay_state;
				// private PayTime pay_time;
				private String pay_type;
				private String pending_time;
				private String persistent;
				private String remarks;
				private String remind_time;
				private String sample_id;
				private String service_for_btime;
				private String service_for_etime;
				private String service_state;
				private String service_type;
				private String shipments_info;
				private String shipments_time;
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

				public String getAssess_order_time() {
					return assess_order_time;
				}

				public void setAssess_order_time(String assess_order_time) {
					this.assess_order_time = assess_order_time;
				}

				public String getBask_order() {
					return bask_order;
				}

				public void setBask_order(String bask_order) {
					this.bask_order = bask_order;
				}

				public String getBask_order_time() {
					return bask_order_time;
				}

				public void setBask_order_time(String bask_order_time) {
					this.bask_order_time = bask_order_time;
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

				public String getFeedback_time() {
					return feedback_time;
				}

				public void setFeedback_time(String feedback_time) {
					this.feedback_time = feedback_time;
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

				public String getNeed_time() {
					return need_time;
				}

				public void setNeed_time(String need_time) {
					this.need_time = need_time;
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

				public String getOrder_off_time() {
					return order_off_time;
				}

				public void setOrder_off_time(String order_off_time) {
					this.order_off_time = order_off_time;
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

				// public PayTime getPay_time() {
				// return pay_time;
				// }
				//
				// public void setPay_time(PayTime pay_time) {
				// this.pay_time = pay_time;
				// }

				public String getPay_type() {
					return pay_type;
				}

				public void setPay_type(String pay_type) {
					this.pay_type = pay_type;
				}

				public String getPending_time() {
					return pending_time;
				}

				public void setPending_time(String pending_time) {
					this.pending_time = pending_time;
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

				public String getRemind_time() {
					return remind_time;
				}

				public void setRemind_time(String remind_time) {
					this.remind_time = remind_time;
				}

				public String getSample_id() {
					return sample_id;
				}

				public void setSample_id(String sample_id) {
					this.sample_id = sample_id;
				}

				public String getService_for_btime() {
					return service_for_btime;
				}

				public void setService_for_btime(String service_for_btime) {
					this.service_for_btime = service_for_btime;
				}

				public String getService_for_etime() {
					return service_for_etime;
				}

				public void setService_for_etime(String service_for_etime) {
					this.service_for_etime = service_for_etime;
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

				public String getShipments_time() {
					return shipments_time;
				}

				public void setShipments_time(String shipments_time) {
					this.shipments_time = shipments_time;
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

	public class WuLiuLianJie {
		private String logisticsUrl;
		private String error;
		private String msg;

		public String getLogisticsUrl() {
			return logisticsUrl;
		}

		public void setLogisticsUrl(String logisticsUrl) {
			this.logisticsUrl = logisticsUrl;
		}

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
	}
}
