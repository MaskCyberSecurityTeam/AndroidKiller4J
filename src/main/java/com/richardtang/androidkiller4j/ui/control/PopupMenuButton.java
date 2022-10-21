package com.richardtang.androidkiller4j.ui.control;

import javax.swing.*;

/**
 * 带下拉层的按钮
 *
 * @author RichardTang
 */
public class PopupMenuButton extends JButton {

    // 按钮对应的菜单组件
    private JPopupMenu popupMenu;

    /**
     * 创建一个菜单按钮
     *
     * @param icon      按钮图标
     * @param popupMenu 按钮点击后显示的菜单
     */
    public PopupMenuButton(Icon icon, JPopupMenu popupMenu) {
        this(null, icon, popupMenu);
    }

    /**
     * 创建一个菜单按钮
     *
     * @param text      按钮显示的文本
     * @param popupMenu 按钮点击后显示的菜单
     */
    public PopupMenuButton(String text, JPopupMenu popupMenu) {
        this(text, null, popupMenu);
    }

    /**
     * 创建一个菜单按钮
     *
     * @param text      按钮显示的文本
     * @param icon      按钮图标
     * @param popupMenu 按钮点击后显示的菜单
     */
    public PopupMenuButton(String text, Icon icon, JPopupMenu popupMenu) {
        super(text, icon);
        this.popupMenu = popupMenu;
        initListener();
    }

    /**
     * 初始化事件
     */
    private void initListener() {
        addActionListener(e -> {
            JButton button = ((JButton) e.getSource());
            if (!popupMenu.isShowing()) {
                popupMenu.show(button, 0, button.getBounds().height);
            } else {
                popupMenu.setVisible(false);
            }
        });
    }
}