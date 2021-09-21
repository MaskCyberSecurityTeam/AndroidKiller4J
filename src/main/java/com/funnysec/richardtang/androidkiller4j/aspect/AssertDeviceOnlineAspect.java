package com.funnysec.richardtang.androidkiller4j.aspect;

import com.android.ddmlib.IDevice;
import com.funnysec.richardtang.androidkiller4j.annotation.AssertDeviceOnline;
import com.funnysec.richardtang.androidkiller4j.core.ddmlib.AndroidDeviceManager;
import com.funnysec.richardtang.androidkiller4j.util.FxUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@link AssertDeviceOnline} 注解的处理逻辑
 * 主要用来实现校验，当前应用中Device是否已经处于连接状态，如果未连接着弹窗进行阻拦。
 *
 * @author RichardTang
 */
@Aspect
@Component
public class AssertDeviceOnlineAspect {

    @Autowired
    private AndroidDeviceManager androidDeviceManager;

    /**
     * 拦截函数上带有DeviceOnlineAspect注解的函数
     *
     * @param proceedingJoinPoint 用来控制函数是否往下执行
     * @return {@link ProceedingJoinPoint}
     * @throws Throwable 调用proceed时抛出的异常
     */
    @Around("@annotation(com.funnysec.richardtang.androidkiller4j.annotation.AssertDeviceOnline)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        IDevice device = androidDeviceManager.getDevice();
        if (device == null || device.isOffline()) {
            FxUtil.alert("提示", "请先连接设备！");
            return proceedingJoinPoint;
        }
        return proceedingJoinPoint.proceed();
    }
}