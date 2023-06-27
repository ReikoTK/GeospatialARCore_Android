package com.google.ar.core.examples.java.geospatial.anchor;

public class AnchorPose{
    public double latitude;
    public double longitude;
    public String name;
    public AnchorPose(double Latitude,double Longitude,String Name){
        this.latitude = Latitude;
        this.longitude = Longitude;
        this.name = Name;
    }

    public String getName() {
        return name;
    }
    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
}