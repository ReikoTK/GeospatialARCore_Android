package com.google.ar.core.examples.java.geospatial;

import static com.google.ar.core.examples.java.common.helpers.LocationPermissionHelper.requestFineLocationPermission;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.ar.core.examples.java.geospatial.anchorList.AnchorPose;
import com.google.ar.core.examples.java.geospatial.anchorList.AnchorViewAdapter;
import com.google.ar.core.examples.java.geospatial.anchorList.AnchorViewHolder;
import com.google.ar.core.examples.java.geospatial.filter.AnchorFilterButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnchorListingActivity extends AppCompatActivity implements OnMapReadyCallback {
    private RecyclerView anchorListing;
    private GoogleMap googleMap;
    private MapView mMapView;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private ArrayList<AnchorPose> poseList;
    private List<AnchorFilterButton> AllFilterBtn;
    //Marker list for filtering
    private List<Marker> CreatedMarkers = new ArrayList<>();
    private AnchorFilterButton FilterEventBtn;
    private AnchorFilterButton FilterFoodBtn;
    private AnchorFilterButton FilterShopBtn;
    private TextView FilterAllBtn;
    private Location lastKnownLocation;
    private TextView topTextView;
    private TextView arViewBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anchor_listing);
        topTextView = findViewById(R.id.topTextView);
        anchorListing = findViewById(R.id.myAnchorList);
        arViewBtn = findViewById(R.id.ARViewBtn);

        //GoogleMap stuff
        Bundle mapViewBundle = null;
        if(savedInstanceState != null){mapViewBundle = savedInstanceState.getBundle("MapViewBundleKey");}
        mMapView = findViewById(R.id.mapView);
        MapsInitializer.initialize(this,MapsInitializer.Renderer.LATEST,null);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);

        //GPS stuff
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestFineLocationPermission(this);
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        arViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openARActivity();
            }
        });

        AllFilterBtn = new ArrayList<>();
        //If there are many filter types may need to find another way to do this shit
        //Filtering buttons binding
        FilterEventBtn = new AnchorFilterButton(findViewById(R.id.FilterEventBtn),EventTypesEnum.イベント,this);
        FilterEventBtn.CreateOnClick(this);
        AllFilterBtn.add(FilterEventBtn);

        FilterFoodBtn= new AnchorFilterButton(findViewById(R.id.FilterFoodBtn),EventTypesEnum.レストラン,this);
        FilterFoodBtn.CreateOnClick(this);
        AllFilterBtn.add(FilterFoodBtn);

        FilterShopBtn= new AnchorFilterButton(findViewById(R.id.FilterShopBtn),EventTypesEnum.ショップ,this);
        FilterShopBtn.CreateOnClick(this);
        AllFilterBtn.add(FilterShopBtn);

        FilterAllBtn = findViewById(R.id.FilterAllBtn);
        FilterAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllSelection();
                FilterAllBtn.setBackgroundColor(getColor(R.color.deepBlue));
                //Markers Reset
                for (Marker m : CreatedMarkers){
                    m.setVisible(true);
                }
                //List Reset
                AnchorViewAdapter adapter = new AnchorViewAdapter(poseList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        anchorListing.setAdapter(adapter);
                    }
                });
            }
        });
    }

    public void StartHTTPCall() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpConn = null;
                try {
                    //testing on webhook
                    //for real implementation, need to do a "POST" method, give current location data to server for searching near area
                    URL url = new URL("https://webhook.site/58772d7b-43e2-42ee-9762-68ea141dd065");
                    httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setRequestMethod("GET");
                    httpConn.setUseCaches(false);
                    httpConn.setDoOutput(false);
                    httpConn.setDoInput(true);
                    httpConn.setRequestProperty("Content-Type", "application/json");
                    httpConn.connect();
                    if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        String outputString = convertInputStreamToString(httpConn.getInputStream());
                        Log.v("HTTPCall", "HttpConnectionABC=" + outputString);
                        Gson gson = new Gson();
                        poseList = gson.fromJson(outputString, new TypeToken<List<AnchorPose>>() {
                        }.getType());

                        topTextView.setText("近くに"+ poseList.size()+"件のイベントがあります。");

                        //Bind On layout finish
                        anchorListing.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                            }
                        });

                        //Start layout items
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
                } catch (IOException e) {
                    Log.e("HTTPCall", e.toString());
                }
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

    //For displaying multiple pins
    public void openARActivity(){
        Bundle extras = new Bundle();
        extras.putSerializable("PinDatas",poseList);
        Intent intent = new Intent(this, GeospatialActivity.class);
        intent.putExtra("PinDatas",extras);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    //For only display one Pin
    public void openARActivity(double latitude, double longitude, String Name, EventTypesEnum EventType) {
        AnchorPose pose = new AnchorPose(latitude,longitude,Name, EventType.getValue());
        ArrayList<AnchorPose> poseList = new ArrayList<>();
        poseList.add(pose);
        Bundle extras = new Bundle();
        extras.putSerializable("PinDatas",poseList);
        Intent intent = new Intent(this, GeospatialActivity.class);
        intent.putExtra("PinDatas", extras);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    //Google Map stuff
    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("Location Services","Error, no location permission granted");
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    lastKnownLocation = location;
                    Log.e("LOCATION",String.valueOf(location.getLatitude()) + " " + String.valueOf(location.getLongitude()));
                    LatLng currentPos = new LatLng(location.getLatitude(),location.getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.zoomTo(19f));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentPos));
                    googleMap.addMarker(new MarkerOptions().position(currentPos).icon(BitmapDescriptorFactory.fromAsset("models/roundpin.jpg")));
                }
            }
        });
        StartHTTPCall();
    }

    public void addMarkerToMap(double latitude, double longitude, String Name, View v,EventTypesEnum eventType){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                LatLng point = new LatLng(latitude,longitude);

                Marker m = googleMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title(Name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

                googleMarkerTag tag = new googleMarkerTag(Name,v,eventType);
                m.setTag(tag);
                //add to filter list
                CreatedMarkers.add(m);

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        googleMarkerTag tag = (googleMarkerTag) marker.getTag();
                        resetViewHolderColor();
                        tag.anchorViewHolder.findViewById(R.id.LLClickable).setBackgroundColor(Color.argb(1,0.6f,0.6f,1f));
                        return false;
                    }
                });
            }
        });
    }

    public void focusToMarker(double lat,double lng){
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lng)));
    }

    public void resetViewHolderColor(){
        for (int i = 0; i < anchorListing.getChildCount(); i++) {
            AnchorViewHolder holder = (AnchorViewHolder) anchorListing.getChildViewHolder(anchorListing.getChildAt(i));
            holder.itemView.findViewById(R.id.LLClickable).setBackgroundColor(Color.argb(1,0.19f,0.19f,0.19f));
        }
    }

    public float calcDist(double latitude, double longitude){
        float[] result = new float[2];
        Location.distanceBetween(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude(),latitude,longitude,result);
        return result[0];
    }

    public void filterList(EventTypesEnum eventType){
        //Markers
        for(Marker m : CreatedMarkers){
            googleMarkerTag tag = (googleMarkerTag) m.getTag();
            if (tag.eventType != eventType){
                m.setVisible(false);
            }else{
                m.setVisible(true);
            }
        }

        //List
        List<AnchorPose> newList = new ArrayList<>();
        for (AnchorPose pose: poseList) {
            if(pose.type == eventType.getValue()){
                newList.add(pose);
            }
        }
        AnchorViewAdapter adapter = new AnchorViewAdapter(newList);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                anchorListing.setAdapter(adapter);
            }
        });
    }

    public void clearAllSelection(){
        FilterAllBtn.setBackgroundColor(getColor(R.color.lightBlue));

        for (AnchorFilterButton btn:AllFilterBtn) {
            btn.clearSelection();
        }
    }

    public boolean markerExistOnMap(String ID){
        for(Marker m : CreatedMarkers){
            googleMarkerTag tag = (googleMarkerTag) m.getTag();
            //FOR REAL IMPLEMENTATION CREATE A UID FOR EACH TAG INSTEAD OF USING THE NAME TO CHECK
            if(Objects.equals(tag.Name, ID)){
                return true;
            }
        }
        return false;
    }
}