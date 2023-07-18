package com.google.ar.core.examples.java.geospatial.anchor;

import com.google.ar.core.examples.java.geospatial.EventTypesEnum;

public class AnchorPose{
    //These variable name and class have to be exactly the same as server Json !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public double latitude;
    public double longitude;
    public String name;
    public Integer type;
    public AnchorPose(double Latitude,double Longitude,String Name,Integer eventType){
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
    public EventTypesEnum getEventType(){
        return EventTypesEnum.createEventTypeEnum(type);
    }
}