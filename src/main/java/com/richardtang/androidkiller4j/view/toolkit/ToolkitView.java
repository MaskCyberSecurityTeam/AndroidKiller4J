package com.richardtang.androidkiller4j.view.toolkit;

import com.formdev.flatlaf.FlatClientProperties;
import com.richardtang.androidkiller4j.core.ddmlib.AndroidDeviceManager;
import com.richardtang.androidkiller4j.ui.control.IconComboBox;
import com.richardtang.androidkiller4j.util.ControlUtil;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

/**
 * 工具栏
 *
 * @author RichardTang
 */
@Data
@Component
public class ToolkitView extends JTabbedPane implements InitializingBean {

    // 系统
    private JPanel  settingPanel;
    private JButton openButton;
    private JButton customButton;
    private JButton settingButton;
    private JButton aboutButton;

    // 安卓
    private JPanel  androidPanel;
    private JButton signatureButton;
    private JButton findShellButton;
    private JButton logButton;
    private JButton processButton;
    private JButton compileButton;
    private JButton installButton;
    private JButton adbButton;
    private JButton fileExplorerButton;

    // 设备
    private JPanel       devicePanel;
    private IconComboBox deviceIconComboBox;
    private JButton      refreshButton;
    private JButton      deviceConnectButton;

    private final IconComboBox.IconComboBoxData deviceIconComboBoxDefaultOption =
            new IconComboBox.IconComboBoxData("DEFAULT", "请选择设备");

    private static final Icon DEVICE_CONNECT_ICON    = ControlUtil.getSVGIcon("connect.svg", 15, 15);
    private static final Icon DEVICE_DISCONNECT_ICON = ControlUtil.getSVGIcon("disconnect.svg", 15, 15);

    @Autowired
    private ToolkitViewAction toolkitViewEvent;

    @Autowired
    private AndroidDeviceManager androidDeviceManager;

    @Override
    public void afterPropertiesSet() {
        renderSettingPanel();
        renderAndroidPanel();
        renderDevicePanel();
        initEvent();

        addTab("系统", ControlUtil.getSVGIcon("setting.svg", 20, 20), settingPanel);
        addTab("安卓", ControlUtil.getSVGIcon("android.svg", 20, 20), androidPanel);
        addTab("设备", ControlUtil.getSVGIcon("device.svg", 20, 20), devicePanel);
        putClientProperty(FlatClientProperties.TABBED_PANE_TAB_ICON_PLACEMENT, SwingConstants.LEFT);
    }

    /**
     * 渲染系统设置面板组件
     */
    private void renderSettingPanel() {
        settingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        openButton    = makeButton("打开", "apk.svg");
        customButton  = makeButton("自定义", "custom.svg");
        settingButton = makeButton("设置", "settings.svg");
        aboutButton   = makeButton("关于", "about.svg");

        settingPanel.add(openButton);
        settingPanel.add(customButton);
        settingPanel.add(settingButton);
        settingPanel.add(aboutButton);
    }

    /**
     * 渲染安卓面板组件
     */
    private void renderAndroidPanel() {
        androidPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        signatureButton    = makeButton("签名", "signature.svg");
        findShellButton    = makeButton("查壳", "search.svg");
        logButton          = makeButton("日志", "bug.svg");
        processButton      = makeButton("进程", "process.svg");
        compileButton      = makeButton("编译", "compile.svg");
        installButton      = makeButton("安装", "install.svg");
        adbButton          = makeButton("ADB", "terminal.svg");
        fileExplorerButton = makeButton("文件管理器", "directory.svg");

        androidPanel.add(signatureButton);
        androidPanel.add(findShellButton);
        androidPanel.add(logButton);
        androidPanel.add(processButton);
        androidPanel.add(compileButton);
        androidPanel.add(installButton);
        androidPanel.add(adbButton);
        androidPanel.add(fileExplorerButton);
    }

    /**
     * 渲染设备面板组件
     */
    private void renderDevicePanel() {
        devicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        deviceIconComboBox = new IconComboBox();
        deviceIconComboBox.addItem(deviceIconComboBoxDefaultOption);
        deviceIconComboBox.setSelectedIndex(0);

        refreshButton       = makeButton("刷新", "refresh.svg");
        deviceConnectButton = makeButton("连接", "disconnect.svg");

        devicePanel.add(deviceIconComboBox);
        devicePanel.add(refreshButton);
        devicePanel.add(deviceConnectButton);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        logButton.addActionListener(event -> toolkitViewEvent.logButtonOnMouseClick(event));
        adbButton.addActionListener(event -> toolkitViewEvent.adbButtonOnMouseClick(event));
        openButton.addActionListener(event -> toolkitViewEvent.openButtonOnMouseClick(event));
        aboutButton.addActionListener(event -> toolkitViewEvent.aboutButtonOnMouseClick(event));
        signatureButton.addActionListener(event -> toolkitViewEvent.signButtonOnMouseClick(event));
        processButton.addActionListener(event -> toolkitViewEvent.processButtonOnMouseClick(event));
        compileButton.addActionListener(event -> toolkitViewEvent.compileButtonOnMouseClick(event));
        fileExplorerButton.addActionListener(event -> toolkitViewEvent.fileExplorerOnMouseClick(event));
        findShellButton.addActionListener(event -> toolkitViewEvent.findShellButtonOnMouseClick(event));
        refreshButton.addActionListener(event -> toolkitViewEvent.deviceRefreshButtonOnMouseClick(event));
        deviceConnectButton.addActionListener(event -> toolkitViewEvent.deviceConnectButtonOnMouseClick(event));
    }

    /**
     * 简化创建JButton代码
     *
     * @param text    JButton显示的文本
     * @param svgName 显示的svg图片名称
     * @return 带有文本和图标的JButton
     */
    private JButton makeButton(String text, String svgName) {
        return new JButton(text, ControlUtil.getSVGIcon(svgName, 15, 15));
    }

    /**
     * 根据isConnect的值设置设备的连接状态
     * 当设备处于连接状态时，显示的icon图标是断开连接的图标，反之是连接的图标。
     *
     * @param isConnect 设备是否连接
     */
    public void setDeviceConnectStateUI(boolean isConnect) {
        if (isConnect) {
            deviceConnectButton.setText("断开");
            deviceConnectButton.setIcon(DEVICE_DISCONNECT_ICON);
        } else {
            deviceConnectButton.setText("连接");
            deviceConnectButton.setIcon(DEVICE_CONNECT_ICON);
        }
    }
}