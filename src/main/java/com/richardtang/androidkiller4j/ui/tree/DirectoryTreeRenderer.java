package com.richardtang.androidkiller4j.ui.tree;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * {@link DirectoryTree}组件节点样式渲染
 *
 * @author RichardTang
 */
public class DirectoryTreeRenderer extends DefaultTreeCellRenderer {

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

        if (value instanceof DirectoryTreeFileNode) {
            DirectoryTreeFileNode fileNode = (DirectoryTreeFileNode) value;
            label.setText(fileNode.getName());
            label.setOpaque(false);
        }

        return label;
    }
}