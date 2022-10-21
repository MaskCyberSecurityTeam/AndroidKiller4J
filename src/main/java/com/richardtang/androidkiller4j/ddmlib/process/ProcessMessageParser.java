package com.richardtang.androidkiller4j.ddmlib.process;

import cn.hutool.core.util.StrUtil;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 解析android设备执行top命令的结果，将top命令的字符串结果转换为{@link ProcessMessage}对象。
 * top命令的每一行为一个{@link ProcessMessage}(除了前边5行)，每一行的每一个项为ProcessMessage中对应的属性。
 * 每一次运行/刷新top命令的显示都得到一个List<ProcessMessage>结果。
 *
 * @author RichardTang
 */
class ProcessMessageParser {

    // top命令每一行中的每一项以空格分隔
    private final String LINE_ITEM_SPACE_REGEX = "\\s+";

    /**
     * 处理top命令产生的字符串，封装成对应的集合。
     *
     * @param lines 这是一个存储了top命令多行字符串的数组
     * @return 解析成ProcessMessage对象集合
     */
    @NonNull
    public List<ProcessMessage> processLogLines(@NonNull String[] lines) {
        List<ProcessMessage> messages = new ArrayList<>(lines.length);
        // 前5行不进行处理因为数据为不同的格式
        for (int i = 5; i < lines.length; i++) {
            // 解析成对应的ProcessMessage对象
            messages.add(parserLineItemToProcessMessage(lines[i]));
        }
        return messages;
    }

    /**
     * 解析Top命令里每一行数据里的每一个项
     * 将这些项封装到{@link ProcessMessage}对象。
     *
     * @param line top命令里要处理的指定行数据
     * @return {@link ProcessMessage}对象
     */
    private ProcessMessage parserLineItemToProcessMessage(String line) {
        String[] items = line.trim().split(LINE_ITEM_SPACE_REGEX);

        ProcessMessage processMessage = new ProcessMessage();
        processMessage.setPid(items[0]);
        processMessage.setUser(items[1]);
        processMessage.setPr(items[2]);
        processMessage.setNi(items[3]);
        processMessage.setVirt(items[4]);
        processMessage.setRes(items[5]);
        processMessage.setShr(items[6]);
        processMessage.setS(items[7]);
        processMessage.setCpu(items[8]);
        processMessage.setMem(items[9]);
        processMessage.setTime(items[10]);

        // 切割后大于11的情况是程序存在多参数的情况
        if (items.length > 11) {
            String join = StrUtil.join(" ", Arrays.copyOfRange(items, 11, items.length));
            processMessage.setArgs(join);
        }

        return processMessage;
    }
}
