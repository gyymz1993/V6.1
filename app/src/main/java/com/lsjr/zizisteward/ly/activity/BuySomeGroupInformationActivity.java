package com.lsjr.zizisteward.ly.activity;

import java.util.HashMap;
import java.util.Map;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.bean.ImageBean;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.lsjr.zizisteward.utils.UploadUtil;
import com.lsjr.zizisteward.utils.UploadUtil.OnUploadProcessListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BuySomeGroupInformationActivity extends Activity implements OnClickListener ,OnUploadProcessListener {
	private LinearLayout ll_back;
	private LinearLayout ll_sure;
	private static EditText et_content;
	private String uri;
	private String groupname;
	private String label_id;
	/**去上传文件*/
	protected static final int TO_UPLOAD_FILE = 1;
	/**上传文件响应*/
	protected static final int UPLOAD_FILE_DONE = 2; 
	/**选择文件*/
	public static final int TO_SELECT_PHOTO = 3;
	/**上传初始化*/
	private static final int UPLOAD_INIT_PROCESS = 4;
	/**上传中*/
	private static final int UPLOAD_IN_PROCESS = 5;
	
	private static Context context;
	
	private Handler handler = new Handler() {
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
			CustomDialogUtils.stopCustomProgressDialog(BuySomeGroupInformationActivity.this);
			switch (msg.what) {
			case TO_UPLOAD_FILE:
				//toUploadFile();
				finish();
				break;
				
			case UPLOAD_INIT_PROCESS:
				// progressBar.setMax(msg.arg1);
				break;
				
			case UPLOAD_IN_PROCESS:
				// progressBar.setProgress(msg.arg1);
				break;
				
			case UPLOAD_FILE_DONE:
				String result = "响应码：" + msg.arg1 + "\n响应信息：" + msg.obj + "\n耗时：" + UploadUtil.getRequestTime() + "秒";
				// uploadImageResult.setText(result);
				String message = (String) msg.obj;
				if (!message.contains("上传失败")) {
					ImageBean bean = GsonUtil.getInstance().fromJson(message, ImageBean.class);
				} else {
					System.out.print("进来了");
					New_Topic_Old_Style.CreateData();
					New_Topic_Old_Style.JoinData();
					New_Topic_Old_Style.FriendsData();
					GanapatiDataActivity.tv_name.setText(groupname);
					Toast.makeText(BuySomeGroupInformationActivity.this, message, Toast.LENGTH_SHORT).show();
					finish();
				}
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.buy_some_group_information_activity);
		this.findViewById();
		context = BuySomeGroupInformationActivity.this;
	}
	
	private void findViewById() {
		uri = getIntent().getStringExtra("uri");
		groupname = getIntent().getStringExtra("groupname");
		label_id = getIntent().getStringExtra("label_id");
		this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
		this.ll_sure = (LinearLayout) super.findViewById(R.id.ll_sure);
		this.et_content = (EditText) super.findViewById(R.id.et_content);
		
		this.ll_back.setOnClickListener(this);
		this.ll_sure.setOnClickListener(this);
	}

	public static void dis() {
		UploadUtil.space = 0;
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
        imm.hideSoftInputFromWindow(et_content.getWindowToken(),0);
		((BuySomeGroupInformationActivity) context).finish();
	}
	
	private void toUploadFile() {
		
			String fileKey = "imgFile";
			UploadUtil uploadUtil = UploadUtil.getInstance();
			uploadUtil.setOnUploadProcessListener(this); // 设置监听器监听上传状态
			Map<String, String> map = new HashMap<>();
			//HttpClientGet.transform = 1;
			map.put("desc", et_content.getText().toString());
			map.put("type", "1");
			map.put("groupname", groupname);
			map.put("owner", PreferencesUtils.getString(BuySomeGroupInformationActivity.this, "user_account"));
			map.put("label_id", label_id);
			UploadUtil.space = 1;
			
			CustomDialogUtils.startCustomProgressDialog(BuySomeGroupInformationActivity.this, "请稍候");
			uploadUtil.uploadFile(uri, fileKey, HttpConfig.adduploadGroupPhoto, map);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_back:
			
			finish();
			
			break;
			
		case R.id.ll_sure:
			
			String content = et_content.getText().toString();
			
			if ( null != content && content.length() >= 10) {
				toUploadFile();
			} else {
				Toast.makeText(BuySomeGroupInformationActivity.this, "群简介不能少于10个字", Toast.LENGTH_SHORT).show();
			}
			
			break;
		}
	}

	@Override
	public void onUploadDone(int responseCode, String message) {
		Message msg = Message.obtain();
		msg.what = UPLOAD_FILE_DONE;
		msg.arg1 = responseCode;
		msg.obj = message;
		handler.sendMessage(msg);
	}

	@Override
	public void onUploadProcess(int uploadSize) {
		Message msg = Message.obtain();
		msg.what = UPLOAD_IN_PROCESS;
		msg.arg1 = uploadSize;
		handler.sendMessage(msg);
	}

	@Override
	public void initUpload(int fileSize) {
		Message msg = Message.obtain();
		msg.what = UPLOAD_INIT_PROCESS;
		msg.arg1 = fileSize;
		handler.sendMessage(msg);
	}
}
