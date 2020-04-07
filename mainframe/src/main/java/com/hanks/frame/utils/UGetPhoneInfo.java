package com.hanks.frame.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by Administrator on 2016/7/7.
 */
public class UGetPhoneInfo {
    private TelephonyManager telephonyManager;
    /**
     * 国际移动用户识别码
     */
    private String IMSI;
    private Context context;

    public UGetPhoneInfo(Context context) {
        this.context = context;
        TelephonyManager   telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 获取电话号码
     */
    public String getNativePhoneNumber() {
        String NativePhoneNumber = null;
        NativePhoneNumber = telephonyManager.getLine1Number();
        //   UtilLog.i("获取电话号码", NativePhoneNumber);
        return NativePhoneNumber;
    }

    /**
     * 获取手机服务商信息
     */
    public String getProvidersName() {
        String ProvidersName = "N/A";
        try {
            IMSI = telephonyManager.getSubscriberId();
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
            System.out.println(IMSI);
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                ProvidersName = "中国移动";
            } else if (IMSI.startsWith("46001")) {
                ProvidersName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                ProvidersName = "中国电信";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //   UtilLog.i("获取手机服务商信息", ProvidersName);
        return ProvidersName;
    }

    public String getPhoneInfo() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        StringBuilder sb = new StringBuilder();
        sb.append(">>>DeviceId(IMEI) = " + tm.getDeviceId());
        sb.append(">>>DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
        sb.append(">>>Line1Number = " + tm.getLine1Number());
        sb.append(">>>NetworkCountryIso = " + tm.getNetworkCountryIso());
        sb.append(">>>NetworkOperator = " + tm.getNetworkOperator());
        sb.append(">>>NetworkOperatorName = " + tm.getNetworkOperatorName());
        sb.append(">>>NetworkType = " + tm.getNetworkType());
        sb.append(">>>PhoneType = " + tm.getPhoneType());
        sb.append(">>>SimCountryIso = " + tm.getSimCountryIso());
        sb.append(">>>SimOperator = " + tm.getSimOperator());
        sb.append(">>>SimOperatorName = " + tm.getSimOperatorName());
        sb.append(">>>SimSerialNumber = " + tm.getSimSerialNumber());
        sb.append(">>>SimState = " + tm.getSimState());
        sb.append(">>>SubscriberId(IMSI) = " + tm.getSubscriberId());
        sb.append(">>>VoiceMailNumber = " + tm.getVoiceMailNumber());
        //   UtilLog.i("getPhoneInfo", sb);
        return sb.toString();
    }

    /**
     * 获取手机型号
     */
    public String getStrPhoneModule() {
        //   UtilLog.i("获取手机型号", android.os.Build.MODEL);
        return android.os.Build.MODEL;
    }

    /**
     * 获取系统版本
     */
    public String getStrSystemType() {
        //   UtilLog.i("获取系统版本", android.os.Build.VERSION.RELEASE);
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取本机IMEI
     */
    public String getStringIMEI() {
        //   UtilLog.i("获取本机IMEI", telephonyManager.getDeviceId());
        return telephonyManager.getDeviceId();
    }

    /**
     * 获取本机IMSI
     */
    public String getStringIMSI() {
        //   UtilLog.i("获取本机IMSI", telephonyManager.getSubscriberId());

        TelephonyManager   telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSubscriberId();
    }


    /**
     * 根据busybox获取本地Mac
     */
    public String getLocalMacAddressFromBusybox() {
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig", "HWaddr");
        //如果返回的result == null，则说明网络不可取
        if (result == null) {
            return "网络异常";
        }
        //对该行数据进行解析
        //例如：eth0      Link encap:Ethernet  HWaddr 00:16:E8:3E:DF:67
        if (result.length() > 0 && result.contains("HWaddr") == true) {
            Mac = result.substring(result.indexOf("HWaddr") + 6, result.length() - 1);
            result = Mac;
            //   UtilLog.i("根据busybox获取本地Mac",result);
        }

        return result;
    }

    private String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            while ((line = br.readLine()) != null && line.contains(filter) == false) {
                result += line;
            }

            result = line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据wifi信息获取本地mac
     *
     * @return
     */
    public String getLocalMacAddressFromWifiInfo() {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo winfo = wifi.getConnectionInfo();
        String mac = winfo.getMacAddress();
        return mac;
    }

    /**
     * 获取本地IP
     *
     * @return
     */
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        //   UtilLog.i("获取本地IP",inetAddress.getHostAddress().toString());
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }


    /**
     * 根据IP获取本地Mac
     *
     * @return
     */
    public String getLocalMacAddressFromIp() {
        String mac_s = "";
        try {
            byte[] mac;
            NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress.getByName(getLocalIpAddress()));
            mac = ne.getHardwareAddress();
            mac_s = byte2hex(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //   UtilLog.i("根据IP获取本地Mac", mac_s);
        return mac_s;
    }


    /**
     * 二进制转十六进制
     *
     * @param b
     * @return
     */
    public String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs = hs.append("0").append(stmp);
            else {
                hs = hs.append(stmp);
            }
        }
        return String.valueOf(hs);
    }

}
