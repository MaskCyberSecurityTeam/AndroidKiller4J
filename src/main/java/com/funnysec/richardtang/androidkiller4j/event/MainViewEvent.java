package com.funnysec.richardtang.androidkiller4j.event;

import com.funnysec.richardtang.androidkiller4j.view.ControlView;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@link com.funnysec.richardtang.androidkiller4j.view.MainView}视图中对应的事件处理类
 *
 * @author RichardTang
 */
@Component
public class MainViewEvent {

    @Autowired
    private ControlView controlView;

    public void clearLogImageButtonOnMouseClick(MouseEvent event) {
        controlView.clearLog();
    }
}
