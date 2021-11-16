package com.richardtang.androidkiller4j.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 扩展Hutool中的压缩包工具类
 *
 * @author RichardTang
 */
public class ZipUtil extends cn.hutool.core.util.ZipUtil {

    /**
     * 读取压缩文件内的文件路径
     *
     * @param file 压缩文件格式的文件
     * @return 该压缩文件内的文件路径
     */
    public static List<String> readZipFiles(File file) {
        List<String> result = new ArrayList<>();
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)));
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    continue;
                }
                String path = zipEntry.getName();
                if (path.contains("/")) {
                    path = path.substring(path.lastIndexOf("/") + 1);
                }
                result.add(path);
            }
            zipInputStream.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
            result.add("ERROR:" + e.getMessage());
        }
        return result;
    }
}
