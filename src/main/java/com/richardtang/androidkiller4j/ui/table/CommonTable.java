package com.richardtang.androidkiller4j.ui.table;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用表格，默认带表格标题、表格内容居中、显示序号列。
 *
 * @param <T> 表格显示的数据类型
 * @author RichardTang
 */
public abstract class CommonTable<T> extends JTable {

    // 存储表格数据的集合
    protected List<T> data = new ArrayList<>();

    // 表格标题
    protected List<String> columns = new ArrayList<>();

    // 表格模型
    protected AbstractTableModel model = new AbstractTableModel() {

        /**
         * 获取每一列的名称
         *
         * @param column 第几列
         * @return 对应的列名
         */
        @Override
        public String getColumnName(int column) {
            return columns.get(column);
        }

        /**
         * 得到表头列数，需要显示在表格上的列数量。
         *
         * @return 需要展示的列数量
         */
        @Override
        public int getColumnCount() {
            return columns.size();
        }

        /**
         * 数据行数
         *
         * @return 数据展示的行数
         */
        @Override
        public int getRowCount() {
            return data.size();
        }

        /**
         * 根据行和列的值获取指定的数据
         *
         * @param rowIndex    需要获取的数据所在行
         * @param columnIndex 需要获取的数据所在列
         * @return 对应的数据
         */
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return rowIndex + 1;
            }
            return bindColumnValue(data.get(rowIndex), columnIndex);
        }
    };

    // 表格渲染
    private DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();

    public CommonTable() {
        // 初始化表格标题。
        columns.add("序号");
        columns.addAll(List.of(bindColumns()));

        // 表格样式
        setModel(model);
        setShowGrid(true);
        setRowHeight(30);
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // 表格渲染
        tableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        setDefaultRenderer(Object.class, tableCellRenderer);
    }

    /**
     * 根据索引获取数据
     *
     * @param index 索引
     * @return T类型数据
     */
    public T getDataItem(int index) {
        return data.get(index);
    }

    /**
     * 获取存储T类型的集合
     *
     * @return 数据集合
     */
    public List<T> getData() {
        return data;
    }

    /**
     * 添加一行数据
     *
     * @param t 数据对应的JavaBean
     */
    public void addRow(T t) {
        data.add(t);
        model.fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }

    /**
     * 添加多行数据
     *
     * @param items 数据集合
     */
    public void addRows(List<T> items) {
        int addBeforeIndex = data.size();
        data.addAll(items);
        model.fireTableRowsInserted(addBeforeIndex - 1, data.size() - 1);
    }

    /**
     * 根据行号删除数据
     *
     * @param rowIndex 行号
     */
    public void removeRow(int rowIndex) {
        data.remove(rowIndex);
        model.fireTableRowsDeleted(rowIndex, rowIndex);
    }

    /**
     * 清空所有数据
     */
    public void removeAll() {
        int dataSize = data.size();
        if (dataSize > 0) {
            data.clear();
            model.fireTableRowsDeleted(0, dataSize - 1);
        }
    }

    /**
     * 使用者需要自定义T类型中哪些数据对应columnIndex的值
     *
     * @param obj         JavaBean的具体类型数据对象
     * @param columnIndex 当前表格中第columnIndex个位置应该对应JavaBean中的哪个属性
     * @return 当前columnIndex列对应的属性值
     */
    public abstract Object bindColumnValue(T obj, int columnIndex);

    /**
     * 设置表格标题
     *
     * @return 表格标题
     */
    public abstract String[] bindColumns();
}