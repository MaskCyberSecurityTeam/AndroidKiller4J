package com.richardtang.androidkiller4j.task.watch;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.watch.SimpleWatcher;
import cn.hutool.core.io.watch.WatchMonitor;
import com.richardtang.androidkiller4j.constant.Cert;
import com.richardtang.androidkiller4j.constant.R;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * 证书文件创建-监控
 *
 * @author RichardTang
 */
public final class CertFileWatchTask extends BaseFileWatchTask {

    private final JComboBox<String> selectKeystoreComboBox;

    public CertFileWatchTask(final JComboBox<String> selectKeystoreComboBox) {
        super(new File(R.CONFIG_DIR));
        this.selectKeystoreComboBox = selectKeystoreComboBox;
    }

    @Override
    protected void startAfter() {
        loadKeyStore();
    }

    @Override
    protected void onCreate(WatchEvent<?> watchEvent, Path path) {
        loadKeyStore();
    }

    @Override
    protected void onModify(WatchEvent<?> watchEvent, Path path) {
        loadKeyStore();
    }

    @Override
    protected void onDelete(WatchEvent<?> watchEvent, Path path) {
        loadKeyStore();
    }

    /**
     * 加载证书文件到签名视图
     */
    private void loadKeyStore() {
        selectKeystoreComboBox.removeAllItems();
        for (File f : FileUtil.ls(R.CONFIG_DIR)) {
            if (!f.getName().contains(Cert.CERT_FILE_SUFFIX)) continue;
            selectKeystoreComboBox.addItem(f.getName());
        }
    }
}
