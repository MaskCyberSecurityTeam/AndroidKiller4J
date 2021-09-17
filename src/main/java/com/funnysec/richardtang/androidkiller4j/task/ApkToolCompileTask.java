package com.funnysec.richardtang.androidkiller4j.task;

/**
 * 使用apktool进行回编译、打包。
 * 需要回编译的APK工程根路径需要是已apktool进行解包的文件夹才能进行回编译。
 *
 * @author RichardTang
 */
public class ApkToolCompileTask extends TerminalCommandTask {

    // 需要回编译的APK工程根路径
    private String apkRootPath;

    /**
     * 使用apktool进行回编译任务
     *
     * @param apkRootPath 需要回编译的APK工程根路径
     */
    public ApkToolCompileTask(String apkRootPath) {
        this.apkRootPath = apkRootPath;
    }

    /**
     * 具体的命令
     *
     * @return 命令
     */
    @Override
    protected String getCommand() {
        return String.format("java -jar bin/apktool.jar b %s/", apkRootPath);
    }
}
