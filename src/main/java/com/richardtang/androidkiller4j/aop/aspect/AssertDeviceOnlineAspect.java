package com.richardtang.androidkiller4j.aop.aspect;

import com.android.ddmlib.IDevice;
import com.richardtang.androidkiller4j.core.ddmlib.AndroidDeviceManager;
import com.richardtang.androidkiller4j.util.ControlUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AssertDeviceOnlineAspect {

    @Autowired
    private AndroidDeviceManager androidDeviceManager;

    @Around(value = "@annotation(com.richardtang.androidkiller4j.aop.annotation.AssertDeviceOnline)")
    public void before(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        IDevice device = androidDeviceManager.getDevice();
        // 判断是否连接了相应ADB设备
        if (device == null || device.isOffline()) {
            ControlUtil.showMsgDialog("提示", "请先连接设备！");
        } else {
            proceedingJoinPoint.proceed();
        }
    }
}