package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

@SuppressLint("SetJavaScriptEnabled")
public class SmallZiZiCareActivity extends BaseActivity {
	private WebView webview;
	private RelativeLayout mRe_base;
	private String titleUrl;
	private RelativeLayout mIv_back;
	private RelativeLayout mRe_right;
	private Handler mHandler2;

	@Override
	public int getContainerView() {
		return R.layout.activity_small_zizi_care;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mProgressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		mIv_back = (RelativeLayout) findViewById(R.id.iv_back);
		mRe_base = (RelativeLayout) findViewById(R.id.re_base);
		mRe_right = (RelativeLayout) findViewById(R.id.re_right);
		webview = (WebView) findViewById(R.id.webview);
		WebSettings settings = webview.getSettings();
		webview.setVerticalScrollBarEnabled(false);
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setSupportMultipleWindows(true);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				titleUrl = url;
				System.out.println("里面得链家" + url);
				if (titleUrl.contains("bwlX")) {
					mRe_right.setVisibility(View.GONE);
					setmTitle("备忘录");
				}
				if (titleUrl.contains("xzi")) {
					mRe_right.setVisibility(View.GONE);
					setmTitle("小孜关怀");
				}
				if (titleUrl.contains("addts")) {
					mRe_right.setVisibility(View.VISIBLE);
					setmTitle("添加提醒");
					setmRight("完成");
					mRe_right.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							webview.loadUrl("JavaScript:addmemo()");
							mHandler2 = new Handler() {

								@Override
								public void handleMessage(Message msg) {
									if (msg.what == 1) {
										webview.goBack();
									}
								}
							};
						}
					});
				}
				if (titleUrl.contains("addtsdetailsX")) {
					mRe_right.setVisibility(View.VISIBLE);
					setmRight("完成");
					setmTitle("备忘录详情");
					mRe_right.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							webview.loadUrl("JavaScript:Modifymemo()");
							mHandler2 = new Handler() {

								@Override
								public void handleMessage(Message msg) {
									if (msg.what == 1) {
										webview.goBack();
									}
								}
							};

						}
					});
				}
				super.onPageFinished(view, url);
			}

		});

		getData();
		mIv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (webview.canGoBack()) {
					webview.goBack();
				} else {
					finish();
				}
			}
		});
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {

				if (newProgress == 100) {
					mProgressBar1.setVisibility(View.GONE);
				} else {
					mProgressBar1.setVisibility(View.VISIBLE);
					mProgressBar1.setProgress(newProgress);
				}

			}
		});
	}

	private void getData() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("OPT", "352");
		map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
		map.put("City", "");
		new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("小孜关怀" + result);
				try {
					JSONObject jsonObject = new JSONObject(result);
					String url = jsonObject.getString("ThekyrgyzUrl");
					webview.loadUrl(HttpConfig.IMAGEHOST + url);// 联网操作
					System.out.println("小孜关怀" + HttpConfig.IMAGEHOST + url);
					webview.addJavascriptInterface(new DemoJavaInterface(), "control");
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});
	}

	Handler handler = new Handler();

	// js调用Java
	public class DemoJavaInterface {

		@JavascriptInterface
		public void Recommended() {// 为您推荐
			Intent mIntent = new Intent(SmallZiZiCareActivity.this, ZiZiRecommendActivity.class);
			startActivity(mIntent);

		}

		@JavascriptInterface
		public void Logistics() {// 查看物流 到待收货订单
			Intent intent = new Intent(SmallZiZiCareActivity.this, MyAllOrderActivity.class);
			intent.putExtra("order", "wait_getgoods");
			startActivity(intent);

		}

		@JavascriptInterface
		public void housekeeper() {// 管家聊天
			Intent intent = new Intent(SmallZiZiCareActivity.this, CallButtonActivtiy.class);
			startActivity(intent);
		}

		@JavascriptInterface
		public void addmemo(int code, String msg) {// 添加备忘录
			if (code >= 0) {
				ToastUtils.show(getApplicationContext(), msg);
				mHandler2.sendEmptyMessage(code);
			} else {
				ToastUtils.show(getApplicationContext(), msg);
			}

		}

		@JavascriptInterface
		public void delectmemo(int code, String msg) {// 删除备忘录
			if (code >= 0) {
				handler.postDelayed(runnable, 100);
			} else {
				ToastUtils.show(getApplicationContext(), msg);
			}
		}

		@JavascriptInterface
		public void Modifymemo(int code, String msg) {// 修改备忘录
			if (code >= 0) {
				ToastUtils.show(getApplicationContext(), msg);
				mHandler2.sendEmptyMessage(code);
			} else {
				ToastUtils.show(getApplicationContext(), msg);
			}
		}

		@JavascriptInterface
		public void Recommendedforyou(String sid) {// 商品详情
			Intent intent = new Intent(getApplicationContext(), HomeBrandDetail.class);
			intent.putExtra("sid", sid);
			startActivity(intent);
		}

	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			final Dialog dialog = new Dialog(SmallZiZiCareActivity.this, R.style.dialog);
			dialog.setContentView(R.layout.dialog_send_goods);
			Window window = dialog.getWindow();
			window.setGravity(Gravity.CENTER | Gravity.CENTER);
			TextView tv_confirm = (TextView) dialog.findViewById(R.id.tv_confirm);
			TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
			tv_msg.setText("删除成功");
			tv_confirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					webview.goBack();
				}
			});
			dialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					dialog.dismiss();
					webview.goBack();
				}
			});
			dialog.show();
		}
	};
	private ProgressBar mProgressBar1;

	// android webview点击返回键返回上一个html
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
			webview.goBack();// 返回前一个页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
