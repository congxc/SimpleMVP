package com.rainshieh.simplemvp.base;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.rainshieh.simplemvp.R;
import com.rainshieh.simplemvp.interfaces.IActivity;
import com.rainshieh.simplemvp.interfaces.IModel;
import com.rainshieh.simplemvp.interfaces.IView;
import com.rainshieh.simplemvp.interfaces.lifecycle.ActivityLifecycleable;
import com.rainshieh.simplemvp.utils.LogUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * author: xiecong
 * create time: 2017/12/5 14:50
 * lastUpdate time: 2017/12/5 14:50
 */
public abstract class BaseActivity<P extends BasePresenter, M extends IModel> extends AppCompatActivity
        implements IActivity, ActivityLifecycleable {
    protected P mPresenter;
    protected M mModel;
    private Unbinder mUnbinder;
    private BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();
    private int mToolBarSize;
    private Toolbar mToolbar;
    private long lastClickTime;

    @Override
    public Subject<ActivityEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLifecycleSubject.onNext(ActivityEvent.CREATE);
        init(savedInstanceState);
        initWidget(savedInstanceState);
        bindEventListener();
    }
    /**
     * 设置状态栏沉浸式
     *
     * @param on
     */
    protected void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        mLifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLifecycleSubject.onNext(ActivityEvent.PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLifecycleSubject.onNext(ActivityEvent.STOP);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        int layoutResId = getLayoutResId();
        if (layoutResId != 0) {
            if (showToolBar() && getToolbarLayoutId() != 0) {
                View rootView = initRootView(layoutResId);
                mUnbinder = ButterKnife.bind(this, rootView);
                setSupportActionBar(mToolbar);
                setContentView(rootView);
            } else {
                setContentView(layoutResId);
                mUnbinder = ButterKnife.bind(this);
            }
        }
        //初始化presenter 和 model
        try {
            Class<P> presenterClass = getPresenterClass();
            if (presenterClass != null) {
                mPresenter = presenterClass.newInstance();
            } else {
                LogUtils.error("TAGxc" + getClass().getCanonicalName(), " presenterClass is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("create presenter error");
        }
        try {
            Class<M> modelClass = getModelClass();
            if (modelClass != null) {
                mModel = modelClass.newInstance();
            } else {
                LogUtils.error("TAGxc" + getClass().getCanonicalName(), " modelClass  is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("create model error");
        }
        if (mPresenter != null && this instanceof IView) {
            mPresenter.attachVM((IView) this, mModel);
        }

    }

    protected boolean showToolBar() {
        return true;
    }

    public int getToolBarSize() {
        return mToolBarSize;
    }

    @Override
    public int getToolbarLayoutId() {
        return R.layout.tool_bar_layout;
    }

    private View initRootView(int layoutResId) {
        FrameLayout rootView = new FrameLayout(this);
        rootView.setBackgroundResource(R.color.white);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        View contentView = getLayoutInflater().inflate(layoutResId, null, false);
        contentView.setLayoutParams(params);
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TypedArray typedArray = this.getTheme().obtainStyledAttributes(android.support.v7.appcompat.R.styleable.AppCompatTheme);
        /*获取主题中定义的toolbar的高度*/
        mToolBarSize = (int) typedArray.getDimension(android.support.v7.appcompat.R.styleable.AppCompatTheme_actionBarSize, (int) this.getResources().getDimension(R.dimen.abc_action_bar_default_height_material));
        typedArray.recycle();
        View toolbarLayout = LayoutInflater.from(this).inflate(getToolbarLayoutId(), null);
        mToolbar = (Toolbar) toolbarLayout.findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.icon_back_selector);
        rootView.addView(contentView, params);
        rootView.addView(toolbarLayout, params2);
        return rootView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLifecycleSubject.onNext(ActivityEvent.DESTROY);
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
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(this,resId,Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(getApplicationContext(), clazz);
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
        Intent intent = new Intent(getApplicationContext(), clazz);
        intent.putExtras(data);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
