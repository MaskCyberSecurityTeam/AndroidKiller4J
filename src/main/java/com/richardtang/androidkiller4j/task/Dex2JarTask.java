package com.richardtang.androidkiller4j.task;

import cn.hutool.core.io.FileUtil;
import com.richardtang.androidkiller4j.constant.ResPath;

/**
 * 使用dex2jar组件，将apk中的dex文件转换成jar文件。
 *
 * @author RichardTang
 */
public class Dex2JarTask extends TerminalCommandTask {

    private String apkPath;

    private String outputPath;

    public Dex2JarTask(String apkPath, String outputPath) {
        this.apkPath    = apkPath;
        this.outputPath = outputPath;
    }

    @Override
    protected String getCommand() {
        String fileName = FileUtil.getName(apkPath).replace(".apk", "");
        return String.format("%s %s -o %s%s.jar", ResPath.DEX2JAR, apkPath, outputPath, fileName);
    }
}
