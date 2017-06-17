package com.lsjr.zizisteward.common.activtiy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.ShiJieDetailActivity;
import com.lsjr.zizisteward.activity.WorldSearchActivity.Orders;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShiShiLabelMoreListActivity extends BaseActivity {
	private String name;
	private SuperListView listview;
	private int pageNum = 1;
	private List<Orders> list = new ArrayList<Orders>();
	private LabelAdapter adapter;
	private boolean isRefresh = true;
	private boolean login_state;
	private Intent intent;

	@Override
	public int getContainerView() {
		return R.layout.activity_shishi_label_more_list;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("标签列表");
		name = getIntent().getStringExtra("name");
		listview = (SuperListView) findViewById(R.id.listview);
		initListener();
		((RelativeLayout) findViewById(R.id.iv_back)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
		});
	}

	@Override
	protected void onResume() {
		login_state = PreferencesUtils.getBoolean(getApplicationContext(), "isLogin");
		super.onResume();
	}

	private void initListener() {
		adapter = new LabelAdapter(ShiShiLabelMoreListActivity.this, list);
		listview.setAdapter(adapter);
		listview.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				isRefresh = true;
				getData();
			}
		});
		listview.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				isRefresh = false;
				getData();
			}
		});
		listview.refresh();
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (login_state == true) {
					intent = new Intent(getApplicationContext(), ShiJieDetailActivity.class);
					intent.putExtra("id", list.get(position - 1).getId());
					intent.putExtra("content", list.get(position - 1).getContent());// 内容
					intent.putExtra("photo", list.get(position - 1).getPhoto());// 头像
					intent.putExtra("shareImg", list.get(position - 1).getShareImg());// 图片
					intent.putExtra("time", list.get(position - 1).getShare_time());// 时间
					intent.putExtra("user_name", list.get(position - 1).getUser_name());// 用户名
					intent.putExtra("sight_type", list.get(position - 1).getSight_type());// 内容类型
					intent.putExtra("custom_tag", list.get(position - 1).getCustom_tag());// 标签内容
					intent.putExtra("zan_count", list.get(position - 1).getShare_like());// 点赞次数
					String share_time_uid = list.get(position - 1).getShare_time_uid();
					if (share_time_uid != null && share_time_uid.length() > 0) {
						String[] zan_ids = share_time_uid.split(",");
						for (int i = 0; i < zan_ids.length; i++) {
							if (App.getUserInfo().getId().equals(zan_ids[i])) {
								intent.putExtra("zan_state", true);// 点赞状态已点赞
							} else {
								intent.putExtra("zan_state", false);// 点赞状态
							}
						}
					}
					String collect_time_uid = list.get(position - 1).getCollect_time_uid();
					if (collect_time_uid != null && collect_time_uid.length() > 0) {
						String[] collect_ids = collect_time_uid.split(",");
						for (int i = 0; i < collect_ids.length; i++) {
							if (App.getUserInfo().getId().equals(collect_ids[i])) {
								intent.putExtra("collect_state", true);// 收藏状态已收藏
							} else {
								intent.putExtra("collect_state", false);// 收藏状态
							}
						}
					}
					intent.putExtra("image_size", list.get(position - 1).getImg_wh());// 图片尺寸
					intent.putExtra("level", list.get(position - 1).getCredit_level_id());// 用户等级
					intent.putExtra("user_id", list.get(position - 1).getUser_id());// 用户id
					startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				} else {
					intent = new Intent(getApplicationContext(), NoteLoginActivity.class);
					intent.putExtra("personal", "shishi_search_more_list_label");
					startActivityForResult(intent, 1);
				}
			}
		});

	}

	private void getData() {
		if (isRefresh) {
			pageNum = 1;
			adapter.removeAll();
		}
		HashMap<String, String> map = new HashMap<>();
		map.put("OPT", "90");
		map.put("keyword", name);
		map.put("currPage", String.valueOf(pageNum++));
		new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("结果" + result);
				LabelList bean = GsonUtil.getInstance().fromJson(result, LabelList.class);
				if (pageNum == 1) {
					adapter = new LabelAdapter(ShiShiLabelMoreListActivity.this, list);
					listview.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				} else {
					list.addAll(bean.getSightLabelList().getPage());
					adapter.setList(list);
				}
				if (list.size() < bean.getSightLabelList().getPageSize()) {
					listview.setIsLoadFull(false);
				}
				listview.finishRefresh();
				listview.finishLoadMore();
			}
		});
	}

	private class LabelAdapter extends BaseAdapter {
		Context context;
		private ViewHolder mHolder;
		List<Orders> list;

		public LabelAdapter(Context context, List<Orders> list) {
			this.context = context;
			this.list = list;
		}

		public void setList(List<Orders> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		public void add(Orders page) {
			this.list.add(page);
			notifyDataSetChanged();
		}

		public void addFirst(Orders page) {
			this.list.add(0, page);
			notifyDataSetChanged();
		}

		public void addAll(List<Orders> list) {
			this.list.addAll(list);
			notifyDataSetChanged();
		}

		public void remove(int position) {
			this.list.remove(position);
			notifyDataSetChanged();
		}

		public void removeAll() {
			this.list.clear();
			notifyDataSetChanged();
		}

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
				convertView = LayoutInflater.from(context).inflate(R.layout.item_labels_search, parent, false);
				mHolder = new ViewHolder(convertView);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}

			Glide.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getSpicfirst())
					.into(mHolder.label_photo);
			mHolder.label_content.setText("包含 : " + list.get(position).getCustom_tag());
			return convertView;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private class ViewHolder {
		private ImageView label_photo;
		private TextView label_content;

		public ViewHolder(View view) {
			label_photo = (ImageView) view.findViewById(R.id.label_photo);
			label_content = (TextView) view.findViewById(R.id.label_content);
		}

	}

	private class LabelList {
		private SightLabelList sightLabelList;

		public SightLabelList getSightLabelList() {
			return sightLabelList;
		}

		public void setSightLabelList(SightLabelList sightLabelList) {
			this.sightLabelList = sightLabelList;
		}
	}

	private class SightLabelList {
		private int currPage;
		private List<Orders> page;
		private int pageSize;
		private String pageTitle;
		private int totalCount;
		private int totalPageCount;

		public int getCurrPage() {
			return currPage;
		}

		public void setCurrPage(int currPage) {
			this.currPage = currPage;
		}

		public List<Orders> getPage() {
			return page;
		}

		public void setPage(List<Orders> page) {
			this.page = page;
		}

		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

		public String getPageTitle() {
			return pageTitle;
		}

		public void setPageTitle(String pageTitle) {
			this.pageTitle = pageTitle;
		}

		public int getTotalCount() {
			return totalCount;
		}

		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}

		public int getTotalPageCount() {
			return totalPageCount;
		}

		public void setTotalPageCount(int totalPageCount) {
			this.totalPageCount = totalPageCount;
		}
	}
}
