package edu.rosehulman.dilta.campusdiningoptions;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
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
import android.view.Window;
import android.widget.CalendarView;
import android.widget.DatePicker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import edu.rosehulman.rosefire.Rosefire;
import edu.rosehulman.rosefire.RosefireResult;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginListener {

    private static final String CALENDAR_DIALOG_TITLE = "Choose a date";
    private static final String GUEST_TEXT = "a guest";
    private static final long NOTIFICATION_TIME = 43200000L;

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

    private ValueEventListener mListener;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private OnCompleteListener mOnCompleteListener;
    private FirebaseAuth mAuth;

    private boolean loggedIn;
    private int RC_ROSEFIRE_LOGIN = 2;
    private MainFragment mainFragment;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setDate();
        updateTitle();

        loggedIn = false;

        mainFragment = MainFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, mainFragment);
        ft.commit();

        updateFavoritesAdapter("");

        mAuth = FirebaseAuth.getInstance();

        initializeListeners();

        setTitle(getString(R.string.app_name));
    }

    public void setDate() {
        Calendar calendar = GregorianCalendar.getInstance();
        mCurrentMonth = calendar.get(Calendar.MONTH)+1;
        mCurrentDay = calendar.get(Calendar.DAY_OF_MONTH);
        mCurrentYear = calendar.get(Calendar.YEAR);

        mMonth = calendar.get(Calendar.MONTH)+1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mYear = calendar.get(Calendar.YEAR);

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d", Locale.US);
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(mCurrentYear, mCurrentMonth-1, mCurrentDay);
        currentDate = sdf.format(cal.getTime());
    }

    public void createCalendarDialog() {
        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                mYear = year;
//                mMonth = monthOfYear+1;
//                mDay = dayOfMonth;
            }
        }, mYear, mMonth-1, mDay );

        // Restricts date selection to one week after the current day
        final Calendar calendar = Calendar.getInstance();
        datePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());
        calendar.add(Calendar.DATE, 7);
        datePicker.getDatePicker().setMaxDate(calendar.getTimeInMillis());

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
        datePicker.setTitle("");
        datePicker.show();
    }

    public void updateTitle() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d", Locale.US);
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(mYear, mMonth-1, mDay);
        focusedDate = sdf.format(cal.getTime());

        if(focusedDate.equals(currentDate)) {
            focusedDate = "Today, " + focusedDate;
        }
        Log.d("MainActivity", "title: " + focusedDate);
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

    public void setupNotifications() {
        Intent intent = new Intent(MainActivity.this, Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC, this.NOTIFICATION_TIME, AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.d("MainActivity", "set notification alarm");
    }

    public FavoritesAdapter getFavoritesAdapter() {
        return this.mFavoritesAdapter;
    }

    public boolean loggedIn() {
        return loggedIn;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    private void initializeListeners() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                Log.d(Constants.TAG, "User: " + user);
                if (user!=null) {
                    loggedIn = true;
                    updateFavoritesAdapter(user.getUid());
//                    mFavoritesAdapter = new FavoritesAdapter(user.getUid());
//                    mainFragment.setFavoritesAdapter(mFavoritesAdapter);
                    switchToMainFragment("favorites/", user.getDisplayName(), user.getUid());
                } else {
//                    switchToLoginFragment();
                }
            }
        };

        mOnCompleteListener = new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(!task.isSuccessful()) {
                    showLoginError("Login failed");
                }
            }
        };
    }

    public void updateFavoritesAdapter(String uid) {
        mFavoritesAdapter = new FavoritesAdapter(uid);
        mainFragment.setFavoritesAdapter(mFavoritesAdapter);
        setupNotifications();
    }

    @Override
    public void onRosefireLogin() {
        Intent signInIntent = Rosefire.getSignInIntent(this, Constants.ROSEFIRE_REGISTRY_TOKEN);
        startActivityForResult(signInIntent, RC_ROSEFIRE_LOGIN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_ROSEFIRE_LOGIN) {
            RosefireResult result = Rosefire.getSignInResultFromIntent(data);
            if (result.isSuccessful()) {
                firebaseAuthWithRosefire(result);
            } else {
                showLoginError("Rosefire authentication failed.");
            }
        }
    }
    private void showLoginError(String message) {
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("login");
        loginFragment.onLoginError(message);
    }
    private void firebaseAuthWithRosefire(RosefireResult result) {
        mAuth.signInWithCustomToken(result.getToken())
                .addOnCompleteListener(mOnCompleteListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void onLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, mOnCompleteListener);
        loggedIn = true;
    }
    private void switchToLoginFragment() {
        getSupportActionBar().hide();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, new LoginFragment(), "Login");
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fm.popBackStackImmediate();
        ft.commit();
        getSupportActionBar().show();
    }

    private void switchToMainFragment(String path, String user, String uid) {
//        Log.d(Constants.TAG, "starting main fragment");
//        getSupportActionBar().show();
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        mainFragment = MainFragment.newInstance();
//
//        Bundle args = new Bundle();
//        args.putString(Constants.FIREBASE_PATH, path);
//        args.putString(Constants.FIREBASE_USER, uid);
//        args.putString(Constants.FIREBASE_NAME, user);
//        mainFragment.setArguments(args);
//
//        ft.replace(R.id.content_main, mainFragment, "Main");
//        ft.commit();

        onBackPressed();
        mainFragment.setUserInfo(path, user, uid);

    }

    public void logOut() {
        mAuth.getInstance().signOut();
        loggedIn = false;
        user = null;
        mainFragment.mMenu.findItem(R.id.login).setTitle(R.string.action_sign_in);
        updateFavoritesAdapter("");
//        mFavoritesAdapter = new FavoritesAdapter("");
//        mainFragment.setFavoritesAdapter(mFavoritesAdapter);
    }

    public FirebaseUser getUser() {
        return this.user;
    }
}
