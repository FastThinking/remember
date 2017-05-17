package com.lzx.allenLee.global;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.lzx.allenLee.os.DeviceInfo;
import com.lzx.allenLee.util.ActivityUtil;
import com.lzx.allenLee.util.encryptionUtil.DESCoder;
import com.lzx.allenLee.util.fileManager.FileUtil;

/**
 * 全局变量
 * 
 * @author Allen.li
 * 
 */
public class Global {
	private static Global gInstance_;
	// 常用的静态变量
	public static String apnname; // apn名称
	public static String cameraSaveFileName; // 照片保存名称
	public static String cameraSavepath = ""; // 照片保存路径
	public static int commonNum;
	public static DeviceInfo deviceInfo; // 设备信息
	public static DisplayMetrics displaymetrics; // 显示信息
	public static String fileRootPath_; // 文件根路径

	public static String interFaceVersion;
	public static boolean isInited;
	// public static LogInfo logInfo_;
	public static String mSdCardPath; // sd卡
	// private static SettingInfo settingInfo;
	public static Uri photoUri;
	public static int screenDrawH;
	public static int screenHeight_;
	public static int screenWidth_;
	private static String serverUrl;
	public static String softVersion_;
	public static boolean taskBarTop;

	public String currentApp_;
	public int delayTime_;
	public int fontSize_;
	public boolean forceupdate_;
	public String imei_;
	public String imsi_;
	public boolean isEnterSpecialAppFailed;
	public boolean isFullScreen_;
	public boolean isLandscape;
	public boolean isLicAlertShow_;
	public boolean isNormalScreen_;
	public boolean isOffLine_;
	public boolean isPad_ = false;
	public boolean isSet_;
	public boolean isSpecialAppLoading;
	public String license_;
	public int menuHeight_ = 2 * this.taskBarHeight_;
	public String osVersion_;
	public int pageBarHeight_;
	public String phoneModel_;
	public String phoneNum_;
	public String rewriteURI_;
	public double screenAnHeightNumber_;
	public double screenAnWidthNumber_;
	public float screenDensity_;
	public double screenHeightNumber_;
	public double screenWidthNumber_;
	public String seriesNum_;
	public String serviceId_;
	public String setCookie_;
	public String skinStyle_;
	public String specifiedAppid_;
	public boolean supportLandscape;
	public int systemMenuHeight_;
	public int systemViewHeight_;
	public int taskBarHeight_ = 26;
	public String version_;
	public static String keyPath = "";
	public static String keyDir = "";
	static {
		cameraSaveFileName = "";
		mSdCardPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		serverUrl = "";
		softVersion_ = "1.1.0";
		interFaceVersion = "";
		commonNum = 75;
		isInited = false;
		apnname = "";
		photoUri = null;
		taskBarTop = true;
		keyDir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/key";
		keyPath = keyDir + "/key.txt";
	}

	/**
	 * 初始化变量值
	 * 
	 * @param paramContext
	 */
	public void init(Context paramContext) {
		if (isInited) {

		} else {
			this.specifiedAppid_ = "";
			this.skinStyle_ = "normal";
			this.serviceId_ = "";
			this.isSpecialAppLoading = false;
			this.isEnterSpecialAppFailed = true;
			this.setCookie_ = "";
			this.license_ = "1";
			this.isLicAlertShow_ = false;
			this.delayTime_ = 500;
			this.supportLandscape = false;
			String rootPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			AppConstant.getProjectName(paramContext);
			fileRootPath_ = rootPath + System.getProperty("file.separator")
					+ AppConstant.projectName + "/";
			File localFile = new File(fileRootPath_);
			if (!localFile.exists())
				localFile.mkdirs();
			deviceInfo = new DeviceInfo();
			deviceInfo.initDeviceInfo(paramContext);
			softVersion_ = ActivityUtil.getSoftwareVersion(paramContext);
			loadMobileInfo(paramContext);
			// loadSetting(paramContext);
			// this.fontSize_ = Utils.parseToInt(settingInfo.fontsize,
			// Font.DEFAULT_FONT_SIZE);
			// loadLogConfig(paramContext);
			this.menuHeight_ = (2 * this.taskBarHeight_);
			// Apn设置
			// loadApn(paramContext);
			// ApnAdapter.PreferId = ApnAdapter.getPreferVpnId(paramContext);
			// ApnAdapter.InsertVPDN(paramContext);
			isInited = true;
		}
	}

	/**
	 * 单例模式获取唯一对象
	 * 
	 * @return
	 */
	public static Global getGlobal() {
		if (gInstance_ == null)
			gInstance_ = new Global();
		return gInstance_;
	}

	public static String getFileRootPath_() {
		return fileRootPath_;
	}

	/**
	 * get the key from outside storage formate BASE64
	 * 
	 * @return
	 */
	public static String getOutSideStorageKey() throws FileNotFoundException {
		String key = null;
		key = FileUtil.readFileContentByFilePath(keyPath);
		return key;
	}

	/**
	 * 获取应用程序的根路径
	 * 
	 * @return
	 */
	public static String getFileRootPath() {
		if ((fileRootPath_ == null) || (fileRootPath_.equals(""))) {
			String str = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			fileRootPath_ = str + System.getProperty("file.separator")
					+ AppConstant.projectName + "/";
		}
		return fileRootPath_;
	}

	/**
	 * 获取显示信息
	 * 
	 * @param paramContext
	 */
	public void getDisplayInfo(Context paramContext) {
		displaymetrics = paramContext.getResources().getDisplayMetrics();
		DisplayMetrics displayMetrics = paramContext.getResources()
				.getDisplayMetrics();
		screenWidth_ = displayMetrics.widthPixels; // 以像素为单位的宽度
		screenHeight_ = displayMetrics.heightPixels;
		this.screenWidthNumber_ = (screenWidth_ / 240.0D);
		this.screenHeightNumber_ = (screenHeight_ / 320.0D);

		this.screenAnWidthNumber_ = displayMetrics.density; // 以dip为单位的宽度
		this.screenAnHeightNumber_ = displayMetrics.density;

		this.screenDensity_ = displayMetrics.density;
	}

	/**
	 * 检查并创建应用的目录
	 */
	public void checkAndCreateFiles() throws IOException {
		String[] filesPath = new String[11];
		filesPath[0] = "res";
		filesPath[1] = "res/i18n";
		filesPath[2] = "res/theme";
		filesPath[3] = "data";
		filesPath[4] = "data/db";
		filesPath[5] = "data/download";
		filesPath[6] = "data/sys";
		filesPath[7] = "data/tmp";
		filesPath[8] = "apps";
		filesPath[9] = "wrapper";
		filesPath[10] = "newpushfile";
		File rootFilePath = new File(getFileRootPath());
		if (!rootFilePath.exists())
			rootFilePath.mkdirs();

		for (int j = 0; j < filesPath.length; j++) {
			String fileName = FileUtil.getFilePath(filesPath[j]);
			File localFile3 = new File(fileName + "/");
			if (!localFile3.exists())
				localFile3.mkdir();
			else {
				if (localFile3.isDirectory()) {
					localFile3.delete();
					localFile3.mkdirs();
				}
			}
		}
	}

	/**
	 * 加载终端信息
	 * 
	 * @param paramContext
	 */
	public void loadMobileInfo(Context paramContext) {
		this.phoneModel_ = loadPhoneModel();
		if ((this.phoneModel_.equals("gt-p7500"))
				|| (this.phoneModel_.equals("sch-p739")))
			this.isPad_ = true;
		this.osVersion_ = getSdkVersion();
		// this.seriesNum_ = paramContext.getString(2131034529);
		loadOtherRc();
		getDeviceInfo(paramContext);
		loadFont(paramContext);
		loadESN(paramContext);
		loadIMSI(paramContext);
	}

	private String loadPhoneModel() {
		return Build.MODEL.toLowerCase();
	}

	public String getSdkVersion() {
		return Build.VERSION.RELEASE;
	}

	private void loadOtherRc() {
		this.pageBarHeight_ = (int) (35.0D * this.screenHeightNumber_);
		this.menuHeight_ = this.pageBarHeight_;
	}

	/**
	 * 获取终端信息
	 * 
	 * @param paramContext
	 */
	private void getDeviceInfo(Context paramContext) {
		TelephonyManager localTelephonyManager = (TelephonyManager) paramContext
				.getSystemService("phone");
		this.imei_ = localTelephonyManager.getDeviceId();
		this.phoneNum_ = localTelephonyManager.getLine1Number();
		this.imsi_ = localTelephonyManager.getSubscriberId();
	}

	/**
	 * 加载字体大小
	 * 
	 * @param paramContext
	 * @return
	 */
	private int loadFont(Context paramContext) {
		new TextView(paramContext).setText("南京烽火");
		this.fontSize_ = 14;
		return this.fontSize_;
	}

	/**
	 * 加载设备号
	 * 
	 * @param paramContext
	 * @return
	 */
	public String loadESN(Context paramContext) {
		String esn = "";
		this.imei_ = ((TelephonyManager) paramContext.getSystemService("phone"))
				.getDeviceId();
		if (this.imei_ == null) {
			esn = "s00000000000004";
		} else {
			esn = this.imei_;
		}
		return esn;
	}

	/**
	 * 加载IMSI号
	 * 
	 * @param paramContext
	 * @return
	 */
	public String loadIMSI(Context paramContext) {
		String IMSI = "";
		this.imsi_ = ((TelephonyManager) paramContext.getSystemService("phone"))
				.getSubscriberId();
		if (this.imsi_ == null) {
			IMSI = "100000000000004";
		} else {
			IMSI = this.imsi_;
		}
		return IMSI;
	}

	public void loadApn(Context paramContext) {
		// apnname = ApnAdapter.getApnName(paramContext);
	}
}
