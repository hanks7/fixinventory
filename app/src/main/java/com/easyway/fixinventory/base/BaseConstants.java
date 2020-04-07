package com.easyway.fixinventory.base;


import com.hanks.frame.utils.Ugson;
import com.hanks.frame.utils.Ulog;
import com.hanks.frame.utils.Usp;

/**
 * Created by admin on 2018/4/8.
 */
public class BaseConstants {
    /**
     * 是否测试 1-测试 0-上线  并不是在这里改,是在assets/appConfig中修改isTest
     */
    public static final boolean IS_RELEASE = true;
    /**
     * ip端口号
     * eg: http://172.16.1.81:7000
     * 116.247.74.76:8682  319271  外网
     */
    public static String FORMAL_URL = getString();//ip_端口号
    /**
     * 此应用存放文件的文件夹名称
     */
    public static String APP_INDEX = "easyway";

    /**
     * 处理從本地获取IP端口号
     * "http://172.16.1.127:8090"
     *
     * @return
     */
    private static String getString() {
        String json = (String) Usp.get(BaseApplication.getInstance(), IPPortBean.class.getSimpleName(), Ugson.toJson(new IPPortBean()));
        IPPortBean ipPortBean = Ugson.toBean(json.replace(" ", ""), IPPortBean.class);
        String strIpPort = ipPortBean.getIpPort().replace(" ", "");
        Ulog.i("getIPPort", strIpPort);
        if (!strIpPort.contains("http://")) {
            strIpPort = "http://" + strIpPort;
        }
        Ulog.i("getIPPort", strIpPort);
        return strIpPort;
    }
}
