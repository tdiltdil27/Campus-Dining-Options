package edu.rosehulman.dilta.campusdiningoptions;

import java.util.ArrayList;

/**
 * Created by dilta on 1/15/2017.
 */

public class MealTime {

    private String mName;
    private int[] mTime;
    private ArrayList<Food> mFoods;

    public MealTime() {

    }

    public String getFoods() {
        String foodString = null;
        for(int i = 0 ; i < mFoods.size(); i++) {
            foodString = foodString + mFoods.get(i) + "\n";
        }
        return foodString;
    }

    public int[] getTime() {
        return mTime;
    }

    public String getName() {
        return mName;
    }

    public void setFoods(ArrayList<Food> mFoods) {
        this.mFoods = mFoods;
    }

    public void setTime(int[] mTime) {
        this.mTime = mTime;
    }
}
