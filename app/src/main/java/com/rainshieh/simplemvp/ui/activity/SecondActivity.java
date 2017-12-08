package com.rainshieh.simplemvp.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.rainshieh.simplemvp.App;
import com.rainshieh.simplemvp.R;
import com.rainshieh.simplemvp.base.BaseActivity;
import com.rainshieh.simplemvp.model.SecondModel;
import com.rainshieh.simplemvp.model.beans.GankItemBean;
import com.rainshieh.simplemvp.model.contract.SecondContract;
import com.rainshieh.simplemvp.presenter.SecondPresenter;
import com.rainshieh.simplemvp.ui.widget.SpaceDecoration;
import com.rainshieh.simplemvp.ui.widget.SwipeBackLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author: xiecong
 * create time: 2017/12/7 14:01
 * lastUpdate time: 2017/12/7 14:01
 */

public class SecondActivity extends BaseActivity<SecondPresenter, SecondModel> implements SecondContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private int startPosition;

    private List<GankItemBean> mData = new ArrayList<>();
    private BaseQuickAdapter<GankItemBean,BaseViewHolder> mAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_second_activity;
    }

    @Override
    protected boolean showToolBar() {
        return false;
    }

    @Override
    public void initWidget(Bundle savedInstanceState) {
        SwipeBackLayout swipeBackLayout = (SwipeBackLayout) LayoutInflater.from(this).inflate(R.layout.layout_swipe_back, null);
        swipeBackLayout.attachToActivity(this);//直接添加此代码 就可实现 滑动关闭界面

        setTranslucentStatus(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.addItemDecoration(new SpaceDecoration(getResources().getDimensionPixelOffset(R.dimen.divider_height)));
        mAdapter = new BaseQuickAdapter<GankItemBean, BaseViewHolder>(R.layout.item_image_view,mData) {
            @Override
            protected void convert(BaseViewHolder helper, GankItemBean item) {
                ImageView imageView = helper.getView(R.id.image);
                imageView.setImageResource(R.mipmap.default_img);
                DisplayMetrics dm = App.getInstance().getApplicationContext().getResources().getDisplayMetrics();
                int width = (int) (dm.widthPixels / 2 - getApplicationContext().getResources().getDimensionPixelOffset(R.dimen.divider_height)*1.5f);//宽度为屏幕宽度一半
                imageView.setMaxWidth(width);
                imageView.setMinimumWidth(width);
                imageView.setAdjustViewBounds(true);
                Glide.with(SecondActivity.this)
                        .applyDefaultRequestOptions(RequestOptions.placeholderOf(R.mipmap.default_img))
                        .load(item.getUrl())
                        .into(imageView);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoadMoreView(new LoadMoreView() {
            @Override
            public int getLayoutId() {
                return R.layout.layout_load_more_view;
            }

            @Override
            protected int getLoadingViewId() {
                return R.id.loading_view;
            }

            @Override
            protected int getLoadFailViewId() {
                return R.id.load_failed_view;
            }

            @Override
            protected int getLoadEndViewId() {
                return R.id.load_end_view;
            }
        });
    }


    /**
     * 本页加载完成
     */
    private void loadMoreComplete(){
        if (mAdapter != null) {
            mAdapter.loadMoreComplete();
        }
    }


    /**
     *全部数据加载完成 ，没有更多数据
     * no more data
     * @param gon true 不显示没有更多视图 false显示没有更多视图
     */
    public void loadMoreEnd(boolean gon){
        if (mAdapter != null) {
            mAdapter.loadMoreEnd(gon);
        }
    }

    @Override
    public void bindEventListener() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this,mRecyclerView);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public Class<SecondPresenter> getPresenterClass() {
        return SecondPresenter.class;
    }

    @Override
    public Class<SecondModel> getModelClass() {
        return SecondModel.class;
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
        loadMoreComplete();
    }

    @Override
    public void showMessage(String message) {
        showToast(message);
    }

    @Override
    public void refreshFaild(String msg) {

    }

    @Override
    public void loadMoreFaild(String msg) {
        mAdapter.loadMoreFail();
    }

    @Override
    public void setList(List<GankItemBean> list) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.clear();
        mData.addAll(list);
        startPosition = mData.size();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            if(list.size() >= SecondPresenter.NUM_OF_PAGE){
                loadMoreComplete();
            }else{
                loadMoreEnd(true);
            }
        }
    }

    @Override
    public void setMoreList(List<GankItemBean> list) {
        mData.addAll(list);
        mAdapter.notifyItemRangeChanged(startPosition,list.size());
        startPosition += list.size();
        if(list.size() >= SecondPresenter.NUM_OF_PAGE){
            loadMoreComplete();
        }else{
            loadMoreEnd(true);
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.onRefresh();
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.loadMore();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(GankItemBean.class.getSimpleName(), (Serializable) mData);
        bundle.putInt("position",position);
        launchActivity(PreviewActivity.class,bundle);
    }
}
