package com.richardtang.androidkiller4j.validator;

import com.richardtang.androidkiller4j.MainWindow;

/**
 * 根据Title校验TaskView中的TabTitle
 *
 * @author RichardTang
 */
public class TabAddedValidator {

    public static boolean verify(String title) {
        return MainWindow.taskView.showTabByTitle(title);
    }
}