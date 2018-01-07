package com.ryo.happymoment.recommend.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by zhouhq on 2018/1/7.
 */

public class RecommendAdapter extends PagerAdapter {
    Context context;

    ArrayList<View> pages = new ArrayList<>();


    public RecommendAdapter(Context context) {
        this.context = context;
    }

    public void addViews(View view) {
        pages.add(view);
    }

    public void addViews(ArrayList<View> views) {
        pages.addAll(views);
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = pages.get(position);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(pages.get(position));
    }
}
