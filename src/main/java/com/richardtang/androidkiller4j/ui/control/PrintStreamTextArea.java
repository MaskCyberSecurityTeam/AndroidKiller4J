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

    private PrintStream printStream;

    public PrintStreamTextArea() {
        setEditable(false);

        printStream = new PrintStream(new ByteArrayOutputStream()) {
            @Override
            public void write(byte[] buf, int off, int len) {
                print(new String(buf, off, len));
            }

            @Override
            public void print(String s) {
                SwingUtilities.invokeLater(() -> PrintStreamTextArea.this.append(s));
            }
        };
    }
}