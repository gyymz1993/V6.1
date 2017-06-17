package com.lsjr.zizisteward.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.BaseViewHolder;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.ToastUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TravelPlanActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout mRe_zuo, car_zuo, car_you, mRe_you, jiu_one, jiu_two, jiu_three, jiu_four, jiu_five, eat_one,
			eat_two, eat_three;
	private ImageView iv_one_he, iv_one_bai, three_bai, three_he, jiu_bai1, jiu_bai2, jiu_bai3, jiu_bai4, jiu_bai5,
			jiu_he1, jiu_he2, jiu_he3, jiu_he4, jiu_he5, car_one_he, car_one_bai, car_two_bai, car_two_he, eat_bai1,
			eat_bai2, eat_bai3, eat_he1, eat_he3, eat_he2;
	private String mNew_end_time;
	private TextView mTijiao, tv_zuche, tv_siji, tv_wifi, tv_daoyou, tv_baoxian, tv_qianzheng, tv_chumenpiuao, tv_fanyi;
	private String mDestination;
	private String mOrigin;
	private String mStart;
	private String mEnd;
	private String mDays;
	private String mDocument;
	private String mChildren;
	private String mState;
	private TextView mTv_gexing;
	private TextView mTv_shangwu;
	private TextView mSixing;
	private TextView mWuxing;
	private TextView mHaohua;
	private TextView mTese;
	private TextView mBuxian;
	private TextView mTv_tese;
	private TextView mTv_zili;
	private TextView mTv_tuijian;
	private TextView mTv_yao;
	private TextView mTv_buyao;
	private EditText mEt_content;

	private String mNew_start_time;

	@Override
	public int getContainerView() {
		return R.layout.activity_travel_plan;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("定制出行");
		Intent intent = getIntent();
		mDestination = intent.getStringExtra("destination");
		mOrigin = intent.getStringExtra("origin");
		mStart = intent.getStringExtra("start");
		mEnd = intent.getStringExtra("end");
		mDays = intent.getStringExtra("days");
		mDocument = intent.getStringExtra("document");
		mChildren = intent.getStringExtra("children");
		mState = intent.getStringExtra("state");

		TextView tv_chufadi = (TextView) findViewById(R.id.tv_chufadi);
		TextView tv_mudedi = (TextView) findViewById(R.id.tv_mudedi);
		TextView tv_date = (TextView) findViewById(R.id.tv_date);
		TextView tv_days = (TextView) findViewById(R.id.tv_days);
		TextView tv_add_days = (TextView) findViewById(R.id.tv_add_days);
		TextView tv_people = (TextView) findViewById(R.id.tv_people);

		tv_chufadi.setText(mOrigin);
		tv_mudedi.setText(mDestination);
		tv_days.setText(mDays + "天");
		if (mState.equals("1")) {
			tv_add_days.setText("可适当增减");
		}
		if (mState.equals("0")) {
			tv_add_days.setText("不可适当增减");
		}
		if (mDocument.equals("") || mDocument == null) {
			tv_people.setText(mChildren + "儿童");
		} else if (mChildren.equals("") || mChildren == null) {
			tv_people.setText(mDocument + "成人");
		} else {
			tv_people.setText(mDocument + "成人" + " / " + mChildren + "儿童");
		}

		mNew_start_time = mStart.substring(5, 11);

		if (!(mEnd.equals("") || mEnd == null)) {
			mNew_end_time = mEnd.substring(5, 11);

			tv_date.setText(mNew_start_time + "至" + "\n" + mNew_end_time + "出发");
		}
		if ((mEnd.equals("") || mEnd == null)) {
			tv_date.setText(mNew_start_time + "出发");
		}

		mRe_zuo = (RelativeLayout) findViewById(R.id.re_zuo);
		mRe_you = (RelativeLayout) findViewById(R.id.re_you);
		jiu_one = (RelativeLayout) findViewById(R.id.jiu_one);
		jiu_two = (RelativeLayout) findViewById(R.id.jiu_two);
		jiu_three = (RelativeLayout) findViewById(R.id.jiu_three);
		jiu_four = (RelativeLayout) findViewById(R.id.jiu_four);
		jiu_five = (RelativeLayout) findViewById(R.id.jiu_five);

		car_zuo = (RelativeLayout) findViewById(R.id.car_zuo);
		car_you = (RelativeLayout) findViewById(R.id.car_you);

		car_one_he = (ImageView) findViewById(R.id.car_one_he);
		car_one_bai = (ImageView) findViewById(R.id.car_one_bai);
		car_two_bai = (ImageView) findViewById(R.id.car_two_bai);
		car_two_he = (ImageView) findViewById(R.id.car_two_he);

		car_one_he.setVisibility(View.VISIBLE);
		car_one_bai.setVisibility(View.GONE);
		car_two_bai.setVisibility(View.VISIBLE);
		car_two_he.setVisibility(View.GONE);

		iv_one_he = (ImageView) findViewById(R.id.iv_one_he);
		iv_one_bai = (ImageView) findViewById(R.id.iv_one_bai);
		three_bai = (ImageView) findViewById(R.id.three_bai);
		three_he = (ImageView) findViewById(R.id.three_he);

		jiu_bai1 = (ImageView) findViewById(R.id.jiu_bai1);
		jiu_bai2 = (ImageView) findViewById(R.id.jiu_bai2);
		jiu_bai3 = (ImageView) findViewById(R.id.jiu_bai3);
		jiu_bai4 = (ImageView) findViewById(R.id.jiu_bai4);
		jiu_bai5 = (ImageView) findViewById(R.id.jiu_bai5);

		jiu_he1 = (ImageView) findViewById(R.id.jiu_he1);
		jiu_he2 = (ImageView) findViewById(R.id.jiu_he2);
		jiu_he3 = (ImageView) findViewById(R.id.jiu_he3);
		jiu_he4 = (ImageView) findViewById(R.id.jiu_he4);
		jiu_he5 = (ImageView) findViewById(R.id.jiu_he5);

		iv_one_he.setVisibility(View.VISIBLE);
		iv_one_bai.setVisibility(View.GONE);

		three_bai.setVisibility(View.VISIBLE);
		three_he.setVisibility(View.GONE);

		jiu_bai1.setVisibility(View.VISIBLE);
		jiu_bai2.setVisibility(View.GONE);
		jiu_bai3.setVisibility(View.VISIBLE);
		jiu_bai4.setVisibility(View.VISIBLE);
		jiu_bai5.setVisibility(View.VISIBLE);

		jiu_he1.setVisibility(View.GONE);
		jiu_he2.setVisibility(View.VISIBLE);
		jiu_he3.setVisibility(View.GONE);
		jiu_he4.setVisibility(View.GONE);
		jiu_he5.setVisibility(View.GONE);

		eat_one = (RelativeLayout) findViewById(R.id.eat_one);
		eat_two = (RelativeLayout) findViewById(R.id.eat_two);
		eat_three = (RelativeLayout) findViewById(R.id.eat_three);

		eat_bai1 = (ImageView) findViewById(R.id.eat_bai1);
		eat_bai2 = (ImageView) findViewById(R.id.eat_bai2);
		eat_bai3 = (ImageView) findViewById(R.id.eat_bai3);
		eat_he1 = (ImageView) findViewById(R.id.eat_he1);
		eat_he2 = (ImageView) findViewById(R.id.eat_he2);
		eat_he3 = (ImageView) findViewById(R.id.eat_he3);

		eat_bai1.setVisibility(View.VISIBLE);
		eat_bai2.setVisibility(View.GONE);
		eat_bai3.setVisibility(View.VISIBLE);
		eat_he1.setVisibility(View.GONE);
		eat_he2.setVisibility(View.VISIBLE);
		eat_he3.setVisibility(View.GONE);

		mTijiao = (TextView) findViewById(R.id.tijiao);

		// 文本 出行性质
		mTv_gexing = (TextView) findViewById(R.id.tv_gexing);
		mTv_shangwu = (TextView) findViewById(R.id.tv_shangwu);

		// 酒店要求
		mSixing = (TextView) findViewById(R.id.tv_1);
		mWuxing = (TextView) findViewById(R.id.tv_2);
		mHaohua = (TextView) findViewById(R.id.tv_3);
		mTese = (TextView) findViewById(R.id.tv_4);
		mBuxian = (TextView) findViewById(R.id.tv_5);

		// 用餐要求
		mTv_tese = (TextView) findViewById(R.id.tv_tese);
		mTv_zili = (TextView) findViewById(R.id.tv_zili);
		mTv_tuijian = (TextView) findViewById(R.id.tv_tuijian);

		// 专车接送
		mTv_yao = (TextView) findViewById(R.id.tv_yao);
		mTv_buyao = (TextView) findViewById(R.id.tv_buyao);

		mEt_content = (EditText) findViewById(R.id.et_content);

		// 特色定制

		tv_zuche = (TextView) findViewById(R.id.tv_zuche);
		tv_siji = (TextView) findViewById(R.id.tv_siji);
		tv_wifi = (TextView) findViewById(R.id.tv_wifi);
		tv_daoyou = (TextView) findViewById(R.id.tv_daoyou);
		tv_baoxian = (TextView) findViewById(R.id.tv_baoxian);
		tv_qianzheng = (TextView) findViewById(R.id.tv_qianzheng);
		tv_chumenpiuao = (TextView) findViewById(R.id.tv_chumenpiuao);
		tv_fanyi = (TextView) findViewById(R.id.tv_fanyi);

		init();
		getData();

	}

	private void getData() {
		HashMap<String, String> map = new HashMap<>();
		map.put("OPT", "60");
		new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("多选" + result);
			}

			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});
	}

	private void init() {
		mRe_zuo.setOnClickListener(this);
		mRe_you.setOnClickListener(this);
		jiu_one.setOnClickListener(this);
		jiu_two.setOnClickListener(this);
		jiu_three.setOnClickListener(this);
		jiu_four.setOnClickListener(this);
		jiu_five.setOnClickListener(this);
		car_zuo.setOnClickListener(this);
		car_you.setOnClickListener(this);
		eat_one.setOnClickListener(this);
		eat_two.setOnClickListener(this);
		eat_three.setOnClickListener(this);
		mTijiao.setOnClickListener(this);
		tv_zuche.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tv_zuche.getCurrentTextColor() == Color.BLACK) {
					tv_zuche.setTextColor(Color.parseColor("#b79771"));
				} else {
					tv_zuche.setTextColor(Color.BLACK);
				}
			}
		});
		tv_siji.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tv_siji.getCurrentTextColor() == Color.BLACK) {
					tv_siji.setTextColor(Color.parseColor("#b79771"));
				} else {
					tv_siji.setTextColor(Color.BLACK);
				}
			}
		});
		tv_wifi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tv_wifi.getCurrentTextColor() == Color.BLACK) {
					tv_wifi.setTextColor(Color.parseColor("#b79771"));
				} else {
					tv_wifi.setTextColor(Color.BLACK);
				}
			}
		});
		tv_daoyou.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tv_daoyou.getCurrentTextColor() == Color.BLACK) {
					tv_daoyou.setTextColor(Color.parseColor("#b79771"));
				} else {
					tv_daoyou.setTextColor(Color.BLACK);
				}
			}
		});
		tv_baoxian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tv_baoxian.getCurrentTextColor() == Color.BLACK) {
					tv_baoxian.setTextColor(Color.parseColor("#b79771"));
				} else {
					tv_baoxian.setTextColor(Color.BLACK);
				}
			}
		});
		tv_qianzheng.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tv_qianzheng.getCurrentTextColor() == Color.BLACK) {
					tv_qianzheng.setTextColor(Color.parseColor("#b79771"));
				} else {
					tv_qianzheng.setTextColor(Color.BLACK);
				}
			}
		});
		tv_chumenpiuao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tv_chumenpiuao.getCurrentTextColor() == Color.BLACK) {
					tv_chumenpiuao.setTextColor(Color.parseColor("#b79771"));
				} else {
					tv_chumenpiuao.setTextColor(Color.BLACK);
				}
			}
		});
		tv_fanyi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tv_fanyi.getCurrentTextColor() == Color.BLACK) {
					tv_fanyi.setTextColor(Color.parseColor("#b79771"));
				} else {
					tv_fanyi.setTextColor(Color.BLACK);
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.re_zuo:
			if (iv_one_he.getVisibility() == View.GONE && iv_one_bai.getVisibility() == View.VISIBLE) {
				iv_one_he.setVisibility(View.VISIBLE);
				iv_one_bai.setVisibility(View.GONE);

				three_bai.setVisibility(View.VISIBLE);
				three_he.setVisibility(View.GONE);

			}
			break;
		case R.id.re_you:
			if (three_bai.getVisibility() == View.VISIBLE && three_he.getVisibility() == View.GONE) {
				three_bai.setVisibility(View.GONE);
				three_he.setVisibility(View.VISIBLE);

				iv_one_he.setVisibility(View.GONE);
				iv_one_bai.setVisibility(View.VISIBLE);

			}

			break;
		case R.id.jiu_one:
			if (jiu_bai1.getVisibility() == View.VISIBLE && jiu_he1.getVisibility() == View.GONE) {
				jiu_bai1.setVisibility(View.GONE);
				jiu_he1.setVisibility(View.VISIBLE);

				jiu_bai2.setVisibility(View.VISIBLE);
				jiu_bai3.setVisibility(View.VISIBLE);
				jiu_bai4.setVisibility(View.VISIBLE);
				jiu_bai5.setVisibility(View.VISIBLE);

				jiu_he2.setVisibility(View.GONE);
				jiu_he3.setVisibility(View.GONE);
				jiu_he4.setVisibility(View.GONE);
				jiu_he5.setVisibility(View.GONE);

			}
			break;
		case R.id.jiu_two:
			if (jiu_bai2.getVisibility() == View.VISIBLE && jiu_he2.getVisibility() == View.GONE) {
				jiu_bai2.setVisibility(View.GONE);
				jiu_he2.setVisibility(View.VISIBLE);

				jiu_bai1.setVisibility(View.VISIBLE);
				jiu_bai3.setVisibility(View.VISIBLE);
				jiu_bai4.setVisibility(View.VISIBLE);
				jiu_bai5.setVisibility(View.VISIBLE);

				jiu_he1.setVisibility(View.GONE);
				jiu_he3.setVisibility(View.GONE);
				jiu_he4.setVisibility(View.GONE);
				jiu_he5.setVisibility(View.GONE);

			}

			break;
		case R.id.jiu_three:
			if (jiu_bai3.getVisibility() == View.VISIBLE && jiu_he3.getVisibility() == View.GONE) {
				jiu_bai3.setVisibility(View.GONE);
				jiu_he3.setVisibility(View.VISIBLE);

				jiu_bai1.setVisibility(View.VISIBLE);
				jiu_bai2.setVisibility(View.VISIBLE);
				jiu_bai4.setVisibility(View.VISIBLE);
				jiu_bai5.setVisibility(View.VISIBLE);

				jiu_he2.setVisibility(View.GONE);
				jiu_he1.setVisibility(View.GONE);
				jiu_he4.setVisibility(View.GONE);
				jiu_he5.setVisibility(View.GONE);

			}
			break;
		case R.id.jiu_four:
			if (jiu_bai4.getVisibility() == View.VISIBLE && jiu_he4.getVisibility() == View.GONE) {
				jiu_bai4.setVisibility(View.GONE);
				jiu_he4.setVisibility(View.VISIBLE);

				jiu_bai1.setVisibility(View.VISIBLE);
				jiu_bai2.setVisibility(View.VISIBLE);
				jiu_bai3.setVisibility(View.VISIBLE);
				jiu_bai5.setVisibility(View.VISIBLE);

				jiu_he2.setVisibility(View.GONE);
				jiu_he3.setVisibility(View.GONE);
				jiu_he1.setVisibility(View.GONE);
				jiu_he5.setVisibility(View.GONE);

			}
			break;
		case R.id.jiu_five:
			if (jiu_bai5.getVisibility() == View.VISIBLE && jiu_he5.getVisibility() == View.GONE) {
				jiu_bai5.setVisibility(View.GONE);
				jiu_he5.setVisibility(View.VISIBLE);

				jiu_bai1.setVisibility(View.VISIBLE);
				jiu_bai2.setVisibility(View.VISIBLE);
				jiu_bai3.setVisibility(View.VISIBLE);
				jiu_bai4.setVisibility(View.VISIBLE);

				jiu_he2.setVisibility(View.GONE);
				jiu_he3.setVisibility(View.GONE);
				jiu_he4.setVisibility(View.GONE);
				jiu_he1.setVisibility(View.GONE);

			}
			break;
		case R.id.car_zuo:
			if (car_one_he.getVisibility() == View.GONE && car_one_bai.getVisibility() == View.VISIBLE) {
				car_one_bai.setVisibility(View.GONE);
				car_one_he.setVisibility(View.VISIBLE);

				car_two_bai.setVisibility(View.VISIBLE);
				car_two_he.setVisibility(View.GONE);

			}
			break;
		case R.id.car_you:
			if (car_two_he.getVisibility() == View.GONE && car_two_bai.getVisibility() == View.VISIBLE) {
				car_two_bai.setVisibility(View.GONE);
				car_two_he.setVisibility(View.VISIBLE);

				car_one_bai.setVisibility(View.VISIBLE);
				car_one_he.setVisibility(View.GONE);

			}

			break;
		case R.id.eat_one:
			if (eat_bai1.getVisibility() == View.VISIBLE && eat_he1.getVisibility() == View.GONE) {

				eat_bai1.setVisibility(View.GONE);
				eat_he1.setVisibility(View.VISIBLE);

				eat_bai2.setVisibility(View.VISIBLE);
				eat_he2.setVisibility(View.GONE);

				eat_bai3.setVisibility(View.VISIBLE);
				eat_he3.setVisibility(View.GONE);

			}
			break;
		case R.id.eat_two:
			if (eat_bai2.getVisibility() == View.VISIBLE && eat_he2.getVisibility() == View.GONE) {

				eat_bai1.setVisibility(View.VISIBLE);
				eat_he1.setVisibility(View.GONE);

				eat_bai2.setVisibility(View.GONE);
				eat_he2.setVisibility(View.VISIBLE);

				eat_bai3.setVisibility(View.VISIBLE);
				eat_he3.setVisibility(View.GONE);

			}
			break;
		case R.id.eat_three:
			if (eat_bai3.getVisibility() == View.VISIBLE && eat_he3.getVisibility() == View.GONE) {

				eat_bai1.setVisibility(View.VISIBLE);
				eat_he1.setVisibility(View.GONE);

				eat_bai2.setVisibility(View.VISIBLE);
				eat_he2.setVisibility(View.GONE);

				eat_bai3.setVisibility(View.GONE);
				eat_he3.setVisibility(View.VISIBLE);

			}
			break;
		case R.id.tijiao:// 提交
			mTv_1 = tv_zuche.getText().toString().trim();
			mTv_2 = tv_siji.getText().toString().trim();
			mTv_3 = tv_wifi.getText().toString().trim();
			mTv_4 = tv_daoyou.getText().toString().trim();
			mTv_5 = tv_baoxian.getText().toString().trim();
			mTv_6 = tv_qianzheng.getText().toString().trim();
			mTv_7 = tv_chumenpiuao.getText().toString().trim();
			mTv_8 = tv_fanyi.getText().toString().trim();

			if (tv_zuche.getCurrentTextColor() == Color.BLACK && tv_siji.getCurrentTextColor() == Color.BLACK
					&& tv_wifi.getCurrentTextColor() == Color.BLACK && tv_daoyou.getCurrentTextColor() == Color.BLACK
					&& tv_baoxian.getCurrentTextColor() == Color.BLACK
					&& tv_qianzheng.getCurrentTextColor() == Color.BLACK
					&& tv_chumenpiuao.getCurrentTextColor() == Color.BLACK
					&& tv_fanyi.getCurrentTextColor() == Color.BLACK) {
				ToastUtils.show(getApplicationContext(), "请至少选择一种特色定制...");

				return;

			}

			HashMap<String, String> map = new HashMap<String, String>();
			map.put("OPT", "57");
			map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
			map.put("bourn", mDestination);
			map.put("destination", mOrigin);

			String first_time = mStart.substring(0, 4) + "-" + mStart.substring(5, 7) + "-" + mStart.substring(8, 10);

			map.put("first_departure_date", first_time);
			System.out.println("出发日期  " + first_time);
			if (TextUtils.isEmpty(mEnd)) {
				map.put("last_departure_date", "");
			} else {
				String lat_time = mEnd.substring(0, 4) + "-" + mEnd.substring(5, 7) + "-" + mEnd.substring(8, 10);
				map.put("last_departure_date", lat_time);
			}

			map.put("trip_days", mDays);

			if (mDocument.equals("") || mDocument == null) {
				map.put("trip_adults", "");
			} else {
				map.put("trip_adults", mDocument);

			}

			if (mChildren.equals("") || mChildren == null) {
				map.put("trip_childs", "");
			} else {
				map.put("trip_childs", mChildren);
			}

			if (iv_one_he.getVisibility() == View.VISIBLE) {// 出行性质
				map.put("trip_nature", "0");
			} else {
				map.put("trip_nature", "1");
			}

			if (jiu_he1.getVisibility() == View.VISIBLE) {// 酒店要求
				map.put("hotel_ask_for", "0");
			} else if (jiu_he2.getVisibility() == View.VISIBLE) {
				map.put("hotel_ask_for", "1");
			} else if (jiu_he3.getVisibility() == View.VISIBLE) {
				map.put("hotel_ask_for", "2");
			} else if (jiu_he4.getVisibility() == View.VISIBLE) {
				map.put("hotel_ask_for", "3");
			} else if (jiu_he5.getVisibility() == View.VISIBLE) {
				map.put("hotel_ask_for", "4");
			}

			if (eat_he1.getVisibility() == View.VISIBLE) {// 用餐要求
				map.put("diming_ask_for", "0");
			} else if (eat_he2.getVisibility() == View.VISIBLE) {
				map.put("diming_ask_for", "1");
			} else if (eat_he3.getVisibility() == View.VISIBLE) {
				map.put("diming_ask_for", "2");
			}

			if (car_one_he.getVisibility() == View.VISIBLE) {// 专车接送
				map.put("shuttle_bus", "0");

			} else if (car_two_he.getVisibility() == View.VISIBLE) {
				map.put("shuttle_bus", "1");
			}

			// 其他要求
			if (TextUtils.isEmpty(mEt_content.getText().toString())) {
				map.put("other_demand", "");
			} else {
				map.put("other_demand", mEt_content.getText().toString());
			}
			List<String> list = new ArrayList<>();
			if (tv_zuche.getCurrentTextColor() == Color.parseColor("#b79771")) {// 租车
				list.add(mTv_1);
			}

			if (tv_siji.getCurrentTextColor() == Color.parseColor("#b79771")) {
				list.add(mTv_2);
			}

			if (tv_wifi.getCurrentTextColor() == Color.parseColor("#b79771")) {
				list.add(mTv_3);
			}

			if (tv_daoyou.getCurrentTextColor() == Color.parseColor("#b79771")) {
				list.add(mTv_4);
			}

			if (tv_baoxian.getCurrentTextColor() == Color.parseColor("#b79771")) {
				list.add(mTv_5);
			}

			if (tv_qianzheng.getCurrentTextColor() == Color.parseColor("#b79771")) {
				list.add(mTv_6);
			}

			if (tv_chumenpiuao.getCurrentTextColor() == Color.parseColor("#b79771")) {
				list.add(mTv_7);
			}

			if (tv_fanyi.getCurrentTextColor() == Color.parseColor("#b79771")) {
				list.add(mTv_8);
			}

			for (int i = 0; i < list.size(); i++) {
				System.out.println("所有的" + list.get(i));
			}
			if (list.size() == 1) {
				map.put("feature_custom", list.get(0));// 特色定制
			} else if (list.size() == 2) {
				map.put("feature_custom", list.get(0) + "," + list.get(1));// 特色定制
			} else if (list.size() == 3) {
				map.put("feature_custom", list.get(0) + "," + list.get(1) + "," + list.get(2));// 特色定制
			} else if (list.size() == 4) {
				map.put("feature_custom", list.get(0) + "," + list.get(1) + "," + list.get(2) + "," + list.get(3));// 特色定制
			} else if (list.size() == 5) {
				map.put("feature_custom",
						list.get(0) + "," + list.get(1) + "," + list.get(2) + "," + list.get(3) + "," + list.get(4));// 特色定制
			} else if (list.size() == 6) {
				map.put("feature_custom", list.get(0) + "," + list.get(1) + "," + list.get(2) + "," + list.get(3) + ","
						+ list.get(4) + "," + list.get(5));// 特色定制
			} else if (list.size() == 7) {
				map.put("feature_custom", list.get(0) + "," + list.get(1) + "," + list.get(2) + "," + list.get(3) + ","
						+ list.get(4) + "," + list.get(5) + "," + list.get(6));// 特色定制
			} else if (list.size() == 8) {
				map.put("feature_custom", list.get(0) + "," + list.get(1) + "," + list.get(2) + "," + list.get(3) + ","
						+ list.get(4) + "," + list.get(5) + "," + list.get(6) + "," + list.get(7));// 特色定制
			}

			new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

				@Override
				public void onSuccess(String result) {
					System.out.println("什么玩意" + result);
					BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
					Toast.makeText(getApplicationContext(), bean.getMsg(), Toast.LENGTH_SHORT).show();
					Intent intent = getIntent();
					TravelPlanActivity.this.setResult(12, intent);
					TravelPlanActivity.this.finish();

				}

				@Override
				public void onFailure(MyError myError) {
					super.onFailure(myError);
				}
			});
			break;
		}
	}

	private String[] names = { "租车", "司机", "出境WiFi", "导游", "保险", "签证", "出门票", "翻译" };
	private String mTv_1;
	private String mTv_2;
	private String mTv_3;
	private String mTv_4;
	private String mTv_5;
	private String mTv_6;
	private String mTv_7;
	private String mTv_8;

	public class MySelectorAdapter extends BaseAdapter {
		private Context context;

		public MySelectorAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return names == null ? 0 : names.length;
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
				convertView = LayoutInflater.from(context).inflate(R.layout.item_duoxuan, parent, false);
			}
			final TextView tv_name = BaseViewHolder.get(convertView, R.id.tv_name);
			tv_name.setText(names[position]);

			return convertView;
		}

	}

}
