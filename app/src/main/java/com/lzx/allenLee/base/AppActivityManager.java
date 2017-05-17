package com.lzx.allenLee.base;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;

import com.lzx.allenLee.global.Global;

/**
 * Activity管理类
 * 
 * @author allenlee
 * 
 */
public class AppActivityManager {
	public static final int BASESTART = 3;
	public static final int DESKTOPSTART = 2;
	public static final int USERDESKTOPSTART = 4;
	public static final int WELCOMESTART = 1;
	private static ArrayList<Activity> activityList;
	private static Global globalInfo_ = null;
	private static ArrayList<Activity> swActiList_;

	static {
		activityList = null;
		swActiList_ = null;
	}

	/**
	 * 将当前Activity添加到activityList中
	 * 
	 * @param paramActivity
	 */
	public static void addActivity(Activity paramActivity) {
		if (paramActivity != null) {
			if (activityList == null) {
				activityList = new ArrayList<Activity>();
			}
			activityList.add(paramActivity);
		}
	}

	public static void addSwActivity(Activity paramActivity) {
		if (paramActivity != null) {
			if (swActiList_ == null)
				swActiList_ = new ArrayList(0);
			swActiList_.add(paramActivity);
		}
	}

	/**
	 * 清除列表
	 */
	public static void clearActivity() {
		if ((activityList == null) || (activityList.size() == 0)) {

		} else {
			activityList.clear();
		}
	}

	public static void doExit() {
		if ((activityList != null) && (activityList.size() > 0)){
			for (int i = -1 + activityList.size(); i >= 0; i--)
				((Activity) activityList.get(i)).finish();
		}
	}

	public static ArrayList<Activity> getActivitys() {
		return activityList;
	}

	public static Global getGlobalInfo() {
		return globalInfo_;
	}

	public static Activity getHomeActivity() {
		if ((activityList == null) || (activityList.size() == 0))
			;
		for (Activity localActivity = null;; localActivity = (Activity) activityList
				.get(0))
			return localActivity;
	}

	public static Activity getLastActivity() {
		if ((activityList == null) || (activityList.size() <= 1))
			;
		for (Activity localActivity = null;; localActivity = (Activity) activityList
				.get(-2 + activityList.size()))
			return localActivity;
	}

	/**
	 * 获得最上层的Activity
	 * 
	 * @return
	 */
	public static Activity getTopActivity() {
		Activity localActivity = null;
		if ((activityList == null) || (activityList.size() == 0)) {

		} else {

			localActivity = (Activity) activityList.get(activityList.size() - 1);
		}
		return localActivity;
	}

	public static void goHome() {
		if ((activityList != null) && (activityList.size() > 0))
			for (int j = -1 + activityList.size(); j > 0; j--)
				((Activity) activityList.get(j)).finish();
		if ((swActiList_ != null) && (swActiList_.size() > 0))
			for (int i = -1 + swActiList_.size(); i >= 0; i--)
				((Activity) swActiList_.get(i)).finish();
	}

	/**
	 * 初始化应用数据
	 * 
	 * @param paramContext
	 * @param paramInt
	 */
	public static void initAppData(android.content.Context paramContext) {

		globalInfo_ = Global.getGlobal();
		try {
			globalInfo_.checkAndCreateFiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Global.isInited) {
			// globalInfo_.reLoad(paramContext);
		} else {

			if (activityList == null) {
				activityList = new ArrayList(0);
				swActiList_ = new ArrayList(0);
			}
			globalInfo_.init(paramContext);
		}

	}

	/**
	 * 删除
	 * 
	 * @param paramInt
	 */
	public static void removeActivity(int paramInt) {
		if ((activityList == null) || (activityList.size() == 0)) {

		} else if (paramInt < activityList.size()) {
			activityList.remove(paramInt);
		}

	}

	/**
	 * 结束当前Activity
	 * 
	 * @param paramActivity
	 */
	public static void removeActivity(Activity paramActivity) {
		activityList.remove(paramActivity);
		if (!paramActivity.isFinishing())
			paramActivity.finish();
	}

	/**
	 * 删除所有的Activity
	 */
	public static void removeAllActivity() {
		if ((activityList == null) || (activityList.size() == 0)) {

		} else {
			for (int i = -1 + activityList.size(); i >= 0; i--)
				removeActivity((Activity) activityList.get(i));
		}
	}

}