package com.richardtang.androidkiller4j.ui.tabframe;

import javax.swing.*;

/**
 * TabFrameItem代表TabFrameBar中的每个选项
 *
 * @author RichardTang
 */
public class TabFrameItem {

    // 显示在ToolBar上的具体按钮
    private JToggleButton toolBarItemBtn;

    // 每个toolBarItemBtn对应一个在内容面板中具体显示的内容组件
    private JComponent contentComponent;

    // 每个toolBarItemBtn对应一个内容面板
    private TabFrameItemPanel tabFrameItemPanel;

    // 小组件按钮，显示在内容面板处标题右边的按钮。
    private JButton[] gadgetButtons;

    /**
     * 创建一个不带小组件按钮的TabFrameItem
     *
     * @param toolBarItemBtn   显示在ToolBar上的按钮
     * @param contentComponent 显示在内容面板中的具体组件
     */
    public TabFrameItem(JToggleButton toolBarItemBtn, JComponent contentComponent) {
        this(toolBarItemBtn, contentComponent, null);
    }

    /**
     * 创建带小组件按钮的TabFrameItem，小组件面板中默认会带一个缩小按钮。
     *
     * @param toolBarItemBtn   显示在ToolBar上的按钮
     * @param contentComponent 显示在内容面板中的具体组件
     * @param gadgetButtons    小组件按钮组
     */
    public TabFrameItem(JToggleButton toolBarItemBtn, JComponent contentComponent, JButton... gadgetButtons) {
        this.toolBarItemBtn   = toolBarItemBtn;
        this.contentComponent = contentComponent;
        this.gadgetButtons    = gadgetButtons;

        createTabFrameItemPanel();
    }

    /**
     * TabFrameItem中需要依赖TabFrameItemPanel，这里需要进行创建。
     */
    private void createTabFrameItemPanel() {
        JLabel titleLabel = new JLabel(toolBarItemBtn.getText());
        tabFrameItemPanel = new TabFrameItemPanel(titleLabel, contentComponent);
        // 判断是否有小组件需要创建
        if (gadgetButtons != null) {
            for (JButton gadgetButton : gadgetButtons) {
                tabFrameItemPanel.getHeaderToolBar().add(gadgetButton);
            }
        }
    }

    /**
     * 获取当前TabFrameItem在TabFrameBar中显示的按钮
     *
     * @return 返回用于在TabFrameBar中显示的按钮
     */
    public JToggleButton getToolBarItemBtn() {
        return toolBarItemBtn;
    }

    /**
     * 获取当前TabFrameItem对应的内容面板组件
     *
     * @return 返回当前TabFrameItem对应的内容面板组件
     */
    public TabFrameItemPanel getTabFrameItemPanel() {
        return tabFrameItemPanel;
    }
}