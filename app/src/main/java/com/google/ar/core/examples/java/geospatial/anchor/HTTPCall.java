package com.google.ar.core.examples.java.geospatial.anchor;

import static android.content.ContentValues.TAG;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class HTTPCall {
    public String HTTPGetAllAnchorDataByJsonStr(String URL){
        AtomicReference<String> ReturnVal = new AtomicReference<>("");
        ExecutorService executor= Executors.newSingleThreadExecutor();
        executor.execute(new Runnable(){
            @Override
            public void run(){
                HttpURLConnection httpConn;
                try{
                    java.net.URL url = new URL(URL);
                    httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setRequestMethod("GET");
                    httpConn.setUseCaches(false);
                    httpConn.setDoOutput(false);
                    httpConn.setDoInput(true);
                    httpConn.setRequestProperty("Content-Type","application/json");
                    httpConn.connect();
                    if(httpConn.getResponseCode()==HttpURLConnection.HTTP_OK){
                        ReturnVal.set(convertInputStreamToString(httpConn.getInputStream()));
                    }
                }catch(IOException e){
                    Log.e(TAG,e.toString());
                }
            }
        });
        return ReturnVal.get();
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
}