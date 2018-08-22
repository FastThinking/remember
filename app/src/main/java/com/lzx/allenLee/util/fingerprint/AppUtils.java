package com.lzx.allenLee.util.fingerprint;


public class AppUtils {
    public static String getMethodName() {
        try {
            StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
            return stacktrace[3].getMethodName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
