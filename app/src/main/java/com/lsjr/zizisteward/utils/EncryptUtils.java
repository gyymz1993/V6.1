package com.lsjr.zizisteward.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.shove.security.Encrypt;

import android.annotation.SuppressLint;

public class EncryptUtils {
	public static String addSign(long id, String action) {
		String des = Encrypt.encrypt3DES(id + "," + action + "," + dateToString(new Date()), "TNNYF17DbevNyxVv");
		String md5 = Encrypt.MD5(des + "TNNYF17DbevNyxVv");
		String sign = des + md5.substring(0, 8);
		return sign;
	}

	@SuppressLint("SimpleDateFormat")
	public static String dateToString(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
}
