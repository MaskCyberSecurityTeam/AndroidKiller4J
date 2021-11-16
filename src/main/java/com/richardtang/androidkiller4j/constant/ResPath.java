package com.richardtang.androidkiller4j.constant;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.system.SystemUtil;

/**
 * 资源路径常量
 *
 * @author RichardTang
 */
public class ResPath {

    // 工作目录
    public static final String WORK_DIR = System.getProperty("user.dir") + "/";

    // 分别对应根目录下的子文件夹
    public static final String BIN_DIR         = WORK_DIR + "bin/";
    public static final String CONFIG_DIR      = WORK_DIR + "config/";
    public static final String PROJECT_DIR     = WORK_DIR + "project/";
    public static final String PROJECT_SRC_DIR = WORK_DIR + "project-src/";

    // 默认的keystore文件路径
    public static final String DEFAULT_KEYSTORE = CONFIG_DIR + "/androidkiller4j.keystore";

    // application.properties文件路径
    public static final String APPLICATION_PROPERTIES = CONFIG_DIR + "/application.properties";

    // 根据操作系统类型返回使用的adb路径
    public static final String ADB = BIN_DIR + "adb/" + (SystemUtil.getOsInfo().isWindows() ? "adb.exe" : "adb");

    // dex2jar工具
    public static final String DEX2JAR = BIN_DIR + "dex2jar/" + (SystemUtil.getOsInfo().isWindows() ? "dex2jar.bat" : "dex2jar");

    public static final String CRF = BIN_DIR + "cfr.jar";

    // mac系统下的terminal.sh脚本位置
    public static final String MAC_TERMINAL = ResourceUtil.getResource("script/terminal.sh").getPath();
}