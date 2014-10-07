package com.example.Journeys;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.android.gms.plus.model.people.Person;

/**
 * Created by Tyson on 10/8/2014.
 */
public class ImageAdapter extends PagerAdapter {

    Context context;
    private int[] Images = new int[]{
            R.drawable.one , R.drawable.two, R.drawable.three,
            R.drawable.four};


    ImageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount(){
            return Images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(Images[position]);

        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){

        ((ViewPager) container).removeView((ImageView)object);
    }

}

