package com.example.Journeys;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.PorterDuff;
import android.location.Criteria;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Tyson on 9/30/2014.
 */
public class CurrentFragment extends Fragment {


    Button bPhoto, bRec;
    private GoogleMap map ;
    Location lastKnown;
    String buffer;
    String locationProvider_network = LocationManager.NETWORK_PROVIDER;
    String locationProvider_GPS = LocationManager.GPS_PROVIDER;
    LocationManager locationManager;
    LocationListener locationListener;
    Journey journey = new Journey();

    ArrayList<Location> locale_buffer = new ArrayList<Location>();
    ArrayList<String> t_d = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View myFragmentView = inflater.inflate(R.layout.current_frag, container, false);

        //Get button and set colour
        bRec = (Button) myFragmentView.findViewById(R.id.bRec);
        bRec.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
       //Get button and set to invisible as user shouldnt be able to use
        //it if they havent started their jouney
        bPhoto = (Button) myFragmentView.findViewById(R.id.button);
        bPhoto.setVisibility(View.GONE);


        /*+++++++++++++++++++Photo capture code+++++++++++++++++++*/
        bPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        /*+++++++++++++++++++Button listeners+++++++++++++++++++*/
        bRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bRec.getText()== getResources().getString(R.string.start)){

                    bRec.setText(R.string.stop);
                    bPhoto.setVisibility(View.VISIBLE);
                    bRec.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
                    startJourney();

                }else {
                    bRec.setText(getResources().getString(R.string.start));
                    buffer = showDialogJourney(getView());
                    bPhoto.setVisibility(View.GONE);
                    bRec.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);

                    journey.endJourney(buffer);
                    locationManager.removeUpdates(locationListener);
                }
            }
        });

       /*+++++++++++++++++++Adding Google Map and updating map+++++++++++++++++++*/

        // Getting Google Play availability status
        int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

        if(available!= ConnectionResult.SUCCESS){ // Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(available, getActivity(), requestCode);
            dialog.show();

        }else { // Services are available

            // Getting GoogleMap object from the fragment
            map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

            // Enabling MyLocation Layer of Google Map
            map.setMyLocationEnabled(true);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

        }


        return myFragmentView;
    }

    @Override // Overiding Activity Result
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*+++++++++++++++++++Get image from camera, caption for image and location image was taken+++++++++++++++++++*/
        Bitmap bm = (Bitmap) data.getExtras().get("data");
        String caption = showDialogCaption(getView());
        String time_date = getDate();
        Location photo_location = getLocale();


           // Save image/caption/location to data model
           journey.addPhotos(bm, caption, photo_location);
            //For adding pin to google maps
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(0, 0))
                    .title(caption)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconphoto)));


    }

   public void startJourney(){

       locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

       locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Save new location and timestamp to db
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                String time_date = getDate();

                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(loc, 20);


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

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, locationListener);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 ,5, locationListener);



    }

    public String showDialogCaption(View view){


        final EditText input = new EditText(getActivity());

        AlertDialog alert = new AlertDialog.Builder(getActivity())
                .setTitle("Photo captured")
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
    public String showDialogJourney(View view){

        // Dialog box for end of journey, entering journey name
        final EditText input = new EditText(getActivity());

        AlertDialog alert = new AlertDialog.Builder(getActivity())
                .setTitle("Journey Complete")
                .setMessage("Please name your Journey")
                .setView(input)
                .setNegativeButton("Discard Journey", new DialogInterface.OnClickListener() {
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
        // Gets formatted date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        String time_date = simpleDateFormat.format(new Date());

        return time_date;
    }

    public Location getLocale(){
        // Gets last known location from available provider
        lastKnown = locationManager.getLastKnownLocation(locationProvider_GPS);

        if(lastKnown == null){
            System.out.println("No GPS location available");
            lastKnown = locationManager.getLastKnownLocation(locationProvider_network);
        }

        return lastKnown;
    }

}