package com.example.firefighter;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.firefighter.server.Connect;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainMapActivity extends AppCompatActivity {
    private static final long SLEEP =10000 ;
    GoogleMap googleMap;
    Connect connect;
    MediaPlayer mp;
    Preference preference;
    Double latitude,longtitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        createMapView();
        Process process=new Process();
        process.start();
    }

    public void goToViewPhoto(View view) {
        Intent i=new Intent(this,ActivityToViewPhoto.class);
        if(connect!=null){
            if(connect.photo!=null){
                i.putExtra("photo",connect.photo);
                startActivity(i);
            }
            else Toast.makeText(MainMapActivity.this, "Нет соединения с сервером, или нет фото!", Toast.LENGTH_LONG).show();
        }
    }

    public  class Process extends Thread{
        @Override
        public void run() {
            super.run();
      while(true){
                connect=new Connect(MainMapActivity.this);
                connect.getFire();
//          if(connect.location!=null&&connect.fireRate>0){
//                    latitude=connect.location.latitude;
//                    longtitude=connect.location.longitude;
//                    addMarker("Пожар уровень:"+connect.fireRate);
//                    animationCamera();
//                }
                try {
                    Thread.currentThread().sleep(SLEEP);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void call(){
          if(connect.location!=null&&connect.fireRate>0) {
              latitude = connect.location.latitude;
              longtitude = connect.location.longitude;
              addMarker("Пожар уровень:" + connect.fireRate);
              animationCamera();
          }
    }

    private void createMapView(){
        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {
            if(null == googleMap){
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if(null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map",Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception){
            Log.e("mapApp", exception.toString());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mp!=null){
            mp.stop();
            mp.release();
            mp=null;
        }

    }
    private void animationCamera(){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude,longtitude))
                .zoom(10)
                .bearing(45)
                .tilt(45)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.animateCamera(cameraUpdate);
    }
    private void addMarker(String text){
        /** Make sure that the map has been initialised **/
        if(null != googleMap){
//            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//                @Override
//                public void onInfoWindowClick(Marker marker) {
//                }
//            });
            googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.firesmaller))
                    .position(new LatLng(latitude,longtitude))
                    .title(text)
                    .draggable(true)
            );
            preference=new Preference(this);
            preference.loadPreference();
            if(preference.checkMusic) {
                if (mp == null) {
                    mp = MediaPlayer.create(this, R.raw.serena);
                }
                mp.start();
            }
        }
    }
    public void optionActivity(View view) {
        Intent i=new Intent(this,ConfigActivity.class);
        startActivity(i);
    }
}
