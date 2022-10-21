package com.richardtang.androidkiller4j.ui.device;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import com.android.ddmlib.FileListingService;
import com.formdev.flatlaf.icons.FlatTreeClosedIcon;
import com.formdev.flatlaf.icons.FlatTreeLeafIcon;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备文件表格
 * TODO 此处可以用BeanTableModel进行简化，之后有时间再弄！
 *
 * @author RichardTang
 */
public class DeviceFileTable extends JTable {

    private DeviceFileTableModel model;
    private FileListingService   fileListingService;

    // 存储表格数据的集合
    private List<FileListingService.FileEntry> data = new ArrayList<>();

    public DeviceFileTable(FileListingService fileListingService) {
        this.model = new DeviceFileTableModel();
        this.fileListingService = fileListingService;
        setModel(this.model);

        setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
        getColumnModel().getColumn(0).setCellRenderer(new DeviceFileIconCellRender());
    }

    /**
     * 向表格中添加数据
     *
     * @param fileEntry 需要添加到表格中的数据对象
     */
    public void addRow(FileListingService.FileEntry fileEntry) {
        data.add(fileEntry);
        ((DeviceFileTableModel) getModel()).fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }

    /**
     * 删除表格数据
     *
     * @param fileEntry 需要删除的数据
     */
    public void remove(FileListingService.FileEntry fileEntry) {
        int index = data.lastIndexOf(fileEntry);
        data.remove(fileEntry);
        ((DeviceFileTableModel) getModel()).fireTableRowsDeleted(index, index);
    }

    /**
     * 删除表格中的所有数据
     */
    public void removeAll() {
        int dataSize = data.size();
        if (dataSize > 0) {
            data.clear();
            ((DeviceFileTableModel) getModel()).fireTableRowsDeleted(0, dataSize - 1);
        }
    }

    /**
     * 添加或者更新，当文件实体存在时则进行更新否则添加。
     *
     * @param fileEntry 需要添加或者更新的实体节点
     */
    public void addOrUpdate(FileListingService.FileEntry fileEntry) {
        for (FileListingService.FileEntry item : data) {
            if (item.getFullEscapedPath().equals(fileEntry.getFullEscapedPath())) {
                remove(item);
            }
        }
        addRow(fileEntry);
    }

    /**
     * 获取表格中正在选中的行数据
     *
     * @return 选中的行数据
     */
    public FileListingService.FileEntry getSelected() {
        try {
            return data.get(getSelectedRow());
        } catch (Exception e) {
            StaticLog.error(e);
        }
        return null;
    }

    /**
     * 根据子节点名称找到子节点
     *
     * @param pFileEntry 父节点对象
     * @param cName      子节点名称
     * @return 返回找到的子节点，未找到则返回null。
     */
    public FileListingService.FileEntry findChildByName(FileListingService.FileEntry pFileEntry, String cName) {
        try {
            for (FileListingService.FileEntry children : fileListingService.getChildrenSync(pFileEntry)) {
                if (cName.equals(children.getName())) {
                    return children;
                }
            }
        } catch (Exception e) {
            StaticLog.error(e);
        }
        return null;
    }

    /**
     * 获取当前表格中显示的数据
     */
    public List<FileListingService.FileEntry> getCurrentData() {
        return data;
    }

    /**
     * 根据字符串路径，找到FileEntry对象。
     *
     * @param path 字符串路径
     * @return 树节点对象，未找到时返回null。
     */
    public FileListingService.FileEntry findFileEntryByPath(String path) {
        List<String> pathSplit = StrUtil.splitTrim(path, "/");
        FileListingService.FileEntry parentFileEntry = fileListingService.getRoot();
        try {
            for (String pathSegment : pathSplit) {
                FileListingService.FileEntry[] childrens =
                        fileListingService.getChildrenSync(parentFileEntry);
                for (int x = 0; x < childrens.length; x++) {
                    if (pathSegment.equals(childrens[x].getName())) {
                        parentFileEntry = childrens[x];
                        break;
                    } else if (x == childrens.length - 1) {
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            StaticLog.error(e);
        }
        return parentFileEntry;
    }

    /**
     * 设备表格模型
     */
    private class DeviceFileTableModel extends AbstractTableModel {

        private final List<String> columns = List.of("图标", "名称", "所有者", "所属组", "权限", "大小", "文件类型");

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columns.size();
        }

        @Override
        public String getColumnName(int column) {
            return columns.get(column);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            FileListingService.FileEntry fileEntry = data.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return fileEntry.isDirectory();
                case 1:
                    return fileEntry.getName();
                case 2:
                    return fileEntry.getOwner();
                case 3:
                    return fileEntry.getGroup();
                case 4:
                    return fileEntry.getPermissions();
                case 5:
                    return fileEntry.getSize();
                case 6:
                    return fileEntry.getType();
            }
            return "";
        }
    }

    private class DeviceFileIconCellRender extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            boolean isDirectory = (boolean) value;

            if (isDirectory) {
                setIcon(new FlatTreeClosedIcon());
            } else {
                setIcon(new FlatTreeLeafIcon());
            }

            // 这里需要调用原有的函数保留样式效果,但是不要设置value的文本值。
            return super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
        }
    }
}