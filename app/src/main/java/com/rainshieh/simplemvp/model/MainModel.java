package com.rainshieh.simplemvp.model;

import android.os.SystemClock;

import com.rainshieh.simplemvp.base.BaseModel;
import com.rainshieh.simplemvp.model.contract.CommonContract;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * author: xiecong
 * create time: 2017/12/6 19:40
 * lastUpdate time: 2017/12/6 19:40
 */

public class MainModel extends BaseModel implements CommonContract.ICommonModel<String>{
    @Override
    public String getData() {
        return "标题";
    }

    @Override
    public Flowable<String> getDataFlowable() {
        return Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                try {
                    // TODO: 2017/12/6 耗时操作
                    SystemClock.sleep(2000);
                    String title = "标题";
                    e.onNext(title);
                    e.onComplete();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    e.onError(e1);
                }
            }
        }, BackpressureStrategy.BUFFER);
    }
}
