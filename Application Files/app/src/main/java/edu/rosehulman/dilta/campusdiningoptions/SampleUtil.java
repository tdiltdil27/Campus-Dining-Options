package edu.rosehulman.dilta.campusdiningoptions;

import android.content.Context;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dilta on 1/15/2017.
 */

public class SampleUtil {

    private final static String ARG_UNION = "Union";

    public static List<MealTime> loadMealTimesFromJsonArray(Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.data);
        List<Location> words = null;
        try {
            words = new ObjectMapper().readValue(is, new TypeReference<List<Location>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<MealTime> mealTimes = null;

        for(int i = 0; i < words.size(); i++) {
            if(words.get(i).getName().equals(ARG_UNION)) {
                mealTimes = words.get(i).getMealTimes();
            }
        }
        return mealTimes;
    }

    public static List<Location> loadLocationsFromJsonArray(Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.data);
        List<Location> words = null;
        try {
            words = new ObjectMapper().readValue(is, new TypeReference<List<Location>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }
}
