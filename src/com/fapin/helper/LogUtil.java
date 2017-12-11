/**
 * @author fapin
 * create at 2017-12-05 09:59:26
 */

package com.fapin.helper;

import android.util.Log;

public class LogUtil {
    private static final boolean IS_DEBUG = false;

    public static void i(String message) {
        if (IS_DEBUG) {
            Log.i(getTag(), message);
        }
    }

    public static void v(String message) {
        if (IS_DEBUG) {
            Log.v(getTag(), message);
        }
    }

    public static void d(String message) {
        if (IS_DEBUG) {
            Log.d(getTag(), message);
        }
    }

    public static void w(String message) {
        if (IS_DEBUG) {
            Log.w(getTag(), message);
        }
    }

    public static void e(String message) {
        if (IS_DEBUG) {
            Log.e(getTag(), message);
        }
    }

    private static String getTag() {
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
        String callingClass = "";
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(LogUtil.class)) {
                callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                break;
            }
        }
        return callingClass;
    }
}
