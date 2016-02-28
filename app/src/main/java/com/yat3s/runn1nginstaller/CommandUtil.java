package com.yat3s.runn1nginstaller;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Yat3s on 2/27/16.
 * Email: yat3s@opentown.cn
 * Copyright (c) 2015 opentown. All rights reserved.
 */

public class CommandUtil {
    public static boolean execCommand(String cmd) {
        Process process = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            process = Runtime.getRuntime().exec("su");
            out = process.getOutputStream();
            in = process.getInputStream();
            out.write(cmd.getBytes());
            byte[] bs = new byte[256];
            Log.d("exeCommand", new String(bs, 0 ,in.read(bs)));
            process.waitFor();
            Log.d("exeCommand", "waitFor");

        } catch (Exception e) {
            return false;
        } finally {
            try {
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }

    public static void exe_command(String paramString)
    {
        try
        {
            String[] arrayOfString = new String[1];
            arrayOfString[0] = paramString;
            DataOutputStream localDataOutputStream = new DataOutputStream(Runtime.getRuntime().exec("su").getOutputStream());
            for (int i = 0; ; i++)
            {
                if (i >= 1)
                {
                    localDataOutputStream.writeBytes("exit\n");
                    localDataOutputStream.flush();
                    localDataOutputStream.wait();
                    return;
                }
                localDataOutputStream.writeBytes(arrayOfString[i] + "\n");
                localDataOutputStream.flush();
            }
        }
        catch (Exception localException)
        {
        }
    }
}
