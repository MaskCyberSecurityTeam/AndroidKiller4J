package com.richardtang.androidkiller4j.util;

import jadx.api.CommentsLevel;
import jadx.api.JadxArgs;
import jadx.api.JadxDecompiler;
import jadx.api.JavaClass;
import jadx.api.impl.NoOpCodeCache;

import java.io.File;

/**
 * 反编译工具类
 *
 * @author RichardTang
 */
public class JadxUtils {

    /**
     * 使用Jadx进行反编译
     *
     * @param srcDir 需要反编译的资源目录
     * @param dstDir 反编译后的结果输出目录
     */
    public static void decompile(File srcDir, File dstDir) {
        JadxArgs jadxArgs = new JadxArgs();
        jadxArgs.setDebugInfo(false);
        jadxArgs.setInputFile(srcDir);
        jadxArgs.setOutDirSrc(dstDir);
        jadxArgs.setCodeCache(new NoOpCodeCache());
        jadxArgs.setCommentsLevel(CommentsLevel.NONE);
        try (JadxDecompiler jadx = new JadxDecompiler(jadxArgs)) {
            jadx.load();
            jadx.save();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jadxArgs.close();
        }
    }

    public static String decompileClass(File classFile) {
        JadxArgs jadxArgs = new JadxArgs();
        jadxArgs.setDebugInfo(false);
        jadxArgs.setInputFile(classFile);
        jadxArgs.setCommentsLevel(CommentsLevel.NONE);
        jadxArgs.setCodeCache(new NoOpCodeCache());
        try (JadxDecompiler jadx = new JadxDecompiler(jadxArgs)) {
            jadx.load();
            for (JavaClass aClass : jadx.getClasses()) {
                return aClass.getCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jadxArgs.close();
        }
        return "反编译失败!";
    }

}
