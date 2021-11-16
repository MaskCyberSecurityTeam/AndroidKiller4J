package com.richardtang.androidkiller4j.aop.aspect;

import com.richardtang.androidkiller4j.aop.annotation.AssertTab;
import com.richardtang.androidkiller4j.view.task.TaskView;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

@Aspect
@org.springframework.stereotype.Component
public class AssertTabAspect {

    @Autowired
    private TaskView taskView;

    @Around(value = "@annotation(com.richardtang.androidkiller4j.aop.annotation.AssertTab)")
    public void before(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        AssertTab assertTabAnno = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getAnnotation(AssertTab.class);

        if (!taskView.showTabByTitle(assertTabAnno.value())) {
            proceedingJoinPoint.proceed();
        }
    }
}