package com.funnysec.richardtang.androidkiller4j.aop.annotation;

import java.lang.annotation.*;

/**
 * 根据TabId校验是否已打开指定Tab
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