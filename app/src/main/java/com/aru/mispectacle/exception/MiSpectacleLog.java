package com.aru.mispectacle.exception;

import android.os.SystemClock;
import android.util.Log;

/**
 * Logging utilities with TAG pre-set.
 */
public class MiSpectacleLog
{
    static final String TAG = "MiSpectacle";

    public static void d(String msg) {
        Log.d(TAG, msg);
    }
    public static void e(String msg) {
        Log.e(TAG, msg);
    }
    public static void i(String msg) {
        Log.i(TAG, msg);
    }
    public static void v(String msg) {
        Log.v(TAG, msg);
    }
    public static void w(String msg) {
        Log.w(TAG, msg);
    }

    public static void d(String msg, Throwable t) {
        Log.d(TAG, msg, t);
    }
    public static void e(String msg, Throwable t) {
        Log.e(TAG, msg, t);
    }
    public static void i(String msg, Throwable t) {
        Log.i(TAG, msg, t);
    }
    public static void v(String msg, Throwable t) {
        Log.v(TAG, msg, t);
    }
    public static void w(String msg, Throwable t) {
        Log.w(TAG, msg, t);
    }

    static String profileLabel;
    static long profileStart;

    public static void profile(String label)
    {
        profileLabel = label;
        profileStart = SystemClock.uptimeMillis();
    }

    public static void end()
    {
        if ( profileLabel != null ) {
            MiSpectacleLog.d("PROFILE " + profileLabel + ": " + (SystemClock.uptimeMillis() - profileStart) + "ms");
            profileLabel = null;
        }
    }
}
