package com.rainshieh.simplemvp.presenter;

import com.rainshieh.simplemvp.base.BasePresenter;
import com.rainshieh.simplemvp.model.beans.GankItemBean;
import com.rainshieh.simplemvp.model.contract.SecondContract;
import com.rainshieh.simplemvp.utils.RxLifecycleUtils;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * author: xiecong
 * create time: 2017/12/7 14:01
 * lastUpdate time: 2017/12/7 14:01
 */

public class SecondPresenter extends BasePresenter<SecondContract.Model,SecondContract.View> {
    public static final int NUM_OF_PAGE = 20;

    private int currentPage = 1;

    @Override
    protected void requestData() {
        onRefresh();
    }
    public void onRefresh() {
        currentPage = 1;
        LifecycleTransformer<List<GankItemBean>> transformer = RxLifecycleUtils.bindToLifecycle(mRootView);
        mModel.getGirlList(NUM_OF_PAGE,currentPage)
                .compose(transformer)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<GankItemBean>>() {
                    @Override
                    public void accept(List<GankItemBean> gankItemBeans) throws Exception {
                        mRootView.setList(gankItemBeans);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mRootView.showMessage("数据加载失败ヽ(≧Д≦)ノ");
                    }
                });
    }

    public void loadMore() {
        LifecycleTransformer<List<GankItemBean>> transformer = RxLifecycleUtils.bindToLifecycle(mRootView);
        mModel.getGirlList(NUM_OF_PAGE,++currentPage)
                .compose(transformer)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<GankItemBean>>() {
                    @Override
                    public void accept(List<GankItemBean> gankItemBeans) throws Exception {
                        mRootView.setMoreList(gankItemBeans);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mRootView.showMessage("加载更多数据失败ヽ(≧Д≦)ノ");
                    }
                });
    }

}
