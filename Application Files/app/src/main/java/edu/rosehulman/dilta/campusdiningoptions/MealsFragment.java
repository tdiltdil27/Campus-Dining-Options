package edu.rosehulman.dilta.campusdiningoptions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

/**
 * Created by dilta on 1/22/2017.
 */
public class MealsFragment extends Fragment {

    private static final String ARG_ADAPTER = "adapter";
    private MealTimeAdapter mAdapter;

    public MealsFragment() {


    }

    public static MealsFragment newInstance(MealTimeAdapter adapter) {
        MealsFragment fragment = new MealsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ADAPTER, adapter);
        fragment.setArguments(args);
        return fragment;
    }
}
