package com.rainshieh.simplemvp.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import com.rainshieh.simplemvp.interfaces.IModel;

/**
 * author: xiecong
 * create time: 2017/12/5 15:25
 * lastUpdate time: 2017/12/5 15:25
 */
public abstract class BaseModel implements IModel, LifecycleObserver {
    @Override
    public void onDestroy() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }
}

