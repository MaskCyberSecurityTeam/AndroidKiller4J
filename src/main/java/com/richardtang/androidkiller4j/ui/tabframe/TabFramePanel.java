package com.richardtang.androidkiller4j.ui.tabframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

/**
 * TabFrame功能的主面板，组合Content和Bar两块的内容。
 * Content中拖拽的实现主要思路是，通过鼠标事件去动态修改TabFramePanel中的Height。
 *
 * @author RichardTang
 */
public class TabFramePanel extends JPanel implements MouseListener, MouseMotionListener {

    // 每一个TabFramePanel只有一个tabFrameBar其实就是toolBar
    private TabFrameBar tabFrameBar;

    // 存储用于定义的TabFrameItem
    private TabFrameItem[] tabFrameItems;

    // 记录面板处于显示状态时的高宽值，当在进行隐藏显示操作时充当第三方临时变量。
    private Dimension tempDimension;

    // 用于标记鼠标是否开始进行拖拽改变面板大小
    private boolean isDragged = false;

    // 即将进行拖拽操作时，记录一下鼠标点击在Panel中的位置。
    private double clickDraggedPanelPosition;

    /**
     * 因为存在多个TabFrameItem的情况，每次只能显示其中一个的内容组件。
     * 需要通过一个中间的JPanel来承载TabFrameItem中的内容组件
     */
    private JPanel contentPanel = new JPanel(new BorderLayout());

    /**
     * 根据TabFrameItem来创建TabFrame，每一个Item代表一个功能选项。
     *
     * @param tabFrameItems 需要添加到ToolBar的多个TabFrameItem
     */
    public TabFramePanel(TabFrameItem... tabFrameItems) {
        this.tabFrameItems = tabFrameItems;
        this.tabFrameBar   = new TabFrameBar();
        for (TabFrameItem item : tabFrameItems) {
            addTabFrameItem(item);
        }
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        add(tabFrameBar, BorderLayout.SOUTH);
    }

    /**
     * 添加TabFrameItem
     *
     * @param tabFrameItem 用于显示具体内容的Item实体
     */
    public void addTabFrameItem(TabFrameItem tabFrameItem) {
        // 取到ToolBar上的按钮和按钮对应的Content
        JToggleButton     itemButton  = tabFrameItem.getToolBarItemBtn();
        TabFrameItemPanel itemContent = tabFrameItem.getTabFrameItemPanel();

        // 给内容组件添加鼠标拖拽改变大小事件
        itemContent.getHeaderPanel().addMouseListener(this);
        itemContent.getHeaderPanel().addMouseMotionListener(this);

        // 添加缩小按钮点击事件，上班使用addActionListener这里才可以使用doClick()。
        itemContent.getMinimizeButton().addActionListener(e -> itemButton.doClick());

        // 点击TabFrameItem面板时的隐藏和显示事件
        itemButton.addActionListener(e -> {
            // 清空ToolBar中的按钮状态，不清空当前触发事件的按钮。
            for (JToggleButton btn : tabFrameBar.getToolBarItems()) {
                if (btn != itemButton) {
                    btn.setSelected(false);
                }
            }

            // 因为多个TabFrameItem的原因，需要清空之前显示的内容。
            contentPanel.removeAll();
            contentPanel.add(itemContent);

            // 内容面板显示和隐藏实现
            if (itemButton.isSelected()) {
                contentPanel.setVisible(true);
                setPreferredSize(tempDimension);
            } else {
                tempDimension = getSize();
                contentPanel.setVisible(false);
                setPreferredSize(new Dimension(tempDimension.width, itemButton.getHeight()));
            }
            updateUI();
        });
        tabFrameBar.addBarItem(itemButton);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        clickDraggedPanelPosition = e.getPoint().getY();

        isDragged = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isDragged = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        ((JPanel) e.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isDragged) {
            Dimension size = getSize();
            size.height += clickDraggedPanelPosition;
            size.height += Math.negateExact(e.getY());
            setPreferredSize(size);
            updateUI();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
