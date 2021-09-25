package com.funnysec.richardtang.androidkiller4j.aop.interceptor;

import com.funnysec.richardtang.androidkiller4j.aop.annotation.AssertTab;
import com.funnysec.richardtang.androidkiller4j.view.TaskView;
import javafx.scene.control.Tab;
import org.nutz.aop.InterceptorChain;
import org.nutz.aop.MethodInterceptor;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * {@link com.funnysec.richardtang.androidkiller4j.aop.annotation.AssertTab}注解的实现
 *
 * @author RichardTang
 */
@IocBean
public class AssertTabInterceptor implements MethodInterceptor {

    @Inject
    private TaskView taskView;

    @Override
    public void filter(InterceptorChain chain) throws Throwable {
        // 获取函数上的注解值
        AssertTab assertTabAnno = chain.getCallingMethod().getAnnotation(AssertTab.class);

        // 根据TabId值判断是否已经打开了日志窗口
        Tab tab = taskView.findTabById(assertTabAnno.value());

        if (tab != null) {
            // 直接显示对应的Tab窗口
            taskView.getRootPane().getSelectionModel().select(tab);
        }
        chain.doChain();
    }
}
