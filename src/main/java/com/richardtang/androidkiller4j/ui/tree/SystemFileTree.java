package com.richardtang.androidkiller4j.ui.tree;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.io.File;

/**
 * 操作系统文件树
 *
 * @author RichardTang
 */
public class SystemFileTree extends JTree {

    private SystemFileTreeNode rootNode;

    public SystemFileTree(File rootFile) {
        rootNode = new SystemFileTreeNode(rootFile);

        // 设置JTree模型、渲染、节点折叠和展开事件
        setShowsRootHandles(true);
        setModel(new SystemFileTreeModel(rootNode));
        setCellRenderer(new SystemFileTreeRenderer());
        addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent event) {
                loadChildNode((SystemFileTreeNode) event.getPath().getLastPathComponent());
            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent event) {

            }
        });
    }

    /**
     * 给定父节点，加载子节点。
     *
     * @param pNode 父节点
     */
    public void loadChildNode(SystemFileTreeNode pNode) {
        for (File file : pNode.getFile().listFiles()) {
            pNode.add(new SystemFileTreeNode(file));
        }
    }

    @Getter
    public class SystemFileTreeModel extends DefaultTreeModel {

        public SystemFileTreeModel(TreeNode root) {
            super(root);

            // 根节点添加初始化节点，必须在该函数内完成，不然显示不出来。
            loadChildNode((SystemFileTreeNode) root);
        }

        @Override
        public boolean isLeaf(Object node) {
            return !((SystemFileTreeNode) node).isDirectory();
        }
    }

    @Getter
    @EqualsAndHashCode(callSuper = true)
    public class SystemFileTreeNode extends DefaultMutableTreeNode {

        private String  name;
        private File    file;
        private boolean isDirectory;

        public SystemFileTreeNode(File file) {
            this.file = file;
            this.name = file.getName();
            this.isDirectory = file.isDirectory();
        }

        /**
         * toString用于JTree组件在显示时的名称作用
         *
         * @return 节点名称
         */
        @Override
        public String toString() {
            return name;
        }
    }

    @Getter
    @EqualsAndHashCode(callSuper = true)
    public class SystemFileTreeRenderer extends DefaultTreeCellRenderer {

        /**
         * 重写显示的样式
         */
        @Override
        public Component getTreeCellRendererComponent(JTree tree,
                                                      Object value,
                                                      boolean sel,
                                                      boolean expanded,
                                                      boolean leaf,
                                                      int row,
                                                      boolean hasFocus) {

            JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            if (value instanceof SystemFileTreeNode) {
                SystemFileTreeNode fileNode = (SystemFileTreeNode) value;
                label.setText(fileNode.getName());
                label.setOpaque(false);
            }

            return label;
        }
    }
}
