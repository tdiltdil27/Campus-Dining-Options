package edu.rosehulman.dilta.campusdiningoptions;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dilta on 1/15/2017.
 */

public class MealTimeAdapter extends RecyclerView.Adapter<MealTimeAdapter.ViewHolder> implements Parcelable {

    private static final String ARG_UNION = "Union Cafe";
    private List<MealTime> mMealTimes;
    private List<Favorite> mFavorites;

    public MealTimeAdapter() {
//        mealTimes = (ArrayList) SampleUtil.loadMealTimesFromJsonArray(mContext);
        Log.d("MealsAdapter", "Created meals adapter");
        mMealTimes = new ArrayList<MealTime>();
        mFavorites = null;
    }

    protected MealTimeAdapter(Parcel in) {
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

    public void setData(List mealTimes, List<Favorite> favorites) {
        mMealTimes = mealTimes;
        mFavorites = favorites;
    }

    @Override
    public MealTimeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_time, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MealTimeAdapter.ViewHolder holder, int position) {
        MealTime name = mMealTimes.get(position);

        holder.mNameView.setText(name.getName());
        holder.mTimeView.setText(name.getHours());

        List<Food> foods = name.getFoods();
        List<Food> favorite_foods = new ArrayList<Food>();
        for (Favorite fav : mFavorites) {
            favorite_foods.add(fav.getFood());
        }

        String foodString = "";

        for (Food food : foods) {
            boolean favorited = false;
            for (Food favorite_food : favorite_foods) {
                if (food.getName().contains(favorite_food.getName())) {
                    foodString = foodString + food.getName() + " ★ " + "\n";
                    favorited = true;
                }
            }

            if (!favorited) {
                foodString = foodString + food.getName() + "\n";
            }
        }

        holder.mFoodView.setText(foodString);
    }

    @Override
    public int getItemCount() {
        return mMealTimes.size();
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
        private TextView mFoodView;

        public ViewHolder(View itemView) {
            super(itemView);

            mNameView = (TextView) itemView.findViewById(R.id.name_view);
            mTimeView = (TextView) itemView.findViewById(R.id.time_view);
            mFoodView = (TextView) itemView.findViewById(R.id.foods_view);
        }
    }

}