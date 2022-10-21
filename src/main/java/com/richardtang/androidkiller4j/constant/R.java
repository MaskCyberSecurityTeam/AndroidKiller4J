package com.richardtang.androidkiller4j.constant;

import cn.hutool.system.SystemUtil;
import com.formdev.flatlaf.util.SystemInfo;
import com.richardtang.androidkiller4j.util.FileUtils;

import java.io.File;

/**
 * 资源类
 *
 * @author RichardTang
 */
public interface R {

    // 工作目录
    String WORK_DIR = System.getProperty("user.dir") + File.separator;

    // 分别对应根目录下的子文件夹
    String BIN_DIR         = WORK_DIR + "bin" + File.separator;
    String LOGS_DIR        = WORK_DIR + "logs" + File.separator;
    String CONFIG_DIR      = WORK_DIR + "config" + File.separator;
    String PROJECT_DIR     = WORK_DIR + "project" + File.separator;
    String PROJECT_SRC_DIR = WORK_DIR + "projectSrc" + File.separator;

    // 默认的keystore文件路径
    String DEFAULT_KEYSTORE = CONFIG_DIR + File.separator + "androidkiller4j.keystore";

    // application.properties文件路径
    String APPLICATION_PROPERTIES = CONFIG_DIR + File.separator + "application.properties";

    // apkShell.properties文件路径
    String APKSHELL_PROPERTIES = CONFIG_DIR + File.separator + "apkShell.properties";

    // cfr java反编译工具
    String CFR = BIN_DIR + "cfr.jar";

    // dex2jar工具
    String DEX2JAR = BIN_DIR + "dex2jar" + File.separator + (SystemInfo.isWindows ? "dex2jar.bat" : "dex2jar");

    // mac系统下的terminal.sh脚本位置
    String MAC_TERMINAL = BIN_DIR + File.separator + "terminal.sh";

    // adb程序
    String ADB = getAdbByPlatform();

    // Java主程序
    String JAVA = getJavaBinByPlatform();

    static String getAdbByPlatform() {
        String adb = BIN_DIR + "adb" + File.separator;
        if (SystemInfo.isWindows) {
            adb += "windows.exe";
        } else if (SystemInfo.isMacOS) {
            adb += "macos";
        } else {
            adb += "linux";
        }
        return adb;
    }

    static String getJavaBinByPlatform() {
        String java = SystemUtil.getJavaRuntimeInfo().getHomeDir() + File.separator + "bin" + File.separator;
        if (SystemInfo.isWindows) {
            java += "java.exe";
        } else {
            java += "java";
        }
        return java;
    }
}