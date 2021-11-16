package com.richardtang.androidkiller4j.view.signature;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.watch.SimpleWatcher;
import cn.hutool.core.io.watch.WatchMonitor;
import com.formdev.flatlaf.FlatClientProperties;
import com.richardtang.androidkiller4j.constant.ResPath;
import com.richardtang.androidkiller4j.ui.document.NumberDocument;
import com.richardtang.androidkiller4j.util.ControlUtil;
import lombok.Data;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;

@Data
@Component
public class SignatureView extends JPanel {

    // apk签名
    private JPanel            apkSignaturePanel;
    private JLabel            signatureApkLabel;
    private JLabel            outputApkPathLabel;
    private JLabel            selectKeystoreLabel;
    private JLabel            optionalLabel;
    private JLabel            operateBtnLabel;
    private JTextField        signatureApkTextField;
    private JTextField        outputApkPathTextField;
    private JComboBox<String> selectKeystoreComboBox;
    private JCheckBox         signAfterOpenDirCheckBox;
    private JCheckBox         signAfterDelSrcFileCheckBox;
    private JButton           cleanButton;
    private JButton           signatureButton;
    private JLabel            signatureApkIconLabel;
    private JLabel            outputApkPathIconLabel;

    // 证书生成
    private JPanel            keystoreGeneratePanel;
    private JLabel            certAliasLabel;
    private JLabel            certKeyAlgLabel;
    private JLabel            certKeySizeLabel;
    private JLabel            certKeyStoreLabel;
    private JLabel            certKeyPassLabel;
    private JLabel            certStorePassLabel;
    private JLabel            certValidityLabel;
    private JLabel            certCommonNameLabel;
    private JLabel            certOrganizationUnitLabel;
    private JLabel            certOrganizationLabel;
    private JLabel            certLocalityLabel;
    private JLabel            certStateLabel;
    private JLabel            certCountryLabel;
    private JLabel            certOperateBtnLabel;
    private JTextField        certAliasTextField;
    private JComboBox<String> certKeyAlgComboBox;
    private JTextField        certKeySizeTextField;
    private JTextField        certKeyStoreTextField;
    private JTextField        certKeyPassTextField;
    private JTextField        certStorePassTextField;
    private JTextField        certValidityTextField;
    private JTextField        certCommonNameTextField;
    private JTextField        certOrganizationUnitTextField;
    private JTextField        certOrganizationTextField;
    private JTextField        certLocalityTextField;
    private JTextField        certStateTextField;
    private JTextField        certCountryTextField;
    private JButton           certOperateButton;
    private JLabel            certKeyStoreSuffixLabel;

    @Autowired
    private SignatureViewAction signatureViewEvent;

    public SignatureView() {
        renderApkSignaturePanel();
        renderKeyStoreGeneratePanel();
        initEvent();
        initWatch();

        add(apkSignaturePanel);
        add(keystoreGeneratePanel);
        setLayout(new MigLayout());
    }

    private void renderApkSignaturePanel() {
        signatureApkLabel           = new JLabel("APK路径");
        outputApkPathLabel          = new JLabel("输出路径");
        selectKeystoreLabel         = new JLabel("选择秘钥");
        optionalLabel               = new JLabel("可选择项");
        operateBtnLabel             = new JLabel("操作按钮");
        signatureApkTextField       = new JTextField();
        outputApkPathTextField      = new JTextField();
        selectKeystoreComboBox      = new JComboBox<>();
        signAfterOpenDirCheckBox    = new JCheckBox("签名结束后打开所在目录");
        signAfterDelSrcFileCheckBox = new JCheckBox("签名结束后删除原APK文件");
        cleanButton                 = new JButton("清空");
        signatureButton             = new JButton("签名");
        apkSignaturePanel           = new JPanel(new MigLayout());
        signatureApkIconLabel       = new JLabel(ControlUtil.getSVGIcon("directory.svg"));
        outputApkPathIconLabel      = new JLabel(ControlUtil.getSVGIcon("directory.svg"));

        signatureApkTextField.setEditable(false);
        outputApkPathTextField.setEditable(false);
        signatureApkIconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        outputApkPathIconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signatureApkTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_COMPONENT, signatureApkIconLabel);
        outputApkPathTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_COMPONENT, outputApkPathIconLabel);


        apkSignaturePanel.setBorder(new TitledBorder("APK签名"));
        apkSignaturePanel.add(signatureApkLabel, "cell 0 0");
        apkSignaturePanel.add(signatureApkTextField, "cell 1 0, grow");
        apkSignaturePanel.add(outputApkPathLabel, "cell 0 1");
        apkSignaturePanel.add(outputApkPathTextField, "cell 1 1, grow");
        apkSignaturePanel.add(selectKeystoreLabel, "cell 0 2");
        apkSignaturePanel.add(selectKeystoreComboBox, "cell 1 2, grow");
        apkSignaturePanel.add(optionalLabel, "cell 0 3");
        apkSignaturePanel.add(signAfterOpenDirCheckBox, "cell 1 3");
        apkSignaturePanel.add(signAfterDelSrcFileCheckBox, "cell 1 3");
        apkSignaturePanel.add(operateBtnLabel, "cell 0 4");
        apkSignaturePanel.add(cleanButton, "cell 1 4, grow");
        apkSignaturePanel.add(signatureButton, "cell 1 4, grow");
    }

    private void renderKeyStoreGeneratePanel() {
        certAliasLabel                = new JLabel("证书别名");
        certKeyAlgLabel               = new JLabel("秘钥算法");
        certKeySizeLabel              = new JLabel("秘钥长度");
        certKeyStoreLabel             = new JLabel("文件名称");
        certKeyPassLabel              = new JLabel("秘钥密码");
        certStorePassLabel            = new JLabel("文件密码");
        certValidityLabel             = new JLabel("有效天数");
        certCommonNameLabel           = new JLabel("名称");
        certOrganizationUnitLabel     = new JLabel("单位");
        certOrganizationLabel         = new JLabel("组织");
        certLocalityLabel             = new JLabel("城市");
        certStateLabel                = new JLabel("省份");
        certCountryLabel              = new JLabel("国家");
        certOperateBtnLabel           = new JLabel("操作按钮");
        certAliasTextField            = new JTextField();
        certKeyAlgComboBox            = new JComboBox<>();
        certKeySizeTextField          = new JTextField();
        certKeyStoreTextField         = new JTextField();
        certKeyPassTextField          = new JTextField();
        certStorePassTextField        = new JTextField();
        certValidityTextField         = new JTextField();
        certCommonNameTextField       = new JTextField();
        certOrganizationUnitTextField = new JTextField();
        certOrganizationTextField     = new JTextField();
        certLocalityTextField         = new JTextField();
        certStateTextField            = new JTextField();
        certCountryTextField          = new JTextField();
        certOperateButton             = new JButton("生成证书");
        certKeyStoreSuffixLabel       = new JLabel(".keystore");
        keystoreGeneratePanel         = new JPanel(new MigLayout("", "[][300]"));

        certKeyAlgComboBox.addItem("RSA");
        certKeyAlgComboBox.addItem("DSA");

        certKeySizeTextField.setDocument(new NumberDocument());
        certValidityTextField.setDocument(new NumberDocument());
        certKeyStoreSuffixLabel.setForeground(Color.gray);
        certKeyStoreSuffixLabel.setBorder(new EmptyBorder(0, 0, 0, 5));

        certKeyStoreTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_COMPONENT, new JLabel(" .keystore "));

        keystoreGeneratePanel.setBorder(new TitledBorder("证书生成"));
        keystoreGeneratePanel.add(certAliasLabel, "cell 0 0");
        keystoreGeneratePanel.add(certKeyAlgLabel, "cell 0 1");
        keystoreGeneratePanel.add(certKeySizeLabel, "cell 0 2");
        keystoreGeneratePanel.add(certKeyStoreLabel, "cell 0 3");
        keystoreGeneratePanel.add(certKeyPassLabel, "cell 0 4");
        keystoreGeneratePanel.add(certStorePassLabel, "cell 0 5");
        keystoreGeneratePanel.add(certValidityLabel, "cell 0 6");
        keystoreGeneratePanel.add(certCommonNameLabel, "cell 0 7");
        keystoreGeneratePanel.add(certOrganizationUnitLabel, "cell 0 8");
        keystoreGeneratePanel.add(certOrganizationLabel, "cell 0 9");
        keystoreGeneratePanel.add(certLocalityLabel, "cell 0 10");
        keystoreGeneratePanel.add(certStateLabel, "cell 0 11");
        keystoreGeneratePanel.add(certCountryLabel, "cell 0 12");
        keystoreGeneratePanel.add(certOperateBtnLabel, "cell 0 13");
        keystoreGeneratePanel.add(certAliasTextField, "cell 1 0,grow");
        keystoreGeneratePanel.add(certKeyAlgComboBox, "cell 1 1,grow");
        keystoreGeneratePanel.add(certKeySizeTextField, "cell 1 2,grow");
        keystoreGeneratePanel.add(certKeyStoreTextField, "cell 1 3,grow");
        keystoreGeneratePanel.add(certKeyPassTextField, "cell 1 4,grow");
        keystoreGeneratePanel.add(certStorePassTextField, "cell 1 5,grow");
        keystoreGeneratePanel.add(certValidityTextField, "cell 1 6,grow");
        keystoreGeneratePanel.add(certCommonNameTextField, "cell 1 7,grow");
        keystoreGeneratePanel.add(certOrganizationUnitTextField, "cell 1 8,grow");
        keystoreGeneratePanel.add(certOrganizationTextField, "cell 1 9,grow");
        keystoreGeneratePanel.add(certLocalityTextField, "cell 1 10,grow");
        keystoreGeneratePanel.add(certStateTextField, "cell 1 11,grow");
        keystoreGeneratePanel.add(certCountryTextField, "cell 1 12,grow");
        keystoreGeneratePanel.add(certOperateButton, "cell 1 13,grow");
    }

    private void initEvent() {
        certOperateButton.addActionListener(e -> signatureViewEvent.certOperateButtonOnMouseClick(e));
        cleanButton.addActionListener(e -> signatureViewEvent.cleanButtonOnMouseClick(e));
        signatureButton.addActionListener(e -> signatureViewEvent.signButtonOnMouseClick(e));

        signatureApkIconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                signatureViewEvent.signApkIconLabelOnMouseClick(e);
            }
        });
        outputApkPathIconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                signatureViewEvent.signApkOutputIconLabelOnMouseClick(e);
            }
        });
    }

    private void initWatch() {
        // 初始化时加载一次
        loadKeyStoreToComboBox();

        WatchMonitor watchMonitor = WatchMonitor.create(new File(ResPath.CONFIG_DIR), WatchMonitor.ENTRY_CREATE);
        // 开启监听config目录
        watchMonitor.setMaxDepth(1);
        watchMonitor.setWatcher(new SimpleWatcher() {
            @Override
            public void onCreate(WatchEvent<?> watchEvent, Path path) {
                loadKeyStoreToComboBox();
            }

            @Override
            public void onModify(WatchEvent<?> watchEvent, Path path) {
                loadKeyStoreToComboBox();
            }

            @Override
            public void onDelete(WatchEvent<?> watchEvent, Path path) {
                loadKeyStoreToComboBox();
            }
        });
        watchMonitor.start();
    }

    private void loadKeyStoreToComboBox() {
        selectKeystoreComboBox.removeAllItems();
        for (File f : FileUtil.ls(ResPath.CONFIG_DIR)) {
            if (!f.getName().contains(".keystore")) continue;
            selectKeystoreComboBox.addItem(f.getName());
        }
    }
}