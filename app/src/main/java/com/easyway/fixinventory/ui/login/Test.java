package com.easyway.fixinventory.ui.login;

/**
 * @author 侯建军 QQ:474664736
 * @time 2019/10/23 11:27
 * @class describe
 */
public class Test {
    public static void main(String[] args) {
        String strHRCode = "admin     ";
        show(strHRCode.length());
        int t = 12 - strHRCode.length();
        if (strHRCode.length() < 12) {
            for (int i = 0; i < t; i++) {
                strHRCode = 0 + strHRCode;

            }
        }
        show(strHRCode);
    }

    private static void show(Object strHRCode) {
        System.out.println(strHRCode);
    }
}
