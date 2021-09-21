package com.funnysec.richardtang.androidkiller4j.view.device;

import com.android.ddmlib.FileListingService;
import com.android.ddmlib.IDevice;
import com.funnysec.richardtang.androidkiller4j.config.ResourcePathConfig;
import com.funnysec.richardtang.androidkiller4j.constant.Icon;
import com.funnysec.richardtang.androidkiller4j.constant.ProtocolString;
import com.funnysec.richardtang.androidkiller4j.pojo.TabUserData;
import com.funnysec.richardtang.androidkiller4j.ui.wrapper.ImageView;
import com.funnysec.richardtang.androidkiller4j.view.BaseView;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import lombok.Data;

import java.io.File;

@Data
public class DeviceExplorerView extends BaseView<Tab> {

    private IDevice iDevice;

    private FileListingService fileListingService;

    private TreeTableView<FileListingService.FileEntry>                           treeTableView;
    private TreeTableColumn<FileListingService.FileEntry, DeviceExplorerTreeItem> fileNameColumn;
    private TreeTableColumn<FileListingService.FileEntry, String>                 fileInfoColumn;
    private TreeTableColumn<FileListingService.FileEntry, String>                 filePermissionColumn;
    private TreeTableColumn<FileListingService.FileEntry, String>                 fileSizeColumn;
    private TreeTableColumn<FileListingService.FileEntry, String>                 fileDateColumn;
    private TreeTableColumn<FileListingService.FileEntry, String>                 fileOwnerColumn;
    private TreeTableColumn<FileListingService.FileEntry, String>                 fileGroupColumn;

    public DeviceExplorerView(IDevice iDevice) {
        this.iDevice = iDevice;
        afterPropertiesSet();
    }

    @Override
    protected void initUi() {
        treeTableView        = new TreeTableView<>();
        fileNameColumn       = new TreeTableColumn<>("文件名");
        fileInfoColumn       = new TreeTableColumn<>("文件信息");
        filePermissionColumn = new TreeTableColumn<>("文件权限");
        fileSizeColumn       = new TreeTableColumn<>("文件大小");
        fileDateColumn       = new TreeTableColumn<>("文件日期");
        fileOwnerColumn      = new TreeTableColumn<>("所有者");
        fileGroupColumn      = new TreeTableColumn<>("所属组");
    }

    @Override
    protected void initAttr() {
        fileNameColumn.setCellFactory(column -> new DeviceExplorerTreeTableCell());

        fileNameColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper(param.getValue()));
        fileInfoColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue().getInfo()));
        filePermissionColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue().getPermissions()));
        fileSizeColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue().getSize()));
        fileDateColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue().getDate()));
        fileOwnerColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue().getOwner()));
        fileGroupColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue().getGroup()));

        fileNameColumn.setPrefWidth(150);
        fileNameColumn.setPrefWidth(150);
        fileInfoColumn.setPrefWidth(150);
        filePermissionColumn.setPrefWidth(150);
        fileSizeColumn.setPrefWidth(150);
        fileDateColumn.setPrefWidth(150);
        fileOwnerColumn.setPrefWidth(150);
        fileGroupColumn.setPrefWidth(150);

        treeTableView.setShowRoot(false);
        getRootPane().setGraphic(Icon.DEVICE_EXPLORER_VIEW_TAB);
        getRootPane().setText("文件管理器");
        getRootPane().setUserData(new TabUserData<Void>("文件管理器", null));
    }

    @Override
    protected void initLayout() {
        treeTableView.getColumns().setAll(
                fileNameColumn, fileInfoColumn, filePermissionColumn, fileSizeColumn,
                fileDateColumn, fileOwnerColumn, fileGroupColumn
        );
        getRootPane().setContent(treeTableView);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initialize() {
        fileListingService = iDevice.getFileListingService();
        FileListingService.FileEntry   rootFileEntry = fileListingService.getRoot();
        FileListingService.FileEntry[] fileEntrys    = fileListingService.getChildren(rootFileEntry, true, null);

        TreeItem<FileListingService.FileEntry> rootTreeItem = new TreeItem<>(rootFileEntry);
        treeTableView.setRoot(rootTreeItem);

        for (FileListingService.FileEntry fileEntry : fileEntrys) {
            DeviceExplorerTreeItem deviceExplorerTreeItem = new DeviceExplorerTreeItem(fileEntry);
            rootTreeItem.getChildren().add(deviceExplorerTreeItem);
        }
    }

    private class DeviceExplorerTreeTableCell extends TreeTableCell<FileListingService.FileEntry, DeviceExplorerTreeItem> {
        @Override
        protected void updateItem(DeviceExplorerTreeItem item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty || item.getValue() == null) {
                setText(null);
                setGraphic(null);
            } else {
                if (item.isLeaf()) {
                    setGraphic(new ImageView(ProtocolString.FILE + ResourcePathConfig.IMAGE + "file.png"));
                } else {
                    setGraphic(new ImageView(ProtocolString.FILE + ResourcePathConfig.IMAGE + "dir.png"));
                }
                setText(item.getValue().getName());
            }
        }
    }

    private class DeviceExplorerTreeItem extends TreeItem<FileListingService.FileEntry> {

        private boolean expanded = false;

        public DeviceExplorerTreeItem(FileListingService.FileEntry fileEntry) {
            super(fileEntry);
            EventHandler<TreeModificationEvent<File>> eventHandler = event -> changeExpanded();
            addEventHandler(TreeItem.branchExpandedEvent(), eventHandler);
            addEventHandler(TreeItem.branchCollapsedEvent(), eventHandler);
        }

        @Override
        public boolean isLeaf() {
            return !getValue().isDirectory();
        }

        private void changeExpanded() {
            if (expanded != isExpanded()) {
                expanded = isExpanded();
                if (expanded) {
                    createChildren();
                } else {
                    getChildren().clear();
                }
                if (getChildren().size() == 0) {
                    Event.fireEvent(this, new TreeItem.TreeModificationEvent<>(TreeItem.valueChangedEvent(), this, getValue()));
                }
            }
        }

        private void createChildren() {
            FileListingService.FileEntry fileEntry = getValue();
            if (fileEntry == null || !fileEntry.isDirectory()) {
                return;
            }

            FileListingService.FileEntry[] childrens =
                    fileListingService.getChildren(fileEntry, true, null);

            for (FileListingService.FileEntry childFileEntry : childrens) {
                getChildren().addAll(new DeviceExplorerTreeItem(childFileEntry));
            }
        }
    }
}
