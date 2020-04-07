package com.hanks.frame.http;


import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 侯建军 47466436@qq.com
 * @date 2018/6/4
 * @description 请填写描述
 *
 * com.easyway.mdsmclient.utils.http.MoreBaseUrlInterceptor
 */
public class MoreBaseUrlInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取原始的originalRequest
        Request originalRequest = chain.request();
        //获取老的url
        HttpUrl oldUrl = originalRequest.url();
        //获取originalRequest的创建者builder
        Request.Builder builder = originalRequest.newBuilder();

        //获取头信息
        String urlname = originalRequest.header("baseUrl");
        if (urlname != null) {
            //获取头信息中配置的value,如：manage或者mdffx
            HttpUrl baseURL = HttpUrl.parse(urlname);
            //重建新的HttpUrl，需要重新设置的url部分
            HttpUrl newHttpUrl = oldUrl.newBuilder()
                    .scheme(baseURL.scheme())//http协议如：http或者https
                    .host(baseURL.host())//主机地址
                    .port(baseURL.port())//端口
                    .build();
            //获取处理后的新newRequest
            Request newRequest = builder.url(newHttpUrl).build();
            return chain.proceed(newRequest);
        } else {
            return chain.proceed(originalRequest);
        }
    }

}

