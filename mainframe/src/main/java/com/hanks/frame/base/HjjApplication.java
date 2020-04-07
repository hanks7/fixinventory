package com.hanks.frame.base;

import android.app.Application;

/**
 * @author 侯建军
 * @data on 2018/1/10 10:56
 * @org www.hopshine.com
 * @function 请填写
 * @email 474664736@qq.com
 */

public class HjjApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static HjjApplication application;

    public static HjjApplication getInstance() {
        return application;
    }

}

