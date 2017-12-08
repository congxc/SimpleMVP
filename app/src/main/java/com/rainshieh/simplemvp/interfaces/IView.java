package com.rainshieh.simplemvp.interfaces;

/**
 * author: xiecong
 * create time: 2017/12/5 14:19
 * lastUpdate time: 2017/12/5 14:19
 */
public interface IView {
    /**
     * 显示加载
     */

    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示信息
     */
     void showMessage(String message);

}

