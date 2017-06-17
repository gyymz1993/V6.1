package com.lsjr.zizisteward.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spanned;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ToggleButton;


/**
 * ͨ��ViewHolder
 * @author lufan  2016��3��9������10:58:31
 *
 */
public class CommonViewHolder {
	private SparseArray<View> mViews;
	private View mConvertView;
	private int mPosition;

	public CommonViewHolder(Context context, int position, int layoutId, ViewGroup parent) {
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}

	/**
	 * ��ڷ���ȥ�жϴ�������convertView�Ƿ�Ϊnull
	 */
	public static CommonViewHolder get(Context context, View convertView, ViewGroup parent, int position, int layoutId) {
		if (convertView == null) {
			return new CommonViewHolder(context, position, layoutId, parent);
		} else {
			CommonViewHolder vh = (CommonViewHolder) convertView.getTag();
			// ����position
			vh.mPosition = position;
			return vh;

		}
	}

	/**
	 * ͨ��viewId��ȡ�ؼ�
	 * 
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);

		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * ����convertView
	 */
	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * ����Position
	 */
	public int getPosition() {
		return mPosition;
	}

	/**
	 * ����TextView��ֵ
	 * 
	 * @param viewId
	 * @param Text
	 * @return
	 */
	public CommonViewHolder setText(int viewId, String Text) {
		TextView tv = getView(viewId);
		tv.setText(Text);
		return this;
	}

	public CommonViewHolder setText(TextView tv, String Text) {
		tv.setText(Text);
		return this;
	}

	public CommonViewHolder setText(TextView tv, Spanned Text) {
		tv.setText(Text);
		return this;
	}

	public CommonViewHolder setText(int viewId, Spanned Text) {
		TextView tv = getView(viewId);
		tv.setText(Text);
		return this;
	}

	/**
	 * ����ImageView��ֵ
	 * 
	 * @param viewId
	 * @param resourceId
	 * @return
	 */
	public CommonViewHolder setImageResource(int viewId, int resourceId) {
		ImageView iv = getView(viewId);
		iv.setImageResource(resourceId);
		return this;
	}

	public CommonViewHolder setImageResource(ImageView iv, int resourceId) {
		iv.setImageResource(resourceId);
		return this;
	}

	public CommonViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
		ImageView iv = getView(viewId);
		iv.setImageBitmap(bitmap);
		return this;
	}

	public CommonViewHolder setImageBitmap(ImageView iv, Bitmap bitmap) {
		iv.setImageBitmap(bitmap);
		return this;
	}

	public CommonViewHolder setProgress(int viewId, int progress) {
		ProgressBar pb = getView(viewId);
		pb.setProgress(progress);
		return this;
	}

	public CommonViewHolder setProgress(ProgressBar pb, int progress) {
		pb.setProgress(progress);
		return this;
	}

	/**
	 * ���ÿؼ�����ʾ��������
	 * 
	 * @param viewId
	 * @param isVisisbility
	 * @return
	 */
	public CommonViewHolder setVisibility_TextView(int viewId, int isVisibility) {
		TextView tv = getView(viewId);
		tv.setVisibility(isVisibility);
		return this;
	}

	public CommonViewHolder setVisibility_TextView(TextView tv, int isVisibility) {
		tv.setVisibility(isVisibility);
		return this;
	}

	public CommonViewHolder setVisibility_ImageView(int viewId, int isVisibility) {
		ImageView iv = getView(viewId);
		iv.setVisibility(isVisibility);
		return this;
	}

	public CommonViewHolder setVisibility_ImageView(ImageView iv, int isVisibility) {
		iv.setVisibility(isVisibility);
		return this;
	}

	public CommonViewHolder setVisibility_LinearLayout(int viewId, int isVisibility) {
		LinearLayout ll = getView(viewId);
		ll.setVisibility(isVisibility);
		return this;
	}

	public CommonViewHolder setVisibility_LinearLayout(LinearLayout ll, int isVisibility) {
		ll.setVisibility(isVisibility);
		return this;
	}

	public CommonViewHolder setVisibility_RelativeLayout(int viewId, int isVisibility) {
		RelativeLayout rl = getView(viewId);
		rl.setVisibility(isVisibility);
		return this;
	}

	public CommonViewHolder setVisibility_RelativeLayout(RelativeLayout rl, int isVisibility) {
		rl.setVisibility(isVisibility);
		return this;
	}

	public CommonViewHolder setVisibility_View(int viewId, int isVisibility) {
		View v = getView(viewId);
		v.setVisibility(isVisibility);
		return this;
	}

	public CommonViewHolder setVisibility_View(View v, int isVisibility) {
		v.setVisibility(isVisibility);
		return this;
	}


	public CommonViewHolder setVisibility_ProgressBar(int viewId, int isVisibility) {
		ProgressBar pbs = getView(viewId);
		pbs.setVisibility(isVisibility);
		return this;
	}

	public CommonViewHolder setVisibility_ProgressBar(ProgressBar pbs, int isVisibility) {
		pbs.setVisibility(isVisibility);
		return this;
	}

	public CommonViewHolder setVisibility_Button(int viewId, int isVisibility) {
		Button btn = getView(viewId);
		btn.setVisibility(isVisibility);
		return this;
	}

	public CommonViewHolder setVisibility_Button(Button btn, int isVisibility) {
		btn.setVisibility(isVisibility);
		return this;
	}

	/**
	 * ΪView����LayoutParams
	 * 
	 * @param viewId
	 * @param params
	 * @return
	 */
	public CommonViewHolder setLayoutParams(int viewId, LayoutParams params) {
		View v = getView(viewId);
		v.setLayoutParams(params);
		return this;
	}

	public CommonViewHolder setLayoutParams(View v, LayoutParams params) {
		v.setLayoutParams(params);
		return this;
	}

	/**
	 * ����������ɫ
	 */
	public CommonViewHolder setTextColor(TextView tv, int color) {
		tv.setTextColor(color);
		return this;
	}

	/**
	 * ΪTextView���ñ���
	 * 
	 * @param tv
	 * @param res
	 * @return
	 */
	public CommonViewHolder setBackgroundResource_TextView(TextView tv, int res) {
		tv.setBackgroundResource(res);
		return this;
	}

	public CommonViewHolder setBackgroundResource_TextView(int viewId, int res) {
		TextView tv = getView(viewId);
		tv.setBackgroundResource(res);
		return this;
	}

	public CommonViewHolder setBackgroundColor_TextView(TextView tv, int color) {
		tv.setBackgroundColor(color);
		return this;
	}

	public CommonViewHolder setBackgroundColor_TextView(int viewId, int color) {
		TextView tv = getView(viewId);
		tv.setBackgroundColor(color);
		return this;
	}

	/**
	 * ΪRelativeLayout���ñ���
	 */

	public CommonViewHolder setBackgroundResource_RelativeLayout(RelativeLayout rel, int res) {
		rel.setBackgroundResource(res);
		return this;
	}

	public CommonViewHolder setBackgroundResource_RelativeLayout(int viewId, int res) {
		RelativeLayout rel = getView(viewId);
		rel.setBackgroundResource(res);
		return this;
	}

	public CommonViewHolder setBackgroundColor_RelativeLayout(RelativeLayout rel, int color) {
		rel.setBackgroundColor(color);
		return this;
	}

	public CommonViewHolder setBackgroundColor_RelativeLayout(int viewId, int color) {
		RelativeLayout rel = getView(viewId);
		rel.setBackgroundColor(color);
		return this;
	}

	/**
	 * ΪLinearLayout���ñ���
	 */

	public CommonViewHolder setBackgroundResource_LinearLayout(LinearLayout lin, int res) {
		lin.setBackgroundResource(res);
		return this;
	}

	public CommonViewHolder setBackgroundResource_LinearLayout(int viewId, int res) {
		LinearLayout lin = getView(viewId);
		lin.setBackgroundResource(res);
		return this;
	}

	public CommonViewHolder setBackgroundColor_LinearLayout(LinearLayout lin, int color) {
		lin.setBackgroundColor(color);
		return this;
	}

	public CommonViewHolder setBackgroundColor_LinearLayout(int viewId, int color) {
		LinearLayout lin = getView(viewId);
		lin.setBackgroundColor(color);
		return this;
	}

	public CommonViewHolder setTextSize(TextView product_name, float size) {
		product_name.setTextSize(size);
		return this;
	}

	public CommonViewHolder setTextSize(int viewId, float size) {
		TextView tv = getView(viewId);
		tv.setTextSize(size);
		return this;
	}

	public CommonViewHolder setTag(ImageView img, Object obj) {
		img.setTag(obj);
		return this;
	}

	public CommonViewHolder setTag(ToggleButton toggleButton, Object obj) {
		toggleButton.setTag(obj);
		return this;
	}

	public CommonViewHolder setTag(Button choosetoggle, Object obj) {
		choosetoggle.setTag(obj);
		return this;
	}

	public CommonViewHolder setOnClickListener(ToggleButton toggleButton, OnClickListener listener) {
		toggleButton.setOnClickListener(listener);
		return this;
	}

	public CommonViewHolder setChecked(ToggleButton toggleButton, boolean isChecked) {
		toggleButton.setChecked(isChecked);
		return this;
	}

	public CommonViewHolder setChecked(CheckBox checkBox, boolean isChecked) {
		checkBox.setChecked(isChecked);
		return this;
	}

	public CommonViewHolder setAdjustViewBounds(ImageView img, boolean adjustViewBounds) {
		img.setAdjustViewBounds(adjustViewBounds);
		return this;
	}

	public CommonViewHolder setScaleType(ImageView img, ScaleType type) {
		img.setScaleType(type);
		return this;
	}

	public CommonViewHolder setOnClickListener(ImageView img, OnClickListener listener) {
		img.setOnClickListener(listener);
		return this;
	}

	public CommonViewHolder setClickable(View v, boolean isclickable) {
		v.setClickable(isclickable);
		return this;
	}

	public CommonViewHolder setClickable(int viewId, boolean isclickable) {
		View v = getView(viewId);
		v.setClickable(isclickable);
		return this;
	}

}
