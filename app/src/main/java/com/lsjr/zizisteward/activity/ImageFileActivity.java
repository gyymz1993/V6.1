package com.lsjr.zizisteward.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.FolderAdapter;
import com.lsjr.zizisteward.basic.App;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 这个类主要是用来进行显示包含图片的文件夹
 * 
 * @author panjun 创建日期2015年12月24日 上午11:54:34
 */
public class ImageFileActivity extends Activity implements OnClickListener {

	private GridView gridView;
	private FolderAdapter folderAdapter;
	/**
	 * 标题信息
	 */
	private LinearLayout layout_title_back;
	private TextView layout_title_tv, layout_title_righttv;
	private ImageView layout_title_catears;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		setContentView(R.layout.plugin_camera_image_file);

		layout_title_back = (LinearLayout) findViewById(R.id.layout_title_back);
		layout_title_back.setOnClickListener(this);

		layout_title_tv = (TextView) findViewById(R.id.layout_title_tv);
		layout_title_tv.setText("选择相册");

		layout_title_righttv = (TextView) findViewById(R.id.layout_title_righttv);
		layout_title_righttv.setVisibility(View.GONE);

		gridView = (GridView) findViewById(R.id.fileGridView);
		folderAdapter = new FolderAdapter(this);
		folderAdapter.clear();
		folderAdapter.addAll(AlbumActivity.contentList);
		gridView.setAdapter(folderAdapter);
	}

	@Override
	public void onClick(View v) {
		finish();
	}
}
