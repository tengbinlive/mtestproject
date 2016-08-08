package com.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * Created by bin.teng on 6/29/16.
 */
public class ManifestUtils {

    private ManifestUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本code信息]
     *
     * @param context
     * @return 当前应用的版本Code
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 在Activity中获取MetaData
     *
     * @param context
     * @param key
     * @return Object
     */
    public static Object getMetaDataAsObject(Activity context, String key) {
        ActivityInfo info = getActivityInfo(context);
        return info == null ? null : info.metaData.get(key);
    }

    /**
     * 在Activity中获取MetaData
     *
     * @param context
     * @param key
     * @return String
     */
    public static String getMetaDataAsString(Activity context, String key) {
        ActivityInfo info = getActivityInfo(context);
        return info == null ? null : info.metaData.getString(key);
    }

    /**
     * 在Activity中获取MetaData
     *
     * @param context
     * @param key
     * @return int
     */
    public static int getMetaDataAsInt(Activity context, String key) {
        ActivityInfo info = getActivityInfo(context);
        return info == null ? null : info.metaData.getInt(key);
    }

    /**
     * 在Activity中获取MetaData
     *
     * @param context
     * @param key
     * @return boolean
     */
    public static boolean getMetaDataAsBoolean(Activity context, String key) {
        ActivityInfo info = getActivityInfo(context);
        return info == null ? null : info.metaData.getBoolean(key);
    }


    private static ActivityInfo getActivityInfo(Activity context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getActivityInfo(context.getComponentName(),
                    PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
