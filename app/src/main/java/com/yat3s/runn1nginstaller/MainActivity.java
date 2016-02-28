package com.yat3s.runn1nginstaller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.run_btn)
    void install() {
        new Thread() {
            public void run() {
                Process process = null;
                OutputStream out = null;
                InputStream in = null;
                try {
                    // 请求root
                    process = Runtime.getRuntime().exec("su");
                    out = process.getOutputStream();
                    // 调用安装
                    out.write(("pm install -r " + "sdcard/xiami.apk" + "\n").getBytes());
                    in = process.getInputStream();
                    int len = 0;
                    byte[] bs = new byte[256];
                    while (-1 != (len = in.read(bs))) {
                        String state = new String(bs, 0, len);
                        Log.d("install", state);
                        if (state.equals("Success\n")) {
                            //安装成功后的操作
                            Log.d("install", "Success");
                            startAPP("fm.xiami.main");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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

    @OnClick(R.id.start_btn)
    void startApp() {
//        startAPP("com.opentown.open");
        CommandUtil.exe_command("0.sh");
//        try {
//            Process proc = Runtime.getRuntime().exec("0.sh");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @OnClick(R.id.stop_btn)
    void stopSh() {
        CommandUtil.exe_command("killall sh");
    }

    public void startAPP(String appPackageName){
        try{
            Intent intent = this.getPackageManager().getLaunchIntentForPackage(appPackageName);
            startActivity(intent);
        }catch(Exception e){
            Toast.makeText(this, "没有安装", Toast.LENGTH_LONG).show();
        }
    }
}
