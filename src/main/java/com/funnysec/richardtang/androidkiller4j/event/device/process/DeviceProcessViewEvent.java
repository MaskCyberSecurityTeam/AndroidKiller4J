package com.funnysec.richardtang.androidkiller4j.event.device.process;

import com.funnysec.richardtang.androidkiller4j.config.ResourceImageConfig;
import com.funnysec.richardtang.androidkiller4j.constant.Icon;
import com.funnysec.richardtang.androidkiller4j.core.device.DeviceProcessReceiverManager;
import com.funnysec.richardtang.androidkiller4j.core.ddmlib.AndroidDeviceManager;
import com.funnysec.richardtang.androidkiller4j.view.device.process.DeviceProcessView;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@link DeviceProcessView}视图对应的事件处理类
 *
 * @author RichardTang
 */
@Component
public class DeviceProcessViewEvent {

    @Autowired
    private DeviceProcessView deviceProcessView;

    @Autowired
    private AndroidDeviceManager androidDeviceManager;

    @Autowired
    private DeviceProcessReceiverManager deviceProcessReceiverManager;

    public void changeButtonOnMouseClick(MouseEvent event) {
        Button btn = ((Button) event.getSource());

        if ("开始".equals(btn.getText())) {
            deviceProcessReceiverManager.run(androidDeviceManager.getDevice(), deviceProcessView.getTableView().getItems());
            btn.setText("暂停");
            btn.setGraphic(Icon.DEVICE_PROCESS_STOP);
        } else {
            deviceProcessReceiverManager.stop();
            btn.setText("开始");
            btn.setGraphic(Icon.DEVICE_PROCESS_START);
        }
    }
}