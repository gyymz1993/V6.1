package com.lsjr.zizisteward.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

/**
 * Created by sdmt-gjw on 2015/8/6.
 */
public class FileUtils {

	public static final int INT = 59;
	public static final int INT1 = 592;
	public static final int INT2 = 595;
	public static final int INT3 = 33;
	public static final int INT4 = 602;
	public static final int INT5 = 452;
	public static final int INT6 = 529;

	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				dirSize += getDirSize(file);
			}
		}
		return dirSize;
	}

	public static String formatFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public static File getExternalCacheDir(Context context) {
		return context.getExternalCacheDir();
	}

	public static boolean deleteFile(File path) {
		if (path == null) {
			return false;
		}
		return deleteFile(path.getAbsolutePath());
	}

	public static String getFolderName(String filePath) {

		if (StringUtils.isEmpty(filePath)) {
			return filePath;
		}

		int filePosi = filePath.lastIndexOf(File.separator);
		return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
	}

	public static boolean makeFolders(String filePath) {
		return makeDirs(filePath);
	}

	public static boolean makeFolders(File filePath) {
		return makeDirs(filePath.getAbsolutePath());
	}

	public static boolean makeDirs(String filePath) {
		String folderName = getFolderName(filePath);
		if (StringUtils.isEmpty(folderName)) {
			return false;
		}

		File folder = new File(folderName);
		return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
	}

	public static boolean deleteFile(String path) {
		if ((path == null || path.trim().length() == 0)) {
			return true;
		}

		File file = new File(path);
		if (!file.exists()) {
			return true;
		}
		if (file.isFile()) {
			return file.delete();
		}
		if (!file.isDirectory()) {
			return false;
		}
		for (File f : file.listFiles()) {
			if (f.isFile()) {
				f.delete();
			} else if (f.isDirectory()) {
				deleteFile(f.getAbsolutePath());
			}
		}
		return file.delete();
	}

	public static boolean existSDCard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}

	public static boolean saveBitmapToGallery(Context context, String path, Bitmap bmp) {
		boolean result = false;
		path = path.substring(path.lastIndexOf("/") + 1);
		try {
			File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "sdmt");
			if (!file.mkdirs()) {
				Log.e("SignaturePad", "Directory not created");
			}
			File photo = new File(file, path);
			if (photo.exists()) {
				return true;
			}
			Bitmap newBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawColor(Color.WHITE);
			canvas.drawBitmap(bmp, 0, 0, null);
			OutputStream stream = new FileOutputStream(photo);
			bmp.compress(Bitmap.CompressFormat.JPEG, 80, stream);
			stream.close();
			Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri contentUri = Uri.fromFile(photo);
			mediaScanIntent.setData(contentUri);
			context.sendBroadcast(mediaScanIntent);
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String readAssets(Context context, String fileName) {
		InputStream is = null;
		String content = null;
		try {
			is = context.getAssets().open(fileName);
			if (is != null) {

				byte[] buffer = new byte[1024];
				ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
				while (true) {
					int readLength = is.read(buffer);
					if (readLength == -1)
						break;
					arrayOutputStream.write(buffer, 0, readLength);
				}
				is.close();
				arrayOutputStream.close();
				content = new String(arrayOutputStream.toByteArray());

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			content = null;
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return content;
	}

}
