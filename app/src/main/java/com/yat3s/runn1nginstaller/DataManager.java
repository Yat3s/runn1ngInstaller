package com.yat3s.runn1nginstaller;

/**
 * Created by Yat3s on 3/13/16.
 * Email: yat3s@opentown.cn
 * Copyright (c) 2015 opentown. All rights reserved.
 */

public class DataManager {
    private static final String KEY_INSTALL_AMOUNT  = "install_amount";

    public static void saveInstallAmount(int amount) {
        CacheUtil.putData(KEY_INSTALL_AMOUNT, amount);
    }

    public static int getInstallAmount() {
        return (int) CacheUtil.getData(KEY_INSTALL_AMOUNT, 0);
    }
}
