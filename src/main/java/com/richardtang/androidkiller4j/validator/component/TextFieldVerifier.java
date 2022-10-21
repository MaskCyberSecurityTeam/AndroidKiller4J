package com.richardtang.androidkiller4j.validator.component;

import com.formdev.flatlaf.FlatClientProperties;
import com.richardtang.androidkiller4j.util.ControlUtils;

import javax.swing.*;
import java.util.function.Function;

/**
 * 对JTextField的InputVerifier简单的封装
 *
 * @author RichardTang
 */
public abstract class TextFieldVerifier {

    /**
     * 校验函数，这里主要增加了FlatLaf对JTextField的边框显示。
     *
     * @param jTextField 需要校验的JTextField组件
     * @param errorTip   校验不通过时的提示信息
     * @param function   校验的逻辑
     */
    public static void verify(JTextField jTextField, String errorTip, Function<JTextField, Boolean> function) {
        jTextField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                Boolean flag = function.apply(jTextField);
                if (!flag) {
                    ControlUtils.getTimeBTip(jTextField, errorTip, 2000);
                    jTextField.putClientProperty(FlatClientProperties.OUTLINE, FlatClientProperties.OUTLINE_ERROR);
                } else {
                    jTextField.putClientProperty(FlatClientProperties.OUTLINE, null);
                }
                return flag;
            }
        });
    }
}