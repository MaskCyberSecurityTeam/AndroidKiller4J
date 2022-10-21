package com.richardtang.androidkiller4j;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.ThreadLocalConnection;
import cn.hutool.log.StaticLog;
import cn.hutool.system.SystemUtil;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.util.SystemInfo;
import com.richardtang.androidkiller4j.constant.R;
import com.richardtang.androidkiller4j.setting.ApplicationSetting;
import com.richardtang.androidkiller4j.ui.rsyntaxtextarea.SmaliTokenMaker;
import com.richardtang.androidkiller4j.ui.rsyntaxtextarea.SyntaxConstantsWrapper;
import com.richardtang.androidkiller4j.util.CompressUtils;
import com.richardtang.androidkiller4j.util.ControlUtils;
import com.richardtang.androidkiller4j.util.FileUtils;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * 程序启动时的加载动画窗口
 *
 * @author RichardTang
 */
public class LoadWindow extends JWindow implements Runnable {

    // 进度条
    private JProgressBar progress;

    // 加载时的提示信息
    private String LOADING_TIP = "加载程序中,请稍候......";

    private String PACK_SUFFIX = "pack.gz";

    public LoadWindow() {
        setLayout(new BorderLayout());

        // 初始化进度条
        progress = new JProgressBar(1, 100);
        progress.setValue(0);
        progress.setStringPainted(true);
        progress.setBackground(Color.white);
        progress.setString(LOADING_TIP);
        progress.putClientProperty("JProgressBar.square", true);

        // 启动界面的Banner

        ImageIcon banner = ControlUtils.getImageIcon(FileUtils.getResource("image/banner.jpg"), 450, 300);

        // 鼠标悬浮显示加载光标
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        // 设置banner和添加进度条
        Container container = getContentPane();
        container.add(new JLabel(banner), BorderLayout.CENTER);
        container.add(progress, BorderLayout.SOUTH);

        // 得到屏幕尺寸。
        Dimension screen = getToolkit().getScreenSize();
        // 窗口适应组件尺寸
        pack();
        // 设置窗口位置
        setLocation((screen.width - getSize().width) / 2, (screen.height - getSize().height) / 2);
    }

    /**
     * 进度条加载线程
     */
    public void run() {
        // 显示进度条加载窗口
        setVisible(true);
        try {
            freeResource();
            loadEditorHighlighting();
        } catch (Exception e) {
            StaticLog.error(e);
        }
        // 释放窗口
        dispose();

        // 运行主窗口
        SwingUtilities.invokeLater(() -> new MainWindow().start());
    }

    /**
     * 释放资源
     */
    public void freeResource() {
        // 释放pack.gz包
        List<File> packGzFiles = FileUtils.loopFiles(R.BIN_DIR, pathname -> pathname.getName().contains(PACK_SUFFIX));

        // 之前已经释放过资源
        if (packGzFiles.size() == 0) {
            // 延迟一秒,不然进度条太快体验不好
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                StaticLog.error(e);
            }
            // 直接进度条拉满
            progress.setValue(100);
            return;
        }

        // 释放资源
        for (int i = 0; i < packGzFiles.size(); i++) {
            String fileFullPath = packGzFiles.get(i).getAbsolutePath();
            String unpackerAfterPath = StrUtil.subBefore(fileFullPath, File.separator, true);
            boolean unpackerFlag = CompressUtils.unpacker(fileFullPath, unpackerAfterPath + File.separator);
            // 解压完成后将.pack.gz包删除。
            if (unpackerFlag) {
                FileUtils.del(packGzFiles.get(i));
            }
            // 更新进度条
            progress.setValue((int) (((i + 1) / (double) packGzFiles.size()) * 100.0));
        }
    }

    /**
     * 加载代码编辑器语法高亮
     */
    public void loadEditorHighlighting() {
        ((AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance())
                .putMapping(SyntaxConstantsWrapper.SYNTAX_STYLE_SMALI, SmaliTokenMaker.class.getName());
    }

    /**
     * 启动加载窗口，并启动进度条加载。
     */
    public void start() {
        // 窗口前端显示
        this.toFront();

        // 启动进度条加载线程
        ThreadUtil.execAsync(this);
    }
}