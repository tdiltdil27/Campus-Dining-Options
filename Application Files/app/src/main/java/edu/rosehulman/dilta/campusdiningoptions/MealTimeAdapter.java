package edu.rosehulman.dilta.campusdiningoptions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dilta on 1/15/2017.
 */

public class MealTimeAdapter extends RecyclerView.Adapter<MealTimeAdapter.ViewHolder>{

    private ArrayList<MealTime> mealTimes;
    private Context mContext;
    private RecyclerView mView;

    public MealTimeAdapter(Context context, RecyclerView view) {
        mealTimes = null;
        mContext = context;
        mView = view;

    }

    @Override
    public MealTimeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_time, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MealTimeAdapter.ViewHolder holder, int position) {
        MealTime name = mealTimes.get(position);
        holder.mNameView.setText(name.getName());

        switch (name.getName()) {
            case "Breakfast":

                holder.mTimeView.setText(String.format(mContext.getResources().getString(R.string.time_layout_breakfast), name.getTime()[0], name.getTime()[1]));
                break;
            case "Lunch":

                holder.mTimeView.setText(String.format(mContext.getResources().getString(R.string.time_layout_lunch), name.getTime()[0], name.getTime()[1]));
                break;
            case "Brunch":

                holder.mTimeView.setText(String.format(mContext.getResources().getString(R.string.time_layout_brunch), name.getTime()[0], name.getTime()[1]));
                break;
            case "Dinner":

                holder.mTimeView.setText(String.format(mContext.getResources().getString(R.string.time_layout_dinner), name.getTime()[0], name.getTime()[1]));
                break;
            case "Grab N' Go":

                holder.mTimeView.setText(String.format(mContext.getResources().getString(R.string.time_layout_grabngo), name.getTime()[0], name.getTime()[1]));
                break;
        }
        holder.mFoodView.setText(name.getFoods());
    }

    @Override
    public int getItemCount() {
        return mealTimes.size();
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
