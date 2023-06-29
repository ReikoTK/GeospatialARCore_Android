package com.google.ar.core.examples.java.geospatial.anchor;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.ar.core.examples.java.geospatial.AnchorListingActivity;
import com.google.ar.core.examples.java.geospatial.R;

import java.text.DecimalFormat;
import java.util.List;

public class AnchorViewAdapter extends RecyclerView.Adapter<AnchorViewHolder>{
    private List<AnchorPose> list;
    AnchorListingActivity act;
    public AnchorViewAdapter(List<AnchorPose> list){
        this.list = list;
    }
    @Override
    public AnchorViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.anchorlist_row,parent,false);
        act = (AnchorListingActivity) view.getContext();
        return new AnchorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnchorViewHolder viewHolder,final int position ){
        viewHolder.NameView.setText(list.get(position).getName());
        viewHolder.LatView.setText(list.get(position).getLatitude() + "");
        viewHolder.LongView.setText(list.get(position).getLongitude() + "");
        viewHolder.latitude = list.get(position).getLatitude();
        viewHolder.longitude = list.get(position).getLongitude();
        viewHolder.Name = list.get(position).getName();
        act.addMarkerToMap(list.get(position).getLatitude(),list.get(position).getLongitude(),list.get(position).getName(),viewHolder.itemView);

        DecimalFormat df = new DecimalFormat("0.00");
        viewHolder.distanceView.setText(df.format(act.calcDist(list.get(position).latitude,list.get(position).longitude)) + "M");
    }

    @Override
    public int getItemCount(){
        return list.size();
    }
}