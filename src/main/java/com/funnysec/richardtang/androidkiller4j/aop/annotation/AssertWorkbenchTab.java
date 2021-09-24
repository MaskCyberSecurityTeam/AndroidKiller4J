package com.funnysec.richardtang.androidkiller4j.aop.annotation;

import java.lang.annotation.*;

/**
 * 校验当前Tab是否为WorkbenchTab
 *
 * @author RichardTang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AssertWorkbenchTab {

}