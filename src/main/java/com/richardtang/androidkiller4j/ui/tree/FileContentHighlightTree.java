package com.richardtang.androidkiller4j.ui.tree;

import cn.hutool.http.HtmlUtil;
import com.formdev.flatlaf.icons.FlatTreeClosedIcon;
import com.formdev.flatlaf.icons.FlatTreeLeafIcon;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

/**
 * 高亮显示文件内容的关键字，已一种树的形式进行展现。
 * 实现的方式主要是靠JLabel，JLabel可以以HTML代码的方式设置样式，从而实现高亮。
 *
 * @author RichardTang
 */
public class FileContentHighlightTree extends JTree {

    // 关键字
    private String keyword;

    // 高亮的关键字
    private String highlightKeyword;

    /**
     * 创建带有高亮效果的树
     *
     * @param root    根节点
     * @param keyword 需要高亮的关键字
     */
    public FileContentHighlightTree(FileContentHighlightNode root, String keyword) {
        this.keyword = keyword;
        this.highlightKeyword = String.format("<font color='red'>%s</font>", keyword);

        setRootVisible(true);
        setModel(new DefaultTreeModel(root));
        setCellRenderer(new FileContentHighlightCellRenderer());
    }

    /**
     * 节点渲染
     */
    private class FileContentHighlightCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            FileContentHighlightNode currentNode = (FileContentHighlightNode) value;

            // 只有为文件节点时才需要进行高亮替换
            if (currentNode.isLineNode()) {
                // 如果内容是html格式的内容，则需要先进行转义。
                String text = String.format("<html><font>第%s行: </font>%s</html>",
                        currentNode.getLineNumber(),
                        HtmlUtil.escape(label.getText()));

                // 将关键字替换为高亮关键字
                label.setText(text.replaceAll(keyword, highlightKeyword));
                label.setIcon(null);
            } else if (currentNode.getFile().isDirectory()) {
                // 目录的话不进行高亮操作
                label.setIcon(new FlatTreeClosedIcon());
            } else {
                // 根节点的情况
                label.setIcon(new FlatTreeLeafIcon());
            }
            return label;
        }
    }
}