package edu.rosehulman.dilta.campusdiningoptions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dilta on 1/22/2017.
 */
public class HoursFragment extends Fragment {

    private HoursAdapter mAdapter;

    public static HoursFragment newInstance() {
        HoursFragment fragment = new HoursFragment();
//        Bundle args = new Bundle();
//        args.putParcelable(ARG_ADAPTER, adapter);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.content_main, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        mAdapter = new HoursAdapter((MainActivity) getContext(), recyclerView);
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

    public HoursAdapter getAdapter() {
        return mAdapter;
    }

}
