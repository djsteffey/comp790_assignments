package djs.assignment02;

import android.app.Activity;
import android.content.Context;
import android.content.Loader;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class LightAnimationView extends View {

    // constants

    // variables
    private Bitmap m_light_image;
    private int m_alpha;

    // functions
    public LightAnimationView(Context context) {
        super(context);
        init();

    }

    public LightAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LightAnimationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        this.m_light_image = BitmapFactory.decodeResource(this.getResources(), R.drawable.light_bulb);
        this.m_alpha = 0;
    }

    void set_value(float x, float max){
        // illuminate the bulb a certain amount based on the value of x versus the max
        this.m_alpha = (int)((x / max) * 255);

        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // base draw
        super.onDraw(canvas);

        // draw box around the canvas area
        Paint paint = new Paint();
        paint.setAlpha(this.m_alpha);
        canvas.drawBitmap(this.m_light_image, 0, 0, paint);
    }
}
