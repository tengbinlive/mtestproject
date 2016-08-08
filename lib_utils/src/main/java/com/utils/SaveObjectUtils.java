package com.utils;

import com.orhanobut.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by bin.teng on 6/29/16.
 */
public class SaveObjectUtils {

    public static String encodeObj(Object obj) {
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(obj);
            // 将字节流编码成base64的字符窜
            return Base64.encode(baos
                    .toByteArray());
        } catch (IOException e) {
            Logger.e(e.getLocalizedMessage());
            return "";
        }
    }

    public static Object decodeObj(String _objBase64) {
        Object obj = null;
        byte[] base64 = new byte[0];
        try {
            base64 = Base64.decode(_objBase64);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                //读取对象
                obj = bis.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
