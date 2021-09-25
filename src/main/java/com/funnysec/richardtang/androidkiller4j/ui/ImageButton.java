package com.funnysec.richardtang.androidkiller4j.ui;

import com.funnysec.richardtang.androidkiller4j.ui.wrapper.ImageView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * 图片按钮，注意这个和 {@link javafx.scene.control.Button}是不一样的。
 * ImageButton本质上是ImageView，只是当做按钮来使用。
 *
 * @author RichardTang
 */
public class ImageButton extends StackPane {

    // 显示的图片
    private ImageView imageView;

    // 白色背景
    private static final BackgroundFill WHITE = new BackgroundFill(
            Color.WHITE,
            new CornerRadii(0.2, true),
            new Insets(1, 1, 1, 1)
    );

    // 鼠标悬浮时显示的按键背景色
    private static final Background MOUSE_ENTERED_BACKGROUND_COLOR = new Background(WHITE);

    /**
     * 创建图片按钮，默认图片大小20。
     *
     * @param imagePath 图片路径
     */
    public ImageButton(String imagePath) {
        this(new Image(imagePath), 20, 20);
    }

    /**
     * 创建图片按钮，默认图片大小20。
     *
     * @param image 图片对象
     */
    public ImageButton(Image image) {
        this(image, 20, 20);
    }

    /**
     * 创建图片按钮
     *
     * @param imagePath   图片对象
     * @param imageWidth  图片宽度
     * @param imageHeight 图片高度
     */
    public ImageButton(String imagePath, int imageWidth, int imageHeight) {
        this(new Image(imagePath), imageWidth, imageHeight);
    }

    /**
     * 创建图片按钮
     *
     * @param image       图片对象
     * @param imageWidth  图片宽度
     * @param imageHeight 图片高度
     */
    public ImageButton(Image image, int imageWidth, int imageHeight) {
        setWidth(imageWidth);
        setHeight(imageHeight);
        setMaxWidth(imageWidth);
        setMaxHeight(imageHeight);
        setCursor(Cursor.HAND);
        setAlignment(Pos.CENTER);

        // 鼠标悬浮和移开时设定背景颜色
        setOnMouseEntered(event -> setBackground(MOUSE_ENTERED_BACKGROUND_COLOR));
        setOnMouseExited(event -> setBackground(Background.EMPTY));

        imageView = new ImageView(image, imageWidth, imageHeight);
        getChildren().add(imageView);
    }

    /**
     * 设置图片
     *
     * @param imagePath 图片路径
     */
    public void setImgView(String imagePath) {
        imageView.setImage(new Image(imagePath));
    }
}