package edu.rosehulman.dilta.campusdiningoptions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;

/**
 * Created by dilta on 1/15/2017.
 */

public class MealTime {


    private String name;
    private int[] mTime = {9,10};
    private String hours;
    private ArrayList<Food> items;

    public MealTime() {

    }

    public String getFoods() {
        String foodString = null;
        for(int i = 0 ; i < items.size(); i++) {
            foodString = foodString + items.get(i) + "\n";
        }
        return foodString;
    }

    public int[] getTime() {
        return mTime;
    }

    public String getName() {
        return name;
    }

    public void setFood(ArrayList<Food> items) {
        this.items = items;
    }

    public void setTime(int[] mTime) {
        this.mTime = mTime;
    }
}
