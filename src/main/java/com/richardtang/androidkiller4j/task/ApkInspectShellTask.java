package com.richardtang.androidkiller4j.task;

import com.richardtang.androidkiller4j.util.ControlUtil;
import com.richardtang.androidkiller4j.util.ZipUtil;

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

    // 用于村粗规则的Map
    private final static Map<String, String> markMap = new HashMap<>();

    // 壳规则 TODO 后边提取到配置文件
    static {
        markMap.put("libchaosvmp.so", "娜迦");
        markMap.put("libddog.so", "娜迦");
        markMap.put("libfdog.so", "娜迦");
        markMap.put("libedog.so", "娜迦企业版");
        markMap.put("libexec.so", "爱加密、腾讯");
        markMap.put("libexecmain.so", "爱加密");
        markMap.put("ijiami.dat", "爱加密");
        markMap.put("ijiami.ajm", "爱加密企业版");
        markMap.put("libsecexe.so", "梆梆免费版");
        markMap.put("libsecmain.so", "梆梆免费版");
        markMap.put("libSecShell.so", "梆梆免费版");
        markMap.put("libDexHelper.so", "梆梆企业版");
        markMap.put("libDexHelper-x86.so", "梆梆企业版");
        markMap.put("libprotectClass.so", "360");
        markMap.put("libjiagu.so", "360");
        markMap.put("libjiagu_art.so", "360");
        markMap.put("libjiagu_x86.so", "360");
        markMap.put("libegis.so", "通付盾");
        markMap.put("libNSaferOnly.so", "通付盾");
        markMap.put("libnqshield.so", "网秦");
        markMap.put("libbaiduprotect.so", "百度");
        markMap.put("aliprotect.dat", "阿里聚安全");
        markMap.put("libsgmain.so", "阿里聚安全");
        markMap.put("libsgsecuritybody.so", "阿里聚安全");
        markMap.put("libmobisec.so", "阿里聚安全");
        markMap.put("libtup.so", "腾讯");
        markMap.put("libshell.so", "腾讯");
        markMap.put("mix.dex", "腾讯");
        markMap.put("lib/armeabi/mix.dex", "腾讯");
        markMap.put("lib/armeabi/mixz.dex", "腾讯");
        markMap.put("libtosprotection.armeabi.so", "腾讯御安全");
        markMap.put("libtosprotection.armeabi-v7a.so", "腾讯御安全");
        markMap.put("libtosprotection.x86.so", "腾讯御安全");
        markMap.put("libnesec.so", "网易易盾");
        markMap.put("libAPKProtect.so", "APKProtect");
        markMap.put("libkwscmm.so", "几维安全");
        markMap.put("libkwscr.so", "几维安全");
        markMap.put("libkwslinker.so", "几维安全");
        markMap.put("libx3g.so", "顶像科技");
        markMap.put("libapssec.so", "盛大");
        markMap.put("librsprotect.so", "瑞星");
    }

    public ApkInspectShellTask(File apkFile) {
        this.apkFile = apkFile;
    }

    @Override
    protected String doInBackground() {
        // 判断文件后缀
        if (apkFile == null || !apkFile.getName().endsWith(".apk")) {
            return "此apk未采用加固或为未知加固厂商!";
        }

        // 判断压缩包内是否有文件
        List<String> filePath = ZipUtil.readZipFiles(apkFile);
        if (filePath.size() <= 0 || filePath.get(0).startsWith("ERROR:")) {
            return "此apk未采用加固或为未知加固厂商!";
        }
        // 根据文件名在markMap中进行查找
        String key = filePath.stream().filter(markMap::containsKey).findFirst().orElse(null);
        return key != null ? markMap.get(key) : "此apk未采用加固或为未知加固厂商！";
    }

    @Override
    protected void done() {
        try {
            ControlUtil.showMsgDialog("查壳结果", get());
        } catch (Exception e) {
            ControlUtil.showMsgDialog("查壳结果", "此apk未采用加固或为未知加固厂商！");
        }
        super.done();
    }
}