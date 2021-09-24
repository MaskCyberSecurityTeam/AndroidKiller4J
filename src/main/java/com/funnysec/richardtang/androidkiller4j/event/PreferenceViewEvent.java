package com.funnysec.richardtang.androidkiller4j.event;

import com.funnysec.richardtang.androidkiller4j.config.ApplicationConfig;
import com.funnysec.richardtang.androidkiller4j.view.preference.PreferenceAppConfigView;
import com.funnysec.richardtang.androidkiller4j.view.preference.PreferenceView;
import javafx.beans.Observable;
import javafx.scene.control.TreeItem;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean
public class PreferenceViewEvent {

    @Inject
    private PreferenceView preferenceView;

    @Inject
    private PreferenceAppConfigView preferenceAppConfigView;

    @Inject
    private ApplicationConfig applicationConfig;

    public void optionTreeViewSelectItem(Observable obs, TreeItem<String> oldValue, TreeItem<String> newValue) {
        preferenceView.getShowStackPane().getChildren().add(preferenceAppConfigView.getRootPane());
    }
}
