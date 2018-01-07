package test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ryo.drawable.CircleImageDrawable;
import com.ryo.happymoment.R;
import com.ryo.view.AmazingTabView;
import com.ryo.view.ExpandMenuButton;
import com.ryo.view.IAmazingTabView;
import com.ryo.view.TitleHeadView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/13.
 */

public class TestActivity extends Activity {
    ViewPager page;
    AmazingTabView tabView;
    String text[]={"福建","广州","杭州","浙江","上海","苏州","其他"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        tabView= (AmazingTabView) findViewById(R.id.tab);
        
        //tabView.setMaxDisplayTab(4);
        //tabView.setTabBgRes(new ColorDrawable(0xffffffff));
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

        TitleHeadView headView=(TitleHeadView)findViewById(R.id.head);
        headView.setTitle("推荐");
        headView.setTitleColor(0xffffffff);

        Bitmap left= BitmapFactory.decodeResource(getResources(),R.drawable.icon);
        CircleImageDrawable drawable= new CircleImageDrawable(left);

        headView.setLeftIcon(drawable);
        headView.setRightIcon(drawable);
        //tabView.setLineBarRes( new ColorDrawable(0xff03A9F4));

        ExpandMenuButton button=(ExpandMenuButton)findViewById(R.id.button);
        ExpandMenuButton button1=(ExpandMenuButton)findViewById(R.id.button1);
        ExpandMenuButton button2=(ExpandMenuButton)findViewById(R.id.button2);
        ExpandMenuButton button3=(ExpandMenuButton)findViewById(R.id.button3);



        Drawable drawable1[]={getDrawable(R.drawable.menu_report),getDrawable(R.drawable.menu_share),getDrawable(R.drawable.menu_shielding)};
        String title[] = {"举报","分享","屏蔽"};

        button.setOrientation(ExpandMenuButton.ORIENTATION.RIGHT);
        button.addMenu(drawable1,title);

        button1.setOrientation(ExpandMenuButton.ORIENTATION.LEFT);
        button1.addMenu(drawable1,title);

        button2.setOrientation(ExpandMenuButton.ORIENTATION.UP);
        button2.addMenu(drawable1,title);

        button3.setOrientation(ExpandMenuButton.ORIENTATION.DOWN);
        button3.addMenu(drawable1,title);
    }
    
    public class MyAdapter extends PagerAdapter{
        ArrayList<View> views=new ArrayList<>();
        public MyAdapter(String text[])
        {
            for(int i=0;i<text.length;i++)
            {
                Button button = new Button(TestActivity.this);
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
