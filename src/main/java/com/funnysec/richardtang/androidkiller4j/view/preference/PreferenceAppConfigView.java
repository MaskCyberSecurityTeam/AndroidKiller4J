package com.funnysec.richardtang.androidkiller4j.view.preference;

import com.funnysec.richardtang.androidkiller4j.config.ApplicationConfig;
import com.funnysec.richardtang.androidkiller4j.view.IocView;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.nutz.ioc.loader.annotation.Inject;

public class PreferenceAppConfigView extends IocView<GridPane> {

    private Label appNameLabel;
    private Label appIconLabel;
    private Label appVersionLabel;
    private Label appDefWidthLabel;
    private Label appDefHeightLabel;
    private Label appMinWidthLabel;
    private Label appMinHeightLabel;

    private TextField appNameTextField;
    private ImageView appIconImageView;
    private TextField appVersionTextField;
    private TextField appDefWidthTextField;
    private TextField appDefHeightTextField;
    private TextField appMinWidthTextField;
    private TextField appMinHeightTextField;

    @Inject
    private ApplicationConfig applicationConfig;

    @Override
    protected void initUi() {
        appNameLabel      = new Label("应用名称");
        appIconLabel      = new Label("应用图标");
        appVersionLabel   = new Label("应用版本");
        appDefWidthLabel  = new Label("窗口宽度");
        appDefHeightLabel = new Label("窗口高度");
        appMinWidthLabel  = new Label("最小宽度");
        appMinHeightLabel = new Label("最小宽度");

        appNameTextField      = new TextField();
        appIconImageView      = new ImageView(new Image(applicationConfig.getAppIconPath()));
        appVersionTextField   = new TextField();
        appDefWidthTextField  = new TextField();
        appDefHeightTextField = new TextField();
        appMinWidthTextField  = new TextField();
        appMinHeightTextField = new TextField();
    }

    @Override
    protected void initAttr() {
        getRootPane().setAlignment(Pos.CENTER);
    }

    @Override
    protected void initLayout() {
        getRootPane().setHgap(10);
        getRootPane().setVgap(2);

        getRootPane().add(appNameLabel, 0, 0);
        getRootPane().add(appIconLabel, 0, 1);
        getRootPane().add(appVersionLabel, 0, 2);
        getRootPane().add(appDefWidthLabel, 0, 3);
        getRootPane().add(appDefHeightLabel, 0, 4);
        getRootPane().add(appMinWidthLabel, 0, 5);
        getRootPane().add(appMinHeightLabel, 0, 6);

        getRootPane().add(appNameTextField, 1, 0);
        getRootPane().add(appIconImageView, 1, 1);
        getRootPane().add(appVersionTextField, 1, 2);
        getRootPane().add(appDefWidthTextField, 1, 3);
        getRootPane().add(appDefHeightTextField, 1, 4);
        getRootPane().add(appMinWidthTextField, 1, 5);
        getRootPane().add(appMinHeightTextField, 1, 6);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initialize() {

    }
}
