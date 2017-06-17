package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.bean.OrderBean;
import com.lsjr.zizisteward.bean.OrderBean.Delivery;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.newview.MyListView;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class WaitSendGoodsOrderDetailActivity extends BaseActivity {

	private String mIndentNo;
	private List<Delivery> list = new ArrayList<Delivery>();
	private TextView mTv_total_price;
	private TextView mTv_name;
	private TextView mTv_phone;
	private TextView mTv_address;
	private TextView mTv_order_num;
	private TextView mTv_trde_num;
	private TextView mTv_order_time;
	private TextView mTv_trde_time;
	private TextView mTv_look_wuliu;
	private MyListView mListview;

	@Override
	public int getContainerView() {
		return R.layout.activity_wait_send_goods;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("提醒发货");
		mIndentNo = getIntent().getStringExtra("indentNo");
		mListview = (MyListView) findViewById(R.id.listview);
		mTv_total_price = (TextView) findViewById(R.id.tv_total_price);
		mTv_name = (TextView) findViewById(R.id.tv_name);
		mTv_phone = (TextView) findViewById(R.id.tv_phone);
		mTv_address = (TextView) findViewById(R.id.tv_address);
		mTv_order_num = (TextView) findViewById(R.id.tv_order_num);
		mTv_trde_num = (TextView) findViewById(R.id.tv_trde_num);
		mTv_order_time = (TextView) findViewById(R.id.tv_order_time);
		mTv_trde_time = (TextView) findViewById(R.id.tv_trde_time);
		mTv_look_wuliu = (TextView) findViewById(R.id.tv_look_wuliu);
		mTv_look_wuliu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(WaitSendGoodsOrderDetailActivity.this, R.style.dialog);
				dialog.setContentView(R.layout.dialog_send_goods);
				Window window = dialog.getWindow();
				window.setGravity(Gravity.CENTER | Gravity.CENTER);
				TextView tv_confirm = (TextView) dialog.findViewById(R.id.tv_confirm);
				tv_confirm.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						HashMap<String, String> map = new HashMap<>();
						map.put("OPT", "51");
						map.put("gnum", mIndentNo);
						new HttpClientGet(WaitSendGoodsOrderDetailActivity.this, null, map, false,
								new HttpClientGet.CallBacks<String>() {

									@Override
									public void onSuccess(String result) {
										BasicParameterBean bean = GsonUtil.getInstance().fromJson(result,
												BasicParameterBean.class);
										Toast.makeText(WaitSendGoodsOrderDetailActivity.this, bean.getMsg(),
												Toast.LENGTH_SHORT).show();

									}

								});

					}
				});

				dialog.show();
			}
		});
		getData();
	}

	private void getData() {
		HashMap<String, String> map = new HashMap<>();
		map.put("OPT", "40");
		map.put("id", App.getUserInfo().getId());
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
		map.put("gnum", mIndentNo);
		new HttpClientGet(WaitSendGoodsOrderDetailActivity.this, null, map, false,
				new HttpClientGet.CallBacks<String>() {

					@Override
					public void onSuccess(String result) {
						System.out.println("提醒发货详情" + result);
						final OrderBean bean = GsonUtil.getInstance().fromJson(result, OrderBean.class);
						list = bean.getContentData().getDelivery();
						SendGoodsAdapter adapter = new SendGoodsAdapter(WaitSendGoodsOrderDetailActivity.this, list);
						mListview.setAdapter(adapter);

						mTv_total_price.setText("合计: ￥" + bean.getContentData().getOrder_price());
						mTv_name.setText(list.get(0).getCname());
						mTv_phone.setText(list.get(0).getCphone());
						mTv_address.setText(list.get(0).getCaddr());
						mTv_order_num.setText("订单编号 : " + mIndentNo);
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");
						if (TextUtils.isEmpty(bean.getContentData().getOrder_time())) {
							mTv_order_time.setText("下单时间 : ");
						} else {
							String time1 = formatter.format(Long.valueOf(bean.getContentData().getOrder_time()));
							mTv_order_time.setText("下单时间 : " + time1);
						}
						if (TextUtils.isEmpty(bean.getContentData().getPay_time())) {
							mTv_trde_time.setText("支付时间 : ");
						} else {
							String time2 = formatter.format(Long.valueOf(bean.getContentData().getPay_time()));
							mTv_trde_time.setText("支付时间 : " + time2);
						}
						mTv_trde_num.setText("交易流水号 : " + bean.getContentData().getTransflow());
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

	private class SendGoodsAdapter extends BaseAdapter {
		private Context context;
		private ViewHolder mHolder;
		private List<Delivery> list;

		public SendGoodsAdapter(Context context, List<Delivery> list) {
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

}
