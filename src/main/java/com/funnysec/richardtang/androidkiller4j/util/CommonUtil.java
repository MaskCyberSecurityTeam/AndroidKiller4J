package com.funnysec.richardtang.androidkiller4j.util;

import java.io.File;

public class CommonUtil {

    public static String getFileSuffix(File file) {
        int lastIndexOf = file.getName().lastIndexOf(".");
        return file.getName().substring(lastIndexOf);
    }

    public static boolean isWindows() {
        return System.getProperties().getProperty("os.name").toUpperCase().contains("WINDOWS");
    }
}
