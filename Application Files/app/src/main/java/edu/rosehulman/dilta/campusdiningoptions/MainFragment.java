package edu.rosehulman.dilta.campusdiningoptions;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private int numPages = 2;
    private final static String ARG_URL = "https://campus-meal-scraper.herokuapp.com/locations/%d-%s-%s/";
    private static final String ARG_UNION = "Union Cafe";
    private MainFragment.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    // TODO: Rename and change types of parameters

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mSectionsPagerAdapter = new MainFragment.SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    public void getData() {
        MainActivity act = (MainActivity) getActivity();
        new getLocationsTask().execute(String.format(
                ARG_URL,
                act.getYear(),
                act.getMonth() < 10 ? "0" + act.getMonth() : act.getMonth(),
                act.getDay() < 10 ? "0" + act.getDay() : act.getDay())
        );
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public MealsFragment mMealTimeFragment;
        public HoursFragment mHoursFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position==0) {
                mMealTimeFragment = MealsFragment.newInstance();
                return mMealTimeFragment;

            } else if(position==1) {
                mHoursFragment = HoursFragment.newInstance();
                return mHoursFragment;
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            // Show total pages
            return numPages;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if(position == 0) {
                return "Cafe Menu";
            } else if (position == 1) {
                return "Location Hours";
            } else {
                return null;
            }
        }
    }

    public class getLocationsTask extends AsyncTask<String, Void, List<Location>> {

        public getLocationsTask() {
        }

        @Override
        protected List<Location> doInBackground(String... urlStrings) {
            List<Location> locations = new ArrayList<Location>();
            String urlString = urlStrings[0];
            try {
                locations = new ObjectMapper().readValue(new URL(urlString), new TypeReference<List<Location>>() {});
            } catch (IOException e) {

            }
            return locations;
        }
        @Override
        protected void onPostExecute(List<Location> locations) {
            super.onPostExecute(locations);
            onLocationsLoaded(locations);
        }

        public void onLocationsLoaded(List<Location> locations) {
            mSectionsPagerAdapter.mHoursFragment.getAdapter().setData(locations);
            mSectionsPagerAdapter.mHoursFragment.getAdapter().notifyDataSetChanged();


            List<MealTime> mealTimes = new ArrayList<>();

            for(int i = 0; i < locations.size(); i++) {
                if(locations.get(i).getName().equals(ARG_UNION)) {
                    mealTimes = locations.get(i).getMealTimes();
                }
            }

            mSectionsPagerAdapter.mMealTimeFragment.getAdapter().setData(mealTimes);
            mSectionsPagerAdapter.mMealTimeFragment.getAdapter().notifyDataSetChanged();
        }
    }



}
