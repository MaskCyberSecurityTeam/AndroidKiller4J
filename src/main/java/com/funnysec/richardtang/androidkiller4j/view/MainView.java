package com.funnysec.richardtang.androidkiller4j.view;

import com.funnysec.richardtang.androidkiller4j.constant.Icon;
import com.funnysec.richardtang.androidkiller4j.event.MainViewEvent;
import com.funnysec.richardtang.androidkiller4j.ui.GadgetSplitPane;
import com.funnysec.richardtang.androidkiller4j.ui.ImageButton;
import javafx.geometry.Orientation;
import javafx.scene.layout.BorderPane;
import lombok.Data;
import org.nutz.ioc.loader.annotation.Inject;

@Data
public class MainView extends IocView<GadgetSplitPane> {

    @Inject
    private TaskView taskView;

    @Inject
    private ToolkitView toolkitView;

    @Inject
    private ControlView controlView;

    @Inject
    private MainViewEvent mainViewEvent;

    // SplitPane的上部部分
    private BorderPane borderPane;

    // 扩展GadgetSplitPane用，清空内容按钮。
    private ImageButton clearLogImageButton;

    @Override
    protected void initUi() {
        borderPane          = new BorderPane();
        clearLogImageButton = new ImageButton(Icon.MAIN_VIEW_LOG_CELAR, 18, 18);
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