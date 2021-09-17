package com.funnysec.richardtang.androidkiller4j.view;

import com.funnysec.richardtang.androidkiller4j.pojo.Apk;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * View基类，规定所有View类要有哪些基础函数。 每个子类View都需要自己给view赋值，来初始化view的值，不然会出现空指针。
 * JavaFx中已Pane来进行布局，那么每个子View就需要指定一个根的Pane来作为当前的布局。
 *
 * @param <P> 当前View对外暴露的根Pane
 * @author RichardTang
 */
public abstract class BaseView<P> implements InitializingBean {

    // 这里的View其实就是Pane
    protected P view;

    public P getView() {
        return view;
    }

    /**
     * 初始化组件UI，主要就是new Ui()的操作。
     */
    protected abstract void initUi();

    /**
     * 初始化组件属性，设置高宽，颜色，大小等。
     */
    protected abstract void initAttr();

    /**
     * 初始化组件之间的关联关系
     */
    protected abstract void initLayout();

    /**
     * 初始化组件的事件
     */
    protected abstract void initEvent();

    /**
     * 初始化操作(initUi、initAttr、initLayout、initEvent)都做完后执行
     */
    protected abstract void initialize();

    /**
     * Spring容器中的Bean初始化完毕后调用该函数，该函数中依次调用UI、布局、事件的初始化函数。
     * <b>注意: 这个初始化函数只有将创建权交给Spring的类，才会调用该函数。就是加了{@link Component}的类</b>
     * <b>注意2: 如果未加{@link Component}则会通过构造函数的方式进行调用，注意加载的顺序问题。</b>
     */
    @Override
    public void afterPropertiesSet() {
        initUi();
        initAttr();
        initLayout();
        initEvent();
        initialize();
    }
}