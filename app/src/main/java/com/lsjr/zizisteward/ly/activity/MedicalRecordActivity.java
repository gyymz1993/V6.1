package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.PullToRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicalRecordActivity extends Activity implements OnClickListener {
	
	/**返回*/
	private LinearLayout ll_back;
	private MyListView mlv;
	
	/**刷新、加载*/
	private PullToRefreshLayout ptrl;
	private TextView tv;
	private MRAdapter adapter;
	private LinearLayout ll_load;
	private LinearLayout ll_edit;
	private List<MRBean.Tsomr> tsomr;
	private TextView tv_prompt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.medical_record_activity);
		this.findViewById();
	}

	private void findViewById() {
		
		this.tv = (TextView) super.findViewById(R.id.tv);
		this.mlv = (MyListView) super.findViewById(R.id.mlv);
		this.tv_prompt = (TextView) super.findViewById(R.id.tv_prompt);
		this.ll_edit = (LinearLayout) super.findViewById(R.id.ll_edit);
		this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
		this.ptrl = (PullToRefreshLayout) super.findViewById(R.id.refresh_view);
		
		this.ptrl.setOnRefreshListener(new MyLinstener());
		
		this.ll_edit.setOnClickListener(this);
		this.ll_back.setOnClickListener(this);
		
		this.getData();
	}
	
	private void getData() {
		
		Map<String, String> map = new HashMap<>();
		map.put("OPT", "304");
		map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
		
		new HttpClientGet(this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("体检记录接口返回值:  " + result);
				MRBean mrBean = new Gson().fromJson(result, MRBean.class);
				tsomr = mrBean.getTsomr();
				
				if (null != tsomr && tsomr.size() > 0) {
					for (int i = 0; i < tsomr.size(); i++) {
						tsomr.get(i).setLy(0);
						tsomr.get(i).setSpace(0);
					}
				}
				
				if (null == tsomr || tsomr.size() < 1) {
					tv_prompt.setVisibility(View.VISIBLE);
					ptrl.setVisibility(View.GONE);
				} else {
					tv_prompt.setVisibility(View.GONE);
					ptrl.setVisibility(View.VISIBLE);
				}
				
				adapter = new MRAdapter(MedicalRecordActivity.this,tsomr);
				
				mlv.setAdapter(adapter);
			}
			
			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});
	}
	
	private class MyLinstener implements PullToRefreshLayout.OnRefreshListener {

		@Override
		public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
			ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
		}

		@Override
		public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
			ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
		}
	}
	
	private class MRAdapter extends BaseAdapter {
		private Context context;
		private ViewHolder view;
		private List<MRBean.Tsomr> tsomr;

		public MRAdapter(Context context,List<MRBean.Tsomr> tsomr) {
			this.context = context;
			this.tsomr = tsomr;
		}

		@Override
		public int getCount() {
			return null == tsomr ? 0 : tsomr.size();
		}

		@Override
		public Object getItem(int position) {
			return tsomr.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (null == convertView) {
				
				convertView = LayoutInflater.from(context).inflate(R.layout.medical_record_activity_item, null);
				
				view = new ViewHolder(convertView);
				
				convertView.setTag(view);
				
			} else {
				
				view = (ViewHolder) convertView.getTag();
				
			}
			
			String[] str = tsomr.get(position).getDescription().split(",");
			
			if (str.length == 2) {
				view.tv_select.setVisibility(View.VISIBLE);
				view.tv_name.setVisibility(View.GONE);
				view.tv_people_name.setVisibility(View.GONE);
				
				this.view.tv_title.setText(str[0]);
			} else {
				this.view.tv_select.setVisibility(View.GONE);
				this.view.tv_name.setVisibility(View.VISIBLE);
				this.view.tv_people_name.setVisibility(View.VISIBLE);
				
				this.view.tv_title.setText(str[0]);
				this.view.tv_people_name.setText(str[2] + "   " + str[3]);
				this.view.tv_name.setText(str[1]);
			}

			
			switch (this.tsomr.get(position).getSpace()) {
			
			case 0:
				this.view.tv_time.setVisibility(View.VISIBLE);
				this.view.tv_date.setVisibility(View.VISIBLE);
				this.view.ll_check.setVisibility(View.GONE);
				break;
				
			case 1:
				this.view.tv_time.setVisibility(View.GONE);
				this.view.tv_date.setVisibility(View.GONE);
				this.view.ll_check.setVisibility(View.VISIBLE);
				
				break;
			}
			
			switch (this.tsomr.get(position).getLy()) {
			
			case 0:
				this.view.iv_check.setImageResource(R.drawable.medical_record_check_false);
				break;
				
			case 1:
				this.view.iv_check.setImageResource(R.drawable.medical_record_check_true);
				break;
			}
			
			String time = tsomr.get(position).getRecord_time().getTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String[] tm = format.format(Long.parseLong(time)).split("-");
			this.view.tv_date.setText(tm[0]);
			this.view.tv_time.setText(tm[1] + " - " + tm[2]);
			
			this.view.ll_check.setTag(position);
			this.view.ll_check.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int pos = (int) v.getTag();
					tsomr.get(pos).setLy(tsomr.get(pos).getLy() == 0 ? 1 : 0);
					boolean space = false;
					for (int i = 0; i < tsomr.size(); i++) {
						if (tsomr.get(i).getLy() == 1) {
							space = true;
						} 
					}
					
					if (space) {
						tv.setText("删除");
					} else {
						tv.setText("完成");
					}
		
					adapter.notifyDataSetChanged();
				}
			});
			
			return convertView;
		}

		private class ViewHolder {
			private TextView tv_time;
			private TextView tv_name;
			private TextView tv_title;
			private TextView tv_select;
			private ImageView iv_check;
			private LinearLayout ll_check;
			private TextView tv_people_name;
			private TextView tv_date;
			
			public ViewHolder(View v) {
				this.tv_date = (TextView) v.findViewById(R.id.tv_date);
				this.tv_time = (TextView) v.findViewById(R.id.tv_time);
				this.tv_name = (TextView) v.findViewById(R.id.tv_name);
				this.tv_title = (TextView) v.findViewById(R.id.tv_title);
				this.iv_check = (ImageView) v.findViewById(R.id.iv_check);
				this.tv_select = (TextView) v.findViewById(R.id.tv_select);
				this.ll_check = (LinearLayout) v.findViewById(R.id.ll_check);
				this.tv_people_name = (TextView) v.findViewById(R.id.tv_people_name);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
			
		case R.id.ll_edit:
			String space = "";
			
			if (tv.getText().toString().equals("编辑")) {
				this.tv.setText("完成");
				if (null != tsomr) {
					for (int i = 0; i < tsomr.size(); i++) {
						
						if (tsomr.get(i).getSpace() == 0) {
							tsomr.get(i).setSpace(1);
							tsomr.get(i).setLy(0);
							
						} else {
							tsomr.get(i).setSpace(0);
							tsomr.get(i).setLy(0);
						}

					}
					adapter.notifyDataSetChanged();
				}
				
			} else if (tv.getText().toString().equals("完成")) {
				this.tv.setText("编辑");
				for (int i = 0; i < tsomr.size(); i++) {
					tsomr.get(i).setSpace(0);
					tsomr.get(i).setLy(0);
				}
				adapter.notifyDataSetChanged();
			} else if (tv.getText().toString().equals("删除")) {
				this.tv.setText("编辑");
				for (int i = 0; i < tsomr.size(); i++) {
					if (tsomr.get(i).getLy() == 1) {
						if (space.equals("")) {
							space = tsomr.get(i).getId();
						} else {
							space += "," + tsomr.get(i).getId();
						}
					}
				}
				
				if (null != space && space.length() > 0) {
					deleteId(space);
				}
			}
			
			break;
		}
	}
	
	private void deleteId(String id) {
		Map<String,String> map = new HashMap<>();
		map.put("OPT", "307");
		map.put("tsomid", id);
		new HttpClientGet(MedicalRecordActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println(result);
				
				getData();
				
				adapter.notifyDataSetChanged();
				
				tv.setText("编辑");
			}
			
			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});
	}
}

