package com.richardtang.androidkiller4j.ddmlib;

import cn.hutool.core.util.RuntimeUtil;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.richardtang.androidkiller4j.constant.R;
import com.richardtang.androidkiller4j.ddmlib.listener.AndroidDeviceChangeListener;
import lombok.Data;

/**
 * Android设备管理类，对Google-ddmlib包做的一些简单封装。
 *
 * @author RichardTang
 */
@Data
public class AndroidDeviceManager {

    // 当前选中的设备
    private IDevice device;

    // 设备桥
    private AndroidDebugBridge bridge;

    // 单利
    private static final AndroidDeviceManager instance = new AndroidDeviceManager();

    private AndroidDeviceManager() {
        AndroidDebugBridge.init(false);
        bridge = AndroidDebugBridge.createBridge(R.ADB, false);
        AndroidDebugBridge.addDeviceChangeListener(new AndroidDeviceChangeListener());
    }

    public static AndroidDeviceManager getInstance() {
        return instance;
    }

    /**
     * 获得设备列表
     *
     * @return 设备数组
     */
    public IDevice[] getDevices() {
        return bridge.getDevices();
    }

    /**
     * 判断ADB是否已经连接指定设备
     *
     * @return true:已连接 false:未连接
     */
    public boolean isConnected() {
        return device != null;
    }

    /**
     * 执行adb kill-server命令
     */
    public void killServer() {
        RuntimeUtil.exec(R.ADB + " kill-server");
    }
}