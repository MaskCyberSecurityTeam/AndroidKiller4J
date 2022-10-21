package com.richardtang.androidkiller4j.ui.document;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Java11之后该类被设置为了private类，这里单独提取出来使用。
 *
 * @author RichardTang
 */
public class NumberDocument extends PlainDocument {

    public void insertString(int offs, String str, AttributeSet atts) throws BadLocationException {
        if (Character.isDigit(str.charAt(0))) {
            super.insertString(offs, str, atts);
        }
    }
}