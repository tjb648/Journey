package com.example.Journeys;

import android.app.ActionBar;

import android.app.FragmentTransaction;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;


public class mainActivity extends FragmentActivity implements ActionBar.TabListener {

    FragmentManager fragmentmanager;
    ViewPager myViewPager;
    ActionBar actionbar;
    LocationManager manager;
    JourneyDatabaseAdapter helper;
    User user;

    public User getUser(){

        return this.user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        helper = new JourneyDatabaseAdapter(this);
        fragmentmanager = getSupportFragmentManager();

       // long id = helper.insertData("Journey 1");
       // if(id<0){


      //  }





/*

        manager = (LocationManager)getSystemService(LOCATION_SERVICE);
        
        for(String prov: manager.getAllProviders()){
            System.out.println("Found Provider");
        }

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        List<String> providers = manager.getProviders(criteria, true);

        if (providers == null || providers.size() == 0){
            System.out.println("No GPS Service");
        }
        String preferred = providers.get(0);*/

       // manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

        myViewPager = (ViewPager) findViewById(R.id.pager);
        myViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        myViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
            actionbar.setSelectedNavigationItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        actionbar = getActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tabCurr = actionbar.newTab().setText("Current Journey");
        ActionBar.Tab tabHist = actionbar.newTab().setText("Journey Vault");
        ActionBar.Tab tabPlay = actionbar.newTab().setText("Replay Journey");

        tabCurr.setTabListener(this);
        tabHist.setTabListener(this);
        tabPlay.setTabListener(this);

        actionbar.addTab(tabCurr);
        actionbar.addTab(tabHist);
        actionbar.addTab(tabPlay);


    }


    /* ++++++++++ ACTIONBAR/SWIPER CODE +++++++++*/
    @Override
    protected void onSaveInstanceState(Bundle outState){
     super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());

    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        myViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    public void viewDatabase(View view){

        String data = helper.getData();
        System.out.println(data);

    }
}

class MyAdapter extends FragmentPagerAdapter {
    public MyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        Fragment fragment=null;
        if(i==0){
        fragment = new CurrentFragment();
        }
        if(i==1){
            fragment = new HistoryFragment();
        }
        if(i==2){
            fragment = new PlaybackFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }


}
