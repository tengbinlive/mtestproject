package com.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by bin.teng on 6/29/16.
 */
public class ImageUtils {

    private ImageUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * convert Bitmap to byte array
     *
     * @param b
     * @return
     */
    private static byte[] bitmapToByte(Bitmap b) {
        if (b == null) {
            return null;
        }

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, o);
        return o.toByteArray();
    }

    /**
     * convert byte array to Bitmap
     *
     * @param b
     * @return
     */
    private static Bitmap byteToBitmap(byte[] b) {
        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * convert Drawable to Bitmap
     *
     * @param d
     * @return
     */
    private static Bitmap drawableToBitmap(Drawable d) {
        return d == null ? null : ((BitmapDrawable) d).getBitmap();
    }

    /**
     * @param b
     * @return
     * @deprecated convert Bitmap to Drawable
     */
    private static Drawable bitmapToDrawable(Bitmap b) {
        //noinspection deprecation
        return b == null ? null : new BitmapDrawable(b);
    }

    /**
     * convert Drawable to byte array
     *
     * @param d
     * @return
     */
    public static byte[] drawableToByte(Drawable d) {
        return bitmapToByte(drawableToBitmap(d));
    }

    /**
     * convert byte array to Drawable
     *
     * @param b
     * @return
     */
    public static Drawable byteToDrawable(byte[] b) {
        //noinspection deprecation
        return bitmapToDrawable(byteToBitmap(b));
    }

    /**
     * 根据尺寸缩放图像
     * scale image
     *
     * @param org
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
        return scaleImage(org, (float) newWidth / org.getWidth(), (float) newHeight / org.getHeight());
    }

    /**
     * 根据倍数缩放图像
     * scale image
     *
     * @param org
     * @param scaleWidth  scale of width
     * @param scaleHeight scale of height
     * @return
     */
    private static Bitmap scaleImage(Bitmap org, float scaleWidth, float scaleHeight) {
        if (org == null) {
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(), matrix, true);
    }

    /**
     * 根据图像路径返回图像的旋转角度
     *
     * @param imageFile
     * @return
     */
    public static int getExifRotation(File imageFile) {
        if (imageFile == null) {
            return 0;
        } else {
            try {
                ExifInterface e = new ExifInterface(imageFile.getAbsolutePath());
                switch (e.getAttributeInt("Orientation", 0)) {
                    case 3:
                        return 180;
                    case 6:
                        return 90;
                    case 8:
                        return 270;
                    default:
                        return 0;
                }
            } catch (IOException var2) {
                return 0;
            }
        }
    }
}
