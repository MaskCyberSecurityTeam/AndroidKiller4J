package com.funnysec.richardtang.androidkiller4j.config;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.combo.ComboIocLoader;

/**
 * Nutz初始化配置类
 *
 * @author RichardTang
 */
public class NutzConfig {

    public static Ioc ioc;

    static {
        try {
            ioc = new NutIoc(new ComboIocLoader(
                    "*js", "config/ioc.js", "*anno", "com.funnysec.richardtang.androidkiller4j",
                    "*com.funnysec.richardtang.androidkiller4j.aop.loader.AssertDeviceOnlineLoader",
                    "*com.funnysec.richardtang.androidkiller4j.aop.loader.AssertTabLoader",
                    "*com.funnysec.richardtang.androidkiller4j.aop.loader.AssertWorkbenchTabLoader"
            ));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
