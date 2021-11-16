package com.richardtang.androidkiller4j.bean;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.setting.Setting;
import com.richardtang.androidkiller4j.constant.ResPath;
import com.richardtang.androidkiller4j.util.ControlUtil;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

/**
 * 配置文件
 *
 * @author RichardTang
 */
@Component
public class ApplicationProps extends Setting {

    /**
     * 构造中配置需要加载的properties文件，以及开启文件变化加载。
     */
    public ApplicationProps() {
        // 加载配置文件
        super(FileUtil.touch(ResPath.APPLICATION_PROPERTIES), CharsetUtil.CHARSET_UTF_8, true);

        // 配置文件变化时自动加载
        autoLoad(true);
    }

    /**
     * 获取应用名称
     *
     * @return 应用名称
     */
    public String getAppName() {
        return getStr("appName");
    }

    /**
     * 获取应用版本
     *
     * @return 应用版本
     */
    public String getAppVersion() {
        return getStr("appVersion");
    }

    /**
     * 获取应用标题
     *
     * @return 程序应用标题
     */
    public String getAppTitle() {
        return String.format("%s - %s", getAppName(), getAppVersion());
    }

    /**
     * 获取应用Icon名称
     *
     * @return Icon名称
     */
    public String getAppIconName() {
        return getStr("appVersion");
    }

    /**
     * 获取应用图标
     *
     * @return 应用图标
     */
    public ImageIcon getIconImage() {
        return ControlUtil.getImageIcon(getAppIconName(), 5, 5);
    }

    /**
     * 获取主视图窗口默认宽度
     *
     * @return 主视图窗口默认宽度
     */
    public Integer getMainWindowDefWidth() {
        return getInt("mainWindowDefWidth");
    }

    /**
     * 获取主视图窗口默认高度
     *
     * @return 主视图窗口默认高度
     */
    public Integer getMainWindowDefHeight() {
        return getInt("mainWindowDefHeight");
    }

    /**
     * 获取主视图窗口最小宽度
     *
     * @return 主视图窗口最小宽度
     */
    public Integer getMainWindowMinWidth() {
        return getInt("mainWindowMinWidth");
    }

    /**
     * 获取主视图窗口最小高度
     *
     * @return 主视图窗口最小高度
     */
    public Integer getMainWindowMinHeight() {
        return getInt("mainWindowMinHeight");
    }

    /**
     * 获取窗口默认大小，已Dimension方式返回。
     *
     * @return Dimension方式大小
     */
    public Dimension getMainDefSizeDimension() {
        return new Dimension(getMainWindowDefWidth(), getMainWindowDefHeight());
    }

    /**
     * 获取最小窗口大小，已Dimension方式返回。
     *
     * @return Dimension方式大小
     */
    public Dimension getMainMinSizeDimension() {
        return new Dimension(getMainWindowMinWidth(), getMainWindowMinHeight());
    }
}