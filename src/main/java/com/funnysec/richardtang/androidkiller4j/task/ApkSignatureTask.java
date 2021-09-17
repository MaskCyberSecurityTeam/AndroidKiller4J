package com.funnysec.richardtang.androidkiller4j.task;

import com.funnysec.richardtang.androidkiller4j.config.ResourcePathConfig;

/**
 * 调用jarsigner进行签名
 *
 * @author RichardTang
 */
public class ApkSignatureTask extends TerminalCommandTask {

    // 需要签名的apk路径
    private String signApkPath;

    // 签名后的存放路径
    private String apkDstPath;

    // 签名时需要使用的keystore文件名称
    private String keystoreName;

    /**
     * 使用jarsigner进行签名
     *
     * @param signApkPath  需要签名的apk路径
     * @param apkDstPath   签名后的存放路径
     * @param keystoreName 签名时需要使用的keystore文件名称
     */
    public ApkSignatureTask(String signApkPath, String apkDstPath, String keystoreName) {
        this.signApkPath  = signApkPath;
        this.apkDstPath   = apkDstPath;
        this.keystoreName = keystoreName;
    }

    /**
     * 具体的命令
     *
     * @return 命令
     */
    @Override
    protected String getCommand() {
        return String.format(
                "jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -storepass 123456 -keypass 123456 -keystore %s -signedjar %s %s AndroidKiller4J",
                ResourcePathConfig.CONFIG + keystoreName, apkDstPath, signApkPath
        );
    }
}
