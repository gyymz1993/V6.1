package com.lsjr.zizisteward.adapter;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.Toast;

/**
 * ͷ��ü���
 * @author lufan  2016��3��3������2:07:21
 *
 */
public class CropBitmap {

	private static CropBitmap mCrop;
	public static final int CROP_FROM_ALBUM = 1;
	public static final int CROP_FROM_CAMERA = 2;

	public static CropBitmap getIntances() {
		if (mCrop == null) {
			mCrop = new CropBitmap();
		}
		return mCrop;
	}

	/**
	 * ���պ����ͷ��
	 */
	public void doCrop(Activity context, Uri imgUri, int isPickOrPhoto) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");
		List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 0);
		int size = list.size();
		if (size == 0) {
			Toast.makeText(context, "can't find crop app", Toast.LENGTH_SHORT).show();
			return;
		} else {
			intent.setData(imgUri);
			intent.putExtra("outputX", 300);
			intent.putExtra("outputY", 300);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", true);
			// only one
			if (size == 1) {
				Intent i = new Intent(intent);
				ResolveInfo res = list.get(0);
				i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
				if (isPickOrPhoto == 1) {
					context.startActivityForResult(i, CROP_FROM_ALBUM);
				} else if (isPickOrPhoto == 2) {
					context.startActivityForResult(i, CROP_FROM_CAMERA);
				}
			}
		}
	}
}

