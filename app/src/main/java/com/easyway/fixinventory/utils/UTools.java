package com.easyway.fixinventory.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.PopupWindow;

import com.easyway.BarcodeObject;
import com.easyway.BarcodeSimple;
import com.easyway.BarcodeType;
import com.easyway.RefObject;
import com.easyway.fixinventory.base.BaseActivity;
import com.easyway.fixinventory.base.BaseApplication;
import com.easyway.fixinventory.base.IPPortBean;
import com.hanks.frame.utils.UToast;
import com.hanks.frame.utils.Ugson;
import com.hanks.frame.utils.Ulog;
import com.hanks.frame.utils.Usp;

import java.io.File;

/**
 * Created by admin on 2018/4/11.
 */

public class UTools {
    /**
     * @param view
     * @param activity 如果此activity是沉浸 置顶view需要添加paddingtop 状态栏高度
     *                 (注意paddingtop是状态栏高度，left right bottom 都是0；）此方法还需要改进
     */
    public static void addViewPaddingTop(View view, Activity activity) {
        if (activity instanceof BaseActivity && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
            view.setPadding(0, activity.getResources().getDimensionPixelSize(resourceId), 0, 0);
        }
    }

    /**
     * 从本地读取数据
     *
     * @return
     */
    public static IPPortBean getIPPortBean() {
        String json = (String) Usp.get(BaseApplication.getInstance(),IPPortBean.class.getSimpleName(), Ugson.toJson(new IPPortBean()));
        IPPortBean ipPortBean = Ugson.toBean(json, IPPortBean.class);
        Ulog.i("json", json);
        return ipPortBean;
    }



    public static int getBarCodeType(String strEditMain) {
        BarcodeObject bo = null;
        RefObject<BarcodeObject> barObject = new RefObject<>(bo);
        BarcodeSimple.Parse(strEditMain, barObject);
        bo = barObject.argvalue;
        Ugson.toJson(bo);
        if (bo.getBarcodeType() == BarcodeType.Primary) {//主码
            return 1;
        } else if (bo.getBarcodeType() == BarcodeType.Secondary) {//次码
            return 2;
        } else if (bo.getBarcodeType() == BarcodeType.Concatenated) {//组合码
            return 3;
        } else {
            UToast.showText("请联系管理员");
            return 4;
        }

    }

    /**
     * @param barCode
     * @return
     *
    "BarType": "R",
    "Barcode": "0108935221210810",
    "BarcodeControlSymbol": "'qy=pT,v22,R,",
    "BarcodeSrc": "'qy=pT,v22,R,0108935221210810",
    "Country": "越南",
    "ErrNo": 0,
    "MainCode": "0108935221210810",
    "Manufacturer": "52212",
    "Merchandise": "1081",
    "Package": "0",
    "RetryTimes": 0,
    "ScanerSN": "30002",
    "barcodeType": "Primary"
     *
    "BarType": "R",
    "Barcode": "1719050010170616V",
    "BarcodeControlSymbol": "~9~9v[\"cc&,R,",
    "BarcodeSrc": "~9~9v[\"cc&,R,1719050010170616V",
    "ErrNo": 0,
    "ExpirationDate": "2019-05-31",
    "Lot": "170616V",
    "RetryTimes": 0,
    "ScanerSN": "30002",
    "SubCode": "1719050010170616V",
    "barcodeType": "Secondary"    "BarType": "R",
    "Barcode": "1719050010170616V",
    "BarcodeControlSymbol": "~9~9v[\"cc&,R,",
    "BarcodeSrc": "~9~9v[\"cc&,R,1719050010170616V",
    "ErrNo": 0,
    "ExpirationDate": "2019-05-31",
    "Lot": "170616V",
    "RetryTimes": 0,
    "ScanerSN": "30002",
    "SubCode": "1719050010170616V",
    "barcodeType": "Secondary"
     */
    public static BarcodeObject getBarcodeObject(String barCode) {
        BarcodeObject bo = null;
        RefObject<BarcodeObject> barObject = new RefObject<>(bo);
        BarcodeSimple.Parse(barCode, barObject);
        bo = barObject.argvalue;
        Ugson.toJson(bo);
       return bo;

    }

    /**
     * 生成数据名称 完整路径
     *
     * @param context
     * @param dbName
     * @return
     */
    public static String getMySqliteName(Context context, String dbName) {

        boolean isSdCardEnble = false;
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {//SD卡是否存在
            isSdCardEnble = true;
        }
        String dbPath = "";
        if (isSdCardEnble) {
            dbPath = Environment.getExternalStorageDirectory().getPath() + "/com.easyway.mismclient/";
        } else {//不存在sd卡，存入内存中
            dbPath = context.getFilesDir().getPath() + "/com.easyway.mismclient/";
        }
        File file = new File(dbPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        Ulog.i("getMySqliteName", dbPath + dbName);
        return dbPath + dbName;
    }

    /**
     * 判断文件是否存在
     *
     * @param strFile
     * @return
     */
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;

    }

    /**
     *
     * @param pw     popupWindow
     * @param anchor v
     * @param xoff   x轴偏移
     * @param yoff   y轴偏移
     */
    public static void showAsDropDown2(final PopupWindow pw, final View anchor, final int xoff, final int yoff) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            pw.setHeight(height);
            pw.showAsDropDown(anchor, xoff, yoff);
        } else {
            pw.showAsDropDown(anchor, xoff, yoff);
        }
    }
}
