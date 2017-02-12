package edu.rosehulman.dilta.campusdiningoptions;

import android.app.DatePickerDialog;
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

    private DatabaseReference mFirebase;
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

        mFavoritesAdapter = new FavoritesAdapter();

        setDate();
        updateTitle();

        loggedIn = false;

        mainFragment = MainFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, mainFragment);
        ft.commit();

        mAuth = FirebaseAuth.getInstance();

        initializeListeners();

        mFirebase = FirebaseDatabase.getInstance().getReference().child("title");

        mListener = mFirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setTitle((String)dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(Constants.TAG,"Database error");
//                mAuth.getInstance().signOut();
//                switchToLoginFragment();
            }
        });
        if(mFirebase == null) {
            setTitle(getString(R.string.app_name));
        }
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
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d", Locale.US);
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(mYear, mMonth-1, mDay);
        focusedDate = sdf.format(cal.getTime());

        if(focusedDate.equals(currentDate)) {
            focusedDate = "Today, " + focusedDate;
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

    public boolean loggedIn() {
        return loggedIn;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fm.popBackStackImmediate();
        ft.commit();
        Snackbar.make(mainFragment.getView(), "Log In cancelled", Snackbar.LENGTH_LONG).show();
        getSupportActionBar().show();
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
    }
    private void switchToLoginFragment() {
        getSupportActionBar().hide();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, new LoginFragment(), "Login");
        ft.commit();
    }
    private void switchToMainFragment(String path, String user, String uid) {
        Log.d(Constants.TAG, "starting main fragment");
        getSupportActionBar().show();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mainFragment = MainFragment.newInstance();

        Bundle args = new Bundle();
        args.putString(Constants.FIREBASE_PATH, path);
        args.putString(Constants.FIREBASE_USER, uid);
        args.putString(Constants.FIREBASE_NAME, user);
        mainFragment.setArguments(args);

        ft.replace(R.id.content_main, mainFragment, "Main");
        ft.commit();

    }

    public void logOut() {
        mAuth.getInstance().signOut();
        loggedIn = false;
        mainFragment.mMenu.findItem(R.id.login).setTitle(R.string.action_sign_in);
    }
}
