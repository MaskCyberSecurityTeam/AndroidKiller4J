package com.richardtang.androidkiller4j.ui.action;

import cn.hutool.log.StaticLog;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.event.*;
import java.lang.reflect.*;

/**
 * 通过反射实现注解绑定组件事件
 *
 * @author RichardTang
 */
public class ClickActionInstaller {

    private static final String ADD_MOUSE_LISTENER  = "addMouseListener";
    private static final String ADD_ACTION_LISTENER = "addActionListener";

    /**
     * 绑定事件
     *
     * @param obj 需要使用注解进行绑定事件的所在类实例
     */
    public static void bind(Object obj) {
        try {
            Class<?> cls = obj.getClass();
            for (Method m : cls.getDeclaredMethods()) {
                // 判断函数上是否有存在这个注解标记
                ClickAction clickAction = m.getAnnotation(ClickAction.class);

                // 函数上没有设置@ClickAction
                if (clickAction == null) {
                    continue;
                }

                // 根据注解上的value找到成员属性
                Field field = cls.getDeclaredField(clickAction.value());
                boolean accessible = field.isAccessible();

                // 是否为私有属性
                if (!accessible) {
                    field.setAccessible(true);
                }

                // 添加事件
                addListener(field.get(obj), clickAction.type(), obj, m);

                // 还原私有
                field.setAccessible(accessible);
            }
        } catch (Exception e) {
            StaticLog.error(e);
        }
    }

    /**
     * 代理形式注入方法
     *
     * @param source            标记了注解函数对应的源
     * @param componentType     需要绑定事件的类型
     * @param sourceMethodParam 标记了注解的函数所需的参数
     * @param sourceMethod      标记了注解的函数
     */
    private static void addListener(Object source, ClickActionType componentType, final Object sourceMethodParam, final Method sourceMethod) throws Exception {
        // 目标函数可能是Private
        sourceMethod.setAccessible(true);
        if (componentType == ClickActionType.ButtonComponent) {
            // 因为ClickActionType默认为ButtonComponent类型，如果成员属性不是一个JButton的话则抛出异常进行提示。
            if (source.getClass() != JButton.class) {
                throw new RuntimeException("@ClickAction指定的type类型和value的成员属性类型不一致！");
            } else {
                Method addActionListener = source.getClass().getMethod(ADD_ACTION_LISTENER, ActionListener.class);
                addActionListener.invoke(source, new ActionListener() {
                    @SneakyThrows
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sourceMethod.invoke(sourceMethodParam, e);
                    }
                });
            }
        } else {
            Method addMouseListener = source.getClass().getMethod(ADD_MOUSE_LISTENER, MouseListener.class);
            addMouseListener.invoke(source, new MouseAdapter() {
                @SneakyThrows
                @Override
                public void mouseClicked(MouseEvent e) {
                    sourceMethod.invoke(sourceMethodParam, e);
                }
            });
        }
    }
}