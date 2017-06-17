package com.lsjr.zizisteward.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import com.lsjr.zizisteward.basic.App;

public class ToastUtils {
	public static void show(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void show(String msg) {
		Toast.makeText(App.getContext(), msg, Toast.LENGTH_SHORT).show();
	}
	public static void scale(View v) {
		ScaleAnimation anim = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(300);
		v.startAnimation(anim);
	}

}