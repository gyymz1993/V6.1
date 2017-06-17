package com.lsjr.zizisteward.ly.activity;

import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.SimpleVoiceActivity;
import com.lsjr.zizisteward.activity.SixthNewActivity;
import com.lsjr.zizisteward.bean.GroupListBean.GroupList;
import com.lsjr.zizisteward.http.HttpConfig;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Topic_Management_Adapter extends BaseAdapter {

	private Context context;
	private ViewHolder view;
	private List<GroupList> list_create;
	
	public Topic_Management_Adapter(Context context,List<GroupList> list_create) {
		this.context = context;
		this.list_create = list_create;
	}
	
	@Override
	public int getCount() {
		return null == list_create ? 0 : list_create.size();
	}

	@Override
	public Object getItem(int position) {
		return list_create.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (null == convertView) {
			
			convertView = LayoutInflater.from(context).inflate(R.layout.topic_management_activity_item, null);
			
			view = new ViewHolder(convertView);
			
			convertView.setTag(view);
			
			
		} else {
			view = (ViewHolder) convertView.getTag();
		}
		
		for (int i = 0; i < SimpleVoiceActivity.list_grouplabel.size(); i++) {
			if (SimpleVoiceActivity.list_grouplabel.get(i).getGname().equals(list_create.get(position).getGroup().getGname())) {
				String[] str = SimpleVoiceActivity.list_grouplabel.get(i).getIcon().split(",");
				if (null != str && str.length >= 2) {
					Glide.with(context).load(HttpConfig.IMAGEHOST + str[1]).diskCacheStrategy(DiskCacheStrategy.ALL).into(view.iv_label);
				}
			}
		}
		
		view.tv_name.setText(list_create.get(position).getGroup().getGroupName());
		
		view.tv_p_n.setText("(" + list_create.get(position).getSize() + "/" + list_create.get(position).getGroup().getMaxusers() + ")");
		
		Glide.with(context).load(HttpConfig.IMAGEHOST + list_create.get(position).getGroup().getGroupImg()).diskCacheStrategy(DiskCacheStrategy.ALL).into(view.riv);
		
		
		if (list_create.get(position).getGroup().getIs_add_friend().equals("0")) {
			view.iv_ban.setVisibility(View.GONE);
		} else {
			view.iv_ban.setVisibility(View.VISIBLE);
		}
		
		if (list_create.get(position).getGroup().isSpace()) {
			view.iv_select.setVisibility(View.VISIBLE);
		} else {
			view.iv_select.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	private class ViewHolder {
		
		private TextView tv_p_n;
		private ImageView iv_ban;
		private TextView tv_name;
		private ImageView iv_label;
		private RoundImageView riv;
		private ImageView iv_select;
		
		public ViewHolder(View v) {
			
			this.riv = (RoundImageView) v.findViewById(R.id.riv);
			this.tv_p_n = (TextView) v.findViewById(R.id.tv_p_n);
			this.iv_ban = (ImageView) v.findViewById(R.id.iv_ban);
			this.tv_name = (TextView) v.findViewById(R.id.tv_name);
			this.iv_label = (ImageView) v.findViewById(R.id.iv_label);
			this.iv_select = (ImageView) v.findViewById(R.id.iv_select);
		}
	}

}
