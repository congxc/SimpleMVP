package com.rainshieh.simplemvp.interfaces;

/**
 * author: xiecong
 * create time: 2017/12/5 14:24
 * lastUpdate time: 2017/12/5 14:24
 */

public interface IPresenter {

    /**
     * 做一些初始化操作
     */
    void onStart();

    /**
     * 销毁
     */
    void onDestroy();
}

