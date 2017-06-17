package com.lsjr.zizisteward.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hyphenate.chatuidemo.DemoHelper;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.utils.BitmapUtils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SelectPicActivity extends Activity implements OnClickListener {

	/***
	 * 使用照相机拍照获取图片
	 */
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	/***
	 * 使用相册中的图片
	 */
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;

	/***
	 * 从Intent获取图片路径的KEY
	 */
	public static final String KEY_PHOTO_PATH = "photo_path";

	private static final String TAG = "SelectPicActivity";

	private LinearLayout dialogLayout;
	private Button takePhotoBtn, pickPhotoBtn, cancelBtn;

	/** 获取到的图片路径 */
	private String picPath;

	private Intent lastIntent;

	private Uri photoUri;
	private Cursor cursor;
	private Uri fileUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.select_pic_layout);
		initView();
	}

	/**
	 * 初始化加载View
	 */
	private void initView() {
		dialogLayout = (LinearLayout) findViewById(R.id.dialog_layout);
		dialogLayout.setOnClickListener(this);
		takePhotoBtn = (Button) findViewById(R.id.btn_take_photo);// 拍照
		takePhotoBtn.setOnClickListener(this);
		pickPhotoBtn = (Button) findViewById(R.id.btn_pick_photo);// 本地上传
		pickPhotoBtn.setOnClickListener(this);
		cancelBtn = (Button) findViewById(R.id.btn_cancel);
		cancelBtn.setOnClickListener(this);

		lastIntent = getIntent();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_layout:
			finish();
			break;
		case R.id.btn_take_photo:
			takePhoto();
			finish();
			break;
		case R.id.btn_pick_photo:
			pickPhoto();
			finish();
			break;
		default:
			finish();
			break;
		}
	}

	/**
	 * 拍照获取图片
	 */
	private void takePhoto() {
		// // 执行拍照前，应该先判断SD卡是否存在
		// String SDState = Environment.getExternalStorageState();
		// if (SDState.equals(Environment.MEDIA_MOUNTED)) {
		//
		// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// // "android.media.action.IMAGE_CAPTURE"
		//
		// ContentValues values = new ContentValues();
		// photoUri =
		// this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
		// values);
		// intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
		// /** ----------------- */
		// startActivityForResult(intent, 1);// 拍照获取图片
		// } else {
		// Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
		// }

		// 先验证手机是否有sdcard
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				File dir = new File(Environment.getExternalStorageDirectory() + "/img");// 新建文件夹
				if (!dir.exists())
					dir.mkdirs();
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());// 当前时间作为文件名称
				File file = new File(dir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

				fileUri = Uri.fromFile(file);

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
				startActivityForResult(intent, 1);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(SelectPicActivity.this, "没有找到储存目录", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(SelectPicActivity.this, "没有储存卡", Toast.LENGTH_LONG).show();
		}
	}

	/***
	 * 从相册中取图片
	 */
	private void pickPhoto() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, 2);

		/**
		 * Intent pickIntent = new Intent(Intent.ACTION_PICK,null);
		 * pickIntent.setDataAndType(MediaStore.Images.Media.
		 * EXTERNAL_CONTENT_URI, "image/*"); startActivityForResult(pickIntent,
		 * 2);
		 */
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		/**
		 * switch (requestCode) { case 1:
		 * 
		 * if (data == null || data.getData() == null) { return; }
		 * 
		 * startPhotoZoom(data.getData());
		 * 
		 * break;
		 * 
		 * case 2:
		 * 
		 * if (data != null) { setPicToView(requestCode,data); }
		 * 
		 * break; }
		 */

		super.onActivityResult(requestCode, resultCode, data);

		Uri uri = data.getData();
		Bitmap bmp = null;
		ContentResolver cr = this.getContentResolver();
		try {
			if (bmp != null)// 如果不释放的话，不断取图片，将会内存不够
				bmp.recycle();
			bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		DemoHelper.getInstance().getUserProfileManager().uploadUserAvatar(Bitmap2Bytes(bmp));
		doPhoto(requestCode, data);
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", true);
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, 2);
	}

	/**
	 * save the picture data
	 * 
	 * @param picdata
	 */
	private void setPicToView(int requestCode, Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(getResources(), photo);
			DemoHelper.getInstance().getUserProfileManager().uploadUserAvatar(Bitmap2Bytes(photo));
			doPhoto(requestCode, picdata);
		}
	}

	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 选择图片后，获取图片的路径
	 *
	 * @param requestCode
	 * @param data
	 */
	@SuppressWarnings("deprecation")
	private void doPhoto(int requestCode, Intent data) {
		String[] pojo = { MediaStore.Images.Media.DATA };
		if (requestCode == 2) {// 从相册取图片，有些手机有异常情况，请注意

			if (data == null) {
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
			photoUri = data.getData();
			if (photoUri == null) {
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
			cursor = managedQuery(photoUri, pojo, null, null, null);
		}
		if (requestCode == 1) {// 拍照

			cursor = managedQuery(fileUri, pojo, null, null, null);
		}

		if (cursor != null) {
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			BitmapUtils.compressImageUpload(picPath);
			cursor.close();
			if (Integer.parseInt(Build.VERSION.SDK) < 14) {
				cursor.close();
			}
		}

		Log.i(TAG, "imagePath = " + picPath);
		if (picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG") || picPath.endsWith(".jpg")
				|| picPath.endsWith(".JPG") || picPath.endsWith(".JPEG") || picPath.endsWith(".jpeg"))) {
			lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
			setResult(Activity.RESULT_OK, lastIntent);
			finish();
		} else {
			Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
		}
	}
}
