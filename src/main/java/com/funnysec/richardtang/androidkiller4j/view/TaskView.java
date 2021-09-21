package com.funnysec.richardtang.androidkiller4j.view;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.funnysec.richardtang.androidkiller4j.config.ResourcePathConfig;
import com.funnysec.richardtang.androidkiller4j.constant.Icon;
import com.funnysec.richardtang.androidkiller4j.constant.ProtocolString;
import com.funnysec.richardtang.androidkiller4j.event.TaskViewEvent;
import com.funnysec.richardtang.androidkiller4j.pojo.Apk;
import com.funnysec.richardtang.androidkiller4j.pojo.TabUserData;
import com.funnysec.richardtang.androidkiller4j.ui.wrapper.ImageView;
import com.funnysec.richardtang.androidkiller4j.view.device.DeviceLogView;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 任务列表视图，每处理一个Apk可以理解为是一个任务。
 * 打开程序时看到的那个Apk列表的任务表格就是这个。
 */
@Data
@Component
public class TaskView extends BaseView<TabPane> {

    // 任务列表
    private Tab taskTab;

    // 存放apk视图的组件
    private ListView<Apk> listView;

    @Autowired
    private TaskViewEvent taskViewEvent;

    @Autowired
    private DeviceLogView deviceLogView;

    @Autowired
    private SignatureView signatureView;

    @Override
    protected void initUi() {
        listView = new ListView<>();
        taskTab  = new Tab("任务列表");
    }

    @Override
    protected void initAttr() {
        // 这里需要给Tab存储一个唯一的标识，用来判断是否处理了相同任务时使用。
        taskTab.setUserData("tasklist");
        taskTab.setClosable(false);
        taskTab.setGraphic(Icon.TASK_VIEW_TAB);

        listView.setCellFactory(param -> new TaskViewApkItemCell());
        getRootPane().setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    }

    @Override
    protected void initLayout() {
        taskTab.setContent(listView);
        getRootPane().getTabs().add(taskTab);
    }

    @Override
    protected void initEvent() {
        listView.setOnMouseClicked(e -> taskViewEvent.listViewItenOnMouseClick(e));
    }

    @Override
    protected void initialize() {
        reload();
    }

    /**
     * 从$WOKR_DIR/project/中读取APK项目，并加载到jobApkCollection中，最后显示在ListView上。
     */
    public void reload() {
        // 清除列表中已有的任务元素
        listView.getItems().removeAll();
        for (File file : FileUtil.ls(ResourcePathConfig.PROJECT)) {
            if (!file.isDirectory()) {
                continue;
            }
            listView.getItems().add(new Apk(ResourcePathConfig.PROJECT + file.getName()));
        }
    }

    /**
     * 根据Tab设置的UserData中的ID值来查找Tabs中对应的Tab
     *
     * @param tabId 查找Tab的ID
     * @return 有则返回Tabs中已经存在的Tab，无则返回null。
     */
    public Tab findTabById(String tabId) {
        return getRootPane().getTabs().stream()
                .filter(t -> tabIdEqualUserData(t, tabId))
                .findFirst()
                .orElse(null);
    }

    /**
     * 简化获取选中的Tab功能函数的调用链
     *
     * @return 选中的Tab
     */
    public Tab getSelectTab() {
        return getRootPane().getSelectionModel().getSelectedItem();
    }

    /**
     * 简化获取选中的Tab对应的UserData
     *
     * @return 选中Tab的UserData值
     */
    public Object getSelectTabUserData() {
        return getSelectTab().getUserData();
    }

    /**
     * 比较Tab中的UserData和TabId是否相等
     * 如果传递的Tab中取出的UserData非TabUserData类型的话就直接进行比较
     *
     * @param tab   需要比较的Tab
     * @param tabId 需要比较的TabId
     * @return true:相等 false:不想登
     */
    private boolean tabIdEqualUserData(Tab tab, String tabId) {
        Object userData = tab.getUserData();
        if (userData instanceof TabUserData) {
            String id = ((TabUserData<?>) userData).getTabId();
            return tabId.equals(id);
        } else {
            return tabId.equals(userData);
        }
    }

    private static class TaskViewApkItemCell extends ListCell<Apk> {

        @Override
        protected void updateItem(Apk apk, boolean empty) {
            super.updateItem(apk, empty);
            if (empty) {
                setStyle(null);
                setGraphic(null);
            } else {
                graphCellItem(apk);
            }
        }

        private void graphCellItem(Apk apk) {
            BorderPane row       = new BorderPane();
            HBox       leftNodes = new HBox();

            Label appNameLabel = new Label(apk.getApplicationName());
            appNameLabel.setPadding(new Insets(3, 0, 0, 2));

            Label dateTimeLabel = new Label(DateUtil.format(apk.getDecodeApkDate(), "yyyy-MM-dd HH:mm:ss"));
            dateTimeLabel.setPadding(new Insets(3, 0, 0, 0));

            ImageView iconImageView = new ImageView(ProtocolString.FILE + apk.getIconPath());
            leftNodes.getChildren().addAll(iconImageView, appNameLabel);

            row.setLeft(leftNodes);
            row.setRight(dateTimeLabel);

            setGraphic(row);
        }
    }
}