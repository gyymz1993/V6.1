package com.lsjr.zizisteward.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.BaseViewHolder;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.DensityUtil;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.mybetterandroid.wheel.other.PullToRefreshView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrandToGoodsListActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {
	private GridView brand_list_gridview;
	private String brand_id, type;
	private List<GoodsListBean.GoodsInfo.GoodsDetail> list = new ArrayList<>();
	private GoodsAdapter mAdapter;
	private PullToRefreshView mMain_pull_refresh_view;
	private int currentPage = 1;

	@Override
	public int getContainerView() {
		return R.layout.activity_brand_list;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("商品列表");
		brand_list_gridview = (GridView) findViewById(R.id.brand_list_gridview);
		int px = DensityUtil.dip2px(this, 3);
		brand_list_gridview.setHorizontalSpacing(px);
		brand_list_gridview.setVerticalSpacing(px);
		brand_id = getIntent().getStringExtra("id");
		type = getIntent().getStringExtra("type");
		mMain_pull_refresh_view = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		mMain_pull_refresh_view.setOnHeaderRefreshListener(this);
		mMain_pull_refresh_view.setOnFooterRefreshListener(this);
		getData(currentPage);
		initLayout();

	}

	private void initLayout() {
		mAdapter = new GoodsAdapter(list, BrandToGoodsListActivity.this);
		brand_list_gridview.setAdapter(mAdapter);
		brand_list_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(BrandToGoodsListActivity.this, HomeBrandDetail.class);
				intent.putExtra("sid", list.get(position).getId());
				startActivity(intent);
			}
		});
	}

	private void getData(final int page) {
		Map<String, String> map = new HashMap<String, String>();
		if ("dianpu".equals(type)) {
			map.put("OPT", "401");
			map.put("sbid", brand_id);
			map.put("currPage", String.valueOf(page));
		} else {
			map.put("OPT", "194");
			map.put("sbid", brand_id);
			map.put("currPage", String.valueOf(page));
		}
		new HttpClientGet(BrandToGoodsListActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("品牌进来的商品列表" + result);
				GoodsListBean bean = GsonUtil.getInstance().fromJson(result, GoodsListBean.class);
				list = bean.getShop().getPage();

				mAdapter.addAll(list);

				if (page == 1) {
					mAdapter.notifyDataSetChanged();
					currentPage = 1;
				}

				if (bean.getShop().getPageSize() > 0) {
					currentPage++;
				}

				if ((currentPage - 1) * 8 > bean.getShop().getTotalCount()
						|| (currentPage - 1) * 8 == bean.getShop().getTotalCount()) {
					System.out.println("当前第几页啊  " + currentPage);
					currentPage = 1;
				}

				if (list.size() > 0) {
					if (mAdapter != null) {
						mAdapter = new GoodsAdapter(list, BrandToGoodsListActivity.this);
						brand_list_gridview.setAdapter(mAdapter);
					} else {
						mAdapter.notifyDataSetChanged();
					}
				}

				if (bean.getShop().getPageSize() % 8 != 0) {
					currentPage = 1;
				}

			}

			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});

	}

	public class GoodsAdapter extends BaseAdapter {
		private List<GoodsListBean.GoodsInfo.GoodsDetail> list;
		private Context context;

		public GoodsAdapter(
				List<GoodsListBean.GoodsInfo.GoodsDetail> list,
				Context context) {
			this.list = list;
			this.context = context;
		}

		public void setList(
				List<GoodsListBean.GoodsInfo.GoodsDetail> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		public void add(
				GoodsListBean.GoodsInfo.GoodsDetail page) {
			this.list.add(page);
			notifyDataSetChanged();
		}

		public void addFirst(
				GoodsListBean.GoodsInfo.GoodsDetail page) {
			this.list.add(0, page);
			notifyDataSetChanged();
		}

		public void addAll(
				List<GoodsListBean.GoodsInfo.GoodsDetail> list) {
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
				convertView = LayoutInflater.from(BrandToGoodsListActivity.this).inflate(R.layout.item_brand_list,
						parent, false);
			}
			ImageView iv_classic = BaseViewHolder.get(convertView, R.id.iv_classic);
			TextView tv_brand = BaseViewHolder.get(convertView, R.id.tv_brand);
			TextView tv_price = BaseViewHolder.get(convertView, R.id.tv_price);
			Picasso.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + list.get(position).getSpicfirst())
					.into(iv_classic);
			tv_brand.setText(list.get(position).getSname());
			tv_price.setText("￥" + list.get(position).getSprice());

			return convertView;
		}

	}

	public class GoodsListBean {
		private String error;
		private String msg;
		private GoodsInfo shop;
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

		public GoodsInfo getShop() {
			return shop;
		}

		public void setShop(GoodsInfo shop) {
			this.shop = shop;
		}

		public String getTotalNum() {
			return totalNum;
		}

		public void setTotalNum(String totalNum) {
			this.totalNum = totalNum;
		}

		private class GoodsInfo {

			private int currPage;
			private List<GoodsDetail> page;
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

			public List<GoodsDetail> getPage() {
				return page;
			}

			public void setPage(List<GoodsDetail> page) {
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

			private class GoodsDetail {
				private String article_number;
				private String audit;
				private String bname;
				private String cost_price;
				private String entityId;
				private String id;
				private String mname;
				private String persistent;
				private String putaway;
				private String samount;
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
				private String spicfirst;
				private String spid;
				private String sprice;
				private String stime;
				private String tname;
				private String tpid;

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

				public String getBname() {
					return bname;
				}

				public void setBname(String bname) {
					this.bname = bname;
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

				public String getSpicfirst() {
					return spicfirst;
				}

				public void setSpicfirst(String spicfirst) {
					this.spicfirst = spicfirst;
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

				public String getTname() {
					return tname;
				}

				public void setTname(String tname) {
					this.tname = tname;
				}

				public String getTpid() {
					return tpid;
				}

				public void setTpid(String tpid) {
					this.tpid = tpid;
				}
			}
		}

	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mMain_pull_refresh_view.postDelayed(new Runnable() {
			public void run() {
				mMain_pull_refresh_view.onFooterRefreshComplete();
				getData(currentPage);
				mAdapter.notifyDataSetChanged();
			}
		}, 500);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mMain_pull_refresh_view.postDelayed(new Runnable() {

			@Override
			public void run() {
				mMain_pull_refresh_view.onHeaderRefreshComplete();
				mAdapter.removeAll();
				getData(1);
				mAdapter.notifyDataSetChanged();
			}
		}, 500);
	}

}
