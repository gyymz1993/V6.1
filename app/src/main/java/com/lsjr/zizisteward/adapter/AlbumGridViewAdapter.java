package com.lsjr.zizisteward.adapter;

import java.util.List;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.BitmapCache.ImageCallback;
import com.lsjr.zizisteward.bean.ImageItem;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * 这个是显示一个文件夹里面的所有图片时用的适配器
 * @author panjun
 *
 * 创建日期2015年12月24日 上午11:56:13
 */
public class AlbumGridViewAdapter extends CommonAdapter<ImageItem> {
	private Context mContext;
	private List<ImageItem> selectedDataList;
	private DisplayMetrics dm;
	public BitmapCache cache;
	public ImageView imageView;
	public ToggleButton toggleButton;
	public Button choosetoggle;

	public AlbumGridViewAdapter(Context context) {
		super(context, R.layout.plugin_camera_select_imageview);
		mContext = context;
		cache = new BitmapCache(context);
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
	}

	public void AddselectedDataList(List<ImageItem> selectedDataList) {
		this.selectedDataList = selectedDataList;
	}

	@Override
	public void convert(CommonViewHolder vh, ImageItem t, int position) {

		imageView = vh.getView(R.id.image_view);
		toggleButton = vh.getView(R.id.toggle_button);
		choosetoggle = vh.getView(R.id.choosedbt);

		String path;
		if (mDatas != null && mDatas.size() > position)
			path = mDatas.get(position).imagePath;
		else
			path = "camera_default";
		if (path.contains("camera_default")) {
			vh.setImageResource(imageView, R.drawable.plugin_camera_no_pictures);
		} else {
			ImageItem item = mDatas.get(position);
			vh.setTag(imageView, item.imagePath);
			cache.displayBmp(imageView, item.thumbnailPath, item.imagePath, callback);
		}
		vh.setTag(toggleButton, position);
		vh.setTag(choosetoggle, position);
		vh.setOnClickListener(toggleButton, new ToggleClickListener(choosetoggle));
		if (selectedDataList.contains(mDatas.get(position))) {
			vh.setChecked(toggleButton, true);
			vh.setVisibility_Button(choosetoggle, View.VISIBLE);
		} else {
			vh.setChecked(toggleButton, false);
			vh.setVisibility_Button(choosetoggle, View.GONE);
		}

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

	public int dipToPx(int dip) {
		return (int) (dip * dm.density + 0.5f);
	}

	private class ToggleClickListener implements OnClickListener {
		TextView chooseBt;

		public ToggleClickListener(TextView choosebt) {
			this.chooseBt = choosebt;
		}

		@Override
		public void onClick(View view) {
			if (view instanceof ToggleButton) {
				ToggleButton toggleButton = (ToggleButton) view;
				int position = (Integer) toggleButton.getTag();
				if (mDatas != null && mOnItemClickListener != null && position < mDatas.size()) {
					mOnItemClickListener.onItemClick(toggleButton, position, toggleButton.isChecked(), chooseBt);
				}
			}
		}
	}

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(ToggleButton view, int position, boolean isChecked, TextView chooseBt);
	}

}
