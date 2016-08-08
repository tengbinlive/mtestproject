package com.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by bin.teng on 6/29/16.
 */
public class ToastUtil {

    /**
     * 显示 长时 Toast
     *
     * @param msgId String resource id
     */
    public static void toast(Context context, int msgId) {
        Toast.makeText(context, msgId, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示 长时 Toast
     *
     * @param msg String
     */
    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示 短时 Toast
     *
     * @param msgId String resource id
     */
    public static void toastShort(Context context, int msgId) {
        Toast.makeText(context, msgId, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示 短时 Toast
     *
     * @param msg String
     */
    public static void toastShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示 Toast 自定义时间
     *
     * @param msgId String resource id
     */
    public static void toastDuration(Context context, int msgId, int duration) {
        Toast.makeText(context, msgId, duration).show();
    }

    /**
     * 显示 Toast 自定义时间
     *
     * @param msg String
     */
    public static void toastDuration(Context context, String msg, int duration) {
        Toast.makeText(context, msg, duration).show();
    }
}
