package com.richardtang.androidkiller4j.task.command;

import lombok.Builder;

/**
 * 证书生成任务
 *
 * @author RichardTang
 */
@Builder
public class CertGenerateTask extends TerminalCommandTask {

    // 生成证书所需的参数
    private String alias;
    private String keyPass;
    private String storePass;
    private String keyStore;
    private String keyAlg;
    private String keySize;
    private String validity;
    private String commonName;
    private String organization;
    private String organizationUnit;
    private String city;
    private String province;
    private String country;

    // 生成证书命令
    private final String COMMAND = "keytool -genkey -v -alias %s -keyalg %s -keysize %s -keypass %s -storepass %s -validity %s -keystore %s -dname CN=%s,OU=%s,O=%s,L=%s,ST=%s,C=%s";

    /**
     * 具体的命令
     *
     * @return 命令
     */
    @Override
    protected String getCommand() {
        System.out.println(String.format(COMMAND, alias, keyAlg, keySize, keyPass, storePass, validity, keyStore, commonName, organizationUnit, organization, city, province, country));
        return String.format(COMMAND, alias, keyAlg, keySize, keyPass, storePass, validity, keyStore, commonName, organizationUnit, organization, city, province, country);
    }
}
