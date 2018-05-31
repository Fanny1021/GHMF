package com.fanny.ghmf.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SharePrefrenceUtil {

	private static SharedPreferences sharedPreferences = null;
	private static Editor editor = null;
	private static final String FILE_NAME = "BiDataParam";
	public static final String TSET_USER_NAME="test_user_name";
	public static final String TEST_USER_ID="test_user_id";

	public static final String USER_NAME="user_id";
	public static final String USER_PSW="user_psw";

	public static final String HOST_USER_NAME="host_user_name";
	public static final String HOST_USER_PSW="hos_user_psw";

	/**
	 * 保存参数
	 * @param context
	 * @param key
	 * @param data
	 */
	public static void saveParam(Context context, String key, Object data) {
		String type = data.getClass().getSimpleName();
		sharedPreferences = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();

		if ("Integer".equals(type)) {
			editor.putInt(key, (Integer) data);
		} else if ("Boolean".equals(type)) {
			editor.putBoolean(key, (Boolean) data);
		} else if ("String".equals(type)) {
			editor.putString(key, (String) data);
		} else if ("Float".equals(type)) {
			editor.putFloat(key, (Float) data);
		} else if ("Boolean".equals(type)) {
			editor.putBoolean(key, (Boolean) data);
		} else if ("Long".equals(type)) {
			editor.putLong(key, (Long) data);
		}
		editor.commit();
	}

	/**
	 * 获取参数
	 * defValue为为默认值，如果当前获取不到数据就返回它
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static Object getData(Context context, String key, Object defValue) {
		String type = defValue.getClass().getSimpleName();
		sharedPreferences = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		if ("Integer".equals(type)) {
			return sharedPreferences.getInt(key, (Integer) defValue);
		} else if ("Boolean".equals(type)) {
			return sharedPreferences.getBoolean(key, (Boolean) defValue);
		} else if ("String".equals(type)) {
			return sharedPreferences.getString(key, (String) defValue);
		} else if ("Float".equals(type)) {
			return sharedPreferences.getFloat(key, (Float) defValue);
		} else if ("Long".equals(type)) {
			return sharedPreferences.getLong(key, (Long) defValue);
		}

		return null;
	}

	/**
	 * 删除某个参数
	 * @param context
	 * @param key
	 */
	public static void selectParam(Context context, String key) {

		sharedPreferences = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		editor.remove(key);
		editor.commit();
	}


	public static boolean CheckConfig(Context context){
		if (getData(context, "BluetoothAdress", "").equals("")) {
			saveParam(context, "BluetoothAdress", "无");
			Log.e("BluetoothAdress", "无");
		}
		if (getData(context, "BluetoothType", "").equals("")) {
			saveParam(context, "BluetoothType", "无");
			Log.e("BluetoothType", "无");
		}
		return true;
	}



}
