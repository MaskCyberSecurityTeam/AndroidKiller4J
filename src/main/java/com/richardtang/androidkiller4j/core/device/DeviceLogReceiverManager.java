package com.richardtang.androidkiller4j.core.device;

import cn.hutool.core.thread.ThreadUtil;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.logcat.LogCatListener;
import com.android.ddmlib.logcat.LogCatMessage;
import com.android.ddmlib.logcat.LogCatReceiverTask;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.List;

/**
 * Locat消息监听处理类，启动Logcat监听任务和停止Logcat监听任务都在该类中进行。
 *
 * @author RichardTang
 */
@Component
public class DeviceLogReceiverManager {

    private List<LogCatMessage> data;

    private LogCatReceiverTask logCatReceiverTask;

    private final LogCatListener deviceLogListener;

    public DeviceLogReceiverManager() {
        deviceLogListener = msgList -> SwingUtilities.invokeLater(() -> data.addAll(msgList));
    }

    /**
     * 开启logcat监听
     *
     * @param iDevice 需要监听的设备
     * @param data    数据存储集合
     */
    public void run(IDevice iDevice, List<LogCatMessage> data) {
        if (data != null) {
            this.data = data;
        }
        logCatReceiverTask = new LogCatReceiverTask(iDevice);
        logCatReceiverTask.addLogCatListener(deviceLogListener);
        ThreadUtil.execAsync(logCatReceiverTask);
    }

    /**
     * 暂停logcat的监听
     */
    public void stop() {
        logCatReceiverTask.stop();
        logCatReceiverTask.removeLogCatListener(deviceLogListener);
        logCatReceiverTask = null;
    }
}