package com.lsjr.zizisteward.activity;

import java.util.HashMap;
import java.util.List;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PaySuccessActivity extends BaseActivity implements OnClickListener {

	private TextView mBack;
	private Intent mIntent;
	private RelativeLayout mIv_back;
	private TextView mTv_look_order;
	private String mGnum;
	private TextView mTv_name;
	private TextView mTv_phone;
	private TextView mTv_address;
	private TextView mTv_price;

	@Override
	public int getContainerView() {
		return R.layout.activity_pay_success;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setmTitle("支付成功");
		mGnum = getIntent().getStringExtra("gnum");
		mBack = (TextView) findViewById(R.id.back);// 返回首页
		mIv_back = (RelativeLayout) findViewById(R.id.iv_back);
		mTv_look_order = (TextView) findViewById(R.id.tv_look_order);
		mTv_name = (TextView) findViewById(R.id.tv_name);
		mTv_phone = (TextView) findViewById(R.id.tv_phone);
		mTv_address = (TextView) findViewById(R.id.tv_address);
		mTv_price = (TextView) findViewById(R.id.tv_price);

		initLayout();

	}

	private void initLayout() {
		mBack.setOnClickListener(this);
		mTv_look_order.setOnClickListener(this);
		mIv_back.setOnClickListener(this);
		HashMap<String, String> map = new HashMap<>();
		map.put("OPT", "40");
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
		map.put("gnum", mGnum);
		new HttpClientGet(PaySuccessActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("支付成功详情" + result);
				SuccessBean bean = GsonUtil.getInstance().fromJson(result, SuccessBean.class);
				mTv_name.setText("姓名: " + bean.getContentData().getDelivery().get(0).getCname());
				mTv_phone.setText("电话: " + bean.getContentData().getDelivery().get(0).getCphone());
				mTv_address.setText("地址: " + bean.getContentData().getDelivery().get(0).getCaddr());
				mTv_price.setText("￥" + bean.getContentData().getOrder_price());
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, SixthNewActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			mIntent = new Intent(PaySuccessActivity.this, SixthNewActivity.class);
			startActivity(mIntent);
			finish();
			break;
		case R.id.iv_back:
			mIntent = new Intent(PaySuccessActivity.this, SixthNewActivity.class);
			startActivity(mIntent);
			finish();
			break;
		case R.id.tv_look_order:
			mIntent = new Intent(PaySuccessActivity.this, WaitSendGoodsOrderDetailActivity.class);
			mIntent.putExtra("indentNo", mGnum);
			startActivity(mIntent);
			finish();
			break;

		}
	}

	public class SuccessBean {
		public SuccessInfo contentData;
		public String error;
		public String msg;

		public SuccessInfo getContentData() {
			return contentData;
		}

		public void setContentData(SuccessInfo contentData) {
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

	public class SuccessInfo {
		public String assess_order;
		public String bask_order;
		public List<SuccessDetail> delivery;
		public String gid;
		public String gnum;
		public String gsname;
		public String order_price;
		public String order_time;
		public String pay_state;
		// private String pay_time Object
		public String transflow;

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

		public List<SuccessDetail> getDelivery() {
			return delivery;
		}

		public void setDelivery(List<SuccessDetail> delivery) {
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

	public class SuccessDetail {
		public String aid;
		public String caddr;
		public String clocation;
		public String cname;
		public String cphone;
		public String dcolor;
		public String dnumber;
		public String dsimg;
		public String dsize;
		public String dsname;
		public String entityId;
		public String gmid;
		public String id;
		public String kid;
		public String persistent;
		public String service_price;

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
