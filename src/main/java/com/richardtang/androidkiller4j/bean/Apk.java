package com.richardtang.androidkiller4j.bean;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.richardtang.androidkiller4j.constant.Size;
import com.richardtang.androidkiller4j.constant.Suffix;
import com.richardtang.androidkiller4j.util.*;
import lombok.Data;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.xpath.XPathConstants;
import java.util.*;

/**
 * 主要用来存储APK解包后从中读取的一些信息
 * 大部分数据的来源都是解析apktool.yml文件中的数据
 *
 * @author RichardTang
 */
@Data
@SuppressWarnings("all")
public class Apk {

    // APK解包后的根路径
    private String basePath;
    // APK对应的ICON图标路径
    private String iconPath;
    // APK文件名（在回编译时用上)
    private String fileName;
    // 包名
    private String packageName;
    // 主Activity
    private String mainActivity;
    // 应用名称
    private String applicationName;
    // 目标sdk
    private String targetSdkVersion;
    // 最小版本sdk
    private String minSdkVersion;
    // APK解包的日期
    private Date   decodeApkDate;

    // string.xml文档对象
    private Document            stringsDocument;
    // AndroidManifest.xml文档对象
    private Document            androidManifestDocument;
    // apktool.yml文件的键值对
    private Map<String, Object> apktoolYmlMap;

    // 当前APK应用所有的Activity组件值
    private List<String> activity;
    // 当前APK应用所有的Service组件值
    private List<String> service;
    // 当前APK应用所有的Receiver组件值
    private List<String> receiver;
    // 当前APK应用所有的UsesPermission组件值
    private List<String> usesPermission;

    // 数据来源文件
    private final String APKTOOL_YML               = "/apktool.yml";
    private final String STRINGS_DOCUMENT          = "/res/values/strings.xml";
    private final String ANDROID_MANIFEST_DOCUMENT = "/AndroidManifest.xml";

    // XPath
    private final String APP_NAME_XPATH     = "/resources/string[@name='app_name']/text()";
    private final String APP_ICON_XPATH     = "/manifest/application/@android:icon";
    private final String PACKAGE_NAME_XPATH = "/manifest/@package";

    // 入口类
    private final String MAIN_ACTIVITY_XPATH = "/manifest/application/activity/intent-filter/action[@android:name=\"android.intent.action.MAIN\"]/../../@android:name";

    // APK中存储图标的路径根据屏幕大小不同会分别存储在不同目录中
    private final List<String> ICON_DIR = Arrays.asList("mipmap-hdpi", "mipmap-mdpi", "mipmap-xhdpi", "mipmap-xxhdpi", "mipmap-xxxhdpi", "drawable-hdpi", "drawable-mdpi", "drawable-xhdpi", "drawable-xxhdpi", "drawable-xxxhdpi");

    public Apk(String basePath) {
        this.basePath = basePath;
        apktoolYmlMap = YamlUtils.fileToMap(basePath + APKTOOL_YML);
        stringsDocument = XmlUtil.readXML(basePath + STRINGS_DOCUMENT);
        androidManifestDocument = XmlUtil.readXML(basePath + ANDROID_MANIFEST_DOCUMENT);
    }

    /**
     * 以apktool.yml文件的创建时间作为apk解包的时间。
     *
     * @return APK解包的时间
     */
    public Date getDecodeApkDate() {
        if (decodeApkDate == null) {
            decodeApkDate = FileUtils.creationTime(basePath + APKTOOL_YML);
        }
        return decodeApkDate;
    }

    /**
     * 从string.xml文件中读取当前应用的名称
     *
     * @return 应用名称
     */
    public String getApplicationName() {
        if (StrUtil.isEmpty(applicationName)) {
            applicationName = getDocByXPathStrVal(APP_NAME_XPATH);
        }
        return applicationName;
    }

    /**
     * 从AndroidManifest.xml文件中读取应用包名
     *
     * @return 应用包名称
     */
    public String getPackageName() {
        if (StrUtil.isEmpty(packageName)) {
            packageName = getDocByXPathStrVal(PACKAGE_NAME_XPATH);
        }
        return packageName;
    }

    /**
     * 从apktool.yml文件中读取文件名
     *
     * @return apk文件名
     */
    public String getFileName() {
        if (StrUtil.isEmpty(fileName)) {
            fileName = (String) apktoolYmlMap.get("apkFileName");
        }
        return fileName;
    }

    /**
     * 获取应用名称，如: XXX.apk 输出为 XXX。
     *
     * @return 获取应用的简单名称
     */
    public String getFileSimpleName() {
        return getFileName().replace(Suffix.POINT_APK, "");
    }

    /**
     * 从AndroidManifest.xml文件中读取主Activity
     *
     * @return 主Activity名称
     */
    public String getMainActivity() {
        if (StrUtil.isNotEmpty(mainActivity)) {
            return mainActivity;
        }
        return getDocByXPathStrVal(MAIN_ACTIVITY_XPATH);
    }

    /**
     * 先从AndroidManifest.xml文件中读取icon的名称
     * 随后在ICON_DIR下找到哪个文件夹中有该文件，并获取对应的路径。
     *
     * @return icon文件路径
     */
    public String getIconPath() {
        if (StrUtil.isNotEmpty(iconPath)) {
            return iconPath;
        }

        // 查找图标
        for (String dir : ICON_DIR) {
            String iconName = getDocByXPathStrVal(APP_ICON_XPATH).split("/")[1];
            String tempIconPath = String.format("%s/res/%s/%s.png", basePath, dir, iconName);

            if (FileUtil.exist(tempIconPath)) {
                // 图标存在情况下
                iconPath = tempIconPath;
                return iconPath;
            }
        }

        // 图标文件不存在的情况
        iconPath = "apk.svg";
        return iconPath;
    }

    /**
     * 根据IconPath来返回一个ImageIcon对象。
     *
     * @return Icon图标对象
     */
    public Icon getImageIcon() {
        return getImageIcon(Size.BIG);
    }

    /**
     * 根据IconPath来返回一个ImageIcon对象。
     *
     * @param wh 图片宽度高度
     * @return Icon图标对象
     */
    public Icon getImageIcon(int wh) {
        String iconPath = getIconPath();

        // Apk未带图标,使用默认图标。
        if (iconPath.contains("apk.svg")) {
            return ControlUtils.getSVGIcon(iconPath, wh, wh);
        } else {
            // 带图标情况下
            return ControlUtils.getImageIcon(iconPath, wh);
        }
    }

    /**
     * 根据IconPath来返回一个JLabel包装的ImageIcon对象。
     *
     * @param wh 图片宽度高度
     * @return JLabel包装的ImageIcon对象。
     */
    public JLabel getImageIconLabel(int wh) {
        return new JLabel(getImageIcon(70));
    }

    /**
     * 获取Apk中的最小SDK信息
     *
     * @return 最小SDK
     */
    public String getMinSdkVersion() {
        if (StrUtil.isEmpty(minSdkVersion)) {
            Object sdkInfo = apktoolYmlMap.get("sdkInfo");
            // 如果未读取到SDK信息，则返回0。
            minSdkVersion = sdkInfo != null ? ((HashMap<String, String>) sdkInfo).get("minSdkVersion") : "0";
        }
        return minSdkVersion;
    }

    /**
     * 获取Apk中的目标SDK信息
     *
     * @return 目标SDK
     */
    public String getTargetSdkVersion() {
        if (StrUtil.isEmpty(targetSdkVersion)) {
            Object sdkInfo = apktoolYmlMap.get("sdkInfo");
            // 如果未读取到SDK信息，则返回0。
            targetSdkVersion = sdkInfo != null ? ((HashMap<String, String>) sdkInfo).get("targetSdkVersion") : "0";
        }
        return targetSdkVersion;
    }

    /**
     * 从AndroidManifest.xml文件中读取Activity节点数据
     *
     * @return Activity节点数据
     */
    public List<String> getActivity() {
        if (CollectionUtil.isEmpty(activity)) {
            activity = getMainFestFileElesAttrValue("activity", "android:name");
        }
        return activity;
    }

    /**
     * 从AndroidManifest.xml文件中读取Service节点数据
     *
     * @return Service节点数据
     */
    public List<String> getService() {
        if (CollectionUtil.isEmpty(service)) {
            service = getMainFestFileElesAttrValue("service", "android:name");
        }
        return service;
    }

    /**
     * 从AndroidManifest.xml文件中读取Receiver节点数据
     *
     * @return Receiver节点数据
     */
    public List<String> getReceiver() {
        if (CollectionUtil.isEmpty(receiver)) {
            receiver = getMainFestFileElesAttrValue("receiver", "android:name");
        }
        return receiver;
    }

    /**
     * 从AndroidManifest.xml文件中读取UsesPermission节点数据
     *
     * @return UsesPermission节点数据
     */
    public List<String> getUsesPermission() {
        if (CollectionUtil.isEmpty(usesPermission)) {
            usesPermission = getMainFestFileElesAttrValue("uses-permission", "android:name");
        }
        return usesPermission;
    }

    /**
     * 从AndroidManifest.xml文件中以给定的节点名称作为条件，获取对应属性的值，并存储到数组中。
     * foo:
     * getMainFestFileElesAttrValue("activity", "android:name");
     * 获取所有Activity节点的android:name属性值。
     *
     * @param eleName  作为条件的节点名称
     * @param attrName 要获取的节点属性名称
     * @return 对应的属性值结果集合
     */
    public ArrayList<String> getMainFestFileElesAttrValue(String eleName, String attrName) {
        ArrayList<String> result = new ArrayList<>();
        NodeList nodeList = androidManifestDocument.getElementsByTagName(eleName);
        for (int i = 0; i < nodeList.getLength(); i++) {
            result.add(nodeList.item(i).getAttributes().getNamedItem(attrName).getNodeValue());
        }
        return result;
    }

    /**
     * 根据xpath值从从AndroidManifestDocument和StringsDocument中获取字符串数据值
     *
     * @param xpath xpath
     * @return 对应获取到的数据值
     */
    private String getDocByXPathStrVal(String xpath) {
        String val = (String) XmlUtil.getByXPath(xpath, androidManifestDocument, XPathConstants.STRING);
        if (StrUtil.isEmpty(val)) {
            return (String) XmlUtil.getByXPath(xpath, stringsDocument, XPathConstants.STRING);
        }
        return val;
    }
}