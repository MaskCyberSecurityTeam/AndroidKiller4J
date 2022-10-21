package com.richardtang.androidkiller4j.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.log.StaticLog;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Yaml工具类
 *
 * @author RichardTang
 */
public class YamlUtils {

    private static final Yaml yaml = new Yaml();

    /**
     * Yaml文件配置转Map
     *
     * @param path 需要转换的yaml文件路径
     * @return 转换后的Map集合
     */
    public static Map<String, Object> fileToMap(String path) {
        return fileToMap(new File(path));
    }

    /**
     * Yaml文件配置转Map
     *
     * @param file 需要转换的yaml文件
     * @return 转换后的Map集合
     */
    public static Map<String, Object> fileToMap(File file) {
        if (FileUtil.isNotEmpty(file)) {
            try {
                return yaml.loadAs(new FileInputStream(file), Map.class);
            } catch (Exception e) {
                StaticLog.error(e);
            }
        }
        return new HashMap<>();
    }
}
