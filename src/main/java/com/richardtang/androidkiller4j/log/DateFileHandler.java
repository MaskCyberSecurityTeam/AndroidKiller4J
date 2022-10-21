package com.richardtang.androidkiller4j.log;

import cn.hutool.core.date.DateUtil;
import com.richardtang.androidkiller4j.constant.R;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;

/**
 * 定义日志已日期格式创建文件，因logging.properties文件中无法直接配置日期生成，所以此处通过代码实现。
 *
 * @author RichardTang
 */
public class DateFileHandler extends FileHandler {

    public DateFileHandler() throws IOException {
        super(R.LOGS_DIR + DateUtil.format(new Date(), "yyyy-MM-dd") + ".log");
    }
}
