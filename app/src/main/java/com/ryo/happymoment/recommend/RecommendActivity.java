package com.ryo.happymoment.recommend;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.ryo.drawable.CircleImageDrawable;
import com.ryo.happymoment.R;
import com.ryo.happymoment.recommend.adapter.RecommendAdapter;
import com.ryo.view.AmazingTabView;
import com.ryo.view.IAmazingTabView;
import com.ryo.view.TitleHeadView;

/**
 * Created by Administrator on 2017/12/13.
 */

public class RecommendActivity extends Activity {
    ViewPager page;
    AmazingTabView tabView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_activity);
        tabView = (AmazingTabView) findViewById(R.id.tab);
        initHead();
        initPage();
    }

    /***
     * 初始化标题栏
     * */
    private void initHead() {
        TitleHeadView headView = (TitleHeadView) findViewById(R.id.head);
        headView.setTitle(getString(R.string.recommend_title));
        headView.setTitleColor(0xffffffff);

        Bitmap left = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        CircleImageDrawable drawable = new CircleImageDrawable(left);

        headView.setLeftIcon(drawable);
        headView.setRightIcon(drawable);
    }

    /**
     * 初始化 关注页和热北京让页
     */
    private void initPage() {
        int tabTextID[] = {R.string.recommend_tab_attention, R.string.recommend_tab_hot};
        String tabText[] = new String[tabTextID.length];

        for (int i = 0; i < tabTextID.length; i++) {
            tabText[i] = (getString(tabTextID[i]));
        }
        tabView.addTabs(tabText);
        page = findViewById(R.id.page);
        page.setAdapter(new RecommendAdapter( this));
        page.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabView.setonPageScrolled(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tabView.setListener(new IAmazingTabView.OnSelectListener() {
            @Override
            public void onSelect(int selectPage, int lastSelectPage) {
                page.setCurrentItem(selectPage);
            }
        });
    }
}
