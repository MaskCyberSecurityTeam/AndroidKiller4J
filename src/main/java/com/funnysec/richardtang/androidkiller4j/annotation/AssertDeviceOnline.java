package com.funnysec.richardtang.androidkiller4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在执行函数前校验当前是否已经连接{@link com.android.ddmlib.IDevice}设备，该注解只适用于函数上。
 *
 * @author RichardTang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssertDeviceOnline {
}
