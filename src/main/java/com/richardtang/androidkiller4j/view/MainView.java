package com.richardtang.androidkiller4j.view;

import com.richardtang.androidkiller4j.view.task.TaskView;
import com.richardtang.androidkiller4j.view.toolkit.ToolkitView;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

/**
 * 主视图
 *
 * @author RichardTang
 */
@Data
@Component
public class MainView extends JPanel implements InitializingBean {

    // 任务视图
    @Autowired
    private TaskView taskView;

    // 工具栏视图
    @Autowired
    private ToolkitView toolkitView;

    // 控制台视图
    @Autowired
    private ConsoleView consoleView;

    @Override
    public void afterPropertiesSet() {
        setLayout(new BorderLayout());
        add(toolkitView, BorderLayout.NORTH);
        add(taskView, BorderLayout.CENTER);
        add(consoleView, BorderLayout.SOUTH);
    }
}