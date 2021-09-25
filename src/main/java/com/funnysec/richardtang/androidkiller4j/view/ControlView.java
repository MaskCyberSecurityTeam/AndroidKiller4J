package com.funnysec.richardtang.androidkiller4j.view;

import com.funnysec.richardtang.androidkiller4j.constant.Icon;
import com.funnysec.richardtang.androidkiller4j.ui.ConsoleTextArea;
import com.funnysec.richardtang.androidkiller4j.util.FxUtil;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.Data;

/**
 * 控制台
 *
 * @author RichardTang
 */
@Data
public class ControlView extends IocView<TabPane> {

    // 日志输出组件
    private static ConsoleTextArea runLogTextArea;

    static {
        runLogTextArea = new ConsoleTextArea();

        // 将java的日志信息重定向到runLogTextArea组件中
//        System.setOut(runLogTextArea.getConsoleTextAreaPrintStream());
//        System.setErr(runLogTextArea.getConsoleTextAreaPrintStream());
    }

    // 日志Tab
    private Tab runLogTab;

    @Override
    protected void initUi() {
        runLogTab = FxUtil.getTab("日志", Icon.CONTROL_VIEW_LOG, "控制台日志", null);
    }

    @Override
    protected void initAttr() {
        runLogTab.setClosable(false);
        getRootPane().setSide(Side.BOTTOM);
    }

    @Override
    protected void initLayout() {
        // 避免TextArea里的内容过长，导致Tab覆盖住。
        runLogTab.setContent(runLogTextArea);
        getRootPane().getTabs().addAll(runLogTab);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initialize() {

    }

    /**
     * 清空控制台里的日志
     */
    public void clearLog() {
        runLogTextArea.clear();
    }
}
