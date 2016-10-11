package com.thienquach.demogooglemap;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import com.thienquach.demogooglemap.model.Doctor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thien.quach on 10/7/2016.
 */
public class SearchActivity extends Activity {

    private Button useMyLocBtn;
    private EditText searchText;
    private Button searchBtn;
    private ListView doctorLV;

    private List<Doctor> doctorList;

    private LocationManager mLocationManager;
    private Location mLocation;
    private LatLng mUserLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        useMyLocBtn = (Button) findViewById(R.id.useMyLocBtn);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        searchText = (EditText) findViewById(R.id.searchText);
        doctorLV = (ListView) findViewById(R.id.doctorLV);



        initDoctorList();

        useMyLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                //get location service
                mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                //request current location from GPS
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1000, mLocationListener);
                mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                //Assign LatLng from GPS to User location
                mUserLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());

                //load the ListView
                loadDoctorListView(mUserLatLng);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //User should input their address manually
                if("".equals(searchText.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Please fille in your address.", Toast.LENGTH_LONG).show();
                    return;
                }
                //Get LatLng from address
                mUserLatLng = getLatLngByAddress(getApplicationContext(), searchText.getText().toString());

                //load the ListView
                loadDoctorListView(mUserLatLng);
            }
        });

        doctorLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Doctor doctor = (Doctor) doctorLV.getItemAtPosition(i);

                Intent intent = new Intent(SearchActivity.this, MapActivity.class);
                intent.putExtra("name", doctor.getName());
                intent.putExtra("address", doctor.getAddress());

                startActivity(intent);

            }
        });



    }

    /*Initialize a list mockup data*/
    private void initDoctorList(){
        doctorList = new ArrayList<>();

        doctorList.add(new Doctor("Chan Tai Man", "1 Cong Hoa, Phuong 4, Tan Binh, HCM, VN"));
        doctorList.add(new Doctor("Alex Quach", "1 Tran Binh Trong, Phuong 5, Binh Thanh, HCM, VN"));
        doctorList.add(new Doctor("Kevin", "20 Cong Hoa, Phuong 4, Tan Binh, HCM, VN"));
        doctorList.add(new Doctor("ABC", "364 Cong Hoa, Phuong 13, Tan Binh, HCM, VN"));
    }

    /*Load the ListView bases on Current location of user*/
    private void loadDoctorListView(LatLng userLatLng){
        if(doctorList != null && doctorList.size() > 0){
            for(Doctor doctor : doctorList){
                LatLng doctorLatLng = getLatLngByAddress(this, doctor.getAddress());

                //calculate the distance between user and doctor
                String distance = calculateUsingDistanceMatrixApi(userLatLng, doctorLatLng);

                //set distance for doctor
                doctor.setDistance(distance);
            }
        }
        doctorLV.setAdapter(new DoctorAdapter(SearchActivity.this, doctorList));
    }

    /**
     * This method calculate the distance based on 2 Geo points
     * */
    private double calculateDistance(LatLng userLatLng, LatLng doctorLatLng){
        Location userLocation = new Location("userLocation");
        userLocation.setLatitude(userLatLng.lat);
        userLocation.setLongitude(userLatLng.lng);

        Location doctorLocation = new Location("doctorLocation");
        doctorLocation.setLatitude(doctorLatLng.lat);
        doctorLocation.setLongitude(doctorLatLng.lng);

        return Math.round(userLocation.distanceTo(doctorLocation));
    }

    /***
     * This method call an API DistanceMatrix to request the distance between 2 Geo points
     *
     */
    private String calculateUsingDistanceMatrixApi(LatLng userLatLng, LatLng doctorLatLng){
        //this API key is the same with the one was defined in AndroidManifest
        GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyD7RsfuZJIvWycqdVxF6R-fxvHGCB8gyrU");

        DistanceMatrix matrix = null;
        try {
            //perform a GET request to get the distance
            matrix = DistanceMatrixApi.newRequest(context)
                    .origins(userLatLng)
                    .destinations(doctorLatLng).await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(matrix != null && "OK".equalsIgnoreCase(matrix.rows[0].elements[0].status.toString())) {
            return matrix.rows[0].elements[0].distance.humanReadable;
        }
        return "Cannot calculate the distance";
    }

    /**
     * This method retrieve LatLng by provided address
     * */
    private LatLng getLatLngByAddress(Context context, String strAddress){
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


    private final LocationListener mLocationListener = new LocationListener(){

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
