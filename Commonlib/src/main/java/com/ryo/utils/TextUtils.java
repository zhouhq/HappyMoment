package com.ryo.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by Administrator on 2017/12/13.
 */

public class TextUtils {
    /**
     * 字体大小SP转成px**
     * */
    public static float spToPx(Context c, int textSize)
    {
        Resources r;
        if (c == null) {
            r = Resources.getSystem();
        } else {
            r = c.getResources();
        }
        float sizePx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, textSize, r.getDisplayMetrics());
        return sizePx;
    }
}
