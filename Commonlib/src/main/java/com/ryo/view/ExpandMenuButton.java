package com.ryo.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by zhouhq on 2018/1/3.
 * 一个展开式菜单的按钮，点击时向左或者向右
 */

public class ExpandMenuButton extends RelativeLayout {
    ArrayList<View> menus = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();

    /**
     * 展开按钮，点击时展再，再点击时缩放
     */
    View expandButton;

    enum ORIENTATION {
        LEFT, RIGHT, UP, DOWN
    }

    /**
     * 菜单的数量，但不包括最小化时的那个展开或者缩放按钮
     ***/
    int count = 0;
    /**
     * 设置一个展开方向
     * 分别是 上下左右4个方向
     **/
    ORIENTATION orientation;

    public ExpandMenuButton(Context context) {
        super(context);
        init(context);
    }

    public ExpandMenuButton(Context context, AttributeSet attr) {
        super(context, attr);
        init(context);
    }

    private void init(Context context) {
        expandButton = new View(context);
        addView(expandButton);
    }

    public void setOrientation(ORIENTATION orientation) {
        this.orientation = orientation;
    }

    /**
     * 增加一组菜单按钮，菜单按钮的序号会自动依次增加
     * 因些各个菜单对面的Index，使用者要自行保存
     */
    public void addMenu(Drawable drawable[], String name[]) {
        int count = drawable.length < name.length ? drawable.length : name.length;
        for (int i = 0; i < count; i++) {
            ImageView view = new ImageView(getContext());
            menus.add(view);
            view.setImageDrawable(drawable[i]);
            titles.add(name[i]);
        }
    }

    /**
     * 增加一个菜单按钮，菜单按钮的序号自动产生，是在原有的按钮加1
     * 因些各个菜单对面的Index，使用者要自行保存
     */
    public void addMenu(Drawable drawable, String name) {
        ImageView view = new ImageView(getContext());
        menus.add(view);
        view.setImageDrawable(drawable);
        titles.add(name);
    }
}
