package com.richardtang.androidkiller4j.view;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.richardtang.androidkiller4j.constant.SvgName;
import com.richardtang.androidkiller4j.ui.action.ClickActionInstaller;
import com.richardtang.androidkiller4j.ui.control.PrintStreamTextArea;
import com.richardtang.androidkiller4j.ui.tabframe.TabFrameItem;
import com.richardtang.androidkiller4j.ui.tabframe.TabFramePanel;
import com.richardtang.androidkiller4j.ui.tabpane.Tab;
import com.richardtang.androidkiller4j.ui.tabpane.TabPane;
import com.richardtang.androidkiller4j.ui.tree.FileContentHighlightNode;
import com.richardtang.androidkiller4j.ui.tree.FileContentHighlightTree;
import com.richardtang.androidkiller4j.util.ControlUtils;
import com.richardtang.androidkiller4j.util.RSyntaxTextAreaUtils;
import lombok.Data;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;

/**
 * 控制台视图，也就是程序打开后最下边的选项卡。
 * 带有日志输出的文本域那一处
 *
 * @author RichardTang
 */
@Data
public final class ConsoleView extends TabFramePanel {

    private final FlatSVGIcon MSG_SVG_ICON    = ControlUtils.getSVGIcon(SvgName.MESSAGE);
    private final FlatSVGIcon SEARCH_SVG_ICON = ControlUtils.getSVGIcon(SvgName.RESULT);

    // 日志组件
    private final PrintStreamTextArea printStreamTextArea = new PrintStreamTextArea();
    private final JToggleButton       logToggleButton     = new JToggleButton("消息", MSG_SVG_ICON);
    private final TabFrameItem        logTabFrameItem     = new TabFrameItem(logToggleButton, new JScrollPane(printStreamTextArea));

    // 搜索结果
    private final JScrollPane   searchResultSrollPanel   = new JScrollPane();
    private final JToggleButton searchResultToggleButton = new JToggleButton("搜索结果", SEARCH_SVG_ICON);
    private final TabFrameItem  searchResultTabFrameItem = new TabFrameItem(searchResultToggleButton, searchResultSrollPanel);

    public ConsoleView() {
         System.setOut(printStreamTextArea.getPrintStream());
         System.setErr(printStreamTextArea.getPrintStream());

        // 添加2个默认的Item，消息和搜索结果！
        addTabFrameItem(logTabFrameItem);
        addTabFrameItem(searchResultTabFrameItem);

        // 绑定事件
        ClickActionInstaller.bind(this);
    }

    public void setSearchResultRoot(FileContentHighlightNode root, String keyword, WorkbenchView workbenchView) {
        FileContentHighlightTree tree = new FileContentHighlightTree(root, keyword);
        tree.addTreeSelectionListener(e -> {
            FileContentHighlightNode clickedNode = (FileContentHighlightNode) tree.getLastSelectedPathComponent();
            if (clickedNode == null) {
                return;
            }

            // 找到TaskView中对应的工作台
            TabPane tabPane = workbenchView.getCodeAreaTabPane();
            Tab currentNodeTab = tabPane.getTabByTitle(clickedNode.getFile().getName());

            // 当前点击的节点文件没有打开
            if (currentNodeTab == null) {
                // 创建新的代码编辑区域
                currentNodeTab = new Tab(clickedNode.getFile().getName(), workbenchView.createCodeEditorComponent(clickedNode.getFile()), clickedNode.getFile());
                tabPane.addTab(currentNodeTab);
            }
            tabPane.setSelectedComponent(currentNodeTab.getComponent());
            RTextArea rTextArea = ((RTextScrollPane) tabPane.getSelectedTab().getComponent()).getTextArea();
            RSyntaxTextAreaUtils.focusPosition(rTextArea, 0);

            if (clickedNode.isLineNode()) {
                RSyntaxTextAreaUtils.focusLineStart(rTextArea, clickedNode.getLineNumber());
                RSyntaxTextAreaUtils.findKeyword(rTextArea, keyword);
            }
        });
        searchResultSrollPanel.getViewport().add(tree);
    }
}