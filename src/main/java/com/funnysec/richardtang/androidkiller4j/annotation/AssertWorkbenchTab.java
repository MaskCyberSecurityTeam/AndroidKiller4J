package com.funnysec.richardtang.androidkiller4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在执行函数前校验当前选中的Tab页是否为ApkTab，也就是WorkbenchView。
 *
 * @author RichardTang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssertWorkbenchTab {

}