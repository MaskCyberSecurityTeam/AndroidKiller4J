package com.funnysec.richardtang.androidkiller4j.view;

import com.funnysec.richardtang.androidkiller4j.config.ResourcePathConfig;
import com.funnysec.richardtang.androidkiller4j.constant.ProtocolString;
import com.funnysec.richardtang.androidkiller4j.event.MainViewEvent;
import com.funnysec.richardtang.androidkiller4j.ui.GadgetSplitPane;
import com.funnysec.richardtang.androidkiller4j.ui.ImageButton;
import javafx.geometry.Orientation;
import javafx.scene.layout.BorderPane;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * AndroidKiller4J的主窗口视图，布局为SplitPane + BoardPane。
 * SplitPane将页面切分成上线两部分，上部分承载BoardPane，下部分用来单独一个控制台View，因为SplitPane可以上下拖动所以选择它。
 * BoardPane只使用Top和Center部分，并且确保AccordionTabPane的收缩不会被盖住。
 * <p>
 * 该类主要整合其他View的布局
 *
 * @author RichardTang
 */
@Data
@Component
public class MainView extends BaseView2<GadgetSplitPane> {

    @Autowired
    private TaskView taskView;

    @Autowired
    private ToolkitView toolkitView;

    @Autowired
    private ControlView controlView;

    @Autowired
    private MainViewEvent mainViewEvent;

    // SplitPane的上部部分
    private BorderPane borderPane;

    // 扩展GadgetSplitPane用，清空内容按钮。
    private ImageButton clearLogImageButton;

    // ClearLogImageButton图标
    private static final String CLEAR_LOG_BTN_ICON = ProtocolString.FILE + ResourcePathConfig.IMAGE + "/clear.png";

    @Override
    protected void initUi() {
        borderPane          = new BorderPane();
        clearLogImageButton = new ImageButton(CLEAR_LOG_BTN_ICON, 18, 18);
    }

    @Override
    protected void initAttr() {
        getRootPane().setOrientation(Orientation.VERTICAL);
    }

    @Override
    protected void initLayout() {
        borderPane.setTop(toolkitView.getRootPane());
        borderPane.setCenter(taskView.getRootPane());

        getRootPane().setTopContent(borderPane);
        getRootPane().setBottomContent(controlView.getRootPane());
        getRootPane().setGadgetButton(clearLogImageButton);
    }

    @Override
    protected void initEvent() {
        clearLogImageButton.setOnMouseClicked(e -> mainViewEvent.clearLogImageButtonOnMouseClick(e));
    }

    @Override
    protected void initialize() {

    }
}