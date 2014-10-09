package com.example.Journeys;

import android.graphics.Bitmap;
import android.location.Location;
import android.provider.ContactsContract;

import java.util.ArrayList;

public class Photos {

    Bitmap bm;
    String captions;
    Location photo_locale;
    Photos(){

        photo_locale = new Location("dummyprovider");

        captions = new String();

    }

    public Bitmap getBm(){

        return bm;
    }
    public String getCaptions(){

        return captions;
    }
    public Location getPhoto_locale(){

        return photo_locale;
    }

    public void addPhoto(Bitmap Bm){

        bm = Bm;

    }
    public void addCaption(String cp){

        captions =cp ;
    }
    public void addPhotoLocale(Location loc){

        photo_locale = loc;
        
    }
    public void add(Bitmap bm, String cap, Location pL){

       // bmArray.add(bm);
        //captions.add(cap);
       // photo_locale.add(pL);
    }
}
