package com.funnysec.richardtang.androidkiller4j.properties;

import com.funnysec.richardtang.androidkiller4j.config.ResourcePathConfig;
import com.funnysec.richardtang.androidkiller4j.constant.ProtocolString;
import lombok.Data;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

@Data
@IocBean
public class ApplicationProperties {

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
}
