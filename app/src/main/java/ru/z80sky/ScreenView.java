package ru.z80sky;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * The type Circle view.
 */
public class ScreenView extends View {
    private int x, y, radius;

    private Paint paint;

    /**
     * Instantiates a new Circle view.
     *
     * @param context the context
     */
    public ScreenView(Context context) {
        super(context);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * Instantiates a new Circle view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public ScreenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * Set color.
     *
     * @param color the color
     */
    public void setColor(int color){
        paint.setColor(0xff000000 | color);
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        //
        x = w>>1;
        y = h>>1;
        radius = w < h ? x : y;
        radius -= 5;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(x, y, radius, paint);
    }
}


