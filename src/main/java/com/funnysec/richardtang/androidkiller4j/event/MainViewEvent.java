package com.funnysec.richardtang.androidkiller4j.event;

import com.funnysec.richardtang.androidkiller4j.view.ControlView;
import javafx.scene.input.MouseEvent;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean
public class MainViewEvent {

    @Inject
    private ControlView controlView;

    public void clearLogImageButtonOnMouseClick(MouseEvent event) {
        controlView.clearLog();
    }
}
