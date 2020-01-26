package edu.cuhackit.breadcrumbs;

import android.graphics.Bitmap;

public class StoryClass {

    private double lat;
    private double lng;

    private Bitmap img;

    private String id;

    StoryClass(double la, double ln, String i){
        lat = la;
        lng = ln;
        id = i;
    }

    private double getLat(){
        return lat;
    }

    private double getLng(){
        return lng;
    }

    private String getId(){
        return id;
    }

}
