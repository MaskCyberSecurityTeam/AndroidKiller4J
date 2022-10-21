package com.richardtang.androidkiller4j.log;

import com.richardtang.androidkiller4j.MainWindow;

import java.util.logging.ConsoleHandler;

/**
 * 将日志数据流导向到ConsoleView组件中
 *
 * @author RichardTang
 */
public class UiConsoleHandler extends ConsoleHandler {

    public UiConsoleHandler() {
        super();
        setOutputStream(MainWindow.consoleView.getPrintStreamTextArea().getPrintStream());
    }
}