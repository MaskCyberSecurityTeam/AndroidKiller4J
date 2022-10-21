package com.richardtang.androidkiller4j.view.device;

import cn.hutool.core.thread.ThreadUtil;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.logcat.LogCatListener;
import com.android.ddmlib.logcat.LogCatMessage;
import com.android.ddmlib.logcat.LogCatReceiverTask;
import com.richardtang.androidkiller4j.ui.action.ClickAction;
import com.richardtang.androidkiller4j.ui.action.ClickActionInstaller;
import com.richardtang.androidkiller4j.ui.panel.CommonTablePanel;
import lombok.Data;

import javax.swing.*;
import java.awt.event.ActionEvent;

@Data
public class DeviceLogView extends CommonTablePanel<LogCatMessage> {

    private IDevice iDevice;

    // 开始、暂停
    public JButton switchButton = new JButton("开始");

    // 日志收集任务类，控制任务的开始和暂停。
    private LogCatReceiverTask logCatReceiverTask;

    // 定义日志收集到的数据需要存放的位置
    private final LogCatListener logCatListener = msgList -> table.addRows(msgList);

    public DeviceLogView(IDevice iDevice) {
        super();
        this.iDevice = iDevice;

        // 设置按钮为正方形
        switchButton.putClientProperty("JButton.buttonType", "square");
        toolBarPanel.add(switchButton);

        // 绑定事件
        ClickActionInstaller.bind(this);
    }

    @Override
    public Object bindTableColumnValue(LogCatMessage logCatMessage, int columnIndex) {
        return switch (columnIndex) {
            case 1 -> logCatMessage.getHeader().getAppName();
            case 2 -> logCatMessage.getMessage();
            case 3 -> logCatMessage.getHeader().getLogLevel().getStringValue();
            case 4 -> logCatMessage.getHeader();
            case 5 -> logCatMessage.getHeader().getTag();
            case 6 -> logCatMessage.getHeader().getTid();
            default -> logCatMessage.getHeader().getTimestamp();
        };
    }

    @Override
    public String[] setTableColumns() {
        return new String[]{"应用名称", "消息", "级别", "头信息", "标签", "TID", "时间"};
    }

    /**
     * 启动日志收集
     */
    private void runLogCatReceiverTask() {
        logCatReceiverTask = new LogCatReceiverTask(iDevice);
        logCatReceiverTask.addLogCatListener(logCatListener);
        ThreadUtil.execAsync(logCatReceiverTask);
    }

    /**
     * 暂停日志收集
     */
    private void stopLogCatReceiverTask() {
        logCatReceiverTask.stop();
        logCatReceiverTask.removeLogCatListener(logCatListener);
        logCatReceiverTask = null;
    }

    /**
     * 暂停/开始按钮鼠标点击事件
     *
     * @param event 事件对象
     */
    @ClickAction("switchButton")
    public void switchButtonClick(ActionEvent event) {
        if ("开始".equals(switchButton.getText())) {
            switchButton.setText("暂停");
            runLogCatReceiverTask();
        } else {
            switchButton.setText("开始");
            stopLogCatReceiverTask();
        }
    }
}