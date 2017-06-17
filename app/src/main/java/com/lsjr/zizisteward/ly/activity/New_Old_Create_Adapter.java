package com.lsjr.zizisteward.ly.activity;

import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.SimpleVoiceActivity;
import com.lsjr.zizisteward.activity.SixthNewActivity;
import com.lsjr.zizisteward.bean.GroupListBean.GroupList;
import com.lsjr.zizisteward.http.HttpConfig;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class New_Old_Create_Adapter extends BaseAdapter {
	
	private Context context;
	private ViewHolder view;
	private List<GroupList> list_create;
	
	public New_Old_Create_Adapter(Context context,List<GroupList> list_create) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.t_fragment_create_item, null);
			view = new ViewHolder(convertView);
			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
		}
		
		view.ll_come_news.setVisibility(View.GONE);
		view.ll_information.setVisibility(View.VISIBLE);
		view.ll_adjunct.setVisibility(View.GONE);
		
		view.tv_title.setText(list_create.get(position).getGroup().getGroupName());
		view.tv_synopsis.setText(list_create.get(position).getGroup().getDescription());
		
		for (int i = 0; i < SimpleVoiceActivity.list_grouplabel.size(); i++) {
			if (SimpleVoiceActivity.list_grouplabel.get(i).getGname().equals(list_create.get(position).getGroup().getGname())) {
				String[] str = SimpleVoiceActivity.list_grouplabel.get(i).getIcon().split(",");
				if (null != str && str.length >= 2) {
					Glide.with(context).load(HttpConfig.IMAGEHOST + str[1]).diskCacheStrategy(DiskCacheStrategy.ALL).into(view.iv_label);
				}
			}
		}
		
		view.tv_number_people.setText("(" + list_create.get(position).getSize() + "/" + list_create.get(position).getGroup().getMaxusers() + ")");
		
		Glide.with(context).load(HttpConfig.IMAGEHOST + list_create.get(position).getGroup().getGroupImg()).diskCacheStrategy(DiskCacheStrategy.ALL).into(view.riv);
		
		Glide.with(context).load(HttpConfig.IMAGEHOST + list_create.get(position).getOwnerPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).into(view.riv_owner);
		
		if (null != list_create.get(position).getGroup().getMsg()) {
			view.ll_come_news.setVisibility(View.VISIBLE);
			view.ll_information.setVisibility(View.GONE);
			view.ll_adjunct.setVisibility(View.VISIBLE);
			
			if (null != list_create.get(position).getGroup().getNo_read_count()
				&& !list_create.get(position).getGroup().getNo_read_count().equals("0")) {
				view.tv_number.setText("[" + list_create.get(position).getGroup().getNo_read_count() + "条] ");
				view.iv_red.setVisibility(View.VISIBLE);
			} else {
				view.tv_number.setText("");
				view.iv_red.setVisibility(View.GONE);
			}
			
			view.tv_content.setText(EaseSmileUtils.getSmiledText(
					context, EaseCommonUtils.getMessageDigest(
							list_create.get(position).getGroup().getMsg(), (context))), BufferType.SPANNABLE);
		
			view.tv_time.setText(EaseUserUtils.returnTime(list_create.get(position).getGroup().getMsg().getMsgTime()));
		}
		
		return convertView;
	}
	
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
