package com.funnysec.richardtang.androidkiller4j.view;

import cn.hutool.core.io.FileUtil;
import com.funnysec.richardtang.androidkiller4j.constant.Icon;
import com.funnysec.richardtang.androidkiller4j.constant.ProtocolString;
import com.funnysec.richardtang.androidkiller4j.pojo.Apk;
import com.funnysec.richardtang.androidkiller4j.pojo.TabUserData;
import com.funnysec.richardtang.androidkiller4j.ui.FxCodeArea;
import com.funnysec.richardtang.androidkiller4j.ui.SystemFileTreeView;
import com.funnysec.richardtang.androidkiller4j.ui.wrapper.ImageView;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Data;

import java.io.File;

/**
 * 任务列表中的每个APK双击后打开的页面就称为工作台
 * 一个Workbench对应一个要处理的Apk
 *
 * @author RichardTang
 */
@Data
public class WorkbenchView extends BaseView<Tab> {

    private Apk apk;

    // 选项卡
    private InfoTabView    infoTab;
    private ManagerTabView managerTab;
    private SearchTabView  searchTab;
    private TabPane        functionTabPane;

    // 右边的编辑器
    private FxCodeArea fxCodeArea;

    // 整体布局，左边为功能，右边为编辑器
    private SplitPane splitPane;

    public WorkbenchView(Apk apk) {
        this.apk = apk;
        createAfterLaunch();
    }

    @Override
    protected void initUi() {
        infoTab         = new InfoTabView();
        managerTab      = new ManagerTabView();
        searchTab       = new SearchTabView();
        functionTabPane = new TabPane();

        fxCodeArea = new FxCodeArea();
        splitPane  = new SplitPane();
    }

    @Override
    protected void initAttr() {
        getRootPane().setClosable(true);
        getRootPane().setText(apk.getApplicationName());
        getRootPane().setUserData(new TabUserData<>(apk.getApplicationName(), apk));
        getRootPane().setGraphic(new ImageView(ProtocolString.FILE + apk.getIconPath()));

        functionTabPane.setSide(Side.LEFT);
        splitPane.setDividerPositions(0.2f, 0.8f);
    }

    @Override
    protected void initLayout() {
        functionTabPane.getTabs().addAll(
                infoTab.getRootPane(), managerTab.getRootPane(), searchTab.getRootPane()
        );
        splitPane.getItems().addAll(functionTabPane, fxCodeArea);
        getRootPane().setContent(splitPane);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initialize() {

    }

    /**
     * 信息选项卡
     */
    private class InfoTabView extends BaseView<Tab> {

        // App信息和Icon图标
        private ImageView apkIcon;
        private Label     appNameLabel;
        private Label     appMainLabel;
        private Label     appPackageLabel;
        private Label     appVersionLabel;

        // 组件树，也就是信息选项卡下半部分的四大组件的折叠。
        private TreeItem<String> activityRootTreeItem;
        private TreeItem<String> serviceRootTreeItem;
        private TreeItem<String> receiveRootTreeItem;
        private TreeItem<String> usesPermissionRootTreeItem;
        private TreeItem<String> rootTreeItem;
        private TreeView<String> componentTreeView;

        // infoTab根容器布局
        private VBox infoTabVBox;

        // 将App信息垂直排列的布局
        private VBox appInfoVBox;

        // icon和app信息水平排列(并行排列)
        private HBox appIconAndInfoHBox;

        InfoTabView() {
            createAfterLaunch();
        }

        @Override
        protected void initUi() {
            appNameLabel    = new Label(String.format("名称: %s", apk.getApplicationName()));
            appMainLabel    = new Label(String.format("包名: %s", apk.getPackageName()));
            appPackageLabel = new Label(String.format("入口: %s", apk.getMainActivity()));
            appVersionLabel = new Label(String.format("MinSdk: %s | TargetSdk: %s", apk.getMinSdkVersion(), apk.getTargetSdkVersion()));
            apkIcon         = new ImageView(ProtocolString.FILE + apk.getIconPath(), 65, 65);

            infoTabVBox        = new VBox();
            appInfoVBox        = new VBox();
            appIconAndInfoHBox = new HBox();

            // 组件树，也就是信息选项卡下半部分的四大组件的折叠。
            activityRootTreeItem       = new TreeItem<>("Activity");
            serviceRootTreeItem        = new TreeItem<>("Service");
            receiveRootTreeItem        = new TreeItem<>("Receive");
            usesPermissionRootTreeItem = new TreeItem<>("UsesPermission");
            rootTreeItem               = new TreeItem<>("Root");
            componentTreeView          = new TreeView<>();
        }

        @Override
        protected void initAttr() {
            getRootPane().setText("信息");
            getRootPane().setClosable(false);
            getRootPane().setGraphic(Icon.WORKBENCH_VIEW_INFO);
            getRootPane().setUserData(new TabUserData<>("信息", null));

            infoTabVBox.setPadding(new Insets(10));
            appInfoVBox.setPadding(new Insets(0, 0, 0, 5));
            appIconAndInfoHBox.setPadding(new Insets(0, 0, 5, 0));

            componentTreeView.prefHeightProperty().bind(infoTabVBox.heightProperty());
            componentTreeView.setShowRoot(false);
        }

        @Override
        protected void initLayout() {
            appInfoVBox.getChildren().addAll(appNameLabel, appPackageLabel, appMainLabel, appVersionLabel);
            appIconAndInfoHBox.getChildren().addAll(apkIcon, appInfoVBox);

            rootTreeItem.getChildren().addAll(
                    activityRootTreeItem, serviceRootTreeItem, receiveRootTreeItem, usesPermissionRootTreeItem
            );
            componentTreeView.setRoot(rootTreeItem);
            infoTabVBox.getChildren().addAll(appIconAndInfoHBox, componentTreeView);

            getRootPane().setContent(infoTabVBox);
        }

        @Override
        protected void initEvent() {
        }

        @Override
        protected void initialize() {
            // 从apk中获取组件数据，添加到对应的根节点上。
            apk.getActivity().forEach(item -> activityRootTreeItem.getChildren().addAll(new TreeItem<>(item)));
            apk.getService().forEach(item -> serviceRootTreeItem.getChildren().addAll(new TreeItem<>(item)));
            apk.getReceiver().forEach(item -> receiveRootTreeItem.getChildren().addAll(new TreeItem<>(item)));
            apk.getUsesPermission().forEach(item -> usesPermissionRootTreeItem.getChildren().addAll(new TreeItem<>(item)));
        }
    }

    /**
     * 管理选项卡
     */
    private class ManagerTabView extends BaseView<Tab> {

        // apk系统文件树
        private SystemFileTreeView apkDirTreeView;

        ManagerTabView() {
            createAfterLaunch();
        }

        @Override
        protected void initUi() {
            apkDirTreeView = new SystemFileTreeView(apk.getBasePath());
        }

        @Override
        protected void initAttr() {
            getRootPane().setText("管理");
            getRootPane().setClosable(false);
            getRootPane().setGraphic(Icon.WORKBENCH_VIEW_MANAGER);
            getRootPane().setUserData(new TabUserData<>("管理", null));
        }

        @Override
        protected void initLayout() {
            getRootPane().setContent(apkDirTreeView);
        }

        @Override
        protected void initEvent() {
            apkDirTreeView.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                Node node = event.getPickResult().getIntersectedNode();
                if (event.getClickCount() != 2) {
                    return;
                }
                File   value   = apkDirTreeView.getSelectionModel().getSelectedItem().getValue();
                String content = FileUtil.readString(value, "UTF-8");
                fxCodeArea.getCodeArea().replaceText(0, 0, content);
            });
        }

        @Override
        protected void initialize() {

        }
    }

    /**
     * 搜索选项卡
     */
    private class SearchTabView extends BaseView<Tab> {

        SearchTabView() {
            createAfterLaunch();
        }

        @Override
        protected void initUi() {
        }

        @Override
        protected void initAttr() {
            getRootPane().setText("搜索");
            getRootPane().setClosable(false);
            getRootPane().setGraphic(Icon.WORKBENCH_VIEW_SEARCH);
            getRootPane().setUserData(new TabUserData<>("搜索", null));
        }

        @Override
        protected void initLayout() {

        }

        @Override
        protected void initEvent() {

        }

        @Override
        protected void initialize() {

        }
    }

}