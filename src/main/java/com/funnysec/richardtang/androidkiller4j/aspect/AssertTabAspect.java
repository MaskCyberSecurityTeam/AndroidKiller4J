package com.funnysec.richardtang.androidkiller4j.aspect;

import com.funnysec.richardtang.androidkiller4j.annotation.AssertTab;
import com.funnysec.richardtang.androidkiller4j.view.TaskView;
import javafx.scene.control.Tab;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@link AssertTab} 注解的处理逻辑
 * 主要用来实现校验，当前应用TaskView中是否已经打开对应的Tab页。如果打开则直接显示对应的Tab页
 *
 * @author RichardTang
 */
@Aspect
@Component
public class AssertTabAspect {

    @Autowired
    private TaskView taskView;

    /**
     * 拦截函数上带有SelectTab注解的函数
     *
     * @param proceedingJoinPoint 控制流程的对象
     * @return {@link ProceedingJoinPoint}
     * @throws Throwable 调用proceed时抛出的异常
     */
    @Around(value = "@annotation(com.funnysec.richardtang.androidkiller4j.annotation.AssertTab)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        // 获取函数上的注解对象
        AssertTab selectTabAnno =
                ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getAnnotation(AssertTab.class);

        // 根据TabId值判断是否已经打开了日志窗口
        Tab logTab = taskView.findTabById(selectTabAnno.value());

        if (logTab != null) {
            // 直接显示对应的Tab窗口
            taskView.getRootPane().getSelectionModel().select(logTab);
            return proceedingJoinPoint;
        }

        return proceedingJoinPoint.proceed();
    }
}
