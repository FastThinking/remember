package com.lzx.allenLee.global;

import com.lzx.allenLee.R;
import com.lzx.allenLee.util.ActivityUtil;

import android.content.Context;

/**
 * 应用程序常量
 *
 * @author allenlee
 */
public class AppConstant {
    public static int DistanceValue = 0;
    public static final String apkName = "Ytt.apk";
    public static final String decodeApkName = "CaptureActivity.apk";
    public static String packageName = "com.lzx.allenLee";
    public static boolean printLog;
    public static String projectName = "remember";

    static {
        printLog = true;
        DistanceValue = 100;
    }

    public static String getProjectName(Context paramContext) {
        projectName = paramContext.getString(R.string.projectName);
        return projectName;
    }

    /**
     * 获取项目包信息
     *
     * @param paramContext
     * @return
     */
    public static String getProjectPackage(Context paramContext) {
        return packageName;
    }

    /**
     * 初始化应用基本信息
     *
     * @param paramContext
     */
    public static void init(Context paramContext) {

        Global.softVersion_ = ActivityUtil.getSoftwareVersion(paramContext);
//      Global.getGlobal().loadMobileInfo(paramContext);
    }
}
