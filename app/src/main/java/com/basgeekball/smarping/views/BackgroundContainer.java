package com.basgeekball.smarping.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class BackgroundContainer extends FrameLayout {

    private boolean showing = false;
    int openAreaTop, openAreaHeight;
    boolean updateBounds = false;

    public BackgroundContainer(Context context) {
        super(context);
    }

    public BackgroundContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackgroundContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void showBackground(int top, int bottom) {
        setWillNotDraw(false);
        openAreaTop = top;
        openAreaHeight = bottom;
        showing = true;
        updateBounds = true;
    }

    public void hideBackground() {
        setWillNotDraw(true);
        showing = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (showing) {
            canvas.save();
            canvas.translate(0, openAreaTop);
            canvas.restore();
        }
    }

}