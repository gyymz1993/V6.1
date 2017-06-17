package com.lsjr.zizisteward.adapter;

import java.util.ArrayList;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.AlbumActivity;
import com.lsjr.zizisteward.activity.ShowAllPhotoActivity;
import com.lsjr.zizisteward.adapter.BitmapCache.ImageCallback;
import com.lsjr.zizisteward.bean.ImageBucket;
import com.lsjr.zizisteward.bean.ImageItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * �������ʾ���а�ͼƬ���ļ��е�������
 * @author panjun
 *
 * ��������2015��12��24�� ����11:56:22
 */
public class FolderAdapter extends CommonAdapter<ImageBucket> {
	private Context mContext;
	private DisplayMetrics dm;
	public BitmapCache cache;
	// ����
	public ImageView imageView;
	public ImageView choose_back;
	// �ļ������
	public TextView folderName;
	// �ļ��������ͼƬ����
	public TextView fileNum;

	public FolderAdapter(Context context) {
		super(context, R.layout.plugin_camera_select_folder);
		mContext = context;
		cache = new BitmapCache(context);
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
	}

	// Ϊÿһ���ļ��й����ļ�����
	private class ImageViewClickListener implements OnClickListener {
		private int position;
		private ImageView choose_back;

		public ImageViewClickListener(int position, ImageView choose_back) {
			this.position = position;
			this.choose_back = choose_back;
		}

		public void onClick(View v) {
			ShowAllPhotoActivity.dataList = (ArrayList<ImageItem>) AlbumActivity.contentList.get(position).imageList;
			Intent intent = new Intent();
			String folderName = AlbumActivity.contentList.get(position).bucketName;
			intent.putExtra("folderName", folderName);
			intent.setClass(mContext, ShowAllPhotoActivity.class);
			mContext.startActivity(intent);
			choose_back.setVisibility(View.VISIBLE);
		}
	}

	public int dipToPx(int dip) {
		return (int) (dip * dm.density + 0.5f);
	}

	@Override
	public void convert(CommonViewHolder vh, ImageBucket bucket, int position) {
		imageView = vh.getView(R.id.file_image);
		choose_back = vh.getView(R.id.choose_back);
		folderName = vh.getView(R.id.name);
		fileNum = vh.getView(R.id.filenum);
		vh.setAdjustViewBounds(imageView, true).setScaleType(imageView, ImageView.ScaleType.FIT_XY);

		String path;
		if (mDatas.get(position).imageList != null) {
			// ����ͼƬ·��
			path = mDatas.get(position).imageList.get(0).imagePath;
			// ��folderName����ֵΪ�ļ������
			// ��fileNum�����ļ�����ͼƬ����
			vh.setText(folderName, mDatas.get(position).bucketName).setText(fileNum, "" + mDatas.get(position).count);
		} else
			path = "android_hybrid_camera_default";
		if (path.contains("android_hybrid_camera_default"))
			vh.setImageResource(imageView, R.drawable.plugin_camera_no_pictures);
		else {
			ImageItem item = mDatas.get(position).imageList.get(0);
			vh.setTag(imageView, item.imagePath);
			cache.displayBmp(imageView, item.thumbnailPath, item.imagePath, callback);
		}
		vh.setOnClickListener(imageView, new ImageViewClickListener(position, choose_back));
	}

	ImageCallback callback = new ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag())) {
					((ImageView) imageView).setImageBitmap(bitmap);
				}
			}
		}
	};
}
