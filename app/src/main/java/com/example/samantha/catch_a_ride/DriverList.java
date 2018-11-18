package com.example.samantha.catch_a_ride;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

/*
 * Author: Val Lapensée-Rankine
 *
 * DriverList
 * Adaptor that allows riders to see a list of available
 * drivers.
 */
public class DriverList extends ArrayAdapter<Driver> {

    private Activity context;
    private List<Driver> driverList;

    public DriverList(Activity context, List<Driver> driverList)
    {
        super(context, R.layout.list_layout, driverList);
        this.context = context;
        this.driverList = driverList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.list_layout, null, true);

        TextView userName = (TextView) view.findViewById(R.id.textViewName);
        TextView userPaymentMethod = (TextView) view.findViewById(R.id.textViewPayment);

        Driver driver = driverList.get(position);
        final Driver temp = driver;
        userName.setText(driver.getDriverName());
        userPaymentMethod.setText(driver.getDriverPaymentType());

        return view;
    }
}