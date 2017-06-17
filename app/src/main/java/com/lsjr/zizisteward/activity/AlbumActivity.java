package com.lsjr.zizisteward.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.AlbumGridViewAdapter;
import com.lsjr.zizisteward.adapter.CropBitmap;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.AlbumHelper;
import com.lsjr.zizisteward.bean.ImageBucket;
import com.lsjr.zizisteward.bean.ImageItem;
import com.lsjr.zizisteward.utils.Bimp;
import com.lsjr.zizisteward.utils.FileUtils_new;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * 这个是进入相册显示所有图片的界面
 * @author panjun
 *
 * 创建日期2015年12月24日 上午11:54:14
 */
@SuppressLint({ "ShowToast", "HandlerLeak" })
public class AlbumActivity extends Activity implements OnClickListener {
	// 显示手机里的所有图片的列表控件
	private GridView gridView;
	// 当手机里没有图片时，提示用户没有图片的控件
	private TextView tv;
	// gridView的adapter
	private AlbumGridViewAdapter gridImageAdapter;
	// 完成按钮
	private TextView okButton;
	private Intent intent;
	private ArrayList<ImageItem> dataList;
	private AlbumHelper helper;
	public static List<ImageBucket> contentList;
	public String mIntentTo = "";
	// 存当前选中和取消选中的下标
	public Set<Integer> mCheckPostion = new HashSet<Integer>();
	public Set<Integer> mUnCheckPostion = new HashSet<Integer>();
	/**
	 * 标题信息
	 */
	private LinearLayout layout_title_back;
	private TextView layout_back_titles;
	private TextView layout_title_tv, layout_title_righttv;
	private ImageView layout_title_catears;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.plugin_camera_album);
		intent = new Intent();

		// 注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
		IntentFilter filter = new IntentFilter("data.broadcast.action");
		registerReceiver(broadcastReceiver, filter);
		init();
		// 这个函数主要用来控制预览和完成按钮的状态
		isShowOkBt();
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			gridImageAdapter.notifyDataSetChanged();
		}
	};

	// 初始化，给一些对象赋值
	private void init() {

		mIntentTo = getIntent().getStringExtra("mIntentTo");

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		contentList = helper.getImagesBucketList(false);
		dataList = new ArrayList<ImageItem>();
		for (int i = 0; i < contentList.size(); i++) {
			dataList.addAll(contentList.get(i).imageList);
		}

		layout_title_back = (LinearLayout) findViewById(R.id.layout_title_back);
		layout_title_back.setOnClickListener(this);

		layout_back_titles = (TextView) findViewById(R.id.layout_back_titles);
		layout_back_titles.setText("选择相册");

		layout_title_tv = (TextView) findViewById(R.id.layout_title_tv);
		layout_title_tv.setText("相册");

		layout_title_righttv = (TextView) findViewById(R.id.layout_title_righttv);
		layout_title_righttv.setOnClickListener(this);
		layout_title_righttv.setText("取消");

		if (mIntentTo.equals("图像上传")) {
			layout_title_back.setVisibility(View.GONE);
		} else {
			layout_title_back.setVisibility(View.VISIBLE);
		}


		tv = (TextView) findViewById(R.id.myText);

		gridView = (GridView) findViewById(R.id.myGrid);
		gridImageAdapter = new AlbumGridViewAdapter(this);
		gridImageAdapter.clear();
		gridImageAdapter.addAll(dataList);
		gridImageAdapter.AddselectedDataList(Bimp.tempSelectBitmap);
		gridImageAdapter.setOnItemClickListener(itemListener);
		gridView.setAdapter(gridImageAdapter);
		gridView.setEmptyView(tv);

		okButton = (TextView) findViewById(R.id.ok_button);
		okButton.setOnClickListener(this);
		okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size() + "/" + Bimp.num + ")");
	}

	private boolean removeOneData(ImageItem imageItem) {
		if (Bimp.tempSelectBitmap.contains(imageItem)) {
			Bimp.tempSelectBitmap.remove(imageItem);
			okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size() + "/" + Bimp.num + ")");
			return true;
		}
		return false;
	}

	public void isShowOkBt() {
		if (Bimp.tempSelectBitmap.size() > 0) {
			okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size() + "/" + Bimp.num + ")");
		} else {
			okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size() + "/" + Bimp.num + ")");
		}
	}

	@Override
	protected void onRestart() {
		if (gridImageAdapter != null) {
			gridImageAdapter.notifyDataSetChanged();
		}
		isShowOkBt();
		super.onRestart();
	}

	protected void onDestroy() {
		super.onDestroy();
		if (broadcastReceiver != null)
			unregisterReceiver(broadcastReceiver);
	};

	AlbumGridViewAdapter.OnItemClickListener itemListener = new AlbumGridViewAdapter.OnItemClickListener() {

		@Override
		public void onItemClick(final ToggleButton toggleButton, int position, boolean isChecked, TextView chooseBt) {
			if (Bimp.tempSelectBitmap.size() >= Bimp.num) {
				toggleButton.setChecked(false);
				chooseBt.setVisibility(View.GONE);
				if (!removeOneData(dataList.get(position))) {
					Toast.makeText(AlbumActivity.this, "超出可选图片张数", 0).show();
				}
				return;
			}
			if (isChecked) {
				chooseBt.setVisibility(View.VISIBLE);
				Bimp.tempSelectBitmap.add(dataList.get(position));
				// 记录当前选中的下标
				mCheckPostion.add(position);
				okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size() + "/" + Bimp.num + ")");
			} else {
				chooseBt.setVisibility(View.GONE);
				Bimp.tempSelectBitmap.remove(dataList.get(position));
				// 记录当前取消选中的下标
				mUnCheckPostion.add(position);
				okButton.setText("完成" + "(" + Bimp.tempSelectBitmap.size() + "/" + Bimp.num + ")");
			}
			isShowOkBt();
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.ok_button:
			if (mIntentTo == null || mIntentTo.equals("")) {
				finish();
			} else if (mIntentTo.equals("意见反馈") || mIntentTo.equals("发送界面")) {
				finish();
			} else if (mIntentTo.equals("喵友圈")) {
			} else if (mIntentTo.equals("图像上传")) {
				// 进行图片裁剪
				String path = Bimp.tempSelectBitmap.get(0).getImagePath();
				File file = new File(path);
				if (file.isFile()) {
					Uri mImgUri = Uri.fromFile(new File(path));
					CropBitmap mCrop = CropBitmap.getIntances();
					mCrop.doCrop(AlbumActivity.this, mImgUri, 1);
				}
			}
			break;
		case R.id.layout_title_back:
			intent.setClass(AlbumActivity.this, ImageFileActivity.class);
			startActivity(intent);
			break;
		case R.id.layout_title_righttv:
			// 取消操作,移除或添加当前用户未选中和选中的图片
			if (mCheckPostion.size() > 0) {
				for (Integer Inl : mCheckPostion) {
					Bimp.tempSelectBitmap.remove(dataList.get(Inl));
				}
			}
			if (mUnCheckPostion.size() > 0) {
				for (Integer Inl : mUnCheckPostion) {
					Bimp.tempSelectBitmap.add(dataList.get(Inl));
				}
			}
			finish();
			mCheckPostion.clear();
			mUnCheckPostion.clear();
			break;
		}
	}

	private Handler mHeadHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x9) {
				setResult(PopWindowCatFriend.PHOTOZOOM_TOUXIANG);
				finish();
			}
		};
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CropBitmap.CROP_FROM_ALBUM:
			if (null != data && resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				if (null != bundle) {
					Bitmap mBitmap = bundle.getParcelable("data");
					String filname = String.valueOf(System.currentTimeMillis());
					FileUtils_new.saveBitmap(mBitmap, filname, AlbumActivity.this);
					// 1.先移除集合里面的原图。
					if (Bimp.tempSelectBitmap.size() > 0) {
						for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
							Bimp.tempSelectBitmap.remove(i);
						}
					}
					// 2.在把裁剪过后的图片保存在集合中,以便后面的上传。
					ImageItem takePhoto = new ImageItem();
					takePhoto.setBitmap(mBitmap);
					String path = FileUtils_new.sDPATH + filname + ".JPEG";
					takePhoto.setImagePath(path);
					Bimp.tempSelectBitmap.add(takePhoto);
					// 3.通知界面可以返回上一层了。
					mHeadHandler.sendEmptyMessage(0x9);
				}
			}
			break;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (mCheckPostion.size() > 0) {
				for (Integer Inl : mCheckPostion) {
					Bimp.tempSelectBitmap.remove(dataList.get(Inl));
				}
			}
			if (mUnCheckPostion.size() > 0) {
				for (Integer Inl : mUnCheckPostion) {
					Bimp.tempSelectBitmap.add(dataList.get(Inl));
				}
			}
			finish();
			mCheckPostion.clear();
			mUnCheckPostion.clear();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}