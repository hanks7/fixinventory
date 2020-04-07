package com.easyway.fixinventory.utils.http;


import com.easyway.fixinventory.base.BaseConstants;
import com.hanks.frame.utils.UToast;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @author 侯建军 47466436@qq.com
 * @date 2018/6/4
 * @description 请填写描述
 */
public class HttpAdapter {


    private static HttpApis service;

    private static OkHttpClient client = new OkHttpClient();

    public static void init() {
        try {
            client = client.newBuilder()
                    .addInterceptor(new MyHttpLogInter())
                    //处理多BaseUrl,添加应用拦截器
                    .addInterceptor(new MoreBaseUrlInterceptor())
                    .readTimeout(5, TimeUnit.SECONDS)
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BaseConstants.FORMAL_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            service = retrofit.create(HttpApis.class);
        } catch (Exception e) {
            UToast.showText("您输入的ip端口不符合规范");
        }
    }


    public static HttpApis getService() {
        if (service == null) {
            init();
        }
        return service;
    }


}
