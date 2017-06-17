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
import com.mybetterandroid.wheel.widget.CirclePageIndicator;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class TravelDeepActivity extends BaseActivity implements OnClickListener {
	private ViewPager home_viewpager;
	private CirclePageIndicator home_cpi;
	private List<TravelDeepAdvertisements> list_adver = new ArrayList<TravelDeepAdvertisements>();
	private List<TravelDeepTheme> list_theme = new ArrayList<TravelDeepTheme>();
	private List<TravelDeepInfo> list_shop = new ArrayList<TravelDeepInfo>();
	private List<TravelDeepInfo> list_helth = new ArrayList<TravelDeepInfo>();
	private List<TravelDeepInfo> list_pulley = new ArrayList<TravelDeepInfo>();
	private List<TravelDeepInfo> list_extrem = new ArrayList<TravelDeepInfo>();
	private TravelDeepBean mBean;
	private GridView mGridview_extremity;
	private GridView mGridview_helth;
	private GridView mGridview_pulley;
	private RelativeLayout mRe_health_travel;
	private RelativeLayout mRe_pulley_travel;
	private RelativeLayout mRe_extremity_travel;
	private Intent mIntent;

	@Override
	public int getContainerView() {
		return R.layout.activity_deep_travel;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("深度旅行");
		home_viewpager = (ViewPager) findViewById(R.id.home_viewpager);
		home_cpi = (CirclePageIndicator) findViewById(R.id.home_cpi);
		int ballradius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3,
				getResources().getDisplayMetrics());
		home_cpi.setRadius(ballradius);
		home_cpi.setFillColor(Color.GRAY);
		home_cpi.setPageColor(Color.WHITE);
		home_cpi.setStrokeWidth(0);
		initUI();
		initData();

	}

	private void initUI() {
		mGridview_helth = (GridView) findViewById(R.id.gridview_helth);
		mGridview_pulley = (GridView) findViewById(R.id.gridview_pulley);
		mGridview_extremity = (GridView) findViewById(R.id.gridview_extremity);

		mRe_health_travel = (RelativeLayout) findViewById(R.id.re_health_travel);
		mRe_pulley_travel = (RelativeLayout) findViewById(R.id.re_pulley_travel);
		mRe_extremity_travel = (RelativeLayout) findViewById(R.id.re_extremity_travel);
		mRe_health_travel.setOnClickListener(this);
		mRe_pulley_travel.setOnClickListener(this);
		mRe_extremity_travel.setOnClickListener(this);

	}

	private void initData() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("OPT", "270");
		new HttpClientGet(TravelDeepActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("深度旅游" + result);
				mBean = GsonUtil.getInstance().fromJson(result, TravelDeepBean.class);
				list_adver = mBean.getAdvertisements();
				// 轮播图片
				home_viewpager.setAdapter(pageAdapter);
				home_cpi.setViewPager(home_viewpager);
				home_cpi.setOnPageChangeListener(pageListener);
				pageListener.onPageSelected(0);

				// 纵向列表数据
				list_theme = mBean.getTheme_types();

				// 健康游
				list_helth = list_theme.get(0).getShop();
				int size = list_helth.size();
				int length = 200;
				DisplayMetrics dm = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm);
				float density = dm.density;
				int gridviewWidth = (int) (size * (length + 4) * density);
				int itemWidth = (int) (length * density);

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridviewWidth,
						LinearLayout.LayoutParams.MATCH_PARENT);
				mGridview_helth.setLayoutParams(params); // 重点
				mGridview_helth.setColumnWidth(itemWidth); // 重点
				mGridview_helth.setHorizontalSpacing(15); // 间距
				mGridview_helth.setStretchMode(GridView.NO_STRETCH);
				mGridview_helth.setNumColumns(size); // 重点

				HealthAdapter recommend_adapter = new HealthAdapter(TravelDeepActivity.this, list_helth);
				mGridview_helth.setAdapter(recommend_adapter);

				// 游轮游
				list_pulley = list_theme.get(1).getShop();
				int size2 = list_pulley.size();
				int length2 = 200;
				DisplayMetrics dm2 = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm2);
				float density2 = dm2.density;
				int gridviewWidth2 = (int) (size2 * (length2 + 4) * density2);
				int itemWidth2 = (int) (length2 * density2);
				LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(gridviewWidth2,
						LinearLayout.LayoutParams.MATCH_PARENT);

				mGridview_pulley.setLayoutParams(params2); // 重点
				mGridview_pulley.setColumnWidth(itemWidth2); // 重点
				mGridview_pulley.setHorizontalSpacing(15); // 间距
				mGridview_pulley.setStretchMode(GridView.NO_STRETCH);
				mGridview_pulley.setNumColumns(size2); // 重点
				ShopAdapter food_adapter = new ShopAdapter(TravelDeepActivity.this, list_pulley);
				mGridview_pulley.setAdapter(food_adapter);

				// 极限游
				list_extrem = list_theme.get(2).getShop();
				int size3 = list_extrem.size();
				int length3 = 200;
				DisplayMetrics dm3 = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm3);
				float density3 = dm3.density;
				int gridviewWidth3 = (int) (size3 * (length3 + 4) * density3);
				int itemWidth3 = (int) (length3 * density3);
				LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(gridviewWidth3,
						LinearLayout.LayoutParams.MATCH_PARENT);

				mGridview_extremity.setLayoutParams(params3); // 重点
				mGridview_extremity.setColumnWidth(itemWidth3); // 重点
				mGridview_extremity.setHorizontalSpacing(15); // 间距
				mGridview_extremity.setStretchMode(GridView.NO_STRETCH);
				mGridview_extremity.setNumColumns(size3); // 重点
				ExtremAdapter good_adapter = new ExtremAdapter(TravelDeepActivity.this, list_extrem);
				mGridview_extremity.setAdapter(good_adapter);

			}

			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});

	}

	public class HealthAdapter extends BaseAdapter {// 健康游
		private Context context;
		private List<TravelDeepInfo> list_helth;
		private ViewHolder mHolder;

		public HealthAdapter(Context context, List<TravelDeepInfo> list_helth) {
			this.context = context;
			this.list_helth = list_helth;
		}

		@Override
		public int getCount() {
			return list_helth == null ? 0 : list_helth.size();
		}

		@Override
		public Object getItem(int position) {
			return list_helth.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.item_shop, null);
				mHolder = new ViewHolder(convertView);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			Glide.with(context).load(HttpConfig.IMAGEHOST + list_helth.get(position).getSpicfirst())
					.into(mHolder.mIv_one);
			mGridview_helth.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(getApplicationContext(), TaiWanHealthTravelActivity.class);
					intent.putExtra("spid", list_helth.get(position).getId());// 商品id
					intent.putExtra("ttid", mBean.getTheme_types().get(0).getTtid());// 主题id
					intent.putExtra("name", mBean.getTheme_types().get(0).getTheme_name());// 主题名称
					intent.putExtra("tdapid", list_helth.get(position).getTdapid());// 区域id
					startActivity(intent);
				}
			});

			return convertView;
		}

	}

	public class ViewHolder {

		private ImageView mIv_one;

		public ViewHolder(View view) {
			mIv_one = (ImageView) view.findViewById(R.id.iv_one);
		}
	}

	public class ShopAdapter extends BaseAdapter {// 游轮游
		private Context context;
		private List<TravelDeepInfo> list_pulley;
		private ViewHolder3 mHolder3;

		public ShopAdapter(Context context, List<TravelDeepInfo> list_pulley) {
			this.context = context;
			this.list_pulley = list_pulley;
		}

		@Override
		public int getCount() {
			return list_pulley == null ? 0 : list_pulley.size();
		}

		@Override
		public Object getItem(int position) {
			return list_pulley.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.item_shop, null);
				mHolder3 = new ViewHolder3(convertView);
				convertView.setTag(mHolder3);
			} else {
				mHolder3 = (ViewHolder3) convertView.getTag();
			}

			Glide.with(context).load(HttpConfig.IMAGEHOST + list_pulley.get(position).getSpicfirst())
					.into(mHolder3.mIv_one);
			mGridview_pulley.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(getApplicationContext(), TaiWanHealthTravelActivity.class);
					intent.putExtra("spid", list_pulley.get(position).getId());// 商品id
					intent.putExtra("ttid", mBean.getTheme_types().get(1).getTtid());// 主题id
					intent.putExtra("name", mBean.getTheme_types().get(1).getTheme_name());
					intent.putExtra("tdapid", list_pulley.get(position).getTdapid());
					startActivity(intent);
				}
			});

			return convertView;
		}

	}

	public class ViewHolder3 {
		private ImageView mIv_one;

		public ViewHolder3(View view) {
			mIv_one = (ImageView) view.findViewById(R.id.iv_one);
		}
	}

	public class ExtremAdapter extends BaseAdapter {// 极限游
		private Context context;
		private List<TravelDeepInfo> list_extrem;
		private ViewHolder3 mHolder3;

		public ExtremAdapter(Context context, List<TravelDeepInfo> list_extrem) {
			this.context = context;
			this.list_extrem = list_extrem;
		}

		@Override
		public int getCount() {
			return list_extrem == null ? 0 : list_extrem.size();
		}

		@Override
		public Object getItem(int position) {
			return list_extrem.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.item_shop, null);
				mHolder3 = new ViewHolder3(convertView);
				convertView.setTag(mHolder3);
			} else {
				mHolder3 = (ViewHolder3) convertView.getTag();
			}

			Glide.with(context).load(HttpConfig.IMAGEHOST + list_extrem.get(position).getSpicfirst())
					.into(mHolder3.mIv_one);
			mGridview_extremity.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(getApplicationContext(), TaiWanHealthTravelActivity.class);
					intent.putExtra("spid", list_extrem.get(position).getId());// 商品id
					intent.putExtra("ttid", mBean.getTheme_types().get(2).getTtid());// 主题id
					intent.putExtra("name", mBean.getTheme_types().get(2).getTheme_name());
					intent.putExtra("tdapid", list_extrem.get(position).getTdapid());
					startActivity(intent);
				}
			});
			return convertView;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.re_health_travel:
			mIntent = new Intent(getApplicationContext(), TravelBaseActivity.class);
			mIntent.putExtra("ttid", mBean.getTheme_types().get(0).getTtid());
			mIntent.putExtra("name", mBean.getTheme_types().get(0).getTheme_name());
			startActivity(mIntent);
			break;
		case R.id.re_pulley_travel:
			mIntent = new Intent(getApplicationContext(), TravelBaseActivity.class);
			mIntent.putExtra("ttid", mBean.getTheme_types().get(1).getTtid());
			mIntent.putExtra("name", mBean.getTheme_types().get(1).getTheme_name());
			startActivity(mIntent);
			break;
		case R.id.re_extremity_travel:
			mIntent = new Intent(getApplicationContext(), TravelBaseActivity.class);
			mIntent.putExtra("ttid", mBean.getTheme_types().get(2).getTtid());
			mIntent.putExtra("name", mBean.getTheme_types().get(2).getTheme_name());
			startActivity(mIntent);
			break;

		}
	}

	private int mLastXIntercept;
	private int mLastYIntercept;

	public boolean onInterceptTouchEvent(MotionEvent event) {
		boolean intercepted = false;
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			intercepted = false;
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			int deltaX = x - mLastXIntercept;
			int deltaY = y = mLastYIntercept;
			if (Math.abs(deltaX) > Math.abs(deltaY)) {
				intercepted = true;
			} else {
				intercepted = false;
			}
			break;
		}
		case MotionEvent.ACTION_UP: {
			intercepted = false;
			break;
		}
		default:
			break;
		}
		mLastXIntercept = x;
		mLastYIntercept = y;
		return intercepted;
	}

	private PagerAdapter pageAdapter = new PagerAdapter() {

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return list_adver == null ? 0 : list_adver.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			ImageView iv = new ImageView(container.getContext());
			Picasso.with(TravelDeepActivity.this)
					.load(HttpConfig.IMAGEHOST + list_adver.get(position).getImage_filename()).into(iv);
			iv.setScaleType(ScaleType.FIT_XY);
			container.addView(iv);
			iv.setOnClickListener(new OnClickListener() {
				private Intent mIntent;

				@Override
				public void onClick(View v) {
					mIntent = new Intent(TravelDeepActivity.this, TravelWebViewActivity.class);
					if (position == 0) {
						mIntent.putExtra("url", list_adver.get(0).getUrl());
						mIntent.putExtra("title", "travel");
					} else if (position == 1) {
						mIntent.putExtra("url", list_adver.get(1).getUrl());
						mIntent.putExtra("title", "travel");
					} else if (position == 2) {
						mIntent.putExtra("url", list_adver.get(2).getUrl());
						mIntent.putExtra("title", "travel");
					} else if (position == 3) {
						mIntent.putExtra("url", list_adver.get(3).getUrl());
						mIntent.putExtra("title", "travel");
					}
					startActivity(mIntent);
				}
			});

			return iv;

		}
	};
	private int current;
	private OnPageChangeListener pageListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			current = position;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	public class TravelDeepBean {

		private List<TravelDeepAdvertisements> advertisements;
		private String error;
		private String msg;
		private List<TravelDeepTheme> theme_types;

		public List<TravelDeepAdvertisements> getAdvertisements() {
			return advertisements;
		}

		public void setAdvertisements(List<TravelDeepAdvertisements> advertisements) {
			this.advertisements = advertisements;
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

		public List<TravelDeepTheme> getTheme_types() {
			return theme_types;
		}

		public void setTheme_types(List<TravelDeepTheme> theme_types) {
			this.theme_types = theme_types;
		}
	}

	public class TravelDeepAdvertisements {
		private String entityId;
		private String file_format;
		private String file_size;
		private String id;
		private String image_filename;
		private String is_link_enabled;
		private String is_use;
		private String location;
		private String no;
		private String persistent;
		private String resolution;
		private String target;
		private String url;

		public String getEntityId() {
			return entityId;
		}

		public void setEntityId(String entityId) {
			this.entityId = entityId;
		}

		public String getFile_format() {
			return file_format;
		}

		public void setFile_format(String file_format) {
			this.file_format = file_format;
		}

		public String getFile_size() {
			return file_size;
		}

		public void setFile_size(String file_size) {
			this.file_size = file_size;
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

		public String getIs_link_enabled() {
			return is_link_enabled;
		}

		public void setIs_link_enabled(String is_link_enabled) {
			this.is_link_enabled = is_link_enabled;
		}

		public String getIs_use() {
			return is_use;
		}

		public void setIs_use(String is_use) {
			this.is_use = is_use;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public String getNo() {
			return no;
		}

		public void setNo(String no) {
			this.no = no;
		}

		public String getPersistent() {
			return persistent;
		}

		public void setPersistent(String persistent) {
			this.persistent = persistent;
		}

		public String getResolution() {
			return resolution;
		}

		public void setResolution(String resolution) {
			this.resolution = resolution;
		}

		public String getTarget() {
			return target;
		}

		public void setTarget(String target) {
			this.target = target;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}

	public class TravelDeepTheme {
		private List<TravelDeepInfo> shop;
		private String theme_name;
		private String ttid;

		public List<TravelDeepInfo> getShop() {
			return shop;
		}

		public void setShop(List<TravelDeepInfo> shop) {
			this.shop = shop;
		}

		public String getTheme_name() {
			return theme_name;
		}

		public void setTheme_name(String theme_name) {
			this.theme_name = theme_name;
		}

		public String getTtid() {
			return ttid;
		}

		public void setTtid(String ttid) {
			this.ttid = ttid;
		}
	}

	public class TravelDeepInfo {
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
		private String sname;
		private String snew;
		private String spic;
		private String spicfirst;
		private String spid;
		private String sprice;
		private String tdapid;
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
