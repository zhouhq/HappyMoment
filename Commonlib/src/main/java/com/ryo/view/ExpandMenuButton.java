package com.ryo.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.commonlib.R;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by zhouhq on 2018/1/3.
 * 一个展开式菜单的按钮，点击时向左或者向右
 */

public class ExpandMenuButton extends ViewGroup implements View.OnClickListener {

    private interface OnClickMenu {
        void onClick(int index);
    }

    /**
     * 保存相关的菜单view
     */
    ArrayList<View> menus = new ArrayList<>();
    ArrayList<TextView> titleViews = new ArrayList<>();


    /**
     * 展开按钮，点击时展再，再点击时缩放
     */
    View expandButton;
    int menuWidth = 100;
    int menuHeight = 100;
    int menuGap = 10;
    int titleSize = 30;
    int textViewSize = (int) (titleSize * 1.5f);

    /**
     * 展开按钮的资源
     */
    Drawable buttonDrawable;
    Drawable buttonCloseDrawable;

    /***
     *是否是在展开的状态
     * */
    private boolean isExpandState = false;

    OnClickMenu clickMenuListener;

    private static final int ani_time = 500;

    AnimatorSet expandAni;
    AnimatorSet shrinkAni;

    @Override
    public void onClick(View view) {
        if (view == expandButton) {
            if (!isExpandState) {
                isExpandState = !isExpandState;
                expandButton.setBackground(buttonCloseDrawable);
                handleExpand();
            } else {
                isExpandState = !isExpandState;
                expandButton.setBackground(buttonDrawable);
                handleShrink();
            }
            return;
        }

        for (int i = 0; i < menus.size(); i++) {
            View v = menus.get(i);
            if (v != view) continue;
            if (clickMenuListener != null) {
                clickMenuListener.onClick(i);
            }
            return;
        }

    }

    /**
     * 将各个状态初始化到缩小不可见状态
     */
    private void initShrink() {
        ObjectAnimator expandButtonAni = ObjectAnimator.ofFloat(expandButton, "rotation", 0, 0f);
        AnimatorSet set = new AnimatorSet();
        LinkedList<Animator> aniList = new LinkedList<>();
        for (int i = 0; i < menus.size(); i++) {
            View v = menus.get(i);
            TextView textView = titleViews.get(i);
            int translateX = expandButton.getRight() - v.getRight();
            PropertyValuesHolder translateXVH = PropertyValuesHolder.ofFloat("translationX", translateX, translateX);
            PropertyValuesHolder rotationVH = PropertyValuesHolder.ofFloat("rotation", -180, -180);
            PropertyValuesHolder alphaVH = PropertyValuesHolder.ofFloat("alpha", 0, 0);
            ObjectAnimator menuAni = ObjectAnimator.ofPropertyValuesHolder(v, translateXVH, rotationVH, alphaVH);
            ObjectAnimator titleAni = ObjectAnimator.ofPropertyValuesHolder(textView, translateXVH, alphaVH);
            aniList.add(menuAni);
            aniList.add(titleAni);
        }
        aniList.add(expandButtonAni);


        set.playTogether(aniList);
        set.setDuration(1);
        set.start();
    }

    private void handleExpand() {
        if (expandAni != null) {
            expandAni.end();
            expandAni.start();
        }
        ObjectAnimator expandButtonAni = ObjectAnimator.ofFloat(expandButton, "rotation", -180, 0f);
        AnimatorSet set = new AnimatorSet();

        LinkedList<Animator> aniList = new LinkedList<>();

        for (int i = 0; i < menus.size(); i++) {
            View v = menus.get(i);
            TextView textView = titleViews.get(i);
            int translateX ;
            PropertyValuesHolder translateXVH ;

            if (orientation == ORIENTATION.LEFT || orientation == ORIENTATION.RIGHT) {
                translateX = expandButton.getRight() - v.getRight();
                translateXVH = PropertyValuesHolder.ofFloat("translationX", translateX, 0);
            } else {
                translateX = expandButton.getTop() - v.getTop();
                translateXVH = PropertyValuesHolder.ofFloat("translationY", translateX, 0);
            }
            PropertyValuesHolder rotationVH = PropertyValuesHolder.ofFloat("rotation", -180, 0);
            PropertyValuesHolder alphaVH = PropertyValuesHolder.ofFloat("alpha", 0, 1);
            ObjectAnimator menuAni = ObjectAnimator.ofPropertyValuesHolder(v, translateXVH, rotationVH, alphaVH);
            ObjectAnimator titleAni = ObjectAnimator.ofPropertyValuesHolder(textView,translateXVH,alphaVH);
            aniList.add(titleAni);
            aniList.add(menuAni);
        }
        aniList.add(expandButtonAni);


        set.playTogether(aniList);
        set.setDuration(ani_time);
        set.start();
        expandAni=set;
    }

    private void handleShrink() {
        if (shrinkAni != null) {
            shrinkAni.end();
            shrinkAni.start();
        }
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator expandButtonAni = ObjectAnimator.ofFloat(expandButton, "rotation", 0, -180);
        LinkedList<Animator> aniList = new LinkedList<>();

        for (int i = 0; i < menus.size(); i++) {
            View v = menus.get(i);
            TextView textView = titleViews.get(i);
            int translateX;
            PropertyValuesHolder translateXVH;

            if (orientation == ORIENTATION.LEFT || orientation == ORIENTATION.RIGHT) {
                 translateX = expandButton.getRight() - v.getRight();
                 translateXVH = PropertyValuesHolder.ofFloat("translationX", 0, translateX);
            }else {
                 translateX = expandButton.getTop() - v.getTop();
                 translateXVH = PropertyValuesHolder.ofFloat("translationY", 0, translateX);
            }
            PropertyValuesHolder rotationVH = PropertyValuesHolder.ofFloat("rotation", 0, -180);
            PropertyValuesHolder alphaVH = PropertyValuesHolder.ofFloat("alpha", 1, 0);
            ObjectAnimator menuAni = ObjectAnimator.ofPropertyValuesHolder(v, translateXVH, rotationVH, alphaVH);
            ObjectAnimator titleAni = ObjectAnimator.ofPropertyValuesHolder(textView,translateXVH,alphaVH);
            aniList.add(titleAni);
            aniList.add(menuAni);
        }
        aniList.add(expandButtonAni);

        set.playTogether(aniList);
        set.setDuration(ani_time);
        set.start();
        shrinkAni = set;
    }

    public enum ORIENTATION {
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
    ORIENTATION orientation = ORIENTATION.RIGHT;

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
        buttonDrawable = getResources().getDrawable(R.drawable.menu_open);
        buttonCloseDrawable = getResources().getDrawable(R.drawable.menu_close);
        expandButton.setBackground(buttonDrawable);
        addView(expandButton);
        expandButton.setOnClickListener(this);

    }

    /**
     * 设置菜单之间的间隔
     */
    public void setMenuGap(int gap) {
        menuGap = gap;
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
            //增加并设置图标
            ImageView view = new ImageView(getContext());
            menus.add(view);
            view.setImageDrawable(drawable[i]);
            addView(view, 0);
            view.setOnClickListener(this);

            //增加并设置标题
            TextView textView = new TextView(getContext());
            textView.setText(name[i]);
            titleViews.add(textView);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
            textView.setGravity(Gravity.CENTER);
            addView(textView);
        }
    }

    /**
     * 增加一个菜单按钮，菜单按钮的序号自动产生，是在原有的按钮加1
     * 因些各个菜单对面的Index，使用者要自行保存
     */
    public void addMenu(Drawable drawable, String name) {
        //增加并设置图标
        ImageView view = new ImageView(getContext());
        menus.add(view);
        view.setImageDrawable(drawable);
        addView(view, 0);
        view.setOnClickListener(this);

        //增加并设置标题
        TextView textView = new TextView(getContext());
        textView.setText(name);
        titleViews.add(textView);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        addView(textView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        switch (orientation) {
            case LEFT:
                onLayoutLeft(changed, l, t, r, b);
                break;
            case UP:
                onLayoutUp(changed, l, t, r, b);
                break;
            case RIGHT:
                onLayoutRight(changed, l, t, r, b);
                break;
            case DOWN:
                onLayoutDown(changed, l, t, r, b);
                break;

        }
        initShrink();
    }

    /***
     * 向左展开
     *
     */
    private void onLayoutLeft(boolean changed, int l, int t, int r, int b) {
        int left;
        int rihgt;
        int top;
        int bottom;

        rihgt = r - l - getPaddingRight();
        left = rihgt - menuWidth;
        top = (b - t - getPaddingTop() - getPaddingRight() - menuHeight - textViewSize) / 2 + getPaddingTop();
        bottom = top + menuHeight;
        expandButton.layout(left, top, rihgt, bottom);

        View v;
        TextView textView;
        for (int i = 0; i < menus.size(); i++) {
            v = menus.get(i);
            textView = titleViews.get(i);
            rihgt = rihgt - menuWidth - menuGap;
            left = rihgt - menuWidth;
            v.layout(left, top, rihgt, bottom);
            textView.layout(left, bottom, rihgt, bottom + textViewSize);
        }
    }

    /***
     * 向右展开
     *
     */
    private void onLayoutRight(boolean changed, int l, int t, int r, int b) {
        int left;
        int rihgt;
        int top;
        int bottom;

        left = getPaddingLeft();
        rihgt = left + menuWidth;
        top = (b - t - getPaddingTop() - getPaddingRight() - menuHeight - textViewSize) / 2 + getPaddingTop();
        bottom = top + menuHeight;
        expandButton.layout(left, top, rihgt, bottom);

        View v;
        TextView textView;
        for (int i = 0; i < menus.size(); i++) {
            v = menus.get(i);
            left = left + menuWidth + menuGap;
            rihgt = left + menuWidth;
            v.layout(left, top, rihgt, bottom);
            textView = titleViews.get(i);
            textView.layout(left, bottom, rihgt, bottom + textViewSize);
        }
    }


    /***
     * 向下展开
     *
     */
    private void onLayoutDown(boolean changed, int l, int t, int r, int b) {
        int left;
        int rihgt;
        int top;
        int bottom;

        left = getPaddingLeft();
        rihgt = left + menuWidth;

        top = getPaddingTop();
        bottom = top + menuHeight;
        expandButton.layout(left, top, rihgt, bottom);

        //加这一行是为了让第一个菜单和展开按钮的间距和其他菜单之间的看上去一致
        bottom =bottom+textViewSize;

        View v;
        TextView textView;
        for (int i = 0; i < menus.size(); i++) {
            v = menus.get(i);
            top = bottom + menuGap;
            bottom = top + menuHeight;
            v.layout(left, top, rihgt, bottom);

            top=bottom;
            bottom = top+textViewSize;
            textView = titleViews.get(i);
            textView.layout(left, top, rihgt, bottom);
        }
    }

    /***
     * 向上展开
     */
    private void onLayoutUp(boolean changed, int l, int t, int r, int b) {
        int left;
        int rihgt;
        int top;
        int bottom;

        left = getPaddingLeft();
        rihgt = left + menuWidth;


        bottom = b-t-getPaddingBottom();
        top = bottom - menuHeight;

        expandButton.layout(left, top, rihgt, bottom);


        View v;
        TextView textView;
        for (int i = 0; i < menus.size(); i++) {
            v = menus.get(i);

            bottom = top - menuGap;
            top = bottom - textViewSize;
            textView = titleViews.get(i);
            textView.layout(left, top, rihgt, bottom);
            bottom = top;
            top = bottom - menuHeight;
            v.layout(left, top, rihgt, bottom);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (orientation == ORIENTATION.DOWN || orientation == ORIENTATION.UP) {
            onMeasureVertical(widthMeasureSpec, heightMeasureSpec);
        } else {
            onMeasureHorizontal(widthMeasureSpec, heightMeasureSpec);
        }

    }

    /**
     * 左右展开水平方向
     * 测量结果是一样的
     */
    private void onMeasureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        int sizeW = MeasureSpec.getSize(widthMeasureSpec);
        int measureW;
        int modeH = MeasureSpec.getMode(heightMeasureSpec);
        int sizeH = MeasureSpec.getSize(heightMeasureSpec);
        int measureH;
        switch (modeW) {
            case MeasureSpec.EXACTLY:

                measureW = sizeW;
                break;
            case MeasureSpec.AT_MOST:
                //（菜单的宽+间隔）*个数+展开按按的宽+左右padding;
                measureW = (menuWidth + menuGap) * menus.size() + menuWidth + getPaddingLeft() + getPaddingRight();
                break;
            default:
                measureW = sizeW;

        }
        switch (modeH) {
            case MeasureSpec.EXACTLY:
                measureH = sizeH;
                break;
            case MeasureSpec.AT_MOST:
                measureH = menuHeight + getPaddingTop() + getPaddingBottom() + textViewSize;
                break;
            default:
                measureH = sizeW;
        }
        setMeasuredDimension(measureW, measureH);
    }

    /**
     * 上下展开水平方向
     * 测量结果是一样的
     */
    private void onMeasureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        int sizeW = MeasureSpec.getSize(widthMeasureSpec);
        int measureW;
        int modeH = MeasureSpec.getMode(heightMeasureSpec);
        int sizeH = MeasureSpec.getSize(heightMeasureSpec);
        int measureH;
        switch (modeW) {
            case MeasureSpec.EXACTLY:

                measureW = sizeW;
                break;
            case MeasureSpec.AT_MOST:
                measureW = getPaddingLeft() + getPaddingRight() + menuWidth;
                break;
            default:
                measureW = sizeW;

        }
        switch (modeH) {
            case MeasureSpec.EXACTLY:
                measureH = sizeH;
                break;
            case MeasureSpec.AT_MOST:
                measureH = (menuHeight + textViewSize + menuGap) * menus.size() + menuHeight + textViewSize + getPaddingTop() + getPaddingBottom();
                break;
            default:
                measureH = sizeW;
        }
        setMeasuredDimension(measureW, measureH);

    }

}
