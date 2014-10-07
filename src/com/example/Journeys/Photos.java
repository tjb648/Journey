package com.example.Journeys;

import android.graphics.Bitmap;
import android.location.Location;

import java.util.ArrayList;

public class Photos {

    ArrayList<Bitmap> bmArray;
    ArrayList<String> captions;
    ArrayList<Location> photo_locale;
    Photos(){

        photo_locale = new ArrayList<Location>();
        bmArray = new ArrayList<Bitmap>();
        captions = new ArrayList<String>();

    }

}
