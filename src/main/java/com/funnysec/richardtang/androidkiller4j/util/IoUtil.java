package com.funnysec.richardtang.androidkiller4j.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * IO工具
 *
 * @author RichardTang
 */
public class IoUtil {

    /**
     * 将inputStream中的流重定向到System.out中
     *
     * @param inputStream Process流
     */
    public static void inputStreamToSysOut(InputStream inputStream) {
        String         line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
}