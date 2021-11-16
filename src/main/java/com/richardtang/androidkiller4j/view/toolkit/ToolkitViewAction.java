package com.richardtang.androidkiller4j.view.toolkit;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.system.SystemUtil;
import com.android.ddmlib.IDevice;
import com.richardtang.androidkiller4j.aop.annotation.AssertDeviceOnline;
import com.richardtang.androidkiller4j.aop.annotation.AssertTab;
import com.richardtang.androidkiller4j.aop.annotation.AssertWorkbenchTab;
import com.richardtang.androidkiller4j.bean.Apk;
import com.richardtang.androidkiller4j.constant.ResPath;
import com.richardtang.androidkiller4j.core.ddmlib.AndroidDeviceManager;
import com.richardtang.androidkiller4j.task.*;
import com.richardtang.androidkiller4j.ui.control.IconComboBox;
import com.richardtang.androidkiller4j.util.ControlUtil;
import com.richardtang.androidkiller4j.view.device.DeviceExplorerView;
import com.richardtang.androidkiller4j.view.device.DeviceLogView;
import com.richardtang.androidkiller4j.view.device.DeviceProcessView;
import com.richardtang.androidkiller4j.view.signature.SignatureView;
import com.richardtang.androidkiller4j.view.task.TaskView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.event.ActionEvent;
import java.io.File;

/**
 * {@link ToolkitView}视图中的组件对应的事件类
 *
 * @author RichardTang
 */
@Component
@SuppressWarnings("all")
public class ToolkitViewAction {

    @Autowired
    private TaskView taskView;

    @Autowired
    private ToolkitView toolkitView;

    @Autowired
    private SignatureView signatureView;

    @Autowired
    private DeviceLogView deviceLogView;

    @Autowired
    private DeviceProcessView deviceProcessView;

    @Autowired
    private AndroidDeviceManager androidDeviceManager;

    /**
     * 打开按钮,选中需要进行反编译的APK,反编译结束后会被添加到TaskView中。
     *
     * @param event 事件对象
     */
    public void openButtonOnMouseClick(ActionEvent event) {
        File apkFile = ControlUtil.chooserApkFileDialog();

        // 未选中APK，该事件直接结束。
        if (FileUtil.isEmpty(apkFile)) {
            return;
        }

        // 以APK的名称作为反编译后的输出目录，除去后边的.apk后缀。
        String apkName                    = apkFile.getName().replace(".apk", "");
        String apkToolDecompilerOutputDir = ResPath.PROJECT_DIR + apkName;

        // dex2jar任务，将dex抓换为.class文件
        Dex2JarTask dex2JarTask = new Dex2JarTask(apkFile.getAbsolutePath(), ResPath.PROJECT_SRC_DIR);
        dex2JarTask.setCallback(() -> {
            String jarPath      = ResPath.PROJECT_SRC_DIR + apkName + ".jar";
            String jarUnzipPath = ResPath.PROJECT_SRC_DIR + apkName + "/";
            ZipUtil.unzip(jarPath, jarUnzipPath);
            FileUtil.del(jarPath);
        });

        // apk解包反编译任务
        ApkToolDecompileTask apkToolDecompileTask = new ApkToolDecompileTask(apkFile.getAbsolutePath(), apkToolDecompilerOutputDir);
        apkToolDecompileTask.setCallback(() -> {
            ThreadUtil.execAsync(dex2JarTask);
            // TODO 这里后边编写自动监听project目录，然后自动刷新加入apk。
            // 避免任务表中存在相同的apk，判断是否存在旧的，存在则删除。
            Integer index = taskView.findTaskApkByApkBasePath(apkToolDecompilerOutputDir);
            if (index != -1) {
                taskView.removeTaskByIndex(index);
            }
            taskView.addRow(new Apk(apkToolDecompilerOutputDir));
        });
        ThreadUtil.execAsync(apkToolDecompileTask);
    }

    /**
     * 关于按钮，显示作者信息。
     *
     * @param event 事件对象
     */
    public void aboutButtonOnMouseClick(ActionEvent event) {
        ControlUtil.showMsgDialog("关于", "作者: RichardTang");
    }

    /**
     * 签名按钮
     *
     * @param event 事件对象
     */
    @AssertTab
    public void signButtonOnMouseClick(ActionEvent event) {
        taskView.addTab("签名", ControlUtil.getSVGIcon("signature.svg", 20, 20), signatureView);
    }

    /**
     * 查壳按钮
     *
     * @param event 事件对象
     */
    public void findShellButtonOnMouseClick(ActionEvent event) {
        File file = ControlUtil.chooserApkFileDialog();

        // 未选中文件
        if (file == null) {
            return;
        } else {
            ThreadUtil.execAsync(new ApkInspectShellTask(file));
        }
    }

    /**
     * 日志按钮，点击后打开{@link DeviceLogView}页面。
     *
     * @param event 事件对象
     */
    @AssertDeviceOnline
    @AssertTab("日志")
    public void logButtonOnMouseClick(ActionEvent event) {
        taskView.addTab("日志列表", ControlUtil.getSVGIcon("bug.svg", 20, 20), deviceLogView);
    }


    /**
     * 进程按钮，点击后打开{@link DeviceProcessView}页面。
     *
     * @param event 事件对象
     */
    @AssertDeviceOnline
    @AssertTab("进程")
    public void processButtonOnMouseClick(ActionEvent event) {
        taskView.addTab("进程列表", ControlUtil.getSVGIcon("process.svg", 20, 20), deviceProcessView);
    }

    /**
     * 编译按钮
     *
     * @param event 事件对象
     */
    @AssertWorkbenchTab
    public void compileButtonOnMouseClick(ActionEvent event) {
        Apk    apk           = taskView.getSelectedApk();
        String basePath      = apk.getBasePath();
        String unsignApkPath = basePath + "/dist/" + apk.getFileName();
        String signApkPath   = basePath + "/dist/sign_" + apk.getFileName();

        // 创建apk签名任务，等待被编译任务的调用，签名任务结束后进行弹窗提示。
        ApkSignatureTask apkSignatureTask = new ApkSignatureTask(unsignApkPath, signApkPath, "androidkiller4j.keystore");

        apkSignatureTask.setCallback(() -> {
            FileUtil.del(unsignApkPath);
            ControlUtil.showMsgDialog("消息提示", "编译任务完成!");
        });

        // 创建apk编译任务，编译完成后调用签名任务。
        ApkToolCompileTask apkToolCompileTask = new ApkToolCompileTask(basePath);
        apkToolCompileTask.setCallback(() -> {
            ThreadUtil.execAsync(apkSignatureTask);
        });

        ThreadUtil.execAsync(apkToolCompileTask);
    }

    @AssertDeviceOnline
    @AssertWorkbenchTab
    public void installButtonOnMouseClick(ActionEvent event) {
        // 获取每个Apk解包后的路径下的/dist/目录
        Apk apk = taskView.getSelectedApk();

        // 不存在则创建，存在则直接返回
        File distDir = FileUtil.mkdir(apk.getBasePath() + "/dist/");

        // 过滤只筛选出后缀是.apk的文件
        File[] files = distDir.listFiles((dir, name) -> name.endsWith(".apk"));

        // 指定目录下没有.apk文件的情况
        if (files == null || files.length == 0) {
            ControlUtil.showMsgDialog("提示信息", "请先编译APK");
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
            ControlUtil.showMsgDialog("提示信息", "安装失败");
        }
    }

    @AssertDeviceOnline
    @AssertTab("终端")
    public void adbButtonOnMouseClick(ActionEvent event) {
        IconComboBox.IconComboBoxData selectedItem =
                (IconComboBox.IconComboBoxData) toolkitView.getDeviceIconComboBox().getSelectedItem();

        if (SystemUtil.getOsInfo().isWindows()) {
            String command = String.format("cmd /k start %s -s %s shell", ResPath.ADB, selectedItem.getText());
            RuntimeUtil.exec(command);
        } else {
            String command = String.format("/bin/bash %s %s %s", ResPath.MAC_TERMINAL, ResPath.ADB, selectedItem.getText());
            RuntimeUtil.exec(command);
        }
    }

    @AssertDeviceOnline
    @AssertTab("文件管理器")
    public void fileExplorerOnMouseClick(ActionEvent event) {
        DeviceExplorerView deviceExplorerView = new DeviceExplorerView(androidDeviceManager.getDevice());
        taskView.addTab("文件管理器", ControlUtil.getSVGIcon("about.svg", 20, 20), deviceExplorerView);
    }

    /**
     * 设备连接按钮，对选中的设备进行连接。
     * 其实就是吧选中的Device对象赋值到androidDeviceManager中。
     *
     * @param event 时间对象
     */
    public void deviceConnectButtonOnMouseClick(ActionEvent event) {
        // 获取连的设备
        IDevice device = androidDeviceManager.getDevice();

        // 设备已连接 断开设备
        if (device != null) {
//            deviceLogReceiverManager.stop();
            androidDeviceManager.setDevice(null);
            toolkitView.setDeviceConnectStateUI(true);
            System.out.println("设备已断开连接: " + device.getName());
            return;
        }

        // 选项卡中没有选择设备
        if (toolkitView.getDeviceIconComboBox().getSelectedIndex() == -1) {
            ControlUtil.showMsgDialog("提示", "请先选择设备");
            return;
        } else {
            // 选中设备，将选项栏中的值进行赋值。
            IconComboBox.IconComboBoxData selectedItem =
                    (IconComboBox.IconComboBoxData) toolkitView.getDeviceIconComboBox().getSelectedItem();
            device = (IDevice) selectedItem.getData();
        }

        // 选项框中有选中设备，判断设备是否在线。
        if (device.isOnline()) {
            androidDeviceManager.setDevice(device);
            toolkitView.setDeviceConnectStateUI(false);
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
    public void deviceRefreshButtonOnMouseClick(ActionEvent event) {
        // 设备已处于连接状态时不进行刷新
        if (androidDeviceManager.getDevice() != null) {
            ControlUtil.showMsgDialog("提示", "设备已处于连接状态，请勿进行刷新！");
        } else {
            System.out.println("正在刷新中.......");
            androidDeviceManager.killServer();
            toolkitView.getDeviceIconComboBox().removeAllItems();
            toolkitView.getDeviceIconComboBox().addItem(toolkitView.getDeviceIconComboBoxDefaultOption());
            toolkitView.getDeviceIconComboBox().setSelectedIndex(0);
        }
    }
//
//    public void settingButtonOnMouseClick(MouseEvent event) {
//        taskView.getRootPane().getTabs().add(settingView.getRootPane());
//    }
}