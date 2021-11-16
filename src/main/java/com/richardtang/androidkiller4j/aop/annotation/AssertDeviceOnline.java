package com.richardtang.androidkiller4j.aop.annotation;

import java.lang.annotation.*;

/**
 * 作用在函数上，用于检查当前应用中设备状态是否已经处于连接状态。
 *
 * @author RichardTang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AssertDeviceOnline {

}
