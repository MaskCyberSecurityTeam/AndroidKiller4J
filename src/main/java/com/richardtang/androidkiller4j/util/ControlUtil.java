package com.richardtang.androidkiller4j.util;

import cn.hutool.core.io.FileUtil;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.LinkedHashMap;

/**
 * 控件工具类，简化代码调用。
 *
 * @author RichardTang
 */
public class ControlUtil {

    public static JFileChooser            fileChooser = new JFileChooser();
    public static FileNameExtensionFilter apkFilter   = new FileNameExtensionFilter("APK文件", "apk");

    // 用于缓存icon图标
    private static final LinkedHashMap<String, Icon> systemIconContainer = new LinkedHashMap<>();

    /**
     * 只选.apk后缀的文件
     *
     * @return apk后缀的文件对象
     */
    public static File chooserApkFileDialog() {
        return chooserFileDialog(apkFilter);
    }

    /**
     * 根据后缀排除掉一些文件并选择需要操作的文件。
     *
     * @param extensionFilter 需要过滤的文件后缀
     * @return 选中的文件对象
     */
    public static File chooserFileDialog(FileNameExtensionFilter extensionFilter) {
        fileChooser.setFileFilter(extensionFilter);
//        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    /**
     * 选择需要保存的文件夹路径
     *
     * @return 需要保存到的文件夹对象
     */
    public static File chooserSaveDirectoryDialog() {
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    /**
     * 显示一个消息弹窗
     *
     * @param title 消息标题
     * @param msg   显示的消息
     */
    public static void showMsgDialog(String title, String msg) {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
//        JOptionPane.showOptionDialog(null, msg, title, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
    }

    /**
     * 获取一个图片对象,可设置图片大小。
     *
     * @param path   图片路径
     * @param width  图片显示宽度
     * @param height 图片显示高度
     * @return 图片对象
     */
    public static ImageIcon getImageIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        icon.setImage(icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        return icon;
    }

    /**
     * 获取一个图片对象,图片高宽为20。
     *
     * @param path 图片路径
     * @return 图片对象
     */
    public static ImageIcon getImageIcon(String path) {
        return getImageIcon(path, 20, 20);
    }

    /**
     * 获取一个SVG图片对象,可设置图片大小。
     *
     * @param path   图片路径
     * @param width  图片显示宽度
     * @param height 图片显示高度
     * @return 图片对象
     */
    public static Icon getSVGIcon(String path, int width, int height) {
        return new FlatSVGIcon("image/" + path, width, height);
    }

    /**
     * 获取一个SVG图片对象,图片高宽为20。
     *
     * @param path 图片路径
     * @return 图片对象
     */
    public static Icon getSVGIcon(String path) {
        return getSVGIcon(path, 20, 20);
    }

    /**
     * 获取该文件的操作系统图标
     *
     * @param file 需要获取图标的文件
     * @return 文件对应的图标
     */
    public static Icon getSysIcon(File file) {
        // 从缓存中查找
        String suffix = FileUtil.getSuffix(file);
        Icon   icon   = systemIconContainer.get(suffix);

        // 缓存中不存在对应数据
        if (icon == null) {
            // 文件夹的情况，不带后缀、
            if (file.isDirectory()) {
                suffix = "directory";
            }
            // 存入缓存中
            systemIconContainer.put(suffix, FileSystemView.getFileSystemView().getSystemIcon(file));
        }

        return systemIconContainer.get(suffix);
    }

    /**
     * 获取该文件的操作系统图标
     *
     * @param filePath 需要获取图标的文件路径
     * @return 文件对应的图标
     */
    public static Icon getSysIcon(String filePath) {
        return getSysIcon(new File(filePath));
    }
}
