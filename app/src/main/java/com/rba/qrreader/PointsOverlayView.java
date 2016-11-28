package com.rba.qrreader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Ricardo Bravo on 27/11/16.
 */

public class PointsOverlayView extends View {

    PointF[] points;
    private Paint paint;

    public PointsOverlayView(Context context) {
        super(context);
        init(context);
    }

    public PointsOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PointsOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        paint.setStyle(Paint.Style.FILL);
    }

    public void setPoints(PointF[] points) {
        this.points = points;
        invalidate();
    }

    @Override public void draw(Canvas canvas) {
        super.draw(canvas);
        if (points != null) {
            for (PointF pointF : points) {
                canvas.drawCircle(pointF.x, pointF.y, 10, paint);
            }
        }
    }
}
