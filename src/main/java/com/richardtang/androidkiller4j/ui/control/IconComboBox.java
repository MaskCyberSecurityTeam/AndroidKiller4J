package com.richardtang.androidkiller4j.ui.control;

import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * 在ComboBox基础上增加了图标，实现了ComboBox上的Item项中可以显示图标的功能。
 *
 * @author RichardTang
 */
public class IconComboBox extends JComboBox<IconComboBox.IconComboBoxData> {

    // ComboBox中的选项
    private Vector<IconComboBoxData> vector = new Vector<>();

    // 默认选项
    private IconComboBoxData defaultOption;

    // 默认选项id
    public static final String DEFAULT_OPTION_ID = "DEFAULT";

    public IconComboBox() {
        setModel(new DefaultComboBoxModel<>(vector));
        setRenderer(new IconComboBoxCellRender());
    }

    /**
     * 带有默认选项的ComboBox,并默认选中。
     *
     * @param defaultOption 默认选项
     */
    public IconComboBox(IconComboBoxData defaultOption) {
        this();
        this.defaultOption = defaultOption;
        addItem(defaultOption);
        setSelectedIndex(0);
    }

    /**
     * 带有默认选项的ComboBox,并默认选中。
     *
     * @param defaultOptionText 默认选项的文本
     */
    public IconComboBox(String defaultOptionText) {
        this(new IconComboBox.IconComboBoxData(DEFAULT_OPTION_ID, defaultOptionText));
    }

    /**
     * 根据id删除选项
     *
     * @param id 选项的id
     */
    public void removeItemById(String id) {
        int index = -1;
        for (int i = 0; i < vector.size(); i++) {
            if (id.equals(vector.get(i).getId())) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            removeItemAt(index);
        }
    }

    /**
     * 删除全部，当存在默认选项时保留默认选项。
     */
    @Override
    public void removeAllItems() {
        super.removeAllItems();
        if (defaultOption != null) {
            addItem(defaultOption);
        }
    }

    /**
     * 单元格渲染
     */
    public static class IconComboBoxCellRender extends JLabel implements ListCellRenderer<IconComboBoxData> {

        public IconComboBoxCellRender() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends IconComboBoxData> list,
                IconComboBoxData value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            if (value != null) {
                setText(value.getText());
                setIcon(value.getIcon());
                setFont(list.getFont());
                setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
                setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            }
            return this;
        }
    }

    /**
     * 选项实体
     */
    @Data
    public static class IconComboBoxData {

        private String id;
        private Icon   icon;
        private String text;
        private Object data;

        public IconComboBoxData(String id, String text) {
            this(id, text, null, null);
        }

        public IconComboBoxData(String id, String text, Icon icon) {
            this(id, text, icon, null);
        }

        public IconComboBoxData(String id, String text, Icon icon, Object data) {
            this.id = id;
            this.text = text;
            this.icon = icon;
            this.data = data;
        }
    }
}