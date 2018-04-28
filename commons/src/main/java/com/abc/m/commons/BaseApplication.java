package com.abc.m.commons;

import android.app.Application;

/**
 * Created by wanglu on 2017/11/27.
 */

public abstract class BaseApplication extends Application {
    private static BaseApplication baseApplication;
    public static boolean isDebug = false;


    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        isDebug = isDebug();
    }

    public static BaseApplication getBaseApplication() {
        return baseApplication;
    }

    public abstract boolean isDebug();

    public abstract String getAuthorization();

    public abstract long currentTime();
}
