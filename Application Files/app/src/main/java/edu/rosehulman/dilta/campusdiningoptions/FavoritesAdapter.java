package edu.rosehulman.dilta.campusdiningoptions;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brandsm on 2/5/2017.
 */
public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> implements Parcelable {

    private List<Food> mFavorites;
    private FavoritesFragment favoritesFrag;

    public FavoritesAdapter() {
        this.mFavorites = new ArrayList<Food>();
    }

    protected FavoritesAdapter(Parcel in) {
    }

    public void setFragment(Fragment f) {
        this.favoritesFrag = (FavoritesFragment) f;
    }

    public static final Creator<FavoritesAdapter> CREATOR = new Creator<FavoritesAdapter>() {
        @Override
        public FavoritesAdapter createFromParcel(Parcel in) {
            return new FavoritesAdapter(in);
        }

        @Override
        public FavoritesAdapter[] newArray(int size) {
            return new FavoritesAdapter[size];
        }
    };

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

    public void addFavorite(Food food) {
        this.mFavorites.add(0, food);
        notifyItemInserted(0);
    }

    public void removeFavorite(int position) {
        this.mFavorites.remove(position);
        notifyItemRemoved(position);
    }

    public void updateFavorite(Food food, String name) {
        food.setName(name);
        notifyDataSetChanged();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageButton removeButton;

        public ViewHolder(View itemView) {
            super(itemView);

            this.name = (TextView) itemView.findViewById(R.id.favorite_text);
            this.removeButton = (ImageButton) itemView.findViewById(R.id.favorite_delete_button);

            this.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeFavorite(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    favoritesFrag.editFavorite(mFavorites.get(getAdapterPosition()));
                    return false;
                }
            });
        }
    }
}
