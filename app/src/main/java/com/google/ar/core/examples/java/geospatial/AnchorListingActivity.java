package com.google.ar.core.examples.java.geospatial;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.ar.core.examples.java.geospatial.anchor.AnchorPose;
import com.google.ar.core.examples.java.geospatial.anchor.AnchorViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnchorListingActivity extends AppCompatActivity {
    private RecyclerView anchorListing;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anchor_listing);
        anchorListing = findViewById(R.id.myAnchorList);
        StartHTTPCall();
    }
    public void StartHTTPCall(){
        ExecutorService executor= Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpConn = null;
                try{
                    URL url = new URL("https://webhook.site/816e44e1-1495-4d9d-a777-9b513a266925");
                    httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setRequestMethod("GET");
                    httpConn.setUseCaches(false);
                    httpConn.setDoOutput(false);
                    httpConn.setDoInput(true);
                    httpConn.setRequestProperty("Content-Type","application/json");
                    httpConn.connect();
                    if(httpConn.getResponseCode()==HttpURLConnection.HTTP_OK){
                        String outputString = convertInputStreamToString(httpConn.getInputStream());
                        Log.v("HTTPCall","HttpConnectionABC=" + outputString);
                        Gson gson = new Gson();
                        List<AnchorPose> poseList = gson.fromJson(outputString,new TypeToken<List<AnchorPose>>(){}.getType());
                        AnchorViewAdapter adapter = new AnchorViewAdapter(poseList);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
                                anchorListing.setLayoutManager(llm);
                                anchorListing.setAdapter(adapter);
                            }
                        });
                    }
                }catch (IOException e){
                    Log.e("HTTPCall",e.toString());
                };
            }
        });
    }

    private static String convertInputStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }

    public void openARActivity(double latitude,double longitude,String Name){
        Intent intent = new Intent(this,GeospatialActivity.class);
        intent.putExtra("TargetLatitude", latitude);
        intent.putExtra("TargetLongitude",longitude);
        intent.putExtra("TargetName",Name);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
