package com.richardtang.androidkiller4j.ui.panel;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.icons.FlatSearchIcon;
import com.richardtang.androidkiller4j.ui.control.PopupMenuButton;
import com.richardtang.androidkiller4j.ui.table.CommonTable;
import com.richardtang.androidkiller4j.util.ControlUtils;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * 带有表格、搜索框、显示/隐藏表格标题功能的通用页面。
 *
 * @author RichardTang
 */
public abstract class CommonTablePanel<T> extends JPanel {

    // 工具栏面板
    protected JPanel toolBarPanel = new JPanel(new MigLayout("insets 2,gapx 1", "[grow][]", ""));

    // 显示和隐藏标题
    private final Icon checkboxSwitchIcon = ControlUtils.getSVGIcon("checkbox-switch.svg");

    // 设置隐藏/显示标题的菜单
    private JPopupMenu popupMenu = new JPopupMenu();

    // 被点击进行隐藏/显示的按钮
    private PopupMenuButton popupMenuButton = new PopupMenuButton(checkboxSwitchIcon, popupMenu);

    // TableColumn
    private ArrayList<TableColumn>   indexOfColumns   = new ArrayList<>();
    private HashMap<String, Integer> keyOfColumnIndex = new HashMap<>();

    // 内容搜索组件
    protected JTextField filterTextField = new JTextField();

    protected CommonTable<T> table = new CommonTable<>() {
        @Override
        public Object bindColumnValue(T obj, int columnIndex) {
            return bindTableColumnValue(obj, columnIndex);
        }

        @Override
        public String[] bindColumns() {
            return setTableColumns();
        }
    };

    // 用于实现条件过滤
    protected TableRowSorter<TableModel> tableRowSorter = new TableRowSorter<>(table.getModel());

    public CommonTablePanel() {
        int index = 0;
        Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn column = columns.nextElement();
            String text = column.getHeaderValue().toString();
            JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem(text);
            indexOfColumns.add(column);
            popupMenu.add(checkBoxMenuItem);
            keyOfColumnIndex.put(text, index++);
            checkBoxMenuItem.addItemListener(new PopupMenuItemListenerImpl());
        }

        table.setRowSorter(tableRowSorter);
        popupMenuButton.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        initConditionSearchLayout();

        // 绑定事件搜索框内容发生改变时触发。
        filterTextField.getDocument().addDocumentListener(new FilterTextFieldDocumentListener());

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(toolBarPanel, BorderLayout.NORTH);
    }

    /**
     * 条件搜索组件布局初始化
     */
    private void initConditionSearchLayout() {
        toolBarPanel.add(filterTextField, "growx");
        toolBarPanel.add(popupMenuButton);

        // 搜索框前加上搜索图标
        filterTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSearchIcon());
    }

    /**
     * 创建过滤器，对表格进行过滤。
     */
    private void newFilter() {
        try {
            String text = filterTextField.getText();
            if (StrUtil.isNotEmpty(text)) {
                tableRowSorter.setRowFilter(RowFilter.regexFilter(text));
            } else {
                tableRowSorter.setRowFilter(null);
            }
        } catch (Exception e) {
            StaticLog.error(e);
        }
    }

    /**
     * 获取Panel中对应的表格
     *
     * @return 表格对象
     */
    public CommonTable<T> getTable() {
        return table;
    }

    /**
     * 获取工具栏(ToolBarPanel)
     *
     * @return 工具类面板
     */
    public JPanel getToolBarPanel() {
        return toolBarPanel;
    }

    /**
     * 使用者需要自定义T类型中哪些数据对应columnIndex的值
     *
     * @param obj         JavaBean的具体类型数据对象
     * @param columnIndex 当前表格中第columnIndex个位置应该对应JavaBean中的哪个属性
     * @return 当前columnIndex列对应的属性值
     */
    public abstract Object bindTableColumnValue(T obj, int columnIndex);

    /**
     * 设置表格标题
     *
     * @return 表格标题
     */
    public abstract String[] setTableColumns();

    /**
     * 内容搜索过滤
     */
    private class FilterTextFieldDocumentListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            newFilter();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            newFilter();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            newFilter();
        }
    }

    /**
     * 隐藏/显示按钮的菜单选项事件
     */
    private class PopupMenuItemListenerImpl implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBoxMenuItem selectedItem = (JCheckBoxMenuItem) e.getItem();
            if (selectedItem.getState()) {
                // 隐藏标题
                Integer columnIndex = keyOfColumnIndex.get(selectedItem.getText());
                table.removeColumn(table.getColumnModel().getColumn(columnIndex));
            } else {
                // 显示标题
                Integer columnIndex = keyOfColumnIndex.get(selectedItem.getText());
                table.addColumn(indexOfColumns.get(columnIndex));
                table.moveColumn(table.getColumnCount() - 1, columnIndex);
            }
        }
    }
}