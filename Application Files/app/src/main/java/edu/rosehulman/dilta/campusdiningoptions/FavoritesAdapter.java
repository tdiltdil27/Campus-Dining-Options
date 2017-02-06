package edu.rosehulman.dilta.campusdiningoptions;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brandsm on 2/5/2017.
 */
public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    private List<Food> mFavorites;

    public FavoritesAdapter() {
        this.mFavorites = new ArrayList<Food>();
    }

    @Override
    public FavoritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite, parent, false);
        return new FavoritesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavoritesAdapter.ViewHolder holder, int position) {
        Food food = mFavorites.get(position);
        String name = food.getName();
        holder.name.setText(name);
    }

    @Override
    public int getItemCount() {
        return this.mFavorites.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        public ViewHolder(View itemView) {
            super(itemView);

            this.name = (TextView) itemView.findViewById(R.id.favorite_text);
        }
    }
}
