package com.richardtang.androidkiller4j.ui.tree;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 * 初始化根节点的子节点，定义节点是否为可展开节点的依据。
 *
 * @author RichardTang
 */
public class DirectoryTreeModel extends DefaultTreeModel {

    public DirectoryTreeModel(TreeNode root) {
        super(root);

        // 根节点添加初始化节点，必须在该函数内完成，不然显示不出来。
        DirectoryTree.loadChildNode((DirectoryTreeFileNode) root);
    }

    @Override
    public boolean isLeaf(Object node) {
        return !((DirectoryTreeFileNode) node).isDirectory();
    }
}
