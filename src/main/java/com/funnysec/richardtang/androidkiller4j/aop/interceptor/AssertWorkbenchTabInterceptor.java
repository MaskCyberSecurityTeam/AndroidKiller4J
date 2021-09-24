package com.funnysec.richardtang.androidkiller4j.aop.interceptor;

import com.funnysec.richardtang.androidkiller4j.pojo.Apk;
import com.funnysec.richardtang.androidkiller4j.pojo.TabUserData;
import com.funnysec.richardtang.androidkiller4j.util.FxUtil;
import com.funnysec.richardtang.androidkiller4j.view.TaskView;
import org.nutz.aop.InterceptorChain;
import org.nutz.aop.MethodInterceptor;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * {@link com.funnysec.richardtang.androidkiller4j.aop.annotation.AssertWorkbenchTab}注解的实现
 *
 * @author RichardTang
 */
@IocBean
public class AssertWorkbenchTabInterceptor implements MethodInterceptor {

    @Inject
    private TaskView taskView;

    @Override
    public void filter(InterceptorChain chain) throws Throwable {
        Object tabUserData = taskView.getSelectTabUserData();
        try {
            if (tabUserData instanceof TabUserData && ((TabUserData<?>) tabUserData).getData() instanceof Apk) {
                chain.doChain();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FxUtil.alert("提示信息", "只能选中为APK工作台的页面");
    }
}
