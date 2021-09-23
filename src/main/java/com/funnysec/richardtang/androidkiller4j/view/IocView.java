package com.funnysec.richardtang.androidkiller4j.view;

import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(create = "createAfterLaunch")
public abstract class IocView<P> extends BaseView<P> {
}
