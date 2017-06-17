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
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class New_Old_Friends_Adapter extends BaseAdapter {

	private int type;
	private Context context;
	private ViewHolder view;
	private List<GroupList> list_frineds;
	
	public New_Old_Friends_Adapter(Context context,List<GroupList> list_frineds,int type) {
		this.type = type;
		this.context = context;
		this.list_frineds = list_frineds;
	}

	@Override
	public int getCount() {
		
		if (type == 0) {
			
			return null == list_frineds || list_frineds.size() < 1 ? 0 : 1;
			
		} else {
			
			return null == list_frineds ? 0 : list_frineds.size();
			
		}
	}

	@Override
	public Object getItem(int position) {
		return list_frineds.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.t_fragment_create_item, null);
			view = new ViewHolder(convertView);
			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
		}
		
		view.ll_come_news.setVisibility(View.GONE); 
		view.ll_information.setVisibility(View.VISIBLE);
		
		//话题名字
		view.tv_title.setText(list_frineds.get(position).getGroup().getGroupName());
		//话题简介
		view.tv_synopsis.setText(list_frineds.get(position).getGroup().getDescription());
		
		view.tv_number_people.setText("(" + list_frineds.get(position).getSize() + "/" + list_frineds.get(position).getGroup().getMaxusers() + ")");
		
		for (int i = 0; i < SimpleVoiceActivity.list_grouplabel.size(); i++) {
			if (SimpleVoiceActivity.list_grouplabel.get(i).getGname().equals(list_frineds.get(position).getGroup().getGname())) {
				String[] str = SimpleVoiceActivity.list_grouplabel.get(i).getIcon().split(",");
				if (null != str && str.length >= 2) {
					Glide.with(context).load(HttpConfig.IMAGEHOST + str[1]).diskCacheStrategy(DiskCacheStrategy.ALL).into(view.iv_label);
				}
			}
		}
		
		view.ll_adjunct.setVisibility(View.VISIBLE);
		
		Glide.with(context).load(HttpConfig.IMAGEHOST + list_frineds.get(position).getOwnerPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(view.riv_owner);
		
		Glide.with(context).load(HttpConfig.IMAGEHOST + list_frineds.get(position).getGroup().getGroupImg()).diskCacheStrategy(DiskCacheStrategy.ALL).into(view.riv);
		
		return convertView;
	}
	
	/**头像*/
	private RoundImageView riv;
	/**话题名*/
	private TextView tv_title;
	
	/**来消息时的布局*/
	private LinearLayout ll_come_news;
	/**消息条数*/
	private TextView tv_number;
	/**消息内容*/
	private TextView tv_content;
	/**消息附件布局*/
	private LinearLayout ll_adjunct;
	/**消息时间*/
	private TextView tv_time;
	/**新消息红点*/
	private ImageView iv_red;
	
	/**群信息布局*/
	private LinearLayout ll_information;
	/**话题标签图片*/
	private ImageView iv_label;
	/**话题简介*/
	private TextView tv_synopsis;
	
	private class ViewHolder {
		/**头像*/
		private RoundImageView riv;
		/**话题名*/
		private TextView tv_title;
		
		/**来消息时的布局*/
		private LinearLayout ll_come_news;
		/**消息条数*/
		private TextView tv_number;
		/**消息内容*/
		private TextView tv_content;
		/**消息附件布局*/
		private LinearLayout ll_adjunct;
		/**消息时间*/
		private TextView tv_time;
		/**新消息红点*/
		private ImageView iv_red;
		
		/**群信息布局*/
		private LinearLayout ll_information;
		/**话题标签图片*/
		private ImageView iv_label;
		/**话题简介*/
		private TextView tv_synopsis;
		
		private TextView tv_number_people;
		private RoundImageView riv_owner;
		
		public ViewHolder(View v) {
			this.riv = (RoundImageView) v.findViewById(R.id.riv);
			this.iv_red = (ImageView) v.findViewById(R.id.iv_red);
			this.tv_time = (TextView) v.findViewById(R.id.tv_time);
			this.tv_title = (TextView) v.findViewById(R.id.tv_title);
			this.iv_label = (ImageView) v.findViewById(R.id.iv_label);
			this.tv_number = (TextView) v.findViewById(R.id.tv_number);
			this.tv_content = (TextView) v.findViewById(R.id.tv_content);
			this.tv_synopsis = (TextView) v.findViewById(R.id.tv_synopsis);
			this.riv_owner = (RoundImageView) v.findViewById(R.id.riv_owner);
			this.ll_adjunct = (LinearLayout) v.findViewById(R.id.ll_adjunct);
			this.ll_come_news = (LinearLayout) v.findViewById(R.id.ll_come_news);
			this.ll_information = (LinearLayout) v.findViewById(R.id.ll_information);
			this.tv_number_people = (TextView) v.findViewById(R.id.tv_number_people);
		}
	}
}
