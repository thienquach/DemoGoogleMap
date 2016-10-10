package com.thienquach.demogooglemap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Get a handle to the fragment
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        //set the callback on the fragment
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Enable the My Location layer on the map
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

        Intent intent = getIntent();
        String strAddress = intent.getStringExtra("address");
        String strName = intent.getStringExtra("name");


        //NOTE: the LatLng using here is com.google.android.gms.maps.model.LatLng
        //which is different from com.google.maps.model.LatLng
        LatLng doctorAddress = getLocationByAddress(this, strAddress);

        //add a marker into the map
        mMap.addMarker(new MarkerOptions().position(doctorAddress).title(strName));

        //move the camera to the marker, and zoom in
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(doctorAddress, 15.0f));


    }

    private LatLng getLocationByAddress(Context context, String strAddress){
        Geocoder coder = new Geocoder(context);

        List<Address> addresses;
        LatLng latLng = null;

        try{
            addresses = coder.getFromLocationName(strAddress, 5);
            if(addresses == null || addresses.size() < 0){
                return null;
            }
            Address location = addresses.get(0);
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
        }catch (Exception e){
            e.printStackTrace();
        }

        return latLng;

    }
}
