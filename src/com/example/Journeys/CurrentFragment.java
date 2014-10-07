package com.example.Journeys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import android.widget.EditText;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Tyson on 9/30/2014.
 */
public class CurrentFragment extends Fragment {


    Button bPhoto, bRec, bNote;
    private GoogleMap map;
    Location lastKnown;
    String buffer;
    String locationProvider_network = LocationManager.NETWORK_PROVIDER;
    String locationProvider_GPS = LocationManager.GPS_PROVIDER;
    LocationManager locationManager;
    LocationListener locationListener;
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    ArrayList<Location> locale_buffer = new ArrayList<Location>();
    ArrayList<String> t_d = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View myFragmentView = inflater.inflate(R.layout.current_frag, container, false);


       // map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        // for changing map location CameraUpdate update = CameraUpdateFactor.newLatLngZoom(LOCATION, 1-20);
        //for marker map.addMarker(new MarkerOptions().position(Location).title("Message");


        bPhoto = (Button) myFragmentView.findViewById(R.id.button);
        bRec = (Button) myFragmentView.findViewById(R.id.bRec);
        bNote = (Button) myFragmentView.findViewById(R.id.bNote);



        /*+++++++++++++++++++Photo capture code+++++++++++++++++++*/
        bPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        bRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(bRec.getText()== "Start Rec"){

                   bRec.setText("Stop Rec");
                 //  startJourney();

               }else {
                   bRec.setText("Start Rec");
                 //  locationManager.removeUpdates(locationListener);
               }
            }
        });



        return myFragmentView;
    }

    @Override // Overiding Activity Result
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bm = (Bitmap) data.getExtras().get("data");
        String caption = showDialog(getView());
        String time_date = getDate();
        Location photo_location = getLocale();


        //For adding pin to google maps
        /*mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            mMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Hello world"));*/

    }

   public void startJourney(){


       locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


       locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

               updateLocation(locale_buffer, t_d);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
       };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 ,5, locationListener);


    }

    public void updateLocation(ArrayList<Location> locale, ArrayList<String> time){

     lastKnown = getLocale();

     String time_date = getDate();

     locale.add(lastKnown);
     time.add(time_date);
    }

    public String showDialog(View view){


        final EditText input = new EditText(getActivity());

        AlertDialog alert = new AlertDialog.Builder(getActivity())
                .setTitle("Caption")
                .setMessage("Attach caption?")
                .setView(input)
                .setNegativeButton("No Thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buffer = "";

                    }
                }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        buffer = input.getText().toString();
                    }
                }).show();


            return buffer;
    }
    public String getDate(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        String time_date = simpleDateFormat.format(new Date());

        return time_date;
    }

    public Location getLocale(){

        lastKnown = locationManager.getLastKnownLocation(locationProvider_GPS);

        if(lastKnown == null){
            System.out.println("No GPS location available");
            lastKnown = locationManager.getLastKnownLocation(locationProvider_network);
        }

        return lastKnown;
    }
}