package edu.rosehulman.dilta.campusdiningoptions;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dilta on 1/22/2017.
 */
public class MealsFragment extends Fragment {

    private static final String ARG_ADAPTER = "adapter";
    private MealTimeAdapter mAdapter;

    public MealsFragment() {


    }

    public static MealsFragment newInstance() {
        MealsFragment fragment = new MealsFragment();
//        Bundle args = new Bundle();
//        args.putParcelable(ARG_ADAPTER, adapter);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //TODO: what layout shuld this use?
        View rootView = inflater.inflate(R.layout.tab_content, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        mAdapter = new MealTimeAdapter((MainActivity) getContext(), recyclerView);
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

    public MealTimeAdapter getAdapter() {
        return mAdapter;
    }

}
