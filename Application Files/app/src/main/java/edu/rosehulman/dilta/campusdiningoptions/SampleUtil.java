package edu.rosehulman.dilta.campusdiningoptions;

import android.content.Context;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by dilta on 1/15/2017.
 */

public class SampleUtil {

    public static List<MealTime> loadWordsFromJsonArray(Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.data.json);
        List<MealTime> words = null;
        try {
            words = new ObjectMapper().readValue(is, new TypeReference<List<MealTime>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

}
