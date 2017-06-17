package com.lsjr.zizisteward.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.fragment.WaitGetGoodsFragment.WuLiuLianJie;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.newview.MyListView;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class WaitGetGoodsOrderDetailActivity extends BaseActivity implements OnClickListener {

	private ScrollView mScrollview;
	private static Handler handler = new Handler();
	private TextView mTv_confirm_getgods;
	private String mIndentNo;
	private TextView tv_total_price, tv_name, tv_phone, tv_address, tv_order_num, tv_trde_num, tv_order_time,
			tv_trde_time, tv_send_time, tv_look_wuliu;
	private List<GetGoods.GetGoodsInfo.GetGoodsDetail> list = new ArrayList<>();
	private RelativeLayout mLook_wuliu;
	private MyListView mListview;
	private TextView mTv_wuhan;

	@Override
	public int getContainerView() {
		return R.layout.activity_wait_get_goods_order_detail;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("待收货");
		mIndentNo = getIntent().getStringExtra("indentNo");
		mListview = (MyListView) findViewById(R.id.listview);
		mScrollview = (ScrollView) findViewById(R.id.scrollview);
		mTv_confirm_getgods = (TextView) findViewById(R.id.tv_confirm_getgods);
		handler.post(new Runnable() {
			public void run() {
				mScrollview.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
		getData();
		initLayout();
	}

	private void getData() {
		HashMap<String, String> map = new HashMap<>();
		map.put("OPT", "40");
		map.put("gnum", mIndentNo);
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
		new HttpClientGet(WaitGetGoodsOrderDetailActivity.this, null, map, false,
				new HttpClientGet.CallBacks<String>() {

					@Override
					public void onSuccess(String result) {
						System.out.println("待收货订单详情" + result);
						final GetGoods bean = GsonUtil.getInstance().fromJson(result, GetGoods.class);
						list = bean.getContentData().getDelivery();
						GetGoodsAdapter adapter = new GetGoodsAdapter(WaitGetGoodsOrderDetailActivity.this, list);
						mListview.setAdapter(adapter);

						tv_total_price.setText("合计: ￥" + bean.getContentData().order_price);
						tv_name.setText(list.get(0).getCname());
						tv_phone.setText(list.get(0).getCphone());
						tv_address.setText(list.get(0).getCaddr());
						tv_order_num.setText("订单编号: " + mIndentNo);
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");
						if (TextUtils.isEmpty(bean.getContentData().getOrder_time())) {
							tv_order_time.setText("下单时间: ");
						} else {
							String time1 = formatter.format(Long.valueOf(bean.getContentData().getOrder_time()));
							tv_order_time.setText("下单时间: " + time1);
						}
						if (TextUtils.isEmpty(bean.getContentData().getPay_time())) {
							tv_trde_time.setText("支付时间: ");
						} else {
							String time2 = formatter.format(Long.valueOf(bean.getContentData().getPay_time()));
							tv_trde_time.setText("支付时间: " + time2);
						}

						tv_trde_num.setText("交易流水号: " + bean.getContentData().getTransflow());
						if (bean.getContentData().getTraces().size() > 0) {
							mTv_wuhan.setText("    " + bean.getContentData().getTraces().get(0).getAcceptStation());
						} else {
							mTv_wuhan.setText("    ");
						}

						mListview.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								Intent intent = new Intent(getApplicationContext(), HomeBrandDetail.class);
								intent.putExtra("sid", bean.getContentData().getDelivery().get(position).getSid());// 商品id
								startActivity(intent);
							}
						});
					}
				});
	}

	private void initLayout() {
		tv_total_price = (TextView) findViewById(R.id.tv_total_price);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_order_num = (TextView) findViewById(R.id.tv_order_num);
		tv_trde_num = (TextView) findViewById(R.id.tv_trde_num);
		tv_order_time = (TextView) findViewById(R.id.tv_order_time);
		tv_trde_time = (TextView) findViewById(R.id.tv_trde_time);
		tv_look_wuliu = (TextView) findViewById(R.id.tv_look_wuliu);
		mLook_wuliu = (RelativeLayout) findViewById(R.id.look_wuliu);
		mTv_wuhan = (TextView) findViewById(R.id.tv_wuhan);
		mTv_confirm_getgods.setOnClickListener(this);
		tv_look_wuliu.setOnClickListener(this);
		mLook_wuliu.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_confirm_getgods:
			final Dialog dialog = new Dialog(WaitGetGoodsOrderDetailActivity.this, R.style.dialog);
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
					map.put("gnum", mIndentNo);
					new HttpClientGet(WaitGetGoodsOrderDetailActivity.this, null, map, false,
							new HttpClientGet.CallBacks<String>() {

								@Override
								public void onSuccess(String result) {
									BasicParameterBean bean = GsonUtil.getInstance().fromJson(result,
											BasicParameterBean.class);
									Toast.makeText(WaitGetGoodsOrderDetailActivity.this, bean.getMsg(),
											Toast.LENGTH_SHORT).show();
									Intent intent = new Intent(WaitGetGoodsOrderDetailActivity.this,
											TradeSuccessActivity.class);
									intent.putExtra("gnum", mIndentNo);
									startActivity(intent);
									finish();
								}

							});

				}
			});

			dialog.show();
			break;
		case R.id.tv_look_wuliu:
			HashMap<String, String> map = new HashMap<>();
			map.put("OPT", "246");
			map.put("gnum", mIndentNo);
			new HttpClientGet(WaitGetGoodsOrderDetailActivity.this, null, map, false,
					new HttpClientGet.CallBacks<String>() {

						@Override
						public void onSuccess(String result) {
							WuLiuLianJie lianJie = GsonUtil.getInstance().fromJson(result, WuLiuLianJie.class);
							Intent intent = new Intent(WaitGetGoodsOrderDetailActivity.this,
									LookLogisticsActivity.class);
							intent.putExtra("lianjie", lianJie.getLogisticsUrl());
							startActivity(intent);
						}
					});

			break;
		case R.id.look_wuliu:
			HashMap<String, String> map2 = new HashMap<>();
			map2.put("OPT", "246");
			map2.put("gnum", mIndentNo);
			new HttpClientGet(WaitGetGoodsOrderDetailActivity.this, null, map2, false,
					new HttpClientGet.CallBacks<String>() {

						@Override
						public void onSuccess(String result) {
							WuLiuLianJie lianJie = GsonUtil.getInstance().fromJson(result, WuLiuLianJie.class);
							Intent intent = new Intent(WaitGetGoodsOrderDetailActivity.this,
									LookLogisticsActivity.class);
							intent.putExtra("lianjie", lianJie.getLogisticsUrl());
							startActivity(intent);
						}
					});
			break;

		}
	}

	private class GetGoodsAdapter extends BaseAdapter {
		private Context context;
		private ViewHolder mHolder;
		private List<GetGoods.GetGoodsInfo.GetGoodsDetail> list;

		public GetGoodsAdapter(Context context,
				List<GetGoods.GetGoodsInfo.GetGoodsDetail> list) {
			this.list = list;
			this.context = context;
		}

		@Override
		public int getCount() {
			return list.size();
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
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.item_order_list, parent, false);
				mHolder = new ViewHolder(convertView);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			Glide.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getDsimg()).into(mHolder.mIv_pic);
			mHolder.mTv_brand.setText(list.get(position).getDsname());
			mHolder.mTv_price.setText("￥" + list.get(position).getService_price());
			mHolder.mTv_count.setText("x" + list.get(position).getDnumber());
			return convertView;
		}

	}

	public class ViewHolder {

		private ImageView mIv_pic;
		private TextView mTv_brand;
		private TextView mTv_price;
		private TextView mTv_count;

		public ViewHolder(View view) {
			mIv_pic = (ImageView) view.findViewById(R.id.iv_pic);
			mTv_brand = (TextView) view.findViewById(R.id.tv_brand);
			mTv_price = (TextView) view.findViewById(R.id.tv_price);
			mTv_count = (TextView) view.findViewById(R.id.tv_count);
		}

	}

	public class GetGoods {
		private GetGoodsInfo contentData;
		private String error;
		private String msg;

		public GetGoodsInfo getContentData() {
			return contentData;
		}

		public void setContentData(GetGoodsInfo contentData) {
			this.contentData = contentData;
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

		private class GetGoodsInfo {
			private String assess_order;
			private String bask_order;
			private List<GetGoodsDetail> delivery;
			private String gid;
			private String gnum;
			private String gsname;
			private String order_price;
			private String order_time;
			private String pay_state;
			private String pay_time;
			private String transflow;
			private List<Traces> traces;

			public String getPay_time() {
				return pay_time;
			}

			public void setPay_time(String pay_time) {
				this.pay_time = pay_time;
			}

			public List<Traces> getTraces() {
				return traces;
			}

			public void setTraces(List<Traces> traces) {
				this.traces = traces;
			}

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

			public List<GetGoodsDetail> getDelivery() {
				return delivery;
			}

			public void setDelivery(List<GetGoodsDetail> delivery) {
				this.delivery = delivery;
			}

			public String getGid() {
				return gid;
			}

			public void setGid(String gid) {
				this.gid = gid;
			}

			public String getGnum() {
				return gnum;
			}

			public void setGnum(String gnum) {
				this.gnum = gnum;
			}

			public String getGsname() {
				return gsname;
			}

			public void setGsname(String gsname) {
				this.gsname = gsname;
			}

			public String getOrder_price() {
				return order_price;
			}

			public void setOrder_price(String order_price) {
				this.order_price = order_price;
			}

			public String getOrder_time() {
				return order_time;
			}

			public void setOrder_time(String order_time) {
				this.order_time = order_time;
			}

			public String getPay_state() {
				return pay_state;
			}

			public void setPay_state(String pay_state) {
				this.pay_state = pay_state;
			}

			public String getTransflow() {
				return transflow;
			}

			public void setTransflow(String transflow) {
				this.transflow = transflow;
			}

			private class Traces {
				private String AcceptStation;
				private String AcceptTime;

				public String getAcceptStation() {
					return AcceptStation;
				}

				public void setAcceptStation(String acceptStation) {
					AcceptStation = acceptStation;
				}

				public String getAcceptTime() {
					return AcceptTime;
				}

				public void setAcceptTime(String acceptTime) {
					AcceptTime = acceptTime;
				}
			}

			private class GetGoodsDetail {
				private String aid;
				private String caddr;
				private String clocation;
				private String cname;
				private String cphone;
				private String dcolor;
				private String dnumber;
				private String dsimg;
				private String dsize;
				private String dsname;
				private String entityId;
				private String gmid;
				private String id;
				private String kid;
				private String persistent;
				private String service_price;
				private String sid;

				public String getSid() {
					return sid;
				}

				public void setSid(String sid) {
					this.sid = sid;
				}

				public String getAid() {
					return aid;
				}

				public void setAid(String aid) {
					this.aid = aid;
				}

				public String getCaddr() {
					return caddr;
				}

				public void setCaddr(String caddr) {
					this.caddr = caddr;
				}

				public String getClocation() {
					return clocation;
				}

				public void setClocation(String clocation) {
					this.clocation = clocation;
				}

				public String getCname() {
					return cname;
				}

				public void setCname(String cname) {
					this.cname = cname;
				}

				public String getCphone() {
					return cphone;
				}

				public void setCphone(String cphone) {
					this.cphone = cphone;
				}

				public String getDcolor() {
					return dcolor;
				}

				public void setDcolor(String dcolor) {
					this.dcolor = dcolor;
				}

				public String getDnumber() {
					return dnumber;
				}

				public void setDnumber(String dnumber) {
					this.dnumber = dnumber;
				}

				public String getDsimg() {
					return dsimg;
				}

				public void setDsimg(String dsimg) {
					this.dsimg = dsimg;
				}

				public String getDsize() {
					return dsize;
				}

				public void setDsize(String dsize) {
					this.dsize = dsize;
				}

				public String getDsname() {
					return dsname;
				}

				public void setDsname(String dsname) {
					this.dsname = dsname;
				}

				public String getEntityId() {
					return entityId;
				}

				public void setEntityId(String entityId) {
					this.entityId = entityId;
				}

				public String getGmid() {
					return gmid;
				}

				public void setGmid(String gmid) {
					this.gmid = gmid;
				}

				public String getId() {
					return id;
				}

				public void setId(String id) {
					this.id = id;
				}

				public String getKid() {
					return kid;
				}

				public void setKid(String kid) {
					this.kid = kid;
				}

				public String getPersistent() {
					return persistent;
				}

				public void setPersistent(String persistent) {
					this.persistent = persistent;
				}

				public String getService_price() {
					return service_price;
				}

				public void setService_price(String service_price) {
					this.service_price = service_price;
				}
			}

		}
	}
}
