package com.richardtang.androidkiller4j.view.device;

import com.richardtang.androidkiller4j.core.ddmlib.AndroidDeviceManager;
import com.richardtang.androidkiller4j.core.device.DeviceProcessReceiverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.event.ActionEvent;

@Component
public class DeviceProcessViewAction {

    @Autowired
    private DeviceProcessView deviceProcessView;

    @Autowired
    private AndroidDeviceManager androidDeviceManager;

    @Autowired
    private DeviceProcessReceiverManager deviceProcessReceiverManager;

    public void changeButtonOnMouseClick(ActionEvent event) {
        deviceProcessReceiverManager.run(androidDeviceManager.getDevice(), deviceProcessView.getTableEventList());
    }
}