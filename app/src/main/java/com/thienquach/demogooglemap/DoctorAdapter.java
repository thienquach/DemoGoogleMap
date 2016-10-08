package com.thienquach.demogooglemap;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.thienquach.demogooglemap.model.Doctor;

import java.util.List;

/**
 * Created by thien.quach on 10/7/2016.
 */
public class DoctorAdapter extends BaseAdapter{

    private Context context;
    private List<Doctor> doctorList;

    public DoctorAdapter(Context context, List<Doctor> doctorList){
        this.context = context;
        this.doctorList = doctorList;
    }


    @Override
    public int getCount() {
        return doctorList.size();
    }

    @Override
    public Object getItem(int position) {
        return doctorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.doctor_list_item, null);
        }

        TextView doctorNameTV = (TextView) convertView.findViewById(R.id.doctorNameTV);
        TextView addressTV = (TextView) convertView.findViewById(R.id.addressTV);
        TextView distanceTV = (TextView) convertView.findViewById(R.id.distanceTV);

        doctorNameTV.setText(doctorList.get(position).getName());
        addressTV.setText(doctorList.get(position).getAddress());
        distanceTV.setText(doctorList.get(position).getDistance());
        return convertView;
    }
}
