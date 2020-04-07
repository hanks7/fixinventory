package com.easyway.fixinventory.utils.http;

import android.text.TextUtils;

import com.hanks.frame.base.BaseModel;
import com.hanks.frame.base.DialogInterface;
import com.hanks.frame.utils.UToast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * @author 侯建军 47466436@qq.com
 * @date 2018/6/4
 * @description 由于BaseModel的数据主要存在AppendData中, 为了 不写BaseModel.getAppendData() ,所以才写了这个类.
 * 但是HttpApi 中还是需要 写BaseModel<T>作为传参泛型.
 */
public abstract class OnResponseListener2<T> implements Callback<BaseModel<T>> {
    DialogInterface infa;

    /**
     * @param infa
     */
    public OnResponseListener2(DialogInterface infa) {
        this.infa = infa;
        show();
    }

    /**
     * dialog隐藏
     */
    public void dismiss() {
        if (infa != null) {
            infa.dismissDialog();
        }
    }

    /**
     * dialog显示
     */
    public void show() {
        if (infa != null) {
            infa.showDialog();
        }
    }


    @Override
    public void onResponse(Call<BaseModel<T>> call, final Response<BaseModel<T>> bean) {

        dismiss();
        dealResponse(bean);

    }

    private void dealResponse(Response<BaseModel<T>> response) {

        if (response.code() == 200) {
            if (response.body().getResultType() == 0) {//getMessage()为 "空"表示 这次请求成功
                onSuccess(response.body().getAppendData());
            } else {
                onError(202, response.body().getMessage());
            }
        } else {
            String strError = "";
            try {
                strError += response.errorBody().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            onError(response.code(), strError);
        }
    }

    @Override
    public void onFailure(Call<BaseModel<T>> call, Throwable t) {
        dismiss();
        String tag = "error";
        String msg = t.getMessage();
        if (!TextUtils.isEmpty(msg)) {
            tag = tag + ":" + msg;
        }
        onError(444, tag);
    }


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