package com.yat3s.runn1nginstaller;

import java.io.DataOutputStream;

/**
 * Created by Yat3s on 2/27/16.
 * Email: yat3s@opentown.cn
 * Copyright (c) 2015 opentown. All rights reserved.
 */

public class CommandUtil {
    public static boolean execCommand(String cmd) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
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
