package edu.cuhackit.breadcrumbs;

import android.graphics.Bitmap;

public class StoryClass {

    private double lat;
    private double lng;

    private Bitmap img;

    private String id;

    private String caption;

    StoryClass(double la, double ln, String i, String c){
        lat = la;
        lng = ln;
        id = i;
        caption = c;
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
