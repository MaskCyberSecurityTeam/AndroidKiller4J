package com.funnysec.richardtang.androidkiller4j.view.preference;

import com.funnysec.richardtang.androidkiller4j.constant.Icon;
import com.funnysec.richardtang.androidkiller4j.event.PreferenceViewEvent;
import com.funnysec.richardtang.androidkiller4j.view.IocView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import lombok.Data;
import org.nutz.ioc.loader.annotation.Inject;

@Data
public class PreferenceView extends IocView<Tab> {

    private SplitPane        splitPane;
    private TreeView<String> optionTreeView;

    private StackPane showStackPane;

    private TreeItem<String> rootTreeItem;
    private TreeItem<String> appConfigTreeItem;

    @Inject
    private PreferenceViewEvent preferenceViewEvent;

    @Override
    protected void initUi() {
        rootTreeItem      = new TreeItem<>("Root");
        appConfigTreeItem = new TreeItem<>("应用配置", Icon.SETTING_VIEW_APP_CONFIG);

        splitPane      = new SplitPane();
        optionTreeView = new TreeView<>();
        showStackPane  = new StackPane();
    }

    @Override
    protected void initAttr() {
        optionTreeView.setShowRoot(false);
        splitPane.setDividerPositions(0.2f, 0.8f);

        getRootPane().setText("系统配置");
        getRootPane().setGraphic(Icon.SETTING_VIEW_TAB);
    }

    @Override
    protected void initLayout() {
        optionTreeView.setRoot(rootTreeItem);
        rootTreeItem.getChildren().add(appConfigTreeItem);

        splitPane.getItems().addAll(optionTreeView, showStackPane);
        getRootPane().setContent(splitPane);
    }

    @Override
    protected void initEvent() {
        optionTreeView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldValue, newValue) -> preferenceViewEvent.optionTreeViewSelectItem(obs, oldValue, newValue)
        );
    }

    @Override
    protected void initialize() {

    }
}
