package com.lsjr.zizisteward.utils;

import java.io.File;
import java.util.Properties;

import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.AppConfig;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by sdmt-gjw on 2015/8/6.
 */
public class ClearCacheUtils {

	private static App ac;

	public static void clearAppCache(final Activity activity) {
		CustomDialogUtils.startCustomProgressDialog(activity, "正在清理缓存");
		ac = (App) activity.getApplication();
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					Toast.makeText(ac, "缓存清除成功", Toast.LENGTH_SHORT).show();
					CustomDialogUtils.stopCustomProgressDialog(activity);
				} else {
					Toast.makeText(ac, "缓存清除失败", Toast.LENGTH_SHORT).show();
					CustomDialogUtils.stopCustomProgressDialog(activity);
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					clearAppCache();
					msg.what = 1;
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}

	public static void clearAppCache() {
		ac.deleteDatabase("webview.db");
		ac.deleteDatabase("webview.db-shm");
		ac.deleteDatabase("webview.db-wal");
		ac.deleteDatabase("webviewCache.db");
		ac.deleteDatabase("webviewCache.db-shm");
		ac.deleteDatabase("webviewCache.db-wal");

		clearCacheFolder(ac.getFilesDir(), System.currentTimeMillis());
		clearCacheFolder(ac.getCacheDir(), System.currentTimeMillis());

		if (isMethodsCompat(Build.VERSION_CODES.FROYO)) {
			clearCacheFolder(FileUtils.getExternalCacheDir(ac), System.currentTimeMillis());
		}

		Properties props = getProperties();
		for (Object key : props.keySet()) {
			String _key = key.toString();
			if (_key.startsWith("temp"))
				removeProperty(_key);
		}
	}

	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}

	private static int clearCacheFolder(File dir, long curTime) {
		int deletedFiles = 0;
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, curTime);
					}
					if (child.lastModified() < curTime) {
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deletedFiles;
	}

	public static Properties getProperties() {
		return AppConfig.getAppConfig(ac).get();
	}

	public static void removeProperty(String... key) {
		AppConfig.getAppConfig(ac).remove(key);
	}

	public static String calCache() {
		long fileSize = 0;
		String cacheSize = "0KB";
		File filesDir = App.getContext().getFilesDir();
		File cacheDir = App.getContext().getCacheDir();
		fileSize += FileUtils.getDirSize(filesDir);
		fileSize += FileUtils.getDirSize(cacheDir);

		if (FileUtils.isMethodsCompat(Build.VERSION_CODES.FROYO)) {
			File externalCacheDir = FileUtils.getExternalCacheDir(App.getContext());
			fileSize += FileUtils.getDirSize(externalCacheDir);
		}
		if (fileSize > 0)
			cacheSize = FileUtils.formatFileSize(fileSize);
		return cacheSize;
	}
}
