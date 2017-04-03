package djs.assignment02;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

public class AccelerometerActivity extends Activity implements SensorEventListener {

    // variables
    private SensorManager m_sensor_manager;
    private Sensor m_sensor;
    private long m_first_timestamp;

    // functions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        // set the activity title
        this.getActionBar().setTitle("Accelerometer");

        // create the sensor manager and sensor
        this.m_sensor_manager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        this.m_sensor = this.m_sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // start data gathering again
        if ((this.m_sensor_manager != null) && (this.m_sensor != null)) {
            this.m_sensor_manager.registerListener(this, this.m_sensor, 100 * 1000);
        }

        // no timestamp yet
        this.m_first_timestamp = -1;

        // set the max range rounded up to the nearest 5
        ((GraphView)this.findViewById(R.id.graphview_accelerometer)).set_max_range((float)(5*(Math.ceil(Math.abs(this.m_sensor.getMaximumRange()/5)))));

    }

    @Override
    public void onPause(){
        super.onPause();

        // stop data gathering on pause
        if (this.m_sensor_manager != null){
            this.m_sensor_manager.unregisterListener(this);
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        // start data gathering again
        if (this.m_sensor_manager != null){
            this.m_sensor_manager.registerListener(this, this.m_sensor, 100 * 1000);
        }

        // make sure the graph is reset
        ((GraphView)AccelerometerActivity.this.findViewById(R.id.graphview_accelerometer)).reset();

        // reset first timestamp
        this.m_first_timestamp = -1;
    }


    @Override
    public void onSensorChanged(SensorEvent event){
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        float plot_value = (float)(Math.sqrt((x * x) + (y * y) + (z * z)));

        if (this.m_first_timestamp == -1){
            this.m_first_timestamp = event.timestamp;
        }

        ((GraphView)this.findViewById(R.id.graphview_accelerometer)).add_value(plot_value, (event.timestamp - this.m_first_timestamp) / 1000000);
        ((AccelerometerAnimationView)this.findViewById(R.id.accelerometeranimationview)).set_xy_acceleration(x, y);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }
}
