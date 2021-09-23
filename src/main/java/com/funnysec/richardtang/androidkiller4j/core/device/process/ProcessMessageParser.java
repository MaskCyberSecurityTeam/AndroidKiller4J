package com.funnysec.richardtang.androidkiller4j.core.device.process;

import cn.hutool.core.util.StrUtil;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 解析ADB执行top命令的结果，将字符串转换为Java对象。
 * top命令的每一行为一个ProcessMessage(除了前边5行)，每一行的每一个项为ProcessMessage中对应的属性。
 * List<ProcessMessage>为每一次Top命令执行的结果。
 *
 * @author RichardTang
 */
class ProcessMessageParser {

    /**
     * 处理top命令执行完的结果
     *
     * @param lines top命令结果每一行为数组中的一个元素
     * @return 解析成ProcessMessage对象的结果集
     */
    @NonNull
    public List<ProcessMessage> processLogLines(@NonNull String[] lines) {
        List<ProcessMessage> messages = new ArrayList<>(lines.length);
        for (int i = 5; i < lines.length; i++) {
            String[]       split          = lines[i].split("\\s+");
            ProcessMessage processMessage = parserLineToProcessMessage(split);
            parserMultiArgs(split, processMessage);
            messages.add(processMessage);
        }
        return messages;
    }

    /**
     * 解析Top命令结果里的每一行(不包过前边5行)，封装成{@link ProcessMessage}对象。
     *
     * @param splitLine 数据行以\\s+进行切割后的数组值
     * @return {@link ProcessMessage}对象
     */
    private ProcessMessage parserLineToProcessMessage(String[] splitLine) {
        ProcessMessage processMessage = new ProcessMessage();
        processMessage.setPid(splitLine[0]);
        processMessage.setUser(splitLine[1]);
        processMessage.setPr(splitLine[2]);
        processMessage.setNi(splitLine[3]);
        processMessage.setVirt(splitLine[4]);
        processMessage.setRes(splitLine[5]);
        processMessage.setShr(splitLine[6]);
        processMessage.setS(splitLine[7]);
        processMessage.setCpu(splitLine[8]);
        processMessage.setMem(splitLine[9]);
        processMessage.setTime(splitLine[10]);
        return processMessage;
    }

    /**
     * 处理top命令中args多参数的情况
     *
     * @param split          以\\s+分隔的行数组
     * @param processMessage 处理完后需要存储到的ProcessMessage对象
     */
    private void parserMultiArgs(String[] split, ProcessMessage processMessage) {
        // 切割后大于11才是多参数情况
        if (split.length > 11) {
            return;
        }

        String[] strings = Arrays.copyOfRange(split, 11, split.length);
        String   join    = StrUtil.join(" ", strings);
        processMessage.setArgs(join);
    }
}
