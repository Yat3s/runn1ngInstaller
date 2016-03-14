package com.yat3s.runn1nginstaller;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Yat3s on 2/27/16.
 * Email: yat3s@opentown.cn
 * Copyright (c) 2015 opentown. All rights reserved.
 */

public class CoolUtil {
    private static final String RESULT_SUCCESS = "Success\n";

    public static void install(final String apkName, final OnInstallListener installListener) {
        execShellCommand("pm install -r "  + "sdcard/" + apkName + ".apk", new OnRunCommandListener() {
            @Override
            public void onExecSuccess(String resultStr) {
                if (TextUtils.equals(RESULT_SUCCESS, resultStr)) {
                    if (null != installListener) {
                        installListener.onInstallSuccess();
                    }
                }
            }
        });
    }

    public static void uninstall(final String packageName, final OnUninstallListener uninstallListener) {
        execShellCommand("pm uninstall " + packageName, new OnRunCommandListener() {
            @Override
            public void onExecSuccess(String resultStr) {
                if (TextUtils.equals(RESULT_SUCCESS, resultStr)) {
                    if (null != uninstallListener) {
                        uninstallListener.onUninstallSuccess();
                    }
                }
            }
        });
    }

    public static void startAPP(Context context, String appPackageName) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "没有安装", Toast.LENGTH_LONG).show();
        }
    }

    public static void execShellCommand(final String commandStr, final OnRunCommandListener runCommandListener) {
        new Thread() {
            public void run() {
                Process process;
                OutputStream out = null;
                InputStream in = null;
                try {
                    // 请求root
                    process = Runtime.getRuntime().exec("su");
                    out = process.getOutputStream();
                    // 调用安装
                    out.write((commandStr + "\n").getBytes());
                    in = process.getInputStream();
                    int len;
                    byte[] bs = new byte[256];
                    while (-1 != (len = in.read(bs))) {
                        String state = new String(bs, 0, len);
                        Log.d("Yat3s.onExecSuccess-->", state);
                        if (null != runCommandListener) {
                            runCommandListener.onExecSuccess(state);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.flush();
                            out.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public static void execCommand(String paramString) {
        try {
            String[] arrayOfString = new String[1];
            arrayOfString[0] = paramString;
            DataOutputStream localDataOutputStream = new DataOutputStream(Runtime.getRuntime().exec("su").getOutputStream());
            for (int i = 0; ; i++) {
                if (i >= 1) {
                    localDataOutputStream.writeBytes("exit\n");
                    localDataOutputStream.flush();
                    localDataOutputStream.wait();
                    return;
                }
                localDataOutputStream.writeBytes(arrayOfString[i] + "\n");
                localDataOutputStream.flush();
            }
        } catch (Exception localException) {
        }
    }

    public static boolean isRoot() {
        boolean bool = false;
        try {
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
                bool = false;
            } else {
                bool = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }

    interface OnInstallListener {
        void onInstallSuccess();
    }

    interface OnUninstallListener {
        void onUninstallSuccess();
    }

    interface OnRunCommandListener {
        void onExecSuccess(String resultStr);
    }
}
