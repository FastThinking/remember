package com.lzx.allenLee;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import com.lzx.allenLee.base.AppActivityManager;
import com.lzx.allenLee.global.Global;
import com.lzx.allenLee.os.AppConstant;
import com.lzx.allenLee.util.databaseUtil.DataBaseManager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;


/**
 * 全局应用程序类：用于保存和调用全局应用配置
 * 
 */
public class AppApplication extends Application {

	private static AppApplication instance;

//	private static DaoMaster daoMaster;
//	private static DaoSession daoSession;

	
	/**
	 * 单例模式中获取唯一的AppContext实例
	 */ 
	public static AppApplication getInstance() {
		if (null == instance) {
			instance = new AppApplication();
		}
		return instance;

	}
		
	@Override
	public void onCreate() {
		super.onCreate();
		initSysInfo();
	}
	
	
	/**
	 * 初始化系统数据库信息
	 */
	private void initSysInfo() {
		
		//初始化应用程序常量
		AppConstant.init(this);
		//获取显示信息
		Global.getGlobal().getDisplayInfo(this);
		//初始化应用信息
		AppActivityManager.initAppData(this);
//		//初始化数据库system.db3
//		DataBaseManager.getInstance();	

	}

	/**
	 * 取得DaoMaster
	 * 
	 * @param context
	 * @return
	 */
//	public static DaoMaster getDaoMaster(Context context) {
//		if (daoMaster == null) {
//			int version = DatabaseManager.getInstance(context).getDbVersion();
//			SQLiteOpenHelper helper = new SQLiteOpenHelper(context,
//					ConfigData.DATABASE, null, version) {
//
//				@Override
//				public void onUpgrade(SQLiteDatabase db, int oldVersion,
//						int newVersion) {
//				}
//
//				@Override
//				public void onCreate(SQLiteDatabase db) {
//				}
//
//				@Override
//				public void onDowngrade(SQLiteDatabase db, int oldVersion,
//						int newVersion) {
//				}
//			};
//			daoMaster = new DaoMaster(helper.getWritableDatabase());
//		}
//		return daoMaster;
//	}

	/**
	 * 取得DaoSession
	 * 
	 * @param context
	 * @return
	 */
//	public static DaoSession getDaoSession(Context context) {
//		if (daoSession == null) {
//			if (daoMaster == null) {
//				daoMaster = getDaoMaster(context);
//			}
//			daoSession = daoMaster.newSession();
//		}
//		return daoSession;
//	}

	private void copyAssetsToSD(String src, String target) {
		InputStream myInput;
		OutputStream myOutput;
		try {
			myOutput = new FileOutputStream(target);
			myInput = this.getAssets().open(src);
			byte[] buffer = new byte[1024];
			int length = myInput.read(buffer);
			while (length > 0) {
				myOutput.write(buffer, 0, length);
				length = myInput.read(buffer);
			}
			myOutput.flush();
			myInput.close();
			myOutput.close();
			buffer = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	/**
	 * 遍历所有Activity并finish
	 */
	public void exitApp() {
		AppActivityManager.doExit();
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.restartPackage(getPackageName());
		System.exit(0);
	}
}
