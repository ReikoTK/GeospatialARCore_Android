package com.google.ar.core.examples.java.geospatial.filter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.google.ar.core.examples.java.geospatial.AnchorListingActivity;
import com.google.ar.core.examples.java.geospatial.EventTypesEnum;
import com.google.ar.core.examples.java.geospatial.R;

public class AnchorFilterButton {
    public TextView ButtonTextView;
    public EventTypesEnum EventType;
    private Context context;
    public void CreateOnClick(AnchorListingActivity act){
        ButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.clearAllSelection();
                displaySelection();
                act.filterList(EventType);
            }
        });
    }

    public AnchorFilterButton(TextView ButtonTextView,EventTypesEnum eventType,Context context){
        this.ButtonTextView = ButtonTextView;
        this.EventType = eventType;
        this.context = context;
    }

    public void displaySelection(){
        ButtonTextView.setBackgroundColor(context.getColor(R.color.deepBlue));
    }
    public void clearSelection(){
        ButtonTextView.setBackgroundColor(context.getColor(R.color.lightBlue));
    }
}
