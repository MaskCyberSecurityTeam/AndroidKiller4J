package com.richardtang.androidkiller4j.util;

import cn.hutool.core.util.ZipUtil;
import cn.hutool.log.StaticLog;
import org.apache.commons.compress.java.util.jar.Pack200;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.*;

/**
 * 压缩包工具类
 *
 * @author RichardTang
 */
@SuppressWarnings("all")
public class CompressUtils extends ZipUtil {

    // Pack200压包
    private static final Pack200.Packer packer = Pack200.newPacker();

    // Pack200解包
    private static final Pack200.Unpacker unpacker = Pack200.newUnpacker();

    /**
     * 对Jar包进行Pack200操作，保存的位置只需要写对应的根路径即可。
     * 默认保存的文件名为Jar包的名称.pack.gz。
     *
     * @param jarFile        需要进行Pack200的Jar包
     * @param outputRootPath Pack200之后输出的根位置
     * @return true:压缩成功 false:压缩失败
     */
    public static boolean packer(JarFile jarFile, String outputRootPath) {
        try {
            String outputPath = outputRootPath + jarFile.getName() + ".pack.gz";
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(new FileOutputStream(outputPath));
            packer.pack(jarFile, gzipOutputStream);
            jarFile.close();
            gzipOutputStream.close();
            return true;
        } catch (Exception e) {
            StaticLog.error(e);
        }
        return false;
    }

    /**
     * 使用Pack200进行解压
     *
     * @param unpackFilePath .pack.gz文件路径
     * @param outputRootPath 解压后的jar包输出的根路径
     * @return true:解压成功 false:解压失败
     */
    public static boolean unpacker(String unpackFilePath, String outputRootPath) {
        try {
            String outputPath = outputRootPath + FileUtils.getName(unpackFilePath).replace(".pack.gz", "") + ".jar";
            GZIPInputStream in = new GZIPInputStream(new FileInputStream(unpackFilePath));
            JarOutputStream out = new JarOutputStream(new FileOutputStream(outputPath));
            unpacker.unpack(in, out);
            out.close();
            in.close();
            return true;
        } catch (Exception e) {
            StaticLog.error(e);
        }
        return false;
    }

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
            StaticLog.error(e);
            result.add("ERROR:" + e.getMessage());
        }
        return result;
    }
}
