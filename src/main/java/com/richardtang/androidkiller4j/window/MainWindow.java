package com.richardtang.androidkiller4j.window;

import com.richardtang.androidkiller4j.bean.ApplicationProps;
import com.richardtang.androidkiller4j.view.MainView;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class MainWindow extends JFrame implements InitializingBean {

    @Autowired
    public ApplicationProps props;

    @Autowired
    private MainView mainView;

    public void showWindow() {
        setVisible(true);
    }

    @Override
    public void afterPropertiesSet() {
        // 配置窗口属性
        setTitle(props.getAppTitle());
        setSize(props.getMainDefSizeDimension());
        // TODO 在Windows上会报错，因为项目没有实际的图标就先不设置了。
        setIconImage(props.getIconImage().getImage());
        setMinimumSize(props.getMainMinSizeDimension());

        //配置主视图
        setContentPane(mainView);
    }
}
