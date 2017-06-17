package com.lsjr.zizisteward.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.BaseViewHolder;
import com.lsjr.zizisteward.bean.HotWordsBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.newview.MyGridView;
import com.lsjr.zizisteward.newview.MyListView;
import com.lsjr.zizisteward.utils.GsonUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class SearchActivity extends Activity implements OnClickListener {

	private TextView quxiao;
	private EditText et_input_key;
	private ImageView iv_search;
	private RecordSQLiteOpenHelper helper = new RecordSQLiteOpenHelper(this);
	private MyGridView gridview_hot;
	private MyListView gridview_zuijin;
	private BaseAdapter adapter2;
	private RelativeLayout mHistory_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		quxiao = (TextView) findViewById(R.id.quxiao);
		et_input_key = (EditText) findViewById(R.id.et_input_key);
		iv_search = (ImageView) findViewById(R.id.iv_search);
		gridview_hot = (MyGridView) findViewById(R.id.gridview_hot);
		gridview_zuijin = (MyListView) findViewById(R.id.gridview_zuijin);
		mTv_clean = (TextView) findViewById(R.id.tv_clean);
		mHistory_search = (RelativeLayout) findViewById(R.id.history_search);
		getData();
		et_input_key.setFocusable(true);
		et_input_key.setFocusableInTouchMode(true);
		et_input_key.requestFocus();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(et_input_key, 0);
			}
		}, 200);
		String trim = et_input_key.getText().toString().trim();
		mTv_clean.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteData();
				queryData("");
			}
		});
		// 搜索框的文本变化实时监听
		et_input_key.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				String tempName = et_input_key.getText().toString().trim();
				// 根据tempName去模糊查询数据库中有没有数据
				queryData(tempName);

			}
		});
		gridview_zuijin.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView textView = (TextView) view.findViewById(android.R.id.text1);
				textView.setTextSize(10);
				textView.setTextColor(Color.GRAY);
				String name = textView.getText().toString();
				et_input_key.setText(name);
				Intent intent = new Intent(SearchActivity.this, KeyWordSearchActivity.class);
				intent.putExtra("keyword", name);
				startActivity(intent);

			}
		});
		queryData("");
		init();

	}

	private String[] mBrands;
	private SQLiteDatabase db;
	private TextView mTv_clean;

	private void getData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("OPT", "36");
		new HttpClientGet(SearchActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("热们" + result);
				HotWordsBean bean = GsonUtil.getInstance().fromJson(result, HotWordsBean.class);
				mBrands = bean.getBrands();
				MyGridViewDetailAdapter adapter = new MyGridViewDetailAdapter();
				gridview_hot.setAdapter(adapter);

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		et_input_key.setFocusable(true);
		et_input_key.setFocusableInTouchMode(true);
		et_input_key.requestFocus();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(et_input_key, 0);
			}
		}, 100);

	}

	private void init() {
		quxiao.setOnClickListener(this);
		iv_search.setOnClickListener(this);
		et_input_key.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					search();
					return true;
				}
				return false;
			}
		});

	}

	protected void search() {
		String keyword = et_input_key.getText().toString().trim();
		if (TextUtils.isEmpty(keyword)) {
			Toast.makeText(SearchActivity.this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
			return;
		}
		boolean hasData = hasData(keyword);
		if (!hasData) {
			insertData(keyword);
			queryData("");
		}
		Intent intent = new Intent(SearchActivity.this, KeyWordSearchActivity.class);
		intent.putExtra("keyword", keyword);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quxiao:
			finish();
			break;
		case R.id.iv_search:
			String keyword = et_input_key.getText().toString().trim();
			if (TextUtils.isEmpty(keyword)) {
				Toast.makeText(SearchActivity.this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
				return;
			}
			boolean hasData = hasData(keyword);
			if (!hasData) {
				insertData(keyword);
				queryData("");
			}
			Intent intent = new Intent(SearchActivity.this, KeyWordSearchActivity.class);
			intent.putExtra("keyword", keyword);
			startActivity(intent);
			break;

		}
	}

	public class MyGridViewDetailAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mBrands == null ? 0 : mBrands.length;
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
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_search_hot, parent,
						false);
			}
			TextView tv_key = BaseViewHolder.get(convertView, R.id.tv_key);
			tv_key.setText(mBrands[position]);
			gridview_hot.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(SearchActivity.this, KeyWordSearchActivity.class);
					intent.putExtra("keyword", mBrands[position]);
					startActivity(intent);
				}
			});

			return convertView;
		}

	}

	/**
	 * 插入数据
	 */
	@SuppressWarnings("unused")
	private void insertData(String tempName) {
		db = helper.getWritableDatabase();
		db.execSQL("insert into records(name) values('" + tempName + "')");
		db.close();
	}

	/**
	 * 模糊查询数据
	 */
	@SuppressWarnings("unused")
	private void queryData(String tempName) {
		Cursor cursor = helper.getReadableDatabase().rawQuery(
				"select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
		// 创建adapter适配器对象
		adapter2 = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[] { "name" },
				new int[] { android.R.id.text1 }, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		// 设置适配器
		int count = cursor.getCount();
		if (count == 0) {
			mHistory_search.setVisibility(View.GONE);
		} else {
			mHistory_search.setVisibility(View.VISIBLE);
		}
		gridview_zuijin.setAdapter(adapter2);
		adapter2.notifyDataSetChanged();
	}

	/**
	 * 检查数据库中是否已经有该条记录
	 */
	@SuppressWarnings("unused")
	private boolean hasData(String tempName) {
		Cursor cursor = helper.getReadableDatabase().rawQuery("select id as _id,name from records where name =?",
				new String[] { tempName });
		// 判断是否有下一个
		return cursor.moveToNext();
	}

	/**
	 * 清空数据
	 */
	@SuppressWarnings("unused")
	private void deleteData() {
		db = helper.getWritableDatabase();
		db.execSQL("delete from records");
		db.close();
	}
}
