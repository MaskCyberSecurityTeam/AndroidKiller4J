package com.richardtang.androidkiller4j.task.command;

import cn.hutool.log.StaticLog;
import com.richardtang.androidkiller4j.constant.Suffix;
import com.richardtang.androidkiller4j.setting.ApkShellSetting;
import com.richardtang.androidkiller4j.util.ControlUtils;
import com.richardtang.androidkiller4j.util.CompressUtils;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * APK查壳任务
 *
 * @author RichardTang
 */
public class ApkInspectShellTask extends SwingWorker<String, Void> {

    // 需要进行查壳的Apk文件对象
    private File apkFile;

    public ApkInspectShellTask(File apkFile) {
        this.apkFile = apkFile;
    }

    @Override
    protected String doInBackground() {
        // 判断文件后缀
        if (apkFile == null || !apkFile.getName().endsWith(Suffix.POINT_APK)) {
            return "此apk未采用加固或为未知加固厂商!";
        }
        // 判断压缩包内是否有文件
        List<String> filePath = CompressUtils.readZipFiles(apkFile);
        if (filePath.size() == 0 || filePath.get(0).startsWith("ERROR:")) {
            return "此apk未采用加固或为未知加固厂商!";
        }
        // 根据文件名在markMap中进行查找
        String key = filePath.stream().filter(s -> ApkShellSetting.getInstance().containsKey(s)).findFirst().orElse(null);
        return key != null ? ApkShellSetting.getInstance().getStr(key) : "此apk未采用加固或为未知加固厂商！";
    }

    @Override
    protected void done() {
        try {
            ControlUtils.showMsgDialog("查壳结果", get());
        } catch (Exception e) {
            StaticLog.error(e);
            ControlUtils.showMsgDialog("查壳结果", "此apk未采用加固或为未知加固厂商！");
        }
        super.done();
    }
}