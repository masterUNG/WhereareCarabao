package appewtc.masterung.wherearecarabao;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private double douOwnerLat, douOwnerLng;
    private LatLng ownerLatLng, carabaoLatLng,
            place1LatLng, place2LatLng, place3LatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        //Receive from Intent
        receiveFromIntent();

        //Create LatLng
        createLatLng();

        setUpMapIfNeeded();

    }   // onCreate

    private void createLatLng() {

        ownerLatLng = new LatLng(douOwnerLat, douOwnerLng);
        carabaoLatLng = new LatLng(13.72631573, 100.52887201);
        place1LatLng = new LatLng(13.72412701, 100.52936554);
        place2LatLng = new LatLng(13.72700361, 100.53076029);
        place3LatLng = new LatLng(13.726168, 100.528585);

    }   // createLatLng

    private void receiveFromIntent() {
        douOwnerLat = getIntent().getExtras().getDouble("Lat");
        douOwnerLng = getIntent().getExtras().getDouble("Lng");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }   // setUpMapIfNeeded


    private void setUpMap() {

        //Assign Center Map to Station Owner
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ownerLatLng, 15));

        //Create Maker
        createMaker();

        //Create Polyline
        createPolyLine();

        //Create Polygon
        createPolygon();

        //Click Active on Map
        clickActiveOnMap();

        //Create MapType
        createMapType();

    }   // setUpMap

    private void createMapType() {
        int intMyMapType = getIntent().getExtras().getInt("MapType");

        switch (intMyMapType) {
            case 1:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case 2:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case 3:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case 4:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
        }

    }

    private void clickActiveOnMap() {

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Lat = " + Double.toString(latLng.latitude))
                        .snippet("Lng = " + Double.toString(latLng.longitude)));
            }
        });

    }

    private void createPolygon() {
        PolygonOptions objPolygonOptions = new PolygonOptions();
        objPolygonOptions.add(place1LatLng)
                .add(place2LatLng)
                .add(place3LatLng)
                .add(place1LatLng)
                .strokeWidth(7)
                .strokeColor(Color.BLUE).fillColor(Color.argb(50, 185, 238, 105)).zIndex(10);
        mMap.addPolygon(objPolygonOptions);

    }

    private void createPolyLine() {
        PolylineOptions objPolylineOptions = new PolylineOptions();
        objPolylineOptions.add(place1LatLng)
                .add(place2LatLng)
                .add(place3LatLng)
                .add(place1LatLng)
                .width(5).color(Color.GREEN);
        mMap.addPolyline(objPolylineOptions);

    }

    private void createMaker() {

        mMap.addMarker(new MarkerOptions()
                .position(ownerLatLng)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.friend)));

        mMap.addMarker(new MarkerOptions()
                .position(carabaoLatLng)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.logo_carabao48)).title("คาราบาว แดง").snippet("โทร 0818595309"));

        mMap.addMarker(new MarkerOptions()
                .position(place1LatLng)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        mMap.addMarker(new MarkerOptions()
                .position(place2LatLng)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.build4)));

        mMap.addMarker(new MarkerOptions().position(place3LatLng));

    }   // createMaker
}   // Main Class
