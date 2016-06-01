package com.binteng.tools;

public class NumberUtils {
    public static int floatToIntBits(float value) {
        return Float.floatToIntBits(value);
    }

    public static int floatToRawIntBits(float value) {
        return Float.floatToRawIntBits(value);
    }

    public static int floatToIntColor(float value) {
        return Float.floatToRawIntBits(value);
    }

    public static float intToFloatColor(int value) {
        // This mask avoids using bits in the NaN range. See Float.intBitsToFloat javadocs.
        // This unfortunately means we don't get the full range of alpha.
        return Float.intBitsToFloat(value & 0xfeffffff);
    }

    public static float intBitsToFloat(int value) {
        return Float.intBitsToFloat(value);
    }

    public static long doubleToLongBits(double value) {
        return Double.doubleToLongBits(value);
    }

    public static double longBitsToDouble(long value) {
        return Double.longBitsToDouble(value);
    }
}
