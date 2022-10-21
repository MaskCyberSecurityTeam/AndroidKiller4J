package com.richardtang.androidkiller4j.ui.control;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * 可自定义ComboBox选项显示的内容的组件
 *
 * @param <E> ComboBox组件的选项中的类型
 * @author RichardTang
 */
public abstract class CustomTextComboBox<E> extends JComboBox<E> {

    {
        // 创建时就启用自定义的渲染
        setRenderer(new CustomTextComboBoxCellRender());
    }

    /**
     * 用户自定义实现的内容获取逻辑
     *
     * @param e ComboBox组件的选项中的类型
     * @return 显示在ComboBox组件的选项文字
     */
    protected abstract String getText(E e);

    @Nullable
    @Override
    public E getSelectedItem() {
        return (E) super.getSelectedItem();
    }

    /**
     * 自定义选项的渲染
     */
    public class CustomTextComboBoxCellRender extends JLabel implements ListCellRenderer<E> {

        public CustomTextComboBoxCellRender() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
            try {
                setText(CustomTextComboBox.this.getText(value));
            } catch (Exception ignored) {
                // 抛异常情况下不进行处理 直接跳过
            }
            return this;
        }
    }
}