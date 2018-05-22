package com.example.android.mynewsstand;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class NewsCategoryAdapter extends FragmentPagerAdapter {

    private Context mContext;


    public NewsCategoryAdapter(Context context, FragmentManager fm){

        super(fm);
        mContext = context;
    }


    @Override
    public Fragment getItem(int position){

        if(position == 0){
            return new EconomyFragment();
        }else if(position == 1){
            return new PoliticsFragment();
        }else if(position == 2){
            return new SportsFragment();
        }else{
            return new EnvironmentFragment();
        }
    }


    public int getCount(){return 4;}

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.cat_eco);
        } else if (position == 1) {
            return mContext.getString(R.string.cat_politics);
        } else if (position == 2) {
            return mContext.getString(R.string.cat_sports);
        } else {
            return mContext.getString(R.string.cat_environment);
        }

}}
