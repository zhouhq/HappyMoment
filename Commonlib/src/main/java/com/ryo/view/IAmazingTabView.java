package com.ryo.view;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by Administrator on 2017/12/13.
 */

public interface IAmazingTabView {
    /**设置普通字体大小*/
     void setNormalTextSize(int size);
    /**设置选中字体大小*/
     void setSelectTextSize(int size);
    /**设置普通字体比例*/
     void setNormalTextRatio(float ratio);
    /**设置选中比例*/
     void setSelectTextRatio(float ratio);
    /**普通字体颜色
     * */
    void setNormalTextColor(int color);
    /**选中字体颜色
     * */
    void setSelectTextColor(int color);
    /**设置tab背景的颜色
     * */
    void setTabBgRes(Drawable drawable);
    
    /** 设置最大显示的tab数，大于这个tab将隐藏在左右两边
      * @param count 最大显示的tab数
      * */
     void setMaxDisplayTab(int count);
     /**
      * 设置滑动条的资源
      * */
    void setLineBarRes(Drawable drawable);

    /**
     * 设置滑动条的和一全tab的比例
     * */
    void setLineBarRatio(float ratio);
    
    /**
     * 监听器相关
     * */
    public interface OnSelectListener {
        void onSelect(int selectPage, int lastSelectPage);
    } 
    void setListener(OnSelectListener listener);
    /**
     * 添加一组tab
     * */
    void addTabs(String texts[]);
    /**
     * 添加单个tab默认放在最后
     * */
    void addOneTab(String texts);
    /**
     * 添加单个tab在指示位置
     * 要检查index合法性，不合法添加不成功
     * */
    void addOneTab(String texts,int index);
    /**
     * 清除所有tab
     * */
    void clearAllTab();
    /**
     * 移除当个tab
     * */
    void removeTab(int index);
    void setPageRatio(float ratio);
    void setonPageScrolled(int pos,float posOffset);
}
