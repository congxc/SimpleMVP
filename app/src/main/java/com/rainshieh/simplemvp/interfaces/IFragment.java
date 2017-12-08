package com.rainshieh.simplemvp.interfaces;

import android.os.Bundle;

/**
 * author: xiecong
 * create time: 2017/12/5 14:52
 * lastUpdate time: 2017/12/5 14:52
 */

public interface IFragment {
    /**
     * 初始化
     */
    void initWidget();

    /**
     * 绑定事件
     */
    void bindEventListener();

    /**
     * @return 内容视图资源ID
     */
    int getLayoutResId();

    /**
     * 初始化方法
     *
     * @param savedInstanceState
     */
    void init(Bundle savedInstanceState);

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onCreate还没执行
     * setData里却调用了presenter的方法时,是会报空的,因为dagger注入是在onCreated方法中执行的,然后才创建的presenter
     * 如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
     * @param data
     */
    //    void setData(Object data);

    /**
     * 跳转  Activity
     */
    void launchActivity(Class<?> clazz);

    void launchActivity(Class<?> clazz, Bundle bundle);


    void showToast(String message);

    void showToast(int resId);
}
