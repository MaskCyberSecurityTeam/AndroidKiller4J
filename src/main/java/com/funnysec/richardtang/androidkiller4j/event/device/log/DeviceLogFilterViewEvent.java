package com.funnysec.richardtang.androidkiller4j.event.device.log;

import com.funnysec.richardtang.androidkiller4j.config.ResourceImageConfig;
import com.funnysec.richardtang.androidkiller4j.constant.Icon;
import com.funnysec.richardtang.androidkiller4j.core.ddmlib.AndroidDeviceManager;
import com.funnysec.richardtang.androidkiller4j.core.device.DeviceLogReceiverManager;
import com.funnysec.richardtang.androidkiller4j.view.device.log.DeviceLogView;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 对应{@link com.funnysec.richardtang.androidkiller4j.view.device.log.DeviceLogFilterView}视图事件
 *
 * @author RichardTang
 */
@Component
public class DeviceLogFilterViewEvent {

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
    public void switchButtonOnMouseClick(MouseEvent event) {
        Button btn = ((Button) event.getSource());

        if ("开始".equals(btn.getText())) {
            deviceLogReceiverManager.run(androidDeviceManager.getDevice(), deviceLogView.getTableView().getItems());
            btn.setText("暂停");
            btn.setGraphic(Icon.DEVICE_LOG_FILTER_STOP);
        } else {
            deviceLogReceiverManager.stop();
            btn.setText("开始");
            btn.setGraphic(Icon.DEVICE_LOG_FILTER_START);
        }
    }
}