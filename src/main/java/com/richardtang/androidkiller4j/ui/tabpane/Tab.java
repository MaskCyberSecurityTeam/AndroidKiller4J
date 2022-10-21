package com.richardtang.androidkiller4j.ui.tabpane;

import lombok.Builder;
import lombok.Data;

import javax.swing.*;
import java.awt.*;

/**
 * Tab页
 *
 * @author RichardTang
 */
@Data
public class Tab {

    // 鼠标悬浮时的提示信息
    private String    tip;
    // 图标
    private Icon      icon;
    // 携带的数据
    private Object    data;
    // 标题
    private String    title;
    // 是否显示关闭按钮
    private Boolean   close;
    // 对应的内容组件
    private Component component;

    public Tab(String title, Icon icon, String tip, Boolean close, Component component, Object data) {
        this.title = title;
        this.icon = icon;
        this.tip = tip;
        this.component = component;
        this.data = data;
        setClose(close);
    }

    public Tab(String title, Component component) {
        this(title, null, null, true, component, null);
    }

    public Tab(String title, Object data) {
        this(title, null, null, true, null, data);
    }

    public Tab(String title, Icon icon, Component component) {
        this(title, icon, null, true, component, null);
    }

    public Tab(String title, Component component, Boolean close) {
        this(title, null, null, close, component, null);
    }

    public Tab(String title, Icon icon, Component component, Boolean close) {
        this(title, icon, null, close, component, null);
    }

    public Tab(String title, Component component, Object data) {
        this(title, null, null, true, component, data);
    }

    public Tab(String title, Icon icon, Component component, Object data) {
        this(title, icon, null, true, component, data);
    }

    public Tab(String title, Component component, Boolean close, Object data) {
        this(title, null, null, close, component, data);
    }

    public Tab(String title, Icon icon, Component component, Boolean close, Object data) {
        this(title, icon, null, close, component, data);
    }

    public void setClose(Boolean close) {
        this.close = close;
        ((JComponent) component).putClientProperty("JTabbedPane.tabClosable", close);
    }
}
