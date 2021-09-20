package com.funnysec.richardtang.androidkiller4j.view.device.log;

import com.android.ddmlib.Log;
import com.funnysec.richardtang.androidkiller4j.constant.Icon;
import com.funnysec.richardtang.androidkiller4j.event.device.log.DeviceLogFilterViewEvent;
import com.funnysec.richardtang.androidkiller4j.view.BaseView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 设备日志条件过滤组件视图
 *
 * @author RichardTang
 */
@Data
@Component
public class DeviceLogFilterView extends BaseView<HBox> {

    private Label                   pidLabel;
    private Label                   tagLabel;
    private Label                   messageLabel;
    private Label                   levelLabel;
    private Label                   packageMessageLabel;
    private TextField               pidTextField;
    private TextField               tagTextField;
    private TextField               messageTextField;
    private TextField               packageMessageTextField;
    private ChoiceBox<Log.LogLevel> logLevelChoiceBox;
    private Button                  switchButton;
    private Label                   splitLine;

    @Autowired
    private DeviceLogFilterViewEvent deviceLogFilterViewEvent;

    @Override
    protected void initUi() {
        pidLabel                = new Label("PID");
        tagLabel                = new Label("标签");
        messageLabel            = new Label("消息");
        levelLabel              = new Label("级别");
        packageMessageLabel     = new Label("包名");
        pidTextField            = new TextField();
        tagTextField            = new TextField();
        messageTextField        = new TextField();
        packageMessageTextField = new TextField();
        logLevelChoiceBox       = new ChoiceBox<>();
        splitLine               = new Label("  ");
        switchButton            = new Button("开始", Icon.DEVICE_LOG_FILTER_START);
    }

    @Override
    protected void initAttr() {
        getRootPane().setPadding(new Insets(2, 2, 3, 2));
        getRootPane().setAlignment(Pos.CENTER_LEFT);

        pidLabel.setPadding(new Insets(5));
        tagLabel.setPadding(new Insets(5));
        messageLabel.setPadding(new Insets(5));
        levelLabel.setPadding(new Insets(5));
        logLevelChoiceBox.getItems().addAll(
                Log.LogLevel.DEBUG, Log.LogLevel.ASSERT, Log.LogLevel.INFO,
                Log.LogLevel.ERROR, Log.LogLevel.VERBOSE, Log.LogLevel.WARN
        );
        switchButton.setPadding(new Insets(4));
    }

    @Override
    protected void initLayout() {
        getRootPane().getChildren().addAll(
                pidLabel, pidTextField,
                tagLabel, tagTextField,
                messageLabel, messageTextField,
                levelLabel, logLevelChoiceBox,
                splitLine, switchButton
        );
    }

    @Override
    protected void initEvent() {
        switchButton.setOnMouseClicked(e -> deviceLogFilterViewEvent.switchButtonOnMouseClick(e));
    }

    @Override
    protected void initialize() {

    }
}
