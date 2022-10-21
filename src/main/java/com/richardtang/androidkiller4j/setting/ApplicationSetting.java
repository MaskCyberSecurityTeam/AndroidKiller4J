package com.richardtang.androidkiller4j.setting;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.Setting;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.richardtang.androidkiller4j.constant.R;

import java.awt.*;

/**
 * application.properties配置文件类
 *
 * @author RichardTang
 */
public class ApplicationSetting {

    // 应用信息
    public static final String APP_NAME    = "AndroidKiller4J";
    public static final String APP_VERSION = "1.0.0";

    // 应用配置
    private Integer mainWindowDefWidth;
    private Integer mainWindowDefHeight;
    private Integer mainWindowMinWidth;
    private Integer mainWindowMinHeight;
    private String  applicationTheme;

    // 环境配置
    private String javaBinPath;

    // 对应配置文件中的key值
    private static final String JAVA_BIN_PATH_KEY          = "java.bin.path";
    private static final String APPLICATION_THEME_KEY      = "application.theme";
    private static final String MAIN_WINDOW_DEF_WIDTH_KEY  = "main.window.def.width";
    private static final String MAIN_WINDOW_DEF_HEIGHT_KEY = "main.window.def.height";
    private static final String MAIN_WINDOW_MIN_WIDTH_KEY  = "main.window.min.width";
    private static final String MAIN_WINDOW_MIN_HEIGHT_KEY = "main.window.min.height";

    // 默认和最小 窗口高度宽度 默认值
    private static final Integer MAIN_WINDOWS_DEF_WIDTH_DEF_VALUE  = 1200;
    private static final Integer MAIN_WINDOWS_DEF_HEIGHT_DEF_VALUE = 850;
    private static final Integer MAIN_WINDOWS_MIN_WIDTH_DEF_VALUE  = 700;
    private static final Integer MAIN_WINDOWS_MIN_HEIGHT_DEF_VALUE = 500;

    // Properties操作类
    private final Setting setting = new Setting(R.APPLICATION_PROPERTIES);

    // 单利
    private static final ApplicationSetting instance = new ApplicationSetting();

    private ApplicationSetting() {
        this.setting.autoLoad(true);
        this.javaBinPath = this.setting.getStr(JAVA_BIN_PATH_KEY);
        this.applicationTheme = this.setting.getStr(APPLICATION_THEME_KEY);
        this.mainWindowMinHeight = this.setting.getInt(MAIN_WINDOW_MIN_HEIGHT_KEY);
        this.mainWindowMinWidth = this.setting.getInt(MAIN_WINDOW_MIN_WIDTH_KEY);
        this.mainWindowDefHeight = this.setting.getInt(MAIN_WINDOW_DEF_HEIGHT_KEY);
        this.mainWindowDefWidth = this.setting.getInt(MAIN_WINDOW_DEF_WIDTH_KEY);
    }

    public static ApplicationSetting getInstance() {
        return instance;
    }

    /**
     * 获取主视图窗口默认宽度
     *
     * @return 主视图窗口默认宽度
     */
    public Integer getMainWindowDefWidth() {
        if (mainWindowDefWidth == null) {
            mainWindowDefWidth = MAIN_WINDOWS_DEF_WIDTH_DEF_VALUE;

        }
        return mainWindowDefWidth;
    }

    /**
     * 设置窗口默认宽度值
     *
     * @param width 宽度
     */
    public void setMainWindowDefWidth(Integer width) {
        this.mainWindowDefWidth = width;
        this.setting.put(MAIN_WINDOW_DEF_WIDTH_KEY, String.valueOf(width));
    }

    /**
     * 获取主视图窗口默认高度
     *
     * @return 主视图窗口默认高度
     */
    public Integer getMainWindowDefHeight() {
        if (mainWindowDefHeight == null) {
            mainWindowDefHeight = MAIN_WINDOWS_DEF_HEIGHT_DEF_VALUE;
        }
        return mainWindowDefHeight;
    }

    /**
     * 设置窗口默认高度值
     *
     * @param height 高度
     */
    public void setMainWindowDefHeight(Integer height) {
        this.mainWindowDefHeight = height;
        this.setting.put(MAIN_WINDOW_DEF_HEIGHT_KEY, String.valueOf(height));
    }

    /**
     * 获取主视图窗口最小宽度
     *
     * @return 主视图窗口最小宽度
     */
    public Integer getMainWindowMinWidth() {
        if (ObjectUtil.isEmpty(mainWindowMinWidth)) {
            mainWindowMinWidth = MAIN_WINDOWS_MIN_WIDTH_DEF_VALUE;
        }
        return mainWindowMinWidth;
    }

    /**
     * 设置窗口最小宽度
     *
     * @param width 宽度
     */
    public void setMainWindowMinWidth(Integer width) {
        this.mainWindowMinWidth = width;
        this.setting.put(MAIN_WINDOW_MIN_WIDTH_KEY, String.valueOf(width));
    }

    /**
     * 获取主视图窗口最小高度
     *
     * @return 主视图窗口最小高度
     */
    public Integer getMainWindowMinHeight() {
        if (ObjectUtil.isEmpty(mainWindowMinHeight)) {
            mainWindowMinHeight = MAIN_WINDOWS_MIN_HEIGHT_DEF_VALUE;
        }
        return mainWindowMinHeight;
    }

    /**
     * 设置窗口最小高度值
     *
     * @param height 高度
     */
    public void setMainWindowMinHeight(Integer height) {
        this.mainWindowMinHeight = height;
        this.setting.put(MAIN_WINDOW_MIN_HEIGHT_KEY, String.valueOf(height));
    }

    /**
     * 获取窗口默认大小，已Dimension方式返回。
     *
     * @return Dimension方式大小
     */
    public Dimension getMainDefSizeDimension() {
        Integer width = getMainWindowDefWidth();
        Integer height = getMainWindowDefHeight();
        return new Dimension(width, height);
    }

    /**
     * 获取最小窗口大小，已Dimension方式返回。
     *
     * @return Dimension方式大小
     */
    public Dimension getMainMinSizeDimension() {
        return new Dimension(getMainWindowMinWidth(), getMainWindowMinHeight());
    }

    /**
     * 获取Java主程序路径配置
     *
     * @return Java主程序路径
     */
    public String getJavaBinPath() {
        if (StrUtil.isEmpty(javaBinPath)) {
            javaBinPath = R.JAVA;
        }
        return javaBinPath;
    }

    /**
     * 设置java主程序路径
     *
     * @param javaBinPath java主程序路径
     */
    public void setJavaBinPath(String javaBinPath) {
        this.javaBinPath = javaBinPath;
        this.setting.put(JAVA_BIN_PATH_KEY, javaBinPath);
    }

    public String getApplicationTheme() {
        if (StrUtil.isEmpty(applicationTheme)) {
            applicationTheme = FlatLightLaf.class.getName();
        }
        return applicationTheme;
    }

    public void setApplicationTheme(Class<? extends FlatLaf> applicationThemeClass) {
        this.applicationTheme = applicationThemeClass.getName();
        this.setting.put(APPLICATION_THEME_KEY, applicationTheme);
    }

    /**
     * 存储至配置文件
     */
    public void store() {
        this.setting.store();
    }
}