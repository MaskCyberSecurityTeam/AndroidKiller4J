package com.richardtang.androidkiller4j.ui.device;

import cn.hutool.core.util.StrUtil;
import com.android.ddmlib.FileListingService;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备目录树
 *
 * @author RichardTang
 */
public abstract class DeviceDirTree extends JTree {

    private DeviceDirTreeNode  rootNode;
    private DeviceDirTreeModel treeModel;
    private FileListingService fileListingService;

    public DeviceDirTree(FileListingService fileListingService) {
        this.fileListingService = fileListingService;
        this.rootNode = new DeviceDirTreeNode(fileListingService.getRoot());
        this.treeModel = new DeviceDirTreeModel();

        setShowsRootHandles(true);
        setModel(treeModel);
        setCellRenderer(new DeviceDirTreeCellRenderer());
        addTreeWillExpandListener(new DeviceDirTreeWillExpandAction());
    }

    /**
     * 加载子节点
     *
     * @param pNode 父节点
     */
    public void loadChildNode(DeviceDirTreeNode pNode) {
        try {
            pNode.removeAllChildren();
            FileListingService.FileEntry[] childrens = fileListingService.getChildrenSync(pNode.getFileEntry());
            for (FileListingService.FileEntry children : childrens) {
                // 在该组件中只有是目录的FileEntry才能做为子节点
                if (!children.isDirectory()) continue;
                pNode.add(new DeviceDirTreeNode(children));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据字符串路径，找到TreeNode对象。
     *
     * @param path 字符串路径
     * @return 树节点对象，未找到时返回null。
     */
    public DeviceDirTreeNode findTreeNodeByPath(String path) {
        List<String> pathSplit = StrUtil.splitTrim(path, "/");
        DeviceDirTreeNode parentNode = (DeviceDirTreeNode) getModel().getRoot();
        for (String pathSegment : pathSplit) {
            for (int x = 0; x < parentNode.getChildCount(); x++) {
                DeviceDirTreeNode node = (DeviceDirTreeNode) parentNode.getChildAt(x);
                if (pathSegment.equals(node.getName())) {
                    if (node.getChildCount() <= 0) loadChildNode(node);
                    parentNode = node;
                    break;
                } else if (x == (parentNode.getChildCount())) {
                    return null;
                }
            }
        }
        return parentNode;
    }

    /**
     * 根据字符串路径，找到TreePath对象。
     *
     * @param path 字符串路径
     * @return 树路径对象，未找到时返回null。
     */
    public TreePath findTreePathByPath(String path) {
        return new TreePath(findTreeNodeByPath(path).getPath());
    }

    /**
     * 根据给定的字符串路径，获取对应的FileEntry。
     * 说明:  当给定的路径为 /data/local/tmp/ 时,result的结果为一个集合, 集合
     * 中的元素分别按顺序对应三个FileEntry，三个FileEntry即为data、local、tmp。
     * <p>
     * 说明2: 当查找的路径中有一个路径是不存在的，则直接返回null，不再向下寻找FileEntry。
     *
     * @param path 需要查找的字符串路径，如:/data/local/tmp/。
     * @return 符合路径顺序的FileEntry集合，未找到则返回null。
     */
    public ArrayList<FileListingService.FileEntry> findFileEntryByPath(String path) {
        // 路径是带/开头，删除开头的/。避免后续的split数组后的，数组的0位元素是空字符串。
        List<String> pathSplit = StrUtil.splitTrim(path, "/");
        ArrayList<FileListingService.FileEntry> result = new ArrayList<>();
        try {
            FileListingService.FileEntry[] children = fileListingService.getChildrenSync(rootNode.getFileEntry());
            for (String pathSegment : pathSplit) {
                for (int x = 0; x < children.length; x++) {
                    if (pathSegment.equals(children[x].getName())) {
                        result.add(children[x]);
                        children = fileListingService.getChildrenSync(children[x]);
                        break;
                    } else if (x == (children.length - 1)) {
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.size() != pathSplit.size() ? null : result;
    }

    /**
     * 刷新节点
     *
     * @param node 需要刷新的节点
     */
    public void refreshNode(DeviceDirTreeNode node) {
        loadChildNode(node);
        treeModel.reload(node);
    }

    public DeviceDirTreeModel getTreeModel() {
        return treeModel;
    }

    /**
     * 节点展开时需要进行的额外操作
     */
    public abstract void treeWillExpandWrapper(TreeExpansionEvent event, DeviceDirTreeNode clickedNode);

    /**
     * 对FileEntry的包装。
     */
    public static class DeviceDirTreeNode extends DefaultMutableTreeNode {

        private String                       name;
        private boolean                      isDirectory;
        private FileListingService.FileEntry fileEntry;

        public DeviceDirTreeNode(FileListingService.FileEntry fileEntry) {
            super(fileEntry.getFullPath());
            this.fileEntry = fileEntry;
            this.name = fileEntry.getName();
            this.isDirectory = fileEntry.isDirectory();
        }

        public String getName() {
            return name;
        }

        public boolean isDirectory() {
            return !isDirectory;
        }

        public FileListingService.FileEntry getFileEntry() {
            return fileEntry;
        }
    }

    /**
     * 设备文件树模型
     */
    public class DeviceDirTreeModel extends DefaultTreeModel {
        public DeviceDirTreeModel() {
            super(rootNode);
            loadChildNode(rootNode);
        }

        @Override
        public boolean isLeaf(Object node) {
            return ((DeviceDirTreeNode) node).isDirectory();
        }
    }

    /**
     * 设备文件树节点的渲染配置。
     */
    public class DeviceDirTreeCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree,
                                                      Object value,
                                                      boolean sel,
                                                      boolean expanded,
                                                      boolean leaf,
                                                      int row,
                                                      boolean hasFocus) {

            JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (value instanceof DeviceDirTreeNode) {
                DeviceDirTreeNode node = (DeviceDirTreeNode) value;
                label.setText(node.getName());
                label.setOpaque(false);
            }
            return label;
        }
    }

    /**
     * 节点折叠和展开事件
     */
    public class DeviceDirTreeWillExpandAction implements TreeWillExpandListener {

        @Override
        public void treeWillExpand(TreeExpansionEvent event) {
            DeviceDirTreeNode currentNode = (DeviceDirTreeNode) event.getPath().getLastPathComponent();
            loadChildNode(currentNode);
            treeWillExpandWrapper(event, currentNode);
        }

        @Override
        public void treeWillCollapse(TreeExpansionEvent event) {

        }
    }
}
