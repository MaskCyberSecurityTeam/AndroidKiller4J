package com.funnysec.richardtang.androidkiller4j.core.ddmlib;

import cn.hutool.core.util.RuntimeUtil;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.funnysec.richardtang.androidkiller4j.config.ResourcePathConfig;
import com.funnysec.richardtang.androidkiller4j.listener.AndroidDeviceChangeListener;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Android设备管理类，基于Google-ddmlib开发包。
 *
 * @author RichardTang
 */
@Data
@Component
public class AndroidDeviceManager implements InitializingBean {

    // 设备对象，当前adb中所选中的设备会存储在这个属性中。
    private IDevice device;

    private AndroidDebugBridge bridge;

    @Autowired
    private AndroidDeviceChangeListener deviceListener;

    public AndroidDeviceManager() {
        AndroidDebugBridge.init(false);
        initBridge();
    }

    /**
     * 当前类在Spring初始化完毕后调用
     */
    @Override
    public void afterPropertiesSet() {
        AndroidDebugBridge.addDeviceChangeListener(deviceListener);
    }

    /**
     * 初始化ADB
     */
    private void initBridge() {
        bridge = AndroidDebugBridge.createBridge(
                ResourcePathConfig.ADB_BIN, false, 10L, TimeUnit.SECONDS
        );
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
        RuntimeUtil.exec(ResourcePathConfig.ADB_BIN + " kill-server");
    }
}