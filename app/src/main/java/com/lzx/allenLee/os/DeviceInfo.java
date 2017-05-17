package com.lzx.allenLee.os;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
/**
 * 终端设备信息
 * @author allenlee
 *
 */
public class DeviceInfo
{
  private float density;
  private String deviceIMEI;	//IMEI号
  private String deviceIMSI;	//IMSI号
  private String deviceId;		//设备ID
  private String deviceName;	//设备名称
  private String firmware;		//固件
  private String language;		//语言
  private boolean netConnect;	//网络连接
  private String netExtraType;	
  private int netType = -1;
  private String resolution;
  private int screenHeight;		//屏幕高度
  private int screenWidth;		//屏幕宽度
  private String sdkVersion;	//sdk版本
  private String simId;			//sim卡Id

  public float getDensity()
  {
    return this.density;
  }

  public String getDeviceIMEI()
  {
    return this.deviceIMEI;
  }

  public String getDeviceIMSI()
  {
    return this.deviceIMSI;
  }

  public String getDeviceId()
  {
    return this.deviceId;
  }

  public String getDeviceName()
  {
    return this.deviceName;
  }

  public String getFirmware()
  {
    return this.firmware;
  }

  public String getLanguage()
  {
    return this.language;
  }

  public String getNetExtraType()
  {
    return this.netExtraType;
  }

  public int getNetType()
  {
    return this.netType;
  }

  public String getResolution()
  {
    return this.resolution;
  }

  public int getScreenHeight()
  {
    return this.screenHeight;
  }

  public int getScreenWidth()
  {
    return this.screenWidth;
  }

  public String getSdkVersion()
  {
    return this.sdkVersion;
  }

  public String getSimId()
  {
    return this.simId;
  }

  public void initDeviceInfo(Context paramContext)
  {
    TelephonyManager localTelephonyManager = (TelephonyManager)paramContext.getSystemService("phone");
    this.deviceIMEI = localTelephonyManager.getDeviceId();
    this.firmware = localTelephonyManager.getDeviceSoftwareVersion();
    this.netType = localTelephonyManager.getNetworkType();
    this.deviceIMSI = localTelephonyManager.getSubscriberId();
    new DisplayMetrics();
    DisplayMetrics localDisplayMetrics = paramContext.getResources().getDisplayMetrics();
    this.screenWidth = localDisplayMetrics.widthPixels;
    this.screenHeight = localDisplayMetrics.heightPixels;
    this.density = localDisplayMetrics.density;
    this.firmware = Build.VERSION.RELEASE;
    this.sdkVersion = Build.VERSION.SDK;
    this.deviceId = Build.MODEL;
  }

  public boolean isNetConnect()
  {
    return this.netConnect;
  }
}