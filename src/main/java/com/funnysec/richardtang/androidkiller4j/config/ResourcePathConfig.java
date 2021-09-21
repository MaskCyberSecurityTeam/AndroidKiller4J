package com.funnysec.richardtang.androidkiller4j.config;

import cn.hutool.system.SystemUtil;
import com.sun.jna.Platform;

/**
 * 初始化获取项目WORK_DIR根路径，将根路径和所要用到的一些资源的文件夹拼接在一起。
 *
 * @author RichardTang
 */
public class ResourcePathConfig {

    // WORKDIR
    public static final String CURRENT_DIR = SystemUtil.getUserInfo().getCurrentDir();

    // 该路径下用来存放adb、apktool.jar
    public static final String BIN = CURRENT_DIR + "bin/";

    // 该路径下存放JavaFx的Css样式文件
    public static final String CSS = CURRENT_DIR + "css/";

    // 该路径下存放AndroidKiller4J这个项目的所用的图片
    public static final String IMAGE = CURRENT_DIR + "image/";

    // 该路径下存放应用的一些配置，如: 窗口名称、大小、icon图片的配置等
    public static final String CONFIG = CURRENT_DIR + "config/";

    // 该路径下存放用来处理的apk项目
    public static final String PROJECT = CURRENT_DIR + "project/";

    // 根据平台选择指定的ADB
    public static final String ADB_BIN = Platform.isWindows() ? BIN + "adb-win/adb.exe" : BIN + "adb-mac/adb";

    // 默认的keystore文件路径
    public static final String DEFAULT_KEYSTORE = CONFIG + "/androidkiller4j.keystore";

    // application.properties文件路径
    public static final String APPLICATION_PROPERTIES = CONFIG + "/application.properties";
}