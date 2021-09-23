package com.funnysec.richardtang.androidkiller4j.core.device;

import cn.hutool.core.thread.ThreadUtil;
import com.android.ddmlib.IDevice;
import com.funnysec.richardtang.androidkiller4j.core.device.process.ProcessMessage;
import com.funnysec.richardtang.androidkiller4j.core.device.process.ProcessMessageListener;
import com.funnysec.richardtang.androidkiller4j.core.device.process.ProcessMessageReceiverTask;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.List;

/**
 * 进程消息处理
 *
 * @author RichardTang
 */
@IocBean
public class DeviceProcessReceiverManager {

    private List<ProcessMessage> data;

    private ProcessMessageReceiverTask processMessageReceiverTask;

    private final ProcessMessageListener processMessageListener = new ProcessMessageListener() {
        @Override
        public void log(List<ProcessMessage> msgList) {
            data.clear();
            data.addAll(msgList);
        }
    };

    public void run(IDevice iDevice, List<ProcessMessage> data) {
        if (data != null) {
            this.data = data;
        }

        processMessageReceiverTask = new ProcessMessageReceiverTask(iDevice);
        processMessageReceiverTask.addLogCatListener(processMessageListener);
        ThreadUtil.execAsync(processMessageReceiverTask);
    }

    public void stop() {
        processMessageReceiverTask.stop();
        processMessageReceiverTask.removeLogCatListener(processMessageListener);
        processMessageReceiverTask = null;
    }
}
