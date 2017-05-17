package com.lzx.allenLee.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Process;
import android.util.DisplayMetrics;
import android.view.Window;

import com.lzx.allenLee.os.AppConstant;


public class ActivityUtil {
	/**
	 * 获取软件版本
	 * 
	 * @param paramContext
	 * @return
	 */
	public static String getSoftwareVersion(Context paramContext) {
		String packageName = AppConstant.getProjectPackage(paramContext);
		try {
			String softwareVersion = paramContext.getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES).versionName;
			return softwareVersion;
		} catch (PackageManager.NameNotFoundException localNameNotFoundException) {

			return "";
		}

	}
	
	/**
	 * 直接打开Activity
	 * @param paramContext
	 * @param paramClass
	 * @return
	 */
	public static boolean DirectToActivity(Context paramContext,
			Class<?> paramClass) {
		paramContext.startActivity(new Intent(paramContext, paramClass));
		return true;
	}
	/**
	 * restart app
	 * @param currentActivity
	 * @param paramClass
	 * @return
	 */
	public static boolean DirectToNewTaskActivity(Activity currentActivity,
			Class<?> paramClass) {
		Intent localIntent = new Intent(currentActivity, paramClass);
		localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		currentActivity.startActivity(localIntent);
		//exit APP
		Process.killProcess(Process.myPid());
		currentActivity.finish();
		System.exit(0);
		return true;
	}

	/**
	 * 添加快捷方式
	 * 
	 * @param paramContext
	 * @param paramString1
	 * @param paramString2
	 * @param paramString3
	 */
	private static void addShortcut(Context paramContext, String paramString1,
			String paramString2, String paramString3) {
	/*	Intent localIntent1 = new Intent("android.intent.action.MAIN");
		localIntent1.addCategory("android.intent.category.LAUNCHER");
		localIntent1.setClass(paramContext, WelcomeActivity.class);
		localIntent1.putExtra("appid", paramString2);
		Intent localIntent2 = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		localIntent2.putExtra("android.intent.extra.shortcut.NAME",
				paramString3);
		localIntent2.putExtra("duplicate", false);
		localIntent2.putExtra("android.intent.extra.shortcut.INTENT",
				localIntent1);
		Bitmap localBitmap = FileUtil.getBitmap(paramString1, paramContext);
		if (localBitmap != null)
			localIntent2.putExtra("android.intent.extra.shortcut.ICON",
					localBitmap);*/
		/*
		 * while (true) { paramContext.sendBroadcast(localIntent2); return;
		 * localIntent2.putExtra("android.intent.extra.shortcut.ICON_RESOURCE",
		 * 2130837728); }
		 */
	}

	public static void addShortcut(Context paramContext, String paramString1,
			String paramString2, String paramString3, Class<?> paramClass) {
		/*Intent localIntent1 = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		localIntent1.putExtra("android.intent.extra.shortcut.NAME",
				paramString2);
		localIntent1.putExtra("duplicate", false);
		Intent localIntent2 = new Intent(paramContext, paramClass);
		localIntent2.setAction(paramString1);
		localIntent1.putExtra("android.intent.extra.shortcut.INTENT",
				localIntent2);
		Bitmap localBitmap = FileUtil.getBitmap(paramString3, paramContext);
		if (localBitmap != null)
			localIntent1.putExtra("android.intent.extra.shortcut.ICON",
					localBitmap);*/
		/*
		 * while (true) { paramContext.sendBroadcast(localIntent1); return;
		 * localIntent1.putExtra("android.intent.extra.shortcut.ICON_RESOURCE",
		 * 2130837728); }
		 */
	}

	public static DisplayMetrics getDisplayInfo(Activity paramActivity) {
		return paramActivity.getResources().getDisplayMetrics();
	}

	/**
	 * 获取内存信息
	 * @param paramContext
	 * @param paramBoolean
	 */
	public static void getMemoryInfo(Context paramContext, boolean paramBoolean) {
		ActivityManager localActivityManager = (ActivityManager) paramContext
				.getSystemService("activity");
		//获取内存信息
		/*ActivityManager.MemoryInfo localMemoryInfo = new ActivityManager.MemoryInfo();
		localActivityManager.getMemoryInfo(localMemoryInfo);
		int availMem = (int) (localMemoryInfo.availMem / 1048576L);
		int threshold = (int) (localMemoryInfo.threshold / 1048576L);
		boolean isLowMemory = localMemoryInfo.lowMemory;

		PackageManager localPackageManager = paramContext.getPackageManager();
		int setMem = Utils.parseToInt(
				paramContext.getResources().getString(2131034116), 0);
		if ((setMem == 0) && (availMem > threshold)){
			
		}else{
			
			if ((availMem > setMem) && (!isLowMemory) && (availMem > threshold)){
				
			}
				
			Log.i("availMem " + localMemoryInfo.availMem + "\n");
			Log.i("lowMemory " + localMemoryInfo.lowMemory + "\n");
			Log.i("threshold " + localMemoryInfo.threshold + "\n");
			//Returns a list of application processes that are running on the device
			//获取运行进程列表存储在TreeMap结构中
			List localList = localActivityManager.getRunningAppProcesses();
			TreeMap localTreeMap = new TreeMap();
			Iterator localIterator1 = localList.iterator();
			while (localIterator1.hasNext()) {
				ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo = (ActivityManager.RunningAppProcessInfo) localIterator1
						.next();
				localTreeMap.put(
						Integer.valueOf(localRunningAppProcessInfo.pid),
						localRunningAppProcessInfo.processName);
			}
			Set pidSet = localTreeMap.keySet();
			int pid_Num = 0;
			int n = 0;
			Object localObject1 = null;
			int[] pidArray;
			int i3;
			Debug.MemoryInfo localMemoryInfo1;
			Object localObject2;
			String pidName;
			Iterator pidIterator2 = pidSet.iterator();
			while (pidIterator2.hasNext()) {
				int pidNum = ((Integer) pidIterator2.next()).intValue();
				pidArray = new int[1];
				pidArray[0] = pidNum;
				Debug.MemoryInfo[] arrayOfMemoryInfo = localActivityManager
						.getProcessMemoryInfo(pidArray);
				int i2 = arrayOfMemoryInfo.length;
				i3 = 0;
				if (i3 >= i2)
					continue;
				localMemoryInfo1 = arrayOfMemoryInfo[i3];
				localObject2 = null;
				pidName = (String) localTreeMap.get(Integer.valueOf(pidArray[0]));
			}
			try {
				ApplicationInfo localApplicationInfo = localPackageManager
						.getApplicationInfo(pidName, 8192);
				localObject2 = localApplicationInfo;
				if ((localObject2 == null)
						|| (AppConstant.packageName.equalsIgnoreCase(pidName))
						|| ((0x1 & localApplicationInfo.flags) != 0)
						|| (pidName.contains("input")) || (pidName.contains("qq"))
						|| (pidName.contains("tencent")))
				{
					
				}
				else{
					i3++;
					
					if (paramBoolean == true) {
						try {
							pid_Num = pidArray[0];
							localObject1 = pidName;
							Process.killProcess(pid_Num);
							localActivityManager.restartPackage(pidName);
						} catch (Exception localException2) {
						}
						continue;
					}
					int i4 = localMemoryInfo1.getTotalPss();
					if (n >= i4)
						continue;
					n = localMemoryInfo1.getTotalPss();
					pid_Num = pidArray[0];
					localObject1 = pidName;
					break;
				}
				if ((pid_Num == 0) || (paramBoolean))
					continue;
				try {
					//杀掉进程
					Process.killProcess(pid_Num);
					localActivityManager.restartPackage(pidName);
					Log.i("killed process pid=====" + pid_Num + "  pkname="
							+ localObject1);
				} catch (Exception localException1) {
				}
			} catch (PackageManager.NameNotFoundException localNameNotFoundException) {

			}
		}*/
	}
	/**
	 * 加载配置文件变量
	 * @param paramContext
	 * @param paramName
	 * @param paramDefaultValue
	 * @return
	 */
	public static String getPreference(Context paramContext,
			String paramName, String paramDefaultValue) {
		return paramContext.getSharedPreferences(
				AppConstant.getProjectPackage(paramContext), 0).getString(
				paramName, paramDefaultValue);
	}



	/**
	 * 获取版本Code
	 * 
	 * @param paramContext
	 * @return
	 */
	public static int getVersionCode(Context paramContext) {
		String str = AppConstant.getProjectPackage(paramContext);
		try {
			int j = paramContext.getPackageManager().getPackageInfo(str, 16384).versionCode;
			return j;
		} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
			return 0;
		}
	}

	public static void intoFlashActivity(Context paramContext) {
	}

	public static int jsStartActivity(Context paramContext,
			String[] paramArrayOfString, boolean paramBoolean) {
		int i = 0;
		Intent localIntent = new Intent();
		if ((paramContext == null) || (paramArrayOfString.length <= 0)
				|| (paramArrayOfString[0].length() <= 0)
				|| (paramArrayOfString[1].length() <= 0))
			i = -1;
		while (true) {

			localIntent.setComponent(new ComponentName(paramArrayOfString[0],
					paramArrayOfString[1]));
			if (paramBoolean)
				localIntent.setAction("android.intent.action.VIEW");
			String str = paramArrayOfString[2];
			if ((str != null) && (str.length() > 0)) {
				String[] arrayOfString1 = str.split("\\|");
				for (int j = 0; j < arrayOfString1.length; j++) {
					String[] arrayOfString2 = arrayOfString1[j].split(":");
					localIntent.putExtra(arrayOfString2[0].trim(),
							arrayOfString2[1].trim());
				}
			}
			try {
				paramContext.startActivity(localIntent);
				i = -2;
			} catch (Exception localException) {

				if (localException.getMessage().startsWith(
						"Unable to find explicit activity"))
					continue;
				i = 1;
			}
			return i;
		}
	}

	/**
	 * 保存配置信息
	 * 
	 * @param paramContext
	 * @param paramString1
	 * @param paramString2
	 */
	public static void savePreference(Context paramContext,
			String paramString1, String paramString2) {
		SharedPreferences.Editor localEditor = paramContext
				.getSharedPreferences(
						AppConstant.getProjectPackage(paramContext), 0).edit();
		localEditor.putString(paramString1, paramString2);
		localEditor.commit();
	}

	public static void savePreference(Context paramContext, String paramString,
			boolean paramBoolean) {
		SharedPreferences.Editor localEditor = paramContext
				.getSharedPreferences(
						AppConstant.getProjectPackage(paramContext), 0).edit();
		localEditor.putBoolean(paramString, paramBoolean);
		localEditor.commit();
	}

	/**
	 * 设置全屏幕
	 * 
	 * @param paramActivity
	 */
	public static void setFullscreen(Activity paramActivity) {
		paramActivity.getWindow().setFlags(1024, 1024);
	}

	/**
	 * 设置输入法隐藏
	 * 
	 * @param paramActivity
	 */
	public static void setInputMethodHidden(Activity paramActivity) {
		paramActivity.getWindow().setSoftInputMode(3);
	}

	/**
	 * 设置窗口无标题
	 * 
	 * @param paramActivity
	 */
	public static void setNoTitle(Activity paramActivity) {
		paramActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
}
