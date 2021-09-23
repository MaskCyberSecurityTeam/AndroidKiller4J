package com.funnysec.richardtang.androidkiller4j.aop.loader;

import com.funnysec.richardtang.androidkiller4j.aop.annotation.AssertTab;
import com.funnysec.richardtang.androidkiller4j.aop.interceptor.AssertTabInterceptor;
import org.nutz.aop.MethodInterceptor;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.aop.SimpleAopMaker;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class AssertTabLoader extends SimpleAopMaker<AssertTab> {

    @Override
    public List<? extends MethodInterceptor> makeIt(AssertTab assertTab, Method method, Ioc ioc) {
        return Arrays.asList(ioc.get(AssertTabInterceptor.class));
    }
}