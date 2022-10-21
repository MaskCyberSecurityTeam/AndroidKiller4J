package com.richardtang.androidkiller4j.view.device;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import com.android.ddmlib.FileListingService;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.NullOutputReceiver;
import com.richardtang.androidkiller4j.constant.SvgName;
import com.richardtang.androidkiller4j.ui.action.ClickActionInstaller;
import com.richardtang.androidkiller4j.ui.action.ClickAction;
import com.richardtang.androidkiller4j.ui.border.MLineBorder;
import com.richardtang.androidkiller4j.ui.device.DeviceDirTree;
import com.richardtang.androidkiller4j.ui.device.DeviceFileTable;
import com.richardtang.androidkiller4j.util.ControlUtils;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * 设备资源
 *
 * @author RichardTang
 */
public class DeviceExplorerView extends JSplitPane {

    private IDevice            iDevice;
    private FileListingService fileListingService;
    private DeviceDirTree      deviceDirTree;
    private DeviceFileTable    deviceFileTable;

    // 执行的命令
    private final static String MOVE_FILE_COMMAND      = "mv %s %s";
    private final static String DELETE_FILE_COMMAND    = "rm -rf %s";
    private final static String MAKE_DIRECTORY_COMMAND = "mkdir %s";

    // 功能组
    private JTextField currentPathTextField = new JTextField(40);
    private JButton    moveFileButton       = ControlUtils.getMediumIconButton("移动文件", SvgName.MOVE);
    private JButton    deleteFileButton     = ControlUtils.getMediumIconButton("删除文件", SvgName.REMOVE);
    private JButton    uploadFileButton     = ControlUtils.getMediumIconButton("上传文件", SvgName.UPLOAD);
    private JButton    downloadFileButton   = ControlUtils.getMediumIconButton("下载文件", SvgName.DOWNLOAD);
    private JButton    makeDirectoryButton  = ControlUtils.getMediumIconButton("创建文件夹", SvgName.ADD);

    private JPanel fnBtnGroupPanel      = new JPanel(new FlowLayout());
    private JPanel currentPathCompPanel = new JPanel(new BorderLayout());
    private JPanel tableAndPathPanel    = new JPanel(new BorderLayout());

    public DeviceExplorerView(IDevice iDevice) {
        this.iDevice = iDevice;
        this.fileListingService = iDevice.getFileListingService();

        deviceFileTable = new DeviceFileTable(fileListingService);
        deviceDirTree = new DeviceDirTree(fileListingService) {
            @Override
            public void treeWillExpandWrapper(TreeExpansionEvent event, DeviceDirTreeNode clickedNode) {
                deviceFileTable.removeAll();
                FileListingService.FileEntry pFileEntry = clickedNode.getFileEntry();
                currentPathTextField.setText(pFileEntry.getFullPath());
                try {
                    for (FileListingService.FileEntry fileEntry : fileListingService.getChildrenSync(pFileEntry)) {
                        deviceFileTable.addRow(fileEntry);
                    }
                    deviceDirTree.loadChildNode(clickedNode);
                    deviceDirTree.getTreeModel().reload(clickedNode.getParent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // 功能按钮组
        fnBtnGroupPanel.add(moveFileButton);
        fnBtnGroupPanel.add(deleteFileButton);
        fnBtnGroupPanel.add(uploadFileButton);
        fnBtnGroupPanel.add(downloadFileButton);
        fnBtnGroupPanel.add(makeDirectoryButton);

        // 当前路径
        currentPathTextField.setEditable(false);
        currentPathTextField.setBackground(Color.WHITE);
        currentPathTextField.putClientProperty("JTextField.leadingComponent", new JLabel("当前路径: "));
        currentPathTextField.setBorder(new MLineBorder(1, false, false, true, false));
        currentPathCompPanel.add(currentPathTextField, BorderLayout.CENTER);
        currentPathCompPanel.add(fnBtnGroupPanel, BorderLayout.NORTH);
        currentPathCompPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, MLineBorder.defaultBorderColor));

        // 表格和路径组合
        tableAndPathPanel.add(currentPathCompPanel, BorderLayout.NORTH);
        tableAndPathPanel.add(new JScrollPane(deviceFileTable), BorderLayout.CENTER);

        setLeftComponent(new JScrollPane(deviceDirTree));
        setRightComponent(tableAndPathPanel);

        // 绑定事件
        deviceFileTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2) return;
                // 双击时展开树中对应的节点
                TreePath treePath = deviceDirTree.findTreePathByPath(deviceFileTable.getSelected().getFullEscapedPath());
                if (treePath != null) {
                    deviceDirTree.expandPath(treePath);
                    deviceDirTree.setSelectionPath(treePath);
                    deviceDirTree.scrollPathToVisible(treePath);
                    currentPathTextField.setText(deviceFileTable.getSelected().getFullPath());
                }
            }
        });
        ClickActionInstaller.bind(this);
    }

    /**
     * 获取表格中选中的文件对象，并在用户未选中文件时进行提示。
     *
     * @param tipMsg 提示信息
     * @return 选中的文件对象，未选中的话则返回null。
     */
    private FileListingService.FileEntry getSelectedFileEntryAndTipMsg(String tipMsg) {
        FileListingService.FileEntry fileEntry = deviceFileTable.getSelected();
        if (fileEntry == null) {
            ControlUtils.showMsgDialog("提示信息", tipMsg);
            return null;
        }
        return fileEntry;
    }

    /**
     * 文件下载
     *
     * @param event 事件对象
     */
    @ClickAction("downloadFileButton")
    public void downloadFileButtonClick(ActionEvent event) {
        FileListingService.FileEntry fileEntry = getSelectedFileEntryAndTipMsg("请选择一个需要下载的文件!");
        if (fileEntry == null) return;

        File saveFilePath = ControlUtils.chooserSaveDirectoryDialog();
        if (saveFilePath != null) {
            try {
                iDevice.pullFile(fileEntry.getFullPath(), saveFilePath.getAbsolutePath() + "/" + fileEntry.getName());
                ControlUtils.showMsgDialog("提示信息", "下载成功");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                ControlUtils.showMsgDialog("提示信息", "下载失败");
            }
        }
    }

    /**
     * 文件上传
     *
     * @param event 事件对象
     */
    @ClickAction("uploadFileButton")
    public void uploadFileButtonClick(ActionEvent event) {
        // 获取文件上传需要保存的位置
        FileListingService.FileEntry selectedFileEntry = deviceFileTable.getSelected();
        if (selectedFileEntry == null) {
            selectedFileEntry = ((DeviceDirTree.DeviceDirTreeNode) deviceDirTree.getLastSelectedPathComponent()).getFileEntry();
        }

        // 弹窗在本地电脑中选择需要上传的文件
        File file = ControlUtils.chooserFileDialog();
        if (file == null) {
            // 未选择需要上传的文件
            return;
        }

        // localPath自己电脑上的路径，remotePath远端设备的路径。
        String localPath = file.getAbsolutePath();
        String remotePath = selectedFileEntry.getFullEscapedPath();

        if (selectedFileEntry.isDirectory()) {
            // adb上选中的为文件夹时，直接已文件夹作为保存路径。
            remotePath += "/";
        } else {
            // 选中的为文件时，已文件所在位置为保存路径。
            remotePath = StrUtil.subBefore(remotePath, "/", true) + "/";
        }

        // 上传的文件存放的位置需要指定具体的文件名
        remotePath += file.getName();

        try {
            iDevice.pushFile(localPath, remotePath);
            // 上传成功更新表格上的数据
            FileListingService.FileEntry childFileEntry = deviceFileTable.findChildByName(selectedFileEntry, file.getName());
            if (childFileEntry != null) {
                deviceFileTable.addOrUpdate(childFileEntry);
                ControlUtils.showMsgDialog("提示信息", "上传成功");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        ControlUtils.showMsgDialog("提示信息", "上传失败");
    }

    /**
     * 删除文件
     *
     * @param event 事件对象
     */
    @ClickAction("deleteFileButton")
    public void deleteFileButtonClick(ActionEvent event) {
        FileListingService.FileEntry selectedFileEntry = getSelectedFileEntryAndTipMsg("请选中一个需要删除的文件!");
        if (selectedFileEntry == null) return;

        try {
            String filePath = selectedFileEntry.getFullEscapedPath();
            iDevice.executeShellCommand(String.format(DELETE_FILE_COMMAND, filePath), new NullOutputReceiver());

            // 更新表格
            deviceFileTable.remove(selectedFileEntry);

            // 是文件夹则需要更新目录树
            if (selectedFileEntry.isDirectory()) {
                DeviceDirTree.DeviceDirTreeNode node = deviceDirTree.findTreeNodeByPath(selectedFileEntry.getFullPath());
                deviceDirTree.refreshNode((DeviceDirTree.DeviceDirTreeNode) node.getParent());
            }

            ControlUtils.showMsgDialog("提示信息", "删除成功");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ControlUtils.showMsgDialog("提示信息", "删除失败");
        }
    }

    /**
     * 创建文件夹
     *
     * @param event 事件对象
     */
    @ClickAction("makeDirectoryButton")
    public void makeDirectoryButtonClick(ActionEvent event) {
        String currentPath = currentPathTextField.getText();
        if (StrUtil.isEmpty(currentPath)) {
            ControlUtils.showMsgDialog("提示信息", "当前路径为空，请选择文件夹的创建位置!");
            return;
        }

        String directoryName = ControlUtils.singleInputDialog("创建文件夹名称:");
        if (StrUtil.isEmpty(directoryName)) {
            return;
        }

        String fullPath = String.format("%s/%s", currentPath, directoryName);
        FileListingService.FileEntry fileEntry = deviceFileTable.findFileEntryByPath(fullPath);
        if (fileEntry != null) {
            ControlUtils.showMsgDialog("提示信息", "该文件夹已经存在!");
            return;
        }

        try {
            String command = String.format(MAKE_DIRECTORY_COMMAND, fullPath);
            iDevice.executeShellCommand(command, new NullOutputReceiver());

            // 更新DirTreeUi
            DeviceDirTree.DeviceDirTreeNode currentTreeNode = deviceDirTree.findTreeNodeByPath(currentPath);
            deviceDirTree.loadChildNode(currentTreeNode);
            deviceDirTree.getTreeModel().reload(currentTreeNode);

            // 更新Table
            FileListingService.FileEntry newFileEntry = deviceFileTable.findFileEntryByPath(fullPath);
            deviceFileTable.addRow(newFileEntry);

            ControlUtils.showMsgDialog("提示信息", "创建成功");
        } catch (Exception e) {
            StaticLog.error(e);
            ControlUtils.showMsgDialog("提示信息", "创建失败!");
        }
    }

    /**
     * 移动文件
     *
     * @param event 事件对象
     */
    @ClickAction("moveFileButton")
    public void moveFileButtonClick(ActionEvent event) {
        // 选中文件
        FileListingService.FileEntry selectedFileEntry = getSelectedFileEntryAndTipMsg("请选中一个需要移动的文件!");
        if (selectedFileEntry == null) return;

        // 获取新存储路径
        String newPath = ControlUtils.singleInputDialog("新路径:");
        if (StrUtil.isEmpty(newPath)) return;

        try {
            String command = String.format(MOVE_FILE_COMMAND, selectedFileEntry.getFullEscapedPath(), newPath);
            iDevice.executeShellCommand(command, new NullOutputReceiver());

            // 更新表格
            deviceFileTable.remove(selectedFileEntry);

            // 更新Tree
            if (selectedFileEntry.isDirectory()) {
                // 更新原来所在目录节点
                DeviceDirTree.DeviceDirTreeNode oldTreeNode = deviceDirTree.findTreeNodeByPath(selectedFileEntry.getFullEscapedPath());
                deviceDirTree.refreshNode((DeviceDirTree.DeviceDirTreeNode) oldTreeNode.getParent());

                // 更新新目录节点
                DeviceDirTree.DeviceDirTreeNode treeNode = deviceDirTree.findTreeNodeByPath(newPath);
                deviceDirTree.refreshNode(treeNode);
            }
            ControlUtils.showMsgDialog("提示信息", "移动完成!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            ControlUtils.showMsgDialog("提示信息", "移动失败!");
        }
    }
}