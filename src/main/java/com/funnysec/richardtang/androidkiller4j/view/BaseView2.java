package com.funnysec.richardtang.androidkiller4j.view;

import cn.hutool.core.util.ReflectUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 所有View的基类，规定所有View类要有哪些基础函数。
 * 所有继承的子类都需要指定一个类型P，来作为根Pane，这个P类型实例对象由父构造函数进行创建。
 *
 * @param <P> 当前View中的根Pane类型
 * @author RichardTang
 */
public abstract class BaseView2<P> implements InitializingBean {

    // 根Pane类型
    private P view;

    /**
     * 构造函数中通过泛型反射，由父类统一创建P的具体类型，并赋值给view成员属性。
     */
    public BaseView2() {
        try {
            Class             clazz             = this.getClass();
            Type              type              = clazz.getGenericSuperclass();
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Class             viewClass         = (Class) parameterizedType.getActualTypeArguments()[0];
            view = (P) viewClass.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public BaseView2(Object... initargs) {
//
//    }


    /**
     * 获取根Pane对象
     *
     * @return 根Pane
     */
    public P getRootPane() {
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
     * <b>注意2: 如果未加{@link Component}则需要手动通过构造函数的方式进行调用，注意加载的顺序问题。</b>
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