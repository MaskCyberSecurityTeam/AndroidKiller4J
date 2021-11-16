package com.richardtang.androidkiller4j.task;

/**
 * 调用apktool工具对apk进行解包操作
 * foo: java -jar bin/apktool.jar d xx.apk -f -o /tmp/xx/
 *
 * @author RichardTang
 */
public class ApkToolDecompileTask extends TerminalCommandTask {

    // 需要解包的apk路径
    private String apkPath;

    // 解包后输出的位置
    private String outputPath;

    private final String COMMAND = "java -jar bin/apktool.jar d %s -f -o %s";

    /**
     * 使用apktool进行解包任务
     *
     * @param apkPath    需要解包的apk路径
     * @param outputPath 解包后输出的位置
     */
    public ApkToolDecompileTask(String apkPath, String outputPath) {
        this.apkPath    = apkPath;
        this.outputPath = outputPath;
    }

    /**
     * 具体的命令
     *
     * @return 命令
     */
    @Override
    protected String getCommand() {
        return String.format(COMMAND, apkPath, outputPath);
    }
}
