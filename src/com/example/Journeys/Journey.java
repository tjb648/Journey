package com.example.Journeys;

import android.graphics.Bitmap;
import android.location.Location;
import android.provider.ContactsContract;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

/**
 * Created by Tyson on 10/1/2014.
 */
public class Journey {

    String name;
    ArrayList<Photos> photo;
    ArrayList<LatLng> locale;

    Journey(){

        name = new String();
        locale = new ArrayList<LatLng>();
        photo = new ArrayList<Photos>();

    }
    Journey(String name){
        this.name = name;
        locale = new ArrayList<LatLng>();
        photo = new ArrayList<Photos>();
    }

    void addPhotos(Bitmap bm, String cap, Location pL){

        // Add photo to Journey
        Photos buffer = new Photos();
        buffer.add(bm, cap, pL);
        photo.add(buffer);


    }
    void addLocation(LatLng LL){

        locale.add(LL);

    }

    void endJourney(String title){

        name = title;
    }

    public ArrayList<LatLng> getLocale(){

        return locale;
    }
    public String getName(){

        return name;
    }



}

