# 介绍

这是一款用JavaSwing编写的AndroidKiller，实现了原先AndroidKiller的相关功能，可运行在MacOS和Windows平台，在基础上增加了Cfr和Jadx对Smali的反编译。

# 使用

## MacOS

```bash
chmod 777
./startup.sh
```

## Windows

双击`startup.bat`脚本运行

# 效果图

## MacOS

![WX20221021-152753@2x](https://user-images.githubusercontent.com/30547741/197141553-15926a55-93d4-4f8a-94d0-63d8d19b6e0f.png)

![WX20221021-152857@2x](https://user-images.githubusercontent.com/30547741/197141616-cc483c16-4d15-498c-af1a-d727786edf4f.png)

![WX20221021-152928@2x](https://user-images.githubusercontent.com/30547741/197141642-6da4020a-48ca-476f-97dd-2875aa2ced2e.png)

![WX20221021-153050@2x](https://user-images.githubusercontent.com/30547741/197141652-96d91af3-08c4-4579-b559-2b9e64e08c05.png)

## Windows

![1666337697481](https://user-images.githubusercontent.com/30547741/197141796-87ee56a4-1f7a-4717-b12c-0c3840d0eda5.jpg)

![WX20221021-153546@2x](https://user-images.githubusercontent.com/30547741/197141818-9ae31f10-9db6-4838-aeba-aeccc5007d1e.png)

![WX20221021-153721@2x](https://user-images.githubusercontent.com/30547741/197141831-9455fa52-2d11-4a7f-8ff6-d4f13aa60a6e.png)

# 编译

安装好Jdk17，导入到IDEA后运行Maven的Install，会在target目录下生成编译后的工具文件夹。

![image](https://user-images.githubusercontent.com/30547741/197142225-f38cc861-929a-434f-ba86-eb2639f1ec84.png)

<img width="376" alt="image" src="https://user-images.githubusercontent.com/30547741/197142364-746c820b-dbaa-4dfe-a0bd-804b87145908.png">

通过jlink生成精简的jre

```bash
jlink --output jre  --compress=2 --no-header-files --no-man-pages --add-modules java.base,java.datatransfer,java.desktop,java.logging,java.xml,java.base,java.sql,jdk.unsupported,java.management,java.naming
```

将生成后的jre文件夹，放置工具文件夹中。

<img width="384" alt="image" src="https://user-images.githubusercontent.com/30547741/197142864-3ff3060d-8c00-4fb0-9acc-724dd12b5497.png">
