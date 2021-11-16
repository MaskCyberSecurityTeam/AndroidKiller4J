package com.richardtang.androidkiller4j.view.workbench;

import cn.hutool.core.io.FileUtil;
import com.formdev.flatlaf.FlatClientProperties;
import com.richardtang.androidkiller4j.bean.Apk;
import com.richardtang.androidkiller4j.ui.tabframe.MLineBorder;
import com.richardtang.androidkiller4j.ui.tree.DirectoryTree;
import com.richardtang.androidkiller4j.ui.tree.DirectoryTreeFileNode;
import com.richardtang.androidkiller4j.util.ControlUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 工作台视图，一个APK对应一个工作台。
 *
 * @author RichardTang
 */
public class WorkbenchView extends JSplitPane {

    // 当前工作台对应要处理的apk
    private Apk apk;

    // 左边的功能选项卡
    private JTabbedPane fnTabbedPane;
    private JPanel      infoPanel;
    private JPanel      managerPanel;
    private JPanel      searchPanel = new JPanel();

    // infoJPanel
    private Box          infoPanelAppInfoVBox;
    private Box          infoPanelIconInfoHBox;
    private BorderLayout infoPanelBorderLayout;
    private TitledBorder infoPanelTitleBorder;

    private static final String APP_NAME_LABEL = "名称: %s";
    private static final String APP_MAIN_LABEL = "入口: %s";
    private static final String APP_PACK_LABEL = "包名: %s";
    private static final String APP_SDK_LABEL  = "MinSdk: %s | TargetSdk: %s";

    private JTree componentJTree;

    private static final Icon LETTER_A_ICON = ControlUtil.getSVGIcon("letter-a.svg", 15, 15);
    private static final Icon LETTER_U_ICON = ControlUtil.getSVGIcon("letter-u.svg", 15, 15);
    private static final Icon LETTER_R_ICON = ControlUtil.getSVGIcon("letter-r.svg", 15, 15);
    private static final Icon LETTER_S_ICON = ControlUtil.getSVGIcon("letter-s.svg", 15, 15);
    private static final Icon LETTER_I_ICON = ControlUtil.getSVGIcon("letter-i.svg", 15, 15);

    // managerJPanel
    private DirectoryTree managerPanelDirectoryTree;

    // 右侧代码编辑器
    private WorkbenchCodeTextAreaView workbenchCodeTextAreaView;

    public WorkbenchView(Apk apk) {
        this.apk = apk;
        render();
    }

    public void render() {
        // 注意这里的调用顺序问题
        renderInfoJPanel();
        renderManagerJPanel();
        renderCodeArea();
        renderFnJTabbedPane();
        initEvent();

        setRightComponent(workbenchCodeTextAreaView);
        setMinimumSize(new Dimension(0, 0));
        setLeftComponent(new JScrollPane(fnTabbedPane));
    }

    /**
     * 渲染最外层的选项卡
     */
    private void renderFnJTabbedPane() {
        fnTabbedPane = new JTabbedPane();
        fnTabbedPane.setTabPlacement(JTabbedPane.LEFT);
        fnTabbedPane.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_ICON_PLACEMENT, SwingConstants.TOP);
        fnTabbedPane.addTab("信息", ControlUtil.getSVGIcon("info.svg", 20, 20), infoPanel);
        fnTabbedPane.addTab("管理", ControlUtil.getSVGIcon("manager.svg", 20, 20), managerPanel);
        fnTabbedPane.addTab("搜索", ControlUtil.getSVGIcon("search.svg", 20, 20), searchPanel);
    }

    /**
     * 渲染信息选项卡
     */
    private void renderInfoJPanel() {
        // 资料卡中的标题
        infoPanelTitleBorder = new TitledBorder("Apk基础信息");

        // app信息列
        infoPanelAppInfoVBox = Box.createVerticalBox();
        infoPanelAppInfoVBox.add(new JLabel(String.format(APP_NAME_LABEL, apk.getApplicationName())));
        infoPanelAppInfoVBox.add(new JLabel(String.format(APP_MAIN_LABEL, apk.getMainActivity())));
        infoPanelAppInfoVBox.add(new JLabel(String.format(APP_PACK_LABEL, apk.getPackageName())));
        infoPanelAppInfoVBox.add(new JLabel(String.format(APP_SDK_LABEL, apk.getMinSdkVersion(), apk.getTargetSdkVersion())));

        // 图标和app信息
        infoPanelIconInfoHBox = Box.createHorizontalBox();
        infoPanelIconInfoHBox.setBorder(infoPanelTitleBorder);
        infoPanelIconInfoHBox.add(new JLabel(apk.getImageIcon(70, 70)));
        infoPanelIconInfoHBox.add(Box.createHorizontalStrut(5));
        infoPanelIconInfoHBox.add(infoPanelAppInfoVBox);

        // 渲染组件树
        renderComponentJTree();

        // 上下布局
        infoPanelBorderLayout = new BorderLayout();
        infoPanelBorderLayout.setVgap(5);

        // 根面板
        infoPanel = new JPanel();
        infoPanel.setLayout(infoPanelBorderLayout);
        infoPanel.add(infoPanelIconInfoHBox, BorderLayout.NORTH);
        infoPanel.add(componentJTree, BorderLayout.CENTER);
        infoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    private void renderComponentJTree() {
        componentJTree = new JTree();
        componentJTree.setBorder(new MLineBorder(Color.decode("#bfbfbf"), 1, true, true, true, true));

        DefaultMutableTreeNode rootNode               = new DefaultMutableTreeNode("Root");
        DefaultMutableTreeNode activityRootNode       = new DefaultMutableTreeNode("Activity");
        DefaultMutableTreeNode serviceRootNode        = new DefaultMutableTreeNode("Service");
        DefaultMutableTreeNode receiveRootNode        = new DefaultMutableTreeNode("Receive");
        DefaultMutableTreeNode usesPermissionRootNode = new DefaultMutableTreeNode("UsesPermission");

        for (String activityChildNode : apk.getActivity()) {
            activityRootNode.add(new DefaultMutableTreeNode(activityChildNode));
        }

        for (String serviceChildNode : apk.getService()) {
            serviceRootNode.add(new DefaultMutableTreeNode(serviceChildNode));
        }

        for (String receiverChildNode : apk.getReceiver()) {
            receiveRootNode.add(new DefaultMutableTreeNode(receiverChildNode));
        }

        for (String usesPermissionChildNode : apk.getUsesPermission()) {
            usesPermissionRootNode.add(new DefaultMutableTreeNode(usesPermissionChildNode));
        }

        rootNode.add(activityRootNode);
        rootNode.add(serviceRootNode);
        rootNode.add(receiveRootNode);
        rootNode.add(usesPermissionRootNode);

        componentJTree.setRootVisible(false);
        componentJTree.setModel(new DefaultTreeModel(rootNode));
        componentJTree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree,
                                                          Object value,
                                                          boolean sel,
                                                          boolean expanded,
                                                          boolean leaf,
                                                          int row,
                                                          boolean hasFocus) {

                JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

                DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) value;
                DefaultMutableTreeNode parentNode  = (DefaultMutableTreeNode) currentNode.getParent();

                if ("Root".equals(parentNode.getUserObject())) {
                    switch ((String) currentNode.getUserObject()) {
                        case "Activity":
                            label.setIcon(LETTER_A_ICON);
                            break;
                        case "Service":
                            label.setIcon(LETTER_S_ICON);
                            break;
                        case "Receive":
                            label.setIcon(LETTER_R_ICON);
                            break;
                        case "UsesPermission":
                            label.setIcon(LETTER_U_ICON);
                            break;
                    }
                } else {
                    label.setIcon(LETTER_I_ICON);
                }
                return label;
            }
        });
    }

    /**
     * 渲染管理选项卡
     */
    private void renderManagerJPanel() {
        managerPanel              = new JPanel();
        managerPanelDirectoryTree = new DirectoryTree(new File(apk.getBasePath()));

        managerPanel.setLayout(new GridLayout(1, 1));
        managerPanel.add(managerPanelDirectoryTree);
    }

    /**
     * 渲染代码编辑器
     */
    private void renderCodeArea() {
        workbenchCodeTextAreaView = new WorkbenchCodeTextAreaView(apk);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        managerPanelDirectoryTree.addTreeSelectionListener(e -> {
            DirectoryTreeFileNode selectNode = (DirectoryTreeFileNode) managerPanelDirectoryTree.getLastSelectedPathComponent();

            // 文件夹不进行代码编辑器调用
            if (selectNode.isDirectory()) return;

            // 添加一个需要进行显示处理的文件
            workbenchCodeTextAreaView.addFile(selectNode.getFile());
        });
    }
}