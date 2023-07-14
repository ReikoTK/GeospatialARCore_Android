package com.google.ar.core.examples.java.geospatial;

public enum EventTypesEnum {
    イベント(0),
    レストラン(1),
    ショップ(2);

    private final int id;

    EventTypesEnum(int id) {
        this.id = id;
    }
    public static EventTypesEnum createEventTypeEnum(int id){
        for(EventTypesEnum e : values()){
            if(e.id == id){
                return e;
            }
        }
        throw new IllegalArgumentException("Invalid id for EventType : " + id);
    }

    public int getValue(){
        return this.id;
    }
}
