package djs.assignment02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

    // constants
    /*
    private int SENSOR_TYPE_GRIP = 65560;
    private int SENSOR_TYPE_ORIENTATION = 65558;
*/

    // variables
    private SensorManager m_sensor_manager;
    private Sensor m_sensor_accelerometer;
    private Sensor m_sensor_light;

    // functions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.create_sensors();
    }

    @Override
    protected void onPause(){
        super.onPause();

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    private void create_sensors(){
        // create sensor manager
        this.m_sensor_manager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> list = this.m_sensor_manager.getSensorList(Sensor.TYPE_ALL);
        for (int i = 0; i < list.size(); ++i){
            Log.v("SENSOR", list.get(i).getName());
        }

        // create accelerometer
        this.m_sensor_accelerometer = this.m_sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (this.m_sensor_accelerometer == null){
            // there is no accelerometer on the device
            ((TextView)this.findViewById(R.id.textview_accelerometer)).setText("Status: Not Supported");
        }
        else{
            String s = "Status: Present.\n";
            s += "Max Range: " + Float.toString(this.m_sensor_accelerometer.getMaximumRange()) + "\n";
            s += "Resolution: " + Float.toString(this.m_sensor_accelerometer.getResolution()) + "\n";
            s += "Min Delay: " + Float.toString(this.m_sensor_accelerometer.getMinDelay());
            ((TextView)this.findViewById(R.id.textview_accelerometer)).setText(s);
        }

        // create grip
        this.m_sensor_light = this.m_sensor_manager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (this.m_sensor_light == null){
            // there is no light on the device
            ((TextView)this.findViewById(R.id.textview_light)).setText("Status: Not Supported");
        }
        else{
            String s = "Status: Present.\n";
            s += "Max Range: " + Float.toString(this.m_sensor_light.getMaximumRange()) + "\n";
            s += "Resolution: " + Float.toString(this.m_sensor_light.getResolution()) + "\n";
            s += "Min Delay: " + Float.toString(this.m_sensor_light.getMinDelay());
            ((TextView)this.findViewById(R.id.textview_light)).setText(s);
        }
    }

    public void on_button_click_accelerometer(View view){
        // create the intent
        Intent intent = new Intent();

        // setup the intent
        intent.setClass(this, AccelerometerActivity.class);

        // start the intent
        this.startActivity(intent);
    }

    public void on_button_click_light(View view){
        // create the intent
        Intent intent = new Intent();

        // setup the intent
        intent.setClass(this, LightActivity.class);

        // start the intent
        this.startActivity(intent);
    }
}
