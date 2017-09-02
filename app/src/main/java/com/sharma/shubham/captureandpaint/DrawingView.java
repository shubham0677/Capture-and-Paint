package com.sharma.shubham.captureandpaint;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Created by Shubham on 2017-09-02.
 */

public class DrawingView extends View {

    // Drawing path
    private Path mDrawPath;
    // Drawing and canvas and text paint
    private Paint mDrawPaint, mCanvasPaint, mTextPaint;
    // Paint color
    private int mPaintColor = 0xFF660000;
    // Canvas
    private Canvas mDrawCanvas;
    // Bitmap to set on Canvas
    private Bitmap mCanvasBitmap;
    // Position of touch
    private float mX, mY;

    private static final float TOUCH_TOLERANCE = 4;

    public DrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing() {
        mDrawPath = new Path();

        mDrawPaint = new Paint();

        // Settings for drawing paint
        mDrawPaint.setColor(mPaintColor);
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setDither(true);
        mDrawPaint.setStrokeWidth(20);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);

        // Settings for text paint
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mTextPaint.setColor(Color.BLUE);
        mTextPaint.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20,
                getResources().getDisplayMetrics()));
        mTextPaint.setTextAlign(Paint.Align.LEFT);


        // Create new canvas paint with mode DITHER_FLAG, which allows smooth drawing on the canvas
        mCanvasPaint = new Paint(Paint.DITHER_FLAG);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Draw canvas on screen with image bitmap set as background
        mDrawCanvas = new Canvas(mCanvasBitmap);
    }

    /***
     * This method is called every time the view is invalidated
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw image bitmap on canvas
        canvas.drawBitmap(mCanvasBitmap, 0, 0, mCanvasPaint);

        // Draw the line/path on canvas
        canvas.drawPath(mDrawPath, mDrawPaint);
    }

    /***
     * This method is called whenever a touch event occurs on the view
     * @param event
     * @return boolean
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Get coordinated from screen where touch happened
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                    mDrawPath.moveTo(touchX, touchY);

                    // Update global variables for touch coordinates
                    mX = touchX;
                    mY = touchY;
                break;
            case MotionEvent.ACTION_MOVE:
                // Calculate absolute positions for touch positions
                float dx = Math.abs(touchX - mX);
                float dy = Math.abs(touchY - mY);
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    mDrawPath.quadTo(mX, mY, (touchX + mX) / 2, (touchY + mY) / 2);
                    mX = touchX;
                    mY = touchY;
                }
                break;
            case MotionEvent.ACTION_UP:
                // Draw path on canvas and commit
                mDrawCanvas.drawPath(mDrawPath, mDrawPaint);
                // Reset touch coordinates for drawPath
                mDrawPath.reset();
                break;
            default:
                return false;
        }
        // Draw the view again
        invalidate();
        return true;
    }
}