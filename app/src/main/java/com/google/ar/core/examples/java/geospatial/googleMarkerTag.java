package com.google.ar.core.examples.java.geospatial;

import android.view.View;

import com.google.ar.core.examples.java.geospatial.anchor.AnchorPose;
import com.google.ar.core.examples.java.geospatial.anchor.AnchorViewHolder;

public class googleMarkerTag {
    public String Name;
    public View anchorViewHolder;
    public EventTypesEnum eventType;
    public googleMarkerTag(String markerName, View holder, EventTypesEnum Type){
        Name = markerName;
        anchorViewHolder = holder;
        eventType = Type;
    }
}
