package com.richardtang.androidkiller4j.ui.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

/**
 * 文件内容高亮节点，用于配合{@see FileContentHighlightTree}，主要用在搜索功能上。
 *
 * @author RichardTang
 */
public class FileContentHighlightNode extends DefaultMutableTreeNode {

    // 每个节点都对应一个文件
    private File file;

    // 关键字所在行号
    private int lineNumber;

    // 是否为行节点
    private boolean isLineNode;

    public FileContentHighlightNode(String text, File file, int lineNumber) {
        this(text, file, lineNumber, false);
    }

    public FileContentHighlightNode(String text, File file, int lineNumber, boolean isLineNode) {
        super(text);
        this.file = file;
        this.isLineNode = isLineNode;
        this.lineNumber = lineNumber;
    }

    public File getFile() {
        return file;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public boolean isLineNode() {
        return isLineNode;
    }
}