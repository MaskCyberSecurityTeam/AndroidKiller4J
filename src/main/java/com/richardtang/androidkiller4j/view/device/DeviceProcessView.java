package com.richardtang.androidkiller4j.view.device;

import cn.hutool.core.thread.ThreadUtil;
import com.android.ddmlib.IDevice;
import com.formdev.flatlaf.FlatClientProperties;
import com.richardtang.androidkiller4j.ddmlib.process.ProcessMessage;
import com.richardtang.androidkiller4j.ddmlib.process.ProcessMessageListener;
import com.richardtang.androidkiller4j.ddmlib.process.ProcessMessageReceiverTask;
import com.richardtang.androidkiller4j.ui.action.ClickAction;
import com.richardtang.androidkiller4j.ui.action.ClickActionInstaller;
import com.richardtang.androidkiller4j.ui.panel.CommonTablePanel;
import lombok.Data;

import javax.swing.*;
import java.awt.event.ActionEvent;

@Data
public class DeviceProcessView extends CommonTablePanel<ProcessMessage> {

    private IDevice iDevice;

    // 开始、暂停
    private JButton switchButton = new JButton("开始");

    // 日志收集任务类，控制任务的开始和暂停。
    private ProcessMessageReceiverTask processMessageReceiverTask;

    // 定义日志收集到的数据需要存放的位置
    private final ProcessMessageListener processMessageListener = msgList -> {
        table.removeAll();
        table.addRows(msgList);
    };

    public DeviceProcessView(IDevice iDevice) {
        super();
        this.iDevice = iDevice;

        // 设置按钮为正方形
        switchButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, "square");
        toolBarPanel.add(switchButton);

        // 绑定事件
        ClickActionInstaller.bind(this);
    }

    @Override
    public Object bindTableColumnValue(ProcessMessage processMessage, int columnIndex) {
        return switch (columnIndex) {
            case 1 -> processMessage.getPid();
            case 2 -> processMessage.getUser();
            case 3 -> processMessage.getPr();
            case 4 -> processMessage.getNi();
            case 5 -> processMessage.getVirt();
            case 6 -> processMessage.getShr();
            case 7 -> processMessage.getS();
            case 8 -> processMessage.getCpu();
            case 9 -> processMessage.getMem();
            case 10 -> processMessage.getTime();
            default -> processMessage.getArgs();
        };
    }

    @Override
    public String[] setTableColumns() {
        return new String[]{"PID", "用户", "PR", "NI", "VIRT", "SHR", "S", "CPU使用率", "内存占用", "时间", "参数值"};
    }

    /**
     * 开启进程监听
     */
    public void runProcessMessageReceiverTask() {
        processMessageReceiverTask = new ProcessMessageReceiverTask(iDevice);
        processMessageReceiverTask.addLogCatListener(processMessageListener);
        ThreadUtil.execAsync(processMessageReceiverTask);
    }

    /**
     * 停止进程监听
     */
    public void stopProcessMessageReceiverTask() {
        processMessageReceiverTask.stop();
        processMessageReceiverTask.removeLogCatListener(processMessageListener);
        processMessageReceiverTask = null;
    }

    @ClickAction("switchButton")
    public void switchButtonClick(ActionEvent event) {
        if ("开始".equals(switchButton.getText())) {
            switchButton.setText("暂停");
            runProcessMessageReceiverTask();
        } else {
            switchButton.setText("开始");
            stopProcessMessageReceiverTask();
        }
    }
}