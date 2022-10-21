package com.richardtang.androidkiller4j.view;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import com.formdev.flatlaf.FlatClientProperties;
import com.richardtang.androidkiller4j.MainWindow;
import com.richardtang.androidkiller4j.bean.Apk;
import com.richardtang.androidkiller4j.constant.R;
import com.richardtang.androidkiller4j.constant.Size;
import com.richardtang.androidkiller4j.constant.Suffix;
import com.richardtang.androidkiller4j.constant.SvgName;
import com.richardtang.androidkiller4j.ui.action.ClickAction;
import com.richardtang.androidkiller4j.ui.action.ClickActionInstaller;
import com.richardtang.androidkiller4j.ui.border.MLineBorder;
import com.richardtang.androidkiller4j.ui.rsyntaxtextarea.SyntaxConstantsWrapper;
import com.richardtang.androidkiller4j.ui.tabpane.Tab;
import com.richardtang.androidkiller4j.ui.tabpane.TabPane;
import com.richardtang.androidkiller4j.ui.tree.FileContentHighlightNode;
import com.richardtang.androidkiller4j.ui.tree.SystemFileTree;
import com.richardtang.androidkiller4j.util.ControlUtils;
import com.richardtang.androidkiller4j.util.JadxUtils;
import lombok.Data;
import net.miginfocom.swing.MigLayout;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * 工作台视图，一个APK对应一个工作台。
 *
 * @author RichardTang
 */
@Data
public class WorkbenchView extends JSplitPane {

    // 当前工作台对应处理的apk
    private final Apk apk;

    // 左侧选项卡
    private JPanel      infoPanel         = new JPanel();
    private JPanel      managerPanel      = new JPanel();
    private JPanel      searchPanel       = new JPanel();
    private JTabbedPane featureTabbedPane = new JTabbedPane();

    // 信息选项卡(infoPanel)组件
    private static final String ROOT            = "Root";
    private static final String ACTIVITY        = "Activity";
    private static final String SERVICE         = "Service";
    private static final String RECEIVE         = "Receive";
    private static final String USES_PERMISSION = "UsesPermission";

    // 组件树
    private JTree appComponentTree = new JTree();

    // 组件树上显示的图标
    private Icon LETTER_A_ICON = ControlUtils.getSVGIcon(SvgName.LETTER_A, Size.MEDIUM);
    private Icon LETTER_U_ICON = ControlUtils.getSVGIcon(SvgName.LETTER_U, Size.MEDIUM);
    private Icon LETTER_R_ICON = ControlUtils.getSVGIcon(SvgName.LETTER_R, Size.MEDIUM);
    private Icon LETTER_S_ICON = ControlUtils.getSVGIcon(SvgName.LETTER_S, Size.MEDIUM);
    private Icon LETTER_I_ICON = ControlUtils.getSVGIcon(SvgName.LETTER_I, Size.MEDIUM);

    // 搜索选项卡(searchPanel)组件
    private JProgressBar      searchProgressBar;
    private JButton           searchButton           = new JButton("开始搜索");
    private JTextField        searchContentTextField = new JTextField();
    private JComboBox<String> fileTypeComboBox       = new JComboBox<>();
    private JCheckBox         caseCheckBox           = new JCheckBox("大小写匹配");
    private JPanel            searchProgressBarPanel = new JPanel(new MigLayout("", "[grow]", ""));

    // 右侧代码编辑器区域
    private JPanel   codeAreaPanel         = new JPanel();
    private TabPane  codeAreaTabPane       = new TabPane();
    private JToolBar codeAreaToolBar       = new JToolBar(JToolBar.HORIZONTAL);
    private JButton  codeAreaSaveButton    = new JButton(ControlUtils.getSVGIcon(SvgName.SAVE));
    private JButton  codeAreaOpenDirButton = new JButton(ControlUtils.getSVGIcon(SvgName.DIRECTORY));

    // 常量
    private static final String CODE_TYPE_OTHER     = "原文";
    private static final String CODE_TYPE_SMALI     = "Smali";
    private static final String CODE_TYPE_JADX_JAVA = "Jadx-Java";
    private static final String CODE_TYPE_CFR_JAVA  = "Cfr-Java";

    // CFR反编译组件调用命令
    private final String CFR_DECOMPILER_COMMAND = R.JAVA + " -jar %s %s%s/%s";

    public WorkbenchView(final Apk apk) {
        this.apk = apk;

        // 注意这里的调用顺序问题
        initInfoPanel();
        initManagerPanel();
        initSearchPanel();
        initCodeArea();

        // 选项卡添加至面板
        featureTabbedPane.setTabPlacement(JTabbedPane.LEFT);
        featureTabbedPane.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_ICON_PLACEMENT, SwingConstants.TOP);
        featureTabbedPane.addTab("信息", ControlUtils.getSVGIcon(SvgName.INFO, Size.BIG), infoPanel);
        featureTabbedPane.addTab("管理", ControlUtils.getSVGIcon(SvgName.MANAGER, Size.BIG), managerPanel);
        featureTabbedPane.addTab("搜索", ControlUtils.getSVGIcon(SvgName.SEARCH, Size.BIG), searchPanel);

        // 设置至根Panel
        setRightComponent(codeAreaPanel);
        setLeftComponent(new JScrollPane(featureTabbedPane));
        setMinimumSize(new Dimension(0, 0));

        ClickActionInstaller.bind(this);
    }

    /**
     * 初始化信息选项卡(infoPanel)
     */
    private void initInfoPanel() {
        // app基础信息
        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(new JLabel(String.format("名称: %s", apk.getApplicationName())));
        verticalBox.add(new JLabel(String.format("入口: %s", apk.getMainActivity())));
        verticalBox.add(new JLabel(String.format("包名: %s", apk.getPackageName())));
        verticalBox.add(new JLabel(String.format("MinSdk: %s | TargetSdk: %s", apk.getMinSdkVersion(), apk.getTargetSdkVersion())));

        // App图标和基础信息排列
        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(apk.getImageIconLabel(70));
        horizontalBox.add(Box.createHorizontalStrut(5));
        horizontalBox.add(verticalBox);
        horizontalBox.setBorder(new TitledBorder("Apk基础信息"));

        // 渲染组件树
        DefaultMutableTreeNode activityRootNode = new DefaultMutableTreeNode(ACTIVITY);
        DefaultMutableTreeNode serviceRootNode = new DefaultMutableTreeNode(SERVICE);
        DefaultMutableTreeNode receiveRootNode = new DefaultMutableTreeNode(RECEIVE);
        DefaultMutableTreeNode usesPermissionRootNode = new DefaultMutableTreeNode(USES_PERMISSION);
        // 从apk中读取组件数据
        for (String activityNode : apk.getActivity()) {
            activityRootNode.add(new DefaultMutableTreeNode(activityNode));
        }
        for (String serviceNode : apk.getService()) {
            serviceRootNode.add(new DefaultMutableTreeNode(serviceNode));
        }
        for (String receiverNode : apk.getReceiver()) {
            receiveRootNode.add(new DefaultMutableTreeNode(receiverNode));
        }
        for (String usesPermissionNode : apk.getUsesPermission()) {
            usesPermissionRootNode.add(new DefaultMutableTreeNode(usesPermissionNode));
        }
        // 组件节点添加至根节点
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(ROOT);
        rootNode.add(activityRootNode);
        rootNode.add(serviceRootNode);
        rootNode.add(receiveRootNode);
        rootNode.add(usesPermissionRootNode);
        // JTree组件样式
        appComponentTree.setRootVisible(false);
        appComponentTree.setModel(new DefaultTreeModel(rootNode));
        appComponentTree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) value;
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) currentNode.getParent();
                if (ROOT.equals(parentNode.getUserObject())) {
                    switch ((String) currentNode.getUserObject()) {
                        case ACTIVITY -> label.setIcon(LETTER_A_ICON);
                        case SERVICE -> label.setIcon(LETTER_S_ICON);
                        case RECEIVE -> label.setIcon(LETTER_R_ICON);
                        case USES_PERMISSION -> label.setIcon(LETTER_U_ICON);
                    }
                } else {
                    label.setIcon(LETTER_I_ICON);
                }
                return label;
            }
        });
        appComponentTree.setBorder(new MLineBorder(Color.decode("#bfbfbf"), 1, true, true, true, true));

        // 组件添加至信息面板
        infoPanel.setLayout(new BorderLayout(0, 5));
        infoPanel.add(horizontalBox, BorderLayout.NORTH);
        infoPanel.add(appComponentTree, BorderLayout.CENTER);
        infoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    /**
     * 初始化管理选项卡(managerPanel)
     */
    private void initManagerPanel() {
        SystemFileTree systemFileTree = new SystemFileTree(new File(apk.getBasePath()));
        systemFileTree.addTreeSelectionListener(e -> {
            SystemFileTree.SystemFileTreeNode selectNode = (SystemFileTree.SystemFileTreeNode) systemFileTree.getLastSelectedPathComponent();

            // 文件夹不进行代码编辑器调用
            if (selectNode.isDirectory()) return;

            // 添加一个需要进行显示处理的文件
            fileToCodeArea(selectNode.getFile());
        });
        managerPanel.setLayout(new GridLayout(1, 1));
        managerPanel.add(systemFileTree);
    }

    /**
     * 渲染搜索选项卡(searchPanel)
     */
    private void initSearchPanel() {
        fileTypeComboBox.setEditable(true);
        fileTypeComboBox.addItem("所有文件");
        fileTypeComboBox.addItem(".smali|.xml|.txt|.htm|.html");

        JPanel jPanel = new JPanel(new MigLayout("", "[]5[grow]", "[]5[]"));
        jPanel.setBorder(new TitledBorder("搜索条件"));
        jPanel.add(new JLabel("搜索内容: "));
        jPanel.add(searchContentTextField, "growx, wrap");
        jPanel.add(new JLabel("文件类型: "));
        jPanel.add(fileTypeComboBox, "growx, wrap");
        jPanel.add(new JLabel("附加功能: "));
        jPanel.add(caseCheckBox, "wrap");
        jPanel.add(searchButton, "span 2, growx");

        searchProgressBar = new JProgressBar();
        searchProgressBar.setMinimum(0);
        searchProgressBar.setMaximum(100);
        searchProgressBar.setStringPainted(true);
        searchProgressBarPanel.setBorder(new TitledBorder("搜索进度"));
        searchProgressBarPanel.add(searchProgressBar, "growx");

        searchPanel.setLayout(new BorderLayout());
        searchPanel.add(jPanel, BorderLayout.NORTH);
        searchPanel.add(searchProgressBarPanel, BorderLayout.CENTER);
        searchPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    /**
     * 渲染代码编辑器
     */
    private void initCodeArea() {
        // 按钮添加提示
        codeAreaSaveButton.setToolTipText("保存");
        codeAreaOpenDirButton.setToolTipText("打开所在目录");

        // 按钮添加至工具栏
        codeAreaToolBar.add(codeAreaSaveButton);
        codeAreaToolBar.add(codeAreaOpenDirButton);
        codeAreaToolBar.addSeparator();
        codeAreaToolBar.setFloatable(false);
        codeAreaToolBar.setBorder(new MLineBorder(1, true, false, false, true));
        codeAreaTabPane.setBorder(new MLineBorder());
        codeAreaTabPane.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSABLE, true);

        // 设置到根Panel
        codeAreaPanel.setLayout(new BorderLayout());
        codeAreaPanel.add(codeAreaToolBar, BorderLayout.NORTH);
        codeAreaPanel.add(codeAreaTabPane, BorderLayout.CENTER);

        // 事件绑定
        codeAreaTabPane.addChangeListener(e -> {
            Tab selectedTab = codeAreaTabPane.getSelectedTab();
            if (selectedTab == null) {
                return;
            }

            codeAreaSaveButton.setEnabled(true);
            codeAreaOpenDirButton.setEnabled(true);

            switch (FileUtil.getSuffix(selectedTab.getTitle())) {
                case Suffix.JAVA:
                    codeAreaSaveButton.setEnabled(false);
                    codeAreaOpenDirButton.setEnabled(false);
                    break;
                case Suffix.SMALI:
                    break;
            }
        });

        // 保存按钮和Ctrl+S保存快捷键实现
        codeAreaSaveButton.registerKeyboardAction(e -> saveSelectCodeAreaTabFileContent(), KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
        codeAreaSaveButton.addActionListener(e -> saveSelectCodeAreaTabFileContent());
    }

    /**
     * 添加一个文件至CodeArea中
     * TODO 目前未处理，相同文件名不同路径的情况。
     *
     * @param file 需要显示的文件
     */
    public void fileToCodeArea(File file) {
        // 用于切换不同代码类似
        JTabbedPane codeAreaShowTypeTabPane = new JTabbedPane();
        codeAreaShowTypeTabPane.setTabPlacement(JTabbedPane.BOTTOM);

        // 展示选中文件的tab页
        Tab tab;

        // Smali文件情况下提供Jadx和Cfr的反编译
        if (Suffix.SMALI.equals(FileUtil.getSuffix(file))) {
            // 创建Smali语法高亮的代码编辑器
            RTextScrollPane smaliEditor = createCodeEditorComponent(file, SyntaxConstantsWrapper.SYNTAX_STYLE_SMALI);
            RTextScrollPane jadxEditor = createCodeEditorComponent("");
            RTextScrollPane cfrEditor = createCodeEditorComponent("");

            // 设置不显示关闭按钮
            smaliEditor.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSABLE, false);
            jadxEditor.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSABLE, false);
            cfrEditor.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSABLE, false);

            // 先添加一个Smali的编辑器，其他2个，通过事件来动态添加。
            codeAreaShowTypeTabPane.addTab(CODE_TYPE_SMALI, smaliEditor);
            codeAreaShowTypeTabPane.addTab(CODE_TYPE_JADX_JAVA, jadxEditor);
            codeAreaShowTypeTabPane.addTab(CODE_TYPE_CFR_JAVA, cfrEditor);

            // 监听用户切换到Jadx-Java或Cfr-Java，进行smali反编译。
            codeAreaShowTypeTabPane.addChangeListener(new ChangeListener() {
                // 注意这里不要用lambda表达式，不然在下边的this中就获取不到匿名实例的this。
                @Override
                public void stateChanged(ChangeEvent e) {
                    // 获取2个引擎的CodeArea区域文本内容
                    String jadxJavaCodeAreaText = ((RTextScrollPane) codeAreaShowTypeTabPane.getComponentAt(1)).getTextArea().getText();
                    String cfrJavaCodeAreaText = ((RTextScrollPane) codeAreaShowTypeTabPane.getComponentAt(2)).getTextArea().getText();

                    // 当所有引擎都已经反编译完成后，就可以卸载监听事件了。
                    if (StrUtil.isNotEmpty(jadxJavaCodeAreaText) && StrUtil.isNotEmpty(cfrJavaCodeAreaText)) {
                        codeAreaShowTypeTabPane.removeChangeListener(this);
                        return;
                    }

                    final RTextScrollPane rTextScrollPane = (RTextScrollPane) codeAreaShowTypeTabPane.getSelectedComponent();
                    String text = rTextScrollPane.getTextArea().getText();

                    // 不等于空的情况代表已经反编译过代码了，那么另外的引擎可能还没反编译过。
                    if (StrUtil.isNotEmpty(text)) {
                        return;
                    }

                    // 根据当前选中的Tab类型来找到对应的反编译器进行反编译代码
                    String javaCode = "";
                    int selectedIndex = codeAreaShowTypeTabPane.getSelectedIndex();
                    String title = codeAreaShowTypeTabPane.getTitleAt(selectedIndex);
                    switch (title) {
                        case CODE_TYPE_JADX_JAVA -> {
                            // 创建Jadx反编译的Java语法高亮代码编辑器，Jadx下直接给Smali文件就能反编译。
                            javaCode = JadxUtils.decompileClass(file);
                            rTextScrollPane.getTextArea().setText(javaCode);
                        }
                        case CODE_TYPE_CFR_JAVA -> {
                            // 创建Cfr反编译的Java语法高亮代码编辑器，CFR需要找到对应的.class，然后再进行反编译。
                            String classFilePath = file.getAbsolutePath().replace((apk.getBasePath() + "/smali/"), "").replace(".smali", ".class");
                            javaCode = RuntimeUtil.execForStr(String.format(CFR_DECOMPILER_COMMAND, R.CFR, R.PROJECT_SRC_DIR, apk.getFileSimpleName(), classFilePath));
                        }
                    }
                    rTextScrollPane.getTextArea().setText(javaCode);
                }
            });
            tab = new Tab(file.getName(), codeAreaShowTypeTabPane, file);
        } else {
            // 无语法高亮的编辑器
            RTextScrollPane codeEditor = createCodeEditorComponent(file, SyntaxConstantsWrapper.SYNTAX_STYLE_NONE);
            codeEditor.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSABLE, true);
            tab = new Tab(file.getName(), codeEditor, file);
        }

        // 添加并设置选中
        codeAreaTabPane.addTab(tab);
        codeAreaTabPane.setSelectedComponent(tab.getComponent());
    }

    /**
     * 创建一个Java高亮语法的CodeArea编辑器
     *
     * @param file 当前代码编辑器对应的文件
     * @return 代码编辑器组件
     */
    public RTextScrollPane createCodeEditorComponent(File file) {
        return createCodeEditorComponent(FileUtil.readString(file, StandardCharsets.UTF_8), SyntaxConstantsWrapper.SYNTAX_STYLE_JAVA);
    }

    /**
     * 创建一个指定高亮语法的CodeArea编辑器
     *
     * @param file            当前代码编辑器对应的文件
     * @param syntaxConstants 需要高亮的语法{@link SyntaxConstantsWrapper}
     * @return 代码编辑器组件
     */
    public RTextScrollPane createCodeEditorComponent(File file, String syntaxConstants) {
        return createCodeEditorComponent(FileUtil.readString(file, StandardCharsets.UTF_8), syntaxConstants);
    }

    /**
     * 创建一个Java语法高亮的CodeAreaEditor并设置内容
     *
     * @param content 当前代码编辑器对应的文件
     * @return 代码编辑器组件
     */
    private RTextScrollPane createCodeEditorComponent(String content) {
        return createCodeEditorComponent(content, SyntaxConstantsWrapper.SYNTAX_STYLE_JAVA);
    }

    /**
     * 创建一个CodeAreaEditor并设置内容
     *
     * @param content 当前代码编辑器对应的文件
     * @return 代码编辑器组件
     */
    private RTextScrollPane createCodeEditorComponent(String content, String syntaxConstants) {
        RSyntaxTextArea rSyntaxTextArea = new RSyntaxTextArea(20, 60);
        rSyntaxTextArea.setText(content);
        rSyntaxTextArea.setCodeFoldingEnabled(true);
        rSyntaxTextArea.setSyntaxEditingStyle(syntaxConstants);
        RTextScrollPane rTextScrollPane = new RTextScrollPane(rSyntaxTextArea);
        rTextScrollPane.getVerticalScrollBar().setValue(0);
        return rTextScrollPane;
    }

    /**
     * 保存当前选中的Tab页中代码编辑的的内容到磁盘中
     */
    private void saveSelectCodeAreaTabFileContent() {
        Tab selectedTab = codeAreaTabPane.getSelectedTab();

        // 获取到当前选中的Tab中的代码编辑器
        RTextArea textArea = ((RTextScrollPane) selectedTab.getComponent()).getTextArea();

        // 将代码编辑器中的内容写入硬盘文件中
        File file = (File) selectedTab.getData();
        FileUtil.writeString(textArea.getText(), file, StandardCharsets.UTF_8);
        StaticLog.info("保存成功");
    }

    /**
     * 搜索文件内容
     *
     * @param file       需要搜索的文件
     * @param str        搜索的关键字
     * @param fileFilter 过滤的文件
     * @return 返回高冷的组件
     */
    private FileContentHighlightNode searchFileContent(File file, String str, FileFilter fileFilter) {
        double progressBar = 0;
        FileContentHighlightNode rootNode = new FileContentHighlightNode(apk.getFileName(), file, 0);

        List<File> files = FileUtil.loopFiles(file, fileFilter);
        double fileSize = files.size();
        try {
            // 遍历每个过滤后的文件，进行搜索。
            for (File f : files) {
                searchProgressBar.setValue(NumberUtil.round(progressBar++ / fileSize * 100.0, 0).intValue());
                Scanner scanner = new Scanner(f, StandardCharsets.UTF_8);
                int lineNumber = 0;
                FileContentHighlightNode currentFileNode = new FileContentHighlightNode(f.getName(), f, 0);
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    lineNumber++;
                    // 是否匹配大小写
                    if (caseCheckBox.isSelected()) {
                        if (line.contains(str)) {
                            currentFileNode.add(new FileContentHighlightNode(line.trim(), f, lineNumber, true));
                        }
                    } else if (StrUtil.containsIgnoreCase(line, str)) {
                        currentFileNode.add(new FileContentHighlightNode(line.trim(), f, lineNumber, true));
                    }
                }
                if (currentFileNode.getChildCount() > 0) {
                    rootNode.add(currentFileNode);
                }
            }
        } catch (Exception e) {
            StaticLog.error(e);
        }
        return rootNode;
    }

    @ClickAction("searchButton")
    private void searchButtonClick(ActionEvent e) {
        String searchContent = searchContentTextField.getText();
        if (StrUtil.isEmpty(searchContent)) {
            ControlUtils.showMsgDialog("提示信息", "搜索内容不能为空!");
            return;
        }

        // 临时禁用按钮操作
        fileTypeComboBox.setEnabled(false);
        caseCheckBox.setEnabled(false);
        searchButton.setEnabled(false);

        // 过滤要搜索的文件
        FileFilter fileFilter;
        String fileType = (String) fileTypeComboBox.getSelectedItem();
        if ("所有文件".equals(fileType)) {
            fileFilter = pathname -> true;
        } else {
            List<String> types = StrUtil.split(fileType, "|");
            fileFilter = types::contains;
        }

        ThreadUtil.execAsync(() -> {
            // 搜索文件
            FileContentHighlightNode root = searchFileContent(new File(apk.getBasePath()), searchContent, fileFilter);
            // 显示在结果控制台上
            MainWindow.consoleView.setSearchResultRoot(root, searchContent, this);
        });

        // 恢复按钮操作
        fileTypeComboBox.setEnabled(true);
        caseCheckBox.setEnabled(true);
        searchButton.setEnabled(true);
    }

    @ClickAction("codeAreaOpenDirButton")
    private void codeAreaOpenDirButtonClick(ActionEvent e) {
        File file = (File) codeAreaTabPane.getSelectedTab().getData();
        try {
            Desktop.getDesktop().open(file.getParentFile());
        } catch (Exception exception) {
            StaticLog.error(exception);
            ControlUtils.showMsgDialog("提示信息", "无法打开");
        }
    }
}