package com.funnysec.richardtang.androidkiller4j.util;

import com.funnysec.richardtang.androidkiller4j.pojo.TabUserData;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

/**
 * 简化一些JavaFx中需要频繁进行New来调用的组件
 *
 * @author RichardTang
 */
public class FxUtil {

    // JavaFx的文件选择器
    private final static FileChooser FILE_CHOOSER = new FileChooser();

    // JavaFx的目录选择器
    private final static DirectoryChooser DIRECTORY_CHOOSER = new DirectoryChooser();

    // 文件过滤器
    private final static FileChooser.ExtensionFilter APK_EXT_FILETER = new FileChooser.ExtensionFilter("只允许格式为:apk的文件", "*.apk");

    public static File showDialog(String title, final Window ownerWindow) {
        DIRECTORY_CHOOSER.setTitle(title);
        return DIRECTORY_CHOOSER.showDialog(ownerWindow);
    }

    public static File showFileChooser(String title, final Window ownerWindow) {
        FILE_CHOOSER.setTitle(title);
        return FILE_CHOOSER.showOpenDialog(ownerWindow);
    }

    public static File showApkFileChooser(String title, final Window ownerWindow) {
        FILE_CHOOSER.setTitle(title);
        FILE_CHOOSER.getExtensionFilters().add(APK_EXT_FILETER);
        return FILE_CHOOSER.showOpenDialog(ownerWindow);
    }

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
