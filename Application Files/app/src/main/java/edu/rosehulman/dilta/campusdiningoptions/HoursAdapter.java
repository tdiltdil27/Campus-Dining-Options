package edu.rosehulman.dilta.campusdiningoptions;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dilta on 1/15/2017.
 */

public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.ViewHolder> implements Parcelable{

    private ArrayList<Location> mLocations;
    private Context mContext;
    private RecyclerView mView;

    public HoursAdapter(Context context, RecyclerView view) {

        mContext = context;
        mLocations = (ArrayList) SampleUtil.loadLocationsFromJsonArray(mContext);
        mView = view;

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
}
