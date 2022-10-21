package com.richardtang.androidkiller4j.task.command;

import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.log.StaticLog;
import lombok.SneakyThrows;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 所有以命令行终端执行命令来处理的任务都继承该类
 * 并实现getCommand()函数，该函数中主要填写具体需要执行的命令。
 * 同时该父类中已经实现将命令执行的Process结果流导向到System.out中。
 *
 * @author RichardTang
 */
public abstract class TerminalCommandTask extends SwingWorker<Void, String> {

    private TerminalCommandCallback callback;

    /**
     * 处理的任务具体执行的命令
     *
     * @return 命令字符串
     */
    protected abstract String getCommand();

    public void setCallback(TerminalCommandCallback callback) {
        this.callback = callback;
    }

    @SneakyThrows
    @Override
    protected void done() {
        if (callback != null) {
            callback.apply();
        }
    }

    /**
     * 后台执行任务
     *
     * @return Void无任务执行结束后需要返回的数据
     */
    @Override
    protected Void doInBackground() {
        Process process = RuntimeUtil.exec(getCommand());
        try {
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = reader.readLine()) != null) {
                publish(line);
            }
        } catch (Exception e) {
            StaticLog.error(e);
        }
        return null;
    }

    /**
     * 将结果重定向到System.out中进行输出打印。
     *
     * @param chunks publish函数中接收到的数据
     */
    @Override
    protected void process(List<String> chunks) {
        super.process(chunks);
        chunks.forEach(StaticLog::info);
    }
}