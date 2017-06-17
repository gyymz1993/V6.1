package com.lsjr.zizisteward.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//更多列表      
public class TravelBaseActivity extends BaseActivity {

	private String mTtid;
	private List<TravelBasicInfo> list = new ArrayList<TravelBasicInfo>();
	private SuperListView mListview;
	private boolean isRefresh = true;
	private int pageNum = 1;
	private ListAdapter mAdapter;
	private String mName;

	@Override
	public int getContainerView() {
		return R.layout.activity_travel_base;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTtid = getIntent().getStringExtra("ttid");
		mName = getIntent().getStringExtra("name");
		mListview = (SuperListView) findViewById(R.id.listview);
		setmTitle(mName);

		init();
	}

	private void init() {
		mAdapter = new ListAdapter(TravelBaseActivity.this, list);
		mListview.setAdapter(mAdapter);
		mListview.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				isRefresh = true;
				getData();
			}
		});
		mListview.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				isRefresh = false;
				getData();
			}
		});
		mListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getApplicationContext(), TaiWanHealthTravelActivity.class);
				intent.putExtra("spid", list.get(position - 1).getId());// 商品id
				intent.putExtra("ttid", list.get(position - 1).getThemeId());// 主题id
				intent.putExtra("name", mName);
				intent.putExtra("tdapid", list.get(position - 1).getTdapid());// 区域id
				startActivity(intent);
			}
		});
		mListview.refresh();
	}

	private void getData() {
		if (isRefresh) {
			pageNum = 1;
			mAdapter.removeAll();
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("OPT", "271");
		map.put("ttid", mTtid);
		map.put("currPage", String.valueOf(pageNum++));
		new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("主题分类" + result);
				TravelBasicBean bean = GsonUtil.getInstance().fromJson(result, TravelBasicBean.class);
				if (pageNum == 1) {
					list = bean.getShop();
					mAdapter = new ListAdapter(TravelBaseActivity.this, list);
					mListview.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();

				} else {
					list.addAll(bean.getShop());
					mAdapter.setList(list);
				}
				if (list.size() < bean.getShop().size()) {
					mListview.setIsLoadFull(false);
				}
				mListview.finishRefresh();
				mListview.finishLoadMore();
			}

			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});
	}

	public class ListAdapter extends BaseAdapter {
		private Context context;
		private List<TravelBasicInfo> list;
		private ViewHolder mHolder;

		public ListAdapter(Context context, List<TravelBasicInfo> list) {
			this.context = context;
			this.list = list;

		}

		public void setList(List<TravelBasicInfo> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		public void add(TravelBasicInfo page) {
			this.list.add(page);
			notifyDataSetChanged();
		}

		public void addFirst(TravelBasicInfo page) {
			this.list.add(0, page);
			notifyDataSetChanged();
		}

		public void addAll(List<TravelBasicInfo> list) {
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
				convertView = LayoutInflater.from(context).inflate(R.layout.item_recommend, null);
				mHolder = new ViewHolder(convertView);
				convertView.setTag(mHolder);

			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			Glide.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getSpicfirst()).centerCrop()
					.animate(android.R.anim.slide_in_left).into(mHolder.mIv_recommend);
			mHolder.mTv_one.setText(list.get(position).getSname());
			mHolder.mTv_two.setText(list.get(position).getSkeyword());

			return convertView;
		}

		private class ViewHolder {
			private ImageView mIv_recommend;
			private TextView mTv_one;
			private TextView mTv_two;

			public ViewHolder(View v) {
				mIv_recommend = (ImageView) v.findViewById(R.id.iv_recommend);
				mTv_one = (TextView) v.findViewById(R.id.tv_one);
				mTv_two = (TextView) v.findViewById(R.id.tv_two);
			}
		}
	}

	public class TravelBasicBean {
		private String error;
		private String msg;
		private List<TravelBasicInfo> shop;

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

		public List<TravelBasicInfo> getShop() {
			return shop;
		}

		public void setShop(List<TravelBasicInfo> shop) {
			this.shop = shop;
		}
	}

	public class TravelBasicInfo {
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
		private String spicfirst;
		private String spid;
		private String sprice;
		private String tdapid;
		private String stime;
		private String themeId;
		private String tname;
		private String tpid;

		public String getTdapid() {
			return tdapid;
		}

		public void setTdapid(String tdapid) {
			this.tdapid = tdapid;
		}

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

		public String getThemeId() {
			return themeId;
		}

		public void setThemeId(String themeId) {
			this.themeId = themeId;
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
