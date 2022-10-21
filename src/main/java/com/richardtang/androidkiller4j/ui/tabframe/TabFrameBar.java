package com.richardtang.androidkiller4j.ui.tabframe;

import com.formdev.flatlaf.util.UIScale;
import com.richardtang.androidkiller4j.ui.border.MLineBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * 工具条，TabFrame最底下的功能条目。
 *
 * @author RichardTang
 */
public class TabFrameBar extends JPanel {

    private JToolBar toolBar = new JToolBar();

    // 存储ToolBar中的按钮
    private ArrayList<JToggleButton> toolBarItems = new ArrayList<>();

    /**
     * 创建一个TabFramePanel中的ToolBar
     *
     * @param barItems 需要添加到ToolBar中的按钮
     */
    public TabFrameBar(JToggleButton... barItems) {
        toolBar.setRollover(true);
        toolBar.setFloatable(false);
        toolBar.setBorder(new MLineBorder(-1, false, false, false, false));

        for (JToggleButton barItem : barItems) {
            addBarItem(barItem);
        }

        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.WEST);
        setBorder(new MLineBorder(1, false, false, true, true));
    }

    /**
     * 向TabFrameBar中的toolBar添加按钮
     *
     * @param barItem 需要添加的item按钮
     */
    public void addBarItem(JToggleButton barItem) {
        barItem.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        barItem.setBorder(new EmptyBorder(UIScale.scale(4), UIScale.scale(10), UIScale.scale(4), UIScale.scale(10)));
        toolBar.add(barItem);
        this.toolBarItems.add(barItem);
    }

    /**
     * 获取ToolBar中存储的所有JToggleButton按钮
     *
     * @return 存储toolBar中按钮的集合
     */
    public ArrayList<JToggleButton> getToolBarItems() {
        return toolBarItems;
    }
}
