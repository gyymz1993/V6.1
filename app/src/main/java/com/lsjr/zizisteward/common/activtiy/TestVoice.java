package com.lsjr.zizisteward.common.activtiy;

import java.io.File;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView.EaseVoiceRecorderCallback;
import com.hyphenate.util.PathUtil;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.TravelActivity;
import com.lsjr.zizisteward.basic.App;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class TestVoice extends Activity {

	private ImageView iv;
	private ImageView iv_voice;
	private LyRecording lrd;
	protected Drawable[] micImages;

	private int space = 0;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			iv_voice.setVisibility(View.VISIBLE);
			if (space < 7) {
				iv_voice.setImageDrawable(micImages[space]);
				space++;

			} else {
				space = 0;
				iv_voice.setImageDrawable(micImages[space]);
				space++;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.test_voice);

		iv = (ImageView) findViewById(R.id.iv);
		iv_voice = (ImageView) findViewById(R.id.iv_voice);

		micImages = new Drawable[] { getResources().getDrawable(R.drawable.icon_voice_01),
				getResources().getDrawable(R.drawable.icon_voice_02),
				getResources().getDrawable(R.drawable.icon_voice_03),
				getResources().getDrawable(R.drawable.icon_voice_04),
				getResources().getDrawable(R.drawable.icon_voice_05),
				getResources().getDrawable(R.drawable.icon_voice_06),
				getResources().getDrawable(R.drawable.icon_voice_07), };
		// getResources().getDrawable(R.drawable.icon_voice_08),
		// getResources().getDrawable(R.drawable.icon_voice_09),
		// getResources().getDrawable(R.drawable.icon_voice_10),
		// getResources().getDrawable(R.drawable.icon_voice_11),
		// getResources().getDrawable(R.drawable.icon_voice_12),
		// getResources().getDrawable(R.drawable.icon_voice_13),
		// getResources().getDrawable(R.drawable.icon_voice_14) };

		lrd = new LyRecording(handler);

		iv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					lrd.startRecording(TestVoice.this);
					return true;

				case MotionEvent.ACTION_MOVE:

					return true;

				case MotionEvent.ACTION_UP:
					iv_voice.setVisibility(View.INVISIBLE);
					int length = lrd.stopRecoding();
					String path = lrd.getVoiceFilePath();
					
					if (length > 0) {
						sendVoiceMessage(path, length);
						setResult(7);
						finish();
					} else {
						iv_voice.setVisibility(View.GONE);
						Toast.makeText(TestVoice.this, "说话时间太短", Toast.LENGTH_SHORT).show();
					}

					return true;

				default:
					return false;
				}
			}
		});
	}

	protected void sendVoiceMessage(String filePath, int length) {
		EMMessage message = EMMessage.createVoiceSendMessage(filePath, length,
				App.getUserInfo().getGname().toLowerCase());
		sendMessage(message);

		// File file = new
		// java.io.File(String.valueOf(PathUtil.getInstance().getVoicePath()));

		// DeleteFile(file);

		// if (file != null && file.exists() && !file.isDirectory()) {
		// file.delete();
		// System.out.println("删除");
		// }
		//
		lrd.discardRecording();
	}

	protected void sendMessage(EMMessage message) {
		if (message == null) {
			return;
		}

		message.setChatType(ChatType.Chat);
		// send message
		EMClient.getInstance().chatManager().sendMessage(message);
	}

}
