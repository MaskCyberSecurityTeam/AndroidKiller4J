package com.richardtang.androidkiller4j.core.ddmlib;

import cn.hutool.core.util.RuntimeUtil;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.richardtang.androidkiller4j.constant.ResPath;
import com.richardtang.androidkiller4j.listener.AndroidDeviceChangeListener;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Android设备管理类，对Google-ddmlib包做的一些简单封装。
 *
 * @author RichardTang
 */
@Data
@Component
public class AndroidDeviceManager implements InitializingBean {

    private IDevice            device;
    private AndroidDebugBridge bridge;

    @Autowired
    private AndroidDeviceChangeListener deviceListener;

    public AndroidDeviceManager() {
        AndroidDebugBridge.init(false);
        bridge = AndroidDebugBridge.createBridge(ResPath.ADB, false);
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
        RuntimeUtil.exec(ResPath.ADB + " kill-server");
    }

    @Override
    public void afterPropertiesSet() {
        AndroidDebugBridge.addDeviceChangeListener(deviceListener);
    }
}