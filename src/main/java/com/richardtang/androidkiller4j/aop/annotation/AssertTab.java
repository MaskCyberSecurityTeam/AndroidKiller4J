package com.richardtang.androidkiller4j.aop.annotation;

import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AssertTab {

    // Tab页的校验根据该值来判断
    String value() default "";
}