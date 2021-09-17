package com.funnysec.richardtang.androidkiller4j.pojo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.XmlUtil;
import com.funnysec.richardtang.androidkiller4j.config.ResourcePathConfig;
import lombok.Data;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.yaml.snakeyaml.Yaml;

import javax.xml.xpath.XPathConstants;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * 主要用来存储APK解包后从中读取的一些信息
 * 大部分数据的来源都是解析apktool.yml文件中的数据
 *
 * @author RichardTang
 */
@Data
public class Apk {

    // 项目APK解包后的根路径
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

    // APK解包的日期
    private Date decodeApkDate;

    private static final Yaml yaml = new Yaml();

    // APK中存储图标的路径根据铺面大小不同会分别存储在不同目录中
    private static final List<String> ICON_DIR = Arrays.asList(
            "mipmap-hdpi", "mipmap-mdpi", "mipmap-xhdpi", "mipmap-xxhdpi", "mipmap-xxxhdpi",
            "drawable-hdpi", "drawable-mdpi", "drawable-xhdpi", "drawable-xxhdpi", "drawable-xxxhdpi"
    );

    public Apk(String basePath) {
        this.basePath = basePath;
        initValue();
    }

    /**
     * 初始化APK中成员属性的值，主要是调用一些其他函数，注意有些是有调动顺序要求的，不要随便做修改。
     */
    public void initValue() {
        stringsDocument         = XmlUtil.readXML(basePath + "/res/values/strings.xml");
        androidManifestDocument = XmlUtil.readXML(basePath + "/AndroidManifest.xml");

        apktoolYmlMap = getApkToolYmlToMap();
        Map<String, String> sdkVersionMap = getSdkVersion();
        minSdkVersion    = sdkVersionMap.get("minSdkVersion");
        targetSdkVersion = sdkVersionMap.get("targetSdkVersion");
        fileName         = (String) apktoolYmlMap.get("apkFileName");

        iconPath        = getIconPath();
        mainActivity    = getMainActivity();
        packageName     = getPackageName();
        applicationName = getApplicationName();
        decodeApkDate   = getDecodeApkDate();

        activity       = getMainFestFileElesAttrValue("activity", "android:name");
        service        = getMainFestFileElesAttrValue("service", "android:name");
        receiver       = getMainFestFileElesAttrValue("receiver", "android:name");
        usesPermission = getMainFestFileElesAttrValue("uses-permission", "android:name");
    }

    /**
     * 获取Apk解包的时间，通过读取apktool生成的Yml文件来判断解包的时间。
     * 如果读取失败则以当前时间为准
     *
     * @return APK解包的时间
     */
    public Date getDecodeApkDate() {
        try {
            BasicFileAttributes attr = Files.getFileAttributeView(
                    Paths.get(basePath + "/apktool.yml"),
                    BasicFileAttributeView.class,
                    LinkOption.NOFOLLOW_LINKS
            ).readAttributes();
            return DateUtil.date(attr.creationTime().toMillis()).toJdkDate();
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }

    /**
     * 从string.xml文件中读取当前应用的名称
     *
     * @return 应用名称
     */
    public String getApplicationName() {
        return (String) XmlUtil.getByXPath(
                "/resources/string[@name='app_name']/text()",
                stringsDocument,
                XPathConstants.STRING
        );
    }

    /**
     * 从AndroidManifest.xml文件中读取应用包名
     *
     * @return 应用包名称
     */
    public String getPackageName() {
        return (String) XmlUtil.getByXPath(
                "/manifest/@package",
                androidManifestDocument,
                XPathConstants.STRING
        );
    }

    /**
     * 从AndroidManifest.xml文件中读取主Activity
     *
     * @return 主Activity名称
     */
    public String getMainActivity() {
        return (String) XmlUtil.getByXPath(
                "/manifest/application/activity[2]/@android:name",
                androidManifestDocument,
                XPathConstants.STRING
        );
    }

    /**
     * 先从AndroidManifest.xml文件中读取icon的名称
     * 随后在ICON_DIR下找到哪个文件夹中有该文件，并获取对应的路径。
     *
     * @return icon文件路径
     */
    public String getIconPath() {
        String iconFileName = ((String) XmlUtil
                .getByXPath("/manifest/application/@android:icon", androidManifestDocument, XPathConstants.STRING))
                .split("/")[1];

        for (String dir : ICON_DIR) {
            String tempIconPath = String.format("%s/res/%s/%s.png", basePath, dir, iconFileName);
            if (FileUtil.exist(tempIconPath)) {
                return tempIconPath;
            }
        }

        return ResourcePathConfig.IMAGE + "android.png";
    }

    /**
     * 将apktool.yml文件中的值转成map键值对
     *
     * @return apktool对应的map键值对
     */
    public Map<String, Object> getApkToolYmlToMap() {
        File apktoolFIle = new File(basePath + "/apktool.yml");
        if (FileUtil.isNotEmpty(apktoolFIle)) {
            try {
                return yaml.loadAs(new FileInputStream(apktoolFIle), Map.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new HashMap<>();
    }

    /**
     * 从apktool中获取minSdkVersion、targetSdkVersion的值
     *
     * @return minSdkVersion和targetSdkVersion的键值对
     */
    public Map<String, String> getSdkVersion() {
        if (this.apktoolYmlMap.size() <= 0) {
            return MapUtil.builder(new HashMap<String, String>())
                    .put("minSdkVersion", "0")
                    .put("targetSdkVersion", "0")
                    .build();
        }
        return (LinkedHashMap<String, String>) apktoolYmlMap.get("sdkInfo");
    }

    /**
     * 从AndroidManifest.xml文件中以给定的节点名称作为条件，获取对应属性的值，并存储到数组中。
     * foo: getMainFestFileElesAttrValue("activity", "android:name"); 获取所有Activity节点的android:name属性值。
     *
     * @param eleName  作为条件的节点名称
     * @param attrName 要获取的节点属性名称
     * @return 对应的属性值结果集合
     */
    public ArrayList<String> getMainFestFileElesAttrValue(String eleName, String attrName) {
        ArrayList<String> result   = new ArrayList<>();
        NodeList          nodeList = androidManifestDocument.getElementsByTagName(eleName);
        for (int i = 0; i < nodeList.getLength(); i++) {
            result.add(nodeList.item(i).getAttributes().getNamedItem(attrName).getNodeValue());
        }
        return result;
    }
}