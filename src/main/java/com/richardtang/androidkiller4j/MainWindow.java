package com.richardtang.androidkiller4j;

import cn.hutool.core.io.FileUtil;
import com.richardtang.androidkiller4j.bean.Apk;
import com.richardtang.androidkiller4j.constant.Suffix;
import com.richardtang.androidkiller4j.setting.ApplicationSetting;
import com.richardtang.androidkiller4j.view.*;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;

/**
 * 应用主窗口
 *
 * @author RichardTang
 */
public class MainWindow extends JFrame {

    // 注意View的声明顺序是有要求的
    public static final TaskView      taskView      = new TaskView();
    public static final ToolkitView   toolkitView   = new ToolkitView();
    public static final ConsoleView   consoleView   = new ConsoleView();
    public static final SignatureView signatureView = new SignatureView();
    public static final SettingView   settingView   = new SettingView();
    public static final MainView      mainView      = new MainView();

    public MainWindow() {
        // 设置窗口属性
        setTitle(ApplicationSetting.APP_NAME);
        setSize(ApplicationSetting.getInstance().getMainDefSizeDimension());
        setMinimumSize(ApplicationSetting.getInstance().getMainMinSizeDimension());

        // 关闭动作
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 添加窗口关闭事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                System.exit(0);
            }
        });

        // 添加文件拖拽处理
        new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, new ApkTaskDropTargetAdapter());

        // 设置显示的主视图
        setContentPane(mainView);
    }

    /**
     * 启动应用窗口
     */
    public void start() {
        setVisible(true);
    }

    /**
     * 用于处理Apk文件拖拽进工具后自动添加至任务列表
     */
    private static class ApkTaskDropTargetAdapter extends DropTargetAdapter {
        @SneakyThrows
        @Override
        public void drop(DropTargetDropEvent dtde) {
            if (!dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                // 拒绝拖拽来的数据
                dtde.rejectDrop();
            }

            // 接受处理该拖拽数据
            dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

            // 拖拽进来的数据，可能是多个文件也可能是文件夹。
            List<File> files = (List) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));

            // 校验后缀，为apk的添加至任务中
            for (File file : files) {
                if (file.isDirectory()) {
                    return;
                }
                // 符合apk的文件添加到任务表中
                if (Suffix.APK.equals(FileUtil.getSuffix(file))) {
                    MainWindow.toolkitView.apkDecompiler(file);
                }
            }

            // 指示拖拽操作已完成
            dtde.dropComplete(true);
        }
    }
}
