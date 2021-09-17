package com.funnysec.richardtang.androidkiller4j;

import com.funnysec.richardtang.androidkiller4j.config.ResourcePathConfig;
import com.funnysec.richardtang.androidkiller4j.constant.ProtocolString;
import com.funnysec.richardtang.androidkiller4j.ui.wrapper.ImageView;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;

import java.io.File;

public class Demo1 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        TreeTableView<File>                 treeTableView  = new TreeTableView<>();
        TreeTableColumn<File, FileTreeItem> fileNameColumn = new TreeTableColumn<>("文件名");
        TreeTableColumn<File, String>       filePathColumn = new TreeTableColumn<>("文件路径");

        fileNameColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>((FileTreeItem) cellData.getValue())
        );

        filePathColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<String>(cellData.getValue().getValue().getAbsolutePath()));

        fileNameColumn.setCellFactory(column -> {
            TreeTableCell<File, FileTreeItem> cell = new TreeTableCell<File, FileTreeItem>() {
                @Override
                protected void updateItem(FileTreeItem item, boolean empty) {
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
            };
            return cell;
        });


        treeTableView.getColumns().addAll(fileNameColumn, filePathColumn);
        FileTreeItem rootFileTreeItem = new FileTreeItem(new File("/"));

        treeTableView.setRoot(rootFileTreeItem);
        treeTableView.setShowRoot(true);

        primaryStage.setScene(new Scene(treeTableView));
        primaryStage.show();
    }

    private class FileTreeItem extends TreeItem<File> {

        private boolean expanded = false;

        public FileTreeItem(File file) {
            super(file);
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
            if (getValue() == null || getValue().isFile()) {
                return;
            }

            File[] files = getValue().listFiles();
            if (files != null && files.length > 0) {
                getChildren().clear();
                for (File f : files) {
                    getChildren().add(new FileTreeItem(f));
                }
            }
        }
    }
}
