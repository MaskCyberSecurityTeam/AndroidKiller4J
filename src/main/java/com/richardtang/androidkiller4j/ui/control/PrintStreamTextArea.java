package com.richardtang.androidkiller4j.ui.control;

import lombok.Data;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * 控制台文本域，可将指定的流数据输出到该JTextArea中进行显示
 *
 * @author RichardTang
 */
@Data
public class PrintStreamTextArea extends JTextArea {

    private PrintStream printStream = new PrintStream(new ByteArrayOutputStream()) {

        @Override
        public void write(byte[] buf, int off, int len) {
            print(new String(buf, off, len));
        }

        /**
         * 将数据追到到JTextArea的文本域中
         *
         * @param s 写入的字符
         */
        @Override
        public void print(String s) {
            SwingUtilities.invokeLater(() -> PrintStreamTextArea.this.append(s));
        }
    };

    public PrintStreamTextArea() {
        // 定义文本域不可编辑
        setEditable(false);
    }
}