package com.funnysec.richardtang.androidkiller4j.view;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.watch.SimpleWatcher;
import cn.hutool.core.io.watch.WatchMonitor;
import com.funnysec.richardtang.androidkiller4j.config.ResourcePathConfig;
import com.funnysec.richardtang.androidkiller4j.constant.Icon;
import com.funnysec.richardtang.androidkiller4j.event.SignatureViewEvent;
import com.funnysec.richardtang.androidkiller4j.pojo.TabUserData;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Data;
import org.controlsfx.control.PrefixSelectionComboBox;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;

@Data
@Component
public class SignatureView extends BaseView<Tab> {

    private SplitPane splitPane;
    private GridPane  leftGridPane;
    private GridPane  rightGridPane;

    // 选择需要签名的APK
    private Label           signApkLabel;
    private CustomTextField signApkTextField;
    private Glyph           signApkGlyphButton;

    // 输出路径
    private Label           signApkOutputLabel;
    private CustomTextField signApkOutputTextField;
    private Glyph           signApkOutputGlyphButton;

    // 选择证书
    private Label                           signCertLabel;
    private PrefixSelectionComboBox<String> signCertComboBox;

    // 可选项
    private Label    optionalLabel;
    private HBox     optionalHBox;
    private CheckBox openDirCheckBox;
    private CheckBox delSourceCheckBox;

    // 操作按钮
    private Label  operateLabel;
    private HBox   operateHBox;
    private Button signButton;
    private Button clearButton;

    /*********************** 生成证书 ***********************/
    // 证书别名
    private Label                           certAliasLabel;
    private TextField                       certAliasTextField;
    // 秘钥算法 https://docs.oracle.com/en/java/javase/16/docs/specs/man/keytool.html
    private Label                           certKeyAlgLabel;
    private PrefixSelectionComboBox<String> certKeyAlgComboBox;
    // 秘钥长度
    private Label                           certKeySizeLabel;
    private TextField                       certKeySizeTextField;
    // 秘钥库名称
    private Label                           certKeyStoreLabel;
    private CustomTextField                 certKeyStoreTextField;
    // 私钥密码
    private Label                           certKeyPassLabel;
    private TextField                       certKeyPassTextField;
    // 秘钥库密码
    private Label                           certStorePassLabel;
    private TextField                       certStorePassTextField;
    // 证书有效天数
    private Label                           certValidityLabel;
    private TextField                       certValidityTextField;
    // 名称
    private Label                           certCommonNameLabel;
    private TextField                       certCommonNameTextField;
    // 单位名称
    private Label                           certOrganizationUnitLabel;
    private TextField                       certOrganizationUnitTextField;
    // 组织名称
    private Label                           certOrganizationLabel;
    private TextField                       certOrganizationTextField;
    // 城市名称
    private Label                           certLocalityLabel;
    private TextField                       certLocalityTextField;
    // 省份名称
    private Label                           certStateLabel;
    private TextField                       certStateTextField;
    // 国家名称
    private Label                           certCountryLabel;
    private TextField                       certCountryTextField;
    // 操作按钮
    private Label                           certOperateBtnLabel;
    private Button                          certOperateButton;

    @Autowired
    private SignatureViewEvent signatureViewEvent;

    // 用于监听项目的config/目录
    private WatchMonitor watchMonitor = WatchMonitor.create(
            new File(ResourcePathConfig.CONFIG), WatchMonitor.ENTRY_CREATE
    );

    @Override
    protected void initUi() {
        splitPane     = new SplitPane();
        leftGridPane  = new GridPane();
        rightGridPane = new GridPane();

        signApkLabel       = new Label("需要签名的APK");
        signApkTextField   = new CustomTextField();
        signApkGlyphButton = Icon.FONT_AWESOME.create(FontAwesome.Glyph.FOLDER_OPEN);

        signApkOutputLabel       = new Label("输出路径");
        signApkOutputTextField   = new CustomTextField();
        signApkOutputGlyphButton = Icon.FONT_AWESOME.create(FontAwesome.Glyph.FOLDER_OPEN);

        signCertLabel    = new Label("选择证书");
        signCertComboBox = new PrefixSelectionComboBox<>();

        optionalLabel     = new Label("可选项");
        optionalHBox      = new HBox();
        openDirCheckBox   = new CheckBox("签名完打开所在目录");
        delSourceCheckBox = new CheckBox("签名完后删除源文件");

        operateLabel = new Label("操作按钮");
        operateHBox  = new HBox();
        clearButton  = new Button("清空", Icon.FONT_AWESOME.create(FontAwesome.Glyph.TRASH));
        signButton   = new Button("签名", Icon.FONT_AWESOME.create(FontAwesome.Glyph.KEY));

        certAliasLabel            = new Label("证书别名");
        certKeyAlgLabel           = new Label("秘钥算法");
        certKeySizeLabel          = new Label("秘钥长度");
        certKeyStoreLabel         = new Label("秘钥库名称");
        certKeyPassLabel          = new Label("私钥密码");
        certStorePassLabel        = new Label("秘钥库密码");
        certValidityLabel         = new Label("证书有效天数");
        certCommonNameLabel       = new Label("名称");
        certOrganizationUnitLabel = new Label("单位名称");
        certOrganizationLabel     = new Label("组织名称");
        certLocalityLabel         = new Label("城市名称");
        certStateLabel            = new Label("省份名称");
        certCountryLabel          = new Label("国家名称");
        certOperateBtnLabel       = new Label("操作按钮");

        certAliasTextField            = new TextField();
        certKeyAlgComboBox            = new PrefixSelectionComboBox<>();
        certKeySizeTextField          = new TextField();
        certKeyStoreTextField         = new CustomTextField();
        certKeyPassTextField          = new TextField();
        certStorePassTextField        = new TextField();
        certValidityTextField         = new TextField();
        certCommonNameTextField       = new TextField();
        certOrganizationUnitTextField = new TextField();
        certOrganizationTextField     = new TextField();
        certLocalityTextField         = new TextField();
        certStateTextField            = new TextField();
        certCountryTextField          = new TextField();
        certOperateButton             = new Button("生成证书");
    }

    @Override
    protected void initAttr() {
        getRootPane().setText("签名");
        getRootPane().setGraphic(Icon.SIGNATURE_VIEW_TAB);
        getRootPane().setUserData(new TabUserData<>("签名", null));

        signApkGlyphButton.setFontSize(14);
        signApkGlyphButton.setTranslateX(-3);
        signApkGlyphButton.setCursor(Cursor.HAND);
        signApkGlyphButton.useGradientEffect().useHoverEffect();

        signApkOutputGlyphButton.setFontSize(14);
        signApkOutputGlyphButton.setTranslateX(-3);
        signApkOutputGlyphButton.setCursor(Cursor.HAND);
        signApkOutputGlyphButton.useGradientEffect().useHoverEffect();

        optionalHBox.setSpacing(10);
        signCertComboBox.setMinWidth(300);

        operateHBox.setSpacing(10);
        clearButton.setMinWidth(145);
        signButton.setMinWidth(145);

        leftGridPane.setHgap(10);
        leftGridPane.setVgap(10);
        leftGridPane.setAlignment(Pos.CENTER);
        leftGridPane.setPadding(new Insets(10));

        rightGridPane.setHgap(10);
        rightGridPane.setVgap(10);
        rightGridPane.setAlignment(Pos.CENTER);
        rightGridPane.setPadding(new Insets(10));

        certOperateButton.setMinWidth(300);
        certKeyAlgComboBox.setMinWidth(300);
        certKeyAlgComboBox.getItems().addAll("RSA", "DSA");
    }

    @Override
    protected void initLayout() {
        operateHBox.getChildren().addAll(clearButton, signButton);
        optionalHBox.getChildren().addAll(openDirCheckBox, delSourceCheckBox);

        signApkTextField.setRight(signApkGlyphButton);
        signApkOutputTextField.setRight(signApkOutputGlyphButton);

        // 标记输入框文件后缀为.keystore
        Label label = new Label(".keystore");
        label.setTextFill(Color.gray(0.5));
        label.setTranslateX(-2);
        certKeyStoreTextField.setRight(label);

        leftGridPane.add(signApkLabel, 0, 0);
        leftGridPane.add(signApkTextField, 1, 0);
        leftGridPane.add(signApkOutputLabel, 0, 1);
        leftGridPane.add(signApkOutputTextField, 1, 1);
        leftGridPane.add(signCertLabel, 0, 2);
        leftGridPane.add(signCertComboBox, 1, 2);
        leftGridPane.add(optionalLabel, 0, 3);
        leftGridPane.add(optionalHBox, 1, 3);
        leftGridPane.add(operateLabel, 0, 4);
        leftGridPane.add(operateHBox, 1, 4);

        rightGridPane.add(certAliasLabel, 0, 0);
        rightGridPane.add(certAliasTextField, 1, 0);
        rightGridPane.add(certKeyAlgLabel, 0, 1);
        rightGridPane.add(certKeyAlgComboBox, 1, 1);
        rightGridPane.add(certKeySizeLabel, 0, 2);
        rightGridPane.add(certKeySizeTextField, 1, 2);
        rightGridPane.add(certKeyStoreLabel, 0, 3);
        rightGridPane.add(certKeyStoreTextField, 1, 3);
        rightGridPane.add(certKeyPassLabel, 0, 4);
        rightGridPane.add(certKeyPassTextField, 1, 4);
        rightGridPane.add(certStorePassLabel, 0, 5);
        rightGridPane.add(certStorePassTextField, 1, 5);
        rightGridPane.add(certValidityLabel, 0, 6);
        rightGridPane.add(certValidityTextField, 1, 6);
        rightGridPane.add(certCommonNameLabel, 0, 7);
        rightGridPane.add(certCommonNameTextField, 1, 7);
        rightGridPane.add(certOrganizationUnitLabel, 0, 8);
        rightGridPane.add(certOrganizationUnitTextField, 1, 8);
        rightGridPane.add(certOrganizationLabel, 0, 9);
        rightGridPane.add(certOrganizationTextField, 1, 9);
        rightGridPane.add(certLocalityLabel, 0, 10);
        rightGridPane.add(certLocalityTextField, 1, 10);
        rightGridPane.add(certStateLabel, 0, 11);
        rightGridPane.add(certStateTextField, 1, 11);
        rightGridPane.add(certCountryLabel, 0, 12);
        rightGridPane.add(certCountryTextField, 1, 12);
        rightGridPane.add(certOperateBtnLabel, 0, 13);
        rightGridPane.add(certOperateButton, 1, 13);

        splitPane.getItems().addAll(leftGridPane, rightGridPane);
        getRootPane().setContent(splitPane);
    }

    @Override
    protected void initEvent() {
        signButton.setOnMouseClicked(e -> signatureViewEvent.signButtonOnMouseClick(e));
        clearButton.setOnMouseClicked(e -> signatureViewEvent.clearButtonOnMouseClick(e));
        signApkGlyphButton.setOnMouseClicked(e -> signatureViewEvent.signApkGlyphButtonOnMouseClick(e));
        certOperateButton.setOnMouseClicked(e -> signatureViewEvent.certOperateButtonOnMouseClick(e));
        signApkOutputGlyphButton.setOnMouseClicked(e -> signatureViewEvent.signApkOutputGlyphButtonOnMouseClick(e));
    }

    @Override
    protected void initialize() {
        // ui初始化完毕后获取一次数据
        getConfigDirKeyStoreToComboBox();

        // 开启监听config目录
        watchMonitor.setMaxDepth(1);
        watchMonitor.setWatcher(new SimpleWatcher() {
            @Override
            public void onCreate(WatchEvent<?> watchEvent, Path path) {
                Platform.runLater(() -> getConfigDirKeyStoreToComboBox());
            }

            @Override
            public void onModify(WatchEvent<?> watchEvent, Path path) {
                Platform.runLater(() -> getConfigDirKeyStoreToComboBox());
            }

            @Override
            public void onDelete(WatchEvent<?> watchEvent, Path path) {
                Platform.runLater(() -> getConfigDirKeyStoreToComboBox());
            }
        });
        watchMonitor.start();

    }

    private void getConfigDirKeyStoreToComboBox() {
        signCertComboBox.getItems().clear();
        for (File f : FileUtil.ls(ResourcePathConfig.CONFIG)) {
            if (f.getName().contains(".keystore")) {
                signCertComboBox.getItems().add(f.getName());
            }
        }
    }
}
