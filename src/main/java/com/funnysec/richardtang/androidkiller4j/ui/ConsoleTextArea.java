package com.funnysec.richardtang.androidkiller4j.ui;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * 控制台文本域，可将指定的流数据输出到该TextArea中进行显示
 *
 * @author RichardTang
 */
public class ConsoleTextArea extends TextArea {

    private ConsoleTextAreaPrintStream consoleTextAreaPrintStream;

    public ConsoleTextArea() {
        setEditable(false);
        consoleTextAreaPrintStream = new ConsoleTextAreaPrintStream(this);
    }

    public class ConsoleTextAreaPrintStream extends PrintStream {

        private TextArea textArea;

        public ConsoleTextAreaPrintStream(TextArea textArea) {
            super(new ByteArrayOutputStream());
            this.textArea = textArea;
        }

        @Override
        public void write(byte[] buf, int off, int len) {
            print(new String(buf, off, len));
        }

        @Override
        public void print(String s) {
            Platform.runLater(() -> textArea.appendText(s));
        }
    }

    public ConsoleTextAreaPrintStream getConsoleTextAreaPrintStream() {
        return consoleTextAreaPrintStream;
    }
}