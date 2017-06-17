package com.lsjr.zizisteward.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.BaseViewHolder;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.newview.MyGridView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OriginActivity extends BaseActivity {

	private MyGridView mGridview;

	@Override
	public int getContainerView() {
		return R.layout.activity_origin;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitle("请选择出发地");
		mGridview = (MyGridView) findViewById(R.id.gridview);
		init();
	}

	private void init() {
		OriginAdapter adapter = new OriginAdapter();
		mGridview.setAdapter(adapter);
	}

	private String[] names = { "武汉", "上海", "北京", "杭州", "深圳", "广州", "东莞", "宜昌", "厦门", "大理", "昆明", "重庆", "成都", "天津", "郑州",
			"沈阳", "长沙", "石家庄", "济南", "宁夏" };

	public class OriginAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return names.length;
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
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_origin, null);

			}
			TextView tv_name = BaseViewHolder.get(convertView, R.id.tv_name);
			tv_name.setText(names[position]);
			mGridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					CharSequence name = ((TextView) mGridview.getChildAt(position).findViewById(R.id.tv_name))
							.getText();
					String name2 = name.toString();
					Intent intent = getIntent();
					intent.putExtra("name", name2);
					OriginActivity.this.setResult(2, intent);
					OriginActivity.this.finish();

				}
			});

			return convertView;
		}

	}

}
