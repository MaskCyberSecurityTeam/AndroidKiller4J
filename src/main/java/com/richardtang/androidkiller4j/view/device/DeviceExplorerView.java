package com.richardtang.androidkiller4j.view.device;

import com.android.ddmlib.FileListingService;
import com.android.ddmlib.IDevice;
import lombok.Data;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

@Data
public class DeviceExplorerView extends JPanel {

    private   IDevice            iDevice;
    protected FileListingService fileListingService;

    private JXTreeTable      treeTable;
    private JXTreeTableModel treeTableModel;

    public DeviceExplorerView(IDevice iDevice) {
        this.iDevice       = iDevice;
        fileListingService = iDevice.getFileListingService();

        treeTableModel = new JXTreeTableModel(fileListingService.getRoot());
        treeTable      = new JXTreeTable(treeTableModel);

        setLayout(new BorderLayout());
        add(new JScrollPane(treeTable), BorderLayout.CENTER);
    }

    /**
     * 代码实现参考: https://www.cnblogs.com/liinux/p/5530702.html
     */
    private class JXTreeTableModel extends AbstractTreeTableModel {

        private FileListingService.FileEntry root;

        private String[] columns = {"文件名", "文件信息", "文件权限", "文件大小", "文件日期", "所有者", "所属组"};

        public JXTreeTableModel(FileListingService.FileEntry root) {
            super(null);
            this.root = root;
        }

        @Override
        public int getColumnCount() {
            return 7;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Class getColumnClass(int i) {
            return String.class;
        }

        @Override
        public Object getValueAt(Object node, int column) {
            FileListingService.FileEntry fileEntry = (FileListingService.FileEntry) node;
            switch (column) {
                case 0:
                    return fileEntry.getName();
                case 1:
                    return fileEntry.getInfo();
                case 2:
                    return fileEntry.getPermissions();
                case 3:
                    return fileEntry.getSize();
                case 4:
                    return fileEntry.getDate();
                case 5:
                    return fileEntry.getOwner();
                case 6:
                    return fileEntry.getGroup();
            }
            return null;
        }

        @Override
        public boolean isCellEditable(Object o, int i) {
            return false;
        }

        @Override
        public Object getChild(Object parent, int index) {
            FileListingService.FileEntry fileEntry = (FileListingService.FileEntry) parent;
            return fileListingService.getChildren(fileEntry, true, null)[index];
        }

        @Override
        public int getChildCount(Object parent) {
            FileListingService.FileEntry fileEntry = (FileListingService.FileEntry) parent;
            return fileListingService.getChildren(fileEntry, true, null).length;
        }

        @Override
        public boolean isLeaf(Object node) {
            FileListingService.FileEntry fileEntry = (FileListingService.FileEntry) node;
            return !fileEntry.isDirectory();
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            FileListingService.FileEntry fileEntry  = (FileListingService.FileEntry) parent;
            FileListingService.FileEntry childEntry = (FileListingService.FileEntry) parent;
            return Arrays.asList(fileListingService.getChildren(fileEntry, true, null)).indexOf(childEntry);
        }

        @Override
        public FileListingService.FileEntry getRoot() {
            return root;
        }
    }
}