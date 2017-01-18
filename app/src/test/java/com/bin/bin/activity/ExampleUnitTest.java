package com.bin.bin.activity;

import android.util.Log;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    private final static String TAG = ExampleUnitTest.class.getSimpleName();

    private final static int MONTH = 12;

    private final static int FEBRUARY = 2;

    private final static int FEBRUARY_COUNT = 28;

    private final static int FEBRUARY_LEAP_COUNT = 29;

    private final static String BIG_MONTH = "@1@3@5@7@8@10@12";

    private final static int BIG_MONTH_COUNT = 31;

    private final static int SMALL_MONTH_COUNT = 30;

    private final static String STR_YEAR = "年";
    private final static String STR_MONTH = "月";
    private final static String STR_DAY = "日";

    private int showYearDetail(int year) {
        Log.e(TAG, year + STR_YEAR);
        for (int i = 0; i < MONTH; i++) {
            Log.e(TAG, i + STR_MONTH);
            int days = getDays(year, i + 1);
            for (int j = 0; j < days; j++) {
                Log.e(TAG, j + STR_DAY);
            }
        }
        return year;
    }

    private int getDays(int year, int month) {
        if (month == FEBRUARY) {
            return isLeapYear(year) ? FEBRUARY_LEAP_COUNT : FEBRUARY_COUNT;
        }
        if (isBigMonth(month)) {
            return BIG_MONTH_COUNT;
        }
        return SMALL_MONTH_COUNT;
    }

    private boolean isLeapYear(int year) {
        return year % 4 == 0 || year % 400 == 0;
    }

    private boolean isBigMonth(int month) {
        return BIG_MONTH.contains("@" + month);
    }


}