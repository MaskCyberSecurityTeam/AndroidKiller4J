package com.richardtang.androidkiller4j.util;

import cn.hutool.log.StaticLog;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

/**
 * RSyntaxTextArea组件工具类
 *
 * @author RichardTang
 */
public class RSyntaxTextAreaUtils {

    /**
     * 光标聚焦到指定位置
     *
     * @param rTextArea RTextArea组件
     * @param position  位置
     */
    public static void focusPosition(RTextArea rTextArea, int position) {
        rTextArea.setCaretPosition(position);
        rTextArea.setFocusable(true);
        rTextArea.requestFocus();
    }

    /**
     * 搜索关键字
     *
     * @param rTextArea RTextArea组件
     * @param keyword   关键字
     */
    public static void findKeyword(RTextArea rTextArea, String keyword) {
        SearchEngine.find(rTextArea, new SearchContext(keyword)).wasFound();
    }

    /**
     * 光标聚焦到指定行的开头
     *
     * @param line 行号
     */
    public static void focusLineStart(RTextArea rTextArea, int line) {
        try {
            int lineStartOffset = rTextArea.getLineStartOffset(line - 1);
            focusPosition(rTextArea, lineStartOffset);
        } catch (Exception e) {
            StaticLog.error(e);
        }
    }
}
