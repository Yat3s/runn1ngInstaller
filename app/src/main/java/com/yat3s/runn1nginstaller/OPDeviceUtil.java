package com.yat3s.runn1nginstaller;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by Yat3s on 11/14/15.
 * Email: yat3s@opentown.cn
 * Copyright (c) 2015 opentown. All rights reserved.
 */

public class OPDeviceUtil {

    /**
     * Returns device ID
     *
     * @return String
     */
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * Returns OS Version
     *
     * @return String
     */
    public static String getOsVersion() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    /**
     * Returns OS Version
     *
     * @return String
     */
    public static String getOsVersionName() {
        return String.valueOf(Build.VERSION.RELEASE);
    }

    /**
     * Returns "android"
     *
     * @return String
     */
    public static String getDeviceType() {
        return "Android";
    }

    /**
     * Returns Device model
     *
     * @return String
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * Returns Device manufacturer
     *
     * @return String
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getWIFILocalIpAddress() {

        //获取wifi服务
        WifiManager wifiManager = (WifiManager) App.getContext().getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = formatIpAddress(ipAddress);
        return ip;
    }

    private static String formatIpAddress(int ipAddress) {
        return (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                (ipAddress >> 24 & 0xFF);
    }

    /**
     * 获取MAC地址
     *
     * @return
     */
    public static String getMacAddress() {
        String macStr;
        WifiManager wifiManager = (WifiManager) App.getContext()
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getMacAddress() != null) {
            macStr = wifiInfo.getMacAddress();// MAC地址
        } else {
            macStr = "无Mac地址";
        }

        return macStr;
    }

    public static String getIMEI() {
        TelephonyManager telephonyManager=(TelephonyManager) App.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static String getHostIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr
                        .hasMoreElements();) {
                    InetAddress inetAddress = ipAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
}
