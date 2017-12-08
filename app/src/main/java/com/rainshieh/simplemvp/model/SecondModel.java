package com.rainshieh.simplemvp.model;

import com.rainshieh.simplemvp.base.BaseModel;
import com.rainshieh.simplemvp.model.beans.GankHttpResponse;
import com.rainshieh.simplemvp.model.beans.GankItemBean;
import com.rainshieh.simplemvp.model.contract.SecondContract;
import com.rainshieh.simplemvp.model.http.exception.NetException;
import com.rainshieh.simplemvp.model.net.RetrofitHelper;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * author: xiecong
 * create time: 2017/12/7 14:03
 * lastUpdate time: 2017/12/7 14:03
 */

public class SecondModel extends BaseModel implements SecondContract.Model{
    @Override
    public Observable<List<GankItemBean>> getGirlList(int num_of_page, int currentPage) {
        return RetrofitHelper.getGankApis().getGirlList(num_of_page,currentPage)
                .subscribeOn(Schedulers.io())
                .compose(handleGankResult());
    }
    private ObservableTransformer<GankHttpResponse<List<GankItemBean>>, List<GankItemBean>> handleGankResult() {   //compose判断结果
            return new ObservableTransformer<GankHttpResponse<List<GankItemBean>>, List<GankItemBean>>() {
                @Override
                public ObservableSource<List<GankItemBean>> apply(Observable<GankHttpResponse<List<GankItemBean>>> upstream) {
                    return upstream.flatMap(new Function<GankHttpResponse<List<GankItemBean>>, ObservableSource<List<GankItemBean>>>() {
                        @Override
                        public ObservableSource<List<GankItemBean>> apply(GankHttpResponse<List<GankItemBean>> tGankHttpResponse) throws Exception {
                            if(!tGankHttpResponse.getError()) {
                                return createData(tGankHttpResponse.getResults());
                            } else {
                                NetException exception = new NetException("服务器error");
                                return Observable.error(exception);
                            }
                        }
                    });
                }
            };
    }

    private ObservableSource<List<GankItemBean>> createData(final List<GankItemBean> results) {
        return Observable.create(new ObservableOnSubscribe<List<GankItemBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<GankItemBean>> e) throws Exception {
                try {
                    e.onNext(results);
                    e.onComplete();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    e.onError(e1);
                }
            }
        });
    }

}
