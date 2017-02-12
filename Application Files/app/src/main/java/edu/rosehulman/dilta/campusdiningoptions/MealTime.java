package edu.rosehulman.dilta.campusdiningoptions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dilta on 1/15/2017.
 */

public class MealTime {


    private String name;
    private String hours;
    private List<Food> items;

    public MealTime() {

    }

    public List<Food> getFoods() {
        return items;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHours() {
        return hours;
    }

    public List<Food> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public void setItems(ArrayList<Food> items) {
        this.items = items;
    }

}
