package com.richardtang.androidkiller4j.constant;

/**
 * 用到的一些证书信息
 *
 * @author RichardTang
 */
public interface Cert {

    // 生成证书文件的后缀
    String CERT_FILE_SUFFIX = ".keystore";

    // 所有生成的证书所使用的密码和别名都指定成这个
    String CERT_PASS_ALIAS = "AndroidKiller4J";
}