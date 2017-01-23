package edu.rosehulman.dilta.campusdiningoptions;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class MainActivity extends AppCompatActivity {

    private static final String CALENDAR_DIALOG_TITLE = "Choose a date";
    private String currentDate;
    private String focusedDate;
    private int numPages = 2;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private MealTimeAdapter mAdapter;

    private DatePickerDialog datePicker;

    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDay;

    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setDate();
        updateTitle();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

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
        cal.set(mYear-1900, mMonth, mDay);
        String dayOfTheWeek = sdf.format(cal.getTime());

        if(focusedDate.equals(currentDate)) {

        } else {
            focusedDate = dayOfTheWeek + ", " + (mMonth) + "/" + mDay +"/"+ mYear;
        }
        setTitle(focusedDate);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private Context mContext;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position==0) {


                return MealsFragment.newInstance();

            } else if(position==1) {

                return HoursFragment.newInstance();
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

}
