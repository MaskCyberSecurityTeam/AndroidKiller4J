package com.funnysec.richardtang.androidkiller4j.view.device.log;

import com.android.ddmlib.logcat.LogCatMessage;
import com.funnysec.richardtang.androidkiller4j.constant.Icon;
import com.funnysec.richardtang.androidkiller4j.event.device.log.DeviceLogViewEvent;
import com.funnysec.richardtang.androidkiller4j.view.BaseView;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 设备日志视图
 *
 * @author RichardTang
 */
@Data
@Component
public class DeviceLogView extends BaseView<Tab> {

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

    @Autowired
    private DeviceLogFilterView deviceLogFilterView;

    @Autowired
    private DeviceLogViewEvent deviceLogViewEvent;

    @Override
    protected void initUi() {
        tableView            = new TableView<>();
        nameTableColumn      = new TableColumn<>("应用名称");
        messageTableColumn   = new TableColumn<>("消息");
        levelTableColumn     = new TableColumn<>("级别");
        headerTableColumn    = new TableColumn<>("头信息");
        pidTableColumn       = new TableColumn<>("PID");
        tagTableColumn       = new TableColumn<>("标签");
        tidTableColumn       = new TableColumn<>("TID");
        timestampTableColumn = new TableColumn<>("时间");

        // 配合进行布局使用
        vBox = new VBox();
    }

    @Override
    protected void initAttr() {
        getRootPane().setText("日志");
        getRootPane().setGraphic(Icon.DEVICE_LOG_TAB);
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

        tableView.prefHeightProperty().bind(vBox.heightProperty());
    }

    @Override
    protected void initLayout() {
        tableView.getColumns().addAll(
                nameTableColumn, messageTableColumn, levelTableColumn, headerTableColumn,
                pidTableColumn, tagTableColumn, tidTableColumn, timestampTableColumn
        );

        vBox.getChildren().addAll(deviceLogFilterView.getRootPane(), tableView);
        getRootPane().setContent(vBox);
    }

    @Override
    protected void initEvent() {

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
}