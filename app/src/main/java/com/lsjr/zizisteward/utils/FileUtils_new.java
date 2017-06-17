package com.lsjr.zizisteward.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

public class FileUtils_new {

	private static Context mContext;
	private static String fileName;
	public static String sDPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Photo_LJ/";
	private static File dir;
	private static File file;

	public static void saveBitmap(Bitmap bm, String picName, Context context) {
		try {
			mContext = context;
			File path1 = new File(sDPATH);
			if (!path1.exists()) {
				path1.mkdirs();
			}
			// if (!isFileExist("")) {
			// file.mkdirs();
			// file = createSDDir("");
			// }

			file = new File(path1, picName + ".JPEG");
			if (file.exists()) {
				file.delete();
			}
			FileOutputStream out = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);

			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File createSDDir(String dirName) throws IOException {
		dir = new File(sDPATH + dirName);
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

			// sDPATH =
			// Environment.getExternalStorageDirectory().getAbsolutePath() +
			// "/Photo_LJ/";
			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(sDPATH + fileName);
		file.isFile();
		return file.exists();
	}

	public static void delFile(String fileName) {
		File file = new File(sDPATH + fileName);
		if (file.isFile()) {
			file.delete();
		}
		file.exists();
	}

	public static void deleteDir() {
		File dir = new File(sDPATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete();
			else if (file.isDirectory())
				deleteDir();
		}
		dir.delete();
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}

}
