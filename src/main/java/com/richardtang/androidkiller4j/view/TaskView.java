package com.richardtang.androidkiller4j.view;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.richardtang.androidkiller4j.bean.Apk;
import com.richardtang.androidkiller4j.constant.SvgName;
import com.richardtang.androidkiller4j.constant.R;
import com.richardtang.androidkiller4j.constant.Size;
import com.richardtang.androidkiller4j.ui.panel.CommonTablePanel;
import com.richardtang.androidkiller4j.ui.table.CommonTable;
import com.richardtang.androidkiller4j.ui.tabpane.TabPane;
import com.richardtang.androidkiller4j.util.ControlUtils;
import lombok.Data;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * 任务列表视图
 *
 * @author RichardTang
 */
@Data
public final class TaskView extends TabPane {

    private final CommonTablePanel<Apk> commonTablePanel = new CommonTablePanel<>() {
        @Override
        public Object bindTableColumnValue(Apk apk, int columnIndex) {
            return switch (columnIndex) {
                case 1 -> apk.getImageIcon();
                case 2 -> apk.getApplicationName();
                case 3 -> apk.getPackageName();
                case 4 -> apk.getTargetSdkVersion();
                case 5 -> apk.getMinSdkVersion();
                default -> DateUtil.formatChineseDate(apk.getDecodeApkDate(), false, true);
            };
        }

        @Override
        public String[] setTableColumns() {
            return new String[]{"应用图标", "应用名称", "应用包名", "目标SDK", "最小SDK", "创建时间"};
        }
    };

    // 任务列表表格
    private final CommonTable<Apk> table = commonTablePanel.getTable();

    private final Icon taskIcon = ControlUtils.getSVGIcon(SvgName.TASK, Size.BIG);

    public TaskView() {
        // 表格ApkIcon自定义渲染
        table.getColumnModel().getColumn(1).setCellRenderer(new AppIconCellRender());
        // Panel边框颜色
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.decode("#c4c4c4")));
        // 添加一个默认的Tab页
        addTab("任务列表", taskIcon, commonTablePanel, false);
        // 表格点击事件
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // 非双击时不向下执行
                if (e.getClickCount() != 2) return;

                // 获取选中，并校验是否已经打开。
                Apk apk = getSelectedApk();
                boolean isShow = showTabByTitle(apk.getFileName());
                if (!isShow) {
                    addTab(apk.getFileName(), apk.getImageIcon(), new WorkbenchView(apk));
                }
            }
        });
        // 刷新一次任务，将apk添加至列表中
        refreshTask();
    }

    /**
     * 刷新任务列表
     */
    public void refreshTask() {
        for (File file : FileUtil.ls(R.PROJECT_DIR)) {
            if (file.isDirectory()) {
                table.addRow(new Apk(R.PROJECT_DIR + file.getName()));
            }
        }
    }

    /**
     * 添加任务
     *
     * @param apk 需要添加到任务列表中的apk对象
     */
    public void addTask(Apk apk) {
        table.addRow(apk);
    }

    /**
     * 删除任务
     *
     * @param index 索引号
     */
    public void removeTask(int index) {
        table.removeRow(index - 1);
    }

    /**
     * 根据apk根路径查找任务列表中是否存在该apk对应的任务
     *
     * @param apkBasePath apk根路径包
     * @return 查找到的apk对象在task表中的索引号，未找到则返回-1。
     */
    public Integer findTaskByBasePath(String apkBasePath) {
        for (int i = 0; i < table.getData().size(); i++) {
            if (table.getData().get(i).getBasePath().equals(apkBasePath)) {
                return i;
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
        return table.getDataItem(table.getSelectedRow());
    }

    /**
     * 应用图标单元格自定义渲染效果
     */
    public static class AppIconCellRender extends DefaultTableCellRenderer {

        public AppIconCellRender() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setIcon((Icon) value);
            // 这里需要调用原有的函数保留样式效果,但是不要设置value的文本值。
            return super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
        }
    }
}