package com.yat3s.runn1nginstaller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Yat3s on 10/13/15.
 * Email: yat3s@opentown.cn
 * Copyright (c) 2015 opentown. All rights reserved.
 */
public class OPBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Uri getRawResourceUri(int resId) {
        return Uri.parse("android.resource://" + getPackageName() + "/" + resId);
    }

    public void startActivity(Class<?> paramClass) {
        startActivity(paramClass, null);
    }

    public void startActivity(Class<?> paramClass, Bundle paramBundle) {
        Intent localIntent = new Intent();
        localIntent.setClass(this, paramClass);
        if (paramBundle != null) {
            localIntent.putExtras(paramBundle);
        }
        startActivity(localIntent);
    }

    public void showToast(String toastStr) {
        Toast toast = Toast.makeText(this, toastStr, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
