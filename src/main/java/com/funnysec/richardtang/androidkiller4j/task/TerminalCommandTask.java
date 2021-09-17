package com.funnysec.richardtang.androidkiller4j.task;

import cn.hutool.core.util.RuntimeUtil;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 所有已命令行终端执行命令来处理的任务都继承该类
 * 并实现getCommand()函数，该函数中主要填写具体需要执行的命令。
 *
 * @author RichardTang
 */
public abstract class TerminalCommandTask extends Task<Process> {

    /**
     * 处理的任务具体执行的命令
     *
     * @return 命令字符串
     */
    protected abstract String getCommand();

    @Override
    protected Process call() throws Exception {
        Process process = RuntimeUtil.exec(getCommand());
        inputStreamToSysOut(process.getInputStream());
        return process;
    }

    /**
     * 将inputStream中的流重定向到System.out中
     *
     * @param inputStream Process流
     */
    private void inputStreamToSysOut(InputStream inputStream) {
        try {
            String         line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}