package com.richardtang.androidkiller4j.view;

import com.richardtang.androidkiller4j.ui.control.PrintStreamTextArea;
import com.richardtang.androidkiller4j.ui.tabframe.TabFrameItem;
import com.richardtang.androidkiller4j.ui.tabframe.TabFramePanel;
import com.richardtang.androidkiller4j.util.ControlUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

/**
 * 控制台视图，也就是程序打开后最下边的选项卡。
 * 带有日志输出的文本域那一处
 *
 * @author RichardTang
 */
@Component
public class ConsoleView extends TabFramePanel implements InitializingBean {

    private TabFrameItem        logTabFrameItem;
    private PrintStreamTextArea printStreamTextArea;

    public ConsoleView() {
        printStreamTextArea = new PrintStreamTextArea();
        logTabFrameItem = new TabFrameItem(
                new JToggleButton("日志", ControlUtil.getSVGIcon("bug.svg")),
                new JScrollPane(printStreamTextArea)
        );
        addTabFrameItem(logTabFrameItem);
    }

    @Override
    public void afterPropertiesSet() {
//        System.setOut(printStreamTextArea.getPrintStream());
//        System.setErr(printStreamTextArea.getPrintStream());
    }
}