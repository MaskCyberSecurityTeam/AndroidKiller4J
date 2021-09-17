package com.funnysec.richardtang.androidkiller4j.ui;

import com.funnysec.richardtang.androidkiller4j.ui.wrapper.ImageView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;

/**
 * 带有手风琴效果的TabPane，实际上就是有个折叠效果。
 * StackPane里套一个Button和TabPane，当点击Button时修改TabPane的高度即可达到隐藏和显示的效果。
 * 在使用时必须传递Tab，否则无法显示出效果。
 *
 * @author RichardTang
 */
public class AccordionTabPane extends StackPane {

    private TabPane tabPane = new TabPane();

    // 触发显示隐藏用的按钮
    private Button changeButton = new Button();

    // 向上箭头图片
    public static final ImageView UP_IMG = new ImageView("image/accordion-tab-pane-up.png");

    // 向下箭头图片
    public static final ImageView DOWN_IMG = new ImageView("image/accordion-tab-pane-down.png");

    /**
     * 创建带有手风琴效果的TabPane
     */
    public AccordionTabPane() {
        initialize();
    }

    /**
     * 创建带有手风琴效果的TabPane
     * 使用时必须传递Tab，不然看不到效果。
     *
     * @param tabs 需要添加的Tab
     */
    public AccordionTabPane(Tab... tabs) {
        initialize();
        tabPane.getTabs().addAll(tabs);
    }

    /**
     * 初始化基本参数
     */
    private void initialize() {
        // 按钮进行相对定位，定位到右上角
        changeButton.setGraphic(DOWN_IMG);
        changeButton.setTranslateX(-10);
        changeButton.setTranslateY(8);
        changeButton.setPickOnBounds(true);
        changeButton.setPadding(new Insets(1, 1, 1, 1));

        // 切换按钮事件，点击时通过修改高度来达到隐藏和显示效果
        changeButton.setOnMouseClicked(event -> {
            if (tabPane.getHeight() >= 40) {
                tabPane.setMaxHeight(33.5);
                changeButton.setGraphic(UP_IMG);
            } else {
                tabPane.setMaxHeight(100);
                changeButton.setGraphic(DOWN_IMG);
            }
        });

        // 设置StackPane布局的默认从右上角开始
        setAlignment(Pos.TOP_RIGHT);
        getChildren().addAll(tabPane, changeButton);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    }

    /**
     * 添加Tab
     *
     * @param tab 需要添加的Tab
     */
    public void addTab(Tab tab) {
        tabPane.getTabs().add(tab);
    }

    /**
     * 添加多个Tab
     *
     * @param tabs 需要添加的Tab
     */
    public void addTabs(Tab... tabs) {
        tabPane.getTabs().addAll(tabs);
    }

    /**
     * 删除Tab
     *
     * @param tab 需要删除的Tab
     */
    public void removeTab(Tab tab) {
        tabPane.getTabs().remove(tab);
    }

    /**
     * 删除多个Tab
     *
     * @param tabs 需要删除的Tab
     */
    public void removeTabs(Tab... tabs) {
        tabPane.getTabs().removeAll(tabs);
    }
}
