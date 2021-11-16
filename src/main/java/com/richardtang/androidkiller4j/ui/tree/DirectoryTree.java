package com.richardtang.androidkiller4j.ui.tree;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import java.io.File;

/**
 * 系统文件树组件，给定一个根路径，自动渲染出对应的子孙目录。
 *
 * @author RichardTang
 */
public class DirectoryTree extends JTree {

    private DirectoryTreeFileNode rootNode;

    public DirectoryTree(File rootFile) {
        rootNode = new DirectoryTreeFileNode(rootFile);

        // 初始化必要的配置
        setShowsRootHandles(true);
        setModel(new DirectoryTreeModel(rootNode));
        setCellRenderer(new DirectoryTreeRenderer());
        addTreeWillExpandListener(new DirectoryTreeFileNodeWillExpandAction());
    }

    /**
     * 加载子节点，根据给定的{@link DirectoryTreeFileNode}自动加载子节点进行加载。
     *
     * @param pNode 需要加载子节点的父节点
     */
    public static void loadChildNode(DirectoryTreeFileNode pNode) {
        for (File file : pNode.getFile().listFiles()) {
            pNode.add(new DirectoryTreeFileNode(file));
        }
    }

    /**
     * 节点关闭展开事件
     */
    private static class DirectoryTreeFileNodeWillExpandAction implements TreeWillExpandListener {

        @Override
        public void treeWillExpand(TreeExpansionEvent event) {
            DirectoryTreeFileNode currentNode = (DirectoryTreeFileNode) event.getPath().getLastPathComponent();
            loadChildNode(currentNode);
        }

        @Override
        public void treeWillCollapse(TreeExpansionEvent event) {
        }
    }
}