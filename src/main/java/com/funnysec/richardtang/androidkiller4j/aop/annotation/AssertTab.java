package com.funnysec.richardtang.androidkiller4j.aop.annotation;

import java.lang.annotation.*;

/**
 * 根据TabId检查TaskView中是否已打开指定Tab
 * 如果已经打开则直接显示对应的Tab不再打开新的Tab
 *
 * @author RichardTang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AssertTab {

    // TabId
    String value() default "";
}