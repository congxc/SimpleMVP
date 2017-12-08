package com.rainshieh.simplemvp.model.contract;

import com.rainshieh.simplemvp.interfaces.IModel;
import com.rainshieh.simplemvp.interfaces.IView;

import io.reactivex.Flowable;

/**
 * author: xiecong
 * create time: 2017/12/6 19:40
 * lastUpdate time: 2017/12/6 19:40
 * 通用IModel和IView
 * 可以为每个界面按照此类方式进行配置特定形式Model和View
 *
 */

public class CommonContract {
    public interface ICommonView<T> extends IView{
        /**
         * 给View设置数据 view刷新UI
         * @param data
         */
        void setData(T data);
    }
    public interface ICommonModel<T> extends IModel{
        /**
         * 同步获取数据
         * @return
         */
        T getData();

        /**
         * 异步获取数据可用此方法
         */
        Flowable<T> getDataFlowable();
    }

}
