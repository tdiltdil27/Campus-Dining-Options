package edu.rosehulman.dilta.campusdiningoptions;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseUser;

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
    private static final String GUEST_TEXT = "a guest";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private int numPages = 2;
    private final static String ARG_URL = "https://campus-meal-scraper.herokuapp.com/locations/%d-%s-%s/";
    private static final String ARG_UNION = "Union Cafe";
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private HoursFragment hoursFragment;
    private MealsFragment mealsFragment;
    private ViewPager mViewPager;
    private MainActivity activity;
    private FavoritesAdapter favoritesAdapter;

    private boolean loggedIn;
    private List<Location> mLocations;
    private List<MealTime> mMealTimes;
    public Menu mMenu;
    private String username;

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
            username = getArguments().getString(Constants.FIREBASE_NAME);
        } else {
            username = null;
        }
        //getData();

        activity = (MainActivity) getActivity();
        Log.d("MainFragment", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);

        loggedIn = activity.loggedIn();
        Log.d("MainFragment", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_main, container, false);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.getAdapter().notifyDataSetChanged();

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        setHasOptionsMenu(true);
        activity.updateTitle();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("MainFrag", "getting data");
        getData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mMenu = menu;
        inflater.inflate(R.menu.menu_main, menu);
        mMenu.findItem(R.id.login).setTitle(getString(loggedIn?R.string.logout:R.string.action_sign_in));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_calendar) {

            activity.createCalendarDialog();
            return true;
        } else if (id == R.id.action_refresh) {
            activity.updateMainFragmentData();
        } else if (id == R.id.action_favorites) {
            FavoritesFragment favorites = FavoritesFragment.newInstance(activity.getFavoritesAdapter());
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, favorites);
            ft.addToBackStack("favorites");
            ft.commit();
        } else if (id == R.id.login) {
            if(!activity.loggedIn()) {
                activity.getSupportActionBar().hide();
                LoginFragment login = new LoginFragment();
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, login);
                ft.addToBackStack("login");
                ft.commit();
            } else {
                activity.logOut();
                Snackbar.make(getView(), "You have logged out", Snackbar.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
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

    public void setFavoritesAdapter(FavoritesAdapter favoritesAdapter) {
        this.favoritesAdapter = favoritesAdapter;
    }

    public void setUserInfo(String path, String user, String uid) {

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position==0) {
                Log.d("MainFrag", "new instance of mealsfragment");
                mealsFragment = MealsFragment.newInstance();
                return mealsFragment;
            } else if(position==1) {
                Log.d("MainFrag", "new instance of hoursfragment");
                hoursFragment = HoursFragment.newInstance();
                return hoursFragment;
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
            Log.d("MainFrag", "in doInBackground");
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
            Log.d("MainFrag", "postExecute");
            onLocationsLoaded(locations);
        }

        public void onLocationsLoaded(List<Location> locations) {
            Log.d("MainFrag", "locationsLoaded");
            hoursFragment.getAdapter().setData(locations);
            hoursFragment.getAdapter().notifyDataSetChanged();

            List<MealTime> mealTimes = new ArrayList<>();

            for(int i = 0; i < locations.size(); i++) {
                if(locations.get(i).getName().equals(ARG_UNION)) {
                    mealTimes = locations.get(i).getMealTimes();
                }
            }

            mLocations = locations;
            mMealTimes = mealTimes;

            mealsFragment.getAdapter().setData(mealTimes,favoritesAdapter.getFavorites());
            mealsFragment.getAdapter().notifyDataSetChanged();
        }
    }
}
