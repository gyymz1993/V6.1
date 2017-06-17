package com.lsjr.zizisteward.common.activtiy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.BrandActivity;
import com.lsjr.zizisteward.activity.BrandToGoodsListActivity;
import com.lsjr.zizisteward.activity.BrandActivity.BrandAdapter;
import com.lsjr.zizisteward.activity.BrandActivity.DianPuAdapter;
import com.lsjr.zizisteward.activity.BrandActivity.DianPuList;
import com.lsjr.zizisteward.activity.BrandActivity.DianPuListDetail;
import com.lsjr.zizisteward.adapter.BaseViewHolder;
import com.lsjr.zizisteward.bean.BrandBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.newview.MyGridView;
import com.lsjr.zizisteward.utils.DensityUtil;
import com.lsjr.zizisteward.utils.GsonUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class ShopFragment extends Fragment {
	private View rootView;
	private MyGridView brand_gridview;
	private List<DianPuListDetail> list = new ArrayList<DianPuListDetail>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_brand, null);
			brand_gridview = (MyGridView) rootView.findViewById(R.id.brand_gridview);
			int px = DensityUtil.dip2px(getContext(), 3);
			brand_gridview.setHorizontalSpacing(px);
			brand_gridview.setVerticalSpacing(px);
			initData();
		}
		return rootView;
	}

	private void initData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("OPT", "299");// 店铺列表
		new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("消息" + result);
				DianPuList dian_bean = GsonUtil.getInstance().fromJson(result, DianPuList.class);
				list = dian_bean.getStore();
				DianPuAdapter adapter2 = new DianPuAdapter(getContext(), list);
				brand_gridview.setAdapter(adapter2);
				brand_gridview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Intent intent = new Intent(getContext(), BrandToGoodsListActivity.class);// 店铺对应的商品列表
						intent.putExtra("type", "dianpu");
						intent.putExtra("id", list.get(position).getId());
						startActivity(intent);
					}
				});

			}

			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});
	}

	public class DianPuAdapter extends BaseAdapter {
		private Context context;
		private List<DianPuListDetail> list;

		public DianPuAdapter(Context context, List<DianPuListDetail> list) {
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
				convertView = LayoutInflater.from(context).inflate(R.layout.item_brand, null);
			}
			ImageView iv_brand = BaseViewHolder.get(convertView, R.id.iv_brand);
			Glide.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getBlogo())
					.animate(android.R.anim.slide_in_left).into(iv_brand);
			return convertView;
		}

	}
}
