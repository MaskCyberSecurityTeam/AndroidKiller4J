package com.funnysec.richardtang.androidkiller4j.event;

import com.funnysec.richardtang.androidkiller4j.pojo.Apk;
import com.funnysec.richardtang.androidkiller4j.view.TaskView;
import com.funnysec.richardtang.androidkiller4j.view.WorkbenchView;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@link TaskView}视图中的组件对应的事件类
 *
 * @author RichardTang
 */
@Component
public class TaskViewEvent {

    @Autowired
    private TaskView taskView;

    /**
     * 任务列表中的任意一个任务被双击时触发该函数
     *
     * @param event 事件对象
     */
    public void listViewItenOnMouseClick(MouseEvent event) {
        // 非双击的不往下继续处理
        if (event.getClickCount() != 2) {
            return;
        }

        Apk apk = taskView.getListView().getSelectionModel().getSelectedItem();
        // 查找是否已开启了该Tab页
        Tab tab = taskView.findTabById(apk.getFileName());
        if (tab == null) {
            Tab WorkbenchTab = new WorkbenchView(apk).getRootPane();
            taskView.getRootPane().getTabs().add(WorkbenchTab);
        } else {
            taskView.getRootPane().getSelectionModel().select(tab);
        }
    }
}