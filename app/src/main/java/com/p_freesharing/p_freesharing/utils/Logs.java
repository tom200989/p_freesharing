package com.p_freesharing.p_freesharing.utils;

import android.util.Log;

public class Logs {
    private static int VERBOSE = 1;
    private static int DEBUG = 2;
    private static int INFO = 3;
    private static int WARN = 4;
    private static int ERROR = 5;
    private static int ASSERT = 6;

    private static int SHOW_ALL = 7;// 全部打开
    private static int STOP_ALL = 0;// 全部关闭

    private static int LOG_FLAG = SHOW_ALL;/* 日志开关 */
    private static String tag;
    private static Logs logs;

    public Logs() {
    }

    public static Logs t(String tag) {
        Logs.tag = tag;
        if (logs == null) {
            synchronized (Logs.class) {
                if (logs == null) {
                    logs = new Logs();
                }
            }
        }
        return logs;
    }

    public  void vv(String msg) {
        if (VERBOSE < LOG_FLAG) {
            Log.v(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (VERBOSE < LOG_FLAG) {
            Log.v(tag, msg);
        }
    }

    public  void dd(String msg) {
        if (DEBUG < LOG_FLAG) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG < LOG_FLAG) {
            Log.d(tag, msg);
        }
    }

    public  void ii(String msg) {
        if (INFO < LOG_FLAG) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (INFO < LOG_FLAG) {
            Log.i(tag, msg);
        }
    }

    public  void ww(String msg) {
        if (WARN < LOG_FLAG) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (WARN < LOG_FLAG) {
            Log.w(tag, msg);
        }
    }

    public  void ee(String msg) {
        if (ERROR < LOG_FLAG) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (ERROR < LOG_FLAG) {
            Log.e(tag, msg);
        }
    }

}
