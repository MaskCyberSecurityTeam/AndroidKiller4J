package com.funnysec.richardtang.androidkiller4j;

import com.android.ddmlib.AndroidDebugBridge;
import com.funnysec.richardtang.androidkiller4j.config.ResourcePathConfig;
import com.funnysec.richardtang.androidkiller4j.core.ddmlib.AndroidDeviceManager;
import com.funnysec.richardtang.androidkiller4j.properties.ApplicationProperties;
import com.funnysec.richardtang.androidkiller4j.view.MainView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.combo.ComboIocLoader;

public class Main extends Application {

    public static Ioc ioc;

    private AndroidDeviceManager androidDeviceManager;

    static {
        try {
            ioc = new NutIoc(new ComboIocLoader(
                    "*js", "config/ioc.js",
                    "*anno", "com.funnysec.richardtang.androidkiller4j",
                    "*com.funnysec.richardtang.androidkiller4j.aop.loader.AssertDeviceOnlineLoader",
                    "*com.funnysec.richardtang.androidkiller4j.aop.loader.AssertTabLoader",
                    "*com.funnysec.richardtang.androidkiller4j.aop.loader.AssertWorkbenchTabLoader"
            ));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        androidDeviceManager = ioc.get(AndroidDeviceManager.class);
        MainView              mainView              = ioc.get(MainView.class);
        ApplicationProperties applicationProperties = ioc.get(ApplicationProperties.class);

        // 应用名称和应用图标需要单独处理
        String title = String.format("%s - %s", applicationProperties.getAppName(), applicationProperties.getAppVersion());
        Image  icon  = new Image(applicationProperties.getAppIconPath());
        Scene  scene = new Scene(mainView.getRootPane());
        scene.getStylesheets().addAll(
                "file://" + ResourcePathConfig.CSS + "style.css", getClass().getResource("/css/androidkiller4j-ui.css").toExternalForm()
        );

        stage.setTitle(title);
        stage.getIcons().add(icon);
        stage.setWidth(Double.parseDouble(applicationProperties.getMainViewDefWidth()));
        stage.setHeight(Double.parseDouble(applicationProperties.getMainViewDefHeight()));
        stage.setMinWidth(Double.parseDouble(applicationProperties.getMainViewMinWidth()));
        stage.setMinHeight(Double.parseDouble(applicationProperties.getMainViewMinHeight()));
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> close());
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
