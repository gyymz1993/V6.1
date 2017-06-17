package com.lsjr.zizisteward.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {
	private static final String SP_NAME = "config.xml";

	public static void writeBoolean(Context context, String key, boolean value) {
		getSp(context).edit().putBoolean(key, value).commit();

	}

	public static boolean readBoolean(Context context, String key, boolean defValue) {
		return getSp(context).getBoolean(key, defValue);
	}

	public static void writeString(Context context, String key, String value) {
		getSp(context).edit().putString(key, value).commit();

	}

	public static String readString(Context context, String key, String defValue) {
		return getSp(context).getString(key, defValue);
	}

	private static SharedPreferences getSp(Context context) {
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		return sp;
	}
}
