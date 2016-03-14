package com.yat3s.runn1nginstaller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nononsenseapps.filepicker.FilePickerActivity;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends OPBaseActivity {
    private static final int FILE_SELECT_CODE = 0x100;
    @Bind(R.id.install_count_today_tv)
    TextView installCountTodayTv;
    @Bind(R.id.install_amount_tv)
    TextView installAmountTv;
    @Bind(R.id.duration_tv)
    TextView durationTv;
    @Bind(R.id.efficiency_tv)
    TextView efficiencyTv;
    @Bind(R.id.current_model_tv)
    TextView currentModelTv;
    @Bind(R.id.current_imei_tv)
    TextView currentImeiTv;
    @Bind(R.id.current_ip_tv)
    TextView currentIpTv;
    @Bind(R.id.current_mac_tv)
    TextView currentMacTv;
    @Bind(R.id.loading)
    AVLoadingIndicatorView loading;
    @Bind(R.id.run_text_tv)
    TextView runTextTv;
    @Bind(R.id.file_path_tv)
    TextView filePathTv;
    @Bind(R.id.run_btn)
    FrameLayout runBtn;
    @Bind(R.id.log_tv)
    TextView logTv;

    private int mInstallAmount;
    private boolean isRunning;

    private TimerTask mTimerTask;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        mInstallAmount = DataManager.getInstallAmount();
        installAmountTv.setText("总安装量: " + mInstallAmount);
        currentModelTv.setText("当前手机型号: " + OPDeviceUtil.getDeviceModel() + "/" + OPDeviceUtil.getManufacturer());
        currentIpTv.setText("当前IP: " + OPDeviceUtil.getWIFILocalIpAddress());
        currentMacTv.setText("当前MAC地址: " + OPDeviceUtil.getMacAddress());
        currentImeiTv.setText("当前IMEI/MEID: " + OPDeviceUtil.getIMEI());
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (isRunning) {
                    loading.setVisibility(View.GONE);
                    runTextTv.setVisibility(View.VISIBLE);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            logTv.setText("正在安装第" + (mInstallAmount + 1) + "次....");
                            loading.setVisibility(View.VISIBLE);
                            runTextTv.setVisibility(View.GONE);
                        }
                    });
                    CoolUtil.install("xiami", new CoolUtil.OnInstallListener() {
                        @Override
                        public void onInstallSuccess() {
                            mInstallAmount++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    logTv.setText("安装完成，正在打开");
                                    installAmountTv.setText("总安装量: " + mInstallAmount);
                                }
                            });
                            CoolUtil.startAPP(MainActivity.this, "fm.xiami.main");
                            runShell();
                        }
                    });
                }
                isRunning = !isRunning;
            }
        };
    }

    @OnClick(R.id.run_btn)
    void go() {
        mTimer.schedule(mTimerTask, 1000);
    }

    void startApp() {
        CoolUtil.startAPP(this, "com.opentown.open");
    }

    void stopSh() {
        CoolUtil.uninstall("fm.xiami.main", new CoolUtil.OnUninstallListener() {
            @Override
            public void onUninstallSuccess() {

            }
        });
    }

    private void runShell() {
        String cmd = "sleep 4 \n" +
                " input tap 500 500 \n" +
                " sleep 2 \n" +
                " input keyevent KEYCODE_BACK \n" +
                " sleep 2 \n" +
                " input swipe 100 1000 100 100\n" +
                " sleep 2 \n" +
                " input keyevent KEYCODE_BACK \n";
        CoolUtil.execShellCommand(cmd, null);
    }

    @OnClick(R.id.select_file_btn)
    void showFileChooser() {
        // This always works
        Intent i = new Intent(this, FilePickerActivity.class);
        // This works if you defined the intent filter
        // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

        // Set these depending on your use case. These are the defaults.
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

        // Configure initial directory by specifying a String.
        // You could specify a String like "/storage/emulated/0/", but that can
        // dangerous. Always use Android's API calls to get paths to the SD-card or
        // internal memory.
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

        startActivityForResult(i, FILE_SELECT_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    if (!data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                        Uri uri = data.getData();
                        filePathTv.setText(uri.getPath());
                    } else {
                        Uri uri = data.getData();
                        // Do something with the URI
                    }
                }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        DataManager.saveInstallAmount(mInstallAmount);
    }
}
