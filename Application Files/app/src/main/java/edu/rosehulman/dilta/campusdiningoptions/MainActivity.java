package edu.rosehulman.dilta.campusdiningoptions;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.DatePicker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class MainActivity extends AppCompatActivity {

    private static final String CALENDAR_DIALOG_TITLE = "Choose a date";
    private static final String ARG_UNION = "Union Cafe";
    private final static String ARG_URL = "https://campus-meal-scraper.herokuapp.com/locations/%d-%s-%s/";


    private String currentDate;
    private String focusedDate;
    private int numPages = 2;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private DatePickerDialog datePicker;

    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDay;

    public int mYear;
    public int mMonth;
    public int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setDate();
        updateTitle();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        getData();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void getData() {
        new getLocationsTask().execute(String.format(ARG_URL, getYear(), getMonth()<10?"0"+getMonth():getMonth(), getDay()<10?"0"+getDay():getDay()));
    }

    private void setDate() {
        Calendar calendar = GregorianCalendar.getInstance();
        mCurrentMonth = calendar.get(Calendar.MONTH)+1;
        mCurrentDay = calendar.get(Calendar.DAY_OF_MONTH);
        mCurrentYear = calendar.get(Calendar.YEAR);

        mMonth = calendar.get(Calendar.MONTH)+1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mYear = calendar.get(Calendar.YEAR);

        currentDate = "Today, " + (mCurrentMonth) + "/" + mCurrentDay +"/"+ mCurrentYear;
        focusedDate = "Today, " + (mMonth) + "/" + mDay +"/"+ mYear;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_calendar) {

            createCalendarDialog();
            return true;
        } else if (id == R.id.action_refresh) {
            mSectionsPagerAdapter.mMealTimeFragment.getAdapter().notifyDataSetChanged();
            mSectionsPagerAdapter.mHoursFragment.getAdapter().notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    private void createCalendarDialog() {
        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear+1;
                mDay = dayOfMonth;
            }
        }, mYear, mMonth-1, mDay );
//        builder.setTitle(CALENDAR_DIALOG_TITLE);

        datePicker.setButton(BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mDay = datePicker.getDatePicker().getDayOfMonth();
                mYear = datePicker.getDatePicker().getYear();
                mMonth = datePicker.getDatePicker().getMonth()+1;
                getData();
                updateTitle();
            }
        });

        datePicker.setButton(BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        });
        datePicker.show();
    }

    private void updateTitle() {
        focusedDate = "Today, " + (mMonth) + "/" + mDay +"/"+ mYear;

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(mYear, mMonth, mDay);
        String dayOfTheWeek = sdf.format(cal.getTime());

        if(focusedDate.equals(currentDate)) {

        } else {
            focusedDate = dayOfTheWeek + ", " + (mMonth) + "/" + mDay +"/"+ mYear;
        }
        setTitle(focusedDate);
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

    public int getYear() {
        return mYear;
    }
    public int getDay() {
        return mDay;
    }
    public int getMonth() {
        return mMonth;
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
