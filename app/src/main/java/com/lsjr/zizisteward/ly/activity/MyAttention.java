package com.lsjr.zizisteward.ly.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.MainPageAdapter;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

/** 我的关注 */
public class MyAttention extends FragmentActivity implements
		OnPageChangeListener, OnClickListener {

	/** 布局 */
	private View rootView;
	/** 店铺 */
	private RelativeLayout rl_store;
	private TextView tv_store;
	private View v_store;
	/** 品牌 */
	private RelativeLayout rl_brand;
	private TextView tv_brand;
	private View v_brand;
	/** ViewPager */
	private ViewPager vp;

	/** 存放3个子界面 */
	private List<Fragment> fragments = new ArrayList<Fragment>();
	/** 子界面适配器 */
	private MainPageAdapter mpa;

	private int child = 0;
	public static TextView tv;
	private TextView tv_cancel;
	private LinearLayout ll_back;
	private LinearLayout ll_cancel;
	private LinearLayout ll_editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.my_attention_activity);

		/** 店铺 */
		fragments.add(new M_F_Store());

		/** 品牌 */
		fragments.add(new M_F_Brand());

		this.v_store = super.findViewById(R.id.v_store);
		this.v_brand = super.findViewById(R.id.v_brand);
		this.tv = (TextView) super.findViewById(R.id.tv);
		this.vp = (ViewPager) super.findViewById(R.id.vp);
		this.tv_store = (TextView) super.findViewById(R.id.tv_store);
		this.tv_brand = (TextView) super.findViewById(R.id.tv_brand);
		this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
		this.tv_cancel = (TextView) super.findViewById(R.id.tv_cancel);
		this.rl_store = (RelativeLayout) super.findViewById(R.id.rl_store);
		this.rl_brand = (RelativeLayout) super.findViewById(R.id.rl_brand);
		this.ll_cancel = (LinearLayout) super.findViewById(R.id.ll_cancel);
		this.ll_editor = (LinearLayout) super.findViewById(R.id.ll_editor);

		this.mpa = new MainPageAdapter(getSupportFragmentManager(), fragments);
		this.vp.setAdapter(mpa);

		this.vp.setOffscreenPageLimit(fragments.size() - 1);
		this.vp.addOnPageChangeListener(this);

		this.ll_back.setOnClickListener(this);
		this.rl_store.setOnClickListener(this);
		this.rl_brand.setOnClickListener(this);
		this.tv_cancel.setOnClickListener(this);
		this.ll_cancel.setOnClickListener(this);
		this.ll_editor.setOnClickListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {

		getTabState(position);

	}

	private void getTabState(int index) {

		child = index;

		switch (index) {

		case 0:

			CancelBrand();
			
			this.v_store.setVisibility(View.VISIBLE);
			this.tv_store.setTextColor(Color.parseColor("#FF9900"));

			this.v_brand.setVisibility(View.INVISIBLE);
			this.tv_brand.setTextColor(Color.parseColor("#323232"));

			break;

		case 1:
			
			CancelStore();

			this.v_store.setVisibility(View.INVISIBLE);
			this.tv_store.setTextColor(Color.parseColor("#323232"));

			this.v_brand.setVisibility(View.VISIBLE);
			this.tv_brand.setTextColor(Color.parseColor("#FF9900"));

			break;
		}
	}

	private void CancelStore() {
		ll_back.setVisibility(View.VISIBLE);
		ll_cancel.setVisibility(View.GONE);
		tv_cancel.setVisibility(View.GONE);
		
		tv.setText("管理");
		
		for (int i = 0; i < M_F_Store.page.size(); i++) {
			M_F_Store.page.get(i).setSpace(false);
		}
		
		M_F_Store.ly = false;
		M_F_Store.adapter.notifyDataSetChanged();
	}

	private void CancelBrand() {
		
		ll_back.setVisibility(View.VISIBLE);
		ll_cancel.setVisibility(View.GONE);
		tv_cancel.setVisibility(View.GONE);
		
		tv.setText("管理");
		
		for (int i = 0; i < M_F_Brand.page.size(); i++) {
			M_F_Brand.page.get(i).setSpace(false);
		}
		
		M_F_Brand.ly = false;
		M_F_Brand.adapter.notifyDataSetChanged();
	}
	
	private void CheckStore() {

		if (tv.getText().toString().equals("管理")) {
			tv.setText("全选");
			tv_cancel.setVisibility(View.VISIBLE);
		} else if (tv.getText().toString().equals("反选")) {
			for (int i = 0; i < M_F_Store.page.size(); i++) {
				M_F_Store.page.get(i).setSpace(
						!M_F_Store.page.get(i).isSpace());
			}
			M_F_Store.adapter.notifyDataSetChanged();
			tv.setText("全选");
		} else if (tv.getText().toString().equals("全选")) {
			for (int i = 0; i < M_F_Store.page.size(); i++) {
				M_F_Store.page.get(i).setSpace(true);
			}
			M_F_Store.adapter.notifyDataSetChanged();
			tv.setText("反选");
		}

		M_F_Store.ly = true;
		M_F_Store.adapter.notifyDataSetChanged();

	}
	
	private void CheckBrand() {

		if (tv.getText().toString().equals("管理")) {
			tv.setText("全选");
			tv_cancel.setVisibility(View.VISIBLE);
		} else if (tv.getText().toString().equals("反选")) {
			for (int i = 0; i < M_F_Brand.page.size(); i++) {
				M_F_Brand.page.get(i).setSpace(
						!M_F_Brand.page.get(i).isSpace());
			}
			M_F_Brand.adapter.notifyDataSetChanged();
			tv.setText("全选");
		} else if (tv.getText().toString().equals("全选")) {
			for (int i = 0; i < M_F_Brand.page.size(); i++) {
				M_F_Brand.page.get(i).setSpace(true);
			}
			M_F_Brand.adapter.notifyDataSetChanged();
			tv.setText("反选");
		}

		M_F_Brand.ly = true;
		M_F_Brand.adapter.notifyDataSetChanged();

	}
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.ll_back:

			this.finish();

			break;

		case R.id.rl_store:

			this.child = 0;

			this.vp.setCurrentItem(0);
			
			this.CancelBrand();

			this.v_brand.setVisibility(View.INVISIBLE);
			this.tv_brand.setTextColor(Color.parseColor("#323232"));

			this.v_store.setVisibility(View.VISIBLE);
			this.tv_store.setTextColor(Color.parseColor("#FF9900"));

			break;

		case R.id.rl_brand:

			this.child = 1;

			this.vp.setCurrentItem(1);
			
			this.CancelStore();

			this.v_brand.setVisibility(View.VISIBLE);
			this.tv_brand.setTextColor(Color.parseColor("#FF9900"));

			this.v_store.setVisibility(View.INVISIBLE);
			this.tv_store.setTextColor(Color.parseColor("#323232"));

			break;

		case R.id.ll_editor:

			ll_back.setVisibility(View.GONE);
			ll_cancel.setVisibility(View.VISIBLE);

			if (child == 0) {
				CheckStore();
			} else {
				CheckBrand();
			}

			break;

		case R.id.ll_cancel:

			CancelStore();
			CancelBrand();

			break;

		case R.id.tv_cancel:
			
			CancelCollection(child);
			
			break;
		}
	}
	
	private void CancelCollection(final int space) {
		
		String id = "";
		
		switch (space) {
		
		case 0:
			
			for (int i = 0; i < M_F_Store.page.size(); i++) {
				
				if (M_F_Store.page.get(i).isSpace()) {
					
					if (id.equals("")) {
						id = M_F_Store.page.get(i).getId();
					} else {
						id += "," + M_F_Store.page.get(i).getId();
					}
					
				}
				
			}
			
			break;

		case 1:
			
			for (int i = 0; i < M_F_Brand.page.size(); i++) {
				
				if (M_F_Brand.page.get(i).isSpace()) {
					
					if (id.equals("")) {
						id = M_F_Brand.page.get(i).getId();
					} else {
						id += "," + M_F_Brand.page.get(i).getId();
					}
				}
			}
			
			break;
			
		}
		
		if (!id.equals("")) {
			Cnacel(space, id);
		} else {
			if (space == 0) {
				Toast.makeText(MyAttention.this, "您还没有选择任何一个店铺", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MyAttention.this, "您还没有选择任何一个品牌", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	private void Cnacel(final int space,String _id) {
		CustomDialogUtils.startCustomProgressDialog(MyAttention.this, "请稍候");
		Map<String, String> map = new HashMap<>();
		map.put("OPT", "331");
		map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
		map.put("collect_id", _id);
		map.put("collect_type", "3");
		new HttpClientGet(MyAttention.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				CustomDialogUtils.stopCustomProgressDialog(MyAttention.this);
				
				ll_back.setVisibility(View.VISIBLE);
				ll_cancel.setVisibility(View.GONE);
				tv_cancel.setVisibility(View.GONE);
				
				tv.setText("管理");
				
				if (space == 0) {
					M_F_Store.ly = false;
					M_F_Store.pageNum = 1;
					M_F_Store.getM_F_Data(0);
				} else {
					M_F_Brand.ly = false;
					M_F_Brand.pageNum = 1;
					M_F_Brand.getM_B_Data(0);
				}
			}
			
			@Override
			public void onFailure(MyError myError) {
				CustomDialogUtils.stopCustomProgressDialog(MyAttention.this);
				super.onFailure(myError);
			}
		});
	}
}
