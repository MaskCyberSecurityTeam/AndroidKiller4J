package com.richardtang.androidkiller4j.view.device;

import com.richardtang.androidkiller4j.core.ddmlib.AndroidDeviceManager;
import com.richardtang.androidkiller4j.core.device.DeviceLogReceiverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.event.ActionEvent;

@Component
public class DeviceLogViewAction {

    @Autowired
    private DeviceLogView deviceLogView;

    @Autowired
    private AndroidDeviceManager androidDeviceManager;

    @Autowired
    private DeviceLogReceiverManager deviceLogReceiverManager;

    /**
     * 暂停/开始按钮鼠标点击事件
     *
     * @param event 事件对象
     */
    public void changeButtonOnMouseClick(ActionEvent event) {
        deviceLogReceiverManager.run(androidDeviceManager.getDevice(), deviceLogView.getTableEventList());
    }
//
//    /**
//     * 条件筛选组件
//     */
//    public void conditionSearch() {
//        String       pid      = deviceLogView.getPidTextField().getText();
//        String       tag      = deviceLogView.getTagTextField().getText();
//        String       msg      = deviceLogView.getMessageTextField().getText();
//        Log.LogLevel logLevel = deviceLogView.getLogLevelChoiceBoxSelectItem();
//
//        if (StrUtil.isAllEmpty(pid, tag, msg) && ObjectUtil.isEmpty(logLevel)) {
//            deviceLogView.getFilteredData().setPredicate(s -> true);
//        } else {
//            deviceLogView.getFilteredData().setPredicate(s -> {
//                boolean result = true;
//                if (StrUtil.isNotBlank(pid)) {
//                    result &= s.getPid() == Integer.parseInt(pid);
//                }
//                if (StrUtil.isNotBlank(msg)) {
//                    result &= s.getMessage().contains(msg);
//                }
//                if (StrUtil.isNotBlank(tag)) {
//                    result &= s.getTag().equals(tag);
//                }
//                if (ObjectUtil.isNotNull(logLevel)) {
//                    result &= logLevel.getStringValue().equals(s.getLogLevel().getStringValue());
//                }
//                return result;
//            });
//        }
//    }
}