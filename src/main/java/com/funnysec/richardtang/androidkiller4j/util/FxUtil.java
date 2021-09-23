package com.funnysec.richardtang.androidkiller4j.util;

import com.funnysec.richardtang.androidkiller4j.pojo.TabUserData;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;

/**
 * 简化一些JavaFx中需要频繁进行New来调用的组件
 *
 * @author RichardTang
 */
public class FxUtil {

    /**
     * 简化需要Alert alert = new Alert();的过程
     *
     * @param title   弹窗标题
     * @param content 弹窗内容
     */
    public static void alert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.NONE, content);
        alert.setTitle(title);
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alert.show();
    }

    /**
     * 简化Tab的创建过程
     *
     * @param title tab显示的标题
     * @param icon  tab显示的icon
     * @param tabId tabId
     * @param data  tab存储的数据
     * @return 创建的Tab对象
     */
    public static Tab getTab(String title, Node icon, String tabId, Object data) {
        Tab tab = new Tab();
        tab.setText(title);
        tab.setGraphic(icon);
        tab.setUserData(new TabUserData<>(tabId, data));
        return tab;
    }
}
