package com.funnysec.richardtang.androidkiller4j.event;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.swing.DesktopUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RuntimeUtil;
import com.funnysec.richardtang.androidkiller4j.config.ResourcePathConfig;
import com.funnysec.richardtang.androidkiller4j.constant.FxConstant;
import com.funnysec.richardtang.androidkiller4j.task.ApkSignatureTask;
import com.funnysec.richardtang.androidkiller4j.util.FxUtil;
import com.funnysec.richardtang.androidkiller4j.view.SignatureView;
import com.funnysec.richardtang.androidkiller4j.view.TaskView;
import javafx.scene.input.MouseEvent;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import java.io.File;
import java.util.ArrayList;

@IocBean
public class SignatureViewEvent {

    @Inject
    private TaskView taskView;

    @Inject
    private SignatureView signatureView;

    /**
     * 拼接keytool的参数值
     * 简化command.append()的代码
     *
     * @param command StringBuilder
     * @param param   keytool参数名
     * @param value   keytool参数值
     */
    private void commandAppend(StringBuilder command, String param, String value) {
        if (!"".equals(value)) {
            command.append(param).append(" ").append(value).append(" ");
        }
    }

    /**
     * 简化构造-dname参数的值拼接
     *
     * @param dname ArrayList
     * @param param 对应-dname的CN、OU、O、L、ST、C
     * @param value 对应CN、OU、O、L、ST、C的=号值
     */
    private void dnameAppend(ArrayList<String> dname, String param, String value) {
        if (!"".equals(value)) {
            dname.add(param + value);
        }
    }

    /**
     * 生成证书按钮
     *
     * @param event 事件对象
     */
    public void certOperateButtonOnMouseClick(MouseEvent event) {
        String alias            = signatureView.getCertAliasTextField().getText();
        String keyAlg           = signatureView.getCertKeyAlgComboBox().getSelectionModel().getSelectedItem();
        String keySize          = signatureView.getCertKeySizeTextField().getText();
        String keyStore         = signatureView.getCertKeyStoreTextField().getText();
        String keyPass          = signatureView.getCertKeyPassTextField().getText();
        String storePass        = signatureView.getCertStorePassTextField().getText();
        String validity         = signatureView.getCertValidityTextField().getText();
        String commonName       = signatureView.getCertCommonNameTextField().getText();
        String organizationUnit = signatureView.getCertOrganizationUnitTextField().getText();
        String organization     = signatureView.getCertOrganizationTextField().getText();
        String locality         = signatureView.getCertLocalityTextField().getText();
        String state            = signatureView.getCertStateTextField().getText();
        String country          = signatureView.getCertCountryTextField().getText();

        // 拼接参数
        StringBuilder command = new StringBuilder("keytool -genkey -v ");
        commandAppend(command, "-alias", alias);
        commandAppend(command, "-keyalg", keyAlg);
        commandAppend(command, "-keysize", keySize);
        commandAppend(command, "-keypass", keyPass);
        commandAppend(command, "-storepass", storePass);
        commandAppend(command, "-validity", validity);
        commandAppend(command, "-keystore", ResourcePathConfig.CONFIG + keyStore + ".keystore");

        // 构造 -dname CN=xxx,OU=xxx,O=xxx,L=xxx,ST=xxx,C=xxx 的参数，注意不要加"号
        ArrayList<String> dname = new ArrayList<>();
        dnameAppend(dname, "CN=", commonName);
        dnameAppend(dname, "OU=", organizationUnit);
        dnameAppend(dname, "O=", organization);
        dnameAppend(dname, "L=", locality);
        dnameAppend(dname, "ST=", state);
        dnameAppend(dname, "C=", country);

        command.append("-dname").append(" ").append(CollUtil.join(dname, ","));
        String resultInfo = RuntimeUtil.execForStr(command.toString());
        System.out.println(resultInfo);

        if (FileUtil.exist(ResourcePathConfig.CONFIG + keyStore + ".keystore")) {
            FxUtil.alert("提示信息", "创建证书成功");
        } else {
            FxUtil.alert("提示信息", "创建证书失败");
        }
    }

    /**
     * 选择需要进行签名的apk
     *
     * @param e 事件对象
     */
    public void signApkGlyphButtonOnMouseClick(MouseEvent e) {
        File file = FxUtil.showApkFileChooser("选择APK", taskView.getRootPane().getScene().getWindow());
        if (file != null) {
            signatureView.getSignApkTextField().setText(file.getAbsolutePath());
        }
    }

    /**
     * 选择签名后的apk输出的路径
     *
     * @param e 事件对象
     */
    public void signApkOutputGlyphButtonOnMouseClick(MouseEvent e) {
        File file = FxUtil.showDialog("选择文件夹", taskView.getRootPane().getScene().getWindow());
        if (file != null) {
            signatureView.getSignApkOutputTextField().setText(file.getAbsolutePath());
        }
    }

    /**
     * 清空用来签名的表单中的数据
     *
     * @param e 事件对象
     */
    public void clearButtonOnMouseClick(MouseEvent e) {
        signatureView.getSignApkTextField().clear();
        signatureView.getSignApkOutputTextField().clear();
        signatureView.getOpenDirCheckBox().setSelected(false);
        signatureView.getDelSourceCheckBox().setSelected(false);
        signatureView.getSignCertComboBox().getSelectionModel().clearSelection();
    }

    /**
     * 签名按钮
     *
     * @param e 事件对象
     */
    public void signButtonOnMouseClick(MouseEvent e) {
        File    signApkFile   = new File(signatureView.getSignApkTextField().getText());
        String  outputPath    = signatureView.getSignApkOutputTextField().getText();
        String  keystore      = signatureView.getSignCertComboBox().getSelectionModel().getSelectedItem();
        boolean openDir       = signatureView.getOpenDirCheckBox().isSelected();
        boolean delSourceFile = signatureView.getDelSourceCheckBox().isSelected();

        ApkSignatureTask apkSignatureTask =
                new ApkSignatureTask(signApkFile.getAbsolutePath(), outputPath + "/sign_" + signApkFile.getName(), keystore);
        apkSignatureTask.setOnSucceeded(event -> {
            if (FileUtil.exist(outputPath)) {
                FxUtil.alert("提示信息", "签名成功");
                if (delSourceFile) {
                    FileUtil.del(signApkFile);
                }
                if (openDir) {
                    DesktopUtil.open(new File(outputPath));
                }
            } else {
                FxUtil.alert("提示信息", "签名失败");
            }
        });
        ThreadUtil.execAsync(apkSignatureTask);
    }
}