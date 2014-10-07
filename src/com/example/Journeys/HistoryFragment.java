package com.example.Journeys;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Tyson on 9/30/2014.
 */
public class HistoryFragment extends Fragment {

    public ViewPager Pager;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.history_fragment, container, false);


        Pager = (ViewPager) myFragmentView.findViewById(R.id.galleryPager);
        ImageAdapter adapter = new ImageAdapter(context);
        Pager.setAdapter(adapter);


        return myFragmentView;
    }







}

