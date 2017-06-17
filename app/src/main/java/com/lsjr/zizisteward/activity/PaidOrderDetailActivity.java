package com.lsjr.zizisteward.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.newview.MyListView;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//待评价订单详情
@SuppressLint("SimpleDateFormat")
public class PaidOrderDetailActivity extends BaseActivity {

	private TextView mTv_total_price;
	private TextView mTv_name;
	private TextView mTv_phone;
	private TextView mTv_address;
	private TextView mIntendNO;
	private TextView mTime;
	private TextView mTurn_time;
	private TextView mTv_bask;
	private TextView mEvaluation;
	private String mIndentNo;
	private List<WaitEvaluateBean2.WaitEvaluateDetail> list = new ArrayList<>();
	private TextView tv_jiaoyihao;
	private MyListView mListview;
	private int CODE = 0;
	private RelativeLayout mIv_back;

	@Override
	public int getContainerView() {
		return R.layout.activity_paid_order_detail;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("订单详情");
		mIv_back = (RelativeLayout) findViewById(R.id.iv_back);
		mListview = (MyListView) findViewById(R.id.listview);
		mTv_total_price = (TextView) findViewById(R.id.tv_total_price);
		mTv_name = (TextView) findViewById(R.id.tv_name);
		mTv_phone = (TextView) findViewById(R.id.tv_phone);
		mTv_address = (TextView) findViewById(R.id.tv_address);
		mIntendNO = (TextView) findViewById(R.id.intendNO);
		mTime = (TextView) findViewById(R.id.time);
		mTurn_time = (TextView) findViewById(R.id.turn_time);
		tv_jiaoyihao = (TextView) findViewById(R.id.jiaoyihao);
		mTv_bask = (TextView) findViewById(R.id.tv_bask);
		mEvaluation = (TextView) findViewById(R.id.evaluation);
		mIndentNo = getIntent().getStringExtra("indentNo");
		getData();
		mIv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("什么码" + CODE);
				if (CODE == 0) {
					finish();
				} else {
					Intent intent = getIntent();
					PaidOrderDetailActivity.this.setResult(1, intent);
					PaidOrderDetailActivity.this.finish();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		getData();
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == 1) {
				// new Handler().postDelayed(new Runnable() {
				// public void run() {
				// Intent intent = getIntent();
				// PaidOrderDetailActivity.this.setResult(1, intent);
				// PaidOrderDetailActivity.this.finish();
				// }
				// }, 500);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void getData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("OPT", "40");
		map.put("id", App.getUserInfo().getId());
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
		map.put("gnum", mIndentNo);
		new HttpClientGet(PaidOrderDetailActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("待评价订单详情" + result);
				final WaitEvaluateBean2 bean = GsonUtil.getInstance().fromJson(result, WaitEvaluateBean2.class);
				list = bean.getContentData().getDelivery();
				EvalAdapter adapter = new EvalAdapter(PaidOrderDetailActivity.this, list);
				mListview.setAdapter(adapter);
				mTv_total_price.setText("合计: ￥" + bean.getContentData().order_price);
				mTv_name.setText(list.get(0).getCname());
				mTv_phone.setText(list.get(0).getCphone());
				mTv_address.setText(list.get(0).getCaddr());
				mIntendNO.setText("订单编号 : " + mIndentNo);

				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				if (TextUtils.isEmpty(bean.getContentData().getOrder_time())) {
					mTime.setText("下单时间 : ");
				} else {
					String time1 = formatter.format(Long.valueOf(bean.getContentData().getOrder_time()));
					mTime.setText("下单时间 : " + time1);
				}
				if (TextUtils.isEmpty(bean.getContentData().getPay_time())) {
					mTurn_time.setText("支付时间 : ");
				} else {
					String time2 = formatter.format(Long.valueOf(bean.getContentData().getPay_time()));
					mTurn_time.setText("支付时间 : " + time2);
				}
				tv_jiaoyihao.setText("交易号码 : " + bean.getContentData().getTransflow());
				if ("1".equals(bean.getContentData().getBask_order())
						&& "1".equals(bean.getContentData().getAssess_order())) {
					mTv_bask.setText("已晒单");
					mTv_bask.setEnabled(false);
					mTv_bask.setTextColor(Color.GRAY);
					mEvaluation.setText("已评价");
					mEvaluation.setEnabled(false);
					mEvaluation.setTextColor(Color.GRAY);
				} else if ("0".equals(bean.getContentData().getBask_order())
						&& "0".equals(bean.getContentData().getAssess_order())) {
					mTv_bask.setText("晒单");
					mEvaluation.setText("评价");
					mTv_bask.setOnClickListener(new OnClickListener() {// 晒单

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(PaidOrderDetailActivity.this, BaskSingleActivity.class);
							intent.putExtra("name", list.get(0).getDsname());
							intent.putExtra("gid", bean.getContentData().getGnum());
							startActivityForResult(intent, 1);
						}
					});

					mEvaluation.setOnClickListener(new OnClickListener() {// 评价

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(PaidOrderDetailActivity.this, EvaluationActivity.class);
							intent.putExtra("gnum", bean.getContentData().getGnum());
							startActivityForResult(intent, 1);
						}
					});

				} else if ("0".equals(bean.getContentData().getBask_order())
						&& "1".equals(bean.getContentData().getAssess_order())) {
					mTv_bask.setText("晒单");
					mEvaluation.setText("已评价");
					mEvaluation.setEnabled(false);
					mEvaluation.setTextColor(Color.GRAY);
					mTv_bask.setOnClickListener(new OnClickListener() {// 晒单

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(PaidOrderDetailActivity.this, BaskSingleActivity.class);
							intent.putExtra("name", list.get(0).getDsname());
							intent.putExtra("gid", bean.getContentData().getGnum());
							startActivityForResult(intent, 1);
						}
					});
				} else if ("1".equals(bean.getContentData().getBask_order())
						&& "0".equals(bean.getContentData().getAssess_order())) {
					mTv_bask.setText("已晒单");
					mTv_bask.setEnabled(false);
					mTv_bask.setTextColor(Color.GRAY);
					mEvaluation.setText("评价");
					mEvaluation.setOnClickListener(new OnClickListener() {// 评价

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(PaidOrderDetailActivity.this, EvaluationActivity.class);
							intent.putExtra("gnum", bean.getContentData().getGnum());
							startActivityForResult(intent, 1);
						}
					});
				}

				mListview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Intent intent = new Intent(getApplicationContext(), HomeBrandDetail.class);
						intent.putExtra("sid", bean.getContentData().getDelivery().get(position).getSid());
						startActivity(intent);
					}
				});

			}

		});
	}

	public class EvalAdapter extends BaseAdapter {
		private Context context;
		private ViewHolder mHolder;
		private List<WaitEvaluateBean2.WaitEvaluateDetail> list;

		public EvalAdapter(Context context,
				List<WaitEvaluateBean2.WaitEvaluateDetail> list) {
			this.context = context;
			this.list = list;
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

	public class WaitEvaluateBean2 {
		private WaitEvaluateInfo contentData;
		private String error;
		private String msg;

		public WaitEvaluateInfo getContentData() {
			return contentData;
		}

		public void setContentData(WaitEvaluateInfo contentData) {
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

		private class WaitEvaluateInfo {
			private String assess_order;
			private String bask_order;
			private String gid;
			private String gnum;
			private String gsname;
			private String order_price;
			private String order_time;
			private String pay_state;
			private String pay_time;
			private String transflow;
			private List<WaitEvaluateDetail> delivery;

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

			public String getPay_time() {
				return pay_time;
			}

			public void setPay_time(String pay_time) {
				this.pay_time = pay_time;
			}

			public String getTransflow() {
				return transflow;
			}

			public void setTransflow(String transflow) {
				this.transflow = transflow;
			}

			public List<WaitEvaluateDetail> getDelivery() {
				return delivery;
			}

			public void setDelivery(List<WaitEvaluateDetail> delivery) {
				this.delivery = delivery;
			}
		}

		private class WaitEvaluateDetail {
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
