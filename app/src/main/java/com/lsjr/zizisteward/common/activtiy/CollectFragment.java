package com.lsjr.zizisteward.common.activtiy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.CollectDetailActivity;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.newview.CircleImageView;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.lsjr.zizisteward.utils.ToastUtils;
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectFragment extends Fragment implements OnClickListener {
	private View rootView;
	private RelativeLayout mRe_select_xia;
	private RelativeLayout mRe_select_shang;
	private TextView mTv_name;
	private RelativeLayout mRe_all;
	private TextView mTv_week;
	private TextView mTv_month;
	private TextView mTv_three_months;
	private TextView mTv_disabled;
	private SuperListView mListview_shijie;
	private boolean isRefresh = true;
	private int pageNum = 1;
	private List<CollectList> list = new ArrayList<CollectList>();
	private MyCollectAdapter mAdapter;
	private RelativeLayout mRe_null;
	private MyCollectBean mBean;
	private Handler handler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_shijie_collect, null);
			mRe_select_xia = (RelativeLayout) rootView.findViewById(R.id.re_select_xia);// 收起
			mRe_select_shang = (RelativeLayout) rootView.findViewById(R.id.re_select_shang);// 展开
			mTv_name = (TextView) rootView.findViewById(R.id.tv_name);// 类别
			mRe_all = (RelativeLayout) rootView.findViewById(R.id.re_all);// 选中全部
			mTv_week = (TextView) rootView.findViewById(R.id.tv_week);// 选中一周
			mTv_month = (TextView) rootView.findViewById(R.id.tv_month);// 选中一月
			mTv_three_months = (TextView) rootView.findViewById(R.id.tv_three_months);// 选中三月
			mTv_disabled = (TextView) rootView.findViewById(R.id.tv_disabled);// 选中已失效
			mListview_shijie = (SuperListView) rootView.findViewById(R.id.listview_shijie);
			mRe_null = (RelativeLayout) rootView.findViewById(R.id.re_null);// 无视界

			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (msg.what == 0) {
						isRefresh = true;
						mAdapter.removeAll();
						getData(msg.what);
						mAdapter.notifyDataSetChanged();
					}
					if (msg.what == 1) {
						isRefresh = true;
						mAdapter.removeAll();
						getData(msg.what);
						mAdapter.notifyDataSetChanged();
					}
					if (msg.what == 2) {
						isRefresh = true;
						mAdapter.removeAll();
						getData(msg.what);
						mAdapter.notifyDataSetChanged();
					}
					if (msg.what == 3) {
						isRefresh = true;
						mAdapter.removeAll();
						getData(msg.what);
						mAdapter.notifyDataSetChanged();
					}
					if (msg.what == 4) {
						isRefresh = true;
						mAdapter.removeAll();
						getData(msg.what);
						mAdapter.notifyDataSetChanged();
					}
				}
			};
			initListener();
			initView();
		}
		return rootView;
	}

	private void initListener() {
		mRe_select_xia.setVisibility(View.VISIBLE);
		mRe_select_shang.setVisibility(View.GONE);
		mRe_select_xia.setOnClickListener(this);
		mRe_select_shang.setOnClickListener(this);
		mRe_all.setOnClickListener(this);
		mTv_week.setOnClickListener(this);
		mTv_month.setOnClickListener(this);
		mTv_three_months.setOnClickListener(this);
		mTv_disabled.setOnClickListener(this);
	}

	private void initView() {
		mAdapter = new MyCollectAdapter(getContext(), list);
		mListview_shijie.setAdapter(mAdapter);
		mListview_shijie.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				mRe_select_xia.setVisibility(View.VISIBLE);
				mRe_select_shang.setVisibility(View.GONE);
				mTv_name.setText("全部");
				isRefresh = true;
				getData(0);
			}
		});
		mListview_shijie.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				isRefresh = false;
				getData(0);
			}
		});
		mListview_shijie.refresh();
		mListview_shijie.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mRe_select_xia.setVisibility(View.VISIBLE);
				mRe_select_shang.setVisibility(View.GONE);
				if ("1".equals(list.get(position - 1).is_dispaly)) {
					ToastUtils.show(getContext(), "群主已删除该文章...");
				} else {
					Intent intent = new Intent(getContext(), CollectDetailActivity.class);
					intent.putExtra("content", list.get(position - 1).getContent());// 内容
					intent.putExtra("photo", list.get(position - 1).getPhoto());// 头像
					intent.putExtra("shareImg", list.get(position - 1).getShareImg());// 图片
					intent.putExtra("time", list.get(position - 1).getCtime().getTime());// 时间
					intent.putExtra("user_name", list.get(position - 1).getUser_name());// 用户名
					intent.putExtra("custom_tag", list.get(position - 1).getCustom_tag());// 标签内容
					intent.putExtra("level", list.get(position - 1).getCredit_level_id());// 用户等级
					intent.putExtra("size", list.get(position - 1).getImg_wh());// 图片尺寸
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public void onResume() {
		mTv_name.setText("全部");
		mRe_select_xia.setVisibility(View.VISIBLE);
		mRe_select_shang.setVisibility(View.GONE);
		getData(0);
		super.onResume();
	}

	protected void getData(int num_type) {
		if (isRefresh) {
			pageNum = 1;
			mAdapter.removeAll();
		}
		HashMap<String, String> map = new HashMap<>();
		map.put("OPT", "70");
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
		map.put("currPage", String.valueOf(pageNum++));
		map.put("collect_type", "0");
		map.put("keyword", String.valueOf(num_type));
		new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("收藏" + result);
				mBean = GsonUtil.getInstance().fromJson(result, MyCollectBean.class);
				if (null != mBean && "1".equals(mBean.getError())) {
					if (mBean.getCollectShare().getTotalCount() == 0) {
						mRe_null.setVisibility(View.VISIBLE);
						mListview_shijie.setVisibility(View.GONE);
					} else {
						mRe_null.setVisibility(View.GONE);
						mListview_shijie.setVisibility(View.VISIBLE);

						if (1 != pageNum) {
							list.addAll(mBean.getCollectShare().getPage());
							mAdapter.setList(list);
						} else {
							list = mBean.getCollectShare().getPage();
							mAdapter = new MyCollectAdapter(getContext(), list);
							mListview_shijie.setAdapter(mAdapter);
							mAdapter.notifyDataSetChanged();
						}

						if (list.size() < mBean.getCollectShare().getPageSize()) {
							mListview_shijie.setIsLoadFull(false);
						}

						mListview_shijie.finishRefresh();
						mListview_shijie.finishLoadMore();
					}

				}

			}

			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});
	}

	public class MyCollectAdapter extends BaseAdapter {
		private Context context;
		private List<CollectList> list;
		private ViewHolder mHolder;

		public MyCollectAdapter(Context context, List<CollectList> list) {
			this.context = context;
			this.list = list;
		}

		public void setList(List<CollectList> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		public void add(CollectList page) {
			this.list.add(page);
			notifyDataSetChanged();
		}

		public void addFirst(CollectList page) {
			this.list.add(0, page);
			notifyDataSetChanged();
		}

		public void addAll(List<CollectList> list) {
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
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.item_collect, null);
				mHolder = new ViewHolder(convertView);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}

			if (list.get(position).getCredit_level_id().equals("0")) {
				mHolder.mIv_level.setImageResource(R.drawable.level_zero);
			} else if (list.get(position).getCredit_level_id().equals("1")) {
				mHolder.mIv_level.setImageResource(R.drawable.level_one);
			} else if (list.get(position).getCredit_level_id().equals("2")) {
				mHolder.mIv_level.setImageResource(R.drawable.level_two);
			} else if (list.get(position).getCredit_level_id().equals("3")) {
				mHolder.mIv_level.setImageResource(R.drawable.level_three);
			} else if (list.get(position).getCredit_level_id().equals("6")) {
				mHolder.mIv_level.setImageResource(R.drawable.level_six);
			} else if (list.get(position).getCredit_level_id().equals("5")) {
				mHolder.mIv_level.setImageResource(R.drawable.level_five);
			} else if (list.get(position).getCredit_level_id().equals("4")) {
				mHolder.mIv_level.setImageResource(R.drawable.level_three);
			}

			if (TextUtils.isEmpty(list.get(position).getCustom_tag())) {
				mHolder.mTv_custom.setVisibility(View.GONE);
			} else {
				mHolder.mTv_custom.setVisibility(View.VISIBLE);
				mHolder.mTv_custom.setText(list.get(position).getCustom_tag());
			}

			if ("1".equals(list.get(position).is_dispaly)) {
				mHolder.mTv_disabled.setVisibility(View.VISIBLE);
				mHolder.mIv_delete.setEnabled(false);
			} else {
				mHolder.mTv_disabled.setVisibility(View.GONE);
				mHolder.mIv_delete.setEnabled(true);
			}

			if (list.size() == 1) {
				mHolder.mView_gray.setVisibility(View.GONE);
			} else {
				mHolder.mView_gray.setVisibility(View.VISIBLE);
			}

			String img_wh = list.get(position).getImg_wh();
			String[] spic_first = img_wh.split(",");
			int width = Integer.valueOf(spic_first[0]);
			int height = Integer.valueOf(spic_first[1]);
			System.out.println("宽度" + width + "高度" + height);

			DisplayMetrics dm = getResources().getDisplayMetrics();
			int widthPixels = dm.widthPixels;
			int heightPixels = dm.heightPixels;

			LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) mHolder.mRe_iv.getLayoutParams();
			RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) mHolder.mIv_photo.getLayoutParams();
			params2.width = widthPixels * 2 / 3;
			params2.height = widthPixels;
			params1.height = widthPixels;
			params1.width = widthPixels;

			mHolder.mIv_photo.setLayoutParams(params2);
			mHolder.mRe_iv.setLayoutParams(params1);

			Glide.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getSpicfirst()).asBitmap().centerCrop()
					.animate(android.R.anim.slide_in_left).into(mHolder.mIv_photo);
			Glide.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getPhoto())
					.into(mHolder.mYouliao_yuantu);
			mHolder.mTv_name.setText(list.get(position).getUser_name());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
			String share_time = formatter.format(Long.valueOf(list.get(position).getCtime().getTime()));
			mHolder.mTv_time.setText(share_time);
			mHolder.mTv_content.setText(list.get(position).getContent());
			mHolder.mTv_count.setText(list.get(position).getPhoto_number() + "张");

			mHolder.mIv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					final Dialog dialog = new Dialog(getContext(), R.style.dialog);
					dialog.setContentView(R.layout.popup_delete_address);
					Window window = dialog.getWindow();
					window.setGravity(Gravity.CENTER | Gravity.CENTER);
					TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
					tv_msg.setText("确定删除该收藏吗?");
					((TextView) dialog.findViewById(R.id.tv_cancel)).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					((TextView) dialog.findViewById(R.id.tv_confirm)).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							Map<String, String> map = new HashMap<String, String>();
							map.put("OPT", "69");
							map.put("share_id", list.get(position).getCsid());
							map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
							new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

								@Override
								public void onSuccess(String result) {
									BasicParameterBean bean = GsonUtil.getInstance().fromJson(result,
											BasicParameterBean.class);
									Toast.makeText(getContext(), bean.getMsg(), Toast.LENGTH_SHORT).show();
									isRefresh = true;
									getData(0);
									mAdapter.notifyDataSetChanged();
									PreferencesUtils.putBoolean(getContext(), "isChange", true);
								}

								@Override
								public void onFailure(MyError myError) {
									super.onFailure(myError);
								}
							});
						}
					});

					dialog.show();

				}
			});

			return convertView;
		}

	}

	public class ViewHolder {
		private CircleImageView mYouliao_yuantu;// 用户头像
		private TextView mTv_name;// 用户名
		private TextView mTv_time;// 时间
		private ImageView mIv_level;// 会员等级图标
		private TextView mTv_content;// 内容
		private TextView mTv_count;// 图片数量
		private ImageView mIv_photo;// 图片
		private TextView mTv_custom;// 自定义标签内容
		private TextView mTv_disabled;// 已失效标签
		private RelativeLayout mIv_delete;// 删除按钮
		private View mView_gray;
		private RelativeLayout mRe_iv;

		public ViewHolder(View view) {

			mYouliao_yuantu = (CircleImageView) view.findViewById(R.id.youliao_yuantu);
			mTv_name = (TextView) view.findViewById(R.id.tv_name);
			mIv_level = (ImageView) view.findViewById(R.id.iv_level);
			mTv_time = (TextView) view.findViewById(R.id.tv_time);
			mTv_content = (TextView) view.findViewById(R.id.tv_content);
			mIv_photo = (ImageView) view.findViewById(R.id.iv_photo);
			mTv_count = (TextView) view.findViewById(R.id.tv_count);
			mTv_custom = (TextView) view.findViewById(R.id.tv_custom);
			mTv_disabled = (TextView) view.findViewById(R.id.tv_disabled);
			mIv_delete = (RelativeLayout) view.findViewById(R.id.iv_delete);
			mView_gray = view.findViewById(R.id.view_gray);
			mRe_iv = (RelativeLayout) view.findViewById(R.id.re_iv);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.re_all:
			mRe_select_xia.setVisibility(View.VISIBLE);
			mRe_select_shang.setVisibility(View.GONE);
			mTv_name.setText("全部");
			handler.sendEmptyMessage(0);
			break;
		case R.id.tv_week:
			mRe_select_xia.setVisibility(View.VISIBLE);
			mRe_select_shang.setVisibility(View.GONE);
			mTv_name.setText("一周内");
			handler.sendEmptyMessage(1);
			break;
		case R.id.tv_month:
			mRe_select_xia.setVisibility(View.VISIBLE);
			mRe_select_shang.setVisibility(View.GONE);
			mTv_name.setText("一月内");
			handler.sendEmptyMessage(2);
			break;
		case R.id.tv_three_months:
			mRe_select_xia.setVisibility(View.VISIBLE);
			mRe_select_shang.setVisibility(View.GONE);
			mTv_name.setText("三月内");
			handler.sendEmptyMessage(3);
			break;
		case R.id.tv_disabled:
			mRe_select_xia.setVisibility(View.VISIBLE);
			mRe_select_shang.setVisibility(View.GONE);
			mTv_name.setText("已失效");
			handler.sendEmptyMessage(4);
			break;
		case R.id.re_select_xia:
			mRe_select_xia.setVisibility(View.INVISIBLE);
			mRe_select_shang.setVisibility(View.VISIBLE);
			break;
		case R.id.re_select_shang:
			mRe_select_xia.setVisibility(View.VISIBLE);
			mRe_select_shang.setVisibility(View.GONE);
			break;
		}
	}

	public class MyCollectBean {
		private CollectShare collectShare;
		private String error;
		private String msg;

		public CollectShare getCollectShare() {
			return collectShare;
		}

		public void setCollectShare(CollectShare collectShare) {
			this.collectShare = collectShare;
		}

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
	}

	public class CollectShare {

		private int currPage;
		private List<CollectList> page;
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

		public List<CollectList> getPage() {
			return page;
		}

		public void setPage(List<CollectList> page) {
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

	public class CollectList {
		private String collect_type;
		private String content;
		private String credit_level_id;
		private String csid;
		private CTime ctime;
		private String cuid;
		private String custom_tag;
		private String id;
		private String img_wh;
		private String is_dispaly;
		private String name;
		private String persistent;
		private String photo;
		private String photo_number;
		private String reality_name;
		private String shareImg;
		private String spicfirst;
		private String tshow;
		private String user_name;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getCredit_level_id() {
			return credit_level_id;
		}

		public void setCredit_level_id(String credit_level_id) {
			this.credit_level_id = credit_level_id;
		}

		public String getCsid() {
			return csid;
		}

		public void setCsid(String csid) {
			this.csid = csid;
		}

		public CTime getCtime() {
			return ctime;
		}

		public void setCtime(CTime ctime) {
			this.ctime = ctime;
		}

		public String getCuid() {
			return cuid;
		}

		public void setCuid(String cuid) {
			this.cuid = cuid;
		}

		public String getCustom_tag() {
			return custom_tag;
		}

		public void setCustom_tag(String custom_tag) {
			this.custom_tag = custom_tag;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getImg_wh() {
			return img_wh;
		}

		public void setImg_wh(String img_wh) {
			this.img_wh = img_wh;
		}

		public String getIs_dispaly() {
			return is_dispaly;
		}

		public void setIs_dispaly(String is_dispaly) {
			this.is_dispaly = is_dispaly;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPersistent() {
			return persistent;
		}

		public void setPersistent(String persistent) {
			this.persistent = persistent;
		}

		public String getPhoto() {
			return photo;
		}

		public void setPhoto(String photo) {
			this.photo = photo;
		}

		public String getPhoto_number() {
			return photo_number;
		}

		public void setPhoto_number(String photo_number) {
			this.photo_number = photo_number;
		}

		public String getReality_name() {
			return reality_name;
		}

		public void setReality_name(String reality_name) {
			this.reality_name = reality_name;
		}

		public String getShareImg() {
			return shareImg;
		}

		public void setShareImg(String shareImg) {
			this.shareImg = shareImg;
		}

		public String getSpicfirst() {
			return spicfirst;
		}

		public void setSpicfirst(String spicfirst) {
			this.spicfirst = spicfirst;
		}

		public String getTshow() {
			return tshow;
		}

		public void setTshow(String tshow) {
			this.tshow = tshow;
		}

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}
	}

	private class CTime {

		private String time;

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

	}
}
