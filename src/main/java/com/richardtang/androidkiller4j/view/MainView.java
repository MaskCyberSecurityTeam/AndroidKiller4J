package com.richardtang.androidkiller4j.view;

import com.richardtang.androidkiller4j.MainWindow;

import javax.swing.*;
import java.awt.*;

/**
 * 主视图
 *
 * @author RichardTang
 */
public final class MainView extends JPanel {

    public MainView() {
        setLayout(new BorderLayout());
        add(MainWindow.taskView, BorderLayout.CENTER);
        add(MainWindow.toolkitView, BorderLayout.NORTH);
        add(MainWindow.consoleView, BorderLayout.SOUTH);
    }
}