package com.richardtang.androidkiller4j.view;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.log.StaticLog;
import com.android.ddmlib.IDevice;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.SystemInfo;
import com.richardtang.androidkiller4j.MainWindow;
import com.richardtang.androidkiller4j.bean.Apk;
import com.richardtang.androidkiller4j.constant.Suffix;
import com.richardtang.androidkiller4j.constant.SvgName;
import com.richardtang.androidkiller4j.constant.R;
import com.richardtang.androidkiller4j.constant.Size;
import com.richardtang.androidkiller4j.ddmlib.AndroidDeviceManager;
import com.richardtang.androidkiller4j.task.command.*;
import com.richardtang.androidkiller4j.ui.action.*;
import com.richardtang.androidkiller4j.ui.control.IconComboBox;
import com.richardtang.androidkiller4j.util.CompressUtils;
import com.richardtang.androidkiller4j.view.device.DeviceExplorerView;
import com.richardtang.androidkiller4j.view.device.DeviceLogView;
import com.richardtang.androidkiller4j.view.device.DeviceProcessView;
import com.richardtang.androidkiller4j.util.ControlUtils;
import com.richardtang.androidkiller4j.validator.DeviceOnlineValidator;
import com.richardtang.androidkiller4j.validator.TabAddedValidator;
import com.richardtang.androidkiller4j.validator.SelectedIsWorkbenchTabValidator;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * 工具栏
 *
 * @author RichardTang
 */
@Getter
public final class ToolkitView extends JTabbedPane {

    // 系统
    private final JButton openButton    = ControlUtils.getMediumIconButton("打开", SvgName.APK);
    private final JButton aboutButton   = ControlUtils.getMediumIconButton("关于", SvgName.ABOUT);
    private final JButton customButton  = ControlUtils.getMediumIconButton("自定义", SvgName.CUSTOM);
    private final JButton settingButton = ControlUtils.getMediumIconButton("设置", SvgName.SETTING);

    // 安卓
    private final JButton logButton          = ControlUtils.getMediumIconButton("日志", SvgName.BUG);
    private final JButton adbButton          = ControlUtils.getMediumIconButton("ADB", SvgName.TERMINAL);
    private final JButton processButton      = ControlUtils.getMediumIconButton("进程", SvgName.PROCESS);
    private final JButton compileButton      = ControlUtils.getMediumIconButton("编译", SvgName.COMPILE);
    private final JButton installButton      = ControlUtils.getMediumIconButton("安装", SvgName.INSTALL);
    private final JButton signatureButton    = ControlUtils.getMediumIconButton("签名", SvgName.SIGNATURE);
    private final JButton findShellButton    = ControlUtils.getMediumIconButton("查壳", SvgName.EYE);
    private final JButton fileExplorerButton = ControlUtils.getMediumIconButton("文件管理器", SvgName.DIRECTORY);

    // 设备
    private final JButton      refreshButton  = ControlUtils.getMediumIconButton("刷新", SvgName.REFRESH);
    private final JButton      connectButton  = ControlUtils.getMediumIconButton("连接", SvgName.DISCONNECT);
    private final IconComboBox deviceComboBox = new IconComboBox("请选择设备");

    // 面板
    private final JPanel devicePanel  = new JPanel(ControlUtils.getLeftFlowLayout(Size.SMALL));
    private final JPanel settingPanel = new JPanel(ControlUtils.getLeftFlowLayout(Size.SMALL));
    private final JPanel androidPanel = new JPanel(ControlUtils.getLeftFlowLayout(Size.SMALL));

    // 连接、断开、系统、安卓、设备、Frida
    private final Icon systemIcon          = ControlUtils.getSVGIcon(SvgName.SYSTEM);
    private final Icon androidIcon         = ControlUtils.getSVGIcon(SvgName.ANDROID);
    private final Icon deviceIcon          = ControlUtils.getSVGIcon(SvgName.DEVICE);
    private final Icon devieConnectIcon    = ControlUtils.getSVGIcon(SvgName.CONNECT, Size.MEDIUM);
    private final Icon devceDisconnectIcon = ControlUtils.getSVGIcon(SvgName.DISCONNECT, Size.MEDIUM);
    private final Icon bugIcon             = ControlUtils.getSVGIcon(SvgName.BUG);
    private final Icon processIcon         = ControlUtils.getSVGIcon(SvgName.PROCESS);
    private final Icon directoryIcon       = ControlUtils.getSVGIcon(SvgName.DIRECTORY);
    private final Icon signatureIcon       = ControlUtils.getSVGIcon(SvgName.SIGNATURE);
    private final Icon fridaIcon           = ControlUtils.getSVGIcon(SvgName.F);
    private final Icon settingIcon         = ControlUtils.getSVGIcon(SvgName.SETTING);

    public ToolkitView() {
        // 系统面板
        settingPanel.add(openButton);
        settingPanel.add(settingButton);
        settingPanel.add(aboutButton);
        // 安卓面板
        androidPanel.add(signatureButton);
        androidPanel.add(findShellButton);
        androidPanel.add(logButton);
        androidPanel.add(processButton);
        androidPanel.add(compileButton);
        androidPanel.add(installButton);
        androidPanel.add(adbButton);
        androidPanel.add(fileExplorerButton);
        // 设备面板
        devicePanel.add(deviceComboBox);
        devicePanel.add(refreshButton);
        devicePanel.add(connectButton);
        // 面板添加至根Panel
        addTab("系统", systemIcon, settingPanel);
        addTab("安卓", androidIcon, androidPanel);
        addTab("设备", deviceIcon, devicePanel);
        // TAB图标摆放位置
        putClientProperty(FlatClientProperties.TABBED_PANE_TAB_ICON_PLACEMENT, SwingConstants.LEFT);
        // 自动绑定事件
        ClickActionInstaller.bind(this);
    }

    /**
     * 根据isConnect的值设置设备的连接状态
     * 当设备处于连接状态时，显示的icon图标是断开连接的图标，反之是连接的图标。
     *
     * @param isConnect 设备是否连接
     */
    public void setDeviceConnectStateUI(boolean isConnect) {
        if (isConnect) {
            connectButton.setText("断开");
            connectButton.setIcon(devceDisconnectIcon);
        } else {
            connectButton.setText("连接");
            connectButton.setIcon(devieConnectIcon);
        }
    }

    /**
     * 对Apk进行反编译
     *
     * @param apkFile 需要反编译的apk
     */
    public void apkDecompiler(File apkFile) {
        MainWindow.consoleView.startLoadingProgressBar("APK反编译中...");

        // 以APK的名称作为反编译后的输出目录，除去后边的.apk后缀。
        String apkName = apkFile.getName().replace(Suffix.POINT_APK, "");
        String apkToolDecompilerOutputDir = R.PROJECT_DIR + apkName;

        // dex2jar任务，将dex抓换为.class文件
        Dex2JarTask dex2JarTask = new Dex2JarTask(apkFile.getAbsolutePath(), R.PROJECT_SRC_DIR);
        dex2JarTask.setCallback(() -> {
            String jarPath = R.PROJECT_SRC_DIR + apkName + Suffix.POINT_JAR;
            String jarUnzipPath = R.PROJECT_SRC_DIR + apkName + "/";
            CompressUtils.unzip(jarPath, jarUnzipPath);
            FileUtil.del(jarPath);
        });

        // apk解包反编译任务
        ApkToolDecompileTask apkToolDecompileTask = new ApkToolDecompileTask(apkFile.getAbsolutePath(), apkToolDecompilerOutputDir);
        apkToolDecompileTask.setCallback(() -> {
            ThreadUtil.execAsync(dex2JarTask);
            // 避免任务表中存在相同的apk，判断是否存在旧的，存在则删除。
            Integer index = MainWindow.taskView.findTaskByBasePath(apkToolDecompilerOutputDir);
            if (index != -1) {
                MainWindow.taskView.removeTask(index);
            }
            MainWindow.taskView.addTask(new Apk(apkToolDecompilerOutputDir));
            MainWindow.consoleView.stopLoadingProgressBar("APK反编译结束");
        });
        ThreadUtil.execAsync(apkToolDecompileTask);
    }

    @ClickAction("openButton")
    public void openButtonClick(ActionEvent event) {
        File apkFile = ControlUtils.chooserApkFileDialog();
        if (apkFile != null) {
            apkDecompiler(apkFile);
        }
    }

    @ClickAction("aboutButton")
    public void aboutButtonClick(ActionEvent event) {
        String msg = """
                    作者: RichardTang
                    团队: Mask安全小组
                    GitHub: https://github.com/Richard-Tang/AndroidKiller4J
                """;
        ControlUtils.showMsgDialog("关于", msg);
    }

    @ClickAction("settingButton")
    public void settingButtonClick(ActionEvent event) {
        if (TabAddedValidator.verify("配置")) return;
        MainWindow.taskView.addTab("配置", settingIcon, MainWindow.settingView);
    }

    @ClickAction("signatureButton")
    public void signatureButtonClick(ActionEvent event) {
        if (TabAddedValidator.verify("签名")) return;
        MainWindow.taskView.addTab("签名", signatureIcon, MainWindow.signatureView);
    }

    @ClickAction("findShellButton")
    public void findShellButtonClick(ActionEvent event) {
        File file = ControlUtils.chooserApkFileDialog();
        if (file != null) {
            ThreadUtil.execAsync(new ApkInspectShellTask(file));
        }
    }

    @ClickAction("logButton")
    public void logButtonClick(ActionEvent event) {
        if (!DeviceOnlineValidator.verify() || TabAddedValidator.verify("日志")) return;
        MainWindow.taskView.addTab("日志", bugIcon, new DeviceLogView(AndroidDeviceManager.getInstance().getDevice()));
    }

    @ClickAction("processButton")
    public void processButtonClick(ActionEvent event) {
        if (!DeviceOnlineValidator.verify() || TabAddedValidator.verify("进程")) return;
        MainWindow.taskView.addTab("进程", processIcon, new DeviceProcessView(AndroidDeviceManager.getInstance().getDevice()));
    }

    @ClickAction("compileButton")
    public void compileButtonClick(ActionEvent event) {
        if (!SelectedIsWorkbenchTabValidator.verify()) return;

        Apk apk = MainWindow.taskView.getSelectedApk();
        String basePath = apk.getBasePath();
        String unsignApkPath = basePath + "/dist/" + apk.getFileName();
        String signApkPath = basePath + "/dist/sign_" + apk.getFileName();

        // 创建apk签名任务，等待被编译任务的调用，签名任务结束后进行弹窗提示。
        ApkSignatureTask apkSignatureTask = new ApkSignatureTask(unsignApkPath, signApkPath, "androidkiller4j.keystore");

        apkSignatureTask.setCallback(() -> {
            FileUtil.del(unsignApkPath);
            ControlUtils.showMsgDialog("消息提示", "编译任务完成!");
        });

        // 创建apk编译任务，编译完成后调用签名任务。
        ApkToolCompileTask apkToolCompileTask = new ApkToolCompileTask(basePath);
        apkToolCompileTask.setCallback(() -> ThreadUtil.execAsync(apkSignatureTask));

        ThreadUtil.execAsync(apkToolCompileTask);
    }

    @ClickAction("installButton")
    public void installButtonClick(ActionEvent event) {
        if (!DeviceOnlineValidator.verify() || !SelectedIsWorkbenchTabValidator.verify()) return;

        // 获取每个Apk解包后的路径下的/dist/目录
        Apk apk = MainWindow.taskView.getSelectedApk();

        // 不存在则创建，存在则直接返回
        File distDir = FileUtil.mkdir(apk.getBasePath() + "/dist/");

        // 过滤只筛选出后缀是.apk的文件
        File[] files = distDir.listFiles((dir, name) -> name.endsWith(Suffix.POINT_APK));

        // 指定目录下没有.apk文件的情况
        if (files == null || files.length == 0) {
            ControlUtils.showMsgDialog("提示信息", "请先编译APK");
            return;
        }

        // 最终需要进行安装的apk文件对象
        File installFile = files[0];

        // 当有多个apk文件时，获取最新创建时间的那个文件。
        if (files.length > 1) {
            for (int i = 1; i < files.length; i++) {
                long tempCreateMillis = FileUtil.getAttributes(installFile.toPath(), true).creationTime().toMillis();
                long fileCreateMillis = FileUtil.getAttributes(files[i].toPath(), true).creationTime().toMillis();
                if (fileCreateMillis > tempCreateMillis) {
                    installFile = files[i];
                }
            }
        }

        try {
            AndroidDeviceManager.getInstance().getDevice().installPackage(installFile.getAbsolutePath(), true, null);
        } catch (Exception e) {
            ControlUtils.showMsgDialog("提示信息", "安装失败");
        }
    }

    @ClickAction("adbButton")
    public void adbButtonClick(ActionEvent event) {
        if (!DeviceOnlineValidator.verify() || TabAddedValidator.verify("终端")) return;

        IconComboBox.IconComboBoxData selectedItem = (IconComboBox.IconComboBoxData) deviceComboBox.getSelectedItem();
        IDevice device = (IDevice) selectedItem.getData();

        String command;
        if (SystemInfo.isWindows) {
            command = String.format("cmd /k start %s -s %s shell", R.ADB, device.getSerialNumber());
        } else {
            command = String.format("/bin/bash %s %s %s", R.MAC_TERMINAL, R.ADB, device.getSerialNumber());
        }
        String s = RuntimeUtil.execForStr(command);
        System.out.println(s);
    }

    @ClickAction("fileExplorerButton")
    public void fileExplorerButtonClick(ActionEvent event) {
        if (!DeviceOnlineValidator.verify() || TabAddedValidator.verify("文件管理器")) return;
        MainWindow.taskView.addTab("文件管理器", directoryIcon, new DeviceExplorerView(AndroidDeviceManager.getInstance().getDevice()));
    }

    @ClickAction("connectButton")
    public void connectButtonClick(ActionEvent event) {
        // 获取连的设备
        IDevice device = AndroidDeviceManager.getInstance().getDevice();

        // 设备已连接 断开设备
        if (device != null) {
//            deviceLogReceiverManager.stop();
            AndroidDeviceManager.getInstance().setDevice(null);
            setDeviceConnectStateUI(true);
            StaticLog.info("设备已断开连接: {}", device.getName());
            return;
        }

        // 选项卡中没有选择设备
        if (deviceComboBox.getSelectedIndex() == -1) {
            ControlUtils.showMsgDialog("提示", "请先选择设备");
            return;
        } else {
            // 选中设备，将选项栏中的值进行赋值。
            IconComboBox.IconComboBoxData selectedItem = (IconComboBox.IconComboBoxData) deviceComboBox.getSelectedItem();
            device = (IDevice) selectedItem.getData();
        }

        // 选项框中有选中设备，判断设备是否在线。
        if (device.isOnline()) {
            AndroidDeviceManager.getInstance().setDevice(device);
            setDeviceConnectStateUI(false);
            StaticLog.info("设备连接成功: {}", device.getName());
        } else {
            StaticLog.info("设备连接失败: {}", device.getName());
        }
    }

    @ClickAction("refreshButton")
    public void refreshButtonClick(ActionEvent event) {
        // 设备已处于连接状态时不进行刷新
        if (AndroidDeviceManager.getInstance().getDevice() != null) {
            ControlUtils.showMsgDialog("提示", "设备已处于连接状态，请勿进行刷新！");
        } else {
            StaticLog.info("正在刷新中.......");
            AndroidDeviceManager.getInstance().killServer();
            deviceComboBox.removeAllItems();
            deviceComboBox.setSelectedIndex(0);
        }
    }
}