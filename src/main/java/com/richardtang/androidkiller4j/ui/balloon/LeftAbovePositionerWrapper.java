package com.richardtang.androidkiller4j.ui.balloon;

import net.java.balloontip.positioners.BasicBalloonTipPositioner;

import java.awt.*;

/**
 * 对{@link net.java.balloontip.positioners.LeftAbovePositioner}增加左边距功能
 * 代码基本上是和LeftAbovePositioner是一样的
 *
 * @author RichardTang
 */
public class LeftAbovePositionerWrapper extends BasicBalloonTipPositioner {

    private Integer leftMargin;

    public LeftAbovePositionerWrapper(Integer leftMargin) {
        super(10, 10);
        this.leftMargin = leftMargin;
    }

    public LeftAbovePositionerWrapper(int hO, int vO) {
        super(hO, vO);
    }

    @Override
    protected void determineLocation(Rectangle attached) {
        int balloonWidth = this.balloonTip.getPreferredSize().width;
        int balloonHeight = this.balloonTip.getPreferredSize().height;
        this.flipX = false;
        this.flipY = false;
        this.hOffset = this.preferredHorizontalOffset;
        if (this.fixedAttachLocation) {
            this.x = (new Float((float) attached.x + (float) attached.width * this.attachLocationX)).intValue() - this.hOffset;
            this.y = (new Float((float) attached.y + (float) attached.height * this.attachLocationY)).intValue() - balloonHeight;
        } else {
            this.x = attached.x;
            this.y = attached.y - balloonHeight;
        }

        if (this.orientationCorrection) {
            if (this.y < 0) {
                this.flipY = true;
                if (this.fixedAttachLocation) {
                    this.y += balloonHeight;
                } else {
                    this.y = attached.y + attached.height;
                }
            }

            if (this.x < 0) {
                this.flipX = true;
                if (this.fixedAttachLocation) {
                    this.x -= balloonWidth - 2 * this.hOffset;
                } else {
                    this.x = attached.x + attached.width - balloonWidth;
                }

                this.hOffset = balloonWidth - this.hOffset;
            }
        }

        this.x += leftMargin;

        if (this.offsetCorrection) {
            this.applyOffsetCorrection();
        }
    }
}
