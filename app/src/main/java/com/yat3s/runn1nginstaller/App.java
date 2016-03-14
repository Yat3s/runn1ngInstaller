package com.yat3s.runn1nginstaller;

import android.app.Application;
import android.content.Context;

/**
 * Created by Yat3s on 3/13/16.
 * Email: yat3s@opentown.cn
 * Copyright (c) 2015 opentown. All rights reserved.
 */

public class App extends Application {

    private static App mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }
}
