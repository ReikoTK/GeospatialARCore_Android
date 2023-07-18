package com.google.ar.core.examples.java.geospatial;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {
    private Button StartBtn;
    private LottieAnimationView AnimationIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        AnimationIcon = findViewById(R.id.IconAnimation);
        AnimationIcon.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {
                //null
            }

            //On End Auto Change to the next screen
            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                    openGeoActivity();
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {
                //null
            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {
                //null
            }
        });
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
