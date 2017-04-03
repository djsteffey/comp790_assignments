package djs.assignment03;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    // enums
    private enum ELocation { BROOKS, POLK, WELL, HOME }

    // constants
    private static final LatLng LOCATION_BROOKS = new LatLng(35.909574, -79.053055);
    private static final LatLng LOCATION_POLK = new LatLng(35.910788, -79.050480);
    private static final LatLng LOCATION_WELL = new LatLng(35.912040, -79.051224);
    private static final LatLng LOCATION_HOME = new LatLng(35.825147, -78.914595);
    private static final float CIRCLE_RADIUS = 50.0f;
    private static final int CIRCLE_STROKE_COLOR = 0xFFFF0000;
    private static final float CIRCLE_STROKE_WIDTH = 0.0f;
    private static final int CIRCLE_FILL_COLOR = 0x88FF0000;
    private static final int CAMERA_ZOOM_LEVEL = 17;
    private static final int LOCATION_UPDATE_INTERVAL = 15000;
    private static final int LOCATION_UPDATE_FASTEST_INTERVAL = 5000;


    // variables
    private GoogleMap m_map;
    private GoogleApiClient m_api_client;
    private Circle m_circle_home;
    private Circle m_circle_brooks;
    private Circle m_circle_polk;
    private Circle m_circle_well;
    private boolean m_first_location_update;
    private MediaPlayer m_media_player;

    // functiosn
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // create and set view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // waiting for first location
        this.m_first_location_update = true;

        // create the media player
        this.m_media_player = null;
        // do the below to setup streaming
        /*
        this.m_media_player = new MediaPlayer();
        this.m_media_player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        String url = "url to mp3 file here";
        try{
            this.m_media_player.setDataSource(url);
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.m_media_player.prepare();
        } catch(Exception e){
            e.printStackTrace();
        }
        this.m_media_player.start();
        */

        // setup the map
        this.create_map();

        // api client not yet
        this.create_google_api();
    }

    private void create_map(){
        this.m_map = null;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void create_google_api(){
        this.m_api_client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart(){
        Log.v("DEBUG", "onStart");

        // connect the api client
        if (this.m_api_client != null) {
            this.m_first_location_update = true;
            this.m_api_client.connect();
            if (this.m_media_player != null){
                this.m_media_player.start();
            }
        }

        this.m_first_location_update = true;

        super.onStart();
    }

    @Override
    protected void onStop(){
        Log.v("DEBUG", "onStop");

        // disconnect the api client
        if (this.m_api_client != null) {
            if (this.m_media_player != null){
                this.m_media_player.pause();
            }
            this.m_api_client.disconnect();
        }

        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.v("DEBUG", "MapsActivity::onMapReady");

        // save the map
        this.m_map = googleMap;

        // enable our location button
        try {
            this.m_map.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();

        }

        // add the markers for the locations to visit
        this.m_map.addMarker(new MarkerOptions().position(MapsActivity.LOCATION_BROOKS).title("Brooks Hall Entrance"));
        this.m_map.addMarker(new MarkerOptions().position(MapsActivity.LOCATION_POLK).title("Polk Place"));
        this.m_map.addMarker(new MarkerOptions().position(MapsActivity.LOCATION_WELL).title("Old Well"));
        this.m_map.addMarker(new MarkerOptions().position(MapsActivity.LOCATION_HOME).title("Home"));
    }

    @Override
    public void onConnected(Bundle connection_hint){
        Log.v("DEBUG", "MapsActivity::onConnected");

        // get the last known location
        Location last_location = null;
        try{
            last_location = LocationServices.FusedLocationApi.getLastLocation(this.m_api_client);
        } catch (SecurityException e){
            Log.v("DEBUG", e.toString());
            e.printStackTrace();
        }

        // convert to lat/long and move camera there
        if (last_location != null){
            Log.v("DEBUG", "Last Known Location: " + last_location.toString());

            // convert the last known location to lat/lon
            LatLng lat_lng = new LatLng(last_location.getLatitude(), last_location.getLongitude());

            ((TextView)this.findViewById(R.id.textview_coords)).setText("Last Known: " +
                    Double.toString(lat_lng.latitude) + ", " + Double.toString(lat_lng.longitude));
            ((TextView)this.findViewById(R.id.textview_address)).setText("Address: Pending");

            // move the camera to center on that location
            if (this.m_map != null) {
                this.m_map.moveCamera(CameraUpdateFactory.newLatLngZoom(lat_lng, CAMERA_ZOOM_LEVEL));
            }
        }

        // now setup how often to receive updates of location
        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(LOCATION_UPDATE_INTERVAL)
                .setFastestInterval(LOCATION_UPDATE_FASTEST_INTERVAL);

        // now request periodic updates
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(this.m_api_client, request, this);
        } catch (SecurityException e){
            Log.v("DEBUG", "LocationServices.FusedLocationApi.requestLocationUpdates: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int value){
        Log.v("DEBUG", "MapsActivity::onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result){
        Log.v("DEBUG", "MapsActivity::onConnectionFailed");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v("DEBUG", "MapsActivity::onLocationChanged");

        // get our current lat/long
        LatLng lat_lng = new LatLng(location.getLatitude(), location.getLongitude());

        // move the camera to center on that location if this is the first update
        if (this.m_first_location_update) {
            Log.v("DEBUG", "first location update: " + location.toString());
            this.m_map.moveCamera(CameraUpdateFactory.newLatLngZoom(lat_lng, CAMERA_ZOOM_LEVEL));
            this.m_first_location_update = false;
        }

        // update coords textview
        ((TextView)this.findViewById(R.id.textview_coords)).setText(
                Double.toString(lat_lng.latitude) + ", " + Double.toString(lat_lng.longitude));

        // get the address from a geocoder and show in the textview
        // need to thread it because if no or slow network connection this will hang the app
        // for several seconds until it timesout
        new AsyncTask<Location, Void, Address>()
        {
            @Override
            protected Address doInBackground(Location... locations)
            {
                try
                {
                    Geocoder g = new Geocoder(MapsActivity.this, Locale.getDefault());
                    List<Address> la = g.getFromLocation(locations[0].getLatitude(), locations[0].getLongitude(), 1);
                    if (la.size() > 0) {
                        return la.get(0);
                    }
                }
                catch (IOException e)
                {
                    Log.v("DEBUG", "Geocoder.getFromLocation: " + e.toString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Address address)
            {
                if (address != null){
                ((TextView) MapsActivity.this.findViewById(R.id.textview_address)).setText(
                        address.getAddressLine(0) + "\n" +
                        address.getAddressLine(1) + "\n" +
                        ((address.getFeatureName().equals(address.getSubThoroughfare()) == false) ? address.getFeatureName() : ""));
                }
                else{
                    ((TextView) MapsActivity.this.findViewById(R.id.textview_address)).setText("Unable to resolve address");
                }
            }
        }.execute(location);

        // check distance to each location
        // if within range then draw circle and play song, else turn off circle and stop song
        // first to home
        this.m_circle_home = this.check_range(lat_lng, LOCATION_HOME, this.m_circle_home, ELocation.HOME);
        this.m_circle_brooks = this.check_range(lat_lng, LOCATION_BROOKS, this.m_circle_brooks, ELocation.BROOKS);
        this.m_circle_polk = this.check_range(lat_lng, LOCATION_POLK, this.m_circle_polk, ELocation.POLK);
        this.m_circle_well = this.check_range(lat_lng, LOCATION_WELL, this.m_circle_well, ELocation.WELL);

        // see if not in range of anything
        if ((this.m_circle_home == null) && (this.m_circle_brooks == null) &&
                (this.m_circle_polk == null) && (this.m_circle_well == null)){
            // make sure no song is playing
            if (this.m_media_player != null) {
                this.m_media_player.stop();
            }
        }
    }

    private Circle check_range(LatLng my_location, LatLng destination_latlng, Circle circle, ELocation location){
        // buffer for the results
        float results[] = new float[1];

        // calculate the distance
        Location.distanceBetween(my_location.latitude, my_location.longitude,
                destination_latlng.latitude, destination_latlng.longitude,
                results);

        // see if within range
        if (results[0] <= CIRCLE_RADIUS){
            Log.v("DEBUG", "IN-RANGE " + location.toString() + ": " + Float.toString(results[0]));
            // within range destination
            if (circle == null){
                // means we just got here
                // so create the circle, put it on the map
                // and start the song
                if (this.m_map != null) {
                    Log.v("DEBUG", "Adding circle and starting song");
                    // put circle on map
                    CircleOptions co = new CircleOptions();
                    co.center(destination_latlng);
                    co.radius(CIRCLE_RADIUS);
                    co.fillColor(CIRCLE_FILL_COLOR);
                    co.strokeWidth(CIRCLE_STROKE_WIDTH);
                    co.strokeColor(CIRCLE_STROKE_COLOR);
                    circle = this.m_map.addCircle(co);

                    // start the song
                    this.play_song(location);
                }
            }
        }
        else{
            Log.v("DEBUG", "NOT IN-RANGE " + location.toString() + ": " + Float.toString(results[0]));
            // not within range destination
            if (circle != null){
                Log.v("DEBUG", "Removing circle and starting song");
                // erase the circle
                circle.remove();
                circle = null;
            }
        }
        return circle;
    }

    private void play_song(ELocation location){
        switch (location){
            case BROOKS:{
                if (this.m_media_player != null){
                    this.m_media_player.stop();
                }
                this.m_media_player = MediaPlayer.create(this, R.raw.song_brooks);
                this.m_media_player.setLooping(true);
                this.m_media_player.start();
            } break;
            case POLK:{
                if (this.m_media_player != null){
                    this.m_media_player.stop();
                }
                this.m_media_player = MediaPlayer.create(this, R.raw.song_polk);
                this.m_media_player.setLooping(true);
                this.m_media_player.start();
            } break;
            case WELL:{
                if (this.m_media_player != null){
                    this.m_media_player.stop();
                }
                this.m_media_player = MediaPlayer.create(this, R.raw.song_well);
                this.m_media_player.setLooping(true);
                this.m_media_player.start();
            } break;
            case HOME:{
                if (this.m_media_player != null){
                    this.m_media_player.stop();
                }
                this.m_media_player = MediaPlayer.create(this, R.raw.song_home);
                this.m_media_player.setLooping(true);
                this.m_media_player.start();
            } break;
        }
    }
}
