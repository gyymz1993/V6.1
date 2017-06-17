package com.lsjr.zizisteward.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.utils.Bimp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
public class PopWindowCatFriend extends PopupWindow implements OnClickListener {
	private View mViewOne;
	private Context mContext;
	public LayoutInflater mInflater;

	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 相册
	public static int SELECT_NUM = 0;// 需要选择多少张

	public static final int PHOTOHRAPH_TOUXIANG = 3;// 头像拍照
	public static final int PHOTOZOOM_TOUXIANG = 4;// 头像相册

	private String mIntentTo = "";
	// 拍照
	public TextView popcatfriendisone_pictures;
	// 相册
	public TextView popcatfriendisone_pictureschoose;
	// 取消
	public TextView popcatfriendisone_cancel;

	// 说明文字
	public TextView popcatfriendisgtone_tvtip;
	// 数据集合
	public ListView popcatfriendisgtone_listview;
	// 取消按钮
	public TextView popcatfriendisgtone_cancel;
	private String localTempImgDir = "ly";

	public PopWindowCatFriend(Context context, String intentTo) {
		mContext = context;
		mIntentTo = intentTo;
		mInflater = LayoutInflater.from(context);
	}

	public void setView() {
		mViewOne = mInflater.inflate(R.layout.popwindowcatfriend_isone, null);
		popcatfriendisone_pictures = (TextView) mViewOne.findViewById(R.id.popcatfriendisone_pictures);
		popcatfriendisone_pictures.setOnClickListener(this);

		popcatfriendisone_pictureschoose = (TextView) mViewOne.findViewById(R.id.popcatfriendisone_pictureschoose);
		popcatfriendisone_pictureschoose.setOnClickListener(this);

		popcatfriendisone_cancel = (TextView) mViewOne.findViewById(R.id.popcatfriendisone_cancel);
		popcatfriendisone_cancel.setOnClickListener(this);

		setBackGround(mViewOne);
	}

	@SuppressWarnings("deprecation")
	public void setBackGround(View mView) {
		this.setBackgroundDrawable(new BitmapDrawable());
		this.setFocusable(true); // 设置PopupWindow可获得焦点
		this.setTouchable(true); // 设置PopupWindow可触摸
		this.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
		this.setContentView(mView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setAnimationStyle(R.style.PopupAnimation);

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mView.setOnTouchListener(mTouchListener);
	}

	OnTouchListener mTouchListener = new OnTouchListener() {

		public boolean onTouch(View v, MotionEvent event) {

			int height = v.findViewById(R.id.pop_layout).getTop();
			int y = (int) event.getY();
			if (event.getAction() == MotionEvent.ACTION_UP) {
				if (y < height) {
					dismiss();
				}
			}
			return true;
		}
	};
	private String localTempImgFileName;
	private final static int CAMERA = 1;

	public void OpenCapture() {

		// 相机
		// String status = Environment.getExternalStorageState();
		// if (status.equals(Environment.MEDIA_MOUNTED)) {
		//
		// File dir = new File(Environment.getExternalStorageDirectory() + "/" +
		// localTempImgDir);
		// if (!dir.exists()) {
		// dir.mkdirs();
		// }
		//
		// Intent intent = new
		// Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		// localTempImgFileName = SendCircleFriendsActivity.p_name + ".jpg";
		// File f = new File(dir, localTempImgFileName);
		// Uri u = Uri.fromFile(f);
		// intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
		// startActivityForResult(intent, CAMERA);
		// }

		// if (!checkCameraDevice(mContext)) {
		// Toast.makeText(mContext, "抱歉,您摄像头功能不可用!", 0).show();
		// return;
		// }
		// if (!hardwareSupportCheck(mContext)) {
		// Toast.makeText(mContext, "请检查是否授权应用相机的权限！", 0).show();
		// return;
		// }
		// try {
		// // SendCircleFriendsActivity.fileName =
		// String.valueOf(System.currentTimeMillis());
		// // SendCircleFriendsActivity.path = FileUtils_new.SDPATH +
		// // SendCircleFriendsActivity.fileName + ".jpg";
		// // 激活相机
		// /**
		// * Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// * // 判断存储卡是否可以用，可用进行存储 File tempFile = new
		// * File(SendCircleFriendsActivity.path);
		// * System.out.println(SendCircleFriendsActivity.path);
		// * SendCircleFriendsActivity.uri = Uri.fromFile(tempFile);
		// * intent.putExtra(MediaStore.EXTRA_OUTPUT,
		// * SendCircleFriendsActivity.uri); //
		// * 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
		// */
		//
		// Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		//
		// ((Activity) mContext).startActivityForResult(intent, PHOTOHRAPH);
		// } catch (ActivityNotFoundException e) {
		// Toast.makeText(mContext, "抱歉,您摄像头功能不可用!", 0).show();
		// }
	}

	/** * 检测Android设备是否支持摄像机 */
	public static boolean checkCameraDevice(Context context) {
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断相机是否开启了
	 */
	public static boolean hardwareSupportCheck(Context context) {
		// Camera needs to open
		Camera c = null;
		try {
			c = Camera.open();
		} catch (RuntimeException e) {
			// throw new RuntimeException();
		}
		if (c == null) {
			return false;
		} else {
			c.release();
		}
		return true;
	}

	/**
	 * 打开系统相册
	 */
	public void OpenPickPicture() {
		Intent intent = new Intent(mContext, AlbumActivity.class);
		intent.putExtra("mIntentTo", mIntentTo);
		if (mIntentTo.equals("图像上传")) {
			Bimp.num = 1;
			((Activity) mContext).startActivityForResult(intent, PHOTOZOOM_TOUXIANG);
		} else {
			Bimp.num = SELECT_NUM;
			mContext.startActivity(intent);
		}
		dismiss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 系统相机
		case R.id.popcatfriendisone_pictures:
			dismiss();
			OpenCapture();
			break;

		// 相册
		case R.id.popcatfriendisone_pictureschoose:
			dismiss();
			OpenPickPicture();
			break;

		case R.id.popcatfriendisone_cancel:
			dismiss();
			break;
		}
	}
}
