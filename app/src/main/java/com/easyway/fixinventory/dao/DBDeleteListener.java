package com.easyway.fixinventory.dao;

/**
 * @author 侯建军 47466436@qq.com
 * @class com.tjstudy.wifidemo.listener.WifiListener
 * @time 2018/10/25 15:25
 * @description 请填写描述
 */
public interface DBDeleteListener {
    void onStart();

    /**
     * @param code     200 表示成功过,其他表示失败
     */
    void onResponse(int code);
}
