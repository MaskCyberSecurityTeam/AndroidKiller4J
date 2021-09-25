package com.funnysec.richardtang.androidkiller4j.view;

import com.android.ddmlib.IDevice;
import com.funnysec.richardtang.androidkiller4j.constant.Icon;
import com.funnysec.richardtang.androidkiller4j.core.ddmlib.AndroidDeviceManager;
import com.funnysec.richardtang.androidkiller4j.event.ToolkitViewEvent;
import com.funnysec.richardtang.androidkiller4j.ui.AccordionTabPane;
import com.funnysec.richardtang.androidkiller4j.util.FxUtil;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.ToolBar;
import lombok.Data;
import org.nutz.ioc.loader.annotation.Inject;

/**
 * 顶部工具栏View，效果为手风琴效果的TabPane。
 *
 * @author RichardTang
 */
@Data
public class ToolkitView extends IocView<AccordionTabPane> {

    // Tab 系统、安卓、设备
    private Tab systemTab;
    private Tab androidTab;
    private Tab deviceTab;

    // ToolBar 首页、工具、安卓、设备
    private ToolBar indexToolBar;
    private ToolBar androidToolBar;
    private ToolBar deviceToolBar;

    // 按钮: 打开 设置 关于 自定义
    private Button openButton;
    private Button settingButton;
    private Button customButton;
    private Button aboutButton;

    // 按钮: 签名 查壳 日志 进程 编译 安装 卸载 BASH 文件管理器
    private Button signButton;
    private Button findShellButton;
    private Button logButton;
    private Button processButton;
    private Button compileButton;
    private Button installButton;
    private Button bashButton;
    private Button fileExplorerButton;

    // 设备连接 设备选择框 设备刷新
    private Button             deviceConnectButton;
    private Button             deviceRefreshButton;
    private ChoiceBox<IDevice> deviceChoiceBox;

    @Inject
    private ToolkitViewEvent toolkitViewEvent;

    @Inject
    private AndroidDeviceManager androidDeviceManager;

    @Override
    protected void initUi() {
        systemTab  = FxUtil.getTab("系统", Icon.TOOLKIT_VIEW_SYSTEM, "系统", null);
        androidTab = FxUtil.getTab("安卓", Icon.TOOLKIT_VIEW_ANDROID, "安卓", null);
        deviceTab  = FxUtil.getTab("设备", Icon.TOOLKIT_VIEW_DEVICE, "设备", null);

        indexToolBar   = new ToolBar();
        androidToolBar = new ToolBar();
        deviceToolBar  = new ToolBar();

        // 打开 自定义 设置 关于
        openButton    = new Button("打开", Icon.TOOLKIT_VIEW_OPEN);
        customButton  = new Button("自定义", Icon.TOOLKIT_VIEW_CUSTOM);
        settingButton = new Button("设置", Icon.TOOLKIT_VIEW_SETTING);
        aboutButton   = new Button("关于", Icon.TOOLKIT_VIEW_ABOUT);

        // 签名 查壳 日志 进程 编译 安装 卸载 BASH
        signButton         = new Button("签名", Icon.TOOLKIT_VIEW_SIGNATURE);
        findShellButton    = new Button("查壳", Icon.TOOLKIT_VIEW_FIND_SHELL);
        logButton          = new Button("日志", Icon.TOOLKIT_VIEW_LOG);
        processButton      = new Button("进程", Icon.TOOLKIT_VIEW_PROCESS);
        compileButton      = new Button("编译", Icon.TOOLKIT_VIEW_COMPILE);
        installButton      = new Button("安装", Icon.TOOLKIT_VIEW_INSTALL);
        bashButton         = new Button("BASH", Icon.TOOLKIT_VIEW_BASH);
        fileExplorerButton = new Button("文件管理器", Icon.TOOLKIT_VIEW_FILE_EXPLORER);

        // 设备连接 设备选择框 设备刷新
        deviceConnectButton = new Button("连接", Icon.TOOLKIT_VIEW_CONNECT);
        deviceRefreshButton = new Button("刷新", Icon.TOOLKIT_VIEW_REFRESH);
        deviceChoiceBox     = new ChoiceBox<>();
    }

    @Override
    protected void initAttr() {
        systemTab.setClosable(false);
        deviceTab.setClosable(false);
        androidTab.setClosable(false);

        deviceChoiceBox.setPrefWidth(150);
        deviceChoiceBox.setPrefHeight(30);
    }

    @Override
    protected void initLayout() {
        indexToolBar.getItems().addAll(openButton, customButton, settingButton, aboutButton);
        androidToolBar.getItems().addAll(
                signButton, findShellButton, logButton,
                processButton, compileButton, installButton,
                bashButton, fileExplorerButton
        );
        deviceToolBar.getItems().addAll(deviceChoiceBox, deviceRefreshButton, deviceConnectButton);

        systemTab.setContent(indexToolBar);
        androidTab.setContent(androidToolBar);
        deviceTab.setContent(deviceToolBar);

        // 应用启动时获取一次设备
        deviceChoiceBox.getItems().addAll(androidDeviceManager.getDevices());

        getRootPane().addTabs(systemTab, androidTab, deviceTab);
    }

    @Override
    protected void initEvent() {
        // 配置组件对应的事件处理
        openButton.setOnMouseClicked(e -> toolkitViewEvent.openButtonOnMouseClick(e));
        aboutButton.setOnMouseClicked(e -> toolkitViewEvent.aboutButtonOnMouseClick(e));
        settingButton.setOnMouseClicked(e -> toolkitViewEvent.settingButtonOnMouseClick(e));
        customButton.setOnMouseClicked(e -> toolkitViewEvent.customButtonOnMouseClick(e));
        signButton.setOnMouseClicked(e -> toolkitViewEvent.signButtonOnMouseClick(e));
        findShellButton.setOnMouseClicked(e -> toolkitViewEvent.findShellButtonOnMouseClick(e));
        logButton.setOnMouseClicked(e -> toolkitViewEvent.logButtonOnMouseClick(e));
        processButton.setOnMouseClicked(e -> toolkitViewEvent.processButtonOnMouseClick(e));
        compileButton.setOnMouseClicked(e -> toolkitViewEvent.compileButtonOnMouseClick(e));
        installButton.setOnMouseClicked(e -> toolkitViewEvent.installButtonOnMouseClick(e));
        bashButton.setOnMouseClicked(e -> toolkitViewEvent.bashButtonOnMouseClick(e));
        deviceConnectButton.setOnMouseClicked(e -> toolkitViewEvent.deviceConnectButtonOnMouseClick(e));
        deviceRefreshButton.setOnMouseClicked(e -> toolkitViewEvent.deviceRefreshButtonOnMouseClick(e));
        fileExplorerButton.setOnMouseClicked(e -> toolkitViewEvent.fileExplorerOnMouseClick(e));
    }

    @Override
    protected void initialize() {

    }
}