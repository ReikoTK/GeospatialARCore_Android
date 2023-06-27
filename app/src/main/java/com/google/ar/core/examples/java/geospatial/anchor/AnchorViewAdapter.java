package com.google.ar.core.examples.java.geospatial.anchor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.ar.core.examples.java.geospatial.R;

import java.util.List;

public class AnchorViewAdapter extends RecyclerView.Adapter<AnchorViewHolder>{
    private List<AnchorPose> list;
    public AnchorViewAdapter(List<AnchorPose> list){
        this.list = list;
    }
    @Override
    public AnchorViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.anchorlist_row,parent,false);
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
    }

    @Override
    public int getItemCount(){
        return list.size();
    }
}