package com.richardtang.androidkiller4j.view.workbench;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import com.richardtang.androidkiller4j.bean.Apk;
import com.richardtang.androidkiller4j.constant.ResPath;
import com.richardtang.androidkiller4j.ui.tabpane.Tab;
import com.richardtang.androidkiller4j.ui.tabpane.TabPane;
import com.richardtang.androidkiller4j.util.ControlUtil;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 工作空间代码编辑器区域
 *
 * @author RichardTang
 */
public class WorkbenchCodeTextAreaView extends JPanel {

    private Apk apk;

    private JToolBar funToolBar;
    private JButton  saveButton;
    private JButton  openDirButton;
    private JButton  decompileButton;
    private TabPane  tabPane;

    public WorkbenchCodeTextAreaView(Apk apk) {
        this.apk = apk;
        renderFunMenuBar();
        tabPane = new TabPane();
        tabPane.addChangeListener(e -> tabChangeAction());

        setLayout(new BorderLayout());
        add(funToolBar, BorderLayout.NORTH);
        add(tabPane, BorderLayout.CENTER);
    }

    private void renderFunMenuBar() {
        // 保存按钮和Ctrl+S保存快捷键实现
        saveButton = new JButton(ControlUtil.getSVGIcon("save.svg"));
        saveButton.setToolTipText("保存");
        saveButton.registerKeyboardAction(
                e -> saveSelectTabFileContent(),
                KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        saveButton.addActionListener(e -> saveSelectTabFileContent());

        // 打开所在目录
        openDirButton = new JButton(ControlUtil.getSVGIcon("directory.svg"));
        openDirButton.setToolTipText("打开所在目录");
        openDirButton.addActionListener(e -> {
            File file = (File) tabPane.getSelectedTab().getData();
            try {
                Desktop.getDesktop().open(file.getParentFile());
            } catch (Exception exception) {
                exception.printStackTrace();
                ControlUtil.showMsgDialog("提示信息", "无法打开");
            }
        });

        // 反编译为java代码
        decompileButton = new JButton(ControlUtil.getSVGIcon("java.svg"));
        decompileButton.setToolTipText("反编译为Java代码");
        decompileButton.addActionListener(e -> {
            File file = (File) tabPane.getSelectedTab().getData();
            // 只能反编译.smali和.class文件
            if (StrUtil.containsAny(file.getName(), ".smali")) {
                // 找到当前smali文件对应的.class文件
                String classFilePath = file.getAbsolutePath()
                        .replace((apk.getBasePath() + "/smali/"), "")
                        .replace(".smali", ".class");

                // 反编译为java代码
                String javaCode = RuntimeUtil.execForStr(
                        String.format("java -jar %s %s%s/%s",
                                ResPath.CRF,
                                ResPath.PROJECT_SRC_DIR,
                                apk.getFileName().replace(".apk", ""),
                                classFilePath
                        )
                );

                // 添加到Tab页上
                RTextScrollPane rTextScrollPane = createCodeEditorComponent(javaCode);
                rTextScrollPane.getTextArea().setEditable(false);
                tabPane.addTab(new Tab(file.getName().replace(".smali", ".java"), rTextScrollPane, file));
            } else {
                ControlUtil.showMsgDialog("提示信息", "反编译只能操作.smali文件");
            }
        });

        funToolBar = new JToolBar(JToolBar.HORIZONTAL);
        funToolBar.setFloatable(false);
        funToolBar.add(saveButton);
        funToolBar.add(openDirButton);
        funToolBar.addSeparator();
        funToolBar.add(decompileButton);
    }

    /**
     * 添加一个需要显示处理的文件
     * TODO 目前未处理，相同文件名不同路径的情况。
     *
     * @param file 需要显示的文件
     */
    public void addFile(File file) {
        Tab tab = new Tab(file.getName(), createCodeEditorComponent(file), file);
        tabPane.addTab(tab);
        tabPane.setSelectedComponent(tab.getComponent());
    }

    /**
     * 一个Tab页面对应一个File，每个Tab单独一个代码编辑器。
     *
     * @param file 当前代码编辑器对应的文件
     * @return 代码编辑器组件
     */
    private RTextScrollPane createCodeEditorComponent(File file) {
        return createCodeEditorComponent(FileUtil.readString(file, StandardCharsets.UTF_8));
    }

    /**
     * 创建一个Tab并设置内容
     *
     * @param content 当前代码编辑器对应的文件
     * @return 代码编辑器组件
     */
    private RTextScrollPane createCodeEditorComponent(String content) {
        RSyntaxTextArea rSyntaxTextArea = new RSyntaxTextArea(20, 60);
        rSyntaxTextArea.setCodeFoldingEnabled(true);
        rSyntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        rSyntaxTextArea.setText(content);
        RTextScrollPane rTextScrollPane = new RTextScrollPane(rSyntaxTextArea);
        rTextScrollPane.getVerticalScrollBar().setValue(0);
        return rTextScrollPane;
    }

    /**
     * 保存当前选中的Tab页中代码编辑的的内容到磁盘中
     */
    private void saveSelectTabFileContent() {
        Tab selectedTab = tabPane.getSelectedTab();

        // 获取到当前选中的Tab中的代码编辑器
        RTextArea textArea = ((RTextScrollPane) selectedTab.getComponent()).getTextArea();

        // 将代码编辑器中的内容写入硬盘文件中
        File file = (File) selectedTab.getData();
        FileUtil.writeString(textArea.getText(), file, StandardCharsets.UTF_8);
        System.out.println("保存成功");
    }

    /**
     * Tab页切换时启用和禁用对应的功能
     */
    private void tabChangeAction() {
        String suffix = FileUtil.getSuffix(tabPane.getSelectedTab().getTitle());

        saveButton.setEnabled(true);
        openDirButton.setEnabled(true);
        decompileButton.setEnabled(true);

        switch (suffix) {
            case "java":
                saveButton.setEnabled(false);
                openDirButton.setEnabled(false);
                decompileButton.setEnabled(false);
                break;
            case "smali":
                break;
            default:
                decompileButton.setEnabled(false);
                break;
        }
    }
}