package com.lsjr.zizisteward.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.BaseViewHolder;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.BrandBean;
import com.lsjr.zizisteward.bean.BrandBean.BrandInfo;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.newview.MyGridView;
import com.lsjr.zizisteward.utils.DensityUtil;
import com.lsjr.zizisteward.utils.GsonUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class BrandActivity extends BaseActivity {
	private MyGridView brand_gridview;
	private List<BrandInfo> list = new ArrayList<BrandInfo>();
	private List<DianPuListDetail> list2 = new ArrayList<DianPuListDetail>();
	private String mType;

	@Override
	public int getContainerView() {
		return R.layout.fragment_brand;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		mType = intent.getStringExtra("type");
		if ("1".equals(mType)) {
			setmTitle("店铺");
		} else {
			setmTitle("品牌");
		}
		brand_gridview = (MyGridView) findViewById(R.id.brand_gridview);
		int px = DensityUtil.dip2px(BrandActivity.this, 3);
		brand_gridview.setHorizontalSpacing(px);
		brand_gridview.setVerticalSpacing(px);
		initData();
	}

	private void initData() {
		Map<String, String> map = new HashMap<String, String>();
		if ("1".equals(mType)) {
			map.put("OPT", "299");// 店铺列表
		} else {
			map.put("OPT", "22");// 品牌列表
		}
		new HttpClientGet(BrandActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("消息" + result);
				if ("1".equals(mType)) {
					DianPuList dian_bean = GsonUtil.getInstance().fromJson(result, DianPuList.class);
					list2 = dian_bean.getStore();
					DianPuAdapter adapter2 = new DianPuAdapter(BrandActivity.this, list2);
					brand_gridview.setAdapter(adapter2);
					brand_gridview.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							Intent intent = new Intent(BrandActivity.this, BrandToGoodsListActivity.class);// 店铺对应的商品列表
							intent.putExtra("type", "dianpu");
							intent.putExtra("id", list2.get(position).getId());
							startActivity(intent);
						}
					});
				} else {
					BrandBean bean = GsonUtil.getInstance().fromJson(result, BrandBean.class);
					list = bean.getBrands();
					BrandAdapter adapter = new BrandAdapter(BrandActivity.this, list);
					brand_gridview.setAdapter(adapter);
					brand_gridview.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							Intent intent = new Intent(BrandActivity.this, BrandToGoodsListActivity.class);// 品牌对应的商品列表
							intent.putExtra("id", list.get(position).getId());
							intent.putExtra("type", "pinpai");
							startActivity(intent);
						}
					});
				}
			}

			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});
	}

	public class BrandAdapter extends BaseAdapter {
		private Context context;
		private List<BrandInfo> list;

		public BrandAdapter(Context context, List<BrandInfo> list) {
			this.context = context;
			this.list = list;

		}

		@Override
		public int getCount() {
			return list.size();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_brand, null);
			}
			ImageView iv_brand = BaseViewHolder.get(convertView, R.id.iv_brand);

			Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + list.get(position).getBlogo())
					.animate(android.R.anim.slide_in_left).into(iv_brand);
			return convertView;
		}

	}

	public class DianPuAdapter extends BaseAdapter {
		private Context context;
		private List<DianPuListDetail> list2;

		public DianPuAdapter(Context context, List<DianPuListDetail> list2) {
			this.context = context;
			this.list2 = list2;

		}

		@Override
		public int getCount() {
			return list2.size();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_brand, null);
			}
			ImageView iv_brand = BaseViewHolder.get(convertView, R.id.iv_brand);
			Glide.with(getApplicationContext()).load(HttpConfig.IMAGEHOST + list2.get(position).getBlogo())
					.animate(android.R.anim.slide_in_left).into(iv_brand);
			return convertView;
		}

	}

	public class RotateTransformation extends BitmapTransformation {

		private float rotateRotationAngle = 0f;

		public RotateTransformation(Context context, float rotateRotationAngle) {
			super(context);

			this.rotateRotationAngle = rotateRotationAngle;
		}

		@Override
		protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
			Matrix matrix = new Matrix();

			matrix.postRotate(rotateRotationAngle);

			return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix,
					true);
		}

		@Override
		public String getId() {
			return "rotate" + rotateRotationAngle;
		}

	}

	public class DianPuList {
		private List<DianPuListDetail> store;

		public List<DianPuListDetail> getStore() {
			return store;
		}

		public void setStore(List<DianPuListDetail> store) {
			this.store = store;
		}
	}

	public class DianPuListDetail {
		private String bdesc;
		private String bimg;
		private String bis_show;
		private String blogo;
		private String bname;
		private String brand_time;
		private String brand_type;
		private String bsort;
		private String bstory;
		private String burl;
		private String entityId;
		private String id;
		private String persistent;

		public String getBdesc() {
			return bdesc;
		}

		public void setBdesc(String bdesc) {
			this.bdesc = bdesc;
		}

		public String getBimg() {
			return bimg;
		}

		public void setBimg(String bimg) {
			this.bimg = bimg;
		}

		public String getBis_show() {
			return bis_show;
		}

		public void setBis_show(String bis_show) {
			this.bis_show = bis_show;
		}

		public String getBlogo() {
			return blogo;
		}

		public void setBlogo(String blogo) {
			this.blogo = blogo;
		}

		public String getBname() {
			return bname;
		}

		public void setBname(String bname) {
			this.bname = bname;
		}

		public String getBrand_time() {
			return brand_time;
		}

		public void setBrand_time(String brand_time) {
			this.brand_time = brand_time;
		}

		public String getBrand_type() {
			return brand_type;
		}

		public void setBrand_type(String brand_type) {
			this.brand_type = brand_type;
		}

		public String getBsort() {
			return bsort;
		}

		public void setBsort(String bsort) {
			this.bsort = bsort;
		}

		public String getBstory() {
			return bstory;
		}

		public void setBstory(String bstory) {
			this.bstory = bstory;
		}

		public String getBurl() {
			return burl;
		}

		public void setBurl(String burl) {
			this.burl = burl;
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

		public String getPersistent() {
			return persistent;
		}

		public void setPersistent(String persistent) {
			this.persistent = persistent;
		}
	}
}
