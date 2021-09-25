package com.funnysec.richardtang.androidkiller4j;

import com.android.ddmlib.AndroidDebugBridge;
import com.funnysec.richardtang.androidkiller4j.config.ApplicationConfig;
import com.funnysec.richardtang.androidkiller4j.config.NutzConfig;
import com.funnysec.richardtang.androidkiller4j.config.ResourcePathConfig;
import com.funnysec.richardtang.androidkiller4j.core.ddmlib.AndroidDeviceManager;
import com.funnysec.richardtang.androidkiller4j.view.MainView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private MainView             mainView;
    private ApplicationConfig    applicationConfig;
    private AndroidDeviceManager androidDeviceManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        mainView             = NutzConfig.ioc.get(MainView.class);
        applicationConfig    = NutzConfig.ioc.get(ApplicationConfig.class);
        androidDeviceManager = NutzConfig.ioc.get(AndroidDeviceManager.class);

        // 应用名称和应用图标需要单独处理
        Scene scene = new Scene(mainView.getRootPane());
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> close());
        stage.setTitle(applicationConfig.getAppTitle());
        stage.getIcons().add(applicationConfig.getIconImage());
        stage.setWidth(applicationConfig.getMainViewDefWidth());
        stage.setHeight(applicationConfig.getMainViewDefHeight());
        stage.setMinWidth(applicationConfig.getMainViewMinWidth());
        stage.setMinHeight(applicationConfig.getMainViewMinHeight());
        scene.getStylesheets().addAll(ResourcePathConfig.STYLE_CSS, ResourcePathConfig.UI_CSS);
        stage.show();
    }

    /**
     * 关闭应用
     * 1. 断开ADB
     * 2. 暂停JavaFx线程
     * 3. 退出Java进程
     */
    public void close() {
        AndroidDebugBridge.disconnectBridge();
        AndroidDebugBridge.terminate();
        androidDeviceManager.killServer();
        Platform.exit();
        System.exit(0);
    }
}
