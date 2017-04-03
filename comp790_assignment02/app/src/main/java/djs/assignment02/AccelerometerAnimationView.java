package djs.assignment02;

import android.app.Activity;
import android.content.Context;
import android.content.Loader;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class AccelerometerAnimationView extends View {

    // constants
    private static final float CIRCLE_RADIUS = 15.0f;

    // variables
    private float m_x_position;
    private float m_y_position;

    // functions
    public AccelerometerAnimationView(Context context) {
        super(context);
        init();

    }

    public AccelerometerAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AccelerometerAnimationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        this.m_x_position = 50.0f;
        this.m_y_position = 50.0f;
    }

    void set_xy_acceleration(float x, float y){
        // we will just use them as the velocity...not accurate according to physics
        // this is *about* every 100 ms....again this is not a physics simulation
        // the update direction will be based on the orientation

        // scale by a fixed amount fo see good movement
        x *= 10.0f;
        y *= 10.0f;

        int rotation = ((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation){
            case Surface.ROTATION_0:{
                this.m_x_position -= x;
                this.m_y_position += y;
            }break;
            case Surface.ROTATION_90:{
                this.m_x_position += y;
                this.m_y_position += x;
            }break;
            case Surface.ROTATION_180:{
                this.m_x_position += x;
                this.m_y_position -= y;
            }break;
            case Surface.ROTATION_270:{
                this.m_x_position -= y;
                this.m_y_position -= x;
            }break;
        }

        // make sure we dont go out of bounds
        if (this.m_x_position < (0.0f + CIRCLE_RADIUS)){
            this.m_x_position = CIRCLE_RADIUS;
        }
        else if (this.m_x_position > (this.getWidth() - CIRCLE_RADIUS)){
            this.m_x_position = this.getWidth() - CIRCLE_RADIUS;
        }
        if (this.m_y_position < (0.0f + CIRCLE_RADIUS)){
            this.m_y_position = CIRCLE_RADIUS;
        }
        else if (this.m_y_position > (this.getHeight() - CIRCLE_RADIUS)){
            this.m_y_position = this.getHeight() - CIRCLE_RADIUS;
        }

        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // base draw
        super.onDraw(canvas);

        // draw box around the canvas area
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(4.0f);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0.0f, 0.0f, this.getWidth(), this.getHeight(), paint);

        // now draw the circle
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.RED);
        canvas.drawCircle(this.m_x_position, this.m_y_position, CIRCLE_RADIUS, paint);
    }
}
