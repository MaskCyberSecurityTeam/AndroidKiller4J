package com.funnysec.richardtang.androidkiller4j.event.device;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.android.ddmlib.Log;
import com.funnysec.richardtang.androidkiller4j.constant.Icon;
import com.funnysec.richardtang.androidkiller4j.core.ddmlib.AndroidDeviceManager;
import com.funnysec.richardtang.androidkiller4j.core.device.DeviceLogReceiverManager;
import com.funnysec.richardtang.androidkiller4j.view.device.DeviceLogView;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean
public class DeviceLogViewEvent {

    @Inject
    private DeviceLogView deviceLogView;

    @Inject
    private AndroidDeviceManager androidDeviceManager;

    @Inject
    private DeviceLogReceiverManager deviceLogReceiverManager;

    /**
     * 暂停/开始按钮鼠标点击事件
     *
     * @param event 事件对象
     */
    public void switchButtonOnMouseClick(MouseEvent event) {
        Button btn = ((Button) event.getSource());

        if ("开始".equals(btn.getText())) {
            deviceLogReceiverManager.run(androidDeviceManager.getDevice(), deviceLogView.getTableViewItem());
            btn.setText("暂停");
            btn.setGraphic(Icon.DEVICE_LOG_VIEW_FILTER_STOP);
        } else {
            deviceLogReceiverManager.stop();
            btn.setText("开始");
            btn.setGraphic(Icon.DEVICE_LOG_VIEW_FILTER_START);
        }
    }

    /**
     * 条件筛选组件
     */
    public void conditionSearch() {
        String       pid      = deviceLogView.getPidTextField().getText();
        String       tag      = deviceLogView.getTagTextField().getText();
        String       msg      = deviceLogView.getMessageTextField().getText();
        Log.LogLevel logLevel = deviceLogView.getLogLevelChoiceBoxSelectItem();

        if (StrUtil.isAllEmpty(pid, tag, msg) && ObjectUtil.isEmpty(logLevel)) {
            deviceLogView.getFilteredData().setPredicate(s -> true);
        } else {
            deviceLogView.getFilteredData().setPredicate(s -> {
                boolean result = true;
                if (StrUtil.isNotBlank(pid)) {
                    result &= s.getPid() == Integer.parseInt(pid);
                }
                if (StrUtil.isNotBlank(msg)) {
                    result &= s.getMessage().contains(msg);
                }
                if (StrUtil.isNotBlank(tag)) {
                    result &= s.getTag().equals(tag);
                }
                if (ObjectUtil.isNotNull(logLevel)) {
                    result &= logLevel.getStringValue().equals(s.getLogLevel().getStringValue());
                }
                return result;
            });
        }
    }
}