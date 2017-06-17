package com.lsjr.zizisteward.ly.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.photoflow.ImageDetailFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class FriendCircleSave extends Activity {
	
	private LinearLayout ll_save;
	private RelativeLayout rl_parent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.friend_circle_save);
		this.rl_parent = (RelativeLayout) super.findViewById(R.id.rl_parent);
		this.ll_save = (LinearLayout) super.findViewById(R.id.ll_save);
	
		this.rl_parent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rl_parent.setVisibility(View.GONE);
				finish();
			}
		});
		
		this.ll_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rl_parent.setVisibility(View.GONE);
				
				saveImageToGallery(FriendCircleSave.this, ImageDetailFragment.bm);
				finish();
			}
		});
	}
	
	public static void saveImageToGallery(Context context, Bitmap bmp) {
	    // 首先保存图片
	    File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
	    if (!appDir.exists()) {
	        appDir.mkdir();
	    }
	    String fileName = System.currentTimeMillis() + ".jpg";
	    File file = new File(appDir, fileName);
	    try {
	        FileOutputStream fos = new FileOutputStream(file);
	        bmp.compress(CompressFormat.JPEG, 100, fos);
	        fos.flush();
	        fos.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
		}
	    
	    // 其次把文件插入到系统图库
	    try {
	        MediaStore.Images.Media.insertImage(context.getContentResolver(),
					file.getAbsolutePath(), fileName, null);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    // 最后通知图库更新
	    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
	    Toast.makeText(context, "保存图片成功...",0).show();
	}
}
