package com.richardtang.androidkiller4j.validator;

import com.richardtang.androidkiller4j.MainWindow;
import com.richardtang.androidkiller4j.util.ControlUtils;
import com.richardtang.androidkiller4j.view.WorkbenchView;

/**
 * 校验当前TaskView中选中的Tab是否为Workbench类型
 */
public class SelectedIsWorkbenchTabValidator {

    public static boolean verify() {
        if (MainWindow.taskView.getSelectedComponent() instanceof WorkbenchView) return true;
        ControlUtils.showMsgDialog("提示信息", "该功能只适用APK工作台页面");
        return false;
    }
}