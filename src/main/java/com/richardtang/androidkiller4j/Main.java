package com.richardtang.androidkiller4j;

import com.formdev.flatlaf.FlatLightLaf;
import com.richardtang.androidkiller4j.config.SpringConfig;
import com.richardtang.androidkiller4j.window.MainWindow;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;

/**
 * AndroidKiller4J主类
 *
 * @author RichardTang
 */
public class Main {

    // Spring上下文
    public static AnnotationConfigApplicationContext context;

    static {
        // 配置Theme
        FlatLightLaf.setup();

        // 注解配置启动
        context = new AnnotationConfigApplicationContext(SpringConfig.class);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> context.getBean(MainWindow.class).showWindow());
    }
}
