package com.funnysec.richardtang.androidkiller4j.event.device;

import com.funnysec.richardtang.androidkiller4j.constant.Icon;
import com.funnysec.richardtang.androidkiller4j.core.ddmlib.AndroidDeviceManager;
import com.funnysec.richardtang.androidkiller4j.core.device.DeviceProcessReceiverManager;
import com.funnysec.richardtang.androidkiller4j.view.device.DeviceProcessView;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean
public class DeviceProcessViewEvent {

    @Inject
    private DeviceProcessView deviceProcessView;

    @Inject
    private AndroidDeviceManager androidDeviceManager;

    @Inject
    private DeviceProcessReceiverManager deviceProcessReceiverManager;

    public void changeButtonOnMouseClick(MouseEvent event) {
        Button btn = ((Button) event.getSource());

        if ("开始".equals(btn.getText())) {
            deviceProcessReceiverManager.run(androidDeviceManager.getDevice(), deviceProcessView.getTableView().getItems());
            btn.setText("暂停");
            btn.setGraphic(Icon.DEVICE_PROCESS_VIEW_STOP);
        } else {
            deviceProcessReceiverManager.stop();
            btn.setText("开始");
            btn.setGraphic(Icon.DEVICE_PROCESS_VIEW_START);
        }
    }
}