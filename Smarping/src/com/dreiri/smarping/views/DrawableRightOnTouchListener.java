package com.dreiri.smarping.views;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public abstract class DrawableRightOnTouchListener implements OnTouchListener {

    private Drawable drawable;
    private int extraTapArea = 10;

    public DrawableRightOnTouchListener(TextView view) {
        super();
        Drawable[] drawables = view.getCompoundDrawables();
        if (drawables != null && drawables.length == 4) {
            this.drawable = drawables[2];
        } else {
            this.drawable = null;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && drawable != null) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Rect bounds = drawable.getBounds();
            if (x >= (v.getWidth() - bounds.width() - extraTapArea) && x <= (v.getWidth() - v.getPaddingRight() + extraTapArea) && y >= (v.getPaddingTop() - extraTapArea) && y <= (v.getHeight() - v.getPaddingBottom() + extraTapArea)) {
                return onDrawableTouch(event);
            }
        }
        return false;
    }

    public abstract boolean onDrawableTouch(MotionEvent event);

}
