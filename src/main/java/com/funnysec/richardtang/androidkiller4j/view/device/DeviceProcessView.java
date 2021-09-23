package com.funnysec.richardtang.androidkiller4j.view.device;

import com.funnysec.richardtang.androidkiller4j.constant.Icon;
import com.funnysec.richardtang.androidkiller4j.core.device.process.ProcessMessage;
import com.funnysec.richardtang.androidkiller4j.event.device.DeviceProcessViewEvent;
import com.funnysec.richardtang.androidkiller4j.view.IocView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Data;
import org.nutz.ioc.loader.annotation.Inject;

@Data
public class DeviceProcessView extends IocView<Tab> {

    private Button changeButton;

    private TableView<ProcessMessage>           tableView;
    private TableColumn<ProcessMessage, String> pid;
    private TableColumn<ProcessMessage, String> user;
    private TableColumn<ProcessMessage, String> pr;
    private TableColumn<ProcessMessage, String> ni;
    private TableColumn<ProcessMessage, String> virt;
    private TableColumn<ProcessMessage, String> res;
    private TableColumn<ProcessMessage, String> shr;
    private TableColumn<ProcessMessage, String> s;
    private TableColumn<ProcessMessage, String> cpu;
    private TableColumn<ProcessMessage, String> mem;
    private TableColumn<ProcessMessage, String> time;
    private TableColumn<ProcessMessage, String> args;

    // 组合布局
    private VBox vBox;
    private HBox hBox;

    @Inject
    private DeviceProcessViewEvent deviceProcessViewEvent;

    @Override
    protected void initUi() {
        tableView = new TableView<>();
        pid       = new TableColumn<>("PID");
        user      = new TableColumn<>("用户");
        pr        = new TableColumn<>("PR");
        ni        = new TableColumn<>("NI");
        virt      = new TableColumn<>("VIRT");
        res       = new TableColumn<>("结果");
        shr       = new TableColumn<>("SHR");
        s         = new TableColumn<>("S");
        cpu       = new TableColumn<>("CPU使用率");
        mem       = new TableColumn<>("内存占用");
        time      = new TableColumn<>("时间");
        args      = new TableColumn<>("参数值");

        changeButton = new Button("开始", Icon.DEVICE_PROCESS_VIEW_STOP);

        // 配合进行布局使用
        vBox = new VBox();
        hBox = new HBox();
    }

    @Override
    protected void initAttr() {
        getRootPane().setText("进程");
        getRootPane().setGraphic(Icon.DEVICE_PROCESS_VIEW_TAB);

        bindCellValueFactory(pid, "pid");
        bindCellValueFactory(user, "user");
        bindCellValueFactory(pr, "pr");
        bindCellValueFactory(ni, "ni");
        bindCellValueFactory(virt, "virt");
        bindCellValueFactory(shr, "shr");
        bindCellValueFactory(s, "s");
        bindCellValueFactory(cpu, "cpu");
        bindCellValueFactory(mem, "mem");
        bindCellValueFactory(time, "time");
        bindCellValueFactory(args, "args");

        getRootPane().setUserData("进程");
        getRootPane().setClosable(true);

        tableView.prefHeightProperty().bind(vBox.heightProperty());

        hBox.setPadding(new Insets(2, 2, 3, 2));
        hBox.setAlignment(Pos.CENTER_LEFT);

        changeButton.setPadding(new Insets(4));
    }

    @Override
    protected void initLayout() {
        tableView.getColumns().addAll(
                pid, user, pr, ni, virt, res,
                shr, s, cpu, mem, time, args
        );

        hBox.getChildren().add(changeButton);
        vBox.getChildren().addAll(hBox, tableView);

        getRootPane().setContent(vBox);
    }

    @Override
    protected void initEvent() {
        changeButton.setOnMouseClicked(e -> deviceProcessViewEvent.changeButtonOnMouseClick(e));
    }

    @Override
    protected void initialize() {

    }

    /**
     * 简化 tableColumn.setCellValueFactory(new PropertyValueFactory<>("xxx")); 的写法
     *
     * @param tableColumn 数据表格对应的TableColumn对象
     * @param bindString  {@link ProcessMessage}中对应的属性名称
     */
    private void bindCellValueFactory(TableColumn<ProcessMessage, String> tableColumn, String bindString) {
        tableColumn.setCellValueFactory(new PropertyValueFactory<>(bindString));
    }
}
