package com.google.ar.core.examples.java.geospatial.anchorList;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.google.ar.core.examples.java.geospatial.AnchorListingActivity;
import com.google.ar.core.examples.java.geospatial.EventTypesEnum;

import androidx.recyclerview.widget.RecyclerView;
import com.google.ar.core.examples.java.geospatial.R;

public class AnchorViewHolder extends RecyclerView.ViewHolder{
    public double latitude;
    public double longitude;
    public String Name;
    public EventTypesEnum EventType;
    public TextView NameView;
    public TextView LatView;
    public TextView LongView;
    public TextView distanceView;
    public AnchorViewHolder(View itemView){
        super(itemView);

        NameView = (TextView) itemView.findViewById(R.id.AnchorName);
        LatView  = (TextView) itemView.findViewById(R.id.AnchorLatitude);
        LongView = (TextView) itemView.findViewById(R.id.AnchorLongitude);
        AnchorListingActivity act = (AnchorListingActivity) itemView.getContext();
        itemView.findViewById(R.id.AnchorButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        act.openARActivity(latitude,longitude,Name,EventType);
                    }
                }
        );

        itemView.findViewById(R.id.LLClickable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.resetViewHolderColor();
                view.findViewById(R.id.LLClickable).setBackgroundColor(Color.argb(1,0.6f,0.6f,1f));
                act.focusToMarker(latitude,longitude);
            }
        });

        distanceView = itemView.findViewById(R.id.DistanceDisplay);
    }

}