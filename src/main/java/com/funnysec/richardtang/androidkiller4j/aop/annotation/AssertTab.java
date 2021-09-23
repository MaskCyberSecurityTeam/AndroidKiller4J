package com.funnysec.richardtang.androidkiller4j.aop.annotation;

import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AssertTab {

    // TabId
    String value() default "";
}