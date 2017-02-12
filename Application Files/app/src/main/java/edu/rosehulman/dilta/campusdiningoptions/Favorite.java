package edu.rosehulman.dilta.campusdiningoptions;

import com.google.firebase.database.Exclude;

/**
 * Created by brandsm on 2/12/2017.
 */

public class Favorite {
    private String uid;
    private Food food;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;
}
