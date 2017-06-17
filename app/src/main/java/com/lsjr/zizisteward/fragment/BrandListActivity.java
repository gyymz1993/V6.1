package com.lsjr.zizisteward.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.HomeBrandDetail;
import com.lsjr.zizisteward.adapter.BaseViewHolder;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.HomeBrandBean;
import com.lsjr.zizisteward.bean.HomeBrandBean.HomePageDetail;
import com.lsjr.zizisteward.bean.LianJieBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.DensityUtil;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.squareup.picasso.Picasso;

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

public class BrandListActivity extends BaseActivity {

	private GridView brand_list_gridview;
	private String brand_id;

	@Override
	public int getContainerView() {
		// TODO Auto-generated method stub
		return R.layout.activity_brand_list;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setmTitle("商品列表");
		brand_list_gridview = (GridView) findViewById(R.id.brand_list_gridview);
		int px = DensityUtil.dip2px(this, 3);
		brand_list_gridview.setHorizontalSpacing(px);
		brand_list_gridview.setVerticalSpacing(px);
		brand_id = getIntent().getStringExtra("id");
		getData();
		initLayout();

	}

	private List<HomePageDetail> list = new ArrayList<HomePageDetail>();

	private void getData() {
		int pageNum = 1;
		Map<String, String> map = new HashMap<String, String>();
		map.put("OPT", "194");
		map.put("sbid", brand_id);
		map.put("currPage", String.valueOf(pageNum++));
		new HttpClientGet(BrandListActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("品牌进来的商品列表" + result);
				HomeBrandBean bean = GsonUtil.getInstance().fromJson(result, HomeBrandBean.class);
				list = bean.getPage().getPage();
				initLayout();
			}

			@Override
			public void onFailure(MyError myError) {
				// TODO Auto-generated method stub
				super.onFailure(myError);
			}
		});

	}

	private String mProductUrl;
	private LianJieBean mBean;

	private void initLayout() {
		MyGridView adapter = new MyGridView();
		brand_list_gridview.setAdapter(adapter);
		brand_list_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				// 这里判断是否登录
				boolean state = PreferencesUtils.getBoolean(BrandListActivity.this, "isLogin");

				if (state == false) {// 未登录 游客登录
					Map<String, String> map = new HashMap<String, String>();
					map.put("OPT", "37");
					map.put("sid", list.get(position).getId());
					map.put("user_id", "");
					new HttpClientGet(BrandListActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

						@Override
						public void onSuccess(String result) {
							System.out.println("游客登录消息" + result);
							mBean = GsonUtil.getInstance().fromJson(result, LianJieBean.class);
							mProductUrl = mBean.getProductUrl();
							Intent intent = new Intent(BrandListActivity.this, HomeBrandDetail.class);
							intent.putExtra("mProductUrl", mProductUrl);
							intent.putExtra("name", list.get(position).getSname());

							startActivity(intent);
						}

						public void onFailure(MyError myError) {
							super.onFailure(myError);
						};

					});
				}
				if (state == true) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("OPT", "37");
					map.put("id", App.getUserInfo().getId());
					map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));

					map.put("sid", list.get(position).getId());
					new HttpClientGet(BrandListActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

						@Override
						public void onSuccess(String result) {
							System.out.println("链接消息" + result);
							mBean = GsonUtil.getInstance().fromJson(result, LianJieBean.class);
							mProductUrl = mBean.getProductUrl();

							Intent intent = new Intent(BrandListActivity.this, HomeBrandDetail.class);
							intent.putExtra("mProductUrl", mProductUrl);
							intent.putExtra("name", list.get(position).getBname());
							startActivity(intent);
						}

						@Override
						public void onFailure(MyError myError) {
							super.onFailure(myError);
						}
					});
				}

			}
		});
	}

	public class MyGridView extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list == null ? 0 : list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(BrandListActivity.this).inflate(R.layout.item_brand_list, parent,
						false);
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

}
