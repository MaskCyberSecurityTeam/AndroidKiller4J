package com.funnysec.richardtang.androidkiller4j.ui;

import com.funnysec.richardtang.androidkiller4j.util.CommonUtil;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

/**
 * 文件树，给定一个路径展示出对应的文件列表。
 *
 * @author RichardTang
 */
public class SystemFileTreeView extends TreeView<File> {

    // 存储系统默认文件类型图标的Map，主要用于缓存。
    private static final HashMap<String, Image> fileSystemDefIconCache = new HashMap<>();

    // Windows上使用
    public static FileSystemView fileSystemView;

    // Mac上使用
    public static JFileChooser jFileChooser;

    static {
        // 根据系统类型使用不通的获取图标的方案
        if (CommonUtil.isWindows()) {
            fileSystemView = FileSystemView.getFileSystemView();
        } else {
            jFileChooser = new JFileChooser();
        }
    }

    public SystemFileTreeView(String fileRootPath) {
        this(new File(fileRootPath));
    }

    public SystemFileTreeView(File rootDir) {
        // 初始化根节点
        TreeItem<File> rootTreeItem = new TreeItem<File>(rootDir);
        setCellFactory(stringTreeView -> new SystemFileTreeCell());
        setRoot(rootTreeItem);

        // 往根节点中添加子节点
        for (File file : rootDir.listFiles()) {
            rootTreeItem.getChildren().add(new SystemFileTreeItem(file));
        }
    }

    /**
     * 获取系统文件默认图标
     *
     * @param file 需要获取图标的文件对象
     * @return 图片对象 {@link Image}
     */
    public static Image getFileSystemIcon(File file) {
        // 先根据文件的后缀名从缓存中取数据
        String key   = CommonUtil.getFileSuffix(file);
        Image  image = fileSystemDefIconCache.get(key);

        if (image != null) {
            return image;
        }

        // 缓存中没有，调用api获取系统对应的默认图片对象
        // fileSystemView为空代表是MacOS系统
        Icon icon = (
                fileSystemView == null ? jFileChooser.getUI().getFileView(jFileChooser).getIcon(file) : fileSystemView.getSystemIcon(file)
        );
        BufferedImage bufferedImage = new BufferedImage(
                icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB
        );
        icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
        image = SwingFXUtils.toFXImage(bufferedImage, null);

        // 存到缓存中
        fileSystemDefIconCache.put(key, image);
        return image;
    }

    protected class SystemFileTreeCell extends TreeCell<File> {
        @Override
        protected void updateItem(File item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                // 注意这里需要设置为null，不然会导致ListView加载后出现显示bug。
                setText(null);
                setGraphic(null);
            } else {
                setText(item.getName());
                setGraphic(new ImageView(getFileSystemIcon(item)));
            }
        }
    }

    protected class SystemFileTreeItem extends TreeItem<File> {

        // 未加载过子目录，默认为true
        private boolean isNoLoadedChildren = true;

        public SystemFileTreeItem(File item) {
            super(item);
        }

        /**
         * TODO 这里后边改为addEventHandler的方式实现添加子节点
         * 参考com.funnysec.richardtang.androidkiller4j.view.device.explorer.DeviceExplorerView的写法。
         */
        @Override
        public ObservableList<TreeItem<File>> getChildren() {
            // 动态加载获取子treeItem
            ObservableList<TreeItem<File>> children = super.getChildren();
            if (this.isNoLoadedChildren && this.isExpanded() && this.getValue().isDirectory()) {
                this.isNoLoadedChildren = false;
                File[] files = new File(getValue().getAbsolutePath()).listFiles();
                if (files != null && files.length > 0) {
                    for (File f : files) {
                        children.add(new SystemFileTreeItem(f));
                    }
                }
            }
            return children;
        }

        @Override
        public boolean isLeaf() {
            // 是目录的时候显示展开的小三角
            return !getValue().isDirectory();
        }
    }
}
