package com.funnysec.richardtang.androidkiller4j.core.device;

import cn.hutool.core.thread.ThreadUtil;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.logcat.LogCatFilter;
import com.android.ddmlib.logcat.LogCatListener;
import com.android.ddmlib.logcat.LogCatMessage;
import com.android.ddmlib.logcat.LogCatReceiverTask;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeviceLogReceiverManager {

    private List data;

    private LogCatReceiverTask logCatReceiverTask;

    private LogCatFilter logCatFilter;

    private final DeviceLogListener deviceLogListener = new DeviceLogListener();

    public void run(IDevice iDevice, List data) {
        if (data != null) {
            this.data = data;
        }

        logCatReceiverTask = new LogCatReceiverTask(iDevice);
        logCatReceiverTask.addLogCatListener(deviceLogListener);
        ThreadUtil.execAsync(logCatReceiverTask);
    }

    public void stop() {
        logCatReceiverTask.stop();
        logCatReceiverTask.removeLogCatListener(deviceLogListener);
        logCatReceiverTask = null;
    }

    public class DeviceLogListener implements LogCatListener {

        @Override
        public void log(List<LogCatMessage> msgList) {
            data.addAll(msgList);
        }
    }
}