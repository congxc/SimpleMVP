package com.rainshieh.simplemvp.interfaces;

import android.os.Bundle;

/**
 * author: xiecong
 * create time: 2017/12/5 14:51
 * lastUpdate time: 2017/12/5 14:51
 */
public interface IActivity {
    /**
     * 初始化方法
     *
     * @param savedInstanceState
     */
    void init(Bundle savedInstanceState);

    /**
     * 初始化view方法
     *
     * @param savedInstanceState
     */
    void initWidget(Bundle savedInstanceState);

    /**
     * 绑定事件
     */
    void bindEventListener();

    /**
     * @return 内容视图资源ID
     */
    int getLayoutResId();

    /**
     * @return toolBar资源ID
     */
    int getToolbarLayoutId();

    /**
     * 跳转  Activity
     */
    void launchActivity(Class<?> clazz);

    void launchActivity(Class<?> clazz, Bundle bundle);


    void showToast(String message);

    void showToast(int resId);
}
