package com.ryo.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.commonlib.R;

/**
 * Created by Ryo(zhouhq) on 2017/12/19.
 * 一个标题栏控制，包括左右两边的两个图标（可null）和中间的 标题
 */

public class TitleHeadView extends RelativeLayout {
    /**
     * 左边的icon，一般可以用来放返回键，人头像等
     */
    private ImageView leftIcon;
    /**
     * 右边的cion，一般可以用来放 "设置"，"更多"等icon
     */
    private ImageView rightIcon;

    /**
     * 中间的textView,用来显示标题，默认显示
     **/
    private TextView titleView;

    public TitleHeadView(Context context) {
        super(context);
        init(context);
    }

    public TitleHeadView(Context context, AttributeSet attr) {
        super(context, attr);
        init(context);
    }

    /**
     * 初始化一人Textview，并添加进去，除标题外，另外两个icon默认不可见
     * 所以在后面的设置对应图标时，再添加
     */
    private void init(Context context) {
        titleView = new TextView(context);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        titleView.setLayoutParams(lp);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(17);
        addView(titleView);
    }

    /**
     * 设置标题栏的文字
     */
    public void setTitle(String title) {
        if (titleView == null) return;
        titleView.setText(title);
    }

    /**
     * 设置标题栏的文字颜色
     */
    public void setTitleColor(int color) {
        if (titleView == null) return;
        titleView.setTextColor(color);
    }

    /**
     * 设置标题栏的文字大小(单位为DP)
     */
    public void setTitleSize(int size) {
        if (titleView == null) return;
        titleView.setTextSize(size);
    }

    /**
     * 设置标题栏的文字大小（单位为像素）
     */
    public void setTitleSizeByPx(int size) {
        if (titleView == null) return;
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    /**
     * 获取Title的view对象
     */
    public TextView getTitleView() {
        return titleView;
    }


    /**
     * 设置左边的icon，未添加的先添加view
     */
    public void setLeftIcon(Drawable drawable) {
        if (leftIcon == null) {
            addLeftIcon();
        }
        leftIcon.setImageDrawable(drawable);
    }

    public void setRightIcon(Drawable drawable) {
        if (rightIcon == null) {
            addRightIcon();
        }
        rightIcon.setImageDrawable(drawable);
    }


    /**
     * 创建左边的icon的view
     * 延时创建是为了，不需要这个icon时，可以省些内存
     */
    private void addLeftIcon() {
        if (leftIcon != null) return;
        leftIcon = new ImageView(getContext());
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        leftIcon.setLayoutParams(lp);
        addView(leftIcon);
    }


    /**
     * 创建右边的icon的view
     * 延时创建是为了，不需要这个icon时，可以省些内存
     */
    private void addRightIcon() {
        if (rightIcon != null) return;
        rightIcon = new ImageView(getContext());
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        rightIcon.setLayoutParams(lp);
        addView(rightIcon);
    }

}
