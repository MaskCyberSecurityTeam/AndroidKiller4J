package com.richardtang.androidkiller4j.ui.tabframe;

import com.formdev.flatlaf.icons.FlatTreeExpandedIcon;
import com.formdev.flatlaf.ui.FlatEmptyBorder;

import javax.swing.*;
import java.awt.*;

/**
 * 用于显示标题和用户自定义作为内容的组件，对应着Bar中每个Item选项点击后展开显示出来的面板。
 *
 * @author RichardTang
 */
public class TabFrameItemPanel extends JPanel {

    // 顶部标题
    private JLabel titleLabel;
    private JPanel headerPanel;

    // 标题栏处带的小按钮组
    private JToolBar headerToolBar;
    // 默认带的缩小按钮
    private JButton  minimizeButton;

    /**
     * 创建一个用于TabFrameItem中的内容面板
     *
     * @param titleLabel       显示的标题Label
     * @param contentComponent 显示的组件
     */
    public TabFrameItemPanel(JLabel titleLabel, JComponent contentComponent) {
        this.titleLabel = titleLabel;
        initializeHeader();

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(contentComponent, BorderLayout.CENTER);
    }

    /**
     * 初始化标题、小组件
     */
    private void initializeHeader() {
        // 标题label
        titleLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        titleLabel.setBorder(new FlatEmptyBorder(4, 4, 4, 4));

        // 缩小按钮初始化
        minimizeButton = new JButton(new FlatTreeExpandedIcon());

        // 初始化小工具栏
        headerToolBar = new JToolBar();
        headerToolBar.setFloatable(false);
        headerToolBar.add(minimizeButton);

        // 组合标题处的组件
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(headerToolBar, BorderLayout.EAST);
        headerPanel.setBorder(new MLineBorder(1, false, false, true, true));
    }

    public JLabel getTitleLabel() {
        return titleLabel;
    }

    public JPanel getHeaderPanel() {
        return headerPanel;
    }

    public JToolBar getHeaderToolBar() {
        return headerToolBar;
    }

    public JButton getMinimizeButton() {
        return minimizeButton;
    }
}