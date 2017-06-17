package com.lsjr.zizisteward.ly.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lsjr.zizisteward.R;

/** 个人头像设置 */
public class PersonImageSetActivity extends Activity implements OnClickListener {
	private TextView tv_camera;
	private TextView tv_cancel;
	private TextView tv_photo_album;
	private RelativeLayout rl_parent;

	private Uri uri;
	private String path;
	private File tempFile;
	private String PHOTO_FILE_NAME = "ly.jpg";

	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.person_image_set_activity);
		this.findViewById();
	}

	private void findViewById() {
		this.tv_camera = (TextView) super.findViewById(R.id.tv_camera);
		this.tv_cancel = (TextView) super.findViewById(R.id.tv_cancel);
		this.rl_parent = (RelativeLayout) super.findViewById(R.id.rl_parent);
		this.tv_photo_album = (TextView) super
				.findViewById(R.id.tv_photo_album);

		this.tv_cancel.setOnClickListener(this);
		this.rl_parent.setOnClickListener(this);
		this.tv_camera.setOnClickListener(this);
		this.tv_photo_album.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_cancel:
			this.rl_parent.setVisibility(View.GONE);
			this.finish();
			break;

		case R.id.rl_parent:
			this.rl_parent.setVisibility(View.GONE);
			this.finish();
			break;

		case R.id.tv_camera:
			// 相机
			Camera();
			break;

		case R.id.tv_photo_album:
			// 相册
			Album();
			break;
		}
	}

	private void Camera() {
		// 激活相机
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
		startActivityForResult(intent, 2);
	}

	private void Album() {
		// 激活系统图库，选择一张图片
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
		startActivityForResult(intent, 1);
	}

	private void crop(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 350);
		intent.putExtra("aspectY", 350);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 350);
		intent.putExtra("outputY", 350);

		intent.putExtra("outputFormat", "JPEG");// 图片格式
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);

		startActivityForResult(intent, 3);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case 1:
			if (data != null) {
				// 得到图片的全路径
				Uri uri = data.getData();
				crop(uri);
			}
			break;

		case 2:
			if (data != null) {
				
				File f = new File(Environment.getExternalStorageDirectory(), "ly");
				if (!f.exists()) {
					f.mkdir();
				}
				String fileName = "temporary" + ".jpg";
				
				File file = new File(f, fileName);
				
				Bundle bundle = data.getExtras();
				Bitmap bm = (Bitmap) bundle.get("data");
				FileOutputStream b = null; 
				try {
					b = new FileOutputStream(file.getPath());
					bm.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件  
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						b.flush();
						b.close(); 
					} catch (IOException e) {
						e.printStackTrace();
					}  
				}
				
				if (file.exists()) {
					crop(Uri.fromFile(file));
				}
			}

			break;

		case 3:
			// 从剪切图片返回的数据
			if (data != null) {
				PersonalInformationActivity.bitmap = data
						.getParcelableExtra("data");
				if (null != PersonalInformationActivity.bitmap) {
					saveBitmap(PersonalInformationActivity.bitmap);
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void saveBitmap(Bitmap bm) {
		File f = new File(Environment.getExternalStorageDirectory(), "ly");
		if (!f.exists()) {
			f.mkdir();
		}
		
		String fileName = "new_temporary" + ".jpg";
		File file = new File(f, fileName);
		
		PersonalInformationActivity.uri = file.getPath();
		
		try {
			FileOutputStream out = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		setResult(1);
		this.rl_parent.setVisibility(View.GONE);
		this.finish();
	}
}
