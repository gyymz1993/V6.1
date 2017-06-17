package com.lsjr.zizisteward.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.HomeBrandDetail;
import com.lsjr.zizisteward.adapter.BaseViewHolder;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.mybetterandroid.wheel.other.PullToRefreshView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApplianceFragemnt extends Fragment implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {
	private View rootView;
	private PullToRefreshView mPulltorefresh;
	List<ApplianceBean.ApplianceInfo.ApplianceDetail> list = new ArrayList<>();
	private GridView mGridview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_appliance, null);
			mPulltorefresh = (PullToRefreshView) rootView.findViewById(R.id.pulltorefresh);
			mGridview = (GridView) rootView.findViewById(R.id.gridview);
			mPulltorefresh.setOnHeaderRefreshListener(this);
			mPulltorefresh.setOnFooterRefreshListener(this);
			getData2();
		}

		return rootView;
	}

	private void getData2() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("OPT", "59");
		map.put("currPage", "1");
		map.put("tpid", "75");

		new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("用品" + result);
				ApplianceBean bean = GsonUtil.getInstance().fromJson(result, ApplianceBean.class);
				list = bean.getPage().getPage();
				DressAdapter adapter = new DressAdapter(getContext(), list);
				mGridview.setAdapter(adapter);

			}
		});
	}

	public class DressAdapter extends BaseAdapter {
		private Context context;
		private List<ApplianceBean.ApplianceInfo.ApplianceDetail> list;

		public DressAdapter(Context context,
				List<ApplianceBean.ApplianceInfo.ApplianceDetail> list) {
			this.list = list;
			this.context = context;
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
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_dress, null);
			}
			ImageView iv_classic = BaseViewHolder.get(convertView, R.id.iv_classic);
			TextView tv_brand = BaseViewHolder.get(convertView, R.id.tv_brand);
			TextView tv_price = BaseViewHolder.get(convertView, R.id.tv_price);
			Glide.with(getContext()).load(HttpConfig.IMAGEHOST + list.get(position).getSpic()).into(iv_classic);
			tv_brand.setText(list.get(position).getSname());
			tv_price.setText("￥" + list.get(position).getSprice());
			mGridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(getContext(), HomeBrandDetail.class);
					intent.putExtra("sid", list.get(position).getId());
					startActivity(intent);
				}
			});
			return convertView;
		}

	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mPulltorefresh.postDelayed(new Runnable() {
			public void run() {
				mPulltorefresh.onFooterRefreshComplete();
			}
		}, 500);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mPulltorefresh.postDelayed(new Runnable() {

			@Override
			public void run() {
				mPulltorefresh.onHeaderRefreshComplete();
			}
		}, 500);
	}

	public class ApplianceBean {
		private String error;
		private String msg;
		private ApplianceInfo page;
		private String totalNum;

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

		public ApplianceInfo getPage() {
			return page;
		}

		public void setPage(ApplianceInfo page) {
			this.page = page;
		}

		public String getTotalNum() {
			return totalNum;
		}

		public void setTotalNum(String totalNum) {
			this.totalNum = totalNum;
		}

		private class ApplianceInfo {

			private String currPage;
			private List<ApplianceDetail> page;
			private String pageSize;
			private String pageTitle;
			private String totalCount;
			private String totalPageCount;

			public String getCurrPage() {
				return currPage;
			}

			public void setCurrPage(String currPage) {
				this.currPage = currPage;
			}

			public List<ApplianceDetail> getPage() {
				return page;
			}

			public void setPage(List<ApplianceDetail> page) {
				this.page = page;
			}

			public String getPageSize() {
				return pageSize;
			}

			public void setPageSize(String pageSize) {
				this.pageSize = pageSize;
			}

			public String getPageTitle() {
				return pageTitle;
			}

			public void setPageTitle(String pageTitle) {
				this.pageTitle = pageTitle;
			}

			public String getTotalCount() {
				return totalCount;
			}

			public void setTotalCount(String totalCount) {
				this.totalCount = totalCount;
			}

			public String getTotalPageCount() {
				return totalPageCount;
			}

			public void setTotalPageCount(String totalPageCount) {
				this.totalPageCount = totalPageCount;
			}

			private class ApplianceDetail {
				private String article_number;
				private String audit;
				private String cost_price;
				private String entityId;
				private String id;
				private String mname;
				private String persistent;
				private String putaway;
				private String samount;
				private String sbin;
				private String sbrand;
				private String scolour;
				private String scount;
				private String sell_points;
				private String shop_type;
				private String shot;
				private String simg;
				private String sinfo;
				private String sisrec;
				private String size;
				private String skeyword;
				private String smarktime;
				private String sname;
				private String snew;
				private String spic;
				private String spid;
				private String sprice;
				private String stime;

				public String getArticle_number() {
					return article_number;
				}

				public void setArticle_number(String article_number) {
					this.article_number = article_number;
				}

				public String getAudit() {
					return audit;
				}

				public void setAudit(String audit) {
					this.audit = audit;
				}

				public String getCost_price() {
					return cost_price;
				}

				public void setCost_price(String cost_price) {
					this.cost_price = cost_price;
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

				public String getMname() {
					return mname;
				}

				public void setMname(String mname) {
					this.mname = mname;
				}

				public String getPersistent() {
					return persistent;
				}

				public void setPersistent(String persistent) {
					this.persistent = persistent;
				}

				public String getPutaway() {
					return putaway;
				}

				public void setPutaway(String putaway) {
					this.putaway = putaway;
				}

				public String getSamount() {
					return samount;
				}

				public void setSamount(String samount) {
					this.samount = samount;
				}

				public String getSbin() {
					return sbin;
				}

				public void setSbin(String sbin) {
					this.sbin = sbin;
				}

				public String getSbrand() {
					return sbrand;
				}

				public void setSbrand(String sbrand) {
					this.sbrand = sbrand;
				}

				public String getScolour() {
					return scolour;
				}

				public void setScolour(String scolour) {
					this.scolour = scolour;
				}

				public String getScount() {
					return scount;
				}

				public void setScount(String scount) {
					this.scount = scount;
				}

				public String getSell_points() {
					return sell_points;
				}

				public void setSell_points(String sell_points) {
					this.sell_points = sell_points;
				}

				public String getShop_type() {
					return shop_type;
				}

				public void setShop_type(String shop_type) {
					this.shop_type = shop_type;
				}

				public String getShot() {
					return shot;
				}

				public void setShot(String shot) {
					this.shot = shot;
				}

				public String getSimg() {
					return simg;
				}

				public void setSimg(String simg) {
					this.simg = simg;
				}

				public String getSinfo() {
					return sinfo;
				}

				public void setSinfo(String sinfo) {
					this.sinfo = sinfo;
				}

				public String getSisrec() {
					return sisrec;
				}

				public void setSisrec(String sisrec) {
					this.sisrec = sisrec;
				}

				public String getSize() {
					return size;
				}

				public void setSize(String size) {
					this.size = size;
				}

				public String getSkeyword() {
					return skeyword;
				}

				public void setSkeyword(String skeyword) {
					this.skeyword = skeyword;
				}

				public String getSmarktime() {
					return smarktime;
				}

				public void setSmarktime(String smarktime) {
					this.smarktime = smarktime;
				}

				public String getSname() {
					return sname;
				}

				public void setSname(String sname) {
					this.sname = sname;
				}

				public String getSnew() {
					return snew;
				}

				public void setSnew(String snew) {
					this.snew = snew;
				}

				public String getSpic() {
					return spic;
				}

				public void setSpic(String spic) {
					this.spic = spic;
				}

				public String getSpid() {
					return spid;
				}

				public void setSpid(String spid) {
					this.spid = spid;
				}

				public String getSprice() {
					return sprice;
				}

				public void setSprice(String sprice) {
					this.sprice = sprice;
				}

				public String getStime() {
					return stime;
				}

				public void setStime(String stime) {
					this.stime = stime;
				}
			}
		}
	}
}
