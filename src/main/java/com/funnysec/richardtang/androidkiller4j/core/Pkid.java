package com.funnysec.richardtang.androidkiller4j.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * APP查壳工具
 *
 * @author RichardTang
 */
public class Pkid {

    public static Map<String, String> markMap = new HashMap<>();

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

    /**
     * 读取压缩文件内的文件路径
     *
     * @param file 压缩文件格式的文件
     * @return 该压缩文件内的文件路径
     */
    public static List<String> readZipFiles(File file) {
        List<String> result = new ArrayList<>();
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)));
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    continue;
                }
                String path = zipEntry.getName();
                if (path.contains("/")) {
                    path = path.substring(path.lastIndexOf("/") + 1);
                }
                result.add(path);
            }
            zipInputStream.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
            result.add("ERROR:" + e.getMessage());
        }
        return result;
    }

    /**
     * 对指定APK文件进行查壳
     *
     * @param apkFile 需要查壳的apk文件
     * @return 壳所有厂商的名称
     */
    public static String check(File apkFile) {
        // 判断文件后缀
        if (apkFile == null || !apkFile.getName().endsWith(".apk")) {
            return "此apk未采用加固或为未知加固厂商!";
        }

        // 判断压缩包内是否有文件
        List<String> filePath = readZipFiles(apkFile);
        if (filePath.size() <= 0 || filePath.get(0).startsWith("ERROR:")) {
            return "此apk未采用加固或为未知加固厂商!";
        }

        // 根据文件名在markMap中进行查找
        String key = filePath.stream().filter(i -> markMap.containsKey(i)).findFirst().orElse(null);
        return key != null ? markMap.get(key) : "此apk未采用加固或为未知加固厂商！";
    }
}