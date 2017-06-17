package com.lsjr.zizisteward.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.NewsDetailActivity;
import com.lsjr.zizisteward.bean.ClassicDetail;
import com.lsjr.zizisteward.bean.NewInfoClassicBean;
import com.lsjr.zizisteward.bean.OrderTime;
import com.lsjr.zizisteward.fragment.NewsInfoFragment.NewsLieBiaoBean.NewsLieBiaoDetail;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.newview.MyGridView;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.mybetterandroid.wheel.other.PullToRefreshView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint({ "InflateParams", "SimpleDateFormat" })
public class NewsInfoFragment extends Fragment implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {
	private View rootView;
	private List<ClassicDetail> list = new ArrayList<ClassicDetail>();
	private List<NewsLieBiaoDetail> list_detail = new ArrayList<>();
	@SuppressWarnings("unused")
	private boolean isRefresh = true;
	private ListView mListview_liebiao;
	private HashMap<String, String> mMap;
	private HorizontalListViewAdapter mAdapter2;
	private NewsLieBiaoAdapter mAdapter;
	private int pageNum = 1;
	private NewsLieBiaoBean mLieBiaoBean;
	private MyGridView gridview;
	private PullToRefreshView mMain_pull_refresh_view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_news_info, null);
			gridview = (MyGridView) rootView.findViewById(R.id.gridview);
			mMain_pull_refresh_view = (PullToRefreshView) rootView.findViewById(R.id.main_pull_refresh_view);

			mListview_liebiao = (ListView) rootView.findViewById(R.id.listview_liebiao);

			for (int i = 0; i < list.size(); i++) {
				if (i == 0) {
					list.add(new ClassicDetail(true));
					continue;
				}
				list.add(new ClassicDetail(false));
			}
			getData();
			getData2();

			mMain_pull_refresh_view.setOnHeaderRefreshListener(this);
			mMain_pull_refresh_view.setOnFooterRefreshListener(this);

		}

		return rootView;

	}

	private void getData2() {// 默认进来加载 财经

		mMap = new HashMap<>();
		mMap.put("OPT", "49");
		mMap.put("label_id", "87");
		mMap.put("shot", "0");
		mMap.put("currPage", "1");
		new HttpClientGet(getContext(), null, mMap, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("财经消息" + result);
				mLieBiaoBean = GsonUtil.getInstance().fromJson(result, NewsLieBiaoBean.class);
				list_detail = mLieBiaoBean.getNews_types().getPage();
				mAdapter = new NewsLieBiaoAdapter(getContext(), list_detail);
				mListview_liebiao.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
			}
		});
	}

	private void getData() {
		mMap = new HashMap<>();
		mMap.put("OPT", "48");
		new HttpClientGet(getContext(), null, mMap, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("新闻分类" + result);
				NewInfoClassicBean bean = GsonUtil.getInstance().fromJson(result, NewInfoClassicBean.class);
				list = bean.getNews_types();
				gridview.setNumColumns(list.size());
				mAdapter2 = new HorizontalListViewAdapter();
				gridview.setAdapter(mAdapter2);

			}
		});

	}

	public class HorizontalListViewAdapter extends BaseAdapter {

		private ViewHolder mHolder;

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
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_news_info, parent, false);
				mHolder = new ViewHolder(convertView);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}

			mHolder.mTv_name.setText(list.get(position).getName());

			if (list.get(position).isChecked()) {
				mHolder.mTv_name.setTextColor(Color.parseColor("#B09367"));

			} else {
				mHolder.mTv_name.setTextColor(Color.parseColor("#000000"));
			}

			gridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					for (ClassicDetail bean : list) {
						bean.setChecked(false);
					}
					list.get(position).setChecked(true);

					mAdapter2.notifyDataSetChanged();

					mMap = new HashMap<>();
					mMap.put("OPT", "49");
					mMap.put("label_id", list.get(position).getId());
					mMap.put("shot", "0");
					mMap.put("currPage", "1");

					new HttpClientGet(getContext(), null, mMap, false, new HttpClientGet.CallBacks<String>() {

						@Override
						public void onSuccess(String result) {
							System.out.println("当前消息" + result);
							mLieBiaoBean = GsonUtil.getInstance().fromJson(result, NewsLieBiaoBean.class);
							list_detail = mLieBiaoBean.getNews_types().getPage();
							mAdapter = new NewsLieBiaoAdapter(getContext(), list_detail);
							mListview_liebiao.setAdapter(mAdapter);
							mAdapter.notifyDataSetChanged();
						}
					});

				}
			});

			return convertView;
		}

	}

	public class ViewHolder {
		private TextView mTv_name;

		public ViewHolder(View view) {
			mTv_name = (TextView) view.findViewById(R.id.tv_name);
		}

	}

	public class NewsLieBiaoAdapter extends BaseAdapter {
		private Context context;
		private List<NewsLieBiaoDetail> list_detail;
		private ViewHolder2 mHolder2;

		public NewsLieBiaoAdapter(Context context, List<NewsLieBiaoDetail> list_detail) {
			this.list_detail = list_detail;
			this.context = context;
		}

		public void setList(List<NewsLieBiaoDetail> list_detail) {
			this.list_detail = list_detail;
			notifyDataSetChanged();
		}

		public void add(NewsLieBiaoDetail page) {
			this.list_detail.add(page);
			notifyDataSetChanged();
		}

		public void addFirst(NewsLieBiaoDetail page) {
			this.list_detail.add(0, page);
			notifyDataSetChanged();
		}

		public void addAll(List<NewsLieBiaoDetail> list_detail) {
			this.list_detail.addAll(list_detail);
			notifyDataSetChanged();
		}

		public void remove(int position) {
			this.list_detail.remove(position);
			notifyDataSetChanged();
		}

		public void removeAll() {
			this.list_detail.clear();
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return list_detail == null ? 0 : list_detail.size();
		}

		@Override
		public Object getItem(int position) {
			return list_detail.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_news_liebiao, parent, false);
				mHolder2 = new ViewHolder2(convertView);
				convertView.setTag(mHolder2);
			} else {
				mHolder2 = (ViewHolder2) convertView.getTag();
			}
			mHolder2.mTv_content.setText(list_detail.get(position).getTitle());

			Glide.with(context).load(HttpConfig.IMAGEHOST + list_detail.get(position).getImage_filename())
					.animate(android.R.anim.slide_in_left).into(mHolder2.mIv_photo);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = formatter.format(Long.valueOf(list_detail.get(position).getTime().getTime()));
			mHolder2.mTv_time.setText(time);
			mHolder2.mRe.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getContext(), NewsDetailActivity.class);
					intent.putExtra("newsid", list_detail.get(position).getId());
					startActivity(intent);
				}
			});
			return convertView;
		}

	}

	public class ViewHolder2 {
		private ImageView mIv_photo;
		private TextView mTv_content;
		private TextView mTv_time;
		private RelativeLayout mRe;

		public ViewHolder2(View view) {
			mIv_photo = (ImageView) view.findViewById(R.id.iv_photo);
			mTv_content = (TextView) view.findViewById(R.id.tv_content);
			mTv_time = (TextView) view.findViewById(R.id.tv_time);
			mRe = (RelativeLayout) view.findViewById(R.id.re);
		}

	}

	public class NewsLieBiaoBean {
		private String error;
		private String msg;
		private NewsClassic news_types;

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

		public NewsClassic getNews_types() {
			return news_types;
		}

		public void setNews_types(NewsClassic news_types) {
			this.news_types = news_types;
		}

		public class NewsClassic {

			private int currPage;
			private List<NewsLieBiaoDetail> page;
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

			public List<NewsLieBiaoDetail> getPage() {
				return page;
			}

			public void setPage(List<NewsLieBiaoDetail> page) {
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

		public class NewsLieBiaoDetail {
			private String content;
			private String entityId;
			private String id;
			private String image_filename;
			private String keywords;
			private String label_id;
			private String link;
			private String persistent;
			private String scource;
			private String shot;
			private OrderTime time;
			private String title;
			private String tpath;
			private String tshow;

			public String getContent() {
				return content;
			}

			public void setContent(String content) {
				this.content = content;
			}

			public String getEntityId() {
				return entityId;
			}

			public void setEntityId(String entityId) {
				this.entityId = entityId;
			}

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getImage_filename() {
				return image_filename;
			}

			public void setImage_filename(String image_filename) {
				this.image_filename = image_filename;
			}

			public String getKeywords() {
				return keywords;
			}

			public void setKeywords(String keywords) {
				this.keywords = keywords;
			}

			public String getLabel_id() {
				return label_id;
			}

			public void setLabel_id(String label_id) {
				this.label_id = label_id;
			}

			public String getLink() {
				return link;
			}

			public void setLink(String link) {
				this.link = link;
			}

			public String getPersistent() {
				return persistent;
			}

			public void setPersistent(String persistent) {
				this.persistent = persistent;
			}

			public String getScource() {
				return scource;
			}

			public void setScource(String scource) {
				this.scource = scource;
			}

			public String getShot() {
				return shot;
			}

			public void setShot(String shot) {
				this.shot = shot;
			}

			public OrderTime getTime() {
				return time;
			}

			public void setTime(OrderTime time) {
				this.time = time;
			}

			public String getTitle() {
				return title;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public String getTpath() {
				return tpath;
			}

			public void setTpath(String tpath) {
				this.tpath = tpath;
			}

			public String getTshow() {
				return tshow;
			}

			public void setTshow(String tshow) {
				this.tshow = tshow;
			}
		}
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mMain_pull_refresh_view.postDelayed(new Runnable() {
			public void run() {
				mMain_pull_refresh_view.onFooterRefreshComplete();
			}
		}, 1000);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mMain_pull_refresh_view.postDelayed(new Runnable() {

			@Override
			public void run() {
				mMain_pull_refresh_view.onHeaderRefreshComplete();
			}
		}, 1000);
	}

}
