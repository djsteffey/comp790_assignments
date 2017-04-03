package djs.assignment02;

import android.content.Context;
import android.content.Loader;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GraphView extends View {

    // constants
    private static final float DRAW_INTERVAL_X = 32;
    private static final float DRAW_CIRCLE_RADIUS = 10.0f;
    private static final float DEFAULT_MAX_RANGE = 30.0f;

    // variables
    private List<Long> m_timestamps;
    private List<Float> m_values;
    private List<Float> m_means;
    private List<Float> m_standard_deviations;
    private long m_max_ms;
    private float m_max_range;

    public GraphView(Context context) {
        super(context);
        init();

    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        this.m_timestamps = new ArrayList<Long>();
        this.m_values = new ArrayList<Float>();
        this.m_means = new ArrayList<Float>();
        this.m_standard_deviations = new ArrayList<Float>();
        this.m_max_range = DEFAULT_MAX_RANGE;
    }

    public void set_max_range(float max_range){
        this.m_max_range = max_range;
    }

    public void add_value(float value, long ms_timestamp){

        // if the value is greater than max range then set the max range
        // to the next multiple of 5 greater than the value
        if (value > this.m_max_range){
            this.m_max_range = (float)(5 * (Math.ceil(Math.abs(value/5))));
        }

        // add to timestamps
        this.m_timestamps.add(ms_timestamp);

        // add to the array of values
        this.m_values.add(value);

        // see if too many
        // max ms is the maximum time we can show based on the width of the view and the x draw interval
        while (ms_timestamp - this.m_timestamps.get(0) > (this.m_max_ms)){
            this.m_timestamps.remove(0);
            this.m_values.remove(0);
            this.m_means.remove(0);
            this.m_standard_deviations.remove(0);
        }

        // MEAN
        float sum = 0.0f;
        float mean = 0.0f;
        for (int i = 0; i < this.m_values.size(); ++i){
            sum += this.m_values.get(i);
        }
        mean = sum / this.m_values.size();
        this.m_means.add(mean);

        // STANDARD DEVIATION
        // calculate a new standard deviation
        float std_dev_sum = 0.0f;
        float variance = 0.0f;
        float std_dev = 0.0f;
        for (int i = 0; i < this.m_values.size(); ++i){
            float diff = (this.m_values.get(i) - mean);
            std_dev_sum += (diff * diff);
        }
        variance = std_dev_sum / this.m_values.size();
        std_dev = (float)(Math.sqrt(variance));
        this.m_standard_deviations.add(std_dev);

        // force redraw
        this.invalidate();
    }

    public float get_max_range(){
        return this.m_max_range;
    }

    public void reset(){
        this.m_timestamps.clear();
        this.m_values.clear();
        this.m_means.clear();
        this.m_standard_deviations.clear();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // base draw
        super.onDraw(canvas);

        // this is the first time size is guaranteed to be set
        this.m_max_ms = (int)(this.getWidth() / DRAW_INTERVAL_X) * 100;

        // anything to actual draw?
        if (this.m_timestamps.size() == 0){
            return;
        }

        // restrict draw area for graph
        float view_height = this.getHeight();
        float graph_y_offset = 25;
        float graph_height = view_height - 2 * graph_y_offset;

        // determine y scale based on graph height and max range
        float y_scale = graph_height / this.m_max_range;


        // invert canvas so higher y values are drawn from bottom to top
//        canvas.translate(0, canvas.getHeight());
//        canvas.scale(1.0f, -1.0f);

        // base timestamp to draw all others offset from
        long base_time_stamp = this.m_timestamps.get(0);

        // paint for drawing
        Paint paint = new Paint();
        paint.setStrokeWidth(2.0f);
        paint.setColor(Color.BLACK);
        paint.setTextSize(24.0f);

        // lets draw a grid
        // first some horizontal lines 5 of them spaced evenly from 0 to max range
        for (int i = 0; i <= this.m_max_range; i+= this.m_max_range / 5){
            canvas.drawLine(0.0f, (view_height - 1) - graph_y_offset -  i * y_scale, this.getWidth(), (view_height - 1) - graph_y_offset -  i * y_scale, paint);
            canvas.drawText(Integer.toString(i), 0.0f, (view_height - 1) - graph_y_offset - i * y_scale, paint);
        }
        // now some vertical lines at every 500 ms for now
        long first_timestamp = (long)(100 * (Math.ceil(Math.abs(this.m_timestamps.get(0)/100))));
        for (long i = first_timestamp; i <= (this.m_max_ms + first_timestamp); i += 500){
            canvas.drawLine(((i - base_time_stamp) / 100.0f) * DRAW_INTERVAL_X, (view_height - 1) - graph_y_offset - 0.0f,
                    ((i - base_time_stamp) / 100.0f) * DRAW_INTERVAL_X, (view_height - 1) - graph_y_offset - graph_height, paint);
            canvas.drawText(Float.toString(i / 1000.0f), ((i - base_time_stamp) / 100.0f) * DRAW_INTERVAL_X, (view_height - 1) - graph_y_offset + 25.0f, paint);
        }

        // draw values
        paint.setStrokeWidth(4.0f);
        float x0 = 0.0f;
        float x1 = 0.0f;
        for (int i = 0; i < this.m_timestamps.size() - 1; ++i){
            x0 = ((this.m_timestamps.get(i) - base_time_stamp) / 100.0f) * DRAW_INTERVAL_X;
            x1 = ((this.m_timestamps.get(i + 1) - base_time_stamp) / 100.0f) * DRAW_INTERVAL_X;

            // values
            paint.setColor(Color.GREEN);
            canvas.drawLine(x0, (view_height - 1) - graph_y_offset - y_scale * this.m_values.get(i), x1, (view_height - 1) - graph_y_offset - y_scale * this.m_values.get(i + 1), paint);
            canvas.drawCircle(x0, (view_height - 1) - graph_y_offset - y_scale * this.m_values.get(i), DRAW_CIRCLE_RADIUS, paint);

            // means
            paint.setColor(Color.RED);
            canvas.drawLine(x0, (view_height - 1) - graph_y_offset - y_scale * this.m_means.get(i), x1, (view_height - 1) - graph_y_offset - y_scale * this.m_means.get(i + 1), paint);
            canvas.drawCircle(x0, (view_height - 1) - graph_y_offset - y_scale * this.m_means.get(i), DRAW_CIRCLE_RADIUS, paint);

            // deviations
            paint.setColor(Color.YELLOW);
            canvas.drawLine(x0, (view_height - 1) - graph_y_offset - y_scale * this.m_standard_deviations.get(i), x1, (view_height - 1) - graph_y_offset - y_scale * this.m_standard_deviations.get(i + 1), paint);
            canvas.drawCircle(x0, (view_height - 1) - graph_y_offset - y_scale * this.m_standard_deviations.get(i), DRAW_CIRCLE_RADIUS, paint);
        }
        paint.setColor(Color.GREEN);
        canvas.drawCircle(x1, (view_height - 1) - graph_y_offset - y_scale * (this.m_values.get(this.m_values.size() - 1)), DRAW_CIRCLE_RADIUS, paint);
        paint.setColor(Color.RED);
        canvas.drawCircle(x1, (view_height - 1) - graph_y_offset - y_scale * (this.m_means.get(this.m_means.size() - 1)), DRAW_CIRCLE_RADIUS, paint);
        paint.setColor(Color.YELLOW);
        canvas.drawCircle(x1, (view_height - 1) - graph_y_offset - y_scale * (this.m_standard_deviations.get(this.m_standard_deviations.size() - 1)), DRAW_CIRCLE_RADIUS, paint);
    }
}
