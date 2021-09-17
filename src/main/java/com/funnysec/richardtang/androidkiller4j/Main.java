package com.funnysec.richardtang.androidkiller4j;

import cn.hutool.setting.dialect.Props;
import com.android.ddmlib.AndroidDebugBridge;
import com.funnysec.richardtang.androidkiller4j.config.ResourcePathConfig;
import com.funnysec.richardtang.androidkiller4j.config.SpringContextConfig;
import com.funnysec.richardtang.androidkiller4j.core.ddmlib.AndroidDeviceManager;
import com.funnysec.richardtang.androidkiller4j.view.MainView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * JavaFx主程序
 *
 * @author RichardTang
 */
@Component
public class Main extends Application {

    // 应用配置对象，application.properties文件对象
    private Props appProp;

    // 主窗口程序
    private MainView mainView;

    // adb管理类
    private AndroidDeviceManager androidDeviceManager;

    // Spring上下文对象
    private static ConfigurableApplicationContext appContext;

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }

    /**
     * 初始化Spring容器，从容器中获取applicationProperties、mainView对象。
     */
    @Override
    public void init() {
        appContext = new AnnotationConfigApplicationContext(SpringContextConfig.class);

        mainView = appContext.getBean("mainView", MainView.class);
        appProp = appContext.getBean("applicationProperties", Props.class);
        androidDeviceManager = appContext.getBean("androidDeviceManager", AndroidDeviceManager.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 应用名称和应用图标需要单独处理
        String title = String.format("%s - %s", appProp.getStr("application.name"), appProp.getStr("application.version"));
        Image icon = new Image(String.format("file://%s%s", ResourcePathConfig.IMAGE, appProp.getStr("application.icon")));
        Scene scene = new Scene(mainView.getRootPane());
        scene.getStylesheets().addAll(
                "file://" + ResourcePathConfig.CSS + "style.css",
                getClass().getResource("/css/androidkiller4j-ui.css").toExternalForm()
        );

        primaryStage.setTitle(title);
        primaryStage.getIcons().add(icon);
        primaryStage.setWidth(appProp.getInt("application.defWidth"));
        primaryStage.setHeight(appProp.getInt("application.defHeight"));
        primaryStage.setMinWidth(appProp.getInt("application.minWidth"));
        primaryStage.setMinHeight(appProp.getInt("application.minHeight"));
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> close());
        primaryStage.show();
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