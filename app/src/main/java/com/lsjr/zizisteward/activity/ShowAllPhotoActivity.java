package com.lsjr.zizisteward.activity;

import java.util.ArrayList;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.AlbumGridViewAdapter;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.ImageItem;
import com.lsjr.zizisteward.utils.Bimp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * �������ʾһ���ļ������������ͼƬʱ�Ľ���
 * @author panjun
 *
 * ��������2015��12��24�� ����11:55:15
 */
public class ShowAllPhotoActivity extends Activity implements OnClickListener {
	private GridView gridView;
	private AlbumGridViewAdapter gridImageAdapter;
	// ��ɰ�ť
	private TextView okButton;
	// Ԥ����ť
	public static ArrayList<ImageItem> dataList = new ArrayList<ImageItem>();

	/**
	 * ������Ϣ
	 */
	private LinearLayout layout_title_back;
	private TextView layout_title_tv, layout_title_righttv;
	private ImageView layout_title_catears;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.plugin_camera_show_all_photo);

		IntentFilter filter = new IntentFilter("data.broadcast.action");
		registerReceiver(broadcastReceiver, filter);

		init();
		isShowOkBt();
	}

	private void init() {

		layout_title_back = (LinearLayout) findViewById(R.id.layout_title_back);
		layout_title_back.setOnClickListener(this);

		layout_title_tv = (TextView) findViewById(R.id.layout_title_tv);

		layout_title_righttv = (TextView) findViewById(R.id.layout_title_righttv);
		layout_title_righttv.setVisibility(View.GONE);

		okButton = (TextView) findViewById(R.id.showallphoto_ok_button);
		okButton.setOnClickListener(this);

		String folderName = getIntent().getStringExtra("folderName");
		if (folderName.length() > 8) {
			folderName = folderName.substring(0, 9) + "...";
		}
		layout_title_tv.setText(folderName);

		gridView = (GridView) findViewById(R.id.showallphoto_myGrid);
		gridImageAdapter = new AlbumGridViewAdapter(this);
		gridImageAdapter.clear();
		gridImageAdapter.addAll(dataList);
		gridImageAdapter.AddselectedDataList(Bimp.tempSelectBitmap);
		gridImageAdapter.setOnItemClickListener(itemListener);
		gridView.setAdapter(gridImageAdapter);

	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			gridImageAdapter.notifyDataSetChanged();
		}
	};

	public void isShowOkBt() {
		if (Bimp.tempSelectBitmap.size() > 0) {
			okButton.setText("���" + "(" + Bimp.tempSelectBitmap.size() + "/" + Bimp.num + ")");
		} else {
			okButton.setText("���" + "(" + Bimp.tempSelectBitmap.size() + "/" + Bimp.num + ")");
		}
	}

	AlbumGridViewAdapter.OnItemClickListener itemListener = new AlbumGridViewAdapter.OnItemClickListener() {

		@Override
		public void onItemClick(final ToggleButton toggleButton, int position, boolean isChecked, TextView button) {

			if (Bimp.tempSelectBitmap.size() >= Bimp.num && isChecked) {
				button.setVisibility(View.GONE);
				toggleButton.setChecked(false);
				Toast.makeText(ShowAllPhotoActivity.this, "������ѡͼƬ����", 0).show();
				return;
			}

			if (isChecked) {
				button.setVisibility(View.VISIBLE);
				Bimp.tempSelectBitmap.add(dataList.get(position));
				okButton.setText("���" + "(" + Bimp.tempSelectBitmap.size() + "/" + Bimp.num + ")");
			} else {
				button.setVisibility(View.GONE);
				Bimp.tempSelectBitmap.remove(dataList.get(position));
				okButton.setText("���" + "(" + Bimp.tempSelectBitmap.size() + "/" + Bimp.num + ")");
			}
			isShowOkBt();
		}

	};

	@Override
	protected void onRestart() {
		isShowOkBt();
		super.onRestart();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_title_back:
			finish();
			break;
		case R.id.showallphoto_ok_button:
			finish();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}
}
