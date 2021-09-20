package com.funnysec.richardtang.androidkiller4j.aspect;

import com.funnysec.richardtang.androidkiller4j.pojo.Apk;
import com.funnysec.richardtang.androidkiller4j.pojo.TabUserData;
import com.funnysec.richardtang.androidkiller4j.util.FxUtil;
import com.funnysec.richardtang.androidkiller4j.view.TaskView;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 校验当前TaskView中选中的Tab页是否为{@link com.funnysec.richardtang.androidkiller4j.view.WorkbenchView}类型
 *
 * @author RichardTang
 */
@Aspect
@Component
public class AssertWorkbenchTabAspect {

    @Autowired
    private TaskView taskView;

    /**
     * 拦截函数上带有AssertWorkbenchTabAspect注解的函数
     *
     * @param proceedingJoinPoint 用来控制函数是否往下执行
     * @return {@link ProceedingJoinPoint}
     * @throws Throwable 调用proceed时抛出的异常
     */
    @Around(value = "@annotation(com.funnysec.richardtang.androidkiller4j.annotation.AssertWorkbenchTab)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            Object tabUserData = taskView.getSelectTabUserData();
            if (tabUserData instanceof TabUserData && ((TabUserData) tabUserData).getData() instanceof Apk) {
                return proceedingJoinPoint.proceed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FxUtil.alert("提示信息", "只能选中为APK工作台的页面");
        return proceedingJoinPoint;
    }
}
