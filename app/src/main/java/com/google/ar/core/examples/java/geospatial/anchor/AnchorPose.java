package com.google.ar.core.examples.java.geospatial.anchor;

import com.google.ar.core.examples.java.geospatial.EventTypesEnum;

public class AnchorPose{
    //These variable name have to be exactly the same as server Json
    public double latitude;
    public double longitude;
    public String name;
    public int type;
    public AnchorPose(double Latitude,double Longitude,String Name,int eventType){
        this.latitude = Latitude;
        this.longitude = Longitude;
        this.name = Name;
        this.type = eventType;
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
    public String getEventType(){
        return EventTypesEnum.createEventTypeEnum(type).name();
    }
}