package com.lsjr.zizisteward.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

	public static boolean isWifi(Context paramContext) {
		return "2".equals(getNetType(paramContext)[0]);
	}

	public static boolean isMobile(Context paramContext) {
		return "1".equals(getNetType(paramContext)[0]);
	}

	public static boolean isNetAvailable(Context paramContext) {
		if ("1".equals(getNetType(paramContext)[0]) || "2".equals(getNetType(paramContext)[0])) {
			return true;
		}
		return false;
	}

	public static String[] getNetType(Context paramContext) {
		String[] arrayOfString = { "Unknown", "Unknown" };
		PackageManager localPackageManager = paramContext.getPackageManager();
		if (localPackageManager.checkPermission("android.permission.ACCESS_NETWORK_STATE",
				paramContext.getPackageName()) != 0) {
			arrayOfString[0] = "Unknown";
			return arrayOfString;
		}

		ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext
				.getSystemService("connectivity");
		if (localConnectivityManager == null) {
			arrayOfString[0] = "Unknown";
			return arrayOfString;
		}

		NetworkInfo localNetworkInfo1 = localConnectivityManager.getNetworkInfo(1);
		if (localNetworkInfo1 != null && localNetworkInfo1.getState() == NetworkInfo.State.CONNECTED) {
			arrayOfString[0] = "2";
			return arrayOfString;
		}

		NetworkInfo localNetworkInfo2 = localConnectivityManager.getNetworkInfo(0);
		if (localNetworkInfo2 != null && localNetworkInfo2.getState() == NetworkInfo.State.CONNECTED) {
			arrayOfString[0] = "1";
			arrayOfString[1] = localNetworkInfo2.getSubtypeName();
			return arrayOfString;
		}

		return arrayOfString;
	}
}