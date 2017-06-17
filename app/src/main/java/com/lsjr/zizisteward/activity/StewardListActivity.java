package com.lsjr.zizisteward.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.BaseViewHolder;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class StewardListActivity extends BaseActivity {

	private String mStname2;
	private ListView mListview;
	private List<GuanJiaListBean.GuanJiaListDetail> list = new ArrayList<>();

	@Override
	public int getContainerView() {
		return R.layout.activity_steward_list;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("管家列表");
		mStname2 = getIntent().getStringExtra("stname2");
		mListview = (ListView) findViewById(R.id.listview);
		getData();

	}

	private void getData() {
		HashMap<String, String> map = new HashMap<>();
		map.put("OPT", "241");
		map.put("stname", mStname2);
		new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("管家列表" + result);
				GuanJiaListBean bean = GsonUtil.getInstance().fromJson(result, GuanJiaListBean.class);
				list = bean.getStewards();
				StewardListAdapter adapter = new StewardListAdapter();
				mListview.setAdapter(adapter);

			}
		});
	}

	public class StewardListAdapter extends BaseAdapter {

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
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_guanjia_list, null);
			}
			ImageView iv_photo = BaseViewHolder.get(convertView, R.id.iv_photo);
			TextView tv_name = BaseViewHolder.get(convertView, R.id.tv_name);
			TextView tv_label = BaseViewHolder.get(convertView, R.id.tv_label);
			ImageView xing_one = BaseViewHolder.get(convertView, R.id.xing_one);
			ImageView xing_two = BaseViewHolder.get(convertView, R.id.xing_two);
			ImageView xing_three = BaseViewHolder.get(convertView, R.id.xing_three);
			ImageView xing_four = BaseViewHolder.get(convertView, R.id.xing_four);
			ImageView xing_five = BaseViewHolder.get(convertView, R.id.xing_five);
			Picasso.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + list.get(position).getGphoto())
					.into(iv_photo);
			tv_name.setText(list.get(position).getGuser_name());
			tv_label.setText(list.get(position).getSteward_territory());
			if ("1".equals(list.get(position).getSteward_rank())) {
				xing_one.setVisibility(View.VISIBLE);
				xing_two.setVisibility(View.GONE);
				xing_three.setVisibility(View.GONE);
				xing_four.setVisibility(View.GONE);
				xing_five.setVisibility(View.GONE);
			} else if ("2".equals(list.get(position).getSteward_rank())) {
				xing_one.setVisibility(View.VISIBLE);
				xing_two.setVisibility(View.VISIBLE);
				xing_three.setVisibility(View.GONE);
				xing_four.setVisibility(View.GONE);
				xing_five.setVisibility(View.GONE);
			} else if ("3".equals(list.get(position).getSteward_rank())) {
				xing_one.setVisibility(View.VISIBLE);
				xing_two.setVisibility(View.VISIBLE);
				xing_three.setVisibility(View.VISIBLE);
				xing_four.setVisibility(View.GONE);
				xing_five.setVisibility(View.GONE);
			} else if ("4".equals(list.get(position).getSteward_rank())) {
				xing_one.setVisibility(View.VISIBLE);
				xing_two.setVisibility(View.VISIBLE);
				xing_three.setVisibility(View.VISIBLE);
				xing_four.setVisibility(View.VISIBLE);
				xing_five.setVisibility(View.VISIBLE);
			} else if ("5".equals(list.get(position).getSteward_rank())) {
				xing_one.setVisibility(View.VISIBLE);
				xing_two.setVisibility(View.VISIBLE);
				xing_three.setVisibility(View.VISIBLE);
				xing_four.setVisibility(View.VISIBLE);
				xing_five.setVisibility(View.VISIBLE);
			}

			mListview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
					Intent intent = new Intent(getApplicationContext(), ExclusiveStewardActivity.class);
					intent.putExtra("name", list.get(position2).getGname());
					intent.putExtra("list", "0");
					startActivity(intent);
				}
			});

			return convertView;
		}

	}

	public class GuanJiaListBean {
		private String error;
		private String msg;
		private List<GuanJiaListDetail> stewards;

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

		public List<GuanJiaListDetail> getStewards() {
			return stewards;
		}

		public void setStewards(List<GuanJiaListDetail> stewards) {
			this.stewards = stewards;
		}

		private class GuanJiaListDetail {
			private String customer_number;
			private String device_id;
			private String gemail;
			private String gid;
			private String gmobile;
			private String gmobile1;
			private String gname;
			private String gphoto;
			private String gposition;
			private String greality_name;
			private String gsex;
			private String guser_name;
			private String id;
			private String is_allow_login;
			private String is_email_verified;
			private String is_login;
			private String is_mobile_verified;
			private String is_password_error_locked;
			private String is_pay_password_error_locked;
			private String is_secret_set;
			private String is_spare;
			private String last_login_ip;
			private String login_count;
			private String password;
			private String password_continuous_errors;
			private String pay_password;
			private String pay_password_continuous_errors;
			private String persistent;
			private String steward_address;
			private String steward_rank;
			private String steward_state;
			private String steward_territory;
			private String user_number;

			public String getCustomer_number() {
				return customer_number;
			}

			public void setCustomer_number(String customer_number) {
				this.customer_number = customer_number;
			}

			public String getDevice_id() {
				return device_id;
			}

			public void setDevice_id(String device_id) {
				this.device_id = device_id;
			}

			public String getGemail() {
				return gemail;
			}

			public void setGemail(String gemail) {
				this.gemail = gemail;
			}

			public String getGid() {
				return gid;
			}

			public void setGid(String gid) {
				this.gid = gid;
			}

			public String getGmobile() {
				return gmobile;
			}

			public void setGmobile(String gmobile) {
				this.gmobile = gmobile;
			}

			public String getGmobile1() {
				return gmobile1;
			}

			public void setGmobile1(String gmobile1) {
				this.gmobile1 = gmobile1;
			}

			public String getGname() {
				return gname;
			}

			public void setGname(String gname) {
				this.gname = gname;
			}

			public String getGphoto() {
				return gphoto;
			}

			public void setGphoto(String gphoto) {
				this.gphoto = gphoto;
			}

			public String getGposition() {
				return gposition;
			}

			public void setGposition(String gposition) {
				this.gposition = gposition;
			}

			public String getGreality_name() {
				return greality_name;
			}

			public void setGreality_name(String greality_name) {
				this.greality_name = greality_name;
			}

			public String getGsex() {
				return gsex;
			}

			public void setGsex(String gsex) {
				this.gsex = gsex;
			}

			public String getGuser_name() {
				return guser_name;
			}

			public void setGuser_name(String guser_name) {
				this.guser_name = guser_name;
			}

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getIs_allow_login() {
				return is_allow_login;
			}

			public void setIs_allow_login(String is_allow_login) {
				this.is_allow_login = is_allow_login;
			}

			public String getIs_email_verified() {
				return is_email_verified;
			}

			public void setIs_email_verified(String is_email_verified) {
				this.is_email_verified = is_email_verified;
			}

			public String getIs_login() {
				return is_login;
			}

			public void setIs_login(String is_login) {
				this.is_login = is_login;
			}

			public String getIs_mobile_verified() {
				return is_mobile_verified;
			}

			public void setIs_mobile_verified(String is_mobile_verified) {
				this.is_mobile_verified = is_mobile_verified;
			}

			public String getIs_password_error_locked() {
				return is_password_error_locked;
			}

			public void setIs_password_error_locked(String is_password_error_locked) {
				this.is_password_error_locked = is_password_error_locked;
			}

			public String getIs_pay_password_error_locked() {
				return is_pay_password_error_locked;
			}

			public void setIs_pay_password_error_locked(String is_pay_password_error_locked) {
				this.is_pay_password_error_locked = is_pay_password_error_locked;
			}

			public String getIs_secret_set() {
				return is_secret_set;
			}

			public void setIs_secret_set(String is_secret_set) {
				this.is_secret_set = is_secret_set;
			}

			public String getIs_spare() {
				return is_spare;
			}

			public void setIs_spare(String is_spare) {
				this.is_spare = is_spare;
			}

			public String getLast_login_ip() {
				return last_login_ip;
			}

			public void setLast_login_ip(String last_login_ip) {
				this.last_login_ip = last_login_ip;
			}

			public String getLogin_count() {
				return login_count;
			}

			public void setLogin_count(String login_count) {
				this.login_count = login_count;
			}

			public String getPassword() {
				return password;
			}

			public void setPassword(String password) {
				this.password = password;
			}

			public String getPassword_continuous_errors() {
				return password_continuous_errors;
			}

			public void setPassword_continuous_errors(String password_continuous_errors) {
				this.password_continuous_errors = password_continuous_errors;
			}

			public String getPay_password() {
				return pay_password;
			}

			public void setPay_password(String pay_password) {
				this.pay_password = pay_password;
			}

			public String getPay_password_continuous_errors() {
				return pay_password_continuous_errors;
			}

			public void setPay_password_continuous_errors(String pay_password_continuous_errors) {
				this.pay_password_continuous_errors = pay_password_continuous_errors;
			}

			public String getPersistent() {
				return persistent;
			}

			public void setPersistent(String persistent) {
				this.persistent = persistent;
			}

			public String getSteward_address() {
				return steward_address;
			}

			public void setSteward_address(String steward_address) {
				this.steward_address = steward_address;
			}

			public String getSteward_rank() {
				return steward_rank;
			}

			public void setSteward_rank(String steward_rank) {
				this.steward_rank = steward_rank;
			}

			public String getSteward_state() {
				return steward_state;
			}

			public void setSteward_state(String steward_state) {
				this.steward_state = steward_state;
			}

			public String getSteward_territory() {
				return steward_territory;
			}

			public void setSteward_territory(String steward_territory) {
				this.steward_territory = steward_territory;
			}

			public String getUser_number() {
				return user_number;
			}

			public void setUser_number(String user_number) {
				this.user_number = user_number;
			}
		}
	}
}
