package com.rainshieh.simplemvp.interfaces.lifecycle;

import io.reactivex.subjects.Subject;

/**
 * author: xiecong
 * create time: 2017/12/5 14:25
 * lastUpdate time: 2017/12/5 14:25
 */
public interface Lifecycleable<T>{
    Subject<T> provideLifecycleSubject();
}
