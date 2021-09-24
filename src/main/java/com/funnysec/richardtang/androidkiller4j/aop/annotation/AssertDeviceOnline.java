package com.funnysec.richardtang.androidkiller4j.aop.annotation;

import java.lang.annotation.*;

/**
 * 校验是否已连接设备
 *
 * @author RichardTang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AssertDeviceOnline {
}
