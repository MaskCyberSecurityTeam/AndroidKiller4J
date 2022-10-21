package com.richardtang.androidkiller4j.util;

import cn.hutool.core.io.FileUtil;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.richardtang.androidkiller4j.constant.Size;
import com.richardtang.androidkiller4j.constant.Suffix;
import com.richardtang.androidkiller4j.ui.balloon.LeftAbovePositionerWrapper;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.EdgedBalloonStyle;
import net.java.balloontip.utils.TimingUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.LinkedHashMap;

/**
 * Ui工具类，简化SwingUi代码的调用。
 *
 * @author RichardTang
 */
public class ControlUtils {

    public static FileNameExtensionFilter apkFilter = new FileNameExtensionFilter("APK文件", Suffix.APK);

    // 用于缓存icon图标
    private static final LinkedHashMap<String, Icon> systemIconContainer = new LinkedHashMap<>();

    // FlatLaf组件颜色
    public static final Color COMPONENT_BORDER_COLOR     = UIManager.getColor("Component.borderColor");
    public static final Color COMPONENT_BACKGROUND_COLOR = UIManager.getColor("TextField.background");

    // BalloonTip组件样式
    public static final BalloonTipStyle BTIP_STYLE = new EdgedBalloonStyle(COMPONENT_BACKGROUND_COLOR, COMPONENT_BORDER_COLOR);

    /**
     * 单个值的输入框提示
     *
     * @param msg 提示消息
     * @return 输入框中输入的值
     */
    public static String singleInputDialog(String msg) {
        return (String) JOptionPane.showInputDialog(null, msg, "提示窗口", JOptionPane.PLAIN_MESSAGE, null, null, null);
    }

    /**
     * 只选.apk后缀的文件
     *
     * @return apk后缀的文件对象
     */
    public static File chooserApkFileDialog() {
        return chooserFileDialog(apkFilter);
    }

    /**
     * 不过滤文件后缀，可选择所有文件。
     *
     * @return 选中的文件对象
     */
    public static File chooserFileDialog() {
        return chooserFileDialog(null);
    }

    /**
     * 根据后缀排除掉一些文件并选择需要操作的文件。
     *
     * @param extensionFilter 需要过滤的文件后缀
     * @return 选中的文件对象
     */
    public static File chooserFileDialog(FileNameExtensionFilter extensionFilter) {
        JFileChooser fileChooser = new JFileChooser();
        if (extensionFilter != null) {
            fileChooser.setFileFilter(extensionFilter);
        }
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
        JFileChooser fileChooser = new JFileChooser();
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
    }

    /**
     * 获取一个图片对象,图片高宽为20。
     *
     * @param path 图片路径
     * @return 图片对象
     */
    public static ImageIcon getImageIcon(String path) {
        return getImageIcon(path, Size.BIG);
    }

    /**
     * 获取一个图片对象,可设置图片大小。
     *
     * @param path 图片路径
     * @param wh   图片显示宽度和高度
     * @return 图片对象
     */
    public static ImageIcon getImageIcon(String path, int wh) {
        return getImageIcon(path, wh, wh);
    }

    /**
     * 获取一个图片对象,可设置图片大小。
     *
     * @param url    图片路径
     * @param width  图片显示宽度
     * @param height 图片显示高度
     * @return 图片对象
     */
    public static ImageIcon getImageIcon(URL url, int width, int height) {
        return scaleImageIcon(new ImageIcon(url), width, height);
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
        return scaleImageIcon(new ImageIcon(path), width, height);
    }

    /**
     * 根据高宽缩放图片
     *
     * @param icon   需要缩放的图片
     * @param width  图片显示宽度
     * @param height 图片显示高度
     * @return 缩放后的图片
     */
    public static ImageIcon scaleImageIcon(ImageIcon icon, int width, int height) {
        icon.setImage(icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        return icon;
    }

    /**
     * 获取一个SVG图片对象,图片高宽为20。
     *
     * @param path 图片路径
     * @return 图片对象
     */
    public static FlatSVGIcon getSVGIcon(String path) {
        return getSVGIcon(path, Size.BIG);
    }

    /**
     * 获取一个SVG图片对象,指定相同的宽度和高度值。
     *
     * @param path 图片路径
     * @param wh   图片显示的宽度和高度值
     * @return 图片对象
     */
    public static FlatSVGIcon getSVGIcon(String path, int wh) {
        return getSVGIcon(path, wh, wh);
    }

    /**
     * 获取一个SVG图片对象,可设置图片大小。
     *
     * @param path   图片路径
     * @param width  图片显示宽度
     * @param height 图片显示高度
     * @return 图片对象
     */
    public static FlatSVGIcon getSVGIcon(String path, int width, int height) {
        return new FlatSVGIcon("image/" + path, width, height);
    }

    /**
     * 获取该文件的操作系统图标
     *
     * @param file 需要获取图标的文件
     * @return 文件对应的图标
     */
    public static Icon getSysIcon(File file) {
        // 从缓存中查找
        String suffix = FileUtil.getSuffix(file.getName());
        Icon icon = systemIconContainer.get(suffix);

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

    /**
     * 获取一个JLabel包装的SVG图片对象, 指定相同的宽度和高度值。
     *
     * @param path 图片路径
     * @param wh   图片显示的宽度和高度值
     * @return 图片对象
     */
    public static JLabel getLabelIcon(String path, int wh) {
        return new JLabel(ControlUtils.getSVGIcon(path, wh));
    }

    /**
     * 获取一个JLabel包装的SVG图片对象, 图片高宽为20。
     *
     * @param path 图片路径
     * @return 图片对象
     */
    public static JLabel getLabelIcon(String path) {
        return getLabelIcon(path, Size.BIG);
    }

    /**
     * 获取一个左布局的FlowLayout
     *
     * @return 左布局的FlowLayout
     */
    public static FlowLayout getLeftFlowLayout(int gap) {
        return new FlowLayout(FlowLayout.LEFT, gap, gap);
    }

    /**
     * 创建图标按钮
     *
     * @param text    按钮文字
     * @param svgName 图标名称
     * @param wh      按钮大小
     * @return 带有图标的按钮
     */
    public static JButton getIconButton(String text, String svgName, int wh) {
        return new JButton(text, getSVGIcon(svgName, wh));
    }

    /**
     * 创建图标按钮 - 中等大小
     *
     * @param text    按钮文字
     * @param svgName 图标名称
     * @return 带有图标的按钮
     */
    public static JButton getMediumIconButton(String text, String svgName) {
        return getIconButton(text, svgName, Size.MEDIUM);
    }

    /**
     * 创建用于提示信息的Tip组件
     *
     * @param component 需要附加tip的组件
     * @param text      提示的文本内容
     * @return BTip组件
     */
    public static BalloonTip getBTip(JComponent component, String text) {
        return new BalloonTip(component, new JLabel(text), BTIP_STYLE, BalloonTip.Orientation.LEFT_ABOVE, BalloonTip.AttachLocation.ALIGNED, 10, 10, false);
    }

    /**
     * 创建用于提示信息的Tip组件，指定时间后消失。
     *
     * @param component 需要附加tip的组件
     * @param text      提示的文本内容
     * @param time      多少毫秒后消失
     * @return 带时间消失的BTip组件
     */
    public static BalloonTip getTimeBTip(JComponent component, String text, int time) {
        BalloonTip bTip = getBTip(component, text);
        bTip.setPositioner(new LeftAbovePositionerWrapper(5));
        TimingUtils.showTimedBalloon(bTip, time);
        return bTip;
    }
}