/**
 * PlaceViewAdapter is a RecyclerView adapter for displaying a list of places.
 * It binds place data to the corresponding views and handles item click events.
 *
 * @author Lee Teck Junn
 * @version 1.0
 */
package my.edu.utar.groupassignment.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import my.edu.utar.groupassignment.DetailActivity;
import my.edu.utar.groupassignment.R;
import my.edu.utar.groupassignment.structure.Place;

import java.io.Serializable;
import java.util.ArrayList;

public class PlaceViewAdapter extends RecyclerView.Adapter<PlaceViewAdapter.ViewHolder> {

    // ArrayList to store a list of Place objects
    ArrayList<Place> placeArrayList;

    // Constructor for PlaceViewAdapter
    public PlaceViewAdapter(ArrayList<Place> placeArrayList){
        this.placeArrayList = placeArrayList;
    }

    @Override
    public PlaceViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for a single item view and create a new ViewHolder
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(PlaceViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Bind data from the Place object to the corresponding views in the ViewHolder
        holder.text_name.setText(placeArrayList.get(position).getName());
        holder.text_rating.setText(placeArrayList.get(position).getRating());

        // Load an image using Glide library and display it in the ImageView
        Glide.with(holder.itemView.getContext())
                .load(placeArrayList.get(position).getPhoto())
                .into(holder.photo);

        // Set an item click listener to open the DetailActivity when an item is clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to open the DetailActivity and pass the selected Place object
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("object", placeArrayList.get(position));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the list
        return placeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        // Views within the ViewHolder
        TextView text_name, text_rating;
        ImageView photo;
        public ViewHolder(View itemView) {
            super(itemView);

            // Initialize views by finding their respective IDs
            text_name = itemView.findViewById(R.id.detail_name);
            text_rating = itemView.findViewById(R.id.place_rating);
            photo = itemView.findViewById(R.id.place_photo);
        }
    }
}
