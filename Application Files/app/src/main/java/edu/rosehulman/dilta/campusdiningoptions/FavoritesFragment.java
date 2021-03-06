package edu.rosehulman.dilta.campusdiningoptions;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {
    private static final String FAVORITES_ADAPTER = "adapter";

    private FavoritesAdapter mAdapter;
    private TextView help_message;
    private boolean show_help = true;
    private String uid;
    private boolean logged_in;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FavoritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesFragment newInstance(FavoritesAdapter adapter) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(FAVORITES_ADAPTER, adapter);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.mAdapter = args.getParcelable(FAVORITES_ADAPTER);
            if (this.mAdapter.getItemCount() > 0) {
                this.show_help = false;
            }
            //this.mAdapter.reset();
        } else {
            this.mAdapter = new FavoritesAdapter("");
        }
        this.mAdapter.setFragment(this);

        MainActivity activity = (MainActivity) getActivity();
        FirebaseUser logged_in_user = activity.getUser();
        this.logged_in = false;
        if (logged_in_user != null) {
            this.logged_in = true;
            this.uid = activity.getUser().getUid();
            Log.d("FavoritesFrag", "logged in: " + uid);
            this.mAdapter.setLoggedIn(true);
        }

        //this.mAdapter.setQuery(uid);

        activity.setTitle(getResources().getString(R.string.favorites_title));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(this.mAdapter);

        help_message = (TextView) rootView.findViewById(R.id.favorites_help_message);
        if (!this.show_help) {
            help_message.setVisibility(View.GONE);
        }

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_favorites, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_favorite) {
            editFavorite(null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void editFavorite(final Favorite fav) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.edit_favorite, null);
        dialog.setView(view);

        final EditText food_name = (EditText) view.findViewById(R.id.food_name_edittext);
        if (fav != null) {
            food_name.setText(fav.getFood().getName());
        }

        dialog.setNegativeButton(android.R.string.cancel, null);
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = food_name.getText().toString();
                if (fav != null) {
                    mAdapter.updateFavorite(fav, name);
                } else {
                    Food food = new Food();
                    food.setName(name);
                    Favorite new_favorite = new Favorite();
                    new_favorite.setFood(food);
                    new_favorite.setUid(uid);
                    mAdapter.addFavorite(new_favorite);
                }

                help_message.setVisibility(View.GONE);
            }
        });

        dialog.create().show();
    }
}
