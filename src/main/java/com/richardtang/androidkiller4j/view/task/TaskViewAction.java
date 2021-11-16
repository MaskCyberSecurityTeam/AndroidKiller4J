package com.richardtang.androidkiller4j.view.task;

import com.richardtang.androidkiller4j.bean.Apk;
import com.richardtang.androidkiller4j.view.workbench.WorkbenchView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.event.MouseEvent;

@Component
public class TaskViewAction {

    @Autowired
    private TaskView taskView;

    public void tableMouseDoubleClickEvent(MouseEvent event) {
        Apk apk = taskView.getSelectedApk();
        taskView.addTab(apk.getFileName(), apk.getImageIcon(), new WorkbenchView(apk));
    }
}