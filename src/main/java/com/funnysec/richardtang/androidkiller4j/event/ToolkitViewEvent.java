package com.funnysec.richardtang.androidkiller4j.event;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.android.ddmlib.IDevice;
import com.funnysec.richardtang.androidkiller4j.aop.annotation.AssertDeviceOnline;
import com.funnysec.richardtang.androidkiller4j.aop.annotation.AssertTab;
import com.funnysec.richardtang.androidkiller4j.aop.annotation.AssertWorkbenchTab;
import com.funnysec.richardtang.androidkiller4j.config.ResourcePathConfig;
import com.funnysec.richardtang.androidkiller4j.constant.FxConstant;
import com.funnysec.richardtang.androidkiller4j.constant.Icon;
import com.funnysec.richardtang.androidkiller4j.core.ddmlib.AndroidDeviceManager;
import com.funnysec.richardtang.androidkiller4j.pojo.Apk;
import com.funnysec.richardtang.androidkiller4j.task.ApkInspectShellTask;
import com.funnysec.richardtang.androidkiller4j.task.ApkSignatureTask;
import com.funnysec.richardtang.androidkiller4j.task.ApkToolCompileTask;
import com.funnysec.richardtang.androidkiller4j.task.ApkToolDecompileTask;
import com.funnysec.richardtang.androidkiller4j.util.FxUtil;
import com.funnysec.richardtang.androidkiller4j.view.SignatureView;
import com.funnysec.richardtang.androidkiller4j.view.TaskView;
import com.funnysec.richardtang.androidkiller4j.view.ToolkitView;
import com.funnysec.richardtang.androidkiller4j.view.device.DeviceExplorerView;
import com.funnysec.richardtang.androidkiller4j.view.device.DeviceLogView;
import com.funnysec.richardtang.androidkiller4j.view.device.DeviceProcessView;
import com.funnysec.richardtang.androidkiller4j.view.preference.PreferenceView;
import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.TerminalTab;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import java.io.File;
import java.io.FilenameFilter;

/**
 * {@link ToolkitView}视图中的组件对应的事件类
 *
 * @author RichardTang
 */
@IocBean
@SuppressWarnings("all")
public class ToolkitViewEvent {

    @Inject
    private TaskView taskView;

    @Inject
    private ToolkitView toolkitView;

    @Inject
    private SignatureView signatureView;

    @Inject
    private DeviceLogView deviceLogView;

    @Inject
    private DeviceProcessView deviceProcessView;

    @Inject
    private PreferenceView settingView;

    @Inject
    private AndroidDeviceManager androidDeviceManager;

    private final TerminalBuilder TERMINAL_BUILDER = new TerminalBuilder();

    /**
     * 打开按钮,选中需要反编译处理的APK,APK反编译结束后会被添加到TaskView中。
     *
     * @param event 事件对象
     */
    public void openButtonOnMouseClick(MouseEvent event) {
        // 选择APK
        FxConstant.FILE_CHOOSER.setTitle("选择APK");
        FxConstant.FILE_CHOOSER.getExtensionFilters().add(FxConstant.APK_EXT_FILETER);
        File apkFile = FxConstant.FILE_CHOOSER.showOpenDialog(toolkitView.getRootPane().getScene().getWindow());

        // 未选中APK，该事件直接结束。
        if (FileUtil.isEmpty(apkFile)) {
            return;
        }

        // 已APK的名称作为outputDir目录，除去后边的.apk后缀
        String dirName   = apkFile.getName().replace(".apk", "");
        String outputDir = ResourcePathConfig.PROJECT + dirName;

        ApkToolDecompileTask apkToolDecompileTask = new ApkToolDecompileTask(apkFile.getAbsolutePath(), outputDir);
        // TODO 这里后边编写自动监听project目录，然后自动刷新加入apk。
        apkToolDecompileTask.setOnScheduled(e -> Platform.runLater(() -> taskView.getListView().getItems().add(new Apk(outputDir))));
        ThreadUtil.execAsync(apkToolDecompileTask);
    }

    /**
     * 关于按钮，显示作者信息。
     *
     * @param event 事件对象
     */
    public void aboutButtonOnMouseClick(MouseEvent event) {
        FxUtil.alert("关于", "作者: RichardTang");
    }

    public void customButtonOnMouseClick(MouseEvent event) {

    }

    /**
     * 签名按钮
     *
     * @param event 事件对象
     */
    @AssertTab("签名")
    public void signButtonOnMouseClick(MouseEvent event) {
        // 这里需要先添加到tab，然后再调用下边的command
        taskView.getRootPane().getTabs().add(signatureView.getRootPane());
    }

    /**
     * 查壳按钮
     *
     * @param event 事件对象
     */
    public void findShellButtonOnMouseClick(MouseEvent event) {
        FxConstant.FILE_CHOOSER.setTitle("选择需要查壳的APK");
        FxConstant.FILE_CHOOSER.getExtensionFilters().add(FxConstant.APK_EXT_FILETER);
        File apkFile = FxConstant.FILE_CHOOSER.showOpenDialog(toolkitView.getRootPane().getScene().getWindow());

        // 未选中文件
        if (ObjectUtil.isNull(apkFile)) {
            return;
        }

        ApkInspectShellTask apkInspectShellTask = new ApkInspectShellTask(apkFile);
        apkInspectShellTask.setOnSucceeded(e -> {
            FxUtil.alert("提示信息", (String) e.getSource().getValue());
        });
        ThreadUtil.execAsync(apkInspectShellTask);
    }

    /**
     * 日志按钮，点击后打开{@link DeviceLogView}页面。
     *
     * @param event 事件对象
     */
    @AssertDeviceOnline
    @AssertTab("日志")
    public void logButtonOnMouseClick(MouseEvent event) {
        taskView.getRootPane().getTabs().add(deviceLogView.getRootPane());
    }

    /**
     * 进程按钮，点击后打开{@link DeviceProcessView}页面。
     *
     * @param event 事件对象
     */
    @AssertDeviceOnline
    @AssertTab("进程")
    public void processButtonOnMouseClick(MouseEvent event) {
        taskView.getRootPane().getTabs().add(deviceProcessView.getRootPane());
    }

    /**
     * 编译按钮
     *
     * @param event 事件对象
     */
    @AssertWorkbenchTab
    public void compileButtonOnMouseClick(MouseEvent event) {
        Object userData = taskView.getRootPane().getSelectionModel().getSelectedItem().getUserData();

        Apk apk           = (Apk) userData;
        String basePath      = apk.getBasePath();
        String unsignApkPath = basePath + "/dist/" + apk.getFileName();
        String signApkPath   = basePath + "/dist/sign_" + apk.getFileName();

        // 创建apk签名任务，等待被编译任务的调用，签名任务结束后进行弹窗提示。
        ApkSignatureTask apkSignatureTask = new ApkSignatureTask(unsignApkPath, signApkPath, "androidkiller4j.keystore");
        apkSignatureTask.setOnSucceeded(e -> {
            FileUtil.del(unsignApkPath);
            Platform.runLater(() -> FxUtil.alert("提示信息", "编译完成"));
        });

        // 创建apk编译任务，编译完成后调用签名任务。
        ApkToolCompileTask apkToolCompileTask = new ApkToolCompileTask(basePath);
        apkToolCompileTask.setOnSucceeded(e -> ThreadUtil.execAsync(apkSignatureTask));
        ThreadUtil.execAsync(apkToolCompileTask);
    }

    @AssertDeviceOnline
    @AssertWorkbenchTab
    public void installButtonOnMouseClick(MouseEvent event) {
        // 获取每个Apk解包后的路径下的/dist/目录
        Apk apk = (Apk) taskView.getSelectTabUserData();

        // 不存在则创建，存在则直接返回
        File distDir = FileUtil.mkdir(apk.getBasePath() + "/dist/");

        // 过滤只筛选出后缀是.apk的文件
        File[] files = distDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.endsWith(".apk");
            }
        });

        // 指定目录下没有.apk的文件
        if (files == null || files.length == 0) {
            FxUtil.alert("提示信息", "请先进行APK编译");
            return;
        }

        // 最终需要进行安装的apk文件对象
        File installFile = files[0];

        // 当有多个apk文件时，获取最新创建时间的那个文件。
        if (files.length > 1) {
            for (int i = 1; i < files.length; i++) {
                long tempCreateMillis = FileUtil.getAttributes(installFile.toPath(), false).creationTime().toMillis();
                long fileCreateMillis = FileUtil.getAttributes(files[i].toPath(), false).creationTime().toMillis();
                if (fileCreateMillis > tempCreateMillis) {
                    installFile = files[i];
                }
            }
        }

        try {
            androidDeviceManager.getDevice().installPackage(installFile.getAbsolutePath(), true, null);
        } catch (Exception e) {
            FxUtil.alert("提示信息", "安装失败");
        }
    }

    @AssertDeviceOnline
    @AssertTab("终端")
    public void bashButtonOnMouseClick(MouseEvent event) {
        // 启动一个终端
        TerminalTab terminal = TERMINAL_BUILDER.newTerminal();
        terminal.setUserData("终端");
        terminal.setText("BASH");
        terminal.setGraphic(Icon.TASK_VIEW_BASH);

        // clear 回车 adb -s deviceName shell
        String command = String.format("clear\r%s -s %s shell\r",
                ResourcePathConfig.ADB_BIN, androidDeviceManager.getDevice().getName()
        );

        // 这里需要先添加到tab，然后再调用下边的command
        taskView.getRootPane().getTabs().add(terminal);

        // TODO 滚动条无法拖动，为组件bug，目前没有提供修复版本。
        terminal.onTerminalFxReady(() -> terminal.getTerminal().command(command));
    }

    @AssertDeviceOnline
    @AssertTab("文件管理器")
    public void fileExplorerOnMouseClick(MouseEvent event) {
        DeviceExplorerView deviceExplorerView = new DeviceExplorerView(androidDeviceManager.getDevice());
        taskView.getRootPane().getTabs().add(deviceExplorerView.getRootPane());
    }

    /**
     * 设备连接按钮，对选中的设备进行连接。
     * 其实就是吧选中的Device对象赋值到androidDeviceManager中。
     *
     * @param event 时间对象
     */
    public void deviceConnectButtonOnMouseClick(MouseEvent event) {
        // 获取连的设备
        IDevice device = androidDeviceManager.getDevice();

        // 设备已连接
        if (device != null) {

            // 断开设备
//            deviceLogReceiverManager.stop();
            androidDeviceManager.setDevice(null);
            toolkitView.getDeviceConnectButton().setText("连接");
            toolkitView.getDeviceConnectButton().setGraphic(Icon.TOOLKIT_VIEW_CONNECT);
            toolkitView.getDeviceChoiceBox().getSelectionModel().clearSelection();
            System.out.println("设备已断开连接: " + device.getName());
            return;
        }

        // 选项卡中没有选择设备
        device = toolkitView.getDeviceChoiceBox().getSelectionModel().getSelectedItem();
        if (device == null) {
            FxUtil.alert("提示", "请先选择设备");
            return;
        }

        // 选项框中有选中设备，判断设备是否在线。
        if (device.isOnline()) {
            androidDeviceManager.setDevice(device);
            toolkitView.getDeviceConnectButton().setText("断开连接");
            toolkitView.getDeviceConnectButton().setGraphic(Icon.TOOLKIT_VIEW_DISCONNECT);
            System.out.println("设备连接成功: " + device.getName());
        } else {
            System.out.println("设备连接失败: " + device.getName());
        }
    }

    /**
     * 设备刷新按钮
     *
     * @param event 事件对象
     */
    public void deviceRefreshButtonOnMouseClick(MouseEvent event) {
        // 设备已处于连接状态时不进行刷新
        if (androidDeviceManager.getDevice() != null) {
            FxUtil.alert("提示", "设备已处于连接状态，请勿进行刷新！");
        } else {
            System.out.println("刷新获取设备中");
            toolkitView.getDeviceChoiceBox().getItems().clear();
            toolkitView.getDeviceChoiceBox().getItems().addAll(androidDeviceManager.getDevices());
            androidDeviceManager.killServer();
            System.out.println("获取设备结束");
        }
    }

    public void settingButtonOnMouseClick(MouseEvent event) {
        taskView.getRootPane().getTabs().add(settingView.getRootPane());
    }
}