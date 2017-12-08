package com.rainshieh.simplemvp.model.contract;

import com.rainshieh.simplemvp.interfaces.IModel;
import com.rainshieh.simplemvp.interfaces.IView;
import com.rainshieh.simplemvp.model.beans.GankItemBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * author: xiecong
 * create time: 2017/12/7 15:36
 * lastUpdate time: 2017/12/7 15:36
 */

public class SecondContract {
    public interface View extends IView{
        void refreshFaild(String msg);

        void loadMoreFaild(String msg);

        void setList(List<GankItemBean> list);

        void setMoreList(List<GankItemBean> list);
    }
    public interface Model extends IModel{

        Observable<List<GankItemBean>> getGirlList(int num_of_page,int currentPage);
    }
}
