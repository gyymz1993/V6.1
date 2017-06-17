package com.lsjr.zizisteward.utils;

import java.lang.reflect.Type;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.preference.PreferenceManager;

/**
 */
public class PreferencesUtils {

	/**
	 * ����һ��boolean�͵�ֵ��ָ����Preference��
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param mode ģʽ������
	 * @param key ��
	 * @param value ֵ
	 */
	public static void putBoolean(Context context, String preferencesName, int mode, String key, boolean value){
        context.getSharedPreferences(preferencesName, mode).edit().putBoolean(key, value).commit();
	}
	
	/**
	 * ����һ��boolean�͵�ֵ��ָ����Preference��
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param key ��
	 * @param value ֵ
	 */
	public static void putBoolean(Context context, String preferencesName, String key, boolean value){
        context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
	}
	
	/**
	 * ����һ��boolean�͵�ֵ��Ĭ�ϵ�Preference��
	 * @param context ������
	 * @param key ��
	 * @param value ֵ
	 */
	public static void putBoolean(Context context, String key, boolean value){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, value).commit();
	}

	/**
	 * ��ָ����Preference��ȡ��һ��boolean�͵�ֵ
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param mode ģʽ������
	 * @param key ��
	 * @param defaultValue Ĭ��ֵ
	 * @return ֵ
	 */
	public static boolean getBoolean(Context context, String preferencesName, int mode, String key, boolean defaultValue){
        return context.getSharedPreferences(preferencesName, mode).getBoolean(key, defaultValue);
	}

	/**
	 * ��ָ����Preference��ȡ��һ��boolean�͵�ֵ
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param key ��
	 * @param defaultValue Ĭ��ֵ
	 * @return ֵ
	 */
	public static boolean getBoolean(Context context, String preferencesName, String key, boolean defaultValue){
        return context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).getBoolean(key, defaultValue);
	}

	/**
	 * ��ָ����Preference��ȡ��һ��boolean�͵�ֵ��Ĭ��ֵΪfalse
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param key ��
	 * @return ֵ
	 */
	public static boolean getBoolean(Context context, String preferencesName, String key){
        return context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).getBoolean(key, false);
	}

	/**
	 * ��Ĭ�ϵ�Preference��ȡ��һ��boolean�͵�ֵ
	 * @param context ������
	 * @param key ��
	 * @param defaultValue Ĭ��ֵ
	 * @return ֵ
	 */
	public static boolean getBoolean(Context context, String key, boolean defaultValue){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defaultValue);
	}

	/**
	 * ��Ĭ�ϵ�Preference��ȡ��һ��boolean�͵�ֵ��Ĭ��ֵΪfalse
	 * @param context ������
	 * @param key ��
	 * @return ֵ
	 */
	public static boolean getBoolean(Context context, String key){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, false);
	}
	
	/**
	 * ����һ��float�͵�ֵ��ָ����Preference��
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param mode ģʽ������
	 * @param key ��
	 * @param value ֵ
	 */
	public static void putFloat(Context context, String preferencesName, int mode, String key, float value){
		context.getSharedPreferences(preferencesName, mode).edit().putFloat(key, value).commit();
	}
	
	/**
	 * ����һ��float�͵�ֵ��ָ����Preference��
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param key ��
	 * @param value ֵ
	 */
	public static void putFloat(Context context, String preferencesName, String key, float value){
		context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).edit().putFloat(key, value).commit();
	}
	
	/**
	 * ����һ��float�͵�ֵ��Ĭ�ϵ�Preference��
	 * @param context ������
	 * @param key ��
	 * @param value ֵ
	 */
	public static void putFloat(Context context, String key, float value){
		PreferenceManager.getDefaultSharedPreferences(context).edit().putFloat(key, value).commit();
	}

	/**
	 * ��ָ����Preference��ȡ��һ��float�͵�ֵ
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param mode ģʽ������
	 * @param key ��
	 * @param defaultValue Ĭ��ֵ
	 * @return ֵ
	 */
	public static float getFloat(Context context, String preferencesName, int mode, String key, float defaultValue){
		return context.getSharedPreferences(preferencesName, mode).getFloat(key, defaultValue);
	}
	
	/**
	 * ��ָ����Preference��ȡ��һ��float�͵�ֵ
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param key ��
	 * @param defaultValue Ĭ��ֵ
	 * @return ֵ
	 */
	public static float getFloat(Context context, String preferencesName, String key, float defaultValue){
		return context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).getFloat(key, defaultValue);
	}
	
	/**
	 * ��ָ����Preference��ȡ��һ��float�͵�ֵ��Ĭ��ֵΪ0
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param key ��
	 * @return ֵ
	 */
	public static float getFloat(Context context, String preferencesName, String key){
		return context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).getFloat(key, 0);
	}
	
	/**
	 * ��Ĭ�ϵ�Preference��ȡ��һ��float�͵�ֵ
	 * @param context ������
	 * @param key ��
	 * @param defaultValue Ĭ��ֵ
	 * @return ֵ
	 */
	public static float getFloat(Context context, String key, float defaultValue){
		return PreferenceManager.getDefaultSharedPreferences(context).getFloat(key, defaultValue);
	}
	
	/**
	 * ��Ĭ�ϵ�Preference��ȡ��һ��float�͵�ֵ��Ĭ��ֵΪ0
	 * @param context ������
	 * @param key ��
	 * @return ֵ
	 */
	public static float getFloat(Context context, String key){
		return PreferenceManager.getDefaultSharedPreferences(context).getFloat(key, 0);
	}


	/**
	 * ����һ��int�͵�ֵ��ָ����Preference��
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param mode ģʽ������
	 * @param key ��
	 * @param value ֵ
	 */
	public static void putInt(Context context, String preferencesName, int mode, String key, int value){
		context.getSharedPreferences(preferencesName, mode).edit().putInt(key, value).commit();
	}
	
	/**
	 * ����һ��int�͵�ֵ��ָ����Preference��
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param key ��
	 * @param value ֵ
	 */
	public static void putInt(Context context, String preferencesName, String key, int value){
		context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
	}
	
	/**
	 * ����һ��int�͵�ֵ��Ĭ�ϵ�Preference��
	 * @param context ������
	 * @param key ��
	 * @param value ֵ
	 */
	public static void putInt(Context context, String key, int value){
		PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, value).commit();
	}
	
	/**
	 * ��ָ����Preference��ȡ��һ��int�͵�ֵ
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param mode ģʽ������
	 * @param key ��
	 * @param defaultValue Ĭ��ֵ
	 * @return ֵ
	 */
	public static int getInt(Context context, String preferencesName, int mode, String key, int defaultValue){
		return context.getSharedPreferences(preferencesName, mode).getInt(key, defaultValue);
	}
	
	/**
	 * ��ָ����Preference��ȡ��һ��int�͵�ֵ
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param key ��
	 * @param defaultValue Ĭ��ֵ
	 * @return ֵ
	 */
	public static int getInt(Context context, String preferencesName, String key, int defaultValue){
		return context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).getInt(key, defaultValue);
	}
	
	/**
	 * ��ָ����Preference��ȡ��һ��int�͵�ֵ��Ĭ��ֵΪ0
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param key ��
	 * @return ֵ
	 */
	public static int getInt(Context context, String preferencesName, String key){
		return context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).getInt(key, 0);
	}
	
	/**
	 * ��Ĭ�ϵ�Preference��ȡ��һ��int�͵�ֵ
	 * @param context ������
	 * @param key ��
	 * @param defaultValue Ĭ��ֵ
	 * @return ֵ
	 */
	public static int getInt(Context context, String key, int defaultValue){
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, defaultValue);
	}
	
	/**
	 * ��Ĭ�ϵ�Preference��ȡ��һ��int�͵�ֵ��Ĭ��ֵΪ0
	 * @param context ������
	 * @param key ��
	 * @return ֵ
	 */
	public static int getInt(Context context, String key){
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, 0);
	}
	
	/**
	 * ����һ��long�͵�ֵ��ָ����Preference��
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param mode ģʽ������
	 * @param key ��
	 * @param value ֵ
	 */
	public static void putLong(Context context, String preferencesName, int mode, String key, long value){
		context.getSharedPreferences(preferencesName, mode).edit().putLong(key, value).commit();
	}
	
	/**
	 * ����һ��long�͵�ֵ��ָ����Preference��
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param key ��
	 * @param value ֵ
	 */
	public static void putLong(Context context, String preferencesName, String key, long value){
		context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).edit().putLong(key, value).commit();
	}
	
	/**
	 * ����һ��long�͵�ֵ��Ĭ�ϵ�Preference��
	 * @param context ������
	 * @param key ��
	 * @param value ֵ
	 */
	public static void putLong(Context context, String key, long value){
		PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(key, value).commit();
	}
	
	/**
	 * ��ָ����Preference��ȡ��һ��long�͵�ֵ
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param mode ģʽ������
	 * @param key ��
	 * @param defaultValue Ĭ��ֵ
	 * @return ֵ
	 */
	public static long getLong(Context context, String preferencesName, int mode, String key, long defaultValue){
		return context.getSharedPreferences(preferencesName, mode).getLong(key, defaultValue);
	}
	
	/**
	 * ��ָ����Preference��ȡ��һ��long�͵�ֵ
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param key ��
	 * @param defaultValue Ĭ��ֵ
	 * @return ֵ
	 */
	public static long getLong(Context context, String preferencesName, String key, long defaultValue){
		return context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).getLong(key, defaultValue);
	}
	
	/**
	 * ��ָ����Preference��ȡ��һ��long�͵�ֵ��Ĭ��ֵΪ0
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param key ��
	 * @return ֵ
	 */
	public static long getLong(Context context, String preferencesName, String key){
		return context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).getLong(key, 0);
	}
	
	/**
	 * ��Ĭ�ϵ�Preference��ȡ��һ��long�͵�ֵ
	 * @param context ������
	 * @param key ��
	 * @param defaultValue Ĭ��ֵ
	 * @return ֵ
	 */
	public static long getLong(Context context, String key, long defaultValue){
		return PreferenceManager.getDefaultSharedPreferences(context).getLong(key, defaultValue);
	}
	
	/**
	 * ��Ĭ�ϵ�Preference��ȡ��һ��long�͵�ֵ��Ĭ��ֵΪ0
	 * @param context ������
	 * @param key ��
	 * @return ֵ
	 */
	public static long getLong(Context context, String key){
		return PreferenceManager.getDefaultSharedPreferences(context).getLong(key, 0);
	}
	
	/**
	 * ����һ��String�͵�ֵ��ָ����Preference��
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param mode ģʽ������
	 * @param key ��
	 * @param value ֵ
	 */
	public static void putString(Context context, String preferencesName, int mode, String key, String value){
		context.getSharedPreferences(preferencesName, mode).edit().putString(key, value).commit();
	}
	
	/**
	 * ����һ��String�͵�ֵ��ָ����Preference��
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param key ��
	 * @param value ֵ
	 */
	public static void putString(Context context, String preferencesName, String key, String value){
		context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).edit().putString(key, value).commit();
	}
	
	/**
	 * ����һ��String�͵�ֵ��Ĭ�ϵ�Preference��
	 * @param context ������
	 * @param key ��
	 * @param value ֵ
	 */
	public static void putString(Context context, String key, String value){
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).commit();
	}
	
	/**
	 * ��ָ����Preference��ȡ��һ��String�͵�ֵ
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param mode ģʽ������
	 * @param key ��
	 * @param defaultValue Ĭ��ֵ
	 * @return ֵ
	 */
	public static String getString(Context context, String preferencesName, int mode, String key, String defaultValue){
		return context.getSharedPreferences(preferencesName, mode).getString(key, defaultValue);
	}
	
	/**
	 * ��ָ����Preference��ȡ��һ��String�͵�ֵ
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param key ��
	 * @param defaultValue Ĭ��ֵ
	 * @return ֵ
	 */
	public static String getString(Context context, String preferencesName, String key, String defaultValue){
		return context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).getString(key, defaultValue);
	}
	
	/**
	 * ��Ĭ�ϵ�Preference��ȡ��һ��String�͵�ֵ
	 * @param context ������
	 * @param key ��
	 * @param defaultValue Ĭ��ֵ
	 * @return ֵ
	 */
	public static String getString(Context context, String key, String defaultValue){
		return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultValue);
	}
	
	/**
	 * ��Ĭ�ϵ�Preference��ȡ��һ��String�͵�ֵ��Ĭ��ֵΪ0
	 * @param context ������
	 * @param key ��
	 * @return ֵ
	 */
	public static String getString(Context context, String key){
		return PreferenceManager.getDefaultSharedPreferences(context).getString(key, null);
	}
	
	/**
	 * ����һ��Set<String>��ָ����Preference�У������ǰϵͳ��SDK�汾С��11����ὫSet<String>ת����JSON�ַ�������
	 * @param preferences Preferences
	 * @param key ��
	 * @param value ֵ
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void putStringSet(SharedPreferences preferences, String key, Set<String> value){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			preferences.edit().putStringSet(key, value).commit();
		}else{
			putObject(preferences, key, value);
		}
	}
	
	/**
	 * ����һ��Set<String>��ָ����Preference�У������ǰϵͳ��SDK�汾С��11����ὫSet<String>ת����JSON�ַ�������
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param mode ģʽ������
	 * @param key ��
	 * @param value ֵ
	 */
	public static void putStringSet(Context context, String preferencesName, int mode, String key, Set<String> value){
		SharedPreferences preferences = context.getSharedPreferences(preferencesName, mode);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			preferences.edit().putStringSet(key, value).commit();
		}else{
			putObject(preferences, key, value);
		}
	}
	
	/**
	 * ����һ��Set<String>��ָ����Preference�У������ǰϵͳ��SDK�汾С��11����ὫSet<String>ת����JSON�ַ�������
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param key ��
	 * @param value ֵ
	 */
	public static void putStringSet(Context context, String preferencesName, String key, Set<String> value){
		SharedPreferences preferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			preferences.edit().putStringSet(key, value).commit();
		}else{
			putObject(preferences, key, value);
		}
	}
	
	/**
	 * ����һ��Set<String>��Ĭ�ϵ�Preference�У������ǰϵͳ��SDK�汾С��11����ὫSet<String>ת����JSON�ַ�������
	 * @param context ������
	 * @param key ��
	 * @param value ֵ
	 */
	public static void putStringSet(Context context, String key, Set<String> value){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			preferences.edit().putStringSet(key, value).commit();
		}else{
			putObject(preferences, key, value);
		}
	}
	
	/**
	 * ��ָ����Preference��ȡ��һ��Set<String>�������ǰϵͳ��SDK�汾С��11�������ȡ��JSON�ַ���Ȼ����ת����Set<String>
	 * @param preferences Preferences
	 * @param key ��
	 * @param defaultValue Ĭ��ֵ
	 * @return �ַ�������
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static Set<String> getStringSet(SharedPreferences preferences, String key, Set<String> defaultValue){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			return preferences.getStringSet(key, defaultValue);
		}else{
			Set<String> strings = getObject(preferences, key, new TypeToken<Set<String>>(){}.getType());
			if(strings == null){
				strings = defaultValue;
			}
			return strings;
		}
	}
	
	/**
	 * ��ָ����Preference��ȡ��һ��Set<String>�������ǰϵͳ��SDK�汾С��11�������ȡ��JSON�ַ���Ȼ����ת����Set<String>
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param mode ģʽ������
	 * @param key ��
	 * @param defaultValue Ĭ��ֵ
	 * @return �ַ�������
	 */
	public static Set<String> getStringSet(Context context, String preferencesName, int mode, String key, Set<String> defaultValue){
		SharedPreferences preferences = context.getSharedPreferences(preferencesName, mode);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			return preferences.getStringSet(key, defaultValue);
		}else{
			Set<String> strings = getObject(preferences, key, new TypeToken<Set<String>>(){}.getType());
			if(strings == null){
				strings = defaultValue;
			}
			return strings;
		}
	}
	
	/**
	 * ��ָ����Preference��ȡ��һ��Set<String>�������ǰϵͳ��SDK�汾С��11�������ȡ��JSON�ַ���Ȼ����ת����Set<String>
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param key ��
	 * @param defaultValue Ĭ��ֵ
	 * @return �ַ�������
	 */
	public static Set<String> getStringSet(Context context, String preferencesName, String key, Set<String> defaultValue){
		SharedPreferences preferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			return preferences.getStringSet(key, defaultValue);
		}else{
			Set<String> strings = getObject(preferences, key, new TypeToken<Set<String>>(){}.getType());
			if(strings == null){
				strings = defaultValue;
			}
			return strings;
		}
	}
	
	/**
	 * ��ָ����Preference��ȡ��һ��Set<String>�������ǰϵͳ��SDK�汾С��11�������ȡ��JSON�ַ���Ȼ����ת����Set<String>
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param key ��
	 * @return �ַ�������
	 */
	public static Set<String> getStringSet(Context context, String preferencesName, String key){
		SharedPreferences preferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			return preferences.getStringSet(key, null);
		}else{
			return getObject(preferences, key, new TypeToken<Set<String>>(){}.getType());
		}
	}
	
	/**
	 * ��Ĭ�ϵ�Preference��ȡ��һ��Set<String>�������ǰϵͳ��SDK�汾С��11�������ȡ��JSON�ַ���Ȼ����ת����Set<String>
	 * @param context ������
	 * @param key ��
	 * @param defaultValue Ĭ��ֵ
	 * @return �ַ�������
	 */
	public static Set<String> getStringSet(Context context, String key, Set<String> defaultValue){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			return preferences.getStringSet(key, defaultValue);
		}else{
			Set<String> strings = getObject(preferences, key, new TypeToken<Set<String>>(){}.getType());
			if(strings == null){
				strings = defaultValue;
			}
			return strings;
		}
	}
	
	/**
	 * ��Ĭ�ϵ�Preference��ȡ��һ��Set<String>�������ǰϵͳ��SDK�汾С��11�������ȡ��JSON�ַ���Ȼ����ת����Set<String>
	 * @param context ������
	 * @param key ��
	 * @return �ַ�������
	 */
	public static Set<String> getStringSet(Context context, String key){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			return preferences.getStringSet(key, null);
		}else{
			return getObject(preferences, key, new TypeToken<Set<String>>(){}.getType());
		}
	}
	
	/**
	 * ����һ������ָ����Preference�У��˶���ᱻ��ʽ��ΪJSON��ʽ�ٴ�
	 * @param preferences Preferences
	 * @param key ��
	 * @param object ����
	 */
	public static void putObject(SharedPreferences preferences, String key, Object object){
		preferences.edit().putString(key, new Gson().toJson(object)).commit();
	}
	
	/**
	 * ����һ������ָ����Preference�У��˶���ᱻ��ʽ��ΪJSON��ʽ�ٴ�
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param mode ģʽ������
	 * @param key ��
	 * @param object ����
	 */
	public static void putObject(Context context, String preferencesName, int mode, String key, Object object){
		context.getSharedPreferences(preferencesName, mode).edit().putString(key, new Gson().toJson(object)).commit();
	}
	
	/**
	 * ����һ������ָ����Preference�У��˶���ᱻ��ʽ��ΪJSON��ʽ�ٴ�
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param key ��
	 * @param object ����
	 */
	public static void putObject(Context context, String preferencesName, String key, Object object){
		context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).edit().putString(key, new Gson().toJson(object)).commit();
	}
	
	/**
	 * ����һ������Ĭ�ϵ�Preference�У��˶���ᱻ��ʽ��ΪJSON��ʽ�ٴ�
	 * @param context ������
	 * @param key ��
	 * @param object ����
	 */
	public static void putObject(Context context, String key, Object object){
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, new Gson().toJson(object)).commit();
	}
	
	/**
	 * ��ָ����Preference��ȡ��һ������
	 * @param preferences Preferences
	 * @param key ��
	 * @param clas ����Class
	 * @return T
	 */
	public static <T> T getObject(SharedPreferences preferences, String key, Class<T> clas){
		String configJson = preferences.getString(key, null);
		if(!StringUtils.isEmpty(configJson)){
			return (T) new Gson().fromJson(configJson, clas);
		}else{
			return null;
		}
	}
	
	/**
	 * ��ָ����Preference��ȡ��һ������
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param mode ģʽ������
	 * @param key ��
	 * @param clas ����Class
	 * @return T
	 */
	public static <T> T getObject(Context context, String preferencesName, int mode, String key, Class<T> clas){
		String configJson = context.getSharedPreferences(preferencesName, mode).getString(key, null);
		if(!StringUtils.isEmpty(configJson)){
			return (T) new Gson().fromJson(configJson, clas);
		}else{
			return null;
		}
	}
	
	/**
	 * ��ָ����Preference��ȡ��һ������
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param key ��
	 * @param clas ����Class
	 * @return T
	 */
	public static <T> T getObject(Context context, String preferencesName, String key, Class<T> clas){
		String configJson = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).getString(key, null);
		if(!StringUtils.isEmpty(configJson)){
			return (T) new Gson().fromJson(configJson, clas);
		}else{
			return null;
		}
	}
	
	/**
	 * ��Ĭ�ϵ�Preference��ȡ��һ������
	 * @param context ������
	 * @param key ��
	 * @param clas ����Class
	 * @return T
	 */
	public static <T> T getObject(Context context, String key, Class<T> clas){
		String configJson = PreferenceManager.getDefaultSharedPreferences(context).getString(key, null);
		if(!StringUtils.isEmpty(configJson)){
			return (T) new Gson().fromJson(configJson, clas);
		}else{
			return null;
		}
	}
	
	/**
	 * ��ָ����Preference��ȡ��һ������
	 * @param preferences Preferences
	 * @param key ��
	 * @param typeOfT ��������
	 * @return T
	 */
	public static <T> T getObject(SharedPreferences preferences, String key, Type typeOfT){
		String configJson = preferences.getString(key, null);
		if(!StringUtils.isEmpty(configJson)){
			return new Gson().fromJson(configJson, typeOfT);
		}else{
			return null;
		}
	}

	/**
	 * ��ָ����Preference��ȡ��һ������
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param mode ģʽ������
	 * @param key ��
	 * @param typeOfT ��������
	 * @return T
	 */
	public static <T> T getObject(Context context, String preferencesName, int mode, String key, Type typeOfT){
		String configJson = context.getSharedPreferences(preferencesName, mode).getString(key, null);
		if(!StringUtils.isEmpty(configJson)){
			return new Gson().fromJson(configJson, typeOfT);
		}else{
			return null;
		}
	}

	/**
	 * ��ָ����Preference��ȡ��һ������
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param key ��
	 * @param typeOfT ��������
	 * @return T
	 */
	public static <T> T getObject(Context context, String preferencesName, String key, Type typeOfT){
		String configJson = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).getString(key, null);
		if(!StringUtils.isEmpty(configJson)){
			return new Gson().fromJson(configJson, typeOfT);
		}else{
			return null;
		}
	}

	/**
	 * ��Ĭ�ϵ�Preference��ȡ��һ������
	 * @param context ������
	 * @param key ��
	 * @param typeOfT
	 * @return T
	 */
	public static <T> T getObject(Context context, String key, Type typeOfT){
		String configJson = PreferenceManager.getDefaultSharedPreferences(context).getString(key, null);
		if(!StringUtils.isEmpty(configJson)){
			return new Gson().fromJson(configJson, typeOfT);
		}else{
			return null;
		}
	}
	
	/**
	 * ɾ��
	 * @param preferences Preferences
	 * @param keys ������
	 */
	public static void remove(SharedPreferences preferences, String... keys){
		if(keys == null){
			return;
		}
		Editor editor = preferences.edit();
		for(String key : keys){
			editor.remove(key);
		}
		editor.commit();
	}
	
	/**
	 * ɾ��
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param mode ģʽ������
	 * @param keys ������
	 */
	public static void remove(Context context, String preferencesName, int mode, String... keys){
		if(keys == null){
			return;
		}
		Editor editor = context.getSharedPreferences(preferencesName, mode).edit();
		for(String key : keys){
			editor.remove(key);
		}
		editor.commit();
	}
	
	/**
	 * ɾ��
	 * @param context ������
	 * @param preferencesName Preferences����
	 * @param keys ������
	 */
	public static void remove(Context context, String preferencesName, String... keys){
		if(keys == null){
			return;
		}
		Editor editor = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE).edit();
		for(String key : keys){
			editor.remove(key);
		}
		editor.commit();
	}
	
	/**
	 * ɾ��
	 * @param context ������
	 * @param keys ������
	 */
	public static void remove(Context context, String... keys){
		if(keys == null){
			return;
		}
		Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		for(String key : keys){
			editor.remove(key);
		}
		editor.commit();
	}
}