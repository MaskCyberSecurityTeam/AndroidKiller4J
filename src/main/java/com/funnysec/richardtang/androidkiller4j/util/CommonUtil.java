package com.funnysec.richardtang.androidkiller4j.util;

public class CommonUtil {

    public static boolean isWindows() {
        return System.getProperties().getProperty("os.name").toUpperCase().contains("WINDOWS");
    }
}
