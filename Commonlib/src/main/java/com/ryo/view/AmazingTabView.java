package com.ryo.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;


import com.example.commonlib.R;
import com.ryo.utils.TextUtils;

import java.util.ArrayList;

/**
 * Created by Ryo on 2017/12/13.
 * 功能1：支持自动隐藏，显示多余TAB页
 * 功能2：支持定制选中页的字体颜色和大小(滑动过程不断变大缩小)
 * 功能3:支持每一个tab页有独立的背景
 * 功能4：支持滑动和直接点击（分两成个监听器）
 */

public class AmazingTabView extends View implements IAmazingTabView {
    /**
     * 文字相关
     * 包含选中和未选中字体像素大小，比例，颜色
     * 如果同时设置和像素大小和比例，比例优先
     */
    float normalTextSize = -1;
    float selectTextSize = -1;
    float normalTextRatio = defaultNormalTextRatio;
    float selectTextRatio = defaultSelectTextRatio;
    int normalTextColor = defaultNormalTextColor;
    int selectTextColor = defaultSelectTextColor;

    /**
     * 滑动条资源
     */
    Drawable drawableLineBar;

    /**
     * tab背景资源
     */
    Drawable drawableTabBg;

    /**
     * tab背景资源
     */
    Drawable drawableAloneTabBg[];

    /**
     * 数据相关
     */
    ArrayList<TabInfo> tabs = new ArrayList<>();
    int maxDisplayCount = 1;
    float lineBarRatio = 0.8f;

    /**
     * 监听器
     */
    OnSelectListener listener;
    int selectTabIndex;
    int clickTabIndex;
    float selectTabIndexOffset;

    /**
     * 和绘制相关的
     */
    Rect tabRect = new Rect();
    Rect tempRect = new Rect();
    Paint paint = new Paint();
    int tabWidth;
    int tabHeight;
    Xfermode mode_src_in = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    Xfermode mode_src_over = new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER);

    /**
     * 本类使用的的一些默认值
     */
    final static int defaultNormalTextColor = 0xFF919191;//默认黑色
    final  static int defaultSelectTextColor = 0xff03A9F4;//0xFFffa9f4;
    final static int defaultTabBgColor = 0xffffffff;
    final static int defaultLineBarColor = 0xff03A9F4;
    final static float defaultNormalTextRatio = 0.4f;
    final static float defaultSelectTextRatio = 0.5f;
    //500毫秒
    final static float def_click_time = 500;
    final static float def_move_threshold = 20;
    /**
     * 是否需要重新计算相关数值
     * 重新设置字体大小时，重新设置tab字符串时，View大小发生改变时
     */
    boolean reCalculate = true;
    /**
     * 手指点击的X
     */
    float downX;
    /**
     * 手指点击的X
     */
    long downTime;
    boolean isMoving = false;

    public AmazingTabView(Context context) {
        super(context);
        init(context);
    }

    public AmazingTabView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AmazingTabView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        TypedArray type = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.RyoTabView, defStyle, 0);

        int count = type.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = type.getIndex(i);
            if (attr == R.styleable.RyoTabView_maxDisplayCount) {
                maxDisplayCount = type.getInt(attr, 2);
            } else if (attr == R.styleable.RyoTabView_normalTextColor) {
                normalTextColor = type.getColor(attr, defaultNormalTextColor);
            } else if (attr == R.styleable.RyoTabView_selectTextColor) {
                selectTextColor = type.getColor(attr, defaultSelectTextColor);
            }else if (attr == R.styleable.RyoTabView_normalTextSize) {
                normalTextSize = type.getDimensionPixelSize(attr, -1);
            } else if (attr == R.styleable.RyoTabView_selectTextSize) {
                selectTextSize = type.getDimensionPixelSize(attr, -1);
            }else if (attr == R.styleable.RyoTabView_normalTextSizeRatio) {
                normalTextRatio = type.getFloat(attr, defaultNormalTextRatio);
            }else if (attr == R.styleable.RyoTabView_selectTextSizeRatio) {
                selectTextRatio = type.getFloat(attr, defaultSelectTextRatio);
            }else if (attr == R.styleable.RyoTabView_tabBackground) {
                drawableTabBg = type.getDrawable(attr);
            }else if (attr == R.styleable.RyoTabView_lightDrawable) {
                drawableLineBar = type.getDrawable(attr);
            }
        }


        init(context);
    }

    /**
     * 做一些初始化的动作
     */
    private void init(Context context) {
        paint.setAntiAlias(true);
    }

    /**
     * tab的信息，用户转进字符串时要转成TAbinfo
     * 方便日后扩展
     */
    private class TabInfo {
        String text;
        float normalDrawHeight;
        float normalDrawWidth;
        float selectDrawHeight;
        float selectDrawWidth;
        Object tag;
        int index;
    }


    /***************************
     * 以下是开放给使用者调用的函数
     *********************/
    @Override
    public void setNormalTextSize(int size) {

        normalTextSize = TextUtils.spToPx(getContext(), size);
        setReCalculate(true);
    }

    @Override
    public void setSelectTextSize(int size) {
        selectTextSize = TextUtils.spToPx(getContext(), size);
        ;
        setReCalculate(true);
    }

    @Override
    public void setNormalTextRatio(float ratio) {
        normalTextRatio = ratio;
        setReCalculate(true);
    }

    @Override
    public void setSelectTextRatio(float ratio) {
        selectTextRatio = ratio;
        setReCalculate(true);
    }

    @Override
    public void setNormalTextColor(int color) {
        normalTextColor = color;
    }

    @Override
    public void setSelectTextColor(int color) {
        selectTextColor = color;
    }

    @Override
    public void setTabBgRes(Drawable drawable) {
        drawableTabBg = drawable;
    }

    @Override
    public void setMaxDisplayTab(int count) {
        maxDisplayCount = count;
    }

    @Override
    public void setLineBarRes(Drawable drawable) {
        drawableLineBar = drawable;
    }

    @Override
    public void setLineBarRatio(float ratio) {
        lineBarRatio = ratio;
    }

    @Override
    public void setListener(OnSelectListener listener) {
        this.listener = listener;
    }

    @Override
    public void addTabs(String[] texts) {
        if (texts == null) return;
        TabInfo info;
        for (int i = 0; i < texts.length; i++) {
            String text = texts[i];
            info = new TabInfo();
            info.text = text;
            info.index = i;
            tabs.add(info);
        }
    }

    @Override
    public void addOneTab(String text) {
        if (text == null) return;

        TabInfo info = new TabInfo();
        info.text = text;
        info.index = tabs.size();
        tabs.add(info);
    }

    @Override
    public void addOneTab(String text, int index) {
        if (text == null) return;
        if (index < 0 || index >= tabs.size()) return;

        TabInfo info = new TabInfo();
        info.text = text;
        info.index = index;
        tabs.add(index, info);
    }

    @Override
    public void clearAllTab() {
        tabs.clear();
    }

    @Override
    public void removeTab(int index) {
        if (index < 0 || index >= tabs.size()) ;
    }

    @Override
    public void setPageRatio(float ratio) {

    }

    @Override
    public void setonPageScrolled(int pos, float posOffset) {
        selectTabIndex = pos;
        selectTabIndexOffset = posOffset;
        if (pos >= maxDisplayCount - 2 && pos < tabs.size() - 2) {
            float scroll = tabWidth * (pos - (maxDisplayCount - 2) + posOffset);
            scrollTo((int) scroll, 0);
        } else if (pos < maxDisplayCount - 2) {
            //没有超过指定位置时，要还原到初始位置，以免点标签时
            scrollTo(0, 0);
        }

        invalidate();
    }

    /***************************
     * 以下内部函数
     *********************/
    @Override
    protected void onDraw(Canvas canvas) {
        calculateTabInfo();
        super.onDraw(canvas);
        //tab不需要独立的背景，则画整体的
        if (drawableAloneTabBg == null) {
            drawTagBg(canvas);
        }

        TabInfo info;
        int x;
        canvas.save();

        for (int i = 0; i < tabs.size(); i++) {
            info = tabs.get(i);
            x = tabWidth * i + getPaddingLeft();
            drawOneTab(canvas, info, x);
        }
        canvas.restore();
        drawLineBar(canvas);
    }

    /**
     * 画单个tab,主要是写上一个文字
     */
    private void drawOneTab(Canvas canvas, TabInfo info, int startx) {
        // Log.e("zhouhq","onPageScrolled = "+selectTabIndex+"f="+selectTabIndexOffset);
        //在屏幕内的不用显示
        if (!needDrawThisTab(info, startx)) return;

        float textW;
        float size;
        if (info.index == selectTabIndex) {
            paint.setColor(selectTextColor);
            textW = info.normalDrawWidth + (info.selectDrawWidth - info.normalDrawWidth) * (1 - selectTabIndexOffset);
            size = normalTextSize + (selectTextSize - normalTextSize) * (1 - selectTabIndexOffset);

        } else if (info.index == selectTabIndex + 1) {
            paint.setColor(normalTextColor);
            textW = info.normalDrawWidth + (info.selectDrawWidth - info.normalDrawWidth) * selectTabIndexOffset;
            size = normalTextSize + (selectTextSize - normalTextSize) * (selectTabIndexOffset);
        } else {
            paint.setColor(normalTextColor);
            textW = info.normalDrawWidth;
            size = normalTextSize;
        }
        paint.setTextSize((int) size);

        //这两个tab为了实现文字颜色不一样，保存一个云
        if (info.index == selectTabIndex || info.index == selectTabIndex + 1) {
            canvas.saveLayer(startx, 0, startx + tabWidth, getHeight(), paint);
        }
        canvas.clipRect(getPaddingLeft() + getScrollX(), 0, getWidth() - getPaddingRight() + getScrollX(), getHeight(), Region.Op.REPLACE);
        int top = (int) (tabHeight - info.normalDrawHeight) / 2 + getPaddingTop();
        canvas.drawText(info.text, startx + (tabWidth - (int) textW) / 2, top + info.normalDrawHeight, paint);
        if (info.index == selectTabIndex) {
            paint.setXfermode(mode_src_in);
            paint.setColor(normalTextColor);
            float l = (tabWidth - textW) / 2 + tabRect.left;
            float r = l + textW * selectTabIndexOffset;
            canvas.drawRect(l + startx, tabRect.top + getPaddingTop(), (int) r + startx, tabRect.bottom + getPaddingTop(), paint);
            paint.setXfermode(mode_src_over);
            canvas.restore();
        } else if (info.index == selectTabIndex + 1) {
            paint.setXfermode(mode_src_in);
            paint.setColor(selectTextColor);
            float l = (tabWidth - textW) / 2 + tabRect.left;
            float r = l + textW * selectTabIndexOffset;
            canvas.drawRect(l + startx, tabRect.top + getPaddingTop(), (int) r + startx, tabRect.bottom + getPaddingTop(), paint);
            paint.setXfermode(mode_src_over);
            canvas.restore();
        }

    }

    /**
     * 是否需要绘制这个tab页
     * 不在当面的最大显示页范围内的不显示
     */
    private boolean needDrawThisTab(TabInfo info, int startX) {
        int l = startX;
        int r = l + tabWidth;
        if (r - getScrollX() <= getPaddingLeft()) {
            return false;
        }
        if (l - getScrollX() >= tabWidth * maxDisplayCount + getPaddingLeft()) {
            return false;
        }
        return true;
    }

    private void drawOneTabBg(Canvas canvas, TabInfo info, int startx) {

    }

    /**
     * 画下面的划动条
     */
    private void drawLineBar(Canvas canvas) {
        if (drawableLineBar == null) {
            float blank = tabWidth * (1 - lineBarRatio) / 2;
            float l = (selectTabIndex + selectTabIndexOffset) * tabWidth + blank;
            float r = l + tabWidth - blank * 2;
            float h = tabRect.height() * 0.05f;
            float t = tabRect.bottom - h + getPaddingTop();
            float b = t + h;
            tempRect.set((int) l + getPaddingLeft(), (int) t, (int) r + getPaddingLeft(), (int) b);
            paint.setColor(defaultLineBarColor);
            canvas.drawRect(tempRect, paint);
        } else {
            float blank = tabWidth * (1 - lineBarRatio) / 2;
            float l = (selectTabIndex + selectTabIndexOffset) * tabWidth + blank;
            float r = l + tabWidth - blank * 2;
            float h = tabRect.height() * 0.05f;
            float t = tabRect.bottom - h + getPaddingTop();
            float b = t + h;
            drawableLineBar.setBounds((int) l + getPaddingLeft(), (int) t, (int) r + getPaddingLeft(), (int) b);
            drawableLineBar.draw(canvas);
        }
    }

    /**
     * 画整个tab的整体背景
     **/
    private void drawTagBg(Canvas canvas) {
        tempRect.set(0, 0, getWidth(), getHeight());
        tempRect.offset(getScrollX(), 0);
        if (drawableTabBg != null) {
            drawableTabBg.setBounds(tempRect);
            drawableTabBg.draw(canvas);
        } else {
            paint.setColor(defaultTabBgColor);
            canvas.drawRect(tempRect, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int tabW = (w - getPaddingLeft() - getPaddingRight()) / maxDisplayCount;
        int tabH = h - getPaddingTop() - getPaddingBottom();
        tabWidth = tabW;
        tabHeight = tabH;
        tabRect.set(0, 0, tabW, tabH);
        calculateTabInfo();
    }

    /**
     * 计算各个字符串的宽和高
     */
    private void calculateTabInfo() {
        //不需要重新计算
        if (!reCalculate) return;
        reCalculate = false;

        if (normalTextSize <= 0) {
            normalTextSize = tabHeight * normalTextRatio;
        }
        if (selectTextSize <= 0) {
            selectTextSize = tabHeight * selectTextRatio;
        }

        for (int i = 0; i < tabs.size(); i++) {

            TabInfo info = tabs.get(i);
            paint.setTextSize(normalTextSize);
            paint.getTextBounds(info.text, 0, info.text.length(), tempRect);
            info.normalDrawWidth = tempRect.width();
            info.normalDrawHeight = tempRect.height();

            paint.setTextSize(selectTextSize);
            paint.getTextBounds(info.text, 0, info.text.length(), tempRect);
            info.selectDrawWidth = tempRect.width();
            info.selectDrawHeight = tempRect.height();


        }


    }

    private void setReCalculate(boolean v) {
        reCalculate = v;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        float x = event.getX();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downTime = System.currentTimeMillis();
                isMoving = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(x - downX) > def_move_threshold) {
                    isMoving = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                handTouchUp(event);
                break;
            default:
        }
        return true;
    }

    /**
     * 处理放手事件，这里只处理点击事件
     * 后续滑动的以后再添加
     */
    private void handTouchUp(MotionEvent event) {
        if (!isMoving && System.currentTimeMillis() - downTime < def_click_time && listener != null) {
            float x = event.getX() + getScrollX() - getPaddingLeft();
            clickTabIndex = (int) x / (tabWidth);
            listener.onSelect(clickTabIndex, selectTabIndex);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode_w = MeasureSpec.getMode(widthMeasureSpec);
        int size_w = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        float size = MeasureSpec.getSize(heightMeasureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
                if (selectTextSize <= 0) {
                    size = (int) TextUtils.spToPx(getContext(), 50);

                } else {
                    size = (int) selectTextSize;
                }
                size = size * 1.2f;
                size = size + getPaddingTop() + getPaddingBottom();
                break;

        }
        setMeasuredDimension(size_w, (int) size);
    }


}
