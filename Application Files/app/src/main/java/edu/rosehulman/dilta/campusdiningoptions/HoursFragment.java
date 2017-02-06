package edu.rosehulman.dilta.campusdiningoptions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dilta on 1/22/2017.
 */
public class HoursFragment extends Fragment {

    private static final String ARG_ADAPTER = "adapter";

    private HoursAdapter mAdapter;

    public static HoursFragment newInstance() {
        HoursFragment fragment = new HoursFragment();
//        Bundle args = new Bundle();
//        args.putParcelable(ARG_ADAPTER, adapter);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("HoursFragment", "onCreateHours");
        super.onCreate(savedInstanceState);
        mAdapter = new HoursAdapter();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d("HoursFragment", "onCreateViewHours");
        View rootView = inflater.inflate(R.layout.tab_content, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

    public HoursAdapter getAdapter() {
        return mAdapter;
    }

}
