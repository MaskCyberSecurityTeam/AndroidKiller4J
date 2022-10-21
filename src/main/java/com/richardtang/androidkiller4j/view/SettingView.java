package com.richardtang.androidkiller4j.view;

import cn.hutool.log.StaticLog;
import com.formdev.flatlaf.*;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.richardtang.androidkiller4j.constant.SvgName;
import com.richardtang.androidkiller4j.setting.ApplicationSetting;
import com.richardtang.androidkiller4j.ui.action.ClickAction;
import com.richardtang.androidkiller4j.ui.action.ClickActionInstaller;
import com.richardtang.androidkiller4j.ui.action.ClickActionType;
import com.richardtang.androidkiller4j.ui.control.CustomTextComboBox;
import com.richardtang.androidkiller4j.ui.control.NumberTextField;
import com.richardtang.androidkiller4j.util.ControlUtils;
import lombok.Data;
import lombok.SneakyThrows;
import net.miginfocom.swing.MigLayout;
import org.reflections.Reflections;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 工具配置面板
 *
 * @author RichardTang
 */
@Data
public final class SettingView extends JPanel {

    private final JTextField javaBinPathTextField         = new JTextField();
    private final JTextField mainWindowDefWidthTextField  = new NumberTextField();
    private final JTextField mainWindowDefHeightTextField = new NumberTextField();
    private final JTextField mainWindowMinWidthTextField  = new NumberTextField();
    private final JTextField mainWindowMinHeightTextField = new NumberTextField();

    private final JLabel mainWindowDefWidthLabel  = new JLabel("主窗口默认宽度");
    private final JLabel mainWindowDefHeightLabel = new JLabel("主窗口默认高度");
    private final JLabel mainWindowMinWidthLabel  = new JLabel("主窗口最小宽度");
    private final JLabel mainWindowMinHeightLabel = new JLabel("主窗口最小高度");
    private final JLabel javaBinPathLabel         = new JLabel("Java路径");
    private final JLabel applicationThemeLabel    = new JLabel("程序主题");

    // 选择Java程序路径
    private final JLabel javaBinPathSelectIconLabel = ControlUtils.getLabelIcon(SvgName.DIRECTORY);

    // 主题选择
    private final CustomTextComboBox<Class<? extends FlatLaf>> applicationThemeComboBox = new CustomTextComboBox<>() {
        @SneakyThrows
        @Override
        protected String getText(Class<? extends FlatLaf> subClass) {
            return (String) subClass.getField("NAME").get(null);
        }
    };

    // 主题包
    private static final String FLATLAF_THEME_PACKAGE = "com.formdev.flatlaf.intellijthemes";

    private final JButton saveButton = new JButton("保存");

    public SettingView() {
        // 添加组件到根Panel
        setLayout(new MigLayout("", "[][grow]"));
        add(mainWindowDefWidthLabel, "cell 0 0");
        add(mainWindowDefWidthTextField, "cell 1 0, grow");
        add(mainWindowDefHeightLabel, "cell 0 1");
        add(mainWindowDefHeightTextField, "cell 1 1, grow");
        add(mainWindowMinWidthLabel, "cell 0 2");
        add(mainWindowMinWidthTextField, "cell 1 2, grow");
        add(mainWindowMinHeightLabel, "cell 0 3");
        add(mainWindowMinHeightTextField, "cell 1 3, grow");
        add(javaBinPathLabel, "cell 0 4");
        add(javaBinPathTextField, "cell 1 4, grow");
        add(applicationThemeLabel, "cell 0 5");
        add(applicationThemeComboBox, "cell 1 5, grow, wrap");
        add(saveButton, "span, grow");

        // 设置JavaBinPath的路径选择Icon图标
        javaBinPathSelectIconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        javaBinPathTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_COMPONENT, javaBinPathSelectIconLabel);

        // 从配置文件中读取值，设置到组件上。
        mainWindowDefWidthTextField.setText(ApplicationSetting.getInstance().getMainWindowDefWidth().toString());
        mainWindowDefHeightTextField.setText(ApplicationSetting.getInstance().getMainWindowDefHeight().toString());
        mainWindowMinWidthTextField.setText(ApplicationSetting.getInstance().getMainWindowMinWidth().toString());
        mainWindowMinHeightTextField.setText(ApplicationSetting.getInstance().getMainWindowMinHeight().toString());
        javaBinPathTextField.setText(ApplicationSetting.getInstance().getJavaBinPath());

        // 通过反射获取所有FlatLafThemes主题包
        Reflections reflections = new Reflections(FLATLAF_THEME_PACKAGE);
        List<Class<? extends FlatLaf>> themeCollection = reflections.getSubTypesOf(FlatLaf.class).stream().sorted((t1, t2) -> t1.getSimpleName().compareToIgnoreCase(t2.getSimpleName())).collect(Collectors.toList());

        // 加入4个FlatLaf的默认主题类
        themeCollection.add(FlatDarkLaf.class);
        themeCollection.add(FlatLightLaf.class);
        themeCollection.add(FlatDarculaLaf.class);
        themeCollection.add(FlatIntelliJLaf.class);

        // 将主题添加至ComboBox中
        String currentThemeClassName = ApplicationSetting.getInstance().getApplicationTheme();
        for (Class<? extends FlatLaf> themeClass : themeCollection) {
            applicationThemeComboBox.addItem(themeClass);
            if (currentThemeClassName.equals(themeClass.getName())) {
                applicationThemeComboBox.setSelectedItem(themeClass);
            }
        }

        // 绑定事件
        ClickActionInstaller.bind(this);
    }

    @ClickAction("saveButton")
    public void saveButtonClick(ActionEvent event) {
        Class<? extends FlatLaf> selectedItem = applicationThemeComboBox.getSelectedItem();
        if (selectedItem != null) {
            // 动态更新程序主题
            try {
                FlatLaf themeLaf = selectedItem.getDeclaredConstructor().newInstance();
                FlatAnimatedLafChange.showSnapshot();
                FlatLaf.setup(themeLaf);
                FlatLaf.updateUI();
                FlatAnimatedLafChange.hideSnapshotWithAnimation();
                ApplicationSetting.getInstance().setApplicationTheme(selectedItem);
            } catch (Exception e) {
                ControlUtils.showMsgDialog("提示信息", "更换主题失败!");
                StaticLog.error(e);
                return;
            }
        }

        ApplicationSetting.getInstance().setJavaBinPath(javaBinPathTextField.getText());
        ApplicationSetting.getInstance().setMainWindowDefHeight(Integer.parseInt(mainWindowDefHeightTextField.getText()));
        ApplicationSetting.getInstance().setMainWindowDefWidth(Integer.parseInt(mainWindowDefWidthTextField.getText()));
        ApplicationSetting.getInstance().setMainWindowMinHeight(Integer.parseInt(mainWindowMinHeightTextField.getText()));
        ApplicationSetting.getInstance().setMainWindowMinWidth(Integer.parseInt(mainWindowMinWidthTextField.getText()));
        ApplicationSetting.getInstance().store();
        ControlUtils.showMsgDialog("提示信息", "保存成功");
    }

    @ClickAction(value = "javaBinPathSelectIconLabel", type = ClickActionType.LabelComponent)
    public void javaBinPathIconLabelClick(MouseEvent event) {
        File file = ControlUtils.chooserFileDialog();
        if (file != null) {
            javaBinPathTextField.setText(file.getAbsolutePath());
        }
    }
}