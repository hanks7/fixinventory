package com.easyway.fixinventory.utils;

import android.util.Base64;

/**
 * Created by admin on 2017/11/20.
 */

public class UtilBase64 {



    //加密
    public static String encode(String str) {
        byte[] encodeBytes = Base64.encode(str.getBytes(), Base64.DEFAULT);
        return new String(encodeBytes);
    }

    //解密
    public static String decode(String str) {
        byte[] decodeBytes = Base64.decode(str, Base64.DEFAULT);
        return new String(decodeBytes);
    }
}
