package com.thienquach.demogooglemap;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        useMyLocBtn = (Button) findViewById(R.id.useMyLocBtn);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        searchText = (EditText) findViewById(R.id.searchText);
        doctorLV = (ListView) findViewById(R.id.doctorLV);

        //get location service
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        useMyLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1000, mLocationListener);
                mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

//                if(mLocation != null){
//                    Toast.makeText(getApplicationContext(), "Latitude: " + mLocation.getLatitude() + ", Longitude: " + mLocation.getLongitude() + ", Altitude: " +mLocation.getAltitude(), Toast.LENGTH_LONG).show();
//                }
                loadDoctorListView();
            }
        });

        initDoctorList();

    }

    private void initDoctorList(){
        doctorList = new ArrayList<>();

        doctorList.add(new Doctor("Chan Tai Man", "1 Cong Hoa, 13, Tan Binh, HCM, VN"));
        doctorList.add(new Doctor("Alex Quach", "10 Cong Hoa, 13, Tan Binh, HCM, VN"));
        doctorList.add(new Doctor("Kevin", "20 Cong Hoa, 13, Tan Binh, HCM, VN"));
    }

    private void loadDoctorListView(){
        doctorLV.setAdapter(new DoctorAdapter(SearchActivity.this, doctorList));
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
