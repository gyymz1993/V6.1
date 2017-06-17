package com.lsjr.zizisteward.ly.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.utils.FileUtils_new;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityChooseUploadPictures extends Activity implements
		OnClickListener {

	private TextView tv_cancel;
	private TextView tv_camera;
	private TextView tv_picture;
	private LinearLayout ll_parent;
	private RelativeLayout rl_parent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_choose_upload_pictures);
		this.findViewById();
	}

	private void findViewById() {
		this.tv_camera = (TextView) super.findViewById(R.id.tv_camera);
		this.tv_cancel = (TextView) super.findViewById(R.id.tv_cancel);
		this.tv_picture = (TextView) super.findViewById(R.id.tv_picture);
		this.ll_parent = (LinearLayout) super.findViewById(R.id.ll_parent);
		this.rl_parent = (RelativeLayout) super.findViewById(R.id.rl_parent);

		this.tv_camera.setOnClickListener(this);
		this.tv_cancel.setOnClickListener(this);
		this.rl_parent.setOnClickListener(this);
		this.tv_picture.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.rl_parent:
			this.ll_parent.setVisibility(View.GONE);
			this.finish();
			break;

		case R.id.tv_cancel:
			this.ll_parent.setVisibility(View.GONE);
			this.finish();
			break;

		case R.id.tv_camera:
			this.setResult(1);
			this.ll_parent.setVisibility(View.GONE);
			this.finish();
			break;

		case R.id.tv_picture:
			this.setResult(2);
			this.ll_parent.setVisibility(View.GONE);
			this.finish();
			break;
		}
	}
}
