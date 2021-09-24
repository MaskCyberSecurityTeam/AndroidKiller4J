package com.funnysec.richardtang.androidkiller4j.aop.loader;

import com.funnysec.richardtang.androidkiller4j.aop.annotation.AssertWorkbenchTab;
import com.funnysec.richardtang.androidkiller4j.aop.interceptor.AssertWorkbenchTabInterceptor;
import org.nutz.aop.MethodInterceptor;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.aop.SimpleAopMaker;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * {@link  com.funnysec.richardtang.androidkiller4j.aop.annotation.AssertWorkbenchTab}注解加载器
 *
 * @author RichardTang
 */
public class AssertWorkbenchTabLoader extends SimpleAopMaker<AssertWorkbenchTab> {

    @Override
    public List<? extends MethodInterceptor> makeIt(AssertWorkbenchTab assertWorkbenchTab, Method method, Ioc ioc) {
        return Arrays.asList(ioc.get(AssertWorkbenchTabInterceptor.class));
    }
}