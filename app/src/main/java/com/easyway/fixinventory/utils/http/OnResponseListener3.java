package com.easyway.fixinventory.utils.http;

import com.hanks.frame.utils.UToast;


/**
 * @author 侯建军 47466436@qq.com
 * @date 2018/6/4
 * @description 给数据库使用的回调
 */
public abstract class OnResponseListener3<T> {



    /**
     * 请求成功时返回，必须重写此方法
     *
     * @param t
     */
    public abstract void onSuccess(T t);

    /**
     * 请求失败时返回，需要时重写此方法(默认是弹出吐司)
     */
    public void onError(int code, String strToast) {
        UToast.showText(strToast);
    }


}