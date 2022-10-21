package com.richardtang.androidkiller4j.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

/**
 * 文件工具类
 *
 * @author RichardTang
 */
public class FileUtils extends FileUtil {

    /**
     * 获取文件创建时间
     *
     * @param path 需要获取时间的文件路径
     * @return 文件创建时间
     */
    public static Date creationTime(String path) {
        return creationTime(file(path));
    }

    /**
     * 获取文件创建时间
     *
     * @param file 需要获取时间的文件
     * @return 文件创建时间
     */
    public static Date creationTime(File file) {
        BasicFileAttributes attributes = getAttributes(file.toPath(), false);
        return DateUtil.date(attributes.creationTime().toMillis()).toJdkDate();
    }

    /**
     * 获取存放在/resources/文件夹下的资源
     * 注意: name参数不要以/号开头
     *
     * @param name 相当于/resources/下的路径资源
     * @return 路径
     */
    public static URL getResource(String name) {
        return FileUtil.class.getClassLoader().getResource(name);
    }
}
