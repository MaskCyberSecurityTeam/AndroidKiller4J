package com.funnysec.richardtang.androidkiller4j.aop.loader;

import com.funnysec.richardtang.androidkiller4j.aop.annotation.AssertDeviceOnline;
import com.funnysec.richardtang.androidkiller4j.aop.interceptor.AssertDeviceOnlineInterceptor;
import org.nutz.aop.MethodInterceptor;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.aop.SimpleAopMaker;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * {@link  com.funnysec.richardtang.androidkiller4j.aop.annotation.AssertDeviceOnline}注解加载器
 *
 * @author RichardTang
 */
public class AssertDeviceOnlineLoader extends SimpleAopMaker<AssertDeviceOnline> {

    @Override
    public List<? extends MethodInterceptor> makeIt(AssertDeviceOnline assertDeviceOnline, Method method, Ioc ioc) {
        return Arrays.asList(ioc.get(AssertDeviceOnlineInterceptor.class));
    }
}