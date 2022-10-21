package com.richardtang.androidkiller4j.ui.control;

import com.richardtang.androidkiller4j.ui.document.NumberDocument;

import javax.swing.*;

/**
 * 只能输入数字的TextField
 *
 * @author RichardTang
 */
public class NumberTextField extends JTextField {

    {
        setDocument(new NumberDocument());
    }
}