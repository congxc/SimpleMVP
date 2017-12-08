package com.rainshieh.simplemvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.rainshieh.simplemvp.R;
import com.rainshieh.simplemvp.base.BaseActivity;
import com.rainshieh.simplemvp.model.MainModel;
import com.rainshieh.simplemvp.model.contract.CommonContract;
import com.rainshieh.simplemvp.presenter.MainPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity<MainPresenter, MainModel> implements CommonContract.ICommonView<String> {
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.button)
    Button mButton;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }


    @Override
    public Class<MainPresenter> getPresenterClass() {
        return MainPresenter.class;
    }

    @Override
    public Class<MainModel> getModelClass() {
        return MainModel.class;
    }


    @Override
    public void initWidget(Bundle savedInstanceState) {

    }

    @Override
    public void bindEventListener() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SecondActivity.class));
            }
        });
    }


    @Override
    public void setData(String title) {
        setTitle(title);
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void showMessage(String message) {
        showToast(message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
