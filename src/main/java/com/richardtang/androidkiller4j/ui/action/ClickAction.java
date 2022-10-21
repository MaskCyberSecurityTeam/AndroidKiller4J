package com.richardtang.androidkiller4j.ui.action;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClickAction {
    // 成员属性名称
    String value();

    // 指定组件类型，默认为按钮类型。
    ClickActionType type() default ClickActionType.ButtonComponent;

}