package com.richardtang.androidkiller4j.ui.tabframe;

import com.formdev.flatlaf.util.ColorFunctions;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * 组件边框的显示效果
 */
public class MLineBorder extends LineBorder {

    // 想起项目UI风格中边框的默认颜色
    public static Color defaultBorderColor;

    static {
        // 根据当前项目的UI风格取一个边框的默认颜色
        Color color = (Color) UIManager.getLookAndFeelDefaults().get("Panel.background");
        // 调整边框默认颜色的亮度
        defaultBorderColor = ColorFunctions.applyFunctions(color, new ColorFunctions.HSLChange(2, 85.0f));
    }

    private boolean left = true, right = true, top = true, bottom = true;

    /**
     * 返回一个灰色的边框
     */
    public MLineBorder() {
        super(Color.darkGray);
    }

    /**
     * 配置边框颜色
     *
     * @param color 颜色
     */
    public MLineBorder(Color color) {
        super(color);
    }

    /**
     * 配置边框颜色、厚度。
     *
     * @param color     颜色
     * @param thickness 厚度
     */
    public MLineBorder(Color color, int thickness) {
        super(color, thickness);
    }

    /**
     * 配置边框四条边、颜色、厚度。
     *
     * @param color     边框颜色
     * @param thickness 厚度
     * @param left      左边线
     * @param right     右边线
     * @param top       上边线
     * @param bottom    下边线
     */
    public MLineBorder(Color color, int thickness, boolean left, boolean right, boolean top, boolean bottom) {
        super(color, thickness);
        this.left   = left;
        this.right  = right;
        this.bottom = bottom;
        this.top    = top;
    }

    /**
     * 配置边框四条边、厚度，使用项目默认颜色作为边框颜色。
     *
     * @param thickness 厚度
     * @param left      左边线
     * @param right     右边线
     * @param top       上边线
     * @param bottom    下边线
     */
    public MLineBorder(int thickness, boolean left, boolean right, boolean top, boolean bottom) {
        super(defaultBorderColor, thickness);
        this.left   = left;
        this.right  = right;
        this.bottom = bottom;
        this.top    = top;
    }

    public void setInsets(boolean left, boolean right, boolean top, boolean bottom) {
        this.left   = left;
        this.right  = right;
        this.bottom = bottom;
        this.top    = top;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if ((this.thickness > 0) && (g instanceof Graphics2D)) {
            Graphics2D g2d      = (Graphics2D) g;
            Color      oldColor = g2d.getColor();
            g2d.setColor(this.lineColor);
            if (left) {
                g2d.drawLine(x, y, x, y + height);
            }
            if (top) {
                g2d.drawLine(x, y, x + width, y);
            }
            if (right) {
                g2d.drawLine(x + width, y, x + width, y + height);
            }
            if (bottom) {
                g2d.drawLine(x, y + height, x + width, y + height);
            }
            g2d.setColor(oldColor);
        }
    }
}
