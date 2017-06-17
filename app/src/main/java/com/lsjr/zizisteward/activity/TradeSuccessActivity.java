package com.lsjr.zizisteward.activity;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class TradeSuccessActivity extends BaseActivity {

	private String mIndent_no;
	private TextView mTv_brand, tv_price, tv_count, tv_total_price, tv_name, tv_phone, tv_address, tv_order_num,
			tv_trde_num, tv_order_time, tv_trde_time;
	private ImageView mIv_pic;

	@Override
	public int getContainerView() {
		return R.layout.activity_trade_success2;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("交易成功");
		mIndent_no = getIntent().getStringExtra("gnum");
		mIv_pic = (ImageView) findViewById(R.id.iv_pic);
		mTv_brand = (TextView) findViewById(R.id.tv_brand);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_count = (TextView) findViewById(R.id.tv_count);
		tv_total_price = (TextView) findViewById(R.id.tv_total_price);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_order_num = (TextView) findViewById(R.id.tv_order_num);
		tv_trde_num = (TextView) findViewById(R.id.tv_trde_num);
		tv_order_time = (TextView) findViewById(R.id.tv_order_time);
		tv_trde_time = (TextView) findViewById(R.id.tv_trde_time);

		getData();
	}

	private void getData() {
		HashMap<String, String> map = new HashMap<>();
		map.put("OPT", "40");
		map.put("gnum", mIndent_no);
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
		new HttpClientGet(TradeSuccessActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("交易完成订单详情" + result);
				TradeSuccseeBean bean = GsonUtil.getInstance().fromJson(result, TradeSuccseeBean.class);
				TradeSuccseeDetail detail = bean.getContentData().getDelivery().get(0);
				Glide.with(TradeSuccessActivity.this)
						.load(HttpConfig.IMAGEHOST + bean.getContentData().getDelivery().get(0).getDsimg())
						.into(mIv_pic);
				mTv_brand.setText(detail.getDsname());
				tv_price.setText("￥" + detail.getService_price());
				tv_count.setText("x" + detail.getDnumber());
				tv_total_price.setText("合计 : ￥ " + bean.getContentData().getOrder_price());
				tv_name.setText(detail.getCname());
				tv_phone.setText(detail.getCphone());

				tv_address.setText(detail.getCaddr());
				tv_order_num.setText("订单编号 : " + bean.getContentData().getGnum());
				tv_trde_num.setText("交易流水号 : " + bean.getContentData().getTransflow());
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				if (TextUtils.isEmpty(bean.getContentData().getOrder_time())) {
					tv_order_time.setText("下单时间 : ");
				} else {
					String time1 = formatter.format(Long.valueOf(bean.getContentData().getOrder_time()));
					tv_order_time.setText("下单时间 : " + time1);
				}

				if (TextUtils.isEmpty(bean.getContentData().getPay_time())) {
					tv_trde_time.setText("支付时间 : ");
				} else {
					String time2 = formatter.format(Long.valueOf(bean.getContentData().getPay_time()));
					tv_trde_time.setText("支付时间 : " + time2);
				}

			}
		});
	}

	public class TradeSuccseeBean {
		private TradeSuccseeInfo contentData;
		private String error;
		private String msg;

		public TradeSuccseeInfo getContentData() {
			return contentData;
		}

		public void setContentData(TradeSuccseeInfo contentData) {
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
	}

	public class TradeSuccseeInfo {
		private String assess_order;
		private String bask_order;
		private List<TradeSuccseeDetail> delivery;
		private String gid;
		private String gnum;
		private String gsname;
		private String order_price;
		private String order_time;
		private String pay_state;
		private String pay_time;
		private String transflow;

		public String getPay_time() {
			return pay_time;
		}

		public void setPay_time(String pay_time) {
			this.pay_time = pay_time;
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

		public List<TradeSuccseeDetail> getDelivery() {
			return delivery;
		}

		public void setDelivery(List<TradeSuccseeDetail> delivery) {
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
	}

	public class TradeSuccseeDetail {
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
