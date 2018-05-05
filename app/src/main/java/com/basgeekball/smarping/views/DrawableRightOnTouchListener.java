package com.basgeekball.smarping.views;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public abstract class DrawableRightOnTouchListener implements OnTouchListener {

    private Drawable drawable;

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
            int extraSpace = 10;
            int x = (int) event.getX();
            int y = (int) event.getY();
            Rect bounds = drawable.getBounds();
            int minX = v.getWidth() - v.getPaddingRight() - bounds.width();
            int maxX = v.getWidth() - v.getPaddingRight();
            int minY = v.getPaddingTop() - extraSpace;
            int maxY = v.getHeight() - v.getPaddingBottom() + extraSpace;
            if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
                return onDrawableTouch(event);
            }
        }
        return false;
    }

    public abstract boolean onDrawableTouch(MotionEvent event);

}
