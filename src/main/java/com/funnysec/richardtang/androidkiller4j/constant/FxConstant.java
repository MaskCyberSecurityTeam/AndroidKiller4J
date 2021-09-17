package com.funnysec.richardtang.androidkiller4j.constant;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 * 定义一些JavaFx中可以重复使用的组件对象
 *
 * @author RichardTang
 */
public class FxConstant {

    // JavaFx的文件选择器
    public final static FileChooser FILE_CHOOSER = new FileChooser();

    // JavaFx的目录选择器
    public final static DirectoryChooser DIRECTORY_CHOOSER = new DirectoryChooser();

    // 文件过滤器
    public final static FileChooser.ExtensionFilter APK_EXT_FILETER = new FileChooser.ExtensionFilter("只允许格式为:apk的文件", "*.apk");
}
