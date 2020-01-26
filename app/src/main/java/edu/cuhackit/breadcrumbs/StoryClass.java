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

    public String getCap(){
        return caption;
    }

    public double getLat(){
        return lat;
    }

    public double getLng(){
        return lng;
    }

    public String getId(){
        return id;
    }

    public void setImg(Bitmap b){
        img = b;
    }

    public Bitmap getImg(){
        return img;
    }

}
