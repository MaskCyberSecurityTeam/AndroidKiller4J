package com.funnysec.richardtang.androidkiller4j.ui.wrapper;

import javafx.scene.image.Image;

/**
 * 扩展JavaFx的ImageView组件，提供了更丰富的构造函数。
 *
 * @author RichardTang
 */
public class ImageView extends javafx.scene.image.ImageView {

    /**
     * 创建ImgView，默认图片高度宽度为20。
     *
     * @param image 图片
     */
    public ImageView(Image image) {
        this(image, 20, 20);
    }

    /**
     * 创建ImgView，默认图片高度宽度为20。
     *
     * @param imagePath 图片路径
     */
    public ImageView(String imagePath) {
        this(new Image(imagePath), 20, 20);
    }

    /**
     * 创建ImgView
     *
     * @param imagePath   图片路径
     * @param imageWidth  图片宽度
     * @param imageHeight 图片高度
     */
    public ImageView(String imagePath, int imageWidth, int imageHeight) {
        this(new Image(imagePath), imageWidth, imageHeight);
    }

    /**
     * 创建ImgView
     *
     * @param img         图片
     * @param imageWidth  图片宽度
     * @param imageHeight 图片高度
     */
    public ImageView(Image img, int imageWidth, int imageHeight) {
        setImage(img);
        setFitWidth(imageWidth);
        setFitHeight(imageHeight);
    }

    /**
     * 根据Image创建ImageView
     *
     * @param image 图片
     * @return ImgView
     */
    public static ImageView instance(Image image) {
        return new ImageView(image);
    }
}
