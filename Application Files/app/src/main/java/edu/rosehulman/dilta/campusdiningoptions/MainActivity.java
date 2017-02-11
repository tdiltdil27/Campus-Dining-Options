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
import android.support.v4.app.FragmentTransaction;
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

    private String currentDate;
    private String focusedDate;

    private FavoritesAdapter mFavoritesAdapter;

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

        mFavoritesAdapter = new FavoritesAdapter();

        setDate();
        updateTitle();

        MainFragment main = MainFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, main);
        ft.commit();
    }
    
    public void setDate() {
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

    public void createCalendarDialog() {
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
                updateMainFragmentData();
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

    public void updateTitle() {
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

    public int getYear() {
        return mYear;
    }
    public int getDay() {
        return mDay;
    }
    public int getMonth() {
        return mMonth;
    }

    public void updateMainFragmentData() {
        MainFragment frag = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.content_main);
        if (frag != null) {
            frag.getData();
        }
    }

    public FavoritesAdapter getFavoritesAdapter() {
        return this.mFavoritesAdapter;
    }
}
