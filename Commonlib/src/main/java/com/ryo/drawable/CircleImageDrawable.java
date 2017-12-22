package com.ryo.drawable;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/12/20.
 */

public class CircleImageDrawable extends Drawable {
    int radius = 0;

    public CircleImageDrawable(Bitmap bitmap) {
        this.bitmap = bitmap;
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setAntiAlias(true);
        paint.setShader(shader);
        radius = Math.min(bitmap.getWidth(), bitmap.getHeight())/2;

    }

    Paint paint = new Paint();
    Bitmap bitmap;

    @Override
    public void draw(Canvas canvas) {
        Rect rect = getBounds();
        int R = Math.min(rect.width(),rect.height())/2;
        int l=rect.left+(rect.width()/2-R);
        int t=rect.top+(rect.height()/2-R);
        canvas.drawCircle(l+R,t+R,R,paint);
    }

    @Override
    public void setAlpha(int i) {
        paint.setAlpha(i);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicHeight() {
        if (bitmap == null) {
            return super.getIntrinsicHeight();
        } else {
            return radius*2;
        }

    }

    @Override
    public int getIntrinsicWidth() {
        if (bitmap == null) {
            return super.getIntrinsicWidth();
        } else {
            return radius*2;
        }
    }
}
