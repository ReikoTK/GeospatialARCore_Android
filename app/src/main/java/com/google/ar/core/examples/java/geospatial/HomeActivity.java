package com.google.ar.core.examples.java.geospatial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    private Button StartBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        StartBtn = findViewById(R.id.HomeScreenStartBtn);
        StartBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openGeoActivity();
                    }
                }
        );
    }

    public void openGeoActivity(){
        Intent intent = new Intent(this,AnchorListingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
}
