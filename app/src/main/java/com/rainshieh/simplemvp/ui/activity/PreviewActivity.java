package com.rainshieh.simplemvp.ui.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rainshieh.simplemvp.R;
import com.rainshieh.simplemvp.base.BaseActivity;
import com.rainshieh.simplemvp.model.beans.GankItemBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author: xiecong
 * create time: 2017/12/8 13:38
 * lastUpdate time: 2017/12/8 13:38
 */

public class PreviewActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private List<GankItemBean> mDatas;
    private int mPosition;

    @Override
    protected boolean showToolBar() {
        return false;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_preview;
    }

    @Override
    public void initWidget(Bundle savedInstanceState) {
        setTranslucentStatus(true);
        Bundle data = getIntent().getExtras();
        if (data != null) {
            mDatas = (List<GankItemBean>) data.getSerializable(GankItemBean.class.getSimpleName());
            mPosition = data.getInt("position", 0);
        }
        mDatas = mDatas == null? new ArrayList<GankItemBean>():mDatas;
        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return mDatas.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = View.inflate(PreviewActivity.this, R.layout.item_image_view, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.image);
                int widthPixels = getApplicationContext().getResources().getDisplayMetrics().widthPixels -
                        getApplicationContext().getResources().getDimensionPixelOffset(R.dimen.dp_10)*2;
                int heightPixels = getApplicationContext().getResources().getDisplayMetrics().heightPixels -
                        getApplicationContext().getResources().getDimensionPixelOffset(R.dimen.dp_10)*2;
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                layoutParams.width = widthPixels;
                layoutParams.height = heightPixels;
                imageView.setLayoutParams(layoutParams);
                imageView.setMaxWidth(widthPixels);
                imageView.setMaxHeight(heightPixels);
                imageView.setAdjustViewBounds(true);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                container.addView(view);
                Glide.with(PreviewActivity.this)
                        .applyDefaultRequestOptions(RequestOptions.placeholderOf(R.mipmap.default_img))
                        .load(mDatas.get(position).getUrl())
                        .into(imageView);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                if(object instanceof View){
                    container.removeView((View)object);
                }
            }
        };
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(mPosition);

    }

    @Override
    public void bindEventListener() {

    }


    @Override
    public Class getPresenterClass() {
        return null;
    }

    @Override
    public Class getModelClass() {
        return null;
    }
}
