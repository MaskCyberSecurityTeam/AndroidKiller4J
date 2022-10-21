package com.richardtang.androidkiller4j.view;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.swing.DesktopUtil;
import cn.hutool.core.util.StrUtil;
import com.formdev.flatlaf.FlatClientProperties;
import com.richardtang.androidkiller4j.constant.Cert;
import com.richardtang.androidkiller4j.constant.R;
import com.richardtang.androidkiller4j.constant.SvgName;
import com.richardtang.androidkiller4j.task.command.ApkSignatureTask;
import com.richardtang.androidkiller4j.task.command.CertGenerateTask;
import com.richardtang.androidkiller4j.task.watch.CertFileWatchTask;
import com.richardtang.androidkiller4j.ui.action.ClickAction;
import com.richardtang.androidkiller4j.ui.action.ClickActionInstaller;
import com.richardtang.androidkiller4j.ui.action.ClickActionType;
import com.richardtang.androidkiller4j.ui.control.NumberTextField;
import com.richardtang.androidkiller4j.util.ControlUtils;
import com.richardtang.androidkiller4j.validator.component.TextFieldVerifier;
import lombok.Data;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;

@Data
public final class SignatureView extends JPanel {

    // apk签名
    private final JPanel            apkSignaturePanel           = new JPanel(new MigLayout());
    private final JLabel            signatureApkLabel           = new JLabel("APK路径");
    private final JLabel            outputApkPathLabel          = new JLabel("输出路径");
    private final JLabel            selectKeystoreLabel         = new JLabel("选择秘钥");
    private final JLabel            optionalLabel               = new JLabel("可选择项");
    private final JLabel            operateBtnLabel             = new JLabel("操作按钮");
    private final JTextField        signatureApkTextField       = new JTextField();
    private final JTextField        outputApkPathTextField      = new JTextField();
    private final JComboBox<String> selectKeystoreComboBox      = new JComboBox<>();
    private final JCheckBox         signAfterOpenDirCheckBox    = new JCheckBox("签名结束后打开所在目录");
    private final JCheckBox         signAfterDelSrcFileCheckBox = new JCheckBox("签名结束后删除原APK文件");
    private final JButton           cleanButton                 = new JButton("清空");
    private final JButton           signatureButton             = new JButton("签名");
    private final JLabel            signatureApkIconLabel       = ControlUtils.getLabelIcon(SvgName.DIRECTORY);
    private final JLabel            outputApkPathIconLabel      = ControlUtils.getLabelIcon(SvgName.DIRECTORY);

    // 证书生成
    private final JPanel            keystoreGeneratePanel         = new JPanel(new MigLayout("", "[][300]"));
    private final JLabel            certKeyAlgLabel               = new JLabel("秘钥算法");
    private final JLabel            certKeySizeLabel              = new JLabel("秘钥长度");
    private final JLabel            certKeyStoreLabel             = new JLabel("文件名称");
    private final JLabel            certValidityLabel             = new JLabel("有效天数");
    private final JLabel            certCommonNameLabel           = new JLabel("名称");
    private final JLabel            certOrganizationUnitLabel     = new JLabel("单位");
    private final JLabel            certOrganizationLabel         = new JLabel("组织");
    private final JLabel            certCityLabel                 = new JLabel("城市");
    private final JLabel            certProvinceLabel             = new JLabel("省份");
    private final JLabel            certCountryLabel              = new JLabel("国家");
    private final JLabel            certOperateBtnLabel           = new JLabel("操作按钮");
    private final JComboBox<String> certKeyAlgComboBox            = new JComboBox<>();
    private final JComboBox<String> certKeySizeComboBox           = new JComboBox<>();
    private final JTextField        certKeyStoreTextField         = new JTextField();
    private final JTextField        certValidityTextField         = new NumberTextField();
    private final JTextField        certCommonNameTextField       = new JTextField();
    private final JTextField        certOrganizationUnitTextField = new JTextField();
    private final JTextField        certOrganizationTextField     = new JTextField();
    private final JTextField        certCityTextField             = new JTextField();
    private final JTextField        certProvinceTextField         = new JTextField();
    private final JTextField        certCountryTextField          = new JTextField();
    private final JButton           certOperateButton             = new JButton("生成证书");
    private final JLabel            certKeyStoreSuffixLabel       = new JLabel(Cert.CERT_FILE_SUFFIX);

    // 监测证书创建
    private final CertFileWatchTask certFileWatchTask = new CertFileWatchTask(selectKeystoreComboBox);

    public SignatureView() {
        // apk签名面板
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
        apkSignaturePanel.add(signatureButton, "cell 1 4, grow");
        apkSignaturePanel.add(cleanButton, "cell 1 4, grow");

        // 密钥长度选项
        certKeySizeComboBox.addItem("1024");
        certKeySizeComboBox.addItem("2048");
        certKeySizeComboBox.setSelectedIndex(0);

        // 加密类型选项
        certKeyAlgComboBox.addItem("RSA");
        certKeyAlgComboBox.addItem("DSA");
        certKeyAlgComboBox.setSelectedIndex(0);

        // 证书生成面板
        certKeyStoreSuffixLabel.setForeground(Color.gray);
        certKeyStoreSuffixLabel.setBorder(new EmptyBorder(0, 0, 0, 5));
        certKeyStoreTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_COMPONENT, new JLabel(".keystore "));
        keystoreGeneratePanel.setBorder(new TitledBorder("证书生成"));
        keystoreGeneratePanel.add(certKeyAlgLabel, "cell 0 0");
        keystoreGeneratePanel.add(certKeySizeLabel, "cell 0 1");
        keystoreGeneratePanel.add(certKeyStoreLabel, "cell 0 2");
        keystoreGeneratePanel.add(certValidityLabel, "cell 0 3");
        keystoreGeneratePanel.add(certCommonNameLabel, "cell 0 4");
        keystoreGeneratePanel.add(certOrganizationUnitLabel, "cell 0 5");
        keystoreGeneratePanel.add(certOrganizationLabel, "cell 0 6");
        keystoreGeneratePanel.add(certCityLabel, "cell 0 7");
        keystoreGeneratePanel.add(certProvinceLabel, "cell 0 8");
        keystoreGeneratePanel.add(certCountryLabel, "cell 0 9");
        keystoreGeneratePanel.add(certOperateBtnLabel, "cell 0 10");
        keystoreGeneratePanel.add(certKeyAlgComboBox, "cell 1 0,grow");
        keystoreGeneratePanel.add(certKeySizeComboBox, "cell 1 1,grow");
        keystoreGeneratePanel.add(certKeyStoreTextField, "cell 1 2,grow");
        keystoreGeneratePanel.add(certValidityTextField, "cell 1 3,grow");
        keystoreGeneratePanel.add(certCommonNameTextField, "cell 1 4,grow");
        keystoreGeneratePanel.add(certOrganizationUnitTextField, "cell 1 5,grow");
        keystoreGeneratePanel.add(certOrganizationTextField, "cell 1 6,grow");
        keystoreGeneratePanel.add(certCityTextField, "cell 1 7,grow");
        keystoreGeneratePanel.add(certProvinceTextField, "cell 1 8,grow");
        keystoreGeneratePanel.add(certCountryTextField, "cell 1 9,grow");
        keystoreGeneratePanel.add(certOperateButton, "cell 1 10,grow");

        // 自动绑定事件
        ClickActionInstaller.bind(this);

        // 组件设置默认值
        certValidityTextField.setText("365");
        certCommonNameTextField.setText("CN");
        certOrganizationUnitTextField.setText("Unit");
        certOrganizationTextField.setText("Organization");
        certCityTextField.setText("City");
        certProvinceTextField.setText("Province");
        certCountryTextField.setText("Country");

        // 参数校验
        TextFieldVerifier.verify(certKeyStoreTextField, "文件名称不能为空!", jTextField -> StrUtil.isNotEmpty(jTextField.getText()));
        TextFieldVerifier.verify(certValidityTextField, "有效天数不能小于0天！", jTextField -> Integer.parseInt(jTextField.getText()) > 0);
        TextFieldVerifier.verify(certCommonNameTextField, "名称不能为空!", jTextField -> StrUtil.isNotEmpty(jTextField.getText()));
        TextFieldVerifier.verify(certOrganizationUnitTextField, "单位不能为空!", jTextField -> StrUtil.isNotEmpty(jTextField.getText()));
        TextFieldVerifier.verify(certOrganizationTextField, "组织不能为空!", jTextField -> StrUtil.isNotEmpty(jTextField.getText()));
        TextFieldVerifier.verify(certCityTextField, "城市不能为空!", jTextField -> StrUtil.isNotEmpty(jTextField.getText()));
        TextFieldVerifier.verify(certProvinceTextField, "省份不能为空!", jTextField -> StrUtil.isNotEmpty(jTextField.getText()));
        TextFieldVerifier.verify(certCountryTextField, "国家不能为空!", jTextField -> StrUtil.isNotEmpty(jTextField.getText()));

        // 启动证书创建监测任务
        certFileWatchTask.start();

        // apk和签名面板添加至根Panel
        add(apkSignaturePanel);
        add(keystoreGeneratePanel);
    }

    /**
     * 生成证书按钮
     *
     * @param event 事件对象
     */
    @ClickAction("certOperateButton")
    public void certOperateButtonClick(ActionEvent event) {
        // 创建证书生成任务
        String keyStore = R.CONFIG_DIR + certKeyStoreTextField.getText() + Cert.CERT_FILE_SUFFIX;
        CertGenerateTask certGenerateTask = CertGenerateTask.builder().keyStore(keyStore).alias(Cert.CERT_PASS_ALIAS).keyPass(Cert.CERT_PASS_ALIAS).storePass(Cert.CERT_PASS_ALIAS).city(certCityTextField.getText()).country(certCountryTextField.getText()).province(certProvinceTextField.getText()).validity(certValidityTextField.getText()).commonName(certCommonNameTextField.getText()).organization(certOrganizationTextField.getText()).keyAlg((String) certKeyAlgComboBox.getSelectedItem()).keySize((String) certKeySizeComboBox.getSelectedItem()).organizationUnit(certOrganizationUnitTextField.getText()).build();

        // 生成证书
        certGenerateTask.setCallback(() -> {
            if (FileUtil.exist(keyStore)) {
                ControlUtils.showMsgDialog("提示信息", "创建证书成功");
            } else {
                ControlUtils.showMsgDialog("提示信息", "创建证书失败");
            }
        });
        SwingUtilities.invokeLater(certGenerateTask);
    }

    /**
     * 选择需要进行签名的apk
     *
     * @param e 事件对象
     */
    @ClickAction(value = "signatureApkIconLabel", type = ClickActionType.LabelComponent)
    public void signatureApkIconLabelClick(MouseEvent e) {
        File file = ControlUtils.chooserApkFileDialog();
        if (file != null) {
            signatureApkTextField.setText(file.getAbsolutePath());
        }
    }

    /**
     * 选择签名后的apk输出的路径
     *
     * @param e 事件对象
     */
    @ClickAction(value = "outputApkPathIconLabel", type = ClickActionType.LabelComponent)
    public void outputApkPathIconLabelClick(MouseEvent e) {
        File file = ControlUtils.chooserSaveDirectoryDialog();
        if (file != null) {
            outputApkPathTextField.setText(file.getAbsolutePath());
        }
    }

    /**
     * 清空用来签名的表单中的数据
     *
     * @param e 事件对象
     */
    @ClickAction("cleanButton")
    public void cleanButtonClick(ActionEvent e) {
        signatureApkTextField.setText("");
        outputApkPathTextField.setText("");
        signAfterOpenDirCheckBox.setSelected(false);
        signAfterDelSrcFileCheckBox.setSelected(false);
    }

    /**
     * 签名按钮
     *
     * @param e 事件对象
     */
    @ClickAction("signatureButton")
    public void signatureButtonClick(ActionEvent e) {
        File signApkFile = new File(signatureApkTextField.getText());
        String outputPath = outputApkPathTextField.getText();
        String signApkPath = String.format("%s" + "/sign_%s", outputPath, signApkFile.getName());
        ApkSignatureTask apkSignatureTask = new ApkSignatureTask(signApkFile.getAbsolutePath(), signApkPath, (String) selectKeystoreComboBox.getSelectedItem());

        apkSignatureTask.setCallback(() -> {
            if (!FileUtil.exist(outputPath)) {
                ControlUtils.showMsgDialog("提示信息", "签名失败");
            } else {
                ControlUtils.showMsgDialog("提示信息", "签名成功");
                if (signAfterDelSrcFileCheckBox.isSelected()) {
                    FileUtil.del(signApkFile.getAbsolutePath());
                }
                if (signAfterOpenDirCheckBox.isSelected()) {
                    DesktopUtil.open(new File(outputPath));
                }
            }
        });
        SwingUtilities.invokeLater(apkSignatureTask);
    }
}