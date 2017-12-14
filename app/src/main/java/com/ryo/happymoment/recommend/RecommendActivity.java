package com.ryo.happymoment.recommend;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ryo.happymoment.R;
import com.ryo.view.AmazingTabView;
import com.ryo.view.IAmazingTabView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/13.
 */

public class RecommendActivity extends Activity {
    ViewPager page;
    AmazingTabView tabView;
    String text[]={"福建","广州","杭州","浙江","上海","苏州","其他"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_activity);
        tabView= (AmazingTabView) findViewById(R.id.tab);
        
        tabView.setMaxDisplayTab(4);
        tabView.setTabBgRes(new ColorDrawable(0xffffffff));
        tabView.addTabs(text);

        page = findViewById(R.id.page);
        page.setAdapter(new MyAdapter(text)); 
        page.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabView.setonPageScrolled(position,positionOffset);
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
    
    public class MyAdapter extends PagerAdapter{
        ArrayList<View> views=new ArrayList<>();
        public MyAdapter(String text[])
        {
            for(int i=0;i<text.length;i++)
            {
                Button button = new Button(RecommendActivity.this);
                button.setBackgroundColor(0xffeeeeee);
                button.setText(text[i]);
                views.add(button);
            }
        }
        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v=views.get(position);
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }
}
