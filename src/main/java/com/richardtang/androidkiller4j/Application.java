package com.richardtang.androidkiller4j;

import cn.hutool.log.StaticLog;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.util.SystemInfo;
import com.richardtang.androidkiller4j.setting.ApplicationSetting;
import com.richardtang.androidkiller4j.util.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.*;

/**
 * AndroidKiller4J
 *
 * @author RichardTang
 */
public class Application {

    static {
        // UI相关属性，需要在加载窗口出来之前就优先加载。

        if (SystemInfo.isMacOS) {
            // 设置应用名称
            System.setProperty("apple.awt.application.name", ApplicationSetting.APP_NAME);
            // 设置Mac下跟随系统色调
            System.setProperty("apple.awt.application.appearance", "system");

            // 设置Dock图标
            URL logoUrl = FileUtils.getResource("image/logo.png");
            Taskbar.getTaskbar().setIconImage(new ImageIcon(logoUrl).getImage());
        }

        // 设置语言
        Locale.setDefault(Locale.CHINA);

        // 根据配置文件加载主题
        try {
            String applicationTheme = ApplicationSetting.getInstance().getApplicationTheme();
            FlatLaf flatLafTheme = (FlatLaf) Class.forName(applicationTheme).getDeclaredConstructor().newInstance();
            FlatLaf.setup(flatLafTheme);
        } catch (Exception e) {
            StaticLog.error("配置文件application.theme值有误!");
            FlatLightLaf.setup();
        }

        // 对所有的PasswordField组件启用密码或隐藏功能
        UIManager.put("PasswordField.showRevealButton", true);
    }

    public static void main(String[] args) {
        // 启动加载界面
        SwingUtilities.invokeLater(() -> new LoadWindow().start());
    }
}