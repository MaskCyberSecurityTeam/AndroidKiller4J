package com.funnysec.richardtang.androidkiller4j.core;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RuntimeUtil;
import com.funnysec.richardtang.androidkiller4j.config.ResourcePathConfig;
import com.funnysec.richardtang.androidkiller4j.util.IoUtil;
import javafx.application.Platform;

import java.io.*;
import java.util.function.Consumer;

/**
 * 通过命令行的方式使用apktool.jar
 * 主要是执行完apktool.jar的操作之后进程结束可以立马释放内存
 *
 * @author RichardTang
 */
public class ApkTool {

    // apktool 回编译命令
    private static final String COMPILER_COMMAND = "java -jar bin/apktool.jar b %s/";

    // apktool 解包命令
    private static final String DECOMPILER_COMMAND = "java -jar bin/apktool.jar d %s -f -o %s";

    // 签名命令
    private static final String SIGNATURE_COMMAND = "jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -storepass 123456 -keypass 123456 -keystore %s -signedjar %s %s AndroidKiller4J";

    /**
     * 打包APK，回编译。
     *
     * @param apkRootPath 需要打包的apk工程根路径
     * @param callback    回调函数
     */
    public static void compile(String apkRootPath, Consumer<Process> callback) {
        String command = String.format(COMPILER_COMMAND, apkRootPath);
        execute(command, callback);
    }

    /**
     * 解包，反编译APK，调用ApkTool.jar工具包。
     *
     * @param apkPath 需要反编译的apk文件
     * @param output  反编译的结果输出到哪个目录
     */
    public static void decompiler(String apkPath, String output, Consumer<Process> callback) {
        String command = String.format(DECOMPILER_COMMAND, apkPath, output);
        execute(command, callback);
    }

    /**
     * apk签名
     *
     * @param apkPath  需要签名的apk文件路径
     * @param dstPath  签名过后输出的路径
     * @param callback 回调函数
     */
    public static void signature(String apkPath, String dstPath, String keystoreName, Consumer<Process> callback) {
        String command = String.format(SIGNATURE_COMMAND, ResourcePathConfig.CONFIG + keystoreName, dstPath, apkPath);
        execute(command, callback);
    }

    /**
     * 启动一个线程执行命令
     *
     * @param command  要执行的命令
     * @param callback 回调函数
     */
    private static void execute(String command, Consumer<Process> callback) {
        ThreadUtil.execAsync(() -> {
            try {
                Process process = RuntimeUtil.exec(command);
                // 重定向process日志流
                Platform.runLater(() -> IoUtil.inputStreamToSysOut(process.getInputStream()));
                // 回调函数
                callback.accept(process);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}