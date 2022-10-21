package com.richardtang.androidkiller4j.setting;

import cn.hutool.setting.Setting;
import com.richardtang.androidkiller4j.constant.R;

public class ApkShellSetting extends Setting {

    // 单利
    private static final ApkShellSetting instance = new ApkShellSetting();

    private ApkShellSetting() {
        super(R.APKSHELL_PROPERTIES);
        this.autoLoad(true);
    }

    public static ApkShellSetting getInstance() {
        return instance;
    }
}
