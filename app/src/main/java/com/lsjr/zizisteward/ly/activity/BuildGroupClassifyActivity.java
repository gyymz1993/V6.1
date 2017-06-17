package com.lsjr.zizisteward.ly.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.PrivateCredentialPermission;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.bean.GrouplabelBean;
import com.lsjr.zizisteward.bean.GrouplabelBean.Grouplabel;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BuildGroupClassifyActivity extends Activity implements OnClickListener {
	
	private GridView gv;
	private LinearLayout ll_back;
	private List<Grouplabel> list_grouplabel;
	private BulidGroupClassifyAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.build_group_classify_activity);
		this.findViewById();
	}

	private void findViewById() {
		this.gv = (GridView) super.findViewById(R.id.gv);
		this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
		
		getData();
		
		this.gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startActivity(new Intent(BuildGroupClassifyActivity.this,ActivityChoosePicture.class)
				.putExtra("label_id", list_grouplabel.get(position).getId())
				.putExtra("activity", "new"));
				finish();
			}
		});
		
		this.ll_back.setOnClickListener(this);
	}
	
	private void getData() {
		
		Map<String, String> map = new HashMap<>();
		map.put("OPT", "220");
		new HttpClientGet(BuildGroupClassifyActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				
				GrouplabelBean gBean = new Gson().fromJson(result, GrouplabelBean.class);
				
				list_grouplabel = gBean.getGrouplabel();
				
				adapter = new BulidGroupClassifyAdapter(BuildGroupClassifyActivity.this, list_grouplabel);
				
				gv.setAdapter(adapter);
			}
		});
	}
	
	private class BulidGroupClassifyAdapter extends BaseAdapter {

		private ViewHolder view;
		private Context context;
		private List<Grouplabel> list_grouplabel;
		
		public BulidGroupClassifyAdapter( Context context ,List<Grouplabel> list_grouplabel) {
			this.context = context;
			this.list_grouplabel = list_grouplabel;
		}
		
		@Override
		public int getCount() {
			return null == list_grouplabel ? 0 : list_grouplabel.size();
		}

		@Override
		public Object getItem(int position) {
			return list_grouplabel.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView( int position, View convertView, ViewGroup parent ) {
			
			if ( null == convertView ) {
				
				convertView = LayoutInflater.from(context).inflate(R.layout.bulid_group_classify_item, null);
				
				view = new ViewHolder(convertView);
				
				convertView.setTag(view);
				
			}else {
				
				view = (ViewHolder) convertView.getTag();
				
			}
			
			String[] str = list_grouplabel.get(position).getIcon().split(",");
			
			if (null != str ) {
				Picasso.with(context).load(HttpConfig.IMAGEHOST + str[0]).into(view.iv_pic);
			}
			
			view.tv_name.setText(list_grouplabel.get(position).getGname());
			
			return convertView;
		}
		
		private class ViewHolder {
			private ImageView iv_pic;
			private TextView tv_name;
			
			public ViewHolder(View v) {
				this.iv_pic = (ImageView) v.findViewById(R.id.iv_pic);
				this.tv_name = (TextView) v.findViewById(R.id.tv_name);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		 case R.id.ll_back:
			
			 finish();
			 
			break;
		}
	}
}
