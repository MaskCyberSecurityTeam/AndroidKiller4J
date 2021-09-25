package com.funnysec.richardtang.androidkiller4j.ui;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

/**
 * 带有小组件按钮的SplitPane，该组件是固定的样式。
 * 小组件按钮指的是分割线处带有按钮的样式
 * <p>
 * 1. 该组件强制布局为VERTICAL，上下结构的SplitPane。
 * 2. 该组件只能存在一条Divider，也就是只有一条分割线。
 *
 * @author RichardTang
 */
public class GadgetSplitPane extends SplitPane {

    // SplitPane的上半部分
    private Region topContent;

    // SplitPane的下半部分
    private Region bottomContent;

    // 存储小组件按钮的容器
    private ToolBar fnToolBar = new ToolBar();

    // 放在SplitPane的下半部分的pane，用来实现fnToolBar和bottomContent部分的整合。
    private StackPane stackPane = new StackPane();

    // 用来实现将ToolBar中的按钮移动到右边的分隔符
    private final Pane rightSpacer = new Pane();

    // 需要借助CSS来调整SplitPane的分割线大小
    private static final String DEFAULT_STYLE_CLASS = "gadget-split-pane";

    // 展开状态下的箭头图片
    private static final String GADGET_SPLIT_PANE_UP_IMG_PATH = "image/gadget-split-pane-up.png";

    // 收缩状态下的箭头图片
    private static final String GADGET_SPLIT_PANE_DOWN_IMG_PATH = "image/gadget-split-pane-down.png";

    // 默认自带收缩按钮
    private ImageButton switchButton = new ImageButton(GADGET_SPLIT_PANE_DOWN_IMG_PATH);

    /**
     * 创建GadgetSplitPane
     * 不带初始化top和bottom，这可能会导致页面很丑陋。
     * 所以在调用不带参数的构造后，需要自行调用，setTopContent和setBottomContent。
     */
    public GadgetSplitPane() {
        super();
        initialize();
    }

    /**
     * 创建GadgetSplitPane
     * 注意这里的bottomContent要和Stage的高度相同，不然会导致分割线无法拖到底下。
     *
     * @param topContent    上半部分内容
     * @param bottomContent 下半部分内容
     * @param gadgetButton  小组件按钮
     */
    public GadgetSplitPane(Region topContent, Region bottomContent, ImageButton... gadgetButton) {
        super();
        initialize();
        setTopContent(topContent);
        setBottomContent(bottomContent);
        setGadgetButton(gadgetButton);
    }

    /**
     * 初始化组件
     */
    private void initialize() {
        HBox.setHgrow(rightSpacer, Priority.SOMETIMES);

        fnToolBar.setTranslateY(0);
        fnToolBar.getItems().addAll(rightSpacer);
        fnToolBar.setPadding(new Insets(1, 2, 2, 2));

        stackPane.setAlignment(Pos.TOP_CENTER);
        switchButton.setOnMouseClicked(new GadgetSplitPaneTouchExpandableEvent());

        setDividerPositions(0.8f, 0.2f);
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    /**
     * 设置SplitPane上半部分的内容
     *
     * @param topContent 上半部分要显示的组件内容
     */
    public void setTopContent(Region topContent) {
        this.topContent = topContent;
        getItems().addAll(this.topContent, stackPane);
    }

    /**
     * 设置SplitPane下半部分的内容
     *
     * @param bottomContent 下半部分要显示的组件内容
     */
    public void setBottomContent(Region bottomContent) {
        this.bottomContent = bottomContent;
        this.bottomContent.setMinHeight(0);
        this.bottomContent.setPadding(new Insets(20, 0, 0, 0));
        stackPane.getChildren().addAll(this.bottomContent, fnToolBar);
    }

    /**
     * 添加分割线处的功能按钮
     *
     * @param gadgetButton 需要添加的功能按钮
     */
    public void setGadgetButton(ImageButton... gadgetButton) {
        fnToolBar.getItems().addAll(gadgetButton);
        fnToolBar.getItems().add(switchButton);
    }

    /**
     * GadgetSplitPane组件折叠事件实现类
     */
    class GadgetSplitPaneTouchExpandableEvent implements EventHandler<MouseEvent> {

        // 记录bottomContent是否展开状态，true为展开，false为不展开。
        private boolean expanded = true;

        // 记录divider[0]的当前位置
        private float markDividerOldPosition = 0.0f;

        private GadgetSplitPaneTouchExpandableEvent() {

        }

        @Override
        public void handle(MouseEvent event) {
            // 注意这里在设置Position的时候只能以小数的方式进行设置0.1-1之间
            // 1代表最底部，0.1就代表最顶部
            if (expanded) {
                // getDividerPosition获取到的是double类型，这里强转成float即可。只保留1位小数。
                markDividerOldPosition = (float) getDividerPositions()[0];
                getDividers().get(0).setPosition(1);
                switchButton.setImgView(GADGET_SPLIT_PANE_UP_IMG_PATH);
            } else {
                getDividers().get(0).setPosition(markDividerOldPosition);
                switchButton.setImgView(GADGET_SPLIT_PANE_DOWN_IMG_PATH);
            }
            expanded = !expanded;
        }
    }
}