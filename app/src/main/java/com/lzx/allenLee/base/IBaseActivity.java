package com.lzx.allenLee.base;

import android.app.Application;
import android.content.Context;

/**
 * Activity的支持类接口，主要定义了Activity中常用的功能
 * @author Allen.li
 *
 */
public interface IBaseActivity {
	/**
	 * 获取Application对象
	 * 
	 * @return
	 */
	public abstract Application getApplication();
	
	/**
	 * 初始化异常捕获处理类
	 */
	public abstract void initUncaughtExceptionHandler();

	public abstract void isExitApp();

	/**
	 * 判断是否有网络连接，若没有，则弹出网络设置对话框，返回false
	 * 
	 * @return
	 */
	public abstract boolean validateInternet();

	/**
	 * 
	 * 判断是否有网络连接,没有返回false
	 * 
	 */
	public abstract boolean hasInternetConnected();

	/**
	 * 判断GPS是否已经开启.
	 * 
	 * @return
	 */
	public abstract boolean hasLocationGPS();

	/**
	 * 判断基站是否已经开启.
	 */
	public abstract boolean hasLocationNetWork();

	/**
	 * 检查内存卡.
	 */
	public abstract void checkMemoryCard();

	/**
	 * 获取进度条.统一进度条样式
	 * 
	 * @return
	 */
//	public abstract ProgressDialog getProgressDialog();

//	/**
//	 * 返回当前Activity上下文.
//	 */
//	public abstract Context getContext();

	/**
	 * 获取当前登录用户的SharedPreferences配置.
	 */
//	public SharedPreferences getLoginUserSharedPre();

	/**
	 * 用户是否在线（当前网络是否重连成功）
	 */
//	public boolean getUserOnlineState();

	/**
	 * 设置用户在线状态 true 在线 false 不在线
	 * 
	 * @param isOnline
	 */
//	public void setUserOnlineState(boolean isOnline);

}
