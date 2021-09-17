package com.funnysec.richardtang.androidkiller4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在执行函数前校验{@link com.funnysec.richardtang.androidkiller4j.view.TaskView}中是否已经打开了对应的Tab页
 * 根据给定的TabId来作为查询的依据
 *
 * @author RichardTang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SelectTab {

    // TabId
    String value() default "";
}