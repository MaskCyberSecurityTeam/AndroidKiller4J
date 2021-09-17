package com.funnysec.richardtang.androidkiller4j.config;

import cn.hutool.setting.dialect.Props;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

/**
 * JavaFx整合Spring的配置类
 *
 * @author RichardTang
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan({"com.funnysec.richardtang.androidkiller4j.*"})
public class SpringContextConfig {

    /**
     * 启动时加载配置文件 $WORK_DIR/config/application.properties
     *
     * @return ApplicationPropertiesd对象
     */
    @Bean("applicationProperties")
    public Props getApplicationProperties() {
        return new Props(ResourcePathConfig.APPLICATION_PROPERTIES);
    }
}