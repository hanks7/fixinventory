package com.hanks.frame.utils;

import android.os.Environment;

import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.hanks.frame.base.HjjApplication;

import java.io.File;

/**
 * @author 侯建军
 * @data on 2018/1/12 10:45
 * @org www.hopshine.com
 * @function 请填写
 * @email 474664736@qq.com
 */

public class Upath {
    public static String getAppCachePath() {
        String path = HjjApplication.getInstance().getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static String getDownLoadPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/hopshine/download";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }
}
