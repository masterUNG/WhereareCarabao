package appewtc.masterung.wherearecarabao;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    //Explicit
    private TextView txtLat, txtLong;
    private LocationManager objLocationManager;
    private Criteria objCriteria;
    private boolean bolGPS, bolNetword;
    private int intMapType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Widget
        bindWidget();

        //Open Service For Get Location
        objLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Create Criteria
        createCriteria();

    }   // onCreate

    @Override
    protected void onStart() {
        super.onStart();

        bolGPS = objLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!bolGPS) {
            bolNetword = objLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }   // if1
        if (bolNetword) {

            //Open Internet
            Intent objIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(objIntent);

        }   // if2

    }

    @Override
    protected void onResume() {
        super.onResume();

        setUpAll();

    }

    private void setUpAll() {

        objLocationManager.removeUpdates(objLocationListener);
        String strLatitute = "Unknow";
        String strLongtitute = "Unknow";

        Location objNetworkLocation = requestUpdateFromProvider(LocationManager.NETWORK_PROVIDER, "Cannot Connected Internet");
        if (objNetworkLocation != null) {
            strLatitute = String.format("%.7f", objNetworkLocation.getLatitude());
            strLongtitute = String.format("%.7f", objNetworkLocation.getLongitude());
        }

        Location objGPSLocation = requestUpdateFromProvider(LocationManager.GPS_PROVIDER, "Cannot Connected GPS");
        if (objGPSLocation != null) {
            strLatitute = String.format("%.7f", objGPSLocation.getLatitude());
            strLongtitute = String.format("%.7f", objGPSLocation.getLongitude());
        }

        txtLat.setText(strLatitute);
        txtLong.setText(strLongtitute);


    }   // setUpAll

    @Override
    protected void onStop() {
        super.onStop();

        objLocationManager.removeUpdates(objLocationListener);

    }

    //Get Location from GPS & ISP
    public Location requestUpdateFromProvider(final String strPrivider, String strError) {

        Location objLocation = null;
        if (objLocationManager.isProviderEnabled(strPrivider)) {

            objLocationManager.requestLocationUpdates(strPrivider, 1000, 10, objLocationListener);
            objLocation = objLocationManager.getLastKnownLocation(strPrivider);

        } else {
            Log.d("MyLocation", "Error ==> " + strError);
        }   // if

        return objLocation;
    }


    //Create LocationListener
    public final LocationListener objLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            txtLat.setText(String.format("%.7f", location.getLatitude()));
            txtLong.setText(String.format("%.7f", location.getLongitude()));

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    private void createCriteria() {
        objCriteria = new Criteria();
        objCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        objCriteria.setAltitudeRequired(false);
        objCriteria.setBearingRequired(false);
    }

    private void bindWidget() {
        txtLat = (TextView) findViewById(R.id.txtLatitude);
        txtLong = (TextView) findViewById(R.id.txtLongtitude);
    }

    public void clickShowMap(View view) {

        //Get Lat, Lng
        double douLat = Double.parseDouble(txtLat.getText().toString());
        double douLng = Double.parseDouble(txtLong.getText().toString());

        Intent objIntent = new Intent(MainActivity.this, MapsActivity.class);
        objIntent.putExtra("Lat", douLat);
        objIntent.putExtra("Lng", douLng);
        objIntent.putExtra("MapType", intMapType);
        startActivity(objIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itemNormal:
                intMapType = 1;
                break;
            case R.id.itemSatellite:
                intMapType = 2;
                break;
            case R.id.itemTerrain:
                intMapType = 3;
                break;
            case R.id.itemHybrid:
                intMapType = 4;
                break;
            default:
                intMapType = 1;
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}   // Main Class
