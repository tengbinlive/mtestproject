package com.binteng.tools;

import android.content.Context;
import android.widget.Toast;

import com.binteng.App;


/**
 * 通用工具类.
 *
 * @author bin.teng
 */
public class CommonUtil {

    private CommonUtil() {
    }

    private static void showToast(Context context, String msg, int time) {
        Toast mToast = Toast.makeText(context, msg, time);
        mToast.show();
    }

    public static void showToast(String msg) {
        showToast(App.getInstance(), msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(int resId) {
        showToast(App.getInstance(), App.getInstance().getResources().getString(resId), Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getResources().getString(resId), Toast.LENGTH_SHORT);
    }


}
