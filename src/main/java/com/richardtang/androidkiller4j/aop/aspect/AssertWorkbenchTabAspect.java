package com.richardtang.androidkiller4j.aop.aspect;

import com.android.ddmlib.IDevice;
import com.richardtang.androidkiller4j.aop.annotation.AssertTab;
import com.richardtang.androidkiller4j.util.ControlUtil;
import com.richardtang.androidkiller4j.view.task.TaskView;
import com.richardtang.androidkiller4j.view.workbench.WorkbenchView;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;

@Aspect
@org.springframework.stereotype.Component
public class AssertWorkbenchTabAspect {

    @Autowired
    private TaskView taskView;

    @Around(value = "@annotation(com.richardtang.androidkiller4j.aop.annotation.AssertWorkbenchTab)")
    public void before(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Component selectedComponent = taskView.getSelectedComponent();
        if (selectedComponent instanceof WorkbenchView) {
            proceedingJoinPoint.proceed();
        } else {
            ControlUtil.showMsgDialog("提示信息", "该功能只适用APK工作台页面");
        }
    }
}