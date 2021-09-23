package com.funnysec.richardtang.androidkiller4j.view.device;

import com.android.ddmlib.Log;
import com.android.ddmlib.logcat.LogCatMessage;
import com.funnysec.richardtang.androidkiller4j.constant.Icon;
import com.funnysec.richardtang.androidkiller4j.event.device.DeviceLogViewEvent;
import com.funnysec.richardtang.androidkiller4j.view.IocView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Data;
import org.nutz.ioc.loader.annotation.Inject;

/**
 * 设备日志视图
 *
 * @author RichardTang
 */
@Data
public class DeviceLogView extends IocView<Tab> {

    // 过滤条件组件
    private Label                   pidLabel;
    private Label                   tagLabel;
    private Label                   messageLabel;
    private Label                   levelLabel;
    private TextField               pidTextField;
    private TextField               tagTextField;
    private TextField               messageTextField;
    private ChoiceBox<Log.LogLevel> logLevelChoiceBox;
    private Button                  switchButton;
    private Label                   splitLine;
    private HBox                    conditionHBox;

    // 数据表格，承载LogCat日志信息的表格。
    private TableView<LogCatMessage>           tableView;
    private TableColumn<LogCatMessage, String> nameTableColumn;
    private TableColumn<LogCatMessage, String> messageTableColumn;
    private TableColumn<LogCatMessage, String> levelTableColumn;
    private TableColumn<LogCatMessage, String> headerTableColumn;
    private TableColumn<LogCatMessage, String> pidTableColumn;
    private TableColumn<LogCatMessage, String> tagTableColumn;
    private TableColumn<LogCatMessage, String> tidTableColumn;
    private TableColumn<LogCatMessage, String> timestampTableColumn;

    // 组合布局
    private VBox vBox;

    FilteredList<LogCatMessage>   filteredData;
    ObservableList<LogCatMessage> tableViewItem;

    @Inject
    private DeviceLogViewEvent deviceLogViewEvent;

    @Override
    protected void initUi() {
        pidLabel          = new Label("PID");
        tagLabel          = new Label("标签");
        messageLabel      = new Label("消息");
        levelLabel        = new Label("级别");
        pidTextField      = new TextField();
        tagTextField      = new TextField();
        messageTextField  = new TextField();
        logLevelChoiceBox = new ChoiceBox<>();
        splitLine         = new Label("  ");
        switchButton      = new Button("开始", Icon.DEVICE_LOG_VIEW_FILTER_START);
        conditionHBox     = new HBox();

        tableView            = new TableView<>();
        nameTableColumn      = new TableColumn<>("应用名称");
        messageTableColumn   = new TableColumn<>("消息");
        levelTableColumn     = new TableColumn<>("级别");
        headerTableColumn    = new TableColumn<>("头信息");
        pidTableColumn       = new TableColumn<>("PID");
        tagTableColumn       = new TableColumn<>("标签");
        tidTableColumn       = new TableColumn<>("TID");
        timestampTableColumn = new TableColumn<>("时间");

        tableViewItem = FXCollections.observableArrayList();
        filteredData  = new FilteredList<>(tableViewItem, s -> true);

        // 配合进行布局使用
        vBox = new VBox();
    }

    @Override
    protected void initAttr() {
        conditionHBox.setPadding(new Insets(2, 2, 3, 2));
        conditionHBox.setAlignment(Pos.CENTER_LEFT);

        pidLabel.setPadding(new Insets(5));
        tagLabel.setPadding(new Insets(5));
        messageLabel.setPadding(new Insets(5));
        levelLabel.setPadding(new Insets(5));
        logLevelChoiceBox.getItems().addAll(
                Log.LogLevel.DEBUG, Log.LogLevel.ASSERT, Log.LogLevel.INFO,
                Log.LogLevel.ERROR, Log.LogLevel.VERBOSE, Log.LogLevel.WARN
        );
        switchButton.setPadding(new Insets(4));

        getRootPane().setText("日志");
        getRootPane().setGraphic(Icon.DEVICE_LOG_VIEW_TAB);
        getRootPane().setUserData("日志");
        getRootPane().setClosable(true);

        bindCellValueFactory(nameTableColumn, "appName");
        bindCellValueFactory(messageTableColumn, "message");
        bindCellValueFactory(levelTableColumn, "logLevel");
        bindCellValueFactory(headerTableColumn, "header");
        bindCellValueFactory(pidTableColumn, "pid");
        bindCellValueFactory(tagTableColumn, "tag");
        bindCellValueFactory(tidTableColumn, "tid");
        bindCellValueFactory(timestampTableColumn, "timestamp");

        tableView.setItems(filteredData);
        tableView.prefHeightProperty().bind(vBox.heightProperty());
    }

    @Override
    protected void initLayout() {
        conditionHBox.getChildren().addAll(
                pidLabel, pidTextField,
                tagLabel, tagTextField,
                messageLabel, messageTextField,
                levelLabel, logLevelChoiceBox,
                splitLine, switchButton
        );

        tableView.getColumns().addAll(
                nameTableColumn, messageTableColumn, levelTableColumn, headerTableColumn,
                pidTableColumn, tagTableColumn, tidTableColumn, timestampTableColumn
        );

        vBox.getChildren().addAll(conditionHBox, tableView);
        getRootPane().setContent(vBox);
    }

    @Override
    protected void initEvent() {
        switchButton.setOnMouseClicked(e -> deviceLogViewEvent.switchButtonOnMouseClick(e));

        pidTextField.textProperty().addListener(obs -> deviceLogViewEvent.conditionSearch());
        tagTextField.textProperty().addListener(obs -> deviceLogViewEvent.conditionSearch());
        messageTextField.textProperty().addListener(obs -> deviceLogViewEvent.conditionSearch());
        logLevelChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                obs -> deviceLogViewEvent.conditionSearch()
        );
    }

    @Override
    protected void initialize() {

    }

    /**
     * 简化 tableColumn.setCellValueFactory(new PropertyValueFactory<>("xxx")); 的写法
     *
     * @param tableColumn 数据表格对应的TableColumn对象
     * @param bindString  {@link LogCatMessage}中对应的属性名称
     */
    private void bindCellValueFactory(TableColumn<LogCatMessage, String> tableColumn, String bindString) {
        tableColumn.setCellValueFactory(new PropertyValueFactory<>(bindString));
    }

    /**
     * 获取日志级别选项卡中选中的Item项
     * 这里不要直接使用getSelectedItem()，因为这个获取数据是获取的oldValue。
     *
     * @return 选中的LogLevel对象
     */
    public Log.LogLevel getLogLevelChoiceBoxSelectItem() {
        int index = logLevelChoiceBox.getSelectionModel().getSelectedIndex();
        return logLevelChoiceBox.getItems().get(index);
    }
}