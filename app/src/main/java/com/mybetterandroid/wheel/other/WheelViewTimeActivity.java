package com.mybetterandroid.wheel.other;

import com.lsjr.zizisteward.R;
import com.mybetterandroid.wheel.widget.TosAdapterView;
import com.mybetterandroid.wheel.widget.TosGallery;
import com.mybetterandroid.wheel.widget.WheelView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
//省市区  
public class WheelViewTimeActivity extends MyAddBaseActivity {
	private WheelView mViewProvince = null;
	private WheelView mViewCity = null;
	private WheelView mViewDistrict = null;

	private Button sel_password = null;
	private View mDecorView = null;

	private NumberAdapter provinceAdapter;
	private NumberAdapter cityAdapter;
	private NumberAdapter districtAdapter;

	private TosAdapterView.OnItemSelectedListener mListener = new TosAdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(TosAdapterView<?> parent, View view, int position, long id) {

			// int index = Integer.parseInt(view.getTag().toString());
			// int count = parent.getChildCount();
			// if(index < count-1){
			// ((WheelTextView)parent.getChildAt(index+1)).setTextSize(25);
			// }
			// if(index>0){
			// ((WheelTextView)parent.getChildAt(index-1)).setTextSize(25);
			// }
			// formatData();
			// for (int i = 0; i < parent.getChildCount(); i++) {
			// ((WheelTextView)parent.getChildAt(i)).setTextColor(Color.parseColor("#000000"));
			// }
			// ((WheelTextView)view).setTextColor(Color.parseColor("#fff660"));
			// ((WheelTextView)view).setTextSize(20);
			if (parent == mViewProvince) {
				updateCities();
			} else if (parent == mViewCity) {
				updateAreas();
			} else if (parent == mViewDistrict) {
				mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[position];
				mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
			}
		}

		@Override
		public void onNothingSelected(TosAdapterView<?> parent) {
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.wheel_time);
		initProvinceDatas();
		sel_password = (Button) findViewById(R.id.sel_password);
		sel_password.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(),
				// mCurrentProviceName+mCurrentCityName+mCurrentDistrictName,
				// Toast.LENGTH_SHORT).show();
				Intent intent = getIntent();
				intent.putExtra("address", mCurrentProviceName + mCurrentCityName + mCurrentDistrictName);
				intent.putExtra("province", mCurrentProviceName);
				intent.putExtra("city", mCurrentCityName);
				intent.putExtra("area", mCurrentDistrictName);
				intent.putExtra("zipCode", mCurrentZipCode);
				WheelViewTimeActivity.this.setResult(77, intent);
				WheelViewTimeActivity.this.finish();
			}
		});

		mViewProvince = (WheelView) findViewById(R.id.wheel1);
		mViewCity = (WheelView) findViewById(R.id.wheel2);
		mViewDistrict = (WheelView) findViewById(R.id.wheel3);

		mViewProvince.setScrollCycle(true);
		mViewCity.setScrollCycle(true);

		provinceAdapter = new NumberAdapter(mProvinceDatas);
		String[] citys = mCitisDatasMap.get(mProvinceDatas[0]);
		cityAdapter = new NumberAdapter(citys);
		String[] district = mDistrictDatasMap.get(citys[0]);
		districtAdapter = new NumberAdapter(district);

		mViewProvince.setAdapter(provinceAdapter);
		mViewCity.setAdapter(cityAdapter);
		mViewDistrict.setAdapter(districtAdapter);

		mViewProvince.setSelection(0, true);
		mViewCity.setSelection(0, true);
		mViewDistrict.setSelection(0, true);

		// View view = mViewProvince.getSelectedView();
		// ((WheelTextView)view).setTextColor(Color.parseColor("#fff660"));
		// ((WheelTextView)mViewProvince.getSelectedView()).setTextSize(30);
		// ((WheelTextView)mMins.getSelectedView()).setTextSize(30);
		mViewProvince.setOnItemSelectedListener(mListener);
		mViewCity.setOnItemSelectedListener(mListener);
		mViewDistrict.setOnItemSelectedListener(mListener);

		mViewProvince.setUnselectedAlpha(0.5f);
		mViewCity.setUnselectedAlpha(0.5f);
		mViewDistrict.setUnselectedAlpha(0.5f);

		mDecorView = getWindow().getDecorView();

	}

	/**
	 * 鏍规嵁褰撳墠鐨勭渷锛屾洿鏂板競WheelView鐨勪俊鎭�
	 */
	// int pCurrent = 0;
	private void updateCities() {
		int pCurrent = mViewProvince.getSelectedItemPosition();
		// this.pCurrent = pCurrent;
		// if(mCurrentProviceName!=null && mProvinceDatas.length >= pCurrent) {
		// System.out.println(pCurrent+"--------------pCurrent------------");
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		cityAdapter.setData(mCitisDatasMap.get(mProvinceDatas[pCurrent]));
		// cityAdapter.notifyDataSetChanged();
		mViewCity.setAdapter(cityAdapter);

		// mViewCity.setSelection(0);
		updateAreas();
		// }
	}

	/**
	 * 鏍规嵁褰撳墠鐨勫競锛屾洿鏂板尯WheelView鐨勪俊鎭�
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getSelectedItemPosition();
		// System.out.println(pCurrent+"--------------pCurrent------------");
		if (mCitisDatasMap.get(mCurrentProviceName).length >= pCurrent) {
			mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
			String[] areas = mDistrictDatasMap.get(mCurrentCityName);
			if (areas == null) {
				areas = new String[] { "" };
			}
			mCurrentDistrictName = areas[0];
			districtAdapter.setData(areas);
			// districtAdapter.notifyDataSetChanged();
			mViewDistrict.setAdapter(districtAdapter);
			// mViewDistrict.setSelection(0);
		}
	}

	private class NumberAdapter extends BaseAdapter {
		int mHeight = 50;
		String[] mData = null;

		public NumberAdapter(String[] data) {
			mHeight = (int) Utils.dipToPx(WheelViewTimeActivity.this, mHeight);
			this.mData = data;
		}

		@Override
		public int getCount() {
			// return (null != mData) ? mData.length : 0;
			return (mData == null) ? 0 : mData.length;
		}

		@Override
		public View getItem(int position) {
			return getView(position, null, null);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void setData(String[] mData) {
			this.mData = mData;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			WheelTextView textView = null;

			if (null == convertView) {
				convertView = new WheelTextView(WheelViewTimeActivity.this);
				convertView.setLayoutParams(new TosGallery.LayoutParams(-1, mHeight));
				textView = (WheelTextView) convertView;
				textView.setTextSize(20);
				textView.setTextColor(Color.BLACK);
				textView.setGravity(Gravity.CENTER);
			}
			if (position > mData.length) {
				position = mData.length;
			}
			String text = mData[position];
			if (null == textView) {
				textView = (WheelTextView) convertView;
			}
			textView.setText(text);
			return convertView;
		}
	}
}
