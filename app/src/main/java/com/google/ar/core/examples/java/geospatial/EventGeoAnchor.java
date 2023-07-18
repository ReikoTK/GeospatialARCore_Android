package com.google.ar.core.examples.java.geospatial;

import com.google.ar.core.Anchor;
import com.google.ar.core.examples.java.common.samplerender.Texture;
import com.google.ar.core.examples.java.geospatial.anchorList.AnchorPose;

//a wrapper to bind google.Anchor with event datas AnchorPose
public class EventGeoAnchor{
    public AnchorPose anchorPose;
    public Anchor anchor;
    public Texture TextTexture;
    public EventGeoAnchor(Anchor Ganchor,AnchorPose pose,Texture textTexture){
        anchorPose = pose;
        anchor = Ganchor;
        TextTexture = textTexture;
    }
}
