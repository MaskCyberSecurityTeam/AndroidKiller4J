package com.richardtang.androidkiller4j.ui.tree;

import lombok.Data;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

/**
 * 该类代表{@link DirectoryTree}中的每个节点
 *
 * @author RichardTang
 */
@Data
public class DirectoryTreeFileNode extends DefaultMutableTreeNode {

    private String  name;
    private File    file;
    private boolean isDirectory;

    public DirectoryTreeFileNode(File file) {
        this.file        = file;
        this.name        = file.getName();
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