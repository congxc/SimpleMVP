package com.rainshieh.simplemvp.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rainshieh.simplemvp.interfaces.IFragment;
import com.rainshieh.simplemvp.interfaces.IModel;
import com.rainshieh.simplemvp.interfaces.IView;
import com.rainshieh.simplemvp.interfaces.lifecycle.FragmentLifecycleable;
import com.rainshieh.simplemvp.utils.LogUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * author: xiecong
 * create time: 2017/12/5 15:23
 * lastUpdate time: 2017/12/5 15:23
 */

public abstract class BaseFragment<P extends BasePresenter, M extends IModel> extends Fragment
        implements IFragment, FragmentLifecycleable {
    protected P mPresenter;
    protected M mModel;
    private Unbinder mUnbinder;
    private BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();
    private long lastClickTime;

    @Override
    public Subject<FragmentEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLifecycleSubject.onNext(FragmentEvent.CREATE);
        int layoutResId = getLayoutResId();
        View rootView = null;
        if (layoutResId != 0) {
            rootView = inflater.inflate(layoutResId,container,false);
            mUnbinder = ButterKnife.bind(this,rootView);
        }
        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();
        mLifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
        mLifecycleSubject.onNext(FragmentEvent.PAUSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        mLifecycleSubject.onNext(FragmentEvent.STOP);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(savedInstanceState);
        initWidget();
        bindEventListener();
    }

    @Override
    public void init( Bundle savedInstanceState) {

        //初始化presenter 和 model
        try {
            Class<P> presenterClass = getPresenterClass();
            if (presenterClass != null) {
                mPresenter = presenterClass.newInstance();
            }else {
                LogUtils.error("TAGxc" + getClass().getCanonicalName(), " presenterClass is null" );
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("create presenter error");
        }
        try {
            Class<M> modelClass = getModelClass();
            if (modelClass != null) {
                mModel = modelClass.newInstance();
            }else {
                LogUtils.error("TAGxc"+getClass().getCanonicalName(), " modelClass  is null" );
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("create model error");
        }
        if (mPresenter != null && this instanceof IView) {
            mPresenter.attachVM((IView) this, mModel);
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mLifecycleSubject.onNext(FragmentEvent.DESTROY);
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mPresenter != null) {
            mPresenter.detachVM();
        }
    }

    public abstract Class<P> getPresenterClass();

    /**
     * model 可以为空
     *
     * @return
     */
    public abstract Class<M> getModelClass();


    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(getActivity(),resId,Toast.LENGTH_LONG).show();
    }

    /**
     * 判定单击
     *
     * @return
     */
    public boolean checkSingleClick() {
        if (System.currentTimeMillis() - lastClickTime < 500) {
            lastClickTime = System.currentTimeMillis();
            return false;
        }
        lastClickTime = System.currentTimeMillis();
        return true;
    }

    @Override
    public void launchActivity(Class<?> clazz) {
        if (!checkSingleClick()) {
            return;
        }
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }

    /**
     * 启动新的activity，带数据传递
     *
     * @param clazz
     * @param data  传递的数据
     */
    @Override
    public void launchActivity(Class<?> clazz, Bundle data) {
        if (!checkSingleClick()) {
            return;
        }
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtras(data);
        startActivity(intent);
    }

}
