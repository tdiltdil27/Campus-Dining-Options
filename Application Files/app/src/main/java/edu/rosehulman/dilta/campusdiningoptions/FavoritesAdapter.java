package edu.rosehulman.dilta.campusdiningoptions;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brandsm on 2/5/2017.
 */
public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> implements Parcelable {

    private List<Favorite> mFavorites;
    private FavoritesFragment favoritesFrag;
    private DatabaseReference mRef;
    private boolean loggedIn;

    public FavoritesAdapter() {
        this.mFavorites = new ArrayList<Favorite>();
        this.mRef = FirebaseDatabase.getInstance().getReference().child("favorites");
        this.mRef.addChildEventListener(new FavoritesChildEventListener());
        this.loggedIn = false;
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
        Food food = mFavorites.get(position).getFood();
        String name = food.getName();
        holder.name.setText(name);
    }

    @Override
    public int getItemCount() {
        return this.mFavorites.size();
    }

    public void addFavorite(Favorite fav) {
        Log.d("FavoritesAdapter", "adding fav, user logged in: " + loggedIn);
        if (loggedIn) {
            mRef.push().setValue(fav);
        } else {
            this.mFavorites.add(0, fav);
            notifyItemInserted(0);
        }
    }

    public void removeFavorite(Favorite fav) {
        Log.d("FavoritesAdapter", "removing fav, user logged in: " + loggedIn);
        if (loggedIn) {
            mRef.child(fav.getKey()).removeValue();
        } else {
            int pos = this.mFavorites.indexOf(fav);
            this.mFavorites.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    public void updateFavorite(Favorite fav, String name) {
        Log.d("FavoritesAdapter", "updating fav, user logged in: " + loggedIn);
        fav.getFood().setName(name);

        if (loggedIn) {
            mRef.child(fav.getKey()).setValue(fav);
        } else {
            notifyDataSetChanged();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
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
                    removeFavorite(mFavorites.get(getAdapterPosition()));
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

    private class FavoritesChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d("FavAdapter", "favorites child added");
            Favorite fav = dataSnapshot.getValue(Favorite.class);
            fav.setKey(dataSnapshot.getKey());
            mFavorites.add(0, fav);
            notifyItemInserted(0);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Log.d("FavAdapter", "favorites child changed");
            Favorite updated = dataSnapshot.getValue(Favorite.class);
            String key = dataSnapshot.getKey();
            for (Favorite f : mFavorites){
                if (f.getKey().equals(key)) {
                    f.setFood(updated.getFood());
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Log.d("FavAdapter", "favorites child removed");
            String key = dataSnapshot.getKey();
            for (Favorite f : mFavorites){
                if (f.getKey().equals(key)) {
                    int pos = mFavorites.indexOf(f);
                    mFavorites.remove(pos);
                    notifyItemRemoved(pos);
                    return;
                }
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e("FavoritesAdapter", "Database error: " + databaseError);
        }
    }
}