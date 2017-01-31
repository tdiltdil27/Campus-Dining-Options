package edu.rosehulman.dilta.campusdiningoptions;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dilta on 1/15/2017.
 */

public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.ViewHolder> implements Parcelable{

    private List<Location> mLocations;
    private Context mContext;
    private RecyclerView mView;
    private final static String ARG_URL = "https://campus-meal-scraper.herokuapp.com/locations/%d-%s-%s/";


    public HoursAdapter(MainActivity context, RecyclerView view) {

        mContext = context;
        mLocations = new ArrayList<Location>();
        mView = view;
        new getLocationsTask().execute(String.format(ARG_URL, context.getYear(), context.getMonth()<10?"0"+context.getMonth():context.getMonth(), context.getDay()<10?"0"+context.getDay():context.getDay()));


    }

    protected HoursAdapter(Parcel in) {
    }

    public static final Creator<MealTimeAdapter> CREATOR = new Creator<MealTimeAdapter>() {
        @Override
        public MealTimeAdapter createFromParcel(Parcel in) {
            return new MealTimeAdapter(in);
        }

        @Override
        public MealTimeAdapter[] newArray(int size) {
            return new MealTimeAdapter[size];
        }
    };

    @Override
    public HoursAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_hours, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HoursAdapter.ViewHolder holder, int position) {
        Location name = mLocations.get(position);
        ArrayList<MealTime> mealTimes = name.getMealTimes();

        String meals = "";

        for(int i = 0; i < mealTimes.size(); i++) {
            meals = meals + mealTimes.get(i).getName() + ": " + mealTimes.get(i).getHours() + "\n";
        }

        holder.mNameView.setText(name.getName());

        holder.mTimeView.setText(meals);

    }

    @Override
    public int getItemCount() {
        return mLocations.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameView;
        private TextView mTimeView;

        public ViewHolder(View itemView) {
            super(itemView);

            mNameView = (TextView) itemView.findViewById(R.id.name_view);
            mTimeView = (TextView) itemView.findViewById(R.id.hours_view);
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
                locations = new ObjectMapper().readValue(new URL(urlString), List.class);
            } catch (IOException e) {

            }
            return locations;
        }
        @Override
        protected void onPostExecute(List<Location> locations) {
            super.onPostExecute(locations);
            mLocations = locations;
        }
    }
}
