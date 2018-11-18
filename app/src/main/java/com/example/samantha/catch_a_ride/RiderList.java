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
 * RiderList
 * Addaptor that allows drivers to see a list of
 * ride requests from riders.
 */
public class RiderList extends ArrayAdapter {

    private Activity context;
    private List<Rider> riderList;

    public RiderList(Activity context, List<Rider> riderList)
    {
        super(context, R.layout.rider_layout, riderList);
        this.context = context;
        this.riderList = riderList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.rider_layout, null, true);

        TextView riderName = (TextView) view.findViewById(R.id.riderName);
        TextView riderStart = (TextView) view.findViewById(R.id.riderStart);
        TextView riderDest = (TextView) view.findViewById(R.id.riderDest);
        TextView riderExtra = (TextView) view.findViewById(R.id.riderExtra);

        Rider rider = riderList.get(position);

        riderName.setText(rider.getName());
        riderStart.setText(rider.getStart());
        riderDest.setText(rider.getDest());
        riderExtra.setText(rider.getExtra());

        return view;
    }
}
