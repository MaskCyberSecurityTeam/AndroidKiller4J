package com.richardtang.androidkiller4j.ui.tabpane;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

/**
 * 进一步封装JTabbedPane，主要提供Tab的默认关闭按钮，以及Tab页可以挂靠数据对象。
 *
 * @author RichardTang
 */
public class TabPane extends JTabbedPane {

    // 用于存放Tab类，Key为Tab类中的Title
    private LinkedHashMap<String, Tab> tabContainer = new LinkedHashMap<>();

    public TabPane() {
        // 配置Tab默认就显示关闭按钮的动作
        this.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSE_CALLBACK, (IntConsumer) this::removeTabAt);
    }

    /**
     * 添加一个tab页
     *
     * @param tab tab页实体类
     */
    public void addTab(Tab tab) {
        tabContainer.put(tab.getTitle(), tab);
        super.addTab(tab.getTitle(), tab.getIcon(), tab.getComponent(), tab.getTip());
    }

    /**
     * 重写原有的addTab函数，需要在添加Tab时将tab组件对象添加到集合容器中。
     *
     * @param title     tab标题
     * @param component tab中显示的内容组件
     */
    @Override
    public void addTab(String title, Component component) {
        addTab(new Tab(title, component));
    }

    @Override
    public void addTab(String title, Icon icon, Component component) {
        addTab(new Tab(title, icon, component));
    }

    @Override
    public void addTab(String title, Icon icon, Component component, String tip) {
        addTab(new Tab(title, icon, component, tip));
    }

    public void addTab(String title, Icon icon, Component component, Boolean close) {
        addTab(new Tab(title, icon, component, close));
    }

    /**
     * 根据标题删除指定tab
     *
     * @param title 需要被删除的tab对应的标题
     */
    public void removeTabByTitle(String title) {
        Tab tab = tabContainer.get(title);
        remove(tab.getComponent());
        tabContainer.remove(title);
    }

    /**
     * 根据索引值删除tab页
     *
     * @param index tab对应的索引
     */
    @Override
    public void removeTabAt(int index) {
        Component componentAt = getComponentAt(index);
        for (String key : tabContainer.keySet()) {
            if (componentAt == tabContainer.get(key).getComponent()) {
                tabContainer.remove(key);
                break;
            }
        }
        super.removeTabAt(index);
    }

    /**
     * 根据标题获取Tab
     *
     * @param title tab对应的标题
     * @return 对应title的tab，返回null为没找到该tab。
     */
    public Tab getTabByTitle(String title) {
        return tabContainer.get(title);
    }

    /**
     * 获取当前处于选中状态的tab
     *
     * @return 当前处于选中状态的tab
     */
    public Tab getSelectedTab() {
        Component selectedComponent = getSelectedComponent();
        for (String key : tabContainer.keySet()) {
            Component component = tabContainer.get(key).getComponent();
            if (component == selectedComponent) {
                return tabContainer.get(key);
            }
        }
        return null;
    }

    /**
     * 获取选中的Tab的标题
     *
     * @return 选中的Tab的标题
     */
    public String getSelectedTabTitle() {
        int selectedIndex = getSelectedIndex();
        return getTitleAt(selectedIndex);
    }

    /**
     * 根据Tab的标题显示对应的Tab
     *
     * @param title 需要显示的Tab对应的标题
     * @return false为tab未打开，true为tab已打开并进行显示。
     */
    public boolean showTabByTitle(String title) {
        Tab tab = tabContainer.get(title);
        if (tab != null) {
            setSelectedComponent(tab.getComponent());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取Tab数量
     *
     * @return tab数量
     */
    public int getTabSize() {
        return tabContainer.size();
    }
}