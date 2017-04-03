package djs.assignment02;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class LightActivity extends Activity implements SensorEventListener {

    // variables
    private SensorManager m_sensor_manager;
    private Sensor m_sensor;
    private long m_first_timestamp;
    private boolean m_run_timer;
    private float m_last_value;

    // functions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        // set the activity title
        this.getActionBar().setTitle("Light");

        // create the sensor manager and sensor
        this.m_sensor_manager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        this.m_sensor = this.m_sensor_manager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // start data gathering again
        if ((this.m_sensor_manager != null) && (this.m_sensor != null)) {
            this.m_sensor_manager.registerListener(this, this.m_sensor, 100 * 1000);
        }

        // no timestamp yet
        this.m_first_timestamp = -1;

        // set the max range rounded up to the nearest 5
        ((GraphView)this.findViewById(R.id.graphview_light)).set_max_range((float)(5*(Math.ceil(Math.abs(this.m_sensor.getMaximumRange()/5)))));

        // doesnt work good for the light, lets set the max range relatively low
        // in the graph if we give it a value higher than its max, then it will just adjust its max
        // to compensate
        ((GraphView)this.findViewById(R.id.graphview_light)).set_max_range(10);//(float)(5*(Math.ceil(Math.abs(this.m_sensor.getMaximumRange()/5)))));

        this.m_run_timer = false;
        this.m_last_value = 0.0f;
    }

    @Override
    public void onPause(){
        super.onPause();

        // stop data gathering on pause
        if (this.m_sensor_manager != null){
            this.m_sensor_manager.unregisterListener(this);
        }

        // stop timer
        this.m_run_timer = false;

        Log.v("LIGHT", "onpause");
    }

    @Override
    public void onResume(){
        super.onResume();

        Log.v("LIGHT", "onresume");

        // start data gathering again
        if (this.m_sensor_manager != null){
            this.m_sensor_manager.registerListener(this, this.m_sensor, 100 * 1000);
        }

        // set a baseline timestamp
        this.m_first_timestamp = System.currentTimeMillis();

        // mark to keep running
        this.m_run_timer = true;

        // make sure the graph is reset
        ((GraphView)LightActivity.this.findViewById(R.id.graphview_light)).reset();

        // start timer
        final Handler handler = new Handler();

        final Runnable runnable_code = new Runnable() {
            @Override
            public void run() {
                // get the graph view
                GraphView gv = (GraphView)LightActivity.this.findViewById(R.id.graphview_light);
                // give the graph a new value at this new timestamp
                long delta_time = System.currentTimeMillis() - LightActivity.this.m_first_timestamp;
                gv.add_value(LightActivity.this.m_last_value, delta_time);
                // give the light animation the new value and the max value
                ((LightAnimationView)LightActivity.this.findViewById(R.id.lightanimationview)).set_value(LightActivity.this.m_last_value, gv.get_max_range());

                // setup the timer to run again at 100ms IF we are still running
                if (LightActivity.this.m_run_timer == true){
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(runnable_code);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        float x = event.values[0];
        this.m_last_value = event.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }
}
