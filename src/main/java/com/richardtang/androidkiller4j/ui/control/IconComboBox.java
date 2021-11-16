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

    private Vector<IconComboBoxData> vector = new Vector<>();

    public IconComboBox() {
        setModel(new DefaultComboBoxModel<>(vector));
        setRenderer(new IconComboBoxCellRender());
    }

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
            this.id   = id;
            this.text = text;
            this.icon = icon;
            this.data = data;
        }
    }
}