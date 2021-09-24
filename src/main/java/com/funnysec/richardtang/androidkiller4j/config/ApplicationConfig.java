package com.funnysec.richardtang.androidkiller4j.config;

import com.funnysec.richardtang.androidkiller4j.constant.ProtocolString;
import javafx.scene.image.Image;
import lombok.Data;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * application.properties配置文件对象
 *
 * @author RichardTang
 */
@Data
@IocBean
public class ApplicationConfig {

    @Inject("java:$conf.get('app.name')")
    private String appName;

    @Inject("java:$conf.get('app.icon')")
    private String appIcon;

    @Inject("java:$conf.get('app.version')")
    private String appVersion;

    @Inject("java:$conf.get('mainView.defWidth')")
    private String mainViewDefWidth;

    @Inject("java:$conf.get('mainView.defHeight')")
    private String mainViewDefHeight;

    @Inject("java:$conf.get('mainView.minWidth')")
    private String mainViewMinWidth;

    @Inject("java:$conf.get('mainView.minHeight')")
    private String mainViewMinHeight;

    public String getAppIconPath() {
        return String.format("%s%s%s", ProtocolString.FILE, ResourcePathConfig.IMAGE, getAppIcon());
    }

    public Image getIconImage() {
        return new Image(getAppIconPath());
    }

    public String getAppTitle() {
        return String.format("%s - %s", getAppName(), getAppVersion());
    }

    public Double getMainViewDefWidth() {
        return Double.parseDouble(mainViewDefWidth);
    }

    public Double getMainViewDefHeight() {
        return Double.parseDouble(mainViewDefHeight);
    }

    public Double getMainViewMinWidth() {
        return Double.parseDouble(mainViewMinWidth);
    }

    public Double getMainViewMinHeight() {
        return Double.parseDouble(mainViewMinHeight);
    }

    /**
     * 将配置项保存至application.properties文件中
     *
     * @throws IOException 文件找不到时抛出
     */
    public void store() throws IOException {
        Properties prop = new Properties();
        prop.put("app.name", getAppName());
        prop.put("app.icon", getAppName());
        prop.put("app.version", getAppName());
        prop.put("mainView.defWidth", getAppName());
        prop.put("mainView.defHeight", getAppName());
        prop.put("mainView.minWidth", getAppName());
        prop.put("mainView.minHeight", getAppName());
        prop.store(new FileWriter(ResourcePathConfig.APPLICATION_PROPERTIES), "保存配置");
    }
}