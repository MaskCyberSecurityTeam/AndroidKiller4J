package com.richardtang.androidkiller4j.core.device;

import cn.hutool.core.thread.ThreadUtil;
import com.android.ddmlib.IDevice;
import com.richardtang.androidkiller4j.core.device.process.ProcessMessage;
import com.richardtang.androidkiller4j.core.device.process.ProcessMessageListener;
import com.richardtang.androidkiller4j.core.device.process.ProcessMessageReceiverTask;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.List;

/**
 * 进程消息监听处理类，启动进程监听任务和停止进程监听任务都在该类中进行。
 *
 * @author RichardTang
 */
@Component
public class DeviceProcessReceiverManager {

    private List<ProcessMessage>       data;
    private ProcessMessageListener     processMessageListener;
    private ProcessMessageReceiverTask processMessageReceiverTask;

    public DeviceProcessReceiverManager() {
        processMessageListener = msgList -> SwingUtilities.invokeLater(() -> {
            data.clear();
            data.addAll(msgList);
        });
    }

    /**
     * 开启进程监听
     *
     * @param iDevice 设备对象
     * @param data    存储数据的集合
     */
    public void run(IDevice iDevice, List<ProcessMessage> data) {
        if (data != null) {
            this.data = data;
        }
        processMessageReceiverTask = new ProcessMessageReceiverTask(iDevice);
        processMessageReceiverTask.addLogCatListener(processMessageListener);
        ThreadUtil.execAsync(processMessageReceiverTask);
    }

    /**
     * 停止进程监听
     */
    public void stop() {
        processMessageReceiverTask.stop();
        processMessageReceiverTask.removeLogCatListener(processMessageListener);
        processMessageReceiverTask = null;
    }
}
