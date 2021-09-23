package com.funnysec.richardtang.androidkiller4j.aop.interceptor;

import com.android.ddmlib.IDevice;
import com.funnysec.richardtang.androidkiller4j.core.ddmlib.AndroidDeviceManager;
import com.funnysec.richardtang.androidkiller4j.util.FxUtil;
import org.nutz.aop.InterceptorChain;
import org.nutz.aop.MethodInterceptor;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean
public class AssertDeviceOnlineInterceptor implements MethodInterceptor {

    @Inject
    private AndroidDeviceManager androidDeviceManager;

    @Override
    public void filter(InterceptorChain chain) throws Throwable {
        IDevice device = androidDeviceManager.getDevice();
        if (device != null && device.isOnline()) {
            chain.doChain();
        } else {
            FxUtil.alert("提示", "请先连接设备！");
        }
    }
}