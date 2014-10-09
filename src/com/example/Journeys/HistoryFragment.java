package com.example.Journeys;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyson on 9/30/2014.
 */
public class HistoryFragment extends Fragment {

    /* This fragment should load a list view of Journeys from the datamodel
    * when a specific journey is clicked it will show the journey and any
    * photos taken whilst on the journey, however it doesnt function as planned*/

    GoogleMap map;
     public ViewPager Pager;
    Context context;
    List<Journey> journey = new ArrayList<Journey>();

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.list_view, container, false);


        journey.add(new Journey("First"));
        journey.add(new Journey("Second"));

        populateListView();
        registerClick();

        return myFragmentView;
    }

    private void populateListView(){

        ArrayAdapter<Journey> adapter = new MyListAdapter();
        ListView list = (ListView) getActivity().findViewById(R.id.listView);
        list.setAdapter(adapter);

    }
    public void loadSelected(Journey journey1){
        //Load the gallery/google map of the selected journey
        Fragment frag = getFragmentManager().findFragmentById(R.layout.history_fragment);

        android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();

        map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.mapHist)).getMap();
        // Add journey route to map
        for(int i = 0; i < journey1.locale.size()-1;i++){

            LatLng src = journey1.locale.get(i);
            LatLng destination = journey1.locale.get(i+1);

            map.addPolyline(new PolylineOptions()
                    .add(new LatLng(src.latitude, src.longitude),
                         new LatLng(destination.latitude, destination.longitude))
                    .width(2).color(Color.BLUE).geodesic(true));

        }
       // View images from current journey in slideable gallery
        Pager = (ViewPager) getActivity().findViewById(R.id.galleryPager);
        ImageAdapter adapter = new ImageAdapter(context);
        Pager.setAdapter(adapter);

    }
    private class MyListAdapter extends ArrayAdapter<Journey>{

        public MyListAdapter(){

            super(context, R.layout.item_view, journey );
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            //Make sure View exists
            if(itemView == null){ //inflate our itemview
                 itemView = getActivity().getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;



            //Find Object to work with
             Journey currentJourney = journey.get(position);
            Bitmap bm = currentJourney.photo.get(position).getBm();
             // Fill views
            ImageView imageView = (ImageView) itemView.findViewById(R.id.Icon);
            TextView textView = (TextView) imageView.findViewById(R.id.jourName);
            textView.setText(currentJourney.getName());
            imageView.setImageBitmap(bm);


            if(bm!=null)
            {
                bm.recycle();
                bm=null;
            }

            return itemView;
        }
    }
    private void registerClick() {
        // This function finds which journey in the list view was clicked and loads the fragment
        ListView list = (ListView) getActivity().findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

                Journey journeyClicked = journey.get(position);

                String message = "You clicked Journey number" + position;
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                //
                loadSelected(journeyClicked);

            }
        });

    }

}

