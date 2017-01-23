package edu.rosehulman.dilta.campusdiningoptions;

import java.util.ArrayList;

/**
 * Created by dilta on 1/22/2017.
 */
public class Location {

    private ArrayList<MealTime> MealTimes;
    private String name;

    public Location() {

    }

    public ArrayList<MealTime> getMealTimes() {
        return MealTimes;
    }

    public void setMealTimes(ArrayList<MealTime> mealTimes) {
        this.MealTimes = mealTimes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
