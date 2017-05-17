package com.lzx.allenLee.util.apnUtil;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;

import com.lzx.allenLee.global.Global;

public class ApnAdapter
{
  private static final String APN = "apn";
  public static final String APN_ = "apn";
  static final String CDMA_APN_ID = "apn_id";
  static final Uri CDMA_PREFERAPN_APN_URI;
  public static final String CM_Operate = "46000";
  static final Uri CONTENT_URI = Uri.parse("content://telephony/carriers");
  public static final Uri CONTENT_URI_;
  public static final String CT1_Operate = "46001";
  public static final String CT2_Operate = "46003";
  private static final String CURRENT = "current";
  public static final String CURRENT_ = "current";
  public static final String DEFAULT_SORT_ORDER = "name ASC";
  static final String GSM_APN_ID = "apn2_id";
  static final Uri GSM_PREFERAPN_APN_URI;
  private static final String ID = "_id";
  public static final String MCC = "mcc";
  public static final String MMSC = "mmsc";
  public static final String MMSPORT = "mmsport";
  public static final String MMSPROXY = "mmsproxy";
  public static final String MNC = "mnc";
  private static final String NAME = "name";
  public static final String NAME_ = "name";
  public static final String NUMERIC = "numeric";
  public static final String PASSWORD = "password";
  public static final String PORT = "port";
  static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
  private static final String PREFER_APN_ID_KEY = "apn_id";
  public static final String PROXY = "proxy";
  public static long PreferId = 0L;
  public static long PreferVpnId = 0L;
  public static final String SERVER = "server";
  private static final String TYPE = "type";
  public static final String TYPE_ = "type";
  public static final String USER = "user";
  public static final String UT_Operate = "46002";
  public static long ctnetId;

  static
  {
    PreferId = -2L;
    ctnetId = -3L;
    CONTENT_URI_ = Uri.parse("content://telephony/carriers");
    CDMA_PREFERAPN_APN_URI = Uri.withAppendedPath(CONTENT_URI, "preferapn");
    GSM_PREFERAPN_APN_URI = Uri.withAppendedPath(CONTENT_URI, "preferapn2");
  }

  public static void DeleteApn(Context paramContext, String paramString)
  {
    try
    {
      paramContext.getContentResolver().delete(Uri.parse("content://telephony/carriers"), "_id = " + paramString, null);
      return;
    }
    catch (Exception localException)
    {
        Log.v("ApnAdapter.DeleteApn-----------------" ,localException.getMessage());
    }
  }

 /* public static void InsertVPDN(Context paramContext)
  {
    String str1 = "";
    Global localGlobal = Global.getGlobal();
    if (localGlobal != null)
      str1 = localGlobal.imsi_;
    SettingInfo localSettingInfo = Global.getOaSetInfo();
    String str2 = "";
    int i = -1;
    if (localSettingInfo != null)
    {
      str2 = localSettingInfo.apnType_;
      i = Utils.parseToInt(localSettingInfo.apnSelected, -1);
    }
    if (("OMS1_5".equals(Global.getGlobal().phoneModel_)) || ((Global.getGlobal().phoneModel_ != null) && (Global.getGlobal().phoneModel_.toUpperCase().contains("MT810"))))
    {
      Global.apnname = "";
      if (i != -1);
    }
    String str7;
    String str8;
    String str9;
    label518: ContentValues localContentValues;
    while (true)
    {
      return;
      if (i == 0)
      {
        stopOphone(paramContext, "wap");
        setOphone(paramContext, "internet");
        Global.apnname = "cmnet";
        continue;
      }
      if (i == 1)
      {
        setOphone(paramContext, "internet");
        setOphone(paramContext, "wap");
        Global.apnname = "wap";
        continue;
      }
      int j = i - 2;
      ArrayList localArrayList1 = Utils.splitStr(localSettingInfo.apnName_, '|');
      if ((localArrayList1 == null) || (localArrayList1.size() <= 0))
        continue;
      stopOphone(paramContext, "internet");
      stopOphone(paramContext, "wap");
      for (int k = 0; ; k++)
      {
        int m = localArrayList1.size();
        if (k >= m)
          break;
        if (k != j)
          continue;
        setOphone(paramContext, getOPhoneType(paramContext, (String)localArrayList1.get(k)));
      }
      if (i == -1)
        continue;
      if ((i == 0) || (i == 1))
      {
        settingApn(paramContext);
        continue;
      }
      settingApn(paramContext);
      int n = i - 2;
      String str3 = "";
      String str4 = "";
      String str5 = "";
      String str6 = "";
      ArrayList localArrayList2 = Utils.splitStr(localSettingInfo.apnName_, '|');
      ArrayList localArrayList3 = Utils.splitStr(localSettingInfo.apnApn_, '|');
      ArrayList localArrayList4 = Utils.splitStr(localSettingInfo.apnUser_, '|');
      ArrayList localArrayList5 = Utils.splitStr(localSettingInfo.apnPassword_, '|');
      if ((localArrayList2 != null) && (n >= 0) && (n < localArrayList2.size()))
        str3 = (String)localArrayList2.get(n);
      if ((localArrayList3 != null) && (n >= 0) && (n < localArrayList3.size()))
        str4 = (String)localArrayList3.get(n);
      if ((localArrayList4 != null) && (n >= 0) && (n < localArrayList4.size()))
        str5 = (String)localArrayList4.get(n);
      if ((localArrayList5 != null) && (n >= 0) && (n < localArrayList5.size()))
        str6 = (String)localArrayList5.get(n);
      if ((str2 == null) || (str2.length() == 0))
      {
        if ((str1 == null) || (str1.length() <= 0))
          continue;
        if (str1.contains("46000"))
        {
          str7 = "460";
          str8 = "00";
          str9 = "46000";
          localContentValues = new ContentValues();
          if ((str3 != null) && (str3.length() > 0))
            localContentValues.put("name", str3);
          if ((str4 != null) && (str4.length() > 0))
            localContentValues.put("apn", str4);
          if ((str5 != null) && (str5.length() > 0))
            localContentValues.put("user", str5);
          if ((str6 != null) && (str6.length() > 0))
            localContentValues.put("password", str6);
          localContentValues.put("mcc", str7);
          localContentValues.put("mnc", str8);
          localContentValues.put("numeric", str9);
          if (!"HTC S710d".equalsIgnoreCase(Build.MODEL))
            break label982;
          localContentValues.put("pppnumber", "#777");
          localContentValues.put("type", "default");
          localContentValues.put("databearer", "分组数据承载");
          try
          {
            label686: long l2 = ContentUris.parseId(paramContext.getContentResolver().insert(CONTENT_URI_, localContentValues));
            PreferVpnId = l2;
            localContentValues.put("apn_id", Long.valueOf(l2));
            paramContext.getContentResolver().update(CDMA_PREFERAPN_APN_URI, localContentValues, null, null);
          }
          catch (Exception localException1)
          {
          }
        }
      }
    }
    while (true)
    {
      try
      {
        if (!Build.MODEL.toLowerCase().startsWith("sch"))
          break label1025;
        localContentValues.remove("ppp_digit");
        long l1 = ContentUris.parseId(paramContext.getContentResolver().insert(CONTENT_URI_, localContentValues));
        PreferVpnId = l1;
        localContentValues.put("apn_id", Long.valueOf(l1));
        paramContext.getContentResolver().update(CDMA_PREFERAPN_APN_URI, localContentValues, null, null);
      }
      catch (Exception localException2)
      {
      }
      break;
      if (str1.contains("46001"))
      {
        str7 = "460";
        str8 = "01";
        str9 = "46001";
        break label518;
      }
      if (str1.contains("46002"))
      {
        str7 = "460";
        str8 = "02";
        str9 = "46002";
        break label518;
      }
      if (!str1.contains("46003"))
        break;
      str7 = "460";
      str8 = "03";
      str9 = "46003";
      break label518;
      if (str2.equalsIgnoreCase("0"))
      {
        str7 = "460";
        str8 = "00";
        str9 = "46000";
        break label518;
      }
      if (str2.equalsIgnoreCase("1"))
      {
        str7 = "460";
        str8 = "03";
        str9 = "46003";
        break label518;
      }
      if (!str2.equalsIgnoreCase("2"))
        break;
      str7 = "460";
      str8 = "02";
      str9 = "46002";
      break label518;
      label982: if (Build.MODEL.toLowerCase().startsWith("sch"))
      {
        localContentValues.put("ppp_digit", "#777");
        break label686;
      }
      localContentValues.put("ppppwd", "#777");
      break label686;
      label1025: localContentValues.remove("ppppwd");
    }
  }*/

 

 
}
