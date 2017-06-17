package com.zizisteward.view.refresh;

import com.lsjr.zizisteward.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * 下拉刷新列表�?
 */
public class PulldownRefreshListHeader extends BasePulldownRefershListHeader {
	private ImageView imageView;
	private TextView refreshHintText;
	private ProgressBar progressBar;

	public PulldownRefreshListHeader(Context context) {
		super(context);
	}

	@Override
	public View onGetContentView() {
		LinearLayout contentView = (LinearLayout) LayoutInflater.from(
				getContext()).inflate(R.layout.list_header_pull_down_refresh,
				null);
		imageView = (ImageView) contentView
				.findViewById(R.id.image_refreshHeader_arrow);
		refreshHintText = (TextView) contentView
				.findViewById(R.id.text_refreshHeader_refreshHint);
		progressBar = (ProgressBar) contentView
				.findViewById(R.id.progressBar_refreshHeader);
		onToggleToNormalState();
		return contentView;
	}

	@Override
	public void onToggleToNormalState() {
		if (isNomoreLoad) {
			setNomoreType();
		} else {
			imageView.setVisibility(View.VISIBLE);
			imageView.setImageResource(R.drawable.xlistview_arrow);
			imageView.clearAnimation();
			progressBar.setVisibility(View.INVISIBLE);
			refreshHintText.setText(res_normal);
		}
		invalidate();
	}

	@Override
	public void onToggleToRefreshingState() {
		if (isNomoreLoad) {
			setNomoreType();
		} else {
			progressBar.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.INVISIBLE);
			imageView.setImageDrawable(null);
			refreshHintText.setText(res_loading);
		}
		invalidate();
	}

	@Override
	public void onNormalToReadyRefreshState() {
		if (isNomoreLoad) {
			setNomoreType();
		} else {
			imageView.clearAnimation();
			RotateAnimation rotateAnimation = new RotateAnimation(0f, 180f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnimation.setFillAfter(true);
			rotateAnimation.setDuration(400);
			imageView.startAnimation(rotateAnimation);
			refreshHintText.setText(res_ready);
		}
	}

	@Override
	public void onReadyRefreshToNormalState() {
		if (isNomoreLoad) {
			setNomoreType();
		} else {
			imageView.clearAnimation();
			RotateAnimation rotateAnimation = new RotateAnimation(-180f, 0f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnimation.setFillAfter(true);
			rotateAnimation.setDuration(400);
			imageView.startAnimation(rotateAnimation);
			refreshHintText.setText(res_normal);
		}
	}

	@Override
	public void onReadyRefreshToRefresingState() {
		onToggleToRefreshingState();
	}

	@Override
	public void onNormalToRefreshingState() {
		onToggleToRefreshingState();
	}

	@Override
	public void onRefreshingToNormalState() {
		onToggleToNormalState();
	}

	/**
	 * 设置没有更多消息�?
	 */
	private void setNomoreType() {
		imageView.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.INVISIBLE);
		refreshHintText.setText(R.string.superlistview_header_hint_nomoremsg);

	}

}