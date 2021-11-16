package com.richardtang.androidkiller4j.view.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.formdev.flatlaf.FlatClientProperties;
import com.richardtang.androidkiller4j.bean.Apk;
import com.richardtang.androidkiller4j.constant.ResPath;
import com.richardtang.androidkiller4j.ui.tabpane.Tab;
import com.richardtang.androidkiller4j.ui.tabpane.TabPane;
import com.richardtang.androidkiller4j.util.ControlUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.util.function.BiConsumer;

/**
 * 任务列表视图
 *
 * @author RichardTang
 */
@org.springframework.stereotype.Component
public class TaskView extends TabPane implements InitializingBean {

    // 任务列表，用于存储加载的APK对象。
    private final Map<Integer, Apk> apkContainer = new HashMap<>();

    // 表格相关
    private JTable                   taskTable;
    private JScrollPane              taskTableScrollPane;
    private DefaultTableModel        taskTableModel;
    private DefaultTableCellRenderer taskTableCellRenderer;

    @Autowired
    private TaskViewAction taskViewAction;

    @Override
    public void afterPropertiesSet() {
        initializeTable();
        initializeEvent();
        reloadTask();

        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.decode("#c4c4c4")));
        addTab("任务列表", ControlUtil.getSVGIcon("task.svg", 20, 20), taskTableScrollPane, false);
    }

    /**
     * render任务列表的表格
     */
    private void initializeTable() {
        taskTableCellRenderer = new DefaultTableCellRenderer();
        taskTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);

        taskTableModel = new DefaultTableModel() {
            // 重写配置单元格不可编辑
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        taskTableModel.setColumnIdentifiers(new Object[]{"序号", "应用图标", "应用名称", "应用包名", "目标SDK", "最小SDK", "创建时间"});

        taskTable = new JTable();
        taskTable.setRowHeight(30);
        taskTable.setShowGrid(true);
        taskTable.setModel(taskTableModel);
        taskTable.setDefaultRenderer(Object.class, taskTableCellRenderer);
        taskTable.getColumnModel().getColumn(1).setCellRenderer(new AppIconCellRender());

        taskTableScrollPane = new JScrollPane(taskTable);
    }

    /**
     * 初始化事件
     */
    private void initializeEvent() {
        // 表格中的条目被双击时触发
        taskTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    taskViewAction.tableMouseDoubleClickEvent(e);
                }
            }
        });
    }

    /**
     * 重加载 /WORKDIR/project/ 目录下的项目到JTable中
     */
    public void reloadTask() {
        for (File file : FileUtil.ls(ResPath.PROJECT_DIR)) {
            if (file.isDirectory()) {
                addRow(new Apk(ResPath.PROJECT_DIR + file.getName()));
            }
        }
    }

    /**
     * 向JTable中添加一行数据
     *
     * @param apk 需要添加到列表中的apk对象
     */
    public void addRow(Apk apk) {
        Vector<Object> vector = new Vector<>();
        vector.add(taskTable.getRowCount() + 1);
        vector.add(apk.getImageIcon());
        vector.add(apk.getApplicationName());
        vector.add(apk.getPackageName());
        vector.add(apk.getTargetSdkVersion());
        vector.add(apk.getMinSdkVersion());
        vector.add(DateUtil.formatDateTime(apk.getDecodeApkDate()));
        apkContainer.put((Integer) vector.get(0), apk);
        taskTableModel.addRow(vector);
    }

    /**
     * 根据索引号进行删除，同时删除apkContainer容器中的数据。
     *
     * @param index 索引号
     */
    public void removeTaskByIndex(int index) {
        apkContainer.remove(index);
        taskTableModel.removeRow(index - 1);
    }

    /**
     * 根据apk任务根路径查找任务列表中对应的apk对象
     *
     * @param apkBasePath apk任务根路径包
     * @return 查找到的apk对象在task表中的索引号，未找到则返回-1。
     */
    public Integer findTaskApkByApkBasePath(String apkBasePath) {
        for (Integer key : apkContainer.keySet()) {
            if (apkContainer.get(key).getBasePath().equals(apkBasePath)) {
                return key;
            }
        }
        return -1;
    }

    /**
     * 获取选中的APK对象
     *
     * @return 用户选中的APK对象
     */
    public Apk getSelectedApk() {
        Integer id = (Integer) taskTableModel.getValueAt(taskTable.getSelectedRow(), 0);
        return apkContainer.get(id);
    }

    /**
     * 应用图标单元格自定义渲染效果
     */
    public static class AppIconCellRender extends DefaultTableCellRenderer {

        public AppIconCellRender() {
            setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setIcon((Icon) value);
            // 这里需要调用原有的函数保留样式效果,但是不要设置value的文本值。
            return super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
        }
    }
}