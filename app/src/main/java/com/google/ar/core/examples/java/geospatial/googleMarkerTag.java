package com.google.ar.core.examples.java.geospatial;

import android.view.View;

import com.google.ar.core.examples.java.geospatial.anchor.AnchorPose;
import com.google.ar.core.examples.java.geospatial.anchor.AnchorViewHolder;

public class googleMarkerTag {
    public String Name;
    public View anchorViewHolder;
    public googleMarkerTag(String markerName, View holder){
        Name = markerName;
        anchorViewHolder = holder;
    }
}
