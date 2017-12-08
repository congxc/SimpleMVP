package com.rainshieh.simplemvp.presenter;

import com.rainshieh.simplemvp.base.BasePresenter;
import com.rainshieh.simplemvp.model.contract.CommonContract;

import org.reactivestreams.Subscription;

import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * author: xiecong
 * create time: 2017/12/6 19:52
 * lastUpdate time: 2017/12/6 19:52
 */

public class MainPresenter extends BasePresenter<CommonContract.ICommonModel<String>,CommonContract.ICommonView<String>> {
    @Override
    protected void requestData() {
        //1.同步获取数据
//        String title = mModel.getData();
//        mRootView.setData(title);
        //2.异步获取数据
        mModel.getDataFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        mRootView.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FlowableSubscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(String s) {
                        mRootView.setData(s);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        mRootView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        mRootView.hideLoading();
                    }
                });
    }
}
